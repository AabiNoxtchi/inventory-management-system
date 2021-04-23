import React, { Component } from 'react';
import '../myStyles/Style.css';
import DatePicker from "react-datepicker";
import CustomSelect from './Filters/CustomSelect';
import UserProfileDataService from '../service/UserProfileDataService';
import { Formik, Form, Field, FieldArray, ErrorMessage } from 'formik';
import Function from './Shared/Function'


class TimelineInnerComponent extends Component {
    constructor(props) {
        super(props)

        this.state =
            {
                items: [],      
                filter: props.filter,
            users: [],
            //usersToGive: props.usersToGive,
            //usersToGiveSet: null,
                firstId: '',
                lastId: '',
            count: 0,
            totalCount: 0,
                errormsg: null,
                givenAtErrors:null,
                returnAtErrors: null,
                timeErrors: null,
            deletedIds: null,
            filteredInventory: '...'
            
            }
        this.refresh = this.refresh.bind(this)
        this.onSubmit = this.onSubmit.bind(this)
    }
   
    componentDidMount() {      
        let search = this.getSearch();
        if(search != null)
            this.refresh(search)       
    } 

    getSearch() {   

        this.nullifyErrors();

        if (!this.state.filter) { this.showError("loading error !!!"); return }
        
        if (!this.state.filter.productDetailId || //!this.state.filter.givenAfter ||
            this.state.filter.productDetailId == 'undefined'// || this.state.filter.givenAfter!= 'undefined'
        ) {
            let msg = "select inventory to activate this search, and preferably limit timeline ";
            this.showError(msg);
            return null;
        }

        let givenAfter = this.state.filter.givenAfter && this.state.filter.givenAfter != 'undefined' ? this.state.filter.givenAfter : null;
        let returnedBefore = this.state.filter.returnedBefore && this.state.filter.returnedBefore != 'undefined' ? this.state.filter.returnedBefore : null;
        console.log("givenAfter = " + givenAfter);
        console.log("returnedBefore = " + returnedBefore);
        let search = `?productDetailId=` + this.state.filter.productDetailId;//
        search += givenAfter ? `&givenAfter=` + givenAfter : '';
        search += returnedBefore ? `&returnedBefore=` + returnedBefore : ``;
        console.log("timeLineSearch = " + search);
        return search;        
    }

    nullifyErrors = () => {      
            this.setState({
                errormsg: null,
                givenAtErrors: null,
                returnAtErrors: null,
                timeErrors: null
            })

        if (this.myInterval)
            clearInterval(this.myInterval)

    }

    nulifyErrors(setFieldValue) {
        setFieldValue("givenAtErrors", null);
        setFieldValue("returnAtErrors", null);
        setFieldValue("timeErrors", null);
        this.nullifyErrors();
    }   

    showError(msg , time) {
       // let time = 10;
        time = time ? time : 10;
        this.setState({
            errormsg: msg,
        })
        this.myInterval = setInterval(() => {
            time = time - 1;
            if (time == 0) {
                this.setState(({ errormsg }) => ({
                    errormsg: null
                }))
                clearInterval(this.myInterval)
            }
        }, 1000)
    }

    componentWillUnmount() {
        clearInterval(this.myInterval)
    }      

    refresh(search) {
        UserProfileDataService.retrieveTimeline(search)
            .then(
            response => {
                console.log("response = " + JSON.stringify(response))
                let users = this.state.users;
               // console.log("users.length = " + users.length);
               // console.log("users.length <1 = " + (users.length < 1));
               // console.log("users.length ==0 = " + (users.length == 0));
                if (users.length < 1) {   
                   // console.log("pushing new select into users ");
                    users = this.state.filter.userNames;
                   users.push(response.data.select);
                    this.setState({ users: users})
                }   
                this.setStateFromResponse(response);
                   /* this.setState({
                        items: response.data.items || [],
                        firstId: response.data.firstId,
                        lastId: response.data.lastId,
                        count: response.data.count,
                        totalCount: response.data.totalCount,
                       
                        msg: response.data.message,
                        filteredInventory: this.getFilteredInventoty()
                });*/

                this.props.updateLink(search)
                }
        ).catch((error) => {
            console.log("error = " + JSON.stringify(error))
            let msg = Function.getErrorMsg(error);
            this.showError(msg)             
            })
    }

    setStateFromResponse(response) {
        this.setState({
            items: response.data.items || [],
            firstId: response.data.firstId,
            lastId: response.data.lastId,
            count: response.data.count,
            totalCount: response.data.totalCount,

            msg: response.data.message,
            filteredInventory: this.getFilteredInventoty()
        });
    }

    getFilteredInventoty() {  //original
        let filteredInventory =
            this.state.filter.inventoryNumbers.find(n => n.value == this.state.filter.productDetailId).name ;
        return filteredInventory

        /*(this.state.filter.productDetailId) ?
            this.state.filter.inventoryNumbers.find(n => n.value == this.state.filter.productDetailId)//.name
            : null;
        return filteredInventory ? filteredInventory.name : ''*/
    }

    getNewFilter(filter) {
        //console.log("gotnew Filter = " + JSON.stringify(filter))
        let originFilter = this.state.filter;
        originFilter.productDetailId = filter.productDetailId;
        originFilter.givenAfter = filter.givenAfter;
        originFilter.returnedBefore = filter.returnedBefore;
       
        this.setState({ filter: originFilter },
            () => {
               // console.log("this.state.filter.returnedBefore = " + (this.state.filter.returnedBefore));
                let search = this.getSearch();
                if (search != null) {
                    this.refresh(search)
                }    
            }
        );
         
    }

    convertDate(g) {
        if (g == null) return
        g = new Date(g);
        g.setHours(g.getHours() - g.getTimezoneOffset() / 60);
        return g;
    }
    onSubmit(values) {       
       // values.items.map(i => {
       //     this.convertDate(i.givenAt);
       //     this.convertDate(i.returnedBefore)          
        // })  
        //console.log("values = " + values.items[0].givenAt);
        //console.log("values after stringify = " + JSON.stringify(values));
        /*let item = [...values];
        item.submitProductDetailId = this.state.filter.productDetailId;
        item.submit*/
        values.submitProductDetailId = this.state.filter.productDetailId;
        values.submitGivenAfter = this.convertDate(this.state.filter.givenAfter);
        values.submitReturnedBefore = this.convertDate(this.state.filter.returnedBefore);

        console.log("values to submit = " + JSON.stringify(values));


        UserProfileDataService.saveTimeline(values)
            .then( response => {               
                // let msg = response.data + ' items saved';
                let msg = response.data.items.length + 'items saved';
                msg += values.deletedIds && values.deletedIds.length > 0 ? ', ' + values.deletedIds.length + ' deleted' : '';
                msg += ' successfully';
                this.setState({ message: msg }) 
                this.setStateFromResponse(response) 
                             
            }).catch((error) => {               
                let msg = Function.getErrorMsg(error);             
                this.showError(msg);               
                if (error.response && typeof error.response.data == 'object' && error.response.data.items != null) {                  
                    this.setState({                      
                        items: error.response.data.items,
                        givenAtErrors: error.response.data.givenAtErrors,
                        returnAtErrors: error.response.data.returnAtErrors,
                        timeErrors: error.response.data.timeErrors
                    })
                }
         })
    }

   /* setErrorsItem(errors, i) {
        if (errors.items == null) errors.items = [];
        if (errors.items[i] == null) errors.items[i] = {};
        return errors
    }*/

    validate(values) {
        console.log("validating");
        let errors = {}
        values.items.map((item, i) => {
                    
            if (item.givenAt == null) {
                if (errors.items == null) errors.items = [];
                if (errors.items[i] == null) errors.items[i] = {};
                errors.items[i].givenAt = 'field is required !!!'
            }
            if ((values.items.length - 1) != i && item.returnedAt == null) {
                if (errors.items == null) errors.items = [];
                if (errors.items[i] == null) errors.items[i] = {};
                errors.items[i].returnedAt = 'field is required !!!'
            }
            if (item.returnedAt != null && new Date(item.returnedAt) < new Date(item.givenAt)) {
                if (errors.items == null) errors.items = [];
                if (errors.items[i] == null) errors.items[i] = {};
                errors.items[i].returnedAt = "can't be before given at !!!"
            }
            if (item.returnedAt != null && new Date(item.returnedAt) > new Date()) {
                if (errors.items == null) errors.items = [];
                if (errors.items[i] == null) errors.items[i] = {};
                errors.items[i].returnedAt = "can't be after today !!!"
            }

            if (new Date(item.givenAt) > new Date()) {
                if (errors.items == null) errors.items = [];
                if (errors.items[i] == null) errors.items[i] = {};
                errors.items[i].givenAt = "can't be after today !!!"
            }

            if (!item.userId || item.userId == 'undefined') {
                if (errors.items == null) errors.items = [];
                if (errors.items[i] == null) errors.items[i] = {};
                errors.items[i].userId = "user not selected !!!"
            }
        })     
        console.log("done validating");
        console.log("errors = " + JSON.stringify(errors));
        return errors
    }

    onprofileRemove(i, values, setFieldValue) {
      //  this.nulifyErrors(setFieldValue);      
        let items = values.items;
        let deletedIds = values.deletedIds;
        if (items[i].id != null) {
            if (deletedIds == null) deletedIds = [];
            deletedIds.push(items[i].id)
        }
        items.splice(i, 1);      
        setFieldValue("items", items);
        setFieldValue("deletedIds", deletedIds);
    }

    onprofileAdd(i, values, setFieldValue) {
       
       // this.nulifyErrors(setFieldValue);     
        let items = values.items;
        if (items.length == 25) {
            this.showError("maximum number reached !!!");
            return;
        }
        let item = { id: null, productDetailId: this.state.filter.productDetailId, userId: null, givenAt: null, returnedAt: null };
        items.splice(i + 1, 0, item); //arr.splice(index, 0, item); will insert item into arr at the specified index (deleting 0 items first,
        setFieldValue("items", items);
    }

   

    getStringDate(date) {
        date = this.convertDate(date);
        date = date.toISOString();
        date = date.substring(0, date.indexOf('T'));
        return date;
    }

    checkDeleted(selected) {
        //if (this.checkDeleted(selected)) return;
        let found = this.state.users.find(n => n.value == selected.value);
        if (found && found.filterBy) {
            // up.error = "user is deleted, can't assign him new inventories !!! ";
            //this.setState({ profileShow: up })
            this.showError("user is deleted, can't assign him new profiles !!! ", 5);
            // showError(msg)
            return true;
        }
        return false
    }

    checkIsValidDate(date, id) {
       // let deletedDate = null;
        let found = this.state.users.find(n => n.value == id);
        if (found) {
            //deletedDate = found.filterBy;
            if (date > new Date(found.filterBy)) {
                this.showError("can't assign date greater than deletion date of the user !!! ", 5);
                return false;
            }
            

        }

        return true;

    }

    render() {

        let { items, firstId, lastId, count, givenAtErrors, returnAtErrors, timeErrors, deletedIds } = this.state;       
        let minDate = this.state.items[0] && new Date(this.state.items[0].givenAt);
        let length = this.state.items.length;
        let maxDate = (length > 0 && this.state.items[length - 1].returnedAt && new Date(this.state.items[length - 1].returnedAt))|| new Date();
       
        return (
            <Formik
                initialValues={{ items, firstId, lastId, count, givenAtErrors, returnAtErrors, timeErrors , deletedIds}}
                    onSubmit={this.onSubmit}
                    validateOnChange={false}
                    validateOnBlur={false}
                    validate={this.validate}
                    enableReinitialize={true}
            >
                {({ setFieldValue, values, dirty }) => (                       
                    <Form>
                        {//console.log("values = " + JSON.stringify(values))
                        }
                            <div className="pt-2 px-2 mx-3 d-flex flex-wrap ">
                                <div >
                                <button className="button btn-mybtn" style={{ padding: ".3rem 1.8rem .4rem 1.8rem" }}
                                    disabled={!dirty} type="submit">Save changes</button>
                                <button className="button btn-delete" style={{ padding: ".3rem 1.8rem .4rem 1.8rem" }}
                                    type="reset" onClick={() => { this.props.updateTimeline(false) }}>Cancel</button>
                                </div>
                                 <div className="ml-auto mr-5">
                                <div >
                                    <label className="pager-label mr-3">showing&nbsp;{this.state.items.length>0?1:0}-{this.state.count}&nbsp; of &nbsp; {this.state.totalCount}</label>
                                    <label className="pager-label mr-3">for &nbsp;{this.state.filteredInventory}</label>
                                     </div>
                                      {this.state.msg && <p style={{ fontSize: "70%", marginLeft:"30%" }}>  {this.state.msg}  </p>}
                                 </div>
                             </div>
                        {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                        {this.state.message && <div className="alert alert-success d-flex">{this.state.message}</div>}
                                <table className="table border-bottom my-table mt-2">
                                 <thead>
                                 <tr className="">
                                    <th style={{ width: "30px" }}></th>
                                    <th className="wl">user</th>
                                    <th className="w20">given at</th>
                                    <th className="w20">returned at</th>
                                    <th ></th>
                                        </tr>
                            </thead></table>
                        {values.items && values.items.map((item, i) =>
                                <fieldset key={item.id || i} className="d-flex align-items-top pb-1 pt-1 w100">                                   
                                  <div className="inline" style={{ width: "30px" }}>{i + 1}-</div>
                                    <div className="inline wl">                                           
                                            <CustomSelect
                                        className={"inline inline-3"}
                                        items={this.state.users}
                                        value={values.items[i].userId}
                                        onChange={(selected) => {
                                            this.nullifyErrors(setFieldValue);
                                            if (this.checkDeleted(selected)) return;
                                           /* let found = this.state.users.find(n => n.value == selected.value);
                                            if (found && found.filterBy) {
                                                // up.error = "user is deleted, can't assign him new inventories !!! ";
                                                //this.setState({ profileShow: up })
                                                this.showError("user is deleted, can't assign him new profiles !!! ", 2);
                                                // showError(msg)
                                                return;
                                            }*/
                                            setFieldValue(`items.${i}.userId`, selected.value)
                                        }} />
                                    <ErrorMessage name={`items.${i}.userId`} component="div"
                                        className="alert alert-warning mbt-01 p-2 inline-3" /> 
                                    </div>
                                    <div className="inline w20">                                          
                                    <DatePicker className="form-control inline-2"
                                        dateFormat="dd MMMM yyyy"
                                        locale="en-GB"
                                        disabled={item.id == firstId}
                                        minDate={minDate}
                                        maxDate={maxDate}
                                        selected={(values.items[i].givenAt && new Date(values.items[i].givenAt)) || null}
                                        onChange={date => {
                                            this.nullifyErrors(setFieldValue);
                                            setFieldValue(`items.${i}.givenAt`, this.getStringDate(date))
                                        }} />
                                    <ErrorMessage name={`items.${i}.givenAt`} component="div"
                                            className="alert alert-warning mbt-01 p-2 inline-2" /> 
                                        {values.givenAtErrors instanceof Array
                                            && values.givenAtErrors[i]
                                            &&<div className="alert alert-warning mbt-01 mr-3 p-1 pl-2">
                                            {values.givenAtErrors[i]}</div>
                                        }
                                    </div>
                                    <div className="inline w20">                                            
                                    <DatePicker className="form-control inline-2"
                                        dateFormat="dd MMMM yyyy"
                                        locale="en-GB"
                                        disabled={item.id == lastId}
                                        maxDate={maxDate}
                                        selected={(values.items[i].returnedAt && new Date(values.items[i].returnedAt)) || null}
                                        onChange={date => {
                                            this.nullifyErrors(setFieldValue);
                                            if (!this.checkIsValidDate(date, values.items[i].userId)) return;
                                            setFieldValue(`items.${i}.returnedAt`, this.getStringDate(date))
                                        }} />
                                        <ErrorMessage name={`items.${i}.returnedAt`} component="div"
                                            className="alert alert-warning mbt-01 p-2 inline-2"/>
                                        {values.returnAtErrors instanceof Array
                                            && values.returnAtErrors[i]
                                            &&<div className="alert alert-warning mbt-01 mr-3 p-1 pl-2">
                                                {values.returnAtErrors[i]}</div>
                                        }
                                        {values.timeErrors instanceof Array
                                        && values.timeErrors[i]
                                        &&<div className="alert alert-warning mr-3 mbt-01 p-1 pl-2">
                                            {values.timeErrors[i]}</div>
                                    }
                                    </div>
                                    <div className="inline">                                       
                                        <button className="btn btn-mybtn btn-delete m-0 ml-1" type="button"
                                            disabled={item.id == this.state.firstId || item.id == this.state.lastId}
                                        onClick={() => {
                                            this.nullifyErrors(setFieldValue);
                                            this.onprofileRemove(i, values, setFieldValue);
                                            }}><i class="fa fa-close ml-auto"></i></button>                                       
                                        {values.items.length - 1 != i &&
                                            <button className="btn btn-mybtn m-0 ml-1" type="button"
                                        onClick={() => {
                                            this.nullifyErrors(setFieldValue);
                                            this.onprofileAdd(i, values, setFieldValue);
                                                }}><i class="fa fa-plus ml-auto"></i></button>
                                        }
                                    </div>
                                    </fieldset>
                                    )} 
                        </Form>
                    )}
                </Formik>
        )
    }
}

export default TimelineInnerComponent