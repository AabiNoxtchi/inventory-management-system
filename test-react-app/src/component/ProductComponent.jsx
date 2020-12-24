import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import ProductDataService from '../service/ProductDataService';
import '../myStyles/Style.css';
import CustomSelect from './Filters/CustomSelect';

class ProductComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            id: this.props.match.params.id,
            name: '',
            description: '',
            productType: '',
            productTypes: [],           
            subCategory: '',          
            subCategories: [],
            categories: [],
            selectedCategoryId:'',
            amortizationPercent: '',
        }
        this.onSubmit = this.onSubmit.bind(this)
        this.validate = this.validate.bind(this)
        this.cancelForm = this.cancelForm.bind(this)
    }

    componentDidMount() {
       
        ProductDataService.retrieve(this.state.id)
            .then(response => {
                if (this.state.id > 0)
                    this.setState({
                        name: response.data.name,
                        description: response.data.description,
                        productType: response.data.productType,
                        subCategory: response.data.subCategory,                       
                        amortizationPercent: response.data.amortizationPercent,
                    })
                this.setState({
                    productTypes: response.data.productTypes,
                    subCategories: response.data.subCategories,
                    categories: response.data.categories,
                })
            }
            )
    }

    onSubmit(values) {
        let item = {
            id: this.state.id,
            name: values.name,
           
            targetDate: values.targetDate
        }
        console.log('item = ' + item);
       ProductDataService.save(item)
            .then(() => this.props.history.push('/products'))
    }

    validate(values) {
        let errors = {}
        if (!values.name) {
            errors.name = 'required field !!!'
        } else if (values.name.length < 5) {
            errors.userName = 'Enter atleast 5 Characters'
        }

       

        return errors
    }

    cancelForm() {
        this.props.history.push('/products')
    }

    changeType = (type) => {

        this.setState({
            productType:type,
        })
    }

    render() {
        let { id, name, description, productType, productTypes, amortizationPercent, categories, subCategory, subCategoryId, subCategories, selectedCategoryId} = this.state
        return (
            <div className="container">
                {this.state.id > 0 ? <h3 className="mb-3"> Update Supplier</h3> : <h3 className="mb-3"> Add New Supplier </h3>}
                <Formik
                    initialValues={{ id, name, description, productType, productTypes, amortizationPercent, subCategory, categories, subCategoryId, subCategories, selectedCategoryId}}
                    onSubmit={this.onSubmit}
                    validateOnChange={false}
                    validateOnBlur={false}
                    validate={this.validate}
                    enableReinitialize={true}
                >
                    {
                        (props, setFieldValue) => (
                            <Form>
                                <Field className="form-control" type="text" name="id" hidden />
                                <fieldset className="form-group">
                                    <label>name</label>
                                    <Field className="form-control w-25" type="text" name="name" />
                                    <ErrorMessage name="name" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                <fieldset className="form-group">
                                    <label>description</label>
                                    <Field className="form-control w-25" type="textarea" name="description" />
                                    <ErrorMessage name="description" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                <fieldset className="form-group">
                                    <label>product type</label>
                                    {
                                        productTypes.map((type) =>
                                            <label>
                                                <Field type="checkbox" name="productType" value={type} onClick={this.changeType(type)} />
                                                type
                                    </label>)
                                    }
                                   
                                    <ErrorMessage name="productType" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                {this.state.productType === 'DMA' &&
                                    <div>
                                    <fieldset className="form-group">
                                        <label>category</label>
                                        <CustomSelect
                                            name="selectedCategoryId"
                                        className={"inline inline-1-5"}
                                        items={categories}
                                        value={subCategory != null ? subCategory.categoryId : ''}
                                        onChange={(selected) => this.setState({ selectedCategoryId: selected.value })}
                                        />
                                        <ErrorMessage name=" selectedCategoryId" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>sub-category</label>
                                        <CustomSelect
                                            name="subCategory"
                                            className={"inline inline-1-5"}
                                            items={subCategories}
                                            value={subCategory != null ? subCategory.id : ''}
                                            onChange={(selected) => setFieldValue("subCategory", selected.value)}
                                        />
                                        <ErrorMessage name=" selectedCategoryId" component="div"
                                            className="alert alert-warning" />
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>amortization percent</label>
                                    <Field className="form-control w-50" type="number" name="amortizationPercent" />
                                    <ErrorMessage name="amortizationPercent" component="div"
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

export default ProductComponent