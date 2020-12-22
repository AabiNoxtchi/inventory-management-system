import AuthenticationService from './AuthenticationService'

export default function authHeader() {
    

    if (AuthenticationService.getLoggedUerName()) {
        return {
            'Authorization': `` + AuthenticationService.getLoggedUerToken()

        };
    } else {
        return {};
    }
}