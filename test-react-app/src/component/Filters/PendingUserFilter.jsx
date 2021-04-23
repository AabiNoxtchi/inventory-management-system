import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css';
import Functions from './Functions';

class PendingUserFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            newCity: props.newCity,
            countries: props.countries,
            countryId: props.countryId,
            prefix: props.prefix,
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
        this.setState({
            newCity:null,           
            countryId: null,            
        })
        this.props.onNewSearch ? this.props.onNewSearch('') : window.location.href = window.location.pathname;
    }

    render() {

        let { newCity, countryId } = this.state
        return (

            <Formik
                initialValues={{ newCity, countryId }}
                onSubmit={this.onSubmit}
                enableReinitialize={true}
            >
                {({ props, setFieldValue, values }) => (
                    <Form className="filter-form">
                        <fieldset >
                            <div className="inline">
                                <label>country&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-2-5"}
                                    items={this.state.countries}
                                    value={values.countryId}
                                    onChange={(selected) => setFieldValue("countryId", selected.value)}
                                />
                            </div>
                            <label className="mb-1 fw-s">city name&nbsp;</label>
                            <Field className="form-control inline inline-2 pt-1 pb-1" type="text" 
                                name="newCity" />
                            <div className="inline">
                                <button className="button px-5" type="submit">Search</button>
                                <button className="button btn-delete" type="button" onClick={this.resetForm}>reset</button>
                            </div>
                        </fieldset>
                    </Form>
                )}
            </Formik>
        )
    }
}

export default PendingUserFilter