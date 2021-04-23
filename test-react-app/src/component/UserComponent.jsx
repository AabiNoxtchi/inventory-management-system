import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import UserDataService from '../service/UserDataService';
import '../myStyles/Style.css';
import AuthenticationService from '../service/AuthenticationService';
import CustomSelect from './Filters/CustomSelect';
import Function from './Shared/Function'


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
            cityId: '',
            register: null,
            waitmsg: null
        }
        this.onSubmit = this.onSubmit.bind(this)
        this.validate = this.validate.bind(this)
        this.cancelForm = this.cancelForm.bind(this)
    }

    componentDidMount() {
        console.log("did mount user component");
        let userRole = this.getUserRole();
        //let userId = AuthenticationService.getLoggedUerId();
       /* console.log("component mount userRole  = " + (userRole))
        console.log("userRole !== 'ROLE_Admin'  = " + (userRole !== 'ROLE_Admin'))
        console.log("( this.state.id === -1)  = " + (this.state.id === -1))
        console.log("( this.state.id == -1)  = " + (this.state.id == -1))
       
        console.log("this.state.id ="+this.state.id)
        console.log("(this.state.id === -1 && userRole !== 'ROLE_Admin') = " + ((this.state.id === -1 && userRole !== 'ROLE_Admin')));*/
        //console.log("(this.state.id == -1 && userRole === 'ROLE_Mol') || userRole === 'ROLE_Employee' =" + ((this.state.id == -1 && userRole === 'ROLE_Mol') || userRole === 'ROLE_Employee'))
        if ((this.state.id == -1 && userRole === 'ROLE_Mol')) { //|| userRole === 'ROLE_Employee' ) {
            return
        }
        UserDataService.retrieve(this.state.id)
            .then(response => {
              //  console.log("response = " + JSON.stringify(response));
                this.setState({
                   // userRole: userRole,
                    firstName: response.data.firstName||'',
                    lastName: response.data.lastName||'',
                    userName: response.data.userName||'',
                    email: response.data.email||'',
                    countries: /*userRole === 'ROLE_Admin' */ response.data.countries || null,
                    cities: /*userRole === 'ROLE_Admin' ?*/ response.data.cities || null,
                    filteredcities: /*userRole === 'ROLE_Admin' ?*/ response.data.countryId ? 
                        this.filterCities([], response.data.cities, response.data.countryId) : response.data.cities || null,
                    countryId: /*userRole === 'ROLE_Admin' && response.data.countryId ? */response.data.countryId || null,
                    cityId: /*userRole === 'ROLE_Admin' && response.data.cityId ?*/ response.data.cityId+'' || ''
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
            cityId: /*this.getUserRole() === 'ROLE_Admin' ?*/this.state.cities != null && values.newCity == null ? values.cityId : null,
            newCity: this.state.cities != null && values.newCity && values.newCity != 'undefined' ? values.newCity : null,
            countryId: this.state.cities != null && values.newCity && values.newCity != 'undefined' ? this.state.countryId : null,

            targetDate: values.targetDate
        }
       // console.log("user to update = " + JSON.stringify(user))
        UserDataService.save(user)
            .then((response) => {
                if (response.data.refreshToken) {
                    AuthenticationService.setRegister(response.data);
                    //this.props.history.push('/home')
                } else if (typeof response.data == 'string') {
                    this.setState({
                        register: true,
                        waitmsg : response.data

                    })
                }

                if (!this.state.register) {

                    if ((this.getUserRole() == 'ROLE_Admin' || this.getUserRole() == 'ROLE_Mol') && this.state.id != AuthenticationService.getLoggedUerId)
                        this.props.history.push('/users');
                    else this.props.history.push('/home')
                }

                
            
    })
            .catch(error => {
                console.log("error = " + error);
                //console.log("error.response.data = " + JSON.stringify(error.response.data))
                //let msg = error.response && typeof error.response.data == 'string' ? error.response.data :
                //  error.response.data.message ? error.response.data.message : error;
                let msg = Function.getErrorMsg(error);
                console.log("msg = " + msg);
                    /*"" + error &&
                    // error.response && error.response.data && typeof error.response.data == 'string' ?
                    //error.response.data :
                    error.response && error.response.data ?
                    error.response.data.errors ?
                        error.response.data.errors[0].defaultMessage : error.response.data.message ?
                            error.response.data.message : 
                                    error : 'something went wrong !!!';*/
                console.log('msg.indexOf("user name") > -1 = ' + (msg.indexOf("user name") > -1));

                if (msg.indexOf("email") > -1)
                    actions.setErrors({ email: msg })
                if (msg.indexOf("user name") > -1)
                    actions.setErrors({ userName: msg })
                if (msg.indexOf("Password") > -1)
                    actions.setErrors({ Password: msg })
                this.showError(msg);
                
               
    })
    }

   
    showError(msg) {
        let time = 10;
        this.setState({
            errormsg: msg,
        })
        this.myInterval = setInterval(() => {
            time = time - 1;
            if (time == 0) {
                this.setState(({ errormsg }) => ({
                    errormsg: null
                }))
                clearInterval(this.myInterval)
            }
        }, 1000)
    }

    componentWillUnmount() {
        clearInterval(this.myInterval)
    }

    validate(values) {
        console.log("values = " + JSON.stringify(values));
        console.log("userRole = " + this.getUserRole());
        console.log("new city = " + values.newCity);
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

        // if (this.getUserRole() === 'ROLE_Admin' && !values.cityId) {
        if (this.state.cities != null && this.getUserRole() != '' && (!values.cityId || values.cityId == 'undefined')) {
            errors.cityId = 'required field !!!'           
        }

        if (this.getUserRole() == '' && (!values.cityId || values.cityId == 'undefined') && (!values.newCity || values.newCity == 'undefined')) {
            let error = "either choose city or write your city name !!!";
            errors.cityId = "either choose city or write your city name !!!";
            errors.newCity = "either choose city or write your city name !!!";
        }

        if (values.newCity && (!this.state.countryId || this.state.countryId == 'undefined')) {
            errors.country = 'required field !!!'
        }

        if (values.newCity && values.newCity.length < 1) {
            errors.newCity = 'too short !!!'
        }

        console.log("errors = " + JSON.stringify(errors))
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

        if (!cities) return null;
        if (!value || value == 'undefined') return cities;
        subs = []
        for (let i = 0; i < cities.length; i++) {

            if (cities[i].filterBy == value) {
                subs.push(cities[i])
            }
        }

        return subs
    }

    render() {
        console.log("rendering user")
        const userRole = this.getUserRole();
        let { id, firstName, lastName, userName, email, password, confirmpassword,/* countries, cities, filteredcities,*/ country, cityId } = this.state
        let newCity = null;
        return (
            <div className="container pt-5">
                <h3 className="mb-3"> {
                    userRole == '' ? "register " :
                    this.state.id > 0 && this.state.id == AuthenticationService.getLoggedUerId() ?  "update profile" :
                            this.state.id > 0 ? "Update User" : "Add New User"}</h3>
                {this.state.register && this.state.waitmsg && 
                    <>
                        <p>{this.state.waitmsg}</p>

                    </>}
                {this.state.register == null &&
                    <Formik
                        initialValues={{ id, firstName, lastName, userName, email, password, confirmpassword,/* countries, cities, filteredcities,*/newCity, country, cityId }}
                        onSubmit={(values, actions) => this.onSubmit(values, actions)}
                        validateOnChange={false}
                        validateOnBlur={false}
                        validate={this.validate}
                        enableReinitialize={true}
                    >
                    {
                        ({ setFieldValue, values, dirty, errors }) => (
                                <Form className="d-flex flex-wrap">
                                    {this.state.errormsg && <div className="alert alert-warning" style={{ width: "100%" }}>{this.state.errormsg}</div>}
                                    {//console.log("values = " + JSON.stringify(values))
                                    }

                                    <div className="inline w50">
                                        <Field className="form-control" type="text" name="id" hidden />

                                        <fieldset className="form-group w90">
                                            <label>first name</label>
                                            <Field className="form-control" type="text" name="firstName"
                                                autoComplete="new-password" />
                                            <ErrorMessage name="firstName" component="div"
                                                className="alert alert-warning" />
                                        </fieldset>

                                       

                                        <fieldset className="form-group w90">
                                            <label className="required-field">user name</label>

                                            <Field className="form-control " type="text" name="userName"
                                                autoComplete="new-password" />
                                            <ErrorMessage name="userName" component="div"
                                                className="alert alert-warning " />
                                        </fieldset>

                                        <fieldset className="form-group w90">
                                            <label className="required-field">email</label>
                                            <Field className="form-control" type="email" name="email"
                                                autoComplete="new-password" />
                                            <ErrorMessage name="email" component="div"
                                                className="alert alert-warning " />
                                        </fieldset>

                                        <div className="w100">
                                            {//userRole === 'ROLE_Admin' &&
                                                this.state.countries &&

                                                <fieldset className="inline form-group pl-0 w70">

                                                    <label className={this.state.addnewCity ? "required-field" : ""}>country&nbsp;</label>
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
                                                                filteredcities: subs

                                                            })
                                                        }}
                                                    />
                                                    <ErrorMessage name="country" component="div"
                                                        className="alert alert-warning w70" />

                                                </fieldset>

                                            }

                                        </div>
                                        <div className="w100">
                                            {//userRole === 'ROLE_Admin' &&
                                                !this.state.addnewCity &&
                                                this.state.cities &&

                                                <fieldset className="inline form-group w70 pl-0">

                                                    <label className="required-field">city</label>
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
                                                        className="alert alert-warning " />

                                                </fieldset>

                                            }
                                            {this.state.addnewCity &&
                                                <fieldset className="inline form-group w70 pl-0">
                                                <label className="required-field">city name</label>
                                                <p style={{ fontSize: "60%" }}>
                                                            if you can't find your city, please write it down,<br />
                                                            you can relate to any major city near you as long as it's in the same time zone<br />
                                                            to make your calculations accurate, Thank you.<br />

                                                        </p>

                                                    <Field className="form-control " type="text" name="newCity"
                                                        autoComplete="new-password" />
                                                    <ErrorMessage name="newCity" component="div"
                                                        className="alert alert-warning w70" />
                                                </fieldset>
                                            }
                                        </div>
                                        <div className="w100">

                                            {this.state.cities && userRole == '' && !this.state.addnewCity &&
                                                <button className="btn btn-mybtn" type="button" onClick={() => this.setState({ addnewCity: true })/*setFieldValue("addnewCity", true)*/}>new city</button>}
                                            {this.state.cities && userRole == '' && this.state.addnewCity &&
                                                <button className="btn btn-mybtn" type="button"
                                                    onClick={() => { this.setState({ addnewCity: false }); setFieldValue("newCity", null) }}>back</button>}

                                        </div>


                                    <fieldset className="form-group mt-5">
                                        <button className="btn btn-mybtn p-x-5" type="submit" disabled={!dirty}>Save</button>
                                            <button className="btn btn-mybtn btn-delete px-5 ml-5" type="button" onClick={this.cancelForm}>cancel</button>
                                        </fieldset>

                                    </div>
                                    <div className="inline w-50 ">
                                        <fieldset className="form-group w90">
                                            <label>last name</label>
                                            <Field className="form-control" type="text" name="lastName"
                                                autoComplete="new-password" />
                                            <ErrorMessage name="lastName" component="div"
                                                className="alert alert-warning" />
                                        </fieldset>

                                        {(this.state.id > 0 && !this.state.changingpassword) &&
                                            <button className="btn btn-mybtn mt-5 mb-3  d-flex" onClick={this.changePassword}>change password</button>}
                                        {(this.state.id > 0 && this.state.changingpassword) &&
                                            <button className="btn btn-mybtn mt-5 mb-3  d-flex" onClick={this.changePassword}>leave password</button>}
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
                }
                </div>
        )
    }
}

export default UserComponent