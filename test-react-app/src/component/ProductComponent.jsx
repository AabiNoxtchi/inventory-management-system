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
            filteredSubCategories:[],
            categories: [],           
            amortizationPercent: '',
            maxamortization: 100,
            selectedCategoryId: '',
        }
        this.onSubmit = this.onSubmit.bind(this)
        this.validate = this.validate.bind(this)
        this.cancelForm = this.cancelForm.bind(this)
    }

    componentDidMount() {
       
        ProductDataService.retrieve(this.state.id)
            .then(response => {
                console.log('got response in component did mount');
                if (this.state.id > 0) {
                    this.setState({
                        name: response.data.name,
                        description: response.data.description,
                        productType: response.data.productType,
                        subCategory: response.data.productType == 'DMA' ? response.data.subCategory : '',
                        selectedCategoryId: response.data.productType == 'DMA' ? response.data.subCategory.category.id : '',
                        amortizationPercent: response.data.productType == 'DMA' ? response.data.amortizationPercent : '',
                    })
                }
                this.setState({
                    productTypes: response.data.productTypes,
                    subCategories: response.data.subCategories,
                    filteredSubCategories: response.data.subCategories,
                    categories: response.data.categories,
                });
            })
    }

    onSubmit(values) {
        let item = {
            id: this.state.id,
            name: values.name,
            description: values.description,
            productType: values.productType,
            subCategory: values.productType == 'DMA' ? values.subCategory : null,
            amortizationPercent: values.productType == 'DMA' ? values.amortizationPercent : null,           
            targetDate: values.targetDate
        }
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

        if (!values.productType) {
            errors.productType = 'required field !!!'
        }

        if (values.productType === 'DMA') {

            if (!values.categories)
                errors.categories = 'required field !!!'
            if (!values.subCategory) {
                errors.subCategory = 'required field !!!'
            }
            if (!values.amortizationPercent) {
                errors.amortizationPercent = 'required field !!!'
            }
        }
        return errors
    }

    cancelForm() {
        //this.props.history.push('/products')
        window.history.back();
    }

    render() {
        console.log('rendering');
        let { id, name, description, productType, productTypes, amortizationPercent, categories, subCategory, subCategories,
            filteredSubCategories, maxamortization, selectedCategoryId } = this.state
        return (
            <div className="container">
                {this.state.id > 0 ? <h3 className="mb-3"> Update Product</h3> : <h3 className="mb-3"> Add New Product </h3>}
                <Formik
                    initialValues={{
                        id, name, description, productType, productTypes, amortizationPercent, subCategory, categories,
                        subCategories, filteredSubCategories, maxamortization, selectedCategoryId
                    }}
                    onSubmit={this.onSubmit}
                    validateOnChange={false}
                    validateOnBlur={false}
                    validate={this.validate}
                    enableReinitialize={true}
                >
                    {
                        ({ setFieldValue, values}) => (
                            <Form>
                                <Field className="form-control" type="text" name="id" hidden />
                                <fieldset className="form-group">
                                    <label>name</label>
                                    <Field className="form-control w-25" type="text" name="name"
                                   />
                                    <ErrorMessage name="name" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                <fieldset className="form-group">
                                    <label>description</label>
                                    <Field className="form-control w-50" as="textarea" type="textarea" name="description" />
                                    <ErrorMessage name="description" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                <fieldset className="form-group">
                                    <label>product type</label>
                                    {
                                        productTypes.map((type) =>
                                            <label className="mx-3">
                                                <Field
                                                    className="mx-1"
                                                    type="radio" name="productType" value={type.value}
                                                   
                                                    />
                                                {type.name}
                                    </label>)
                                    }
                                   
                                    <ErrorMessage name="productType" component="div"
                                        className="alert alert-warning" />
                                </fieldset>
                                {
                                    values.productType=='DMA'  ? (
                                   
                                    <div>
                                    <fieldset className="form-group">
                                        <label>category</label>
                                                <CustomSelect      
                                                    id="selectedCategoryId"
                                                    name="selectedCategoryId"
                                                    className={"w-50"}
                                                    items={categories}
                                                    value={subCategory !== '' ? subCategory.category.id : ''}
                                                    onChange={(value) => {
                                                        setFieldValue("selectedCategoryId", value.value);
                                                        setFieldValue("amortizationPercent", '');
                                                        setFieldValue("maxamortization", categories.find(c => c.id == value.value).amortizationPercent);
                                                        
                                                        let subs = [];
                                                        for (let i = 0; i < values.subCategories.length; i++) {
                                                          
                                                            if (values.subCategories[i].category.id == value.value) {
                                                                subs.push(values.subCategories[i])
                                                            }
                                                        }
                                                        setFieldValue("filteredSubCategories", subs);
                                                    }
                                                    }
                                      
                                            />
                                        <ErrorMessage name=" selectedCategoryId" component="div"
                                            className="alert alert-warning" />
                                            </fieldset>
                                        </div>
                                    ) : null
                                }                        
                                {values.selectedCategoryId != '' && values.productType == 'DMA' ? (
                                        <div>
                                        <fieldset className="form-group">
                                        <label>sub category</label>
                                        <CustomSelect
                                                    name="subCategory"
                                                    className={"w-50"}
                                                    items={values.filteredSubCategories}//categories.find(c => c.id == values.selectedCategoryId).subCategories}
                                                    value={subCategory !== '' ? subCategory.id : ''}
                                                    onChange={(value) => {
                                                        let sub = values.filteredSubCategories.find(s => s.id == value.value);
                                                        setFieldValue("subCategory", sub)
                                                    }}
                                        />
                                        <ErrorMessage name=" selectedCategoryId" component="div"
                                            className="alert alert-warning" />
                                            </fieldset>
                                            <fieldset className="form-group">
                                                <label>amortization percent</label>
                                                <Field className="form-control w-50" min="0" max={values.maxamortization} type="number" name="amortizationPercent" placeholder={"max =" + values.maxamortization} />
                                                <ErrorMessage name="amortizationPercent" component="div"
                                                    className="alert alert-warning" />
                                            </fieldset>
                                       
                                        </div>
                                            ):null}
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