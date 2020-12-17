import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import AuthenticationService from '../service/AuthenticationService';

class LoginComponent extends Component {

    constructor(props) {
        super(props)

        this.state = {
            username: '',
            password: '',
            hasLoginFailed: false,
            showSuccessMsg: false
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

    loginClicked() {

      /*  if (this.state.username === 'aabi' && this.state.password === 'dummy') {
            AuthenticationService.registerSuccessfulLogin(this.state.username, this.state.password)
            this.setState({ showSuccessMsg: true })
            ths.setState({ hasLoginFailed: false })
        }
        else {
            this.setState({ showSuccessMsg: false })
            this.setState({ hasLoginFailed : true })
        }*/

        console.log('loginClicked' + this.state.username+ this.state.password)

        AuthenticationService
            .executeAuthentication(this.state.username, this.state.password)
            .then((response) => {
                console.log('response recieved '+response.data.token)
                this.setState({ showSuccessMsg: true })
                this.setState({ hasLoginFailed: false })
                AuthenticationService.registerSuccessfulLogin(this.state.username,response.data.token)
               // AuthenticationService.registerSuccessfullLogin(this.state.username,response.data.token)
                this.props.history.push('/courses')
            }).catch(() => {
                console.log('catched sth ')

        this.setState({ showSuccessMsg: false })
        this.setState({ hasLoginFailed : true })
        })           

    }

  /*  render() {

        let { username, password, hasLoginFailed, showSuccessMsg } = this.state

        return (
            <div>
                <h1>Login</h1>

                <div className="container">

                    {this.state.hasLoginFailed && <div className="alert alert-warning">Invalid user name and/or password</div>}
                    {this.state.showSuccessMsg && <div>Login Successfull</div>}

                    User Name:<br/>
                    <input type="text" name="username" value={this.state.username} onChange={this.handleChange} /><br/>
                    Password:<br/>
                    <input type="password" name="password" value={this.state.password} onChange={this.handleChange} /><br/>

                    <button className="btn btn-success" onClick={this.loginClicked}>Login</button>

                </div>

            </div>
            
            )
    }*/

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
            <div>
                <h3>Login</h3>
                <div className="container">

                    {this.state.hasLoginFailed && <div className="alert alert-warning">Invalid user name and/or password</div>}
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
                                        <Field className="form-control" type="text" name="username" value={this.state.username} onChange={this.handleChange} />
                                        <ErrorMessage name="username" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Password:</label>
                                        <Field className="form-control" type="password" name="password" value={this.state.password} onChange={this.handleChange} />
                                        <ErrorMessage name="password" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>

                                    <button className="btn btn-success" type="submit">Save</button>
                                </Form>
                            )
                        }
                    </Formik>

                </div>
            </div>
        )
    }
   

}

export default LoginComponent











