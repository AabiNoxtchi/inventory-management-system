import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import AuthenticationService from '../service/AuthenticationService';
import '../myStyles/Style.css';

class LoginComponent extends Component {

    constructor(props) {
        super(props)
        this.state = {
            username: '',
            password: '',
            hasLoginFailed: false,
            showSuccessMsg: false,
            errormsg:null
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
        if (''+error == 'Error: Request failed with status code 401' )
            return 'Invalid user name and/or password'
        else return '' + error
    }

    loginClicked() {
        AuthenticationService
            .executeAuthentication(this.state.username, this.state.password)
            .then((response) => {
                this.setState({ showSuccessMsg: true })
                this.setState({ hasLoginFailed: false })
                AuthenticationService.registerSuccessfulLogin(this.state.username, response.data.token, response.data.role)
                let userRole = AuthenticationService.getLoggedUerRole();
                console.log("user role = " + userRole);
                console.log("user role == 'ROLE_Employee'" + (userRole == 'ROLE_Employee'));
               /* switch (userRole) {
                    case 'ROLE_Employee':
                        this.props.history.push('/userprofiles');
                    default:
                         this.props.history.push('/courses');
                }*/
                this.props.history.push('/userprofiles');
                
            }).catch((error) => {
                console.log('error = ' + error);
                this.setState({
                    showSuccessMsg: false,
                    hasLoginFailed: true,
                    errormsg: this.getErrorMsg(error)
                })
        })           
    }

    validate(values) {
        let errors = {}
        if (!values.username) {
            errors.username = 'Enter user name'
        } else if (values.username.length < 3) {
            errors.username = 'Enter atleast 3 Characters for user name'
        }
        if (!values.password) {
            errors.password = 'Enter password'
        } else if (values.password.length < 6) {
            errors.password = 'Enter atleast 6 Characters for password'
        }
        return errors
    }

    render() {
        let { username, password } = this.state
        return (
           
               
            <div className="container">
                <h3 className="mb-3">Login</h3>
                    {this.state.hasLoginFailed && <div className="alert alert-warning">{this.state.errormsg || 'Invalid user name and/or password'}</div>}
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

                                    <button className="btn btn-mybtn px-5" type="submit">Login</button>
                                </Form>
                            )
                        }
                    </Formik>
                </div>
           
        )
    }
}

export default LoginComponent











