import React, { Component } from 'react'
import { Link, withRouter } from 'react-router-dom'
import AuthenticationService from '../service/AuthenticationService';
import '../myStyles/Menu.css'

class MenuComponent extends Component {

    constructor(props) {
        super(props)
       
        this.state = {
            activeLinkId: 0,
            menuClicked: props.menuClicked||false,
            
        }
        console.log('menu component : activelink = ' + this.state.activeLinkId || 'none');
        console.log("pathname = " + window.location.pathname)
        this.setActiveLink = this.setActiveLink.bind(this);
       
    }

    setActiveLink(index) {
        this.setState({
            activeLinkId: index,
            menuClicked:false
        });
        console.log('activelink = index=' + index);
    }

   /* myFunction() {
    var x = document.getElementById("myTopnav");
    if (x.className === "topnav") {
        x.className += " responsive";
    } else {
        x.className = "topnav";
    }
}*/

    openMenu=()=>{
        console.log("open menu clicked");
        let menuClicked = this.state.menuClicked;
        menuClicked = !menuClicked;
        this.setState({ menuClicked: menuClicked })
    }

    render() {
        console.log("rendering menu  = ");
        const isUserLoggedIn = AuthenticationService.isUserLoggedIn();
        const userRole = AuthenticationService.getLoggedUerRole();
        return (
            <header style={{
                height: "60px"
            }}>
                {console.log("this.state.menuclickd = " + this.state.menuClicked)}
                   <nav className="navbar navbar-expand-md">
                    <div>
                        < a href="#" className="navbar-brand special-h2-li pl-5">Inventory UI</ a>
                    </div>
                    {/*<div className={this.state.menuClicked ? "overlay d-block" : "d-none"}></div>*/}
                    <div className={this.state.menuClicked ? "clicked ml-auto" : "ml-auto"}>
                        {/* <div className={userRole === 'ROLE_Mol' ? "mol" : userRole === 'ROLE_Admin'? "admin" : "other"}>*/}
                            <ul className="navbar-nav justify-content-end">
                       
                        {userRole !== 'ROLE_Admin' &&
                            <li><Link className={this.state.activeLinkId == 7 ||
                            window.location.pathname.indexOf("/userprofiles") > -1 ? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(7)} to="/userprofiles">profiles</Link></li>
                        }
                        {(userRole === 'ROLE_Admin' || userRole === 'ROLE_Mol')&&
                            <li><Link className={this.state.activeLinkId == 2 ||
                                window.location.pathname.indexOf("/users") > -1 ? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(2)} to="/users">users</Link></li>
                        }
                       
                        {
                            userRole === 'ROLE_Admin' &&
                            <>
                                <li><Link className={this.state.activeLinkId == 9 ||
                                    window.location.pathname.indexOf("/categories") > -1 ? "nav-link selected" : "nav-link"}
                                    onClick={() => this.setActiveLink(9)} to="/categories">categories</Link></li>
                            <li><Link className={this.state.activeLinkId == 8 ||
                                window.location.pathname.indexOf("/countries") > -1 ? "nav-link selected" : "nav-link"}
                                    onClick={() => this.setActiveLink(8)} to="/countries">countries & cities</Link></li>
                                </>
                        }
                        {userRole === 'ROLE_Mol' &&
                            <>
                       
                            <li><Link className={this.state.activeLinkId == 4 &&
                                window.location.pathname.indexOf("/products") > -1 ? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(4)} to="/products">products</Link></li>
                            <li><Link className={this.state.activeLinkId == 5 ||
                                window.location.pathname.indexOf("/productDetails") > -1 || window.location.pathname.indexOf("/productdetails") > -1 ?
                                "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(5)} to="/productdetails">inventory</Link></li>
                            <li><Link className={this.state.activeLinkId == 10 ||
                                window.location.pathname.indexOf("/usercategories") > -1 ? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(10)} to="/usercategories">categories</Link></li>
                            <li><Link className={this.state.activeLinkId == 3 ||
                                window.location.pathname.indexOf("/suppliers") > -1 ? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(3)} to="/suppliers">suppliers</Link></li>
                        
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

                                <li className="icon"><Link
                                    
                                    onClick={() => {
                                        console.log("open menu clicked ***********************************************");
                                        let menuClicked = !this.state.menuClicked;
                                       // menuClicked = !menuClicked;
                                        this.setState({ menuClicked: menuClicked })
                                    }} 
                                    to="#"><i class="fa fa-bars"></i></Link></li>

                                {/*  <a href="#" class="icon" onclick={() => {
                                    console.log("open menu clicked ***********************************************");
                                    let menuClicked = this.state.menuClicked;
                                    menuClicked = !menuClicked;
                                    this.setState({ menuClicked: menuClicked })
                                }}>
                            <i class="fa fa-bars"></i>
                        </a>*/}
                        </ul>
                        </div>
                 
                </nav >
                    

                {/* <div class="topnav" id="myTopnav">
                    <a href="#home" class="active">Home</a>
                    <a href="#news">News</a>
                    <a href="#contact">Contact</a>
                    <a href="#about">About</a>
                    <a href="javascript:void(0);" class="icon" onclick="myFunction()">
                        <i class="fa fa-bars"></i>
                    </a>
                </div>

                <div style="padding-left:16px">
                    <h2>Responsive Topnav Example</h2>
                    <p>Resize the browser window to see how it works.</p>
                </div>*/}


            </header>

            )
    }
}

export default withRouter(MenuComponent)