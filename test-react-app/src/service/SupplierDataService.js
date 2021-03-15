import axios from 'axios'

import authHeader from './AuthHeader';


const URL = 'http://localhost:8080/api/inventory'
const API_URL = `${URL}/suppliers`

class SupplierDataService {

    retrieveAll(search) {
        console.log('path to server = ' + API_URL + search);

        return axios.get(`${API_URL}${search}`, { headers: authHeader() });
    }

    retrieve(id) {
        return axios.get(`${API_URL}/${id}`, { headers: authHeader() });
    }

    save(item) {
        console.log('supplierdataservice' + item + ' path to servr = ' + API_URL);
        return axios.put(`${API_URL}`, item, { headers: authHeader() });
    }

    delete(id) {
        return axios.delete(`${API_URL}/${id}`, { headers: authHeader() });
    }


}

export default new SupplierDataService()