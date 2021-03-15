import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css'
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

class DeliveryFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            all: props.all,
            number: props.number,
            numbers: props.numbers,
            supplierId: props.supplierId,
            suppliers: props.suppliers,
            productId: props.productId,
            products: props.products,
            dateCreatedBefore: props.dateCreatedBefore,
            dateCreatedAfter: props.dateCreatedAfter,
            totalBillMoreThan: props.totalBillMoreThan,
            totalBillLessThan: props.totalBillLessThan,
            prefix: props.prefix,
            maxmore: 100,
            minless: 0,
            mintotal: 0,
        }

        this.onSubmit = this.onSubmit.bind(this)
        this.resetForm = this.resetForm.bind(this)
    }

    onSubmit(values) {

        let path = window.location.pathname;
        let search = window.location.search; //this.props.search || window.location.search;
        let newPath = ``;

        if (search.length > 1) {
            while (search.charAt(0) === '?') {
                search = search.substring(1);
            }
            let searchItems = search.split('&');
            for (let i = 0; i < searchItems.length; i++) {

                if (searchItems[i].startsWith('Pager.itemsPerPage='))
                    newPath += searchItems[i] + '&'
                else if (searchItems[i].startsWith('deliveryView'))
                    newPath += searchItems[i] + '&'
            }
        }
       /* if (this.props.search == null) newPath += 'deliveryView=DeliveryDetailView&';*/
       // console.log("this.props.search = " + this.props.search);
       // console.log("this.props.search == null = " + (this.props.search == null));
        let prefix = this.state.prefix;
        Object.entries(values).map(([key, value]) => {
           // console.log('field key =' + key);
           // console.log('field value =' + value);
            if (!key.endsWith("s") && value && value != 'undefined') {
                if ((key === 'maxmore') ||
                    (key === 'minless') ||
                    (key === 'maxtotal') ||
                    (key === 'mintotal')) { }
                else if (key.startsWith('date')) {
                    value = (new Date(value)).toISOString();
                    value = value.substring(0, value.indexOf('T'))
                       /* new Intl.DateTimeFormat("en-GB", {
                           month: "numeric",
                           day: "2-digit",
                           year: "numeric",                          
                        }).format(new Date(value));*/
                   // console.log('value = '+value)
                    newPath += prefix + '.' + key + '=' + value + '&'
                }
                else { newPath += prefix + '.' + key + '=' + value + '&' }
            }

        })
        newPath = newPath.substring(0, newPath.length - 1);
        newPath = path + '?' + newPath;
        console.log('newPath =' + newPath);

        window.location.href = newPath;
    }

    resetForm() {
       // console.log("this.props.search = " + this.props.search);

        //window.location.href = window.location.pathname; 
        let index = window.location.search.indexOf("DeliveryDetailView");
        window.location.href = index < 0 ? window.location.pathname : window.location.pathname + '?deliveryView=DeliveryDetailView';
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

        let { all, number, numbers, supplierId, suppliers,productId, products, dateCreatedBefore, dateCreatedAfter,
            totalBillMoreThan, totalBillLessThan, maxmore, minless, mintotal } = this.state

        return (

            <Formik
                initialValues={{
                    all, number, numbers, supplierId, suppliers, productId, products, dateCreatedBefore, dateCreatedAfter,
                    totalBillMoreThan, totalBillLessThan, maxmore, minless, mintotal
                }}
                onSubmit={this.onSubmit}
                enableReinitialize={true}
            >
                {({ props, setFieldValue, values }) => (
                    <Form className="filter-form">
                        <fieldset >
                            <div className="inline">
                                <label>number&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-1-5"}
                                    items={numbers}
                                    value={values.number}
                                    onChange={(selected) => setFieldValue("number", selected.value)}
                                />
                            </div>
                            <div className="inline">
                                <label>supplier&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-2-5"}
                                    items={suppliers}
                                    value={values.supplierId}
                                    onChange={(selected) => setFieldValue("supplierId", selected.value)}
                                />
                            </div>
                            <div className="inline">
                                <label className="mb-1">date&nbsp;</label>
                                <div className="inline px-2 border">
                                <label className="mb-1 fw-s">after&nbsp;</label>
                                <div className="inline ">
                                <DatePicker className="form-control in-inline inline-2"
                                    dateFormat="dd MMMM yyyy"
                                    locale="en-GB"
                                    maxDate={values.dateCreatedBefore}
                                        selected={values.dateCreatedAfter && new Date(values.dateCreatedAfter)}
                                        onChange={date => setFieldValue("dateCreatedAfter", date)} />
                                    </div>

                                <label className="pl-1 mb-1 fw-s">before&nbsp;</label>
                                <DatePicker className="form-control in-inline inline-2"
                                    dateFormat="MMMM dd yyyy"
                                    locale="en-GB"
                                    minDate={values.dateCreatedAfter}
                                    selected={values.dateCreatedBefore && new Date(values.dateCreatedBefore)}
                                    onChange={date => setFieldValue("dateCreatedBefore", date)}
                                        highlightDates={new Date()} />
                                </div>
                            </div>
                            <div className="inline">
                                <label>product&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-2-5"}
                                    items={products}
                                    value={values.productId}
                                    onChange={(selected) => setFieldValue("productId", selected.value)}
                                />
                            </div>
                                <div className="inline">
                                <label className="mb-1">total bill&nbsp;</label>
                                <div className="inline px-2 border">
                                    <label className="mb-1 fw-s">more than&nbsp;</label>
                                    <Field className="form-control in-inline inline-100px" type="number" min="0" max={values.totalBillLessThan || ""}
                                        name="totalBillMoreThan" />
                                    <label className="pl-1 mb-1 fw-s">less than&nbsp;</label>
                                    <Field className="form-control in-inline inline-100px" type="number" min={values.totalBillMoreThan || 0}
                                        name="totalBillLessThan" />
                                </div>
                            </div>
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

export default DeliveryFilter