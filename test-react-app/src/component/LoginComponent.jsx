import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import AuthenticationService from '../service/AuthenticationService';
import '../myStyles/Style.css';
import Function from './Shared/Function'


class LoginComponent extends Component {

    constructor(props) {
        super(props)
        this.state = {
            username: '',
            password: '',
            hasLoginFailed: false,
            showSuccessMsg: false,
            errormsg: null,
            isLoggedIn: props.isLoggedIn
        }
        this.handleChange = this.handleChange.bind(this)
        this.loginClicked = this.loginClicked.bind(this)
        this.validate = this.validate.bind(this)
    }

    handleChange(event) {
        this.setState({
            [event.target.name] : event.target.value
        })
    }

    getErrorMsg = (error) => {
       // if (''+error == 'Error: Request failed with status code 401' )
       //     return 'Invalid user name and/or password'
       // else return '' + error
        let msg = Function.getErrorMsg(error);
        if (msg.startsWith('Error : Unauthorized,'))
            msg = 'Invalid user name and/or password';
        //this.showError(msg, 10) 
        return msg;
    }

    showError(msg,time) {
        time = time || 10;
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

    loginClicked() {
        AuthenticationService
            .executeAuthentication(this.state.username, this.state.password)
            .then((response) => {
                this.setState({ showSuccessMsg: true })
                this.setState({ hasLoginFailed: false })
                let userRole = response.data.role;
                AuthenticationService.registerSuccessfulLogin(this.state.username, response.data.token, userRole, response.data.id);
               // console.log("type of onLogin = " + typeof this.props.isLoggedIn);
                //console.log(" onLogin != null = " + (this.props.isLoggedIn != null));
                //console.log(" state onLogin != null = " + (this.state.isLoggedIn != null));
                this.props.onLogin(true);
               // this.setState({ isUserLoggedIn: AuthenticationService.isUserLoggedIn() })
               // console.log("onlogin = "+this.props.onLogin);
               // let userRole = AuthenticationService.getLoggedUerRole();
               
              //  console.log("user role = " + userRole);
              //  console.log("user role == 'ROLE_Employee'" + (userRole == 'ROLE_Employee'));
               /* switch (userRole) {
                    case 'ROLE_Employee':
                        this.props.history.push('/userprofiles');
                    default:
                         this.props.history.push('/courses');
                }*/
                if (userRole == "ROLE_Employee") this.props.history.push('/userprofiles');
               // if (userRole == "ROLE_Admin") this.props.history.push('/users');
                else this.props.history.push('/home');
                
            }).catch((error) => {
                console.log('error = ' + error);
                this.setState({
                    showSuccessMsg: false,
                    hasLoginFailed: true,
                   // errormsg: this.getErrorMsg(error)
                })
                this.showError(this.getErrorMsg(error))
        })           
    }

    validate(values) {
        let errors = {}
        if (!values.username) {
            errors.username = 'Enter user name'
        } else if (values.username.length < 3) {
            errors.username = 'Enter at least 3 Characters for user name'
        }
        if (!values.password) {
            errors.password = 'Enter password'
        } else if (values.password.length < 6) {
            errors.password = 'Enter at least 6 Characters for password'
        }
        return errors
    }

    render() {
        let { username, password } = this.state;
       // let isUserLoggedIn = AuthenticationService.isUserLoggedIn();
        console.log("rendering log in component");
       // console.log("is user logged in = " + isUserLoggedIn);
        return (
           
               
            <div className="container pt-5">
               
               
                <h3 className="mb-3">Login</h3>
                {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg /*|| 'Invalid user name and/or password'*/}</div>}
                    {this.state.showSuccessMsg && <div>Login Successfull</div>}
                    <Formik
                        initialValues={{ username, password }}
                        onSubmit={this.loginClicked}
                        validateOnChange={false}
                        validateOnBlur={false}
                        validate={this.validate}
                        enableReinitialize={true}
                    >
                        {
                            (props) => (
                                <Form>
                                    <fieldset className="form-group">
                                        <label>User Name:</label>
                                        <Field className="form-control w-25" type="text" name="username" value={this.state.username} onChange={this.handleChange} />
                                        <ErrorMessage name="username" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Password:</label>
                                        <Field className="form-control w-25" type="password" name="password" value={this.state.password} onChange={this.handleChange} />
                                        <ErrorMessage name="password" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>

                                    <button className="btn btn-mybtn p-x-5 mt-3" type="submit">Login</button>
                                </Form>
                            )
                        }
                    </Formik>
                </div>
           
        )
    }
}

export default LoginComponent











