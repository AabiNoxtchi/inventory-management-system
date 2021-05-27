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
        this.setActiveLink = this.setActiveLink.bind(this);       
    }
    setActiveLink(index) {
        this.setState({
            activeLinkId: index,
            menuClicked:false
        });
    }
    
    openMenu=()=>{
        let menuClicked = this.state.menuClicked;
        menuClicked = !menuClicked;
        this.setState({ menuClicked: menuClicked })
    }

    render() {
        const isUserLoggedIn = AuthenticationService.isUserLoggedIn();
        const userRole = AuthenticationService.getLoggedUerRole();
        const userName = AuthenticationService.getLoggedUerName();
        const { location } = this.props;
        return (
            <header style={{
                height: "60px"
            }}>               
                   <nav className="navbar navbar-expand-md">
                    <div>                       
                        <Link className={
                            location.pathname == "/home" ? "navbar-brand special-h2-li selected" : "navbar-brand special-h2-li"}
                            onClick={() => this.setActiveLink(1)} to="/home">Inventory UI</Link>
                    </div>                   
                    <div className={this.state.menuClicked ? "clicked ml-auto" : "ml-auto"}>                      
                            <ul className="navbar-nav justify-content-end">                           
                        {(userRole === 'ROLE_Admin' || userRole === 'ROLE_Mol')&&
                            <li><Link className={location.pathname.indexOf("/users") > -1? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(2)} to="/users">users</Link></li>
                            }
                            {(userRole === 'ROLE_Mol' || userRole === 'ROLE_Employee') &&
                                <li><Link className={
                                    location.pathname.indexOf("/userprofiles") >-1? "nav-link selected" : "nav-link"}
                                    onClick={() => this.setActiveLink(3)} to="/userprofiles">profiles</Link></li>
                            }                       
                        {userRole === 'ROLE_Admin' &&
                                <>
                                    <li><Link className={
                                        location.pathname.indexOf("/pendingrequests") > -1 ? "nav-link selected" : "nav-link"}
                                        onClick={() => this.setActiveLink(13)} to="/pendingrequests">pending requests</Link></li>
                                    <li><Link className={
                                        location.pathname.indexOf("/categories") >-1? "nav-link selected" : "nav-link"}
                                        onClick={() => this.setActiveLink(4)} to="/categories">categories</Link></li>
                                    <li><Link className={
                                        location.pathname.indexOf("/countries") >-1? "nav-link selected" : "nav-link"}
                                    onClick={() => this.setActiveLink(5)} to="/countries">countries & cities</Link></li>
                                </>
                        }
                        {userRole === 'ROLE_Mol' &&
                            <>                       
                                <li><Link className={
                                    location.pathname.indexOf("/products") >-1? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(6)} to="/products">products</Link></li>
                                <li><Link className={
                                    (location.pathname.indexOf("/productdetails") > -1 || location.pathname.indexOf("/productDetails") > -1) ?
                                "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(7)} to="/productdetails">inventory</Link></li>
                                <li><Link className={
                                    location.pathname.indexOf("/usercategories") >-1? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(8)} to="/usercategories">categories</Link></li>
                                <li><Link className={
                                    location.pathname.indexOf("/suppliers") >-1? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(9)} to="/suppliers">suppliers</Link></li>                        
                            <li><Link
                                    className={
                                        location.pathname.indexOf("/deliveries") >-1? "nav-link selected" : "nav-link"}
                                onClick={() => this.setActiveLink(10)} to="/deliveries">deliveries</Link></li>
                            </>
                            }
                            {
                                <li className="ml-5"><Link
                                    className={
                                        location.pathname.indexOf("/register") > -1 || location.pathname.indexOf("/editprofile") > -1 ?
                                            "nav-link selected" : "nav-link"}
                                    to={!isUserLoggedIn ? `/register/-1` : `/editprofile/${AuthenticationService.getLoggedUerId()}`}
                                    onClick={() => {
                                        this.setActiveLink(11)}}>
                                    {!isUserLoggedIn ? "register" : userName}
                                   </Link></li>
                            }
                            {
                                !isUserLoggedIn && <li className=""><Link
                                    className={
                                        location.pathname.indexOf("/login") > -1 ? "nav-link selected" : "nav-link"} to="/login">
                                Login</Link></li>
                        }
                            {
                                isUserLoggedIn && <li className=""><Link className="nav-link" to="/logout"
                                    onClick={() => {
                                        this.setActiveLink(12); AuthenticationService.logout() ?
                                            this.props.onLogout() : console.log("we r fine")
                                    }}>Logout</Link></li>
                            }
                                <li className="icon"><Link                                    
                                    onClick={() => {
                                        let menuClicked = !this.state.menuClicked;
                                        this.setState({ menuClicked: menuClicked })
                                    }} 
                                    to="#"><i class="fa fa-bars"></i></Link></li>
                        </ul>
                        </div>                 
                </nav >
            </header>

            )
    }
}

export default withRouter(MenuComponent)
