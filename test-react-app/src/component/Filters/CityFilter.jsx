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
       // console.log("values = " + JSON.stringify(values));
       // let path = window.location.pathname;
        //let search = window.location.search;

        let path = window.location.pathname;
        let search = window.location.search;

        Functions.getSubmitPath(path, search, this.state.prefix, values, this.props.onNewSearch)
        /*let newPath = ``;

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
        newPath = '?' + newPath;
        newPath = this.props.onNewSearch ? newPath : path + newPath;
        this.props.onNewSearch ? this.props.onNewSearch(newPath) : this.props.history ? this.props.history.push(newPath) : window.location.href = newPath;*/
    }

    resetForm() {

        this.props.onNewSearch ?
            this.props.onNewSearch('') :
            window.location.href = window.location.pathname;
       // window.location.href = window.location.pathname;
       
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
        //console.log("rendering filter props.timeline = " + this.props.timeline.show);

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
                                       /* values.filteredcities;
                                        if (!selected.value || selected.value == 'undefined')
                                            subs = cities;
                                        else {
                                            subs = []
                                            for (let i = 0; i < cities.length; i++) {

                                                if (cities[i].filterBy == selected.value) {
                                                    subs.push(cities[i])
                                                }
                                            }
                                        }*/
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