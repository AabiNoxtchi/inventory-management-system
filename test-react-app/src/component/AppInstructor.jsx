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
import ListSuppliersComponent from './ListSuppliersComponent';
import SupplierComponent from './SupplierComponent';
import ListProductsComponent from './ListProductsComponent';
import ProductComponent from './ProductComponent';
import ListDeliveriesComponent from './ListDeliveriesComponent';
import DeliveryComponent from './DeliveryComponent';
import ListProductDetails from './ListProductDetails';
import ListUserProfilesComponent from './ListUserProfilesComponent';

import AuthenticatedRoute from './AuthenticatedRoute';

class AppInstructor extends Component {

    render() {
        return (

            <Router>           
                <>
                    <MenuComponent />                    
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
                            <AuthenticatedRoute path="/suppliers" exact component={ListSuppliersComponent} />
                            <AuthenticatedRoute path="/suppliers?:search" exact component={ListSuppliersComponent} />
                            <AuthenticatedRoute path="/suppliers/:id" component={SupplierComponent} />
                            <AuthenticatedRoute path="/products" exact component={ListProductsComponent} />
                            <AuthenticatedRoute path="products?:search" exact component={ListProductsComponent} />
                            <AuthenticatedRoute path="/products/:id" component={ProductComponent} />
                            <AuthenticatedRoute path="/deliveries" exact component={ListDeliveriesComponent} />
                            <AuthenticatedRoute path="/deliveries/:id" exact component={DeliveryComponent} />
                            <AuthenticatedRoute path="/deliveries/:id/:deliveryView" exact component={DeliveryComponent} />
                            <AuthenticatedRoute path="/productdetails" exact component={ListProductDetails} />
                            <AuthenticatedRoute path="/userprofiles" exact component={ListUserProfilesComponent} />
                        </Switch>
                        <FooterComponent />
                    </div>
                   
                   
            </>
            </Router>
           
                
            )
    }
}

export default AppInstructor