import axios from 'axios'

import authHeader from './AuthHeader';


const URL = 'http://localhost:8080/api/inventory'
const API_URL = `${URL}/users`

class UserDataService {

    retrieveAll(search) {
        //console.log('path to server = ' + API_URL + search);

        return axios.get(`${API_URL}${search}`, { headers: authHeader() });
    }

    retrieve(id) {
        return axios.get(`${API_URL}/${id}`, { headers: authHeader() });
    }

    save(user) {
        return axios.post(`${API_URL}/signup`, user , { headers: authHeader() });
    }

    delete(id) {
        return axios.delete(`${API_URL}/${id}` , { headers: authHeader() });
    }

        
}

export default new UserDataService()