
var VelibIconGreen = L.icon({
    iconUrl : "img/bike-green.svg",
    iconSize: [38,95],
    inconAnchor: [22,94]
})

var VelibIcon = L.icon({
    iconUrl : "img/ike-green.svg",
    iconSize: [38,95],
    inconAnchor: [22,94]
})

var VelibIconBlack = L.icon({
    iconUrl : "img/bike-black.svg",
    iconSize: [38,95],
    inconAnchor: [22,94]
})

var VelibIconYellow = L.icon({
    iconUrl : "img/bike-yellow.svg",
    iconSize: [38,95],
    inconAnchor: [22,94]
})

var VelibIconOrange = L.icon({
    iconUrl : "img/bike-orange.svg",
    iconSize: [38,95],
    inconAnchor: [22,94]
})

var VelibIconRed = L.icon({
    iconUrl : "img/bike-red.svg",
    iconSize: [38,95],
    inconAnchor: [22,94]
})



var map = L.map('map').setView([48.683331, 6.2], 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

function fetchData() {
    fetch('http://localhost:8000/infoVelibs')
        .then(response => response.json())
        .then(async data => {
            for (let velib of data.data.stations) {
                let dispo = await getdispo(velib.station_id);
                console.log(dispo)
                if (dispo === 100) {
                    L.marker([velib.lat, velib.lon], {icon: VelibIconGreen}).addTo(map)
                } else if (100 < dispo && dispo > 75) {
                    L.marker([velib.lat, velib.lon], {icon: VelibIconYellow}).addTo(map)
                } else if (75 <= dispo && dispo > 50) {
                    L.marker([velib.lat, velib.lon], {icon: VelibIconOrange}).addTo(map)
                } else if (50 <= dispo && dispo > 25) {
                    L.marker([velib.lat, velib.lon], {icon: VelibIconRed}).addTo(map)
                } else if (25 <= dispo && dispo > 0) {
                    L.marker([velib.lat, velib.lon], {icon: VelibIconRed}).addTo(map)
                } else if (dispo === 0) {
                    L.marker([velib.lat, velib.lon], {icon: VelibIconBlack}).addTo(map)
                }


            }
        })
        .catch(error => console.error('Error fetching data:', error));
}

fetchData()

function getdispo(id){
    return fetch('http://localhost:8000/statusVelibs')
        .then(response => response.json())
        .then(data => {
            for (let velibs of data.data.stations){
                if (velibs.station_id == id){
                    var dataVelib = velibs
                }
            }
            return (dataVelib.num_bikes_available/(dataVelib.num_bikes_available+(dataVelib.num_docks_available))*100)
        })
}