import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'

import ListCoursesComponent from './ListCoursesComponent';
import CourseComponent from './CourseComponent';
import LoginComponent from './LoginComponent';
import LogoutComponent from './LogoutComponent';
import MenuComponent from './MenuComponent';
import AuthenticationServise from '../service/AuthenticationService';
import AuthenticatedRoute from './AuthenticatedRoute';

class AppInstructor extends Component {

    render() {
        return (

            <Router>           
                <>
                    <MenuComponent/>
                    
                    <div className="container pt-5">
                <Switch>
                    <Route path="/" exact component={LoginComponent} />
                    <Route path="/login" exact component={LoginComponent} />
                    <AuthenticatedRoute path="/logout" exact component={LogoutComponent}/>
                    <AuthenticatedRoute path="/courses" exact component={ListCoursesComponent} />
                    <AuthenticatedRoute path="/courses/:id" component={CourseComponent} />
                        </Switch>
                    </div>
            </>
            </Router>
                
            )
    }
}

export default AppInstructor