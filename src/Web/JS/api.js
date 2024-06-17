export async function velibs_info(){
    const response = await fetch('https://transport.data.gouv.fr/gbfs/nancy/station_information.json')
    return await response.json();
}

export async function velibs_status(){
    const response = await fetch('https://transport.data.gouv.fr/gbfs/nancy/station_status.json')
    return await response.json();
}

export async function dataMeteo() {
    try {
        const response = await fetch('https://www.infoclimat.fr/public-api/gfs/json?_ll=48.687,6.222&_auth=BB5TRFEvACJRfAE2AnRXflE5V2IBd1J1BnoCYQlsUi9SN1U5BmJQLVEhBGBTfVJkU2JQMgoqAiUBYlIyXTFTOARnUyhRLQB%2FUT8BfAItV2JRa1c0ASBSbwZlAnsJZVIyUjRVLwZnUDJROAR5U3xSZ1NnUDEKPQI4AWJSNl0wUzgEZFMoUS0AZFE4AWUCO1cxUWBXZAFsUj8GYwIwCWVSOVI5VS8GYFAzUTkEYFNqUm9TZFA1CioCJQEbUkZdLVNwBCVTYlF0AH9RawE9AmY%3D&_c=d9e5c2412377972a721b3086672c43ca');
        if (!response.ok) {
            throw new Error(`Erreur: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error("Api Non Chargé", error);
    }
}

/*
idées:
api adresses et coordonnées du grand nancy :
https://www.datagrandest.fr/data4citizen/visualisation/map/?id=adresses_2021&location=14,48.68801,6.19281

api météo:

 */