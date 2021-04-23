import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import SupplierDataService from '../service/SupplierDataService';
import '../myStyles/Style.css';
import CustomSelect from './Filters/CustomSelect'
import Function from './Shared/Function';
import PhoneInput from 'react-phone-input-2'
import 'react-phone-input-2/lib/style.css'

class SupplierComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.match.params.id,
            name: '',
            phoneNumber: '',
            ddcnumber: '',
            email: '',          
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
            .then(response => {
                this.setState({
                    name: response.data.name||'',
                    phoneNumber: response.data.phoneNumber||'',
                    ddcnumber: response.data.ddcnumber||'',
                    email: response.data.email,
                })
            })
    }

    onSubmit(values, actions) {      
        let item = {
            id: this.state.id,
            name: values.name,
            phoneNumber: this.getTrimmedNumber(values.phoneNumber),
            ddcnumber: values.ddcnumber,
            email: values.email,
            targetDate: values.targetDate
        }
       SupplierDataService.save(item)
            .then(() => this.props.history.push('/suppliers'))
            .catch(error => {              
                let msg = Function.getErrorMsg(error);                  
               if (msg.indexOf("phone") > -1)
                    actions.setErrors({ phoneNumber: msg }) 
                if (msg.indexOf("name") > -1)
                    actions.setErrors({ name: msg }) 
                if (msg.indexOf("DDC number") > -1)
                    actions.setErrors({ ddcnumber: msg }) 
                if (msg.indexOf("email") > -1)
                    actions.setErrors({ email: msg }) 
                this.setState({
                    errormsg: msg
                })
            } )
    }

    getTrimmedNumber(value) {
        if (value == null || value.length < 1) return;
       value = value.trim();
        value = value.replace(/ +/g, " ");
        return value;
    }

    validate(values) {

        let errors = {}
        if (!values.name) {
            errors.name = 'required field !!!'
        } else if (values.name.length < 5) {
            errors.userName = 'Enter atleast 5 Characters'
        } else if (values.name.length >50) {
            errors.userName = 'too Long Max is 50 charachters'
        }
        if (!values.ddcnumber) {
            errors.ddcnumber = 'required field !!!'
        } else if (values.ddcnumber.length < 4) {
            errors.ddcnumber = 'too short'
        } else if (values.ddcnumber.length > 15) {
            errors.ddcnumber = 'Enter max 15 Characters'
        }       
        return errors
    }

    cancelForm() {
        window.history.back();
    }

    render() {
        let { id, name, phoneNumber, ddcnumber, email } = this.state
        return (
            <div className="container pt-5">
                {this.state.id > 0 ? <h3 className="mb-3"> Update Supplier</h3> : <h3 className="mb-3"> Add New Supplier </h3>}
                <Formik
                    initialValues={{ id, name, phoneNumber, ddcnumber, email }}
                    onSubmit={(values, actions)=>this.onSubmit(values, actions)}
                    validateOnChange={false}
                    validateOnBlur={false}
                    validate={this.validate}
                    enableReinitialize={true}
                >
                    {({ setFieldValue, values, dirty }) => (
                            <Form>
                                {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                                <Field className="form-control " type="text" name="id" hidden />
                                <fieldset className="form-group w-50">
                                    <label className="required-field">name</label>
                                    <Field className="form-control " type="text" name="name" />
                                    <ErrorMessage name="name" component="div"
                                        className="alert alert-warning " />
                                </fieldset>
                                <fieldset className="form-group w-25">
                                    <label>phone number</label>
                                <PhoneInput
                                        country={'bg'}
                                        value={values.phoneNumber}
                                        onChange={(value, country, e, phone) => {                                            
                                            setFieldValue("phoneNumber", phone)
                                        }}
                                    />
                                </fieldset>
                                <fieldset className="form-group inline-3">
                                    <label className="required-field">DDC number</label>
                                    <Field className="form-control " maxlength="14" type="text" name="ddcnumber" />
                                    <ErrorMessage name="ddcnumber" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                <fieldset className="form-group w-50">
                                    <label>email</label>
                                    <Field className="form-control " type="email" name="email" />
                                    <ErrorMessage name="email" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                <fieldset className="form-group mt-5">  
                                <button className="btn btn-mybtn p-x-5" type="submit" disabled={!dirty}>Save</button>
                                    <button className="btn btn-mybtn btn-delete px-5 ml-5" type="button" onClick={this.cancelForm}>cancel</button>
                                </fieldset>
                            </Form>
                        )}
                </Formik>
            </div>
        )
    }
}

export default SupplierComponent