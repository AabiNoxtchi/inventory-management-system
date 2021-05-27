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
        let msg = Function.getErrorMsg(error);
        if (msg.startsWith('Error : Unauthorized,'))
            msg = 'Invalid user name and/or password';       
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
                this.setState(() => ({
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
                AuthenticationService.registerSuccessfulLogin(
                    this.state.username, response.data.token, userRole, response.data.id);              
                this.props.onLogin(true);
              
                if (userRole == "ROLE_Employee") this.props.history.push('/userprofiles');
                else this.props.history.push('/home');                
            }).catch((error) => {
                this.setState({
                    showSuccessMsg: false,
                    hasLoginFailed: true,
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
        return (
            <div className="container pt-5">
                <h3 className="mb-3">Login</h3>
                {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                    {this.state.showSuccessMsg && <div>Login Successfull</div>}
                    <Formik
                        initialValues={{ username, password }}
                        onSubmit={this.loginClicked}
                        validateOnChange={false}
                        validateOnBlur={false}
                        validate={this.validate}
                        enableReinitialize={true}
                    >{(props) => (
                        <Form>
                            <fieldset className="form-group">
                                <label>User Name:</label>
                                <Field className="form-control w-25"
                                    type="text" name="username"
                                    value={this.state.username}
                                    onChange={this.handleChange} />
                                <ErrorMessage name="username" component="div"
                                    className="alert alert-warning" />
                            </fieldset>
                            <fieldset className="form-group">
                                <label>Password:</label>
                                <Field className="form-control w-25"
                                    type="password" name="password"
                                    value={this.state.password}
                                    onChange={this.handleChange} />
                                <ErrorMessage name="password" component="div"
                                    className="alert alert-warning" />
                            </fieldset>
                            <button className="btn btn-mybtn p-x-5 mt-3" type="submit">Login</button>
                        </Form>                               
                            )}
                    </Formik>
            </div>
        )}
}

export default LoginComponent











