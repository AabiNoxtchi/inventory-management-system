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
           // userNames: props.userNames,
            userId: props.userId,
            myProfile: props.myProfile,
            productNames: props.productNames,
            productId: props.productId,
            inventoryNumbers: props.inventoryNumbers,
            filteredInventoryNumbers: props.inventoryNumbers,//this.getFilteredListFunctions.getFilteredList([], props.inventoryNumbers, props.productId),//props.inventoryNumbers,
            productDetailId: props.productDetailId,
            givenAfter: props.givenAfter,
            returnedBefore: props.returnedBefore,
            prefix: props.prefix,
            userRole: props.userRole,
            current: props.current,
            allUser: props.allUser,
            withDetail: props.withDetail
            //timeline: props.timeline.show
        }
       // console.log("userRole = " + this.state.userRole);
       // console.log("timeline = " + this.state.timeline);
       // console.log("props.timeline = " + JSON.stringify(this.props.timeline));

        this.onSubmit = this.onSubmit.bind(this)
        this.resetForm = this.resetForm.bind(this)
      //  this.productDetailIdRef = React.createRef();
       // this.givenAfterRef = React.createRef();
       // this.formikRef = React.createRef();
    }

    /*showOriginal = (filter) => {
      //  console.log("filter.productdetailid = " + filter.productDetailId);
       // console.log("filter.givenat = " + filter.givenAfter)
        //console.log("random = " + Functions.getRandom);
       // let values = this.formikRef.current.props.values;

       // this.formikRef.current.setFieldValue(values.givenAfter, filter.givenAfter);
       // console.log("got original filter = " + JSON.stringify(filter))
        //this.productDetailIdRef.current.set
       // this.givenAfterRef.current.setSelected(new Date(filter.givenAfter));
        //  this.productDetailIdRef.current.setSelected(filter.productDetailId);
        this.setState({
            productDetailId: Functions.getRandom(),
            givenAfter: Functions.getRandom(),
            returnedBefore: Functions.getRandom(),

        })
        console.log("show original")
        console.log("filter.productDetailId = " + filter.productDetailId)
        this.setState({
            productDetailId: filter.productDetailId||null ,
            givenAfter: filter.givenAfter ,
            returnedBefore: filter.returnedBefore 
        })
    }*/

    convertDate(value) {
       /* if (value && value != 'undefined') {
            // let date = 
            value = (new Date(value)).toISOString();
            value = value.substring(0, value.indexOf('T'))
            return value;
        } else return '';*/
       return Functions.convertDate(value);
    }
    onSubmit(values) {
       // console.log("in submit filter");
       // console.log("this.props.timeline.show = " + this.props.timeline.show);
        if (this.props.timeline.show) {
           /* if (key == 'givenAfter' || key == 'returnedBefore') {
                value = (new Date(value)).toISOString();
                value = value.substring(0, value.indexOf('T'))
            }*/
           // console.log("preparing filter = ");
            let filter = { "productDetailId": values.productDetailId, "givenAfter": '', "returnedBefore": '' };
              //filter = JSON.parse(JSON.stringify(filter));
           // console.log("filter 1 = " + JSON.stringify(filter));
           // console.log("filter.givenAfter = " + JSON.stringify(filter.givenAfter));
           // filter.givenAfter = filter.givenAfter.substring(0, filter.givenAfter.indexOf('T'));//this.convertDate(filter.givenAfter);
            //filter.returnedBefore = filter.returnedBefore.substring(0, filter.returnedBefore.indexOf('T'));//this.convertDate(filter.returnedBefore);
            filter.givenAfter = this.convertDate(values.givenAfter);
           // console.log("filter 2 = " + JSON.stringify(filter));
            filter.returnedBefore = this.convertDate(values.returnedBefore);
          //  console.log("filter 3 = " + JSON.stringify(filter));
           // console.log("filter after conversion = " + JSON.stringify(filter));
            this.props.onSearch(filter);
            return;
        }

        let path = window.location.pathname;
        let search = this.props.search || window.location.search;
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
                if (key == 'givenAfter' || key == 'returnedBefore') {
                    value = this.convertDate(value);//(new Date(value)).toISOString();
                    //value = value.substring(0, value.indexOf('T'))
                }
              /*  if (this.props.timeline.show && (
                    key == 'givenAfter' || key == 'returnedBefore' || key == 'productDetailId'
                    )) {
                    newPath += prefix + '.' + key + '=' + value + '&'
                }else*/
                     newPath += prefix + '.' + key + '=' + value + '&'
                //console.log("new path = " + newPath)
            }

        })
        newPath = newPath.substring(0, newPath.length - 1);
       /* if (this.props.timeline.show) {
              newPath += "Filter.filtersSet=true";
              newPath = '?' + newPath;
              this.props.onSearch(newPath);
        } else {*/
        newPath = '?' + newPath;
        newPath = this.props.onNewSearch ? newPath : path + newPath;
            // console.log('newPath =' + newPath);

        this.props.onNewSearch ? this.props.onNewSearch(newPath)
            : window.location.href = newPath;
           // window.location.href = newPath;

       // }
      
       
       
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

        let userNames = this.props.userNames

        let { all,  userId, myProfile, inventoryNumbers, productDetailId, productNames, withDetail,
            productId, givenAfter, returnedBefore, current, allUser, filteredInventoryNumbers} = this.state;
       // let filteredInventoryNumbers = this.props.inventoryNumbers;
        return (

            <Formik
               // ref={this.formikRef}
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
                            {/*console.log("rendering values productDetailId = " + values.productDetailId)}
                            {/*console.log("rendering stateproductDetailId = " + this.state.productDetailId)}
                            {console.log("rendering filte value given at = " + values.givenAfter)}
                            {console.log("rendering state  given at = " + this.state.givenAfter)}
                            {console.log("props given at = " + this.props.givenAfter)*/}
                            <Field
                                className="ml-2 mr-1" type="checkbox" name="current"
                                value={values.current} checked={values.current}
                                disabled={this.props.timeline.show}
                                onChange={(value) => {
                                    // console.log('value of checked = ' + value.target.value);
                                    setFieldValue("current", !values.current);
                                    // setFieldValue("userId", 'undefined')
                                }}
                            />  <span className="font-weight-bold">current</span>
                           
                            {
                                this.state.userRole == 'ROLE_Mol' &&
                                <div className="inline">
                                    <div className="inline ml-3">
                                        <Field
                                        className="mr-1" type="checkbox" name="myProfile"
                                        value={values.myProfile} checked={values.myProfile}
                                        disabled={this.props.timeline.show}
                                            onChange={(value) => {
                                                // console.log('value of checked = ' + value.target.value);
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
                                            // console.log('value of checked = ' + value.target.value);
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
                                    value={this.props.timeline.show ? '' : values.productId}
                                    onChange={(selected) => {
                                        setFieldValue("productId", selected.value);

                                       // let subs = values.filteredInventoryNumbers;
                                        // let subs = values.filteredInventoryNumbers.
                                       // let subs = Functions.getFilteredList(values.inventoryNumbers, selected.value);//this.getFilteredList(subs, values.inventoryNumbers);
                                        let subs = values.filteredNames;
                                        subs = this.filter(subs, values.inventoryNumbers, selected.value);

                                        /*  [];
                                        for (let i = 0; i < values.inventoryNumbers.length; i++) {

                                            if (values.inventoryNumbers[i].filterBy == selected.value) {
                                                subs.push(values.inventoryNumbers[i])
                                            }
                                        }*/
                                        setFieldValue("filteredInventoryNumbers", subs);
                                    }}
                                />
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
                                           // ref={this.givenAfterRef}
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
                )
                }
            </Formik>
        )
    }
}

export default UserProfileFilter