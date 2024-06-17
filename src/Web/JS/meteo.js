import { dataMeteo } from './api.js';

const API_HOURS = [" 02:00:00"," 05:00:00"," 08:00:00"," 11:00:00"," 14:00:00"," 17:00:00"," 20:00:00"," 23:00:00"," 23:00:00"];
var API_TEMP = [];
var API_PLUIE = [];

function getCurrentDate() {
    let currentDate = new Date();
    let year = currentDate.getFullYear();
    if (currentDate.getMonth() < 9) {
        var month = "0" + (currentDate.getMonth() + 1).toString();
    }else{
        var month = currentDate.getMonth().toString();
    }
    const day = String(currentDate.getDate());
    return `${year}-${month}-${day}`;
}

async function getTodayPrevision(){
    let data = await dataMeteo();
    const currentDate = getCurrentDate();
    let i = 0;
    API_HOURS.forEach(element => {
        const key = `${currentDate}${element}`;
        if (data[key] && data[key].temperature) {
            API_TEMP[i] = data[key].temperature.sol - 273.15;
            API_PLUIE[i] = data[key].pluie;
        }
        i++;
    });
}

function calcTemp() {
    let tMatin = (API_TEMP[1] + API_TEMP[2] + API_TEMP[3]) / 3;
    let tAprem = (API_TEMP[3] + API_TEMP[4] + API_TEMP[5]) / 3;
    let tSoir = (API_TEMP[5] + API_TEMP[6] + API_TEMP[7]) / 3;
    let texteMatin = document.getElementById("txt_matin");
    texteMatin.innerHTML = Math.round(tMatin*100)/100 + " °C";
    let texteAprem = document.getElementById("txt_aprem");
    texteAprem.innerHTML = Math.round(tAprem*100)/100 + " °C";
    let texteSoir = document.getElementById("txt_soir");
    texteSoir.innerHTML = Math.round(tSoir*100)/100 + " °C";
}

function calcPluie() {
    let pMatin = (API_PLUIE[1] + API_PLUIE[2] + API_PLUIE[3]) / 3;
    let pAprem = (API_PLUIE[3] + API_PLUIE[4] + API_PLUIE[5]) / 3;
    let pSoir = (API_PLUIE[5] + API_PLUIE[6] + API_PLUIE[7]) / 3;
    let imgMatin = document.getElementById("meteo_matin");
    let imgAprem = document.getElementById("meteo_aprem");
    let imgSoir = document.getElementById("meteo_soir");
    tstPluie(pMatin,imgMatin);
    tstPluie(pAprem,imgAprem);
    tstPluie(pSoir,imgSoir);
}

function tstPluie(nP,element){
    if (nP > 0.2) {
        element.src = "../src/img/rain.png";
    } else if (nP > 0) {
        element.src = "../src/img/cloud.png";
    } else {
        element.src = "../src/img/sun.png";
    }
}

async function main() {
    await getTodayPrevision();
    calcTemp();
    calcPluie();
}

main();