firebase.initializeApp(config);
const database = firebase.database();
const storage = firebase.storage();
var time, latLng;
var map, marker, autocomplete, location;
var lat = undefined;
var lng = undefined;


$(document).ready(function () {
  $(document).on('submit', '#loader', function () {
    load();
    return false;
  });

  $("#generateCode").click(function () {
    $("#code").val(randomstring(8));
    return false;
  });

  $("#image").change(function () {
    previewImage(this);
  });

  $('#datetimepicker1').datetimepicker();

  $("#location").click(function () {
    $("#exampleModal").modal();
  });
});

function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    zoom: 15,
    center: { lat: 39.4, lng: -8.2 },
    mapTypeControl: false,
    panControl: false,
    zoomControl: false,
    streetViewControl: false
  });

  // Create the autocomplete object and associate it with the UI input control.
  // Restrict the search to the default country, and to place type "cities".
  autocomplete = new google.maps.places.Autocomplete(
      /** @type {!HTMLInputElement} */(
      document.getElementById('autocomplete')), {

    });
  places = new google.maps.places.PlacesService(map);

  google.maps.event.addListener(autocomplete, 'place_changed', onPlaceChanged);
}

function onPlaceChanged() {
  var place = autocomplete.getPlace();
  if (place.geometry) {
    map.panTo(place.geometry.location);
    map.setZoom(15);
  } else {
    document.getElementById('autocomplete').placeholder = 'Enter a city';
  }
}

function geocodePosition(pos) {
  geocoder = new google.maps.Geocoder();
  geocoder.geocode
    ({
      latLng: pos
    },
      function (results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
          lat = marker.position.lat()
          lng = marker.position.lng();
        }
      }
    );
}


function previewImage(input) {
  if (input.files && input.files[0]) {
    var reader = new FileReader();

    reader.onload = function (e) {
      $('#preview').attr('src', e.target.result);
      $('#preview').css('visibility', 'visible');
    }

    reader.readAsDataURL(input.files[0]);
  }
}


function getLocation() {
  var place = autocomplete.getPlace();
  if (!place.geometry)
    return { lat: undefined, lng: undefined };
  return { lat: Number(place.geometry.location.lat()), lng: Number(place.geometry.location.lng()) };
}

function getTime() {
  return $('#datetimepicker1').datetimepicker('viewDate').format("dd/MM/YYYY HH:mm");
}

function load() {
  var file = document.getElementById('image').files[0];
  time = getTime();
 // latLng = getLocation();
  var newEvent = {
    "id": getRandom(15),
    "tittle": $("#tittle").val().toLowerCase(),
    "description": $("#description").val(),
    "location": "" + lat + "," + lng,
    "date": time,
    "tags": $("#tags").val(),
    "code": $("#code").val(),
    "mark": 0,
    "numberOfComments": 0
  };

  console.log(newEvent)
  var storageRef = storage.ref('' + file.name)
  var task = storageRef.put(file)

  task.on('state_changed',
    function progress(snapshot) {
      var percentage = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
      console.log('File uploaded at' + percentage + ' %');
    },
    function error(err) {
      console.log(err);
    },
    function complete() {
      newEvent.imgURL = toURL(storageRef);
      database.ref('events').child(newEvent.id).set(newEvent).then(
        function (snapshot) {
          console.log('Update event');
          alert("Evento añadido");
          $("#loader")[0].reset();
          $("#preview").attr('src','');
        },
        function (error) {
          console.err(error);
          alert("Error en la conexión");
        }
      );
    });
}

function toURL(storageRef) {
  return 'https://firebasestorage.googleapis.com/v0/b/' + storageRef.bucket + '/o/'
    + encodeURIComponent(storageRef.name)
    + '?alt=media';
  //&token='
  //+ storageRef.metadata.firebaseStorageDownloadTokens';
}

function randomstring(L) {
  var s = '';
  var randomchar = function () {
    var n = Math.floor(Math.random() * 62);
    if (n < 10) return n; //1-10
    if (n < 36) return String.fromCharCode(n + 55); //A-Z
    return String.fromCharCode(n + 61); //a-z
  }
  while (s.length < L) s += randomchar();
  return s;
}

function getRandom(length) {

  return Math.floor(Math.pow(10, length - 1) + Math.random() * 9 * Math.pow(10, length - 1));

}
