import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'

import LoginComponent from './LoginComponent';
import LogoutComponent from './LogoutComponent';
import MenuComponent from './MenuComponent';
import ListUsersComponent from './ListUsersComponent';
import UserComponent from './UserComponent';
import FooterComponent from './footer'
import ListSuppliersComponent from './ListSuppliersComponent';
import SupplierComponent from './SupplierComponent';
import ListProductsComponent from './ListProductsComponent';
import ProductComponent from './ProductComponent';
import ListDeliveriesComponent from './ListDeliveriesComponent';
import DeliveryComponent from './DeliveryComponent';
import ListProductDetails from './ListProductDetails';
import ListUserProfilesComponent from './ListUserProfilesComponent';
import ListCountriesComponent from './ListCountriesComponent';
import ListCategoriesComponent from './ListCategoriesComponent';
import ListUserCategoriesComponent from './ListUserCategoriesComponent';
import HomeComponent from './HomeComponent';
import ListPendingUsersComponent from './ListPendingUsersComponent';
import AuthenticatedRoute from './AuthenticatedRoute';
import AuthenticationService from '../service/AuthenticationService'
import EventListner from './EventListner';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

class AppInstructor extends Component {

    render() {

        if (AuthenticationService.isUserLoggedIn())
            this.props.loggedIn(true);
       
        return (           
           <>
                  <div className="pb-5">
                    <Router>   
                        <>                           
                            {this.props.userLoggedIn && <EventListner/>}
                            <MenuComponent menuclicked={false} onLogout={() => this.props.loggedIn(false)} />                           
                        <Switch>                           
                            <Route path="/login" exact render={(props) => (
                                 <LoginComponent {...props} onLogin={(value) => this.props.loggedIn(value)} />                                    
                            )} />                               
                            <AuthenticatedRoute path="/logout" exact component={LogoutComponent}/>                   
                            <AuthenticatedRoute path="/users" exact component={ListUsersComponent} />
                            <AuthenticatedRoute path="/pendingrequests" exact render={(props) => (<ListPendingUsersComponent {...props} />)} />
                            <AuthenticatedRoute path="/users?:search" exact component={ListUsersComponent} />
                            <AuthenticatedRoute path="/users/:id" component={UserComponent} />
                            <AuthenticatedRoute path="/suppliers" exact component={ListSuppliersComponent} />
                            <AuthenticatedRoute path="/suppliers?:search" exact component={ListSuppliersComponent} />
                            <AuthenticatedRoute path="/suppliers/:id" component={SupplierComponent} />
                            <AuthenticatedRoute path="/products" exact component={ListProductsComponent} />
                            <AuthenticatedRoute path="products?:search" exact component={ListProductsComponent} />
                            <AuthenticatedRoute path="/products/:id" component={ProductComponent} />
                            <AuthenticatedRoute path="/deliveries" exact component={ListDeliveriesComponent} />
                            <AuthenticatedRoute path="/deliveries/:id" exact component={DeliveryComponent} />
                            <AuthenticatedRoute path="/deliveries/:id/:deliveryView" exact component={DeliveryComponent} />
                            <AuthenticatedRoute  path="/productdetails" exact render={(props) => (<ListProductDetails {...props} />)} />
                            <AuthenticatedRoute path="/userprofiles" exact component={ListUserProfilesComponent} />
                            <AuthenticatedRoute path="/countries" exact component={ListCountriesComponent} />
                            <AuthenticatedRoute path="/categories" exact component={ListCategoriesComponent} />
                            <AuthenticatedRoute path="/usercategories" exact component={ListUserCategoriesComponent} />
                            <AuthenticatedRoute path="/home" exact component={HomeComponent} />
                            <AuthenticatedRoute path="/" exact component={HomeComponent} />
                            <Route key={"unique1"} path="/register/:id" exact render={(props) => (<UserComponent {...props} />)} />
                            <AuthenticatedRoute key={"unique2"} path="/editprofile/:id" exact render={(props) => (<UserComponent {...props} />)} />
                        </Switch>
                   </>
                       {this.props.userLoggedIn && <ToastContainer/>}                           
                    </Router>
                <FooterComponent />
                </div>
                </>
            )
    }
}

export default AppInstructor