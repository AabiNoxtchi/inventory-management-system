import React, { Component } from 'react'
import { Link, withRouter } from 'react-router-dom'
import AuthenticationService from '../service/AuthenticationService';
import '../myStyles/Menu.css'

class MenuComponent extends Component {

    constructor() {
        super();
       
        this.state = {
            activeLinkId: 0,
            
        }
        console.log('menu component : activelink = ' + this.state.activeLinkId || 'none');
        console.log("pathname = " + window.location.pathname)
        this.setActiveLink = this.setActiveLink.bind(this);
       
    }

    setActiveLink(index) {
        this.setState({
            activeLinkId: index
        });
        console.log('activelink = index=' + index);
    }

    render() {
        console.log("rendering menu  = ");
        const isUserLoggedIn = AuthenticationService.isUserLoggedIn();
        const userRole = AuthenticationService.getLoggedUerRole();
        return (
            <header>
                <nav className="navbar navbar-expand-md  navbar-inverse navbar-fixed-top px-5">
                    <div>
                        < a href="#" className="navbar-brand special-h2-li">Inventory UI</ a>
                    </div>
                    <ul className="navbar-nav justify-content-center pr-5 mr-5">
                        <li><Link className={this.state.activeLinkId == 1 ? "nav-link selected" : "nav-link"}
                                  onClick={() => this.setActiveLink(1)} to="/courses">Courses</Link></li>
                        {userRole !== 'ROLE_Employee' &&
                            <li><Link className={this.state.activeLinkId == 7 ||
                            window.location.pathname.indexOf("/userprofiles") > -1 ? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(7)} to="/userprofiles">profiles</Link></li>
                        }
                        {(userRole === 'ROLE_Employee' || userRole === 'ROLE_Mol')&&
                            <li><Link className={this.state.activeLinkId == 2 ||
                                window.location.pathname.indexOf("/users") > -1 ? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(2)} to="/users">users</Link></li>
                        }
                        {userRole === 'ROLE_Mol' &&
                            <>
                            <li><Link className={this.state.activeLinkId == 3 ||
                                window.location.pathname.indexOf("/suppliers") > -1 ? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(3)} to="/suppliers">suppliers</Link></li>
                        
                       
                            <li><Link className={this.state.activeLinkId == 4 ||
                                window.location.pathname.indexOf("/products") > -1 ? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(4)} to="/products">products</Link></li>
                            <li><Link className={this.state.activeLinkId == 5 ||
                                window.location.pathname.indexOf("/productdetails") > -1 ? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(5)} to="/productdetails">inventory</Link></li>
                        
                        
                            <li><Link
                            className={this.state.activeLinkId == 6 ||
                                window.location.pathname.indexOf("/deliveries") > -1 ? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(6)} to="/deliveries">deliveries</Link></li>
                            </>
                        }
                        {
                            !isUserLoggedIn && <li><Link
                                className={this.state.activeLinkId == 0 ? "nav-link selected" : "nav-link"} to="/login">
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