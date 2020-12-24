import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import SupplierDataService from '../service/SupplierDataService';
import '../myStyles/Style.css';

class SupplierComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.match.params.id,
            name: '',
            phoneNumber: '',
            ddcnumber: '',
            email: ''
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
       SupplierDataService.retrieve(this.state.id)
            .then(response =>

                this.setState({
                    name: response.data.name,
                    phoneNumber: response.data.phoneNumber,
                    ddcnumber: response.data.ddcnumber,
                    email: response.data.email
                })
            )
    }

    onSubmit(values) {
        let item = {
            id: this.state.id,
            name: values.name,
            phoneNumber: values.phoneNumber,
            ddcnumber: values.ddcnumber,
            email: values.email,
            targetDate: values.targetDate
        }
        console.log('item = ' + item);
       SupplierDataService.save(item)
            .then(() => this.props.history.push('/suppliers'))
    }

    validate(values) {
        let errors = {}
        if (!values.name) {
            errors.name = 'required field !!!'
        } else if (values.name.length < 5) {
            errors.userName = 'Enter atleast 5 Characters'
        }

        if (!values.ddcnumber) {
            errors.ddcnumber = 'required field !!!'
        } else if (values.ddcnumber.length < 11) {
            errors.email = 'Enter atleast 11 Characters'
        }

        if (!values.email) {
            errors.email = 'required field !!!'
        } else if (values.email.length < 4) {
            errors.email = 'Enter atleast 4 Characters'
        }

        return errors
    }

    cancelForm() {
        this.props.history.push('/suppliers')
    }

    render() {
        let { id, name, phoneNumber, ddcnumber, email } = this.state
        return (
            <div className="container">
                {this.state.id > 0 ? <h3 className="mb-3"> Update Supplier</h3> : <h3 className="mb-3"> Add New Supplier </h3>}
                <Formik
                    initialValues={{ id, name, phoneNumber, ddcnumber, email }}
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
                                    <label>name</label>
                                    <Field className="form-control w-25" type="text" name="name" />
                                    <ErrorMessage name="name" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                <fieldset className="form-group">
                                    <label>phone numbere</label>
                                    <Field className="form-control w-25" type="text" name="phoneNumber" />
                                    <ErrorMessage name="phoneNumber" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                <fieldset className="form-group">
                                    <label>DDC number</label>
                                    <Field className="form-control w-25" type="text" name="ddcnumber" />
                                    <ErrorMessage name="ddcnumber" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                <fieldset className="form-group">
                                    <label>email</label>
                                    <Field className="form-control w-50" type="email" name="email" />
                                    <ErrorMessage name="email" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
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

export default SupplierComponent