


var map = L.map('map').setView([48.683331, 6.2], 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

var marker = L.marker([48.683331, 6.2]).addTo(map);

var circle = L.circle([48.683331, 6.2], {
    color: 'red',
    fillColor: '#f03',
    fillOpacity: 0.5,
    radius: 500
}).addTo(map);

var polygon = L.polygon([
    [48.6833, 6.21],
    [48.6830, 6.22],
    [48.6832, 6.23]
]).addTo(map);


fetch('localhost:8000').then(console.log("yes"))

