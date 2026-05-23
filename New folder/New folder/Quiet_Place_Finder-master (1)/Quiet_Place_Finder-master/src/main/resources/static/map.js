let map;
let markers = [];

function initMap() {
    // Initialize map centered on Cairo
    map = L.map('map').setView([30.0444, 31.2357], 13);

    // Add tiles
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);
}

function searchPlaces(lat, lng, radius, noiseLevel, purpose) {
    fetch(`/api/places/search?lat=${lat}&lng=${lng}&radius=${radius}&noiseLevel=${noiseLevel}&purpose=${purpose}`)
        .then(response => response.json())
        .then(places => {
            clearMarkers();
            places.forEach(place => {
                addMarker(place);
            });
        })
        .catch(error => console.error('Error:', error));
}

function addMarker(place) {
    const marker = L.marker([place.latitude, place.longitude]).addTo(map);
    marker.bindPopup(`<b>${place.name}</b><br>${place.address}`);
    markers.push(marker);
}

function clearMarkers() {
    markers.forEach(marker => map.removeLayer(marker));
    markers = [];
}

// Call this when page loads
document.addEventListener('DOMContentLoaded', initMap);