const API_HOURS = [" 02:00:00"," 05:00:00"," 08:00:00"," 11:00:00"," 14:00:00"," 17:00:00"," 20:00:00"," 23:00:00"," 23:00:00"];
var API_TEMP = [];
var API_PLUIE = [];

function getCurrentDate() {
    currentDate = new Date();
    year = currentDate.getFullYear();
    if (currentDate.getMonth() < 9) {
        month = "0" + (currentDate.getMonth() + 1).toString();
    }else{
        month = currentDate.getMonth().toString();
    }
    const day = String(currentDate.getDate());
    return `${year}-${month}-${day}`;
}

async function getTodayPrevision(){
    data = await loadApi();
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
    tMatin = (API_TEMP[1] + API_TEMP[2] + API_TEMP[3]) / 3;
    tAprem = (API_TEMP[3] + API_TEMP[4] + API_TEMP[5]) / 3;
    tSoir = (API_TEMP[5] + API_TEMP[6] + API_TEMP[7]) / 3;
    console.log(Math.round(tMatin*100)/100 + " C");
    console.log(Math.round(tAprem*100)/100 + " C");
    console.log(Math.round(tSoir*100)/100 + " C");

}

function calcPluie() {
    pMatin = (API_PLUIE[1] + API_PLUIE[2] + API_PLUIE[3]) / 3;
    pAprem = (API_PLUIE[3] + API_PLUIE[4] + API_PLUIE[5]) / 3;
    pSoir = (API_PLUIE[5] + API_PLUIE[6] + API_PLUIE[7]) / 3;
    tstPluie(pMatin);
    tstPluie(pAprem);
    tstPluie(pSoir);
}

function tstPluie(nP){
    if (nP > 0.2) {
        console.log("Il va pleuvoir");
    } else if (nP > 0) {
        console.log("Il va y avoir des nuages");
    } else {
        console.log("Il va faire beau");
    }
}

async function main() {
    await getTodayPrevision();
    calcTemp();
    calcPluie();
}

main();