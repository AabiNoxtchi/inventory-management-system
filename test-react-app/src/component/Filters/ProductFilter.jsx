import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css';
import Functions from './Functions';

class ProductFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            all: props.all,
            name: props.name,
            names: props.names ||[],
            productType: props.productType,
            productTypes: props.productTypes||[],
            amortizationPercentMoreThan: props.amortizationPercentMoreThan,
            amortizationPercentLessThan: props.amortizationPercentLessThan,
            totalCountMoreThan: props.totalCountMoreThan,
            totalCountLessThan: props.totalCountLessThan,
            prefix: props.prefix,
            maxmore: 100,
            minless:0,
            mintotal: 0,
            userCategories: props.userCategories || [],
            filteredUserCategories: this.filter([], props.userCategories, props.productType),
            userCategoryId: props.userCategoryId
        }

        this.onSubmit = this.onSubmit.bind(this)
        this.resetForm = this.resetForm.bind(this)
    }

    filter(subs, names, value) {
        subs = [];
        if (value == null) subs = names;
        else {
            for (let i = 0; i < names.length; i++) {
                if (names[i].filterBy == value || !names[i].value || names[i].value == 'undefined') {
                    subs.push(names[i])
                }
            }
        }
        return subs
    }

    onSubmit(values) {

        let path = window.location.pathname;
        let search = window.location.search;
        Functions.getSubmitPath(path, search, this.state.prefix, values, this.props.onNewSearch)
     }

    resetForm() {
        this.props.onNewSearch ?
            this.props.onNewSearch('') :
            window.location.href = window.location.pathname;      
    }

    render() {

        let { all, name, names, productType, productTypes, amortizationPercentMoreThan, amortizationPercentLessThan,
            totalCountMoreThan, totalCountLessThan, maxmore, minless, mintotal, userCategories, userCategoryId, filteredUserCategories} = this.state

        return (

            <Formik
                initialValues={{
                    all, name, names, productType, productTypes, amortizationPercentMoreThan,
                    amortizationPercentLessThan, totalCountMoreThan, totalCountLessThan, maxmore, minless, mintotal, userCategories, userCategoryId, filteredUserCategories
                }}
                onSubmit={this.onSubmit}
                enableReinitialize={true}
            >
                {({ props, setFieldValue, values}) => (
                    <Form className="filter-form">
                        <fieldset >
                            <div className="inline">
                                <label>name&nbsp;</label>
                                <CustomSelect                                    
                                    className={"inline inline-2-5"}
                                    items={names}
                                    value={name}
                                    onChange={(selected) => setFieldValue("name", selected.value)}
                                />
                            </div>
                            {
                                <div className="inline" >
                                    <label className="mb-1">total&nbsp;</label>
                                    <div className="inline px-2 border" style={{ borderRadius: "3px" }}>
                                    <label className="mb-1 fw-s">more than&nbsp;</label>
                                    <Field className="form-control in-inline inline-100px" type="number" min="0" max={values.totalCountLessThan || ""}
                                        name="totalCountMoreThan" />
                                    <label className="pl-1 mb-1 fw-s">less than&nbsp;</label>
                                    <Field className="form-control in-inline inline-100px" type="number" min={values.totalCountMoreThan || 0}
                                        name="totalCountLessThan" />
                                    </div>
                                </div>
                            }                            
                            <div className="inline px-2 mx-2">
                                <label>product type :</label>
                                { productTypes && productTypes.map((type) =>
                                        <div className="inline">                                        
                                            <Field 
                                                className="mx-2" type="checkbox" name="productType"
                                                value={type.value} checked={type.name === values.productType}
                                                onChange={(value) => {
                                                    console.log('value of checked = ' + value.target.value);
                                                    setFieldValue("productType", value.target.value == values.productType ? null : value.target.value);
                                                    let subs = values.filteredUserCategories;
                                                    subs = this.filter([], values.userCategories, value.target.value)
                                                    setFieldValue("filteredUserCategories", subs);
                                                }} 
                                            />
                                            {type.name}
                                        </div>
                                   )}
                            </div>
                            <div className="inline">
                                <label>category&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-2-5"}
                                    items={values.filteredUserCategories}
                                    value={values.userCategoryId}
                                    onChange={(selected) => setFieldValue("userCategoryId", selected.value)}
                                />
                            </div>
                            <div className="inline">
                                <label className="mb-1">amortization&nbsp;</label>
                                <div className="inline px-2 border" style={{ borderRadius: "3px" }}>
                                    <label className="mb-1 fw-s">more than&nbsp;</label>
                                    <Field className="form-control in-inline inline-50px" type="number" min="0" max={values.amortizationPercentLessThan || 100}
                                        name="amortizationPercentMoreThan" disabled={values.productType == 'STA'}
                                        value={values.productType == 'STA' ? '' : values.amortizationPercentMoreThan} />&nbsp;%
                                    <label className="pl-2 mb-1 fw-s">less than&nbsp;</label>
                                    <Field className="form-control in-inline inline-50px" type="number" min={values.amortizationPercentMoreThan || 0} max="100"
                                        name="amortizationPercentLessThan" disabled={values.productType == 'STA'}
                                        value={values.productType == 'STA' ? '' : values.amortizationPercentLessThan}/>&nbsp;%
                                </div>
                            </div>
                            <div className="inline">
                                <button className="button px-5" type="submit">Search</button>
                                <button className="button btn-delete" type="reset" onClick={this.resetForm}>reset</button>
                            </div>
                        </fieldset>
                    </Form>
                )}
            </Formik>
        )
    }
}

export default ProductFilter