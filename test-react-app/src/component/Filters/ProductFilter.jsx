import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css'

class ProductFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            all: props.all,
            name: props.name,
            names: props.names,
            productType: props.productType,
            productTypes: props.productTypes,
            amortizationPercentMoreThan: props.amortizationPercentMoreThan,
            amortizationPercentLessThan: props.amortizationPercentLessThan,
            totalCountMoreThan: props.totalCountMoreThan,
            totalCountLessThan: props.totalCountLessThan,
            prefix: props.prefix,
            maxmore: 100,
            minless:0,
            mintotal: 0,
            userCategories: props.userCategories,
            filteredUserCategories: props.userCategories,
            userCategoryId: props.userCategoryId
        }

        this.onSubmit = this.onSubmit.bind(this)
        this.resetForm = this.resetForm.bind(this)
    }

    onSubmit(values) {

        let path = window.location.pathname;
        let search = window.location.search;
        let newPath = ``;

        if (search.length > 1) {
            while (search.charAt(0) === '?') {
                search = search.substring(1);
            }
            let searchItems = search.split('&');
            for (let i = 0; i < searchItems.length; i++) {

                if (searchItems[i].startsWith('Pager.itemsPerPage='))
                    newPath += searchItems[i] + '&'
            }
        }

        let prefix = this.state.prefix;
        Object.entries(values).map(([key, value]) => {
            console.log('field key =' + key);
            console.log('field value =' + value);
            if (!key.endsWith("s") && value) {
                if ((values.productType === 'STA' && key === 'amortizationPercentMoreThan') ||
                    (values.productType === 'STA' && key === 'amortizationPercentLessThan') ||
                    (key === 'maxmore') ||
                    (key === 'minless') ||
                    (key === 'maxtotal') ||
                    (key === 'mintotal')) { }
                else { newPath += prefix + '.' + key + '=' + value + '&' }
            }

        })
        newPath = newPath.substring(0, newPath.length - 1);
        newPath = path + '?' + newPath;
        console.log('newPath =' + newPath);

        window.location.href = newPath;
    }

    resetForm() {

        window.location.href = window.location.pathname;
       // values.name = null;

       // this.props.history.push('/products');
       /* this.setState({
            all: '',
            name: '',
            productType: '',
            amortizationPercentMoreThan: '',
            amortizationPercentLessThan: '',
            totalCountMoreThan: '',
            totalCountLessThan:''
        });
        console.log('in reset form ');*/
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
                                {                                     
                                    productTypes.map((type) =>
                                        <div className="inline">                                        
                                            <Field 
                                                className="mx-2" type="checkbox" name="productType"
                                                value={type.value} checked={type.name === values.productType}
                                                onChange={(value) => {
                                                    console.log('value of checked = ' + value.target.value);
                                                    setFieldValue("productType", value.target.value == values.productType ? null : value.target.value);
                                                    let subs = values.filteredUserCategories;
                                                    subs = [];
                                                    for (let i = 0; i < values.userCategories.length; i++) {
                                                        
                                                        if (values.userCategories[i].filterBy == value.target.value) {
                                                            subs.push(values.userCategories[i])
                                                        }
                                                    }
                                                    setFieldValue("filteredUserCategories", subs);
                                                }} 
                                            />
                                            {type.name}
                                        </div>
                                   )
                                }
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
                            {/*values.productType == 'STA' ? "inline d-none" : "inline"*/} 
                            {
                                <div className="inline">
                                    <label className="mb-1">amortization&nbsp;</label>
                                    <div className="inline px-2 border" style={{ borderRadius: "3px" }}>
                                    <label className="mb-1 fw-s">more than&nbsp;</label>
                                    <Field className="form-control in-inline inline-50px" type="number" min="0" max={values.amortizationPercentLessThan || 100}
                                            name="amortizationPercentMoreThan" disabled={values.productType == 'STA'}
                                            value={values.productType == 'STA' ? '' : values.amortizationPercentMoreThan}/>&nbsp;%
                                    <label className="pl-2 mb-1 fw-s">less than&nbsp;</label>
                                        <Field className="form-control in-inline inline-50px" type="number" min={values.amortizationPercentMoreThan || 0} max="100"
                                            name="amortizationPercentLessThan" disabled={values.productType == 'STA'}
                                            value={values.productType == 'STA' ? '' : values.amortizationPercentLessThan}/>&nbsp;%
                                        </div>
                            </div>
                            }
                           
                            <div className="inline">
                                <button className="button" type="submit">Search</button>
                                <button className="button btn-delete" type="reset" onClick={this.resetForm}>reset</button>
                            </div>
                        </fieldset>
                    </Form>
                )
                }
            </Formik>
        )
    }
}

export default ProductFilter