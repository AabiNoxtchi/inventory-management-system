import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css'

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

class ProductDetailFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            all: props.all,
            priceMoreThan: props.priceMoreThan,
            priceLessThan: props.priceLessThan,
            isDiscarded: props.isDiscarded,
            isAvailable: props.isAvailable,
            deliveryNumbers: props.deliveryNumbers,
            deliveryId: props.deliveryId,

            productNames: props.productNames,
            productId: props.productId,           
            inventoryNumbers: props.inventoryNumbers,
            id: props.id,
           // inventoryNumber: props.inventoryNumber,
            productTypes: props.productTypes,
            productType: props.productType,
            dateCreatedBefore: props.dateCreatedBefore,
            dateCreatedAfter: props.dateCreatedAfter,

            amortizationPercentMoreThan: props.amortizationPercentMoreThan,
            amortizationPercentLessThan: props.amortizationPercentLessThan,
           
            prefix: props.prefix,            
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
            //console.log('field key =' + key);
            //console.log('field value =' + value);
            if (!key.endsWith("s") && value && value != 'undefined') {
                /*if ((values.productType === 'MA' && key === 'amortizationPercentMoreThan') ||
                    (values.productType === 'MA' && key === 'amortizationPercentLessThan') ||
                    (key === 'maxmore') ||
                    (key === 'minless') ||
                    (key === 'maxtotal') ||
                    (key === 'mintotal')) { }
                else*/
                if (key.startsWith('date')) {
                    value = (new Date(value)).toISOString();
                    value = value.substring(0, value.indexOf('T'))
                }
                 newPath += prefix + '.' + key + '=' + value + '&' 
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

        let { all, priceMoreThan, priceLessThan, isDiscarded, isAvailable, deliveryNumbers, deliveryId,
            productNames, productId, inventoryNumbers, id, productTypes, productType, dateCreatedBefore,//inventoryNumber
            dateCreatedAfter, amortizationPercentMoreThan, amortizationPercentLessThan} = this.state

        return (

            <Formik
                initialValues={{
                    all, priceMoreThan, priceLessThan, isDiscarded, isAvailable, deliveryNumbers, deliveryId,
                    productNames, productId, inventoryNumbers, id, productTypes, productType, dateCreatedBefore,
                    dateCreatedAfter, amortizationPercentMoreThan, amortizationPercentLessThan//inventoryNumber,
                }}
                onSubmit={this.onSubmit}
                enableReinitialize={true}
            >
                {({ props, setFieldValue, values }) => (
                    <Form className="filter-form">
                        <fieldset >
                            <div className="inline">
                                <label>product&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-3 foo"}
                                    items={productNames}
                                    value={values.productId}
                                    onChange={(selected) => setFieldValue("productId", selected.value)}
                                />
                            </div>
                            <div className="inline">
                                <label>inventory number&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-4 foo"}
                                    items={inventoryNumbers}
                                    value={values.id}
                                    onChange={(selected) => setFieldValue("id", selected.value)}
                                />
                            </div>
                            {!this.props.short&&
                                <div className="inline">
                                    <label className="mb-1">price&nbsp;</label>
                                    <div className="inline px-2 border ">
                                        <label className="mb-1 fw-s">more than&nbsp;</label>
                                        <Field className="form-control in-inline inline-100px" type="number" min="0" max={values.priceLessThan || ""}
                                            name="priceMoreThan" />
                                        <label className="pl-1 mb-1 fw-s">less than&nbsp;</label>
                                        <Field className="form-control in-inline inline-100px" type="number" min={values.priceMoreThan || 0}
                                            name="priceLessThan" />
                                    </div>
                                </div>
                            }
                            <div className="inline">
                                <label className="mb-1">date created&nbsp;</label>
                                <div className="inline px-2 border">
                                    <label className="mb-1 fw-s">after&nbsp;</label>
                                    <div className="inline ">
                                        <DatePicker className="form-control in-inline inline-2 foo"
                                            dateFormat="dd MMMM yyyy"
                                            locale="en-GB"
                                            maxDate={values.dateCreatedBefore}
                                            selected={values.dateCreatedAfter && new Date(values.dateCreatedAfter)}
                                            onChange={date => setFieldValue("dateCreatedAfter", date)} />
                                    </div>

                                    <label className="pl-1 mb-1 fw-s">before&nbsp;</label>
                                    <DatePicker className="form-control in-inline inline-2 foo"
                                        dateFormat="MMMM dd yyyy"
                                        locale="en-GB"
                                        minDate={values.dateCreatedAfter}
                                        selected={values.dateCreatedBefore && new Date(values.dateCreatedBefore)}
                                        onChange={date => setFieldValue("dateCreatedBefore", date)}
                                        highlightDates={new Date()} />
                                </div>
                            </div>
                            {!this.props.short &&
                            <>
                            {values.productTypes &&
                                <div className="inline">
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
                                                    }}
                                                />
                                                {type.name}
                                            </div>
                                        )
                                    }
                                </div>
                            }
                               

                                
                            <div className={values.productType == 'MA' ? "inline d-none" : "inline"}
                                >
                                    <label className="mb-1">amortization&nbsp;</label>
                                    <div className="inline px-2 border">
                                        <label className="mb-1 fw-s">more than&nbsp;</label>
                                        <Field className="form-control in-inline inline-75px" type="number" min="0" max={values.amortizationPercentLessThan || 100}
                                            name="amortizationPercentMoreThan" />&nbsp;%
                                    <label className="pl-2 mb-1 fw-s">less than&nbsp;</label>
                                        <Field className="form-control in-inline inline-75px" type="number" min={values.amortizationPercentMoreThan || 0} max="100"
                                            name="amortizationPercentLessThan" />&nbsp;%
                                        </div>
                            </div>
                            {values.deliveryNumbers &&
                                <div className="inline">
                                    <label>delivery&nbsp;</label>
                                    <CustomSelect
                                        className={"inline inline-2"}
                                        items={deliveryNumbers}
                                        value={values.deliveryId}
                                        onChange={(selected) => setFieldValue("deliveryId", selected.value)}
                                    />
                                </div>
                            }
                           

                            <div className="inline pr-2 mr-2">
                                <Field
                                    className="mr-2 pt-3" type="checkbox" name="isDiscarded"                                    
                                    value={true} checked={values.isDiscarded == true}
                                    onChange={(value) => {
                                        console.log('value of checked = ' + value.target.value);
                                        setFieldValue("isDiscarded", values.isDiscarded == true ? null : true);
                                    }}
                                />discarded
                                <Field
                                    className="mx-2 pt-3" type="checkbox" name="isDiscarded"
                                    value={false} checked={values.isDiscarded == false}
                                    onChange={(value) => {
                                        console.log('value of checked = ' + value.target.value);
                                        setFieldValue("isDiscarded", values.isDiscarded == false ? null : false);
                                    }}
                                />not discarded
                            </div>

                            <div className="inline pr-2 mr-2">
                                <Field
                                    className="mr-2 pt-3" type="checkbox" name="isAvailable"
                                    value={true} checked={values.isAvailable == true}
                                    onChange={(value) => {
                                        // console.log('value of checked = ' + value.target.value);
                                        setFieldValue("isAvailable", values.isAvailable == true ? null : true);
                                    }}
                                />available
                                <Field
                                    className="mx-2 pt-3" type="checkbox" name="isAvailable"
                                    value={false} checked={values.isAvailable == false}
                                    onChange={(value) => {
                                        // console.log('value of checked = ' + value.target.value);
                                        setFieldValue("isAvailable", values.isAvailable == false ? null : false);
                                    }}
                                />missing
                            </div>
                                </>

                        }


                           
                                

                            <div className="inline">
                                <button className="button px-5" type="submit">Search</button>
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

export default ProductDetailFilter