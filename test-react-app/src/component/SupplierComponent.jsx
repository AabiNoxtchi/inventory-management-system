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
            //phoneCodes: [],
           // defaultCodeValue: '',
            //selectedCode:''
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
               // console.log("response = " + JSON.stringify(response));
                this.setState({
                    name: response.data.name||'',
                    phoneNumber: response.data.phoneNumber||'',
                    ddcnumber: response.data.ddcnumber||'',
                    email: response.data.email,
                   // phoneCodes: response.data.phoneCodes,
                   // defaultCodeValue: response.data.defaultCodeValue
                })
            })
    }

    onSubmit(values, actions) {
       /* if (values.phoneNumber.length > 1) {
            let phone = this.getPhone();           
        }*/
        let item = {
            id: this.state.id,
            name: values.name,
            phoneNumber: this.getTrimmedNumber(values.phoneNumber),//.length > 1 ? this.getCode() + "/" + values.phoneNumber : null,
            ddcnumber: values.ddcnumber,
            email: values.email,
            targetDate: values.targetDate
        }
        console.log('item = ' + JSON.stringify(item));
       SupplierDataService.save(item)
            .then(() => this.props.history.push('/suppliers'))
            .catch(error => {
               // console.log("error" + error);
                console.log("error.response.data = " + JSON.stringify(error.response.data))
                //console.log("error.response.data.errors = " + JSON.stringify(error.response.data.errors))
                //console.log("error.response.data.errors[0].codes) = " + JSON.stringify(error.response.data.errors[0].codes))
               // console.log("error.response.data.errors[0].defaultmsg) = " + error.response.data.errors[0].defaultMessage)
                let msg = Function.getErrorMsg(error);
                   /* "" + error.response && typeof error.response.data == 'string' ?
                    error.response.data : error.response.data.errors ?
                        error.response.data.errors[0].defaultMessage : error.response.data.message ?
                            error.response.data.message : error;*/
               // console.log("msg = " + msg)
                //console.log("msg.indexOf nop = " + (msg.indexOf("nop")))
                //console.log("msg.indexOf phone  > 0 = " + (msg.indexOf("phone") > 0))
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

   /* getCode() {
        let code = (this.state.defaultCodeValue.name || this.state.defaultCodeValue.label);
        code = code.substring(code.indexOf("/")+1, code.length);
        return code;
    }*/

    validate(values) {

        console.log("values = " + JSON.stringify(values));
       /* console.log("(validating values = ");
        console.log("(values.phoneNumber.length = " + (values.phoneNumber.length));
        console.log("(values.phoneNumber.length > 1 = " + (values.phoneNumber.length > 1));
        console.log("(values.phoneNumber[0] = " + (values.phoneNumber[0]));
        console.log("(values.phoneNumber[0]==0 = " + (values.phoneNumber[0] == "0"));*/


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

       /* if (!values.email) {
            errors.email = 'required field !!!'
        } else if (values.email.length < 4) {
            errors.email = 'Enter atleast 4 Characters'
        }*/

       /* if (values.phoneNumber&&values.phoneNumber.length > 1 && values.phoneNumber[0]=="0") {
            errors.phoneNumber = 'phone number must be without leading 0 !!!'
        } else if (values.phoneNumber.length > 15)
            errors.phoneNumber = 'phone number too long !!!'*/

        return errors
    }

    cancelForm() {
       // this.props.history.push('/suppliers')
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
                    {
                        ({ setFieldValue, values, dirty }) => (
                            <Form>
                                {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                                <Field className="form-control " type="text" name="id" hidden />
                                <fieldset className="form-group w-50">
                                    <label className="required-field">name</label>
                                    <Field className="form-control " type="text" name="name" />
                                    <ErrorMessage name="name" component="div"
                                        className="alert alert-warning " />
                                </fieldset>
                                {/* <fieldset className="d-flex">*/}
                                    {/*  <fieldset className="form-group inline w-25">
                                    <label>country/code</label>
                                        <CustomSelect
                                            style={{overflow:"auto"}}
                                        className=""
                                        items={this.state.phoneCodes}
                                            value={this.state.defaultCodeValue.value}
                                            onChange={(value) => {
                                                this.setState({
                                                    defaultCodeValue: value,
                                                    //selectedCode: value.label.substring(value.label.indexOf('+'), value.label.length)
                                                })
                                            }}
                                        />
                                        <ErrorMessage name="phoneCode" component="div"
                                            className="alert alert-warning" />
                                </fieldset>*/}
                                    {/* <fieldset className="form-group inline">
                                        <label>code</label>
                                        <p className="form-group mt-2 border-bottom">+{this.state.defaultCodeValue}</p>
                                    </fieldset>*/}
                                {/*  <div className="d-flex align-items-top">
                                <fieldset className="form-group inline w-25 p-0">
                                    <label>phone number</label>
                                    <Field className="form-control" type="text" name="phoneNumber" />
                                    <ErrorMessage name="phoneNumber" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                {values.phoneNumber.length > 0 && <p className="inline ml-5" style={{
                                    fontSize: "60%"
                                }}>
                                    phone number patterns :<br />
                                    0phone number<br />
                                    phone number<br/>
                                    +code/phone number<br />
                                    +codephone number
                                </p>}
                                </div>*/}

                                <fieldset className="form-group w-25">
                                    <label>phone number</label>
                                <PhoneInput
                                        country={'bg'}
                                        value={values.phoneNumber}
                                        onChange={(value, country, e, phone) => {
                                            //console.log(phone.target.value);
                                            //console.log("country code = "+Data.)
                                           // console.log("phone = " + phone);
                                            setFieldValue("phoneNumber", phone)
                                        }
                                        }
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
                                {//console.log("values = " + JSON.stringify(values))
                                }
                                <fieldset className="form-group mt-5">  
                                <button className="btn btn-mybtn p-x-5" type="submit" disabled={!dirty}>Save</button>
                                    <button className="btn btn-mybtn btn-delete px-5 ml-5" type="button" onClick={this.cancelForm}>cancel</button>
                                </fieldset>
                            </Form>
                        )
                    }
                </Formik>
            </div>
        )
    }
}

export default SupplierComponent