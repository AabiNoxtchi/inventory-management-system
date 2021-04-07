import axios from 'axios'

import authHeader from './AuthHeader';


const URL = 'http://localhost:8080/api/inventory'
const API_URL = `${URL}/userprofiles`

class UserProfileDataService {

    retrieveAll(search) {
        console.log('path to server = ' + API_URL + search);

        return axios.get(`${API_URL}${search}`, { headers: authHeader() });
    }

    retrieve(id) {
        return axios.get(`${API_URL}/${id}`, { headers: authHeader() });
    }

    retrieveTimeline(search) {
        console.log('path to server = ' + API_URL+'/timeline' + search);

        return axios.get(`${API_URL}/timeline${search}`, { headers: authHeader() });
    }

    save(item) {
        //console.log("userProfiledata service save item");
        return axios.put(`${API_URL}`, item, { headers: authHeader() });
    }

    saveTimeline(item) {
       // console.log("userProfiledata service save item");
        return axios.put(`${API_URL}/timeline`, item, { headers: authHeader() });
    }

    delete(id) {
        return axios.delete(`${API_URL}/${id}`, { headers: authHeader() });
    }

    //@DeleteMapping("/{productDetailId}/before/{date}")
    deleteAllBefore(date, id) {
        return axios.delete(`${API_URL}/${id}/before/${date}`, { headers: authHeader() });
    }

}

export default new UserProfileDataService()