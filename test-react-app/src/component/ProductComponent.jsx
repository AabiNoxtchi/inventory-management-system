import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import ProductDataService from '../service/ProductDataService';
import '../myStyles/Style.css';
import CustomSelect from './Filters/CustomSelect';
import Function from './Shared/Function'


class ProductComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.match.params.id,
            name: '',
            description: '',
            productType: '',
            productTypes: [],           
            userCategoryId: '',          
            userCategories: [],
            filteredUserCategories:[],
            amortizationPercent: '',
        }
        this.onSubmit = this.onSubmit.bind(this)
        this.validate = this.validate.bind(this)
        this.cancelForm = this.cancelForm.bind(this)
    }

    componentDidMount() {       
        ProductDataService.retrieve(this.state.id)
            .then(response => {
                if (this.state.id > 0) {
                    this.setState({
                        name: response.data.name||'',
                        description: response.data.description||'',
                        productType: response.data.productType||'',
                        userCategoryId: response.data.userCategoryId||'',//response.data.productType == 'DMA' ? response.data.subCategory : '',
                        amortizationPercent: response.data.amortizationPercent||''
                    })
                }
                this.setState({
                    productTypes: response.data.productTypes||[],
                    userCategories: response.data.userCategories||[],
                    filteredUserCategories: response.data.userCategories||[],
                });
            })
    }

    onSubmit(values, actions) {
        let item = {
            id: this.state.id,
            name: values.name,
            description: values.description,
            userCategoryId: /*values.productType == 'DMA' ?*/ values.userCategoryId,//: null,
            targetDate: values.targetDate
        }
        ProductDataService.save(item)
            .then(() => this.props.history.push('/products'))
            .catch(error =>{              
                let msg = Function.getErrorMsg(error);
                actions.setErrors({ phoneNumber: msg })
                if (msg.indexOf("name") > -1)
                    actions.setErrors({ name: msg }) 
                this.setState({
                    errormsg: msg
                })
            })
    }

    validate(values) {
        let errors = {}
        if (!values.name) {
            errors.name = 'required field !!!'
        } else if (values.name.length < 5) {
            errors.name = 'Enter atleast 5 Characters'
        }
        if (!values.userCategoryId) {
            errors.userCategoryId = 'required field !!!'
        }                      
        return errors
    }

    cancelForm() {
        window.history.back();
    }

    render() {
        const precent = "%";
        let { id, name, description,  userCategoryId} = this.state
        return (
            <div className="container pt-5">
                {this.state.id > 0 ? <h3 className="mb-3"> Update Product</h3> : <h3 className="mb-3"> Add New Product </h3>}
                <Formik
                    initialValues={{ id, name, description, userCategoryId}}
                    onSubmit={(values, actions) => this.onSubmit(values, actions)}
                    validateOnChange={false}
                    validateOnBlur={false}
                    validate={this.validate}
                    enableReinitialize={true}
                >
                    {({ setFieldValue, values, dirty}) => (
                            <Form>
                                {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                                <Field className="form-control" type="text" name="id" hidden />
                                <fieldset className="form-group">
                                    <label className="required-field">name</label>
                                    <Field className="form-control w-50" type="text" name="name"
                                   />
                                    <ErrorMessage name="name" component="div"
                                        className="alert alert-warning  w-25" />
                                </fieldset>
                                
                                <fieldset className="form-group">
                                    <label>product type</label>
                                    {this.state.productTypes.map((type) =>
                                            <label className="mx-3">
                                                <Field
                                                    className="mx-1"
                                                    type="radio" name="productType" value={type.value}
                                                    checked={this.state.productType == type.value}
                                                    onChange={(value) => {                                                        
                                                        this.setState({ productType: value.target.value });                                                       
                                                        let subs = [];
                                                        for (let i = 0; i < this.state.userCategories.length; i++) {
                                                            if (this.state.userCategories[i].category.productType == value.target.value) {
                                                                subs.push(this.state.userCategories[i])
                                                            }
                                                        }
                                                        this.setState({ filteredUserCategories: subs })
                                                    }}/>
                                                {type.name}
                                    </label>)}                                   
                                    <ErrorMessage name="productType" component="div"
                                        className="alert alert-warning" />
                                </fieldset>                                        
                                        <fieldset className="form-group">
                                        <label className="required-field">category</label>
                                        <CustomSelect
                                                    name="category"
                                                    className={"w-50"}
                                                    items={this.state.filteredUserCategories}
                                                    value={values.userCategoryId}
                                                    onChange={(value) => {
                                                        let sub = this.state.filteredUserCategories.find(s => s.id == value.value);
                                                        setFieldValue("userCategoryId", value.value);
                                                        console.log("sub = " + JSON.stringify(sub));
                                                        this.setState({
                                                            amortizationPercent: sub.amortizationPercent,                                                           
                                                        })
                                                       if (this.state.productType == '')
                                                            this.setState({                                                              
                                                                productType: sub.category.productType
                                                            })
                                                    }}
                                        />
                                        <ErrorMessage name="userCategoryId" component="div"
                                            className="alert alert-warning w-50" />
                                            </fieldset>
                                            <fieldset className="form-group">
                                                <label>amortization percent</label><br/>
                                <Field className="form-control ws inline pt-2 pb-2"
                                    readOnly type="number" value={this.state.amortizationPercent} />&nbsp;%                                            
                                            </fieldset>
                                <fieldset className="form-group">
                                    <label>description</label>
                                    <Field className="form-control w-50" as="textarea" type="textarea" name="description" />
                                    <ErrorMessage name="description" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                <fieldset className="form-group mt-5">    
                                <button className="btn btn-mybtn p-x-5" disabled={!dirty} type="submit">Save</button>
                                    <button className="btn btn-mybtn btn-delete px-5 ml-5" type="button" onClick={this.cancelForm}>cancel</button>
                                </fieldset>
                            </Form>
                        )}
                </Formik>
            </div>
        )
    }
}

export default ProductComponent