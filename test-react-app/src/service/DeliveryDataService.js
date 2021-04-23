import axios from 'axios'
import authHeader from './AuthHeader';


const URL = 'http://localhost:8080/api/inventory'
const API_URL = `${URL}/deliveries`

class DeliveryDataService {

    retrieveAll(search) {
       
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

    deleteChild(id, parentId) {
        return axios.delete(`${API_URL}/${parentId}/child/${id}`, { headers: authHeader() });
    }


}

export default new DeliveryDataService()