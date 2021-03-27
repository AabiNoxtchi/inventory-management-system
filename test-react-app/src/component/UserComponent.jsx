import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import UserDataService from '../service/UserDataService';
import '../myStyles/Style.css';
import AuthenticationService from '../service/AuthenticationService';
import CustomSelect from './Filters/CustomSelect';


class UserComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.match.params.id || -1,
            firstName: '',
            lastName: '',
            userName: '',
            email: '',
            password: '',
            confirmpassword: '',
            changingpassword: this.props.match.params.id > 0 ? false : true,
            countries: null,
            cities: null,
            filteredcities: null,
            countryId: null,
            cityId:''
        }
        this.onSubmit = this.onSubmit.bind(this)
        this.validate = this.validate.bind(this)
        this.cancelForm = this.cancelForm.bind(this)
    }

    componentDidMount() {
        let userRole = this.getUserRole();
        console.log("component mount userRole  = " + (userRole))
       
        console.log(this.state.id)
        console.log("(this.state.id === -1 && userRole !== 'ROLE_Admin') = " + ((this.state.id === -1 && userRole !== 'ROLE_Admin')));
        if (this.state.id === -1 && userRole !== 'ROLE_Admin') {
            return
        }
        UserDataService.retrieve(this.state.id)
            .then(response => {
                console.log("response = " + JSON.stringify(response));
                this.setState({
                   // userRole: userRole,
                    firstName: response.data.firstName||'',
                    lastName: response.data.lastName||'',
                    userName: response.data.userName||'',
                    email: response.data.email||'',
                    countries: userRole === 'ROLE_Admin' ? response.data.countries : null,
                    cities: userRole === 'ROLE_Admin' ? response.data.cities : null,
                    filteredcities: userRole === 'ROLE_Admin' ? response.data.countryId ? 
                        this.filterCities([], response.data.cities, response.data.countryId): response.data.cities : null,
                    countryId: userRole === 'ROLE_Admin' && response.data.countryId ? response.data.countryId : null,
                    cityId: userRole === 'ROLE_Admin' && response.data.cityId ? response.data.cityId+'' : ''
                })
            })
    }

    getUserRole() {
        return AuthenticationService.getLoggedUerRole();
    }

    onSubmit(values, actions) {
        let user = {
            id: this.state.id,
            firstName: values.firstName,
            lastName: values.lastName,
            username: values.userName,
            email: values.email,
            password: this.state.changingpassword ? values.password : null,
            cityId: this.getUserRole() === 'ROLE_Admin' ? values.cityId : null,
            targetDate: values.targetDate
        }
        console.log("user to update = " + JSON.stringify(user))
        UserDataService.save(user)
            .then(() => this.props.history.push('/users'))
            .catch(error => {
                console.log("error = " + error);
                console.log("error.response.data = " + JSON.stringify(error.response.data))
                //let msg = error.response && typeof error.response.data == 'string' ? error.response.data :
                  //  error.response.data.message ? error.response.data.message : error;
                let msg = "" + error.response && typeof error.response.data == 'string' ?
                    error.response.data : error.response.data.errors ?
                        error.response.data.errors[0].defaultMessage : error.response.data.message ?
                            error.response.data.message : error;
                           

                if (msg.indexOf("Email") > -1)
                    actions.setErrors({ email: msg })
                if (msg.indexOf("User name") > -1)
                    actions.setErrors({ username: msg })
                if (msg.indexOf("Password") > -1)
                    actions.setErrors({ Password: msg })
                this.setState({
                    errormsg: msg
                })
               
    })
    }

    validate(values) {
        let errors = {}
        if (!values.userName) {
            errors.userName = 'required field !!!'
        } else if (values.userName.length < 4) {
            errors.userName = 'Enter at least 4 Characters'
        }

        if (!values.email) {
            errors.email = 'required field !!!'
        } else if (values.email.length < 4) {
            errors.email = 'Enter at least 4 Characters'
        }

        if (this.state.id === -1 || this.state.changingpassword) {
            if (!values.password) {
                errors.password = 'required field !!!'
            } else if (values.password.length < 6) {
                errors.password = 'Enter at least 6 Characters'
            } else if (values.password != values.confirmpassword) {
                errors.confirmpassword='password and confirm password don\'t match'
            }
        }

        if (this.getUserRole() === 'ROLE_Admin' && !values.cityId) {
            errors.cityId = 'required field !!!'           
        }

        return errors
    }

   cancelForm() {
      // this.props.history.push('/users')
       window.history.back();
    }

    changePassword = () => {
        this.setState({
            changingpassword: this.state.changingpassword ? false : true
        })
    }

    onFocus = event => {
        console.log("on focus ");

        if (event.target.autocomplete) {
            event.target.autocomplete = "whatever";
        }

    }

    filterCities(subs, cities, value) {
       
        subs = []
        for (let i = 0; i < cities.length; i++) {

            if (cities[i].filterBy == value) {
                subs.push(cities[i])
            }
        }

        return subs
    }

    render() {
        const userRole = this.getUserRole();
        let { id, firstName, lastName, userName, email, password, confirmpassword,/* countries, cities, filteredcities, countryId,*/ cityId } = this.state
       
        return (
            <div className="container">
                {this.state.id > 0 ? <h3 className="mb-3"> Update User</h3> : <h3 className="mb-3"> Add New User </h3>}
                <Formik
                    initialValues={{ id, firstName, lastName, userName, email, password, confirmpassword,/* countries, cities, filteredcities, countryId,*/ cityId }}
                    onSubmit={(values, actions) => this.onSubmit(values, actions)}
                        validateOnChange={false}
                        validateOnBlur={false}
                        validate={this.validate}
                        enableReinitialize={true}
                    >
                        {
                        ({ setFieldValue, values, dirty }) => (
                            <Form className="d-flex flex-wrap">
                                {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                                {console.log("values = " + JSON.stringify(values))}

                                <div className="inline w50">
                                    <Field className="form-control" type="text" name="id" hidden />
                                   
                                    <fieldset className="form-group w90">
                                        <label>first name</label>
                                    <Field className="form-control" type="text" name="firstName"
                                        autoComplete="new-password"/>
                                        <ErrorMessage name="firstName" component="div"
                                        className="alert alert-warning" />
                                    </fieldset>

                                    <fieldset className="form-group w90">
                                        <label>last name</label>
                                        <Field className="form-control" type="text" name="lastName"
                                            autoComplete="new-password" />
                                        <ErrorMessage name="lastName" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>

                                    <fieldset className="form-group w90">
                                        <label className="required-field">user name</label>

                                        <Field className="form-control " type="text" name="userName"
                                            autoComplete="new-password" />
                                        <ErrorMessage name="userName" component="div"
                                            className="alert alert-warning " />
                                    </fieldset>

                                    

                                   
                                    
                               
                                   
                                {//console.log("userRole  = " + (userRole))
                                }
                                {//console.log("userRole === 'ROLE_Admin' = " + (userRole === 'ROLE_Admin'))
                                }
                                {//console.log("countries = " + JSON.stringify(countries))
                                }
                                    {//console.log("filteredcities = " + JSON.stringify(filteredcities))
                                    }
                                {//console.log("values.filteredcities = " + JSON.stringify(values.filteredcities))
                                }
                               

                               

                                {userRole === 'ROLE_Admin' &&
                                   
                                        <fieldset className="inline form-group pl-0 w90">

                                            <label>Country&nbsp;</label>
                                            <CustomSelect
                                                className={""}
                                                items={this.state.countries}
                                              //  value={values.countryId || ''}
                                            value={this.state.countryId || ''}
                                               /* onChange={(selected) => {
                                                    setFieldValue("countryId", selected.value);

                                                    let subs = values.filteredcities;
                                                    subs = this.filterCities(subs, values.cities, selected.value);
                                                    // let sub = values.filteredInventoryNumbers.
                                                    /*  subs = [];
                                                      for (let i = 0; i < values.cities.length; i++) {

                                                          if (values.cities[i].filterBy == selected.value) {
                                                              subs.push(values.cities[i])
                                                          }
                                                      }*/
                                                   // setFieldValue("filteredcities", subs);
                                  //  }}*/
                                        onChange={(selected) => {
                                            

                                            let subs = this.state.filteredcities;
                                            subs = this.filterCities(subs, this.state.cities, selected.value);
                                            // let sub = values.filteredInventoryNumbers.
                                            /*  subs = [];
                                              for (let i = 0; i < values.cities.length; i++) {

                                                  if (values.cities[i].filterBy == selected.value) {
                                                      subs.push(values.cities[i])
                                                  }
                                              }*/
                                            //setFieldValue("filteredcities", subs);
                                            // setFieldValue("countryId", selected.value);
                                            this.setState({
                                                countryId: selected.value,
                                                filteredcities:subs

                                            })
                                        }}
                                            />
                                        </fieldset>
                                      
                                    }

                                   
                                    {userRole === 'ROLE_Admin' &&

                                        <fieldset className="form-group w90">

                                            <label className="required-field">city&nbsp;</label>
                                            <CustomSelect
                                                className={""}
                                                // items={values.filteredcities || filteredcities || cities}
                                                items={this.state.filteredcities || this.state.cities}
                                                // value={values.cityId || ''}
                                                // onChange={(selected) => setFieldValue("cityId", selected.value)}
                                                value={values.cityId || ''}
                                                onChange={(selected) => setFieldValue("cityId", selected.value)}
                                            />
                                            <ErrorMessage name="cityId" component="div"
                                                className="alert alert-warning inline-4" />

                                        </fieldset>

                                    }
                                    <fieldset className="form-group w100">
                                        <label className="required-field">email</label>
                                        <Field className="form-control" type="email" name="email"
                                            autoComplete="new-password" />
                                        <ErrorMessage name="email" component="div"
                                            className="alert alert-warning " />
                                    </fieldset>
                                    <fieldset className="form-group mt-5">
                                        <button className="btn btn-mybtn p-x-5" type="submit" disabled={!dirty}>Save</button>
                                        <button className="btn btn-mybtn btn-delete px-5 ml-5" type="button" onClick={this.cancelForm}>cancel</button>
                                    </fieldset>
                                   
                                </div>
                                <div className="inline w-50 ">
                                    
                                   
                                    {(this.state.id > 0 && !this.state.changingpassword) &&
                                        <button className="btn btn-mybtn mt-3 mb-3  d-flex" onClick={this.changePassword}>change password</button>}
                                    {(this.state.id > 0 && this.state.changingpassword) &&
                                        <button className="btn btn-mybtn mt-3 mb-3  d-flex" onClick={this.changePassword}>leave password</button>}
                                    {(this.state.id < 1 || this.state.changingpassword) &&
                                        <div>
                                            <fieldset className="form-group w90">
                                                <label className="required-field">password</label>
                                                <Field className="form-control " type="password" name="password"
                                                    autoComplete="new-password" />
                                                <ErrorMessage name="password" component="div"
                                                    className="alert alert-warning " />
                                            </fieldset>
                                            <fieldset className="form-group w90">
                                                <label className="required-field">confirm password</label>
                                                <Field className="form-control" type="password" name="confirmpassword"
                                                    autoComplete="new-password" />
                                                <ErrorMessage name="confirmpassword" component="div"
                                                    className="alert alert-warning" />
                                            </fieldset>
                                        </div>
                                    }
                                   
                               
                                </div>
                                </Form>
                            )
                        }
                    </Formik>
                </div>
        )
    }
}

export default UserComponent