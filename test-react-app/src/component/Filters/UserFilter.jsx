import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css';
import Functions from './Functions';

import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

class UserFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            all:props.all,
            firstNames: props.firstNames || [],
            firstName: props.firstName,
            lastNames: props.lastNames || [],
            lastName: props.lastName,
            userNames: props.userNames || [],
            userName: props.userName,
            emails: props.emails || [],
            email: props.email,
            cityId: props.cityId,
            cities: props.cities,
            filteredcities: props.countryId ? this.filter([], props.cities, props.countryId) : props.cities,
            countryId: props.countryId,
            countries: props.countries,
            prefix: props.prefix,
            userRole: props.userRole,
            lastActiveBefore: props.lastActiveBefore
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
        if (!value || value == 'undefined') subs = names;
        else {
            for (let i = 0; i < names.length; i++) {
                if (names[i].filterBy == value || names[i].value == '') {
                    subs.push(names[i])
                }
            }
        }
        return subs
    }
   
    render() {  

        let { all, firstNames, firstName, lastNames, lastName, userNames, userName, emails,
            email, countryId, filteredcities, cityId, lastActiveBefore } = this.state
        return (                        
               
            <Formik
                initialValues={{
                    all, firstNames, firstName, lastNames, lastName, userNames,
                    userName, emails, email, countryId, filteredcities, cityId, lastActiveBefore
                }}
                        onSubmit={this.onSubmit}                       
                        enableReinitialize={true}
                >
                {({ props, setFieldValue, values}) => (
                    <Form className="filter-form">
                            <fieldset >
                                 <div className="inline">
                                 <label>first name&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-2-5"}
                                        items={firstNames}
                                        value={values.firstName}
                                        onChange={(selected) => setFieldValue("firstName", selected.value)}
                                />
                            </div>  
                            <div className="inline">
                                 <label >last name&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-2-5"}
                                        items={lastNames}
                                        value={values.lastName}
                                        onChange={(selected) => setFieldValue("lastName", selected.value)}
                                />
                            </div>
                                <div className="inline">
                                <label>user name&nbsp;</label>
                                <CustomSelect  
                                    className={"inline inline-2-5"}
                                       name="userNames"
                                       items={userNames}
                                       value={values.userName}
                                       onChange={(selected) => setFieldValue("userName", selected.value)}
                                />
                            </div>
                            <div className="inline">
                                <label >email&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-3"}
                                      items={emails}
                                      value={values.email}
                                      onChange={(selected) =>setFieldValue("email", selected.value)}
                                />
                            </div>
                            {this.state.userRole == 'ROLE_Admin' &&
                                <>
                                <div className="inline">
                                <label >country&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-3"}
                                    items={this.state.countries}
                                    value={values.countryId}
                                    onChange={(selected) => {
                                        setFieldValue("countryId", selected.value);
                                        let subs = this.filter([], this.state.cities, selected.value);
                                        setFieldValue("filteredcities", subs);
                                    }}/>
                                </div>
                            <div className="inline">
                                <label >city&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-3"}
                                    items={values.filteredcities}
                                    value={values.cityId}
                                    onChange={(selected) => setFieldValue("cityId", selected.value)}
                                />
                                </div>
                                <div className="inline">
                                    <label className="mb-1">last active&nbsp;</label>
                                    <label className="pl-1 mb-1 fw-s">before&nbsp;</label>
                                    <DatePicker className="form-control in-inline inline-2 foo p-1 pl-2"
                                        dateFormat="MMMM dd yyyy"
                                        locale="en-GB"
                                        // minDate={values.dateCreatedAfter}
                                        selected={values.lastActiveBefore && new Date(values.lastActiveBefore)}
                                        isClearable
                                        onChange={date => setFieldValue("lastActiveBefore", Functions.convertDate(date))}
                                        highlightDates={new Date()}
                                        shouldCloseOnSelect={true}
                                        showYearDropdown
                                        dropdownMode="select" />
                                </div>
                                </>
                            }
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

export default UserFilter