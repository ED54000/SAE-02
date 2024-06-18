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
var popresto = L.popup();

function addResto(e) {
    let coord = e.latlng.toString().split("(")[1].split(")")[0].split(",");
    coord[1] = coord[1].substring(1);
    console.log(coord)
    popresto
        .setLatLng(e.latlng)
        .setContent(`<form id='restoForm'> 
            <h3> Ajoutez un restaurant </h3>
            <input type='hidden' id='latitude' name='latitude' value='${coord[0]}'"><br>
            <input type='hidden' id='longitude' name='longitude' value='${coord[1]}'><br>
            <label for='nom'>Nom:</label><br>
            <input type='text' id='nom' name='nom' required><br> 
            <label for='numero'>Numéro de rue:</label><br>
            <input type='text' id='numero' name='numero' required><br>
            <label for='adresse'>Adresse:</label><br>
            <input type='text' id='adresse' name='adresse' required><br>
            <label for='nbPlaces'>Nombre de places:</label><br>
            <input type='number' id='nbPlaces' name='nbPlaces' min='0' required><br>
            <input type='submit' value='Soumettre' class='btnRestoAdd'>   
        </form>`)
        .openOn(map);

    document.getElementById('restoForm').addEventListener('submit', function(event) {
        event.preventDefault();
        submitRestoForm();
    });
}

map.on('click', addResto);
fetchData();

var markers = new L.LayerGroup().addTo(map);

function reinitialiser(){
    markers.clearLayers();
    fetchData();
}



var checkboxs = document.querySelectorAll('.legend-item input[type="checkbox"]');

checkboxs.forEach( checkbox => {
    checkbox.addEventListener('change', reinitialiser);
    }
)

function fetchData(){
    if(document.getElementById('Velibs').checked){
        getVelib();
    }

    if(document.getElementById('Resto').checked){
        getResto();
    }

    if(document.getElementById('Etude').checked){
        getEtude();
    }

    if(document.getElementById('Incident').checked){
        getIncident();
    }
}
function getResto() {
    fetch('http://localhost:8000/restaurants')
        .then(response => response.json())
        .then(data => {
            for (let resto of data.data) {
                var marker = L.marker([resto.latitude, resto.longitude], {icon: IconResto});
                marker.bindPopup(`
                    <h1>${resto.nom}</h1>
                    <h3>${resto.numero} ${resto.adresse}<br> Nombre de places : ${resto.nbPlaces}</h3>
                    <form id="guestForm">
                        
                        <input type="hidden" id="idrestaurant" name="idrestaurant" value="${resto.id}">   
                        <label for="nom">Nom:</label><br>
                        <input type="text" id="nom" name="nom" required><br>
                        <label for="prenom">Prénom:</label><br>
                        <input type="text" id="prenom" name="prenom" required><br>
                        <label for="nbconvives">Nombre de convives:</label><br>
                        <input type="number" id="nbconvives" name="nbconvives" min="0" required><br>
                        <label for="tel">Numéro de téléphone:</label><br>
                        <input type="tel" id="tel" name="tel" pattern="[0-9]{10}" required><br><br>
                        <input type="submit" value="Soumettre" class="btnRsv">
                    </form>
                `).openPopup();
                marker.addTo(markers);

                marker.on('popupopen', function () {
                    document.querySelector('.btnRsv').addEventListener('click', function (event) {
                        event.preventDefault();
                        submitForm();
                    });
                });
            }

        })
        .catch(error => console.error('Error fetching data:', error));

}

function submitForm() {
    const form = document.getElementById('guestForm');
    const formData = new FormData(form);
    const queryParams = new URLSearchParams();
    formData.forEach((value, key) => {
        queryParams.append(key, value);
    });
    const url = `http://localhost:8000/reserver?${queryParams.toString()}`;
    fetch(url, {
        method: 'GET'
    })
        .then(response => response.text())
        .then(data => {
            alert(data);
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function submitRestoForm() {
    const form = document.getElementById('restoForm');
    const formData = new FormData(form);
    const queryParams = new URLSearchParams();
    formData.forEach((value, key) => {
        queryParams.append(key, value);
    });
    const url = `http://localhost:8000/ajouterRestaurant?${queryParams.toString()}`;
    fetch(url, {
        method: 'GET'
    })
        .then(response => response.text())
        .then(data => {
            alert(data);
            window.location.reload();
        })
        .catch(error => {
            console.error('Erreur lors de l\'ajout du restaurant:', error);
        });
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
                marker.addTo(markers)
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
                marker.addTo(markers)
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
            marker.addTo(markers)
        }
    })
    .catch(error => console.error('Error fetching data:', error));
}


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
