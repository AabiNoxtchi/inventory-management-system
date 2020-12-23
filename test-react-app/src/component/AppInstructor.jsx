import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'

import ListCoursesComponent from './ListCoursesComponent';
import CourseComponent from './CourseComponent';
import LoginComponent from './LoginComponent';
import LogoutComponent from './LogoutComponent';
import MenuComponent from './MenuComponent';
import ListUsersComponent from './ListUsersComponent';
import UserComponent from './UserComponent';
import FooterComponent from './footer'

import AuthenticationServise from '../service/AuthenticationService';
import AuthenticatedRoute from './AuthenticatedRoute';

class AppInstructor extends Component {

    render() {
        return (

            <Router>           
                <>
                    <MenuComponent/>                    
                    <div className="pt-3 pb-5">
                    <Switch>
                    <Route path="/" exact component={LoginComponent} />
                    <Route path="/login" exact component={LoginComponent} />
                    <AuthenticatedRoute path="/logout" exact component={LogoutComponent}/>
                    <AuthenticatedRoute path="/courses" exact component={ListCoursesComponent} />
                    <AuthenticatedRoute path="/courses/:id" component={CourseComponent} />
                    <AuthenticatedRoute path="/users" exact component={ListUsersComponent} />
                    <AuthenticatedRoute path="/users?:search" exact component={ListUsersComponent} />
                    <AuthenticatedRoute path="/users/:id" component={UserComponent} />
                        </Switch>
                        <FooterComponent />
                    </div>
                   
                   
            </>
            </Router>
           
                
            )
    }
}

export default AppInstructor