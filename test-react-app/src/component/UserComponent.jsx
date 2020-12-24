import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import UserDataService from '../service/UserDataService';
import '../myStyles/Style.css';

class UserComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.match.params.id,
            firstName: '',
            lastName: '',
            userName: '',
            email: '',
            password: '',
            confirmpassword: '',
            changingpassword: false
        }
        this.onSubmit = this.onSubmit.bind(this)
        this.validate = this.validate.bind(this)
        this.cancelForm = this.cancelForm.bind(this)
    }

    componentDidMount() {
        console.log(this.state.id)
        if (this.state.id === -1) {
            return
        }
       UserDataService.retrieve( this.state.id)
            .then(response =>

                this.setState({
                    firstName: response.data.firstName,
                    lastName: response.data.lastName,
                    userName: response.data.userName,
                    email: response.data.email
                })
            )
    }

    onSubmit(values) {
        let user = {
            id: this.state.id,
            firstName: values.firstName,
            lastName: values.lastName,
            username: values.userName,
            email: values.email,
            password: this.state.changingpassword ? values.password : null,
            targetDate: values.targetDate
        }       
            UserDataService.save(user)
                .then(() => this.props.history.push('/users'))       
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

        return errors
    }

   cancelForm() {
       this.props.history.push('/users')
    }

    changePassword = () => {
        this.setState({
            changingpassword: this.state.changingpassword ? false : true
        })
    }

    render() {
        let { id, firstName, lastName, userName, email, password , confirmpassword } = this.state
        return (
            <div className="container">
                {this.state.id > 0 ? <h3 className="mb-3"> Update User</h3> : <h3 className="mb-3"> Add New User </h3>}
                <Formik
                    initialValues={{ id, firstName, lastName, userName, email, password, confirmpassword }}
                        onSubmit={this.onSubmit}
                        validateOnChange={false}
                        validateOnBlur={false}
                        validate={this.validate}
                        enableReinitialize={true}
                    >
                        {
                            (props) => (
                                <Form>                                   
                                    <Field className="form-control" type="text" name="id" hidden />
                                    <fieldset className="form-group">
                                        <label>first name</label>
                                        <Field className="form-control w-25" type="text" name="firstName" />
                                        <ErrorMessage name="firstName" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>last name</label>
                                        <Field className="form-control w-25" type="text" name="lastName" />
                                        <ErrorMessage name="lastName" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>user name</label>
                                        <Field className="form-control w-25" type="text" name="userName" />
                                        <ErrorMessage name="userName" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>email</label>
                                        <Field className="form-control w-50" type="email" name="email" />
                                        <ErrorMessage name="email" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>

                                    {(this.state.id > 0 && !this.state.changingpassword) &&
                                        <button className="btn btn-mybtn mt-5 mb-5 d-flex" onClick={this.changePassword}>change password</button>}
                                    {(this.state.id > 0 && this.state.changingpassword) &&
                                        <button className="btn btn-mybtn mt-5 mb-5 d-flex" onClick={this.changePassword}>leave password</button>}
                                    {(this.state.id < 1 || this.state.changingpassword) &&
                                        <div>
                                            <fieldset className="form-group">
                                                <label>password</label>
                                            <Field className="form-control w-50" type="password" name="password" />
                                                <ErrorMessage name="password" component="div"
                                                    className="alert alert-warning" />
                                            </fieldset>
                                            <fieldset className="form-group">
                                                <label>confirm password</label>
                                            <Field className="form-control w-50" type="password" name="confirmpassword" />
                                                <ErrorMessage name="confirmpassword" component="div"
                                                    className="alert alert-warning" />
                                            </fieldset>
                                        </div>
                                    }

                                    <button className="btn btn-mybtn px-5" type="submit">Save</button>
                                    <button className="btn btn-mybtn btn-delete px-5 ml-5" type="button" onClick={this.cancelForm}>cancel</button>
                                </Form>
                            )
                        }
                    </Formik>
                </div>
        )
    }
}

export default UserComponent