import { Field, Form, Formik } from 'formik';
import React, { Component } from 'react';
import DatePicker from "react-datepicker";
import CustomSelect from './CustomSelect';
import './Filter.css';
import Functions from './Functions';


class UserProfileFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            all: props.all,            
           userId: props.userId,
            myProfile: props.myProfile,
            productNames: props.productNames,
            productId: props.productId,
            inventoryNumbers: props.inventoryNumbers,
            filteredInventoryNumbers: props.inventoryNumbers,
            productDetailId: props.productDetailId,
            givenAfter: props.givenAfter,
            returnedBefore: props.returnedBefore,
            prefix: props.prefix,
            userRole: props.userRole,
            current: props.current,
            allUser: props.allUser,
            withDetail: props.withDetail
             }
     
        this.onSubmit = this.onSubmit.bind(this)
        this.resetForm = this.resetForm.bind(this)
     
    }

    convertDate(value) {
       return Functions.convertDate(value);
    }
    onSubmit(values) {
       if (this.props.timeline.show) {
           let filter = { "productDetailId": values.productDetailId, "givenAfter": '', "returnedBefore": '' };
           filter.givenAfter = this.convertDate(values.givenAfter);
           filter.returnedBefore = this.convertDate(values.returnedBefore);
           this.props.onSearch(filter);
           return;
        }

        let path = window.location.pathname;
        let search = this.props.search || window.location.search;
        Functions.getSubmitPath(path, search, this.state.prefix, values, this.props.onNewSearch)      
    }

    filter(subs, names, value) {
        subs = [];
        if (value == null) subs = names;
        else {
            for (let i = 0; i < names.length; i++) {
                if (names[i].filterBy == value || names[i].value == '') {
                    subs.push(names[i])
                }
            }
        }
        return subs
    }

    resetForm() {

       if (this.props.onNewSearch) {
            this.props.onNewSearch('');
            this.setState({
                all: null,
                userId: null,
                myProfile: null,
                productId: null,
                productDetailId: null,
                givenAfter: null,
                returnedBefore: null,
                current: null,
                allUser: null,
                withDetail: null
            })
        }

        else
            window.location.href = window.location.pathname;      
    }

   
    render() {
       let userNames = this.props.userNames
        let { all,  userId, myProfile, inventoryNumbers, productDetailId, productNames, withDetail,
            productId, givenAfter, returnedBefore, current, allUser, filteredInventoryNumbers} = this.state;
       return (
            <Formik
              enableReinitialize={true}
                initialValues={{
                    all, userNames, userId, myProfile, inventoryNumbers, productDetailId, productNames, withDetail,
                    productId, givenAfter, returnedBefore, filteredInventoryNumbers, current, allUser
                }}
                onSubmit={this.onSubmit}                
            >
                {({ props, setFieldValue, values }) => (
                    <Form className="filter-form">
                        <fieldset >
                             <Field
                                className="ml-2 mr-1" type="checkbox" name="current"
                                value={values.current} checked={values.current}
                                disabled={this.props.timeline.show}
                                onChange={(value) => {                                   
                                    setFieldValue("current", !values.current);                                   
                                }}
                            />  <span className="font-weight-bold">current</span>                           
                            {this.state.userRole == 'ROLE_Mol' &&
                                <div className="inline">
                                    <div className="inline ml-3">
                                        <Field
                                        className="mr-1" type="checkbox" name="myProfile"
                                        value={values.myProfile} checked={values.myProfile}
                                        disabled={this.props.timeline.show}
                                            onChange={(value) => {                                               
                                                setFieldValue("myProfile", !values.myProfile);
                                                setFieldValue("userId", 'undefined');
                                                setFieldValue("allUser", false)
                                            }}
                                    />  <span className="font-weight-bold">my profile</span>
                                    </div>
                                    <div className="inline ml-3">
                                    <Field
                                        className="mr-1" type="checkbox" name="allUser"
                                        value={values.allUser} checked={values.allUser}
                                        disabled={this.props.timeline.show}
                                        onChange={(value) => {
                                           setFieldValue("allUser", !values.allUser);
                                            setFieldValue("userId", 'undefined');
                                            setFieldValue("myProfile", false)
                                        }}
                                        />  <span className="font-weight-bold">users</span>
                                    </div>
                                    <div className="inline ml-3">
                                        <label className="font-weight-bold">user&nbsp;</label>
                                        <CustomSelect
                                            className={"inline inline-4"}
                                            items={userNames}
                                            disabled={this.props.timeline.show}
                                            value={values.userId}
                                            onChange={(selected) => {
                                                setFieldValue("myProfile", false);
                                                setFieldValue("userId", selected.value);
                                                setFieldValue("allUser", false)
                                            }}/>
                                    </div>
                                </div>
                            }
                            <div className="inline">
                                <label >product&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-4"}
                                    items={productNames}
                                    disabled={this.props.timeline.show}
                                    value={this.props.timeline.show ? '' : values.productId}
                                    onChange={(selected) => {
                                        setFieldValue("productId", selected.value);
                                        let subs = values.filteredNames;
                                        subs = this.filter(subs, values.inventoryNumbers, selected.value);
                                        setFieldValue("filteredInventoryNumbers", subs);
                                    }}/>
                            </div>
                            <div className="inline">
                                <label >inventory number&nbsp;</label>
                                <CustomSelect
                                   // ref={(x) => { this.productDetailIdRef = x}}
                                    className={"inline inline-4"}
                                    items={values.filteredInventoryNumbers}
                                    log={true}
                                    value={values.productDetailId == null || values.productDetailId==undefined ?'undefined' : values.productDetailId }
                                    onChange={(selected) => setFieldValue("productDetailId", selected.value)}
                                />
                            </div>
                            <div className="inline">
                                <label className="mb-1">timeline&nbsp;</label>
                                <div className="inline px-2 border" style={{borderRadius:"3px"}}>
                                    <label className="mb-1 fw-s">given after&nbsp;</label>
                                    <div className="inline ">
                                        <DatePicker className="form-control in-inline inline-2"
                                            dateFormat="dd MMMM yyyy"                                           
                                            locale="en-GB"
                                            maxDate={values.returnedBefore && new Date(values.returnedBefore) || (new Date()).setDate((new Date()).getDate() - 1)}
                                            selected={values.givenAfter && new Date(values.givenAfter)}
                                            isClearable
                                            onChange={date => setFieldValue("givenAfter", date)}
                                            shouldCloseOnSelect={true}
                                            showYearDropdown
                                            dropdownMode="select"/>
                                    </div>
                                    <label className="pl-1 mb-1 fw-s">returned before&nbsp;</label>
                                    <DatePicker className="form-control in-inline inline-2"
                                        dateFormat="MMMM dd yyyy"
                                        
                                        locale="en-GB"
                                        minDate={values.givenAfter &&
                                            new Date(values.givenAfter).setDate((new Date(values.givenAfter)).getDate() + 1)}
                                        maxDate={(new Date()).setDate((new Date()).getDate()+1)}
                                        selected={values.returnedBefore && new Date(values.returnedBefore)}
                                        onChange={date => setFieldValue("returnedBefore", date)}
                                        isClearable
                                        highlightDates={new Date()}
                                        shouldCloseOnSelect={true}
                                        showYearDropdown
                                        dropdownMode="select"/>
                                </div>
                            </div>
                            <div className="inline pr-2 mr-2">
                                <Field
                                    className="mr-2 pt-3" type="checkbox" name="withDetail"
                                    value={true} checked={values.withDetail == true}
                                    onChange={(value) => {
                                        console.log('value of checked = ' + value.target.value);
                                        setFieldValue("withDetail", values.withDetail == true ? null : true);
                                    }}
                                /><label>with owings</label>
                                </div>                           
                            <div className="inline">
                                <button className="button px-5" type="submit" >Search</button>
                                <button className="button btn-delete" type="button" disabled={this.props.timeline.show} onClick={this.resetForm}>reset</button>
                            </div>
                        </fieldset>
                    </Form>
                )}
            </Formik>
        )
    }
}

export default UserProfileFilter