
function getLocation() {
    navigator.geolocation.getCurrentPosition(successCallback);
    // TODO 1: Get the user location in one line(use the "one-time location" function)
    // Pass the success and error callback to the method
    // Position option is not required here
    // The success and error callback have been already defined and are
    // respectively called "successCallback" and "errorCallback"
    
}

function successCallback(pos) {
    // TODO 2: The successCallback is provided a Position object: pos
    // Using the Google Maps API, we want to get a coordinate
    // The google.maps.LatLng function expects a latitude and a longitude as parameters
    var myLatlng = new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
    
    var myOptions = {
        zoom: 12,
        center: myLatlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    
    var map = new google.maps.Map(document.getElementById("map_canvas"),myOptions);
    
    
    var marker = new google.maps.Marker({
        position: myLatlng, 
        map: map, 
        animation: google.maps.Animation.DROP,

        title:"Your phone is here!" + pos.coords.accuracy + " meters"
    });
    google.maps.event.addListener(marker, 'click', toggleBounce);
}

function toggleBounce() {

  if (marker.getAnimation() != null) {
    marker.setAnimation(null);
  } else {
    marker.setAnimation(google.maps.Animation.BOUNCE);
  }
}

function errorCallback (err) {
    switch(err.code) {
        case 1:
            alert("Sorry, but this application does not have the permission to use geolocation");
            break;
        case 2:
            alert("Sorry, but a problem happened while getting your location");
            break;
        case 3:
            alert("Sorry, this is taking too long...");
            break;
        default:
            alert("unknown");
    }
}

// When the window has finished to load, call the getLocation function
window.addEventListener("load", getLocation, true);