export async function velibs_info(){
    const response = await fetch('https://transport.data.gouv.fr/gbfs/nancy/station_information.json')
    return await response.json();
}

export async function velibs_status(){
    const response = await fetch('https://transport.data.gouv.fr/gbfs/nancy/station_status.json')
    return await response.json();
}


/*
idées:
api adresses et coordonnées du grand nancy :
https://www.datagrandest.fr/data4citizen/visualisation/map/?id=adresses_2021&location=14,48.68801,6.19281

api météo:

 */