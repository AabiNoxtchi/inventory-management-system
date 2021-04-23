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
        //console.log("values.countryId = " + values.countryId)

       /* let path = window.location.pathname;
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
            if (!key.endsWith("s") && value && value.length > 0 && value != 'undefined') {
                newPath += prefix + '.' + key + '=' + value + '&'
            }

        })
        newPath = newPath.substring(0, newPath.length - 1);
       // newPath = path + '?' + newPath;
        newPath = '?' + newPath;
        newPath = this.props.onNewSearch ? newPath : path + newPath;
        //console.log('newPath =' + newPath);

      //  console.log("this.props.onNewSearch = " + this.props.onNewSearch != null)
       // window.location.href = newPath;
        this.props.onNewSearch ? this.props.onNewSearch(newPath)
            : window.location.href = newPath;*/
    }

    resetForm() {
        /* this.setState({
             all: '',
             firstName: '',
             lastName: '',
             userName: '',
             email: '',
         });
         console.log('in reset form ');*/

        //  window.location.href = window.location.pathname;
        this.setState({
            newCity:null,
            //countries: props.countries,
            countryId: null,
            //prefix: props.prefix,
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
                )
                }
            </Formik>
        )
    }
}

export default PendingUserFilter