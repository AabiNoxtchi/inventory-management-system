import React, { Component } from 'react'
import { Route, Redirect } from 'react-router-dom'
import AuthenticationService from '../service/AuthenticationService';

class AuthenticatedRoute extends Component {

    render() {
       

        if (AuthenticationService.isUserLoggedIn()) {

           // const { match, location, history } = this.props;
            //console.log("match path=" + match.path);

            return <Route {...this.props} />
                }else{
                    return <Redirect to="/login" />
                }
    }
}

export default AuthenticatedRoute