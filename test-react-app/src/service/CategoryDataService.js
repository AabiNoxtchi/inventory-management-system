import axios from 'axios'
import authHeader from './AuthHeader';


const URL = 'http://localhost:8080/api/inventory'
const API_URL = `${URL}/categories`

class CategoryDataService {

    retrieveAll(search) {
        console.log('path to server = ' + API_URL + search);

        return axios.get(`${API_URL}${search}`, { headers: authHeader() });
    }


    retrieve(id) {
        return axios.get(`${API_URL}/${id}`, { headers: authHeader() });
    }

    save(item) {
        return axios.put(`${API_URL}`, item, { headers: authHeader() });
    }

    delete(id) {
        return axios.delete(`${API_URL}/${id}`, { headers: authHeader() });
    }
    
}

export default new CategoryDataService()