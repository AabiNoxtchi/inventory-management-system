import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';

import UserDataService from '../service/UserDataService';


class UserComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            firstName: '',
            lastName: '',
            userName: '',
            email: ''
        }

        this.onSubmit = this.onSubmit.bind(this)
        this.validate = this.validate.bind(this)

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
            userName: values.userName,
            email: values.email,
            targetDate: values.targetDate
        }

       
            UserDataService.save(user)
                .then(() => this.props.history.push('/users'))
       

        console.log(values);
    }

    validate(values) {

        let errors = {}

        if (!values.userName) {
            errors.userName = 'required field !!!'
        } else if (values.userName.length < 4) {
            errors.userName = 'Enter atleast 4 Characters'
        }

        if (!values.email) {
            errors.email = 'required field !!!'
        } else if (values.email.length < 4) {
            errors.email = 'Enter atleast 4 Characters'
        }

        return errors
    }

   

    render() {

        let { id, firstName, lastName, userName, email } = this.state

        return (
            <div>
                <h3>User</h3>
                <div className="container">
                    <Formik
                        initialValues={{ id, firstName, lastName, userName, email}}
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
                                        <Field className="form-control" type="text" name="firstName" />
                                        <ErrorMessage name="firstName" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>

                                    <fieldset className="form-group">
                                        <label>last name</label>
                                        <Field className="form-control" type="text" name="lastName" />
                                        <ErrorMessage name="lastName" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>

                                    <fieldset className="form-group">
                                        <label>user name</label>
                                        <Field className="form-control" type="text" name="userName" />
                                        <ErrorMessage name="userName" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>

                                    <fieldset className="form-group">
                                        <label>email</label>
                                        <Field className="form-control" type="text" name="email" />
                                        <ErrorMessage name="email" component="div"
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

export default UserComponent