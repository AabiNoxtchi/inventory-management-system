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
        AuthenticationService
            .executeAuthentication(this.state.username, this.state.password)
            .then((response) => {
                this.setState({ showSuccessMsg: true })
                this.setState({ hasLoginFailed: false })
                AuthenticationService.registerSuccessfulLogin(this.state.username, response.data.token, response.data.role)
                this.props.history.push('/courses')
            }).catch(() => {
        this.setState({ showSuccessMsg: false })
        this.setState({ hasLoginFailed : true })
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











