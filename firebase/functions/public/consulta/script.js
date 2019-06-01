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
        categories: ['Muy mal','Mal','Regular','Bien','Muy bien']
      },
      y: {
        label: { text: 'Nº de comentarios', position: 'outer-top' }
      }
    },
    grid: { y: { lines: [{ value: 0 }] } },
  })
}

function generateTimeChart(comments) {
  var timeKeys = Object.keys(comments['time']);
  timeKeys[0] = "Dates";
  var timeVals = Object.values(comments['time']);
  timeVals[0] = "Fechas";
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
        label: { text: 'Fechas', position: 'outer-center' },
        type: 'timeseries',
        tick: {
          count: 4,
          format: '%d-%m-%Y',
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

function processComents(snapshot) {
  var mark = 0;
  var timestamp;
  var marks = {};
  var timestamps = {};
  snapshot.forEach(element => {
    element = element.val();
    mark = Math.round(element.rate);
    !marks[mark] ? marks[mark] = 1 : marks[mark]++;
    timestamp = element.timestamp.day + "-" + (element.timestamp.month + 1) + "-" + (element.timestamp.year + 1900) + " " + element.timestamp.hours;
    !timestamps[timestamp] ? timestamps[timestamp] = 1 : timestamps[timestamp]++;
  });
  return { 'marks': marks, 'time': timestamps };
}
