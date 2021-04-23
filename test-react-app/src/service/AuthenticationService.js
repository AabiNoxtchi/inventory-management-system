import axios from 'axios';

const API_URL = 'http://localhost:8080'
const AUTH_API_URL = `${API_URL}/api/inventory/auth/signin`

export const USER_NAME_SESSION_ATTRIBUTE_NAME = 'authenticatedUserName'
export const USER_ROLE_SESSION_ATTRIBUTE_NAME = 'authenticatedUserRole'
export const USER_TOKEN_SESSION_ATTRIBUTE_NAME = 'authenticatedUserToken'
export const USER_ID_SESSION_ATTRIBUTE_NAME = 'authenticatedUserId'


class AuthenticationService {

    executeAuthentication(username, password) {
        return axios.post(`${AUTH_API_URL}`, {
            username,
            password
        });
    }

    registerSuccessfulLogin(username, token, role, id) {
        console.log('registerSuccessfull login username = ' + username + 'token = ' + token)
        //EventListner.subscribe();
       // localStorage.setItem("user", JSON.stringify(response.data));

        sessionStorage.setItem(USER_NAME_SESSION_ATTRIBUTE_NAME, username)
        sessionStorage.setItem(USER_ROLE_SESSION_ATTRIBUTE_NAME, role)
       // sessionStorage.setItem(USER_ID_SESSION_ATTRIBUTE_NAME, id)
        sessionStorage.setItem(USER_TOKEN_SESSION_ATTRIBUTE_NAME, this.createToken(token))
        sessionStorage.setItem(USER_ID_SESSION_ATTRIBUTE_NAME, id)
       // console.log('registerSuccessfull login = ' + sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_NAME))
       // console.log('token = ' + sessionStorage.getItem(USER_TOKEN_SESSION_ATTRIBUTE_NAME))
       // console.log('Role = ' + sessionStorage.getItem(USER_ROLE_SESSION_ATTRIBUTE_NAME))

       // this.setupAxiosInterceptors(this.createToken(token))
       
    }

    setRegister(data) {
        sessionStorage.setItem(USER_TOKEN_SESSION_ATTRIBUTE_NAME, this.createToken(data.jwtToken))
        sessionStorage.setItem(USER_NAME_SESSION_ATTRIBUTE_NAME, data.userName)

    }

    createToken(token) {
        return 'Bearer ' + token
    }

    logout() {
        sessionStorage.removeItem(USER_NAME_SESSION_ATTRIBUTE_NAME);
        sessionStorage.removeItem(USER_ROLE_SESSION_ATTRIBUTE_NAME);
        sessionStorage.removeItem(USER_ID_SESSION_ATTRIBUTE_NAME);
        sessionStorage.removeItem(USER_TOKEN_SESSION_ATTRIBUTE_NAME);
        return true;
    }

    isUserLoggedIn() {
        let user = sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_NAME)
        if (user === null) return false
        return true
    }

    getLoggedUerName() {
        let user = sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_NAME)
        if (user === null) return ''
        return user
    }

    getLoggedUerId() {
        let id = sessionStorage.getItem(USER_ID_SESSION_ATTRIBUTE_NAME)
        if (id === null) return ''
        return id
    }

    getLoggedUerRole() {
        let role = sessionStorage.getItem(USER_ROLE_SESSION_ATTRIBUTE_NAME)
        if (role === null) return ''
        return role
    }

   

    getLoggedUerToken() {
        let token = sessionStorage.getItem(USER_TOKEN_SESSION_ATTRIBUTE_NAME)
        if (token === null) return ''
        return token
    }

  /*  setupAxiosInterceptors(token) {
        axios.interceptors.request.use(
            (config) => {
                if (this.isUserLoggedIn()) {
                    config.headers.authorization = token
                }
                return config
            }
        )
    }*/
}

export default new AuthenticationService()

