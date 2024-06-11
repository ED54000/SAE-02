


var map = L.map('map').setView([48.683331, 6.2], 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

function fetchData() {
    fetch('http://localhost:8000/infoVelibs')
        .then(response => response.json())
        .then(data => {
            data.data.stations.forEach(
                (velib) =>  L.marker([velib.lat, velib.lon]).addTo(map)
            )
        })
        .catch(error => console.error('Error fetching data:', error));
}

fetchData()