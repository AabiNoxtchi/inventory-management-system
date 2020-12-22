import axios from 'axios';

const API_URL = 'http://localhost:8080'
const AUTH_API_URL = `${API_URL}/api/inventory/auth/signin`

export const USER_NAME_SESSION_ATTRIBUTE_NAME = 'authenticatedUserName'
export const USER_ROLE_SESSION_ATTRIBUTE_NAME = 'authenticatedUserRole'
export const USER_TOKEN_SESSION_ATTRIBUTE_NAME = 'authenticatedUserToken'


class AuthenticationService {

    executeAuthentication(username, password) {
        return axios.post(`${AUTH_API_URL}`, {
            username,
            password
        });
    }

    registerSuccessfulLogin(username, token, role) {
        console.log('registerSuccessfull login username = ' + username + 'token = ' + token)

       // localStorage.setItem("user", JSON.stringify(response.data));

        sessionStorage.setItem(USER_NAME_SESSION_ATTRIBUTE_NAME, username)
        sessionStorage.setItem(USER_ROLE_SESSION_ATTRIBUTE_NAME, role)
        sessionStorage.setItem(USER_TOKEN_SESSION_ATTRIBUTE_NAME, this.createToken(token))
        console.log('registerSuccessfull login = ' + sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_NAME))
        console.log('token = ' + sessionStorage.getItem(USER_TOKEN_SESSION_ATTRIBUTE_NAME))

       // this.setupAxiosInterceptors(this.createToken(token))
       
    }

    createToken(token) {
        return 'Bearer ' + token
    }

    logout() {
        sessionStorage.removeItem(USER_NAME_SESSION_ATTRIBUTE_NAME);
        sessionStorage.removeItem(USER_ROLE_SESSION_ATTRIBUTE_NAME);
        sessionStorage.removeItem(USER_TOKEN_SESSION_ATTRIBUTE_NAME);
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

