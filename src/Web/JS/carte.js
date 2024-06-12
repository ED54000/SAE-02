
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


var VelibIconBlue = L.icon({
    iconUrl : "img/bike-blue.svg",
    iconSize: [38,95],
    inconAnchor: [22,94]
})



var map = L.map('map').setView([48.683331, 6.2], 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

function fetchData(){
    //getVelib();
    getResto();


}

function getResto(){
    fetch('http://localhost:8000/restaurants')
        .then(response => response.json())
        .then(data => {
            console.log(data.data)
            for (let resto of data.data) {
                var marker = L.marker([resto.latitude, resto.longitude])
                marker.bindPopup("<h1>"+resto.nom+"</h1><br><h2> "+resto.numero+" "+resto.adresse+"<br> Nombre de tables : "+ resto.nbTables + "</h2>").openPopup();
                marker.addTo(map)

            }
        })
        .catch(error => console.error('Error fetching data:', error));
}

function getVelib(){
    fetch('http://localhost:8000/infoVelibs')
        .then(response => response.json())
        .then(async data => {
            for (let velib of data.data.stations) {
                let dispo = await getdispo(velib.station_id);
                let ratio = (dispo[0]/(dispo[0]+dispo[1]))*100
                var marker = L.marker([velib.lat, velib.lon])
                if (ratio === 100) {
                    marker = L.marker([velib.lat, velib.lon], {icon: VelibIconGreen})
                } else if (100 > ratio && ratio > 75) {
                    marker = L.marker([velib.lat, velib.lon], {icon: VelibIconYellow})
                } else if (75 >= ratio && ratio > 50) {
                    marker = L.marker([velib.lat, velib.lon], {icon: VelibIconBlue})
                } else if (50 >= ratio && ratio > 25) {
                    marker = L.marker([velib.lat, velib.lon], {icon: VelibIconOrange})
                } else if (25 >= ratio && ratio > 0) {
                    marker = L.marker([velib.lat, velib.lon], {icon: VelibIconRed})
                } else if (ratio === 0) {
                    marker = L.marker([velib.lat, velib.lon], {icon: VelibIconBlack})
                }
                marker.bindPopup("<h1>"+velib.address+"</h1><br><h2> Vélos diponibles : "+dispo[0].toString()+"<br> Docks disponibles: "+ dispo[1].toString()+"</h2>").openPopup();
                marker.addTo(map)
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
            return [dataVelib.num_bikes_available,dataVelib.num_docks_available]
        })
}