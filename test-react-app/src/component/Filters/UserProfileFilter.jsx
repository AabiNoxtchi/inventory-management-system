import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css';
import DatePicker from "react-datepicker";


class UserProfileFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            all: props.all,            
            userNames: props.userNames,
            userId: props.userId,
            myProfile: props.myProfile,
            productNames: props.productNames,
            productId: props.productId,
            inventoryNumbers: props.inventoryNumbers,
            productDetailId: props.productDetailId,
            givenAfter: props.givenAfter,
            returnedBefore: props.returnedBefore,
            prefix: props.prefix,
            userRole: props.userRole,
            //timeline: props.timeline.show
        }
       // console.log("userRole = " + this.state.userRole);
       // console.log("timeline = " + this.state.timeline);
       // console.log("props.timeline = " + JSON.stringify(this.props.timeline));

        this.onSubmit = this.onSubmit.bind(this)
        this.resetForm = this.resetForm.bind(this)
    }

    onSubmit(values) {

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
            /*if (!key.endsWith("s")) {
                console.log("key : " + key + " value = " + value);
               // console.log("value != 'undefined' : " + (value != 'undefined'))//+ " value.length > 1 = " + (value.length > 1));
                if(value)
               // console.log( " value.length > 1 = " + (value.length > 1));
                     console.log("value != 'undefined' : " + (value != 'undefined'))//+ " value.length > 1 = " + (value.length > 1));

            }*/
            if (!key.endsWith("s") && value && value != 'undefined') {
                if (key == 'givenAfter' || key =='returnedBefore') {
                    value = (new Date(value)).toISOString();
                    value = value.substring(0, value.indexOf('T'))
                }
                newPath += prefix + '.' + key + '=' + value + '&'
                //console.log("new path = " + newPath)
            }

        })
        newPath = newPath.substring(0, newPath.length - 1);
        newPath = path + '?' + newPath;
        console.log('newPath =' + newPath);

        window.location.href = newPath;
    }

    resetForm() {

        window.location.href = window.location.pathname;
       /* this.setState({
            all: '',
            firstName: '',
            lastName: '',
            userName: '',
            email: '',
        });
        console.log('in reset form ');*/
    }

    render() {
        //console.log("rendering filter props.timeline = " + this.props.timeline.show);

        let { all, userNames, userId, myProfile, inventoryNumbers, productDetailId, productNames, productId, givenAfter, returnedBefore } = this.state
        return (

            <Formik
                initialValues={{ all, userNames, userId, myProfile, inventoryNumbers, productDetailId, productNames, productId, givenAfter, returnedBefore }}
                onSubmit={this.onSubmit}
                enableReinitialize={true}
            >
                {({ props, setFieldValue, values }) => (
                    <Form className="filter-form">
                        <fieldset >
                            {
                                this.state.userRole == 'ROLE_Mol' &&
                                <div className="inline">
                                   
                                        <Field
                                            className="mx-2" type="checkbox" name="myProfile"
                                        value={values.myProfile} checked={values.myProfile}
                                        disabled={this.props.timeline.show}
                                            onChange={(value) => {
                                                // console.log('value of checked = ' + value.target.value);
                                                setFieldValue("myProfile", !values.myProfile);
                                                setFieldValue("userId", 'undefined')
                                            }}
                                        />  <span className="font-weight-bold">my profile</span>
                                   

                                    <div className="inline ml-3">
                                        <label className="font-weight-bold">user&nbsp;</label>
                                        <CustomSelect
                                            className={"inline inline-4"}
                                            items={userNames}
                                            disabled={this.props.timeline.show}
                                            value={values.userId}
                                            onChange={(selected) => {
                                                setFieldValue("myProfile", false);
                                                setFieldValue("userId", selected.value)
                                            }}
                                        />
                                    </div>
                                </div>
                            }
                            <div className="inline">
                                <label >product&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-4"}
                                    items={productNames}
                                    disabled={this.props.timeline.show}
                                    value={values.productId}
                                    onChange={(selected) => setFieldValue("productId", selected.value)}
                                />
                            </div>
                            <div className="inline">
                                <label >inventory number&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-4"}
                                    items={inventoryNumbers}
                                    disabled={this.props.timeline.show}
                                    value={values.productDetailId}
                                    onChange={(selected) => setFieldValue("productDetailId", selected.value)}
                                />
                            </div>

                            <div className="inline">
                                <label className="mb-1">timeline&nbsp;</label>
                                <div className="inline px-2 border" style={{
                                    borderRadius:"3px"
                                }}>
                                    <label className="mb-1 fw-s">given after&nbsp;</label>
                                    <div className="inline ">
                                        <DatePicker className="form-control in-inline inline-2"
                                            dateFormat="dd MMMM yyyy"
                                            disabled={this.props.timeline.show}
                                            locale="en-GB"
                                            maxDate={values.returnedBefore || (new Date()).setDate((new Date()).getDate() - 1)}
                                            selected={values.givenAfter && new Date(values.givenAfter)}
                                            onChange={date => setFieldValue("givenAfter", date)} />
                                    </div>

                                    <label className="pl-1 mb-1 fw-s">returned before&nbsp;</label>
                                    <DatePicker className="form-control in-inline inline-2"
                                        dateFormat="MMMM dd yyyy"
                                        disabled={this.props.timeline.show}
                                        locale="en-GB"
                                        minDate={values.givenAfter}
                                        maxDate={(new Date()).setDate((new Date()).getDate()+1)}
                                        selected={values.returnedBefore && new Date(values.returnedBefore)}
                                        onChange={date => setFieldValue("returnedBefore", date)}
                                        highlightDates={new Date()} />
                                </div>
                            </div>

                           
                           
                           
                            <div className="inline">
                                <button className="button px-5" type="submit" disabled={this.props.timeline.show}>Search</button>
                                <button className="button btn-delete" type="button" disabled={this.props.timeline.show} onClick={this.resetForm}>reset</button>
                            </div>
                        </fieldset>
                    </Form>
                )
                }
            </Formik>
        )
    }
}

export default UserProfileFilter