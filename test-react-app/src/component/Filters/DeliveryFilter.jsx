import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css'
import DatePicker from "react-datepicker";
import Functions from './Functions';


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
        let search = window.location.search;
        Functions.getSubmitPath(path, search, this.state.prefix, values, this.props.onNewSearch)     
    }

    resetForm() {
      
        let index = window.location.search.indexOf("DeliveryDetailView");
        if (index < 0)
            this.props.onNewSearch('');
        else
            this.props.onNewSearch('?deliveryView=DeliveryDetailView');
      
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
                                <div className="inline px-2 border" style={{ borderRadius: "3px" }}>
                                <label className="mb-1 fw-s">after&nbsp;</label>
                                <div className="inline ">
                                <DatePicker className="form-control in-inline inline-2"
                                    dateFormat="dd MMMM yyyy"
                                    locale="en-GB"
                                    maxDate={values.dateCreatedBefore || new Date()}
                                            selected={values.dateCreatedAfter && new Date(values.dateCreatedAfter)}
                                            onChange={date => setFieldValue("dateCreatedAfter", date)}
                                            shouldCloseOnSelect={true}
                                            showYearDropdown
                                            dropdownMode="select"
                                            isClearable/>
                                    </div>
                                <label className="pl-1 mb-1 fw-s">before&nbsp;</label>
                                <DatePicker className="form-control in-inline inline-2"
                                    dateFormat="MMMM dd yyyy"
                                    locale="en-GB"
                                        minDate={values.dateCreatedAfter}
                                        maxDate={values.dateCreatedBefore||new Date()}
                                    selected={values.dateCreatedBefore && new Date(values.dateCreatedBefore)}
                                        onChange={date => setFieldValue("dateCreatedBefore", date)}
                                        shouldCloseOnSelect={true}
                                        showYearDropdown
                                        dropdownMode="select"
                                        isClearable
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
                                <div className="inline px-2 border" style={{ borderRadius: "3px" }}>
                                    <label className="mb-1 fw-s">more than&nbsp;</label>
                                    <Field className="form-control in-inline inline-100px" type="number" min="0" max={values.totalBillLessThan || ""}
                                        name="totalBillMoreThan" />
                                    <label className="pl-1 mb-1 fw-s">less than&nbsp;</label>
                                    <Field className="form-control in-inline inline-100px" type="number" min={values.totalBillMoreThan || 0}
                                        name="totalBillLessThan" />
                                </div>
                            </div>
                            <div className="inline">
                                <button className="button px-5" type="submit">Search</button>
                                <button className="button btn-delete" type="reset" onClick={this.resetForm}>reset</button>
                            </div>
                            {this.props.ids && <div className="">
                                <h5 className="mt-2 ml-2 font-bold" ><b><u>{window.location.search.indexOf("Filter.empty=true") > -1 ? "empty Deliveries"
                                    : window.location.search.indexOf("Filter.discarded=true") > -1 ? "Deliveries with all discared inventories" : ""}</u></b></h5>
                            </div>}
                        </fieldset>
                    </Form>
                )
                }
            </Formik>
        )
    }
}

export default DeliveryFilter