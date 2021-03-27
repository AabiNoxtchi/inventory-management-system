import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css';

class UserCategoryFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            categoryId: props.categoryId,
            names: props.names,
            filteredNames: this.filter([], props.names, props.productType),
            productType: props.productType,
            productTypes: props.productTypes,
            amortizationPercentMoreThan: props.amortizationPercentMoreThan,
            amortizationPercentLessThan: props.amortizationPercentLessThan,
            prefix: props.prefix,
        }

        this.onSubmit = this.onSubmit.bind(this)
        this.resetForm = this.resetForm.bind(this)
    }

    onSubmit(values) {
        console.log("values = " + JSON.stringify(values));
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

            if (!key.endsWith("s") && value && value != 'undefined') {
                newPath += prefix + '.' + key + '=' + value + '&'
            }
        })
        newPath = newPath.substring(0, newPath.length - 1);
        console.log("new path = " + newPath);
        newPath = path + '?' + newPath;
        window.location.href = newPath;
    }

    resetForm() {
        window.location.href = window.location.pathname;

    }

    filter(subs, names, value) {
        subs = [];
        if (value == null) subs = names;
        else {
            for (let i = 0; i < names.length; i++) {

                if (names[i].filterBy == value) {
                    subs.push(names[i])
                }
            }
        }
        return subs
    }

    render() {
        //console.log("rendering filter props.timeline = " + this.props.timeline.show);

        let { categoryId, names, productType, productTypes, filteredNames, amortizationPercentMoreThan, amortizationPercentLessThan } = this.state
        return (

            <Formik
                initialValues={{ categoryId, names, productType, productTypes, filteredNames, amortizationPercentMoreThan, amortizationPercentLessThan }}
                onSubmit={this.onSubmit}
                enableReinitialize={true}
            >
                {({ props, setFieldValue, values }) => (
                    <Form className="filter-form">
                        <fieldset >

                            <div className="inline px-2 mx-2">
                                <label>product type :</label>
                                {
                                    productTypes && productTypes.map((type) =>
                                        <div className="inline">
                                            <Field
                                                className="mx-2" type="checkbox" name="productType"
                                                value={type.value} checked={type.name === values.productType}
                                                onChange={(value) => {
                                                    // console.log('value of checked = ' + value.target.value);
                                                    value = value.target.value;
                                                    value = values.productType == value ? null : value;
                                                    let subs = values.filteredNames;
                                                    subs = this.filter(subs, values.names, value);

                                                    setFieldValue("productType", value);
                                                    setFieldValue("filteredNames", subs);
                                                }}
                                            />
                                            {type.name}
                                        </div>
                                    )
                                }
                            </div>
                            <div className="inline">
                                <label >category&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-4"}
                                    items={values.filteredNames || filteredNames || names}
                                    value={values.categoryId || ''}
                                    onChange={(selected) => setFieldValue("categoryId", selected.value)}
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
                                            value={values.productType == 'STA' ? '' : values.amortizationPercentLessThan} />&nbsp;%
                                        </div>
                                </div>
                           


                            <div className="inline">
                                <button className="button px-5" type="submit">Search</button>
                                <button className="button btn-delete" type="button" onClick={this.resetForm}>reset</button>
                            </div>
                        </fieldset>
                    </Form>
                )
                }
            </Formik>
        )
    }
}

export default UserCategoryFilter