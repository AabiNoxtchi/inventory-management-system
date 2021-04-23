import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css';
import Functions from './Functions';

class CityFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            countries: props.countries,
            countryId: props.countryId,
            cities: props.cities,
            filteredcities: this.filter([], props.cities, props.countryId),
            cityId: props.cityId,
            zones: props.zones,
            timeZone: props.timeZone,
            currencies: props.currencies,
            currency: props.currency,
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

        this.props.onNewSearch ?
            this.props.onNewSearch('') :
            window.location.href = window.location.pathname;
      
    }

    filter(subs, names, value) {
        subs = [];
        if (value == null || value == 'undefined') subs = names;
        else {
            for (let i = 0; i < names.length; i++) {

                if (names[i].filterBy == value || !names[i].value || names[i].value == 'undefined') {
                    subs.push(names[i])
                }
            }
        }
        return subs
    }

    render() {
       
        let { countries, countryId, cities, cityId, zones, timeZone, currencies, currency, filteredcities} = this.state
        return (

            <Formik
                initialValues={{ countries, countryId, cities, cityId, zones, timeZone, currencies, currency, filteredcities }}
                onSubmit={this.onSubmit}
                enableReinitialize={true}
            >
                {({ props, setFieldValue, values }) => (
                    <Form className="filter-form">
                        <fieldset >
                           
                            <div className="inline">
                                <label >country&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-4"}
                                    items={countries}
                                    value={values.countryId}
                                    onChange={(selected) => {
                                        setFieldValue("countryId", selected.value);
                                        let subs = this.filter([], values.cities, selected.value);                                      
                                        setFieldValue("filteredcities", subs);
                                    }}
                                />
                            </div>
                            <div className="inline">
                                <label >cities&nbsp;</label>
                                <CustomSelect                                  
                                    className={"inline inline-4"}
                                    items={values.filteredcities || filteredcities || cities}
                                    value={values.cityId || ''}
                                    onChange={(selected) => setFieldValue("cityId", selected.value)}                                
                                />
                            </div>
                            <div className="inline">
                                <label >time zone&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-4"}
                                    items={values.zones}
                                    value={values.timeZone || ''}
                                    onChange={(selected) => setFieldValue("timeZone", selected.value)}
                                />
                            </div>
                            <div className="inline">
                                <label >currency&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-4"}
                                    items={values.currencies}
                                    value={values.currency || ''}
                                    onChange={(selected) => setFieldValue("currency", selected.value)}
                                />
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

export default CityFilter