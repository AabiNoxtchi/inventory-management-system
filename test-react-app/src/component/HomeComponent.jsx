import React, { Component } from 'react';

import { Link } from 'react-router-dom'
import AuthenticationService from '../service/AuthenticationService';
import '../myStyles/Menu.css'

class HomeComponent extends Component {

    render() {

        const userRole = AuthenticationService.getLoggedUerRole();

        return (
            <div>
                <div class="jumbotron">
                <h1>Inventory UI</h1>
                </div>
                <div className="ml-5">
                    <div className="row"> 
                {
                    (userRole === 'ROLE_Admin' || userRole === 'ROLE_Mol' ) &&
                
                    <div class="col-md-3">
                        <h2>Users</h2>
                        <p>Manage users .</p>
                        <p><Link to='/users' className="btn btn-default">Users &raquo;</Link></p>
                        <p><Link to='/users/-1' className="btn btn-default">Add New User &raquo;</Link></p>
                            </div >
                            }
                    {(userRole === 'ROLE_Admin') &&
                        <>
                            <div class="col-md-3">
                                <h2>Categories</h2>
                                <p>Manage categories .</p>
                                <p><Link to='/categories' className="btn btn-default">Categories &raquo;</Link></p>
                                <p><Link to='/categories/-1' className="btn btn-default">Add New Category &raquo;</Link></p>
                            </div >
                            <div class="col-md-3">
                                <h2>Countries & Cities</h2>
                                <p>Manage countries & cities .</p>
                                <p><Link to='/countries' className="btn btn-default">Countries & Cities &raquo;</Link></p>
                                <p><Link to='/countries/-1' className="btn btn-default">Add New Countries or Cities &raquo;</Link></p>
                            </div >
                            </>               
                        }
                        {(userRole === 'ROLE_Mol' || userRole == 'ROLE_Employee') &&
                            <>
                                <div class="col-md-3">
                                    <h2>Profiles</h2>
                                    <p>{userRole === 'ROLE_Mol' ? 'Manage profiles .' : 'My Profiles .'}</p>
                                    <p><Link to='/userprofiles' className="btn btn-default">Profiles &raquo;</Link></p>
                                </div >
                            </>
                        }
                    {(userRole === 'ROLE_Mol') &&
                            <>
                            <div class="col-md-3">
                                <h2>Products</h2>
                                <p>Manage products .</p>
                                <p><Link to='/products' className="btn btn-default">Products &raquo;</Link></p>
                                <p><Link to='/products/-1' className="btn btn-default">Add New Product &raquo;</Link></p>
                            </div >
                            <div class="col-md-3">
                                <h2>Inventories</h2>
                                <p>Manage inventories .</p>
                                <p><Link to='/productDetails' className="btn btn-default">Inventories &raquo;</Link></p>
                                <p><Link to='/productDetails/-1' className="btn btn-default">Add New Inventory &raquo;</Link></p>
                            </div >                           
                        </>
                    }
                </div>
                <div className="row mt-5"> 
                {userRole === 'ROLE_Mol' &&
                            <>
                            <div class="col-md-3">
                                <h2>Categories</h2>
                                <p>Manage categories .</p>
                                <p><Link to='/usercategories' className="btn btn-default">Categories &raquo;</Link></p>
                                <p><Link to='/usercategories/-1' className="btn btn-default">Add New Category &raquo;</Link></p>
                            </div >
                        <div class="col-md-3">
                            <h2>Suppliers</h2>
                            <p>Manage suppliers .</p>
                            <p><Link to='/suppliers' className="btn btn-default">Suppliers &raquo;</Link></p>
                            <p><Link to='/suppliers/-1' className="btn btn-default">Add New Supplier &raquo;</Link></p>
                        </div >
                        <div class="col-md-3">
                            <h2>Deliveries</h2>
                            <p>Manage deliveries .</p>
                            <p><Link to='/deliveries' className="btn btn-default">Deliveries &raquo;</Link></p>
                            <p><Link to='/deliveries/-1' className="btn btn-default">Add New Delivery &raquo;</Link></p>
                        </div >
                    </>
                    }
                    </div>
                </div>
                        </div>    
         )
    }

}

export default HomeComponent