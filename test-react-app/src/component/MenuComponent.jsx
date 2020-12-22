import React, { Component } from 'react'
import { Link, withRouter } from 'react-router-dom'
import AuthenticationService from '../service/AuthenticationService';

class MenuComponent extends Component {
    render() {

        const isUserLoggedIn = AuthenticationService.isUserLoggedIn();
        const userRole = AuthenticationService.getLoggedUerRole();

        return (

           
            <header>

                <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                    <div>
                        < a href="#" className="navbar-brand special-h2-li">Inventory Management System</ a>
                    </div>
                    <ul className="navbar-nav">
                        <li><Link className="nav-link" to="/courses">Courses</Link></li> 
                        {userRole !== 'ROLE_Employee' && <li><Link className="nav-link" to="/users">users</Link></li> }
                    </ul>
                    <ul className="navbar-nav navbar-collapse justify-content-end">
                        {
                            !isUserLoggedIn && <li><Link className="nav-link" to="/login">
                            Login</Link></li>
                        }
                        {

                            isUserLoggedIn && <li><Link className="nav-link" to="/logout"
                                onClick={AuthenticationService.logout}>Logout</Link></li>
                        }

                    </ul>
                </nav>
            </header>



            )
    }
}

export default withRouter(MenuComponent)