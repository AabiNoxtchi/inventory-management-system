import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css';
import Functions from './Functions';

class SupplierFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            all: props.all,
            name: props.name,
            names: props.names,
            phoneNumber: props.phoneNumber,
            phoneNumbers: props.phoneNumbers,
            ddcnumber: props.ddcnumber,
            ddcnumbers: props.ddcnumbers,
            emails: props.emails,
            email: props.email,
            prefix: props.prefix
        }

        this.onSubmit = this.onSubmit.bind(this)
        this.resetForm = this.resetForm.bind(this)
    }

    onSubmit(values) {
        let path = window.location.pathname;
        let search = window.location.search;

        Functions.getSubmitPath(path, search, this.state.prefix, values, this.props.onNewSearch)
      /*  let path = window.location.pathname;
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
            if (!key.endsWith("s") && value && value != 'undefined' && value.length > 1) {
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
       /* this.setState({
            all: '',
            name: '',
            phoneNumber: '',
            ddcnumber: '',
            email: '',
        });
        console.log('in reset form ');*/

       // window.location.href = window.location.pathname;

        this.props.onNewSearch ?
            this.props.onNewSearch('') :
            window.location.href = window.location.pathname;
    }

    render() {

        let { all, name, names, phoneNumber, phoneNumbers, ddcnumber, ddcnumbers, email, emails } = this.state
        return (

            <Formik
                initialValues={{ all, name, names, phoneNumber, phoneNumbers, ddcnumber, ddcnumbers, email, emails }}
                onSubmit={this.onSubmit}
                enableReinitialize={true}
            >
                {({ props, setFieldValue , values}) => (
                    <Form className="filter-form">
                        <fieldset >
                            <div className="inline">
                                <label>name&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-2-5"}
                                    items={names}
                                    value={values.name}
                                    onChange={(selected) => setFieldValue("name", selected.value)}
                                />
                            </div>
                            <div className="inline">
                                <label >phone number&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-2"}
                                    items={phoneNumbers}
                                    value={values.phoneNumber}
                                    onChange={(selected) => setFieldValue("phoneNumber", selected.value)}
                                />
                            </div>
                            <div className="inline">
                                <label>DDC number&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-2"}
                                    name="ddcnumber"
                                    items={ddcnumbers}
                                    value={values.ddcnumber+''}
                                    onChange={(selected) => setFieldValue("ddcnumber", selected.value+'')}
                                />
                            </div>
                            <div className="inline">
                                <label >email&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-3"}
                                    items={emails}
                                    value={values.email}
                                    onChange={(selected) => setFieldValue("email", selected.value)}
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

export default SupplierFilter