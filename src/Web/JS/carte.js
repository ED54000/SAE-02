import {dataStatus, dataVelib} from "./api.js";


var VelibIconGreen = L.icon({
    iconUrl : "img/bike-green.svg",
    iconSize: [19,47],
    inconAnchor: [11,47]
})

var VelibIcon = L.icon({
    iconUrl : "img/ike-green.svg",
    iconSize: [19,47],
    inconAnchor: [11,47]
})

var VelibIconBlack = L.icon({
    iconUrl : "img/bike-black.svg",
    iconSize: [19,47],
    inconAnchor: [11,47]
})

var VelibIconYellow = L.icon({
    iconUrl : "img/bike-yellow.svg",
    iconSize: [19,47],
    inconAnchor: [11,47]
})

var VelibIconOrange = L.icon({
    iconUrl : "img/bike-orange.svg",
    iconSize: [19,47],
    inconAnchor: [11,47]
})

var VelibIconRed = L.icon({
    iconUrl : "img/bike-red.svg",
    iconSize: [19,47],
    inconAnchor: [11,47]
})


var VelibIconBlue = L.icon({
    iconUrl : "img/bike-blue.svg",
    iconSize: [19,47],
    inconAnchor: [11,47]
})

var IconResto = L.icon({
    iconUrl : "img/restaurant.svg",
    iconSize: [19,47],
    inconAnchor: [11,47]
})

var IconEtude = L.icon({
    iconUrl : "img/university.svg",
    iconSize: [19,47],
    inconAnchor: [11,47]
})

var IconIncident = L.icon({
    iconUrl : "img/worker.svg",
    iconSize: [25,47],
    inconAnchor: [17,47]
})

var map = L.map('map').setView([48.683331, 6.2], 13);

L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

function fetchData(){
    getVelib();
    getResto();
    getEtude();
    getIncident();


}

function getResto(){
    fetch('http://localhost:8000/restaurants')
        .then(response => response.json())
        .then(data => {
            for (let resto of data.data) {
                var marker = L.marker([resto.latitude, resto.longitude], {icon: IconResto})
                marker.bindPopup("<h1>"+resto.nom+"</h1><br><h2> "+resto.numero+" "+resto.adresse+"<br> Nombre de tables : "+ resto.nbTables + "</h2><button id='btnRsv'>Reserver</button>").openPopup();
                marker.addTo(map)

            }
        })
        .catch(error => console.error('Error fetching data:', error));
    let btnReserv = document.getElementById("btnRsv")
    btnReserv.addEventListener(function(){console.log("Reservation")});
}



function getIncident(){
    // Mise en forme de la date
    const options = {
        weekday: 'long',
        day: 'numeric',
        month: 'long',
        year: 'numeric',

    };
    fetch('http://localhost:8000/trafic')
        .then(response => response.json())
        .then(data => {
            for (let incident of data.incidents) {
                let coord = incident.location.polyline.split(" ")
                let endtime = new Date(incident.endtime)
                var marker = L.marker([coord[0], coord[1]], {icon: IconIncident});
                marker.bindPopup("<h1>"+incident.type+"</h1><br><h2> "+incident.description+"<br><br> Date de fin: "+ endtime.toLocaleDateString('fr-FR',options) + "</h2>").openPopup();
                marker.addTo(map)

            }
        })
        .catch(error => console.error('Error fetching data:', error));

}



function getEtude(){
    fetch('http://localhost:8000/etudeSup')
        .then(response => response.json())
        .then(data => {
            for (let etude of data.results) {
                var marker = L.marker([etude.coordonnees.lat, etude.coordonnees.lon],{icon: IconEtude})
                marker.bindPopup("<h1>"+etude.implantation_lib+"</h1><br><h2> "+etude.adresse_uai+"<br> Effectif : "+ etude.effectif + "</h2>").openPopup();
                marker.addTo(map)

            }
        })
        .catch(error => console.error('Error fetching data:', error));
}

function getVelib(){
    dataVelib().then(async data => {
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
    return dataStatus().then(data => {
            for (let velibs of data.data.stations){
                if (velibs.station_id === id){
                    var dataVelib = velibs
                }
            }
            return [dataVelib.num_bikes_available,dataVelib.num_docks_available]
        })
}