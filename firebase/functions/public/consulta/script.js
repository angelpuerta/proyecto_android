firebase.initializeApp(config);
const database = firebase.database();
const storage = firebase.storage();
var time, latLng;
var map, marker, autocomplete, location;
var lat = 0;
var lng = 0;

$(document).ready(function () {
  getEvent()
    .then((event) => {
      displayEvent(event);
      getComments()
        .then((comments) => {
          generateRateChart(comments);
          generateTimeChart(comments);
        });
      getAssitance().then((assistance) => {
        generateAssistanceChart(assistance);
        generateGenderChart(assistance['users']);
      });
    })
});

function displayEvent(event) {
  $("#tittle").text(event.tittle.toUpperCase());
  $("#description").text(event.description);
}

function generateRateChart(comments) {
  var marksChart = c3.generate({
    bindto: '#puntuacionChart',
    title: {
      text: 'Puntuaciones'
    },
    padding: {
      top: 20,
      right: 20,
      bottom: 20,
    },
    data: {
      json: comments['marks'],
      type: 'bar',
      labels: true
    },
    bar: { space: 0.25 },
    axis: {
      x: {
        label: { text: 'Puntuaciones', position: 'outer-center' },
        type: 'category',
      },
      y: {
        label: { text: 'Nº de comentarios', position: 'outer-top' }
      }
    },
    grid: { y: { lines: [{ value: 0 }] } },
  })
}

function generateGenderChart(users) {
  var genders = {};
  for (user in users) {
    !genders[users[user].sexo] ? genders[users[user].sexo] = 1 : genders[users[user].sexo]++;
  }
  var genderChart = c3.generate({
    bindto: "#genderChart",
    title: {
      text: "Género de los asistentes"
    },
    padding: {
      top: 20,
      right: 20,
      bottom: 20,
    },
    data: {
      json: genders,
      type: 'bar',
      labels: true
    },
    bar: { space: 0.25 },
    axis: {
      x: {
        label: { text: 'Géneros', position: 'outer-center' },
        type: 'category'
      },
      y: {
        label: { text: 'Nº de asistentes', position: 'outer-top' }
      }
    },
  });
}

function generateTimeChart(comments) {
  var timeKeys = Object.keys(comments['time']);
  timeKeys.unshift("Dates");
  var timeVals = Object.values(comments['time']);
  timeVals.unshift("Fechas");
  var timeChart = c3.generate({
    bindto: '#fechasChart',
    title: {
      text: 'Evolución del número de comentarios'
    },
    padding: {
      right: 50
    },
    data: {
      x: "Dates",
      xFormat: '%d-%m-%Y %H',
      columns: [timeKeys, timeVals],
      type: 'area'
    },
    legend: { show: false },
    axis: {
      x: {
        label: { text: 'Fechas', position: 'outer-center', format: '%d-%m-%Y %H'},
        type: 'timeseries',
        tick: {
          count: 4,
          format: '%d-%m-%Y %H:%M',
        },
      },
      y: {
        label: { text: 'Nº de comentarios', position: 'outer-top' }
      }
    }
  });
}

function generateAssistanceChart(assistance) {
  var timeKeys = Object.keys(assistance['assistance']);
  timeKeys.unshift("Dates");
  var timeVals = Object.values(assistance['assistance']);
  timeVals.unshift("Fechas");
  var assistanceChart = c3.generate({
    bindto: '#asistenciaChart',
    title: {
      text: "Evolución de la asistencia"
    },
    padding: {
      right: 50
    },
    data: {
      x: "Dates",
      xFormat: '%d-%m-%Y %H',
      columns: [timeKeys, timeVals],
      type: 'area'
    },
    legend: { show: false },
    axis: {
      x: {
        label: { text: 'Fechas', position: 'outer-center'},
        type: 'timeseries',
        tick: {
          count: 4,
          format: '%d-%m-%Y %H:%M',
        },
      },
      y: {
        label: { text: 'Nº de asistentes', position: 'outer-top' }
      }
    }
  });
}

function getComments() {
  return firebase.database().ref('/comments/' + eventId).orderByChild('mark').once('value').then((snapshot) => processComents(snapshot));
}

function getEvent() {
  return firebase.database().ref('/events/' + eventId).once('value').then((snapshot) => snapshot.val());
}

const categories = ['Muy mal', 'Mal', 'Regular', 'Bien', 'Muy bien'];


function processComents(snapshot) {
  var mark = 0;
  var timestamp;
  var marks = {};
  var timestamps = {};
  snapshot.forEach(element => {
    element = element.val();
    mark = Math.round(element.rate);
    !marks[categories[mark]] ? marks[categories[mark]] = 1 : marks[categories[mark]]++;
    timestamp = element.timestamp.day + "-" + (element.timestamp.month + 1) + "-" + (element.timestamp.year + 1900) + " " + element.timestamp.hours;
    !timestamps[timestamp] ? timestamps[timestamp] = 1 : timestamps[timestamp]++;
  });
  return { 'marks': marks, 'time': timestamps };
}

function getAssitance() {
  var users = {};
  var assitance = {};
  var promises = [];

  return firebase.database().ref('/assisted/' + eventId).once('value').then(snap => {
    snap.forEach(snap => {
      promises.push(firebase.database().ref('/usuarios/' + snap.key).once("value").then(user => {
        date = new Date(snap.val());
        timestamp = date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear() + " " + date.getHours()
        !assitance[timestamp] ? assitance[timestamp] = 1 : assitance[timestamp]++;
        users[user.key] = user.val();
      }))
    })
  }).then(() => {
    var chained = promises.reduce((promiseChain, currentOne) => { return currentOne.then(promiseChain) });
    return chained.then(() => { return { 'assistance': assitance, 'users': users } });
  });
}
