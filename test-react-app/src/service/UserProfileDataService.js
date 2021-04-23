import axios from 'axios'
import authHeader from './AuthHeader';

const URL = 'http://localhost:8080/api/inventory'
const API_URL = `${URL}/userprofiles`

class UserProfileDataService {

    retrieveAll(search) {
      
        return axios.get(`${API_URL}${search}`, { headers: authHeader() });
    }

    retrieve(id) {
        return axios.get(`${API_URL}/${id}`, { headers: authHeader() });
    }

    retrieveTimeline(search) {
      
        return axios.get(`${API_URL}/timeline${search}`, { headers: authHeader() });
    }

    save(item) {
        return axios.put(`${API_URL}`, item, { headers: authHeader() });
    }

    saveTimeline(item) {
       return axios.put(`${API_URL}/timeline`, item, { headers: authHeader() });
    }

    delete(id) {
        return axios.delete(`${API_URL}/${id}`, { headers: authHeader() });
    }

    deleteAllBefore(date, id) {
        return axios.delete(`${API_URL}/${id}/before/${date}`, { headers: authHeader() });
    }
}

export default new UserProfileDataService()