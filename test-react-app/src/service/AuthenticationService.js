import axios from 'axios'

const API_URL = 'http://localhost:8080'
const AUTH_API_URL = `${API_URL}/api/inventory/auth/signin`

export const USER_NAME_SESSION_ATTRIBUTE_NAME = 'authenticatedUser'

class AuthenticationService {

    executeAuthentication(username, password) {
        return axios.post(`${AUTH_API_URL}`, {
            username,
            password
        });
    }

    registerSuccessfulLogin(username, token) {
        console.log('registerSuccessfull login username = ' + username + 'token = '+token)

        sessionStorage.setItem(USER_NAME_SESSION_ATTRIBUTE_NAME, username)
        console.log('registerSuccessfull login = ' + sessionStorage.getItem(USER_NAME_SESSION_ATTRIBUTE_NAME))

        this.setupAxiosInterceptors(this.createToken(token))
       
    }

    createToken(token) {
        return 'Bearer ' + token
    }

    logout() {
        sessionStorage.removeItem(USER_NAME_SESSION_ATTRIBUTE_NAME);
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

    setupAxiosInterceptors(token) {
        axios.interceptors.request.use(
            (config) => {
                if (this.isUserLoggedIn()) {
                    config.headers.authorization = token
                }
                return config
            }
        )
    }
}

export default new AuthenticationService()

