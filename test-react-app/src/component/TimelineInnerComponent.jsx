import React, { Component } from 'react';
import '../myStyles/Style.css';
import DatePicker from "react-datepicker";
import CustomSelect from './Filters/CustomSelect';
import UserProfileDataService from '../service/UserProfileDataService';
import { Formik, Form, Field, FieldArray, ErrorMessage } from 'formik';


class TimelineInnerComponent extends Component {
    constructor(props) {
        super(props)

        this.state =
            {
                items: [],      
            filter: props.filter,
            users:[],
            firstId: '',
            lastId: '',
            count: '',
            errormsg: null,
            givenAtErrors:null,
            returnAtErrors: null,
            timeErrors: null,
            deletedIds:null
            
            
            }
        this.refresh = this.refresh.bind(this)
        this.onSubmit = this.onSubmit.bind(this)
    }

   
    componentDidMount() {
        

      
        let search = `?productDetailId=` + this.state.filter.productDetailId + `&givenAfter=` + this.state.filter.givenAfter;
        search += this.state.returnedBefore ? `&returnedBefore=` + this.state.filter.returnedBefore : ``;
        this.refresh(search)
    } 

    refresh(search) {
        UserProfileDataService.retrieveTimeline(search)
            .then(
            response => { 
                console.log("response = " + JSON.stringify(response));
                let users = JSON.parse(JSON.stringify(this.state.filter.userNames));
                users.push(response.data.select);
                    this.setState({
                        items: response.data.items || [],
                        firstId: response.data.firstId,
                        lastId: response.data.lastId,
                        count: response.data.count,
                        users:users
                       // pager: response.data.pager,
                    });
                }
        ).catch((error) => {
            let msg = error.response && typeof error.response.data == 'string' ? error.response.data :
                error.response.data.message ? error.response.data.message : error;
                this.setState({
                    errormsg: msg
                })
            })
    }

    getFilteredInventoty() {//original
        let filteredInventory = (this.state.filter.productDetailId) ? this.state.filter.inventoryNumbers.find(n => n.value == this.state.filter.productDetailId).name
            : null;
        return filteredInventory
    }

    onSubmit(values) {
        console.log("in submit");
        /* let item = {
             items: values.items,
             firstId: this.state.firstId,
             lastId: this.state.lastId,
             targetDate: values.targetDate
         }*/

        values.items.map(i => {
            let g = i.givenAt;
            g = new Date(g);
            console.log("g before turn = " + g);
            console.log("type og g = " + typeof g);
            g.setHours(g.getHours() - g.getTimezoneOffset() / 60);
            console.log("g after turn = " + g);

        }
        )
        console.log("values to submit = " + JSON.stringify(values));
        UserProfileDataService.saveTimeline(values)
            .then(
                response => {
                   // console.log("response ok = " + JSON.stringify(response));
                    let msg = response.data + ' items saved';
                    msg += values.deletedIds && values.deletedIds.length > 0 ? ', ' + values.deletedIds.length + ' deleted' : '';
                    msg+= ' successfully';
                    this.props.refresh();
                    this.props.setMessage(msg);
                   // this.props.setMessage(response)
                    


                }
            ).catch((error) => {
                console.log("error = " + error);
               // console.log("json error.response = " + JSON.stringify(error.response));
                let msg = error.response && error.response.data && typeof error.response.data == 'string' ? error.response.data :
                   
                    error.response && error.response.data && typeof error.response.data == 'object'
                        && error.response.data.message != null ? error.response && error.response.data &&error.response.data.message :
                        error.response && error.response.data && typeof error.response.data == 'object'
                        && error.response.data.message == null ? 'errors found !!!' : error;
                //console.log("errormsg = " + msg);
                
                this.setState({ errormsg: msg })

                console.log("json error.response = " + JSON.stringify(error.response));
                if (error.response && typeof error.response.data == 'object' && error.response.data.items != null) {
                    console.log("typeof error.response.data == 'object' ");
                   /* let items = this.state.items;
                    items = error.response.data.items;
                    let givenAtErrors = this.state.givenAtErrors;
                    givenAtErrors = error.response.givenAtErrors;
                    let returnAtErrors = this.state.returnAtErrors;*/
                    this.setState({
                        errormsg: msg,
                        items: error.response.data.items,
                        givenAtErrors: error.response.data.givenAtErrors,
                        returnAtErrors: error.response.data.returnAtErrors,
                        timeErrors: error.response.data.timeErrors
                    })

                   // values.items = error.response.data.items;
                   // values.givenAtErrors = error.response.data.givenAtErrors;
                   // values.returnAtErrors = error.response.data.returnAtErrors;


                }/* else {
                    this.setState({ errormsg: msg })
                }*/
           /* console.log("error = " + error);
            console.log("json error.response = " + JSON.stringify(error.response));
            console.log(" error.response && typeof error.response.data == 'string'  = " + error.response && typeof error.response.data == 'string');
            console.log(" error.response && typeof error.response.data == 'object'  = " + error.response && typeof error.response.data == 'object');*/
            
    })
    }

    validate(values) {
       console.log("values.items.length = "+values.items.length)
        let errors = {}
       
        values.items.map((item, i) => {
                    
            if (item.givenAt == null) {
                if (errors.items == null) errors.items = [];
                if (errors.items[i] == null) errors.items[i] = {};
                errors.items[i].givenAt = 'field is required !!!'
            }
            else if ((values.items.length - 1) != i && item.returnedAt == null) {
                if (errors.items == null) errors.items = [];
                if (errors.items[i] == null) errors.items[i] = {};
                errors.items[i].returnedAt = 'field is required !!!'}
            else if (item.returnedAt != null && new Date(item.returnedAt) < new Date(item.givenAt)) {
                if (errors.items == null) errors.items = [];
                if (errors.items[i] == null) errors.items[i] = {};
                errors.items[i].returnedAt = "can't be before given at !!!"
            }
        })
        console.log("validate errors = " + JSON.stringify(errors))
        return errors
    }

    onprofileRemove(i, values, setFieldValue) {
        this.nulifyErrors(values,setFieldValue);
        console.log("remove profile i = "+i)
        let items = values.items;
        let deletedIds = values.deletedIds;
        if (items[i].id != null) {
            if (deletedIds == null) deletedIds = [];
            deletedIds.push(items[i].id)
        }
        items.splice(i, 1);
       /* this.setState({
            items: items,
            deletedIds: deletedIds
        })*/

        setFieldValue("items", items);
        setFieldValue("deletedIds", deletedIds);
    }
    onprofileAdd(i, values, setFieldValue) {
        console.log("adding profile");
        this.nulifyErrors(values,setFieldValue);
       /* let items = this.state.items;
        let item = { id:null, productDetailId:this.state.filter.productDetailId, userId:null, givenAt:null, returnedAt:null };
        items.splice(i+1, 0, item);//arr.splice(index, 0, item); will insert item into arr at the specified index (deleting 0 items first,
        this.setState({ items: items })*/

        let items = values.items;
        let item = { id: null, productDetailId: this.state.filter.productDetailId, userId: null, givenAt: null, returnedAt: null };
        items.splice(i + 1, 0, item); //arr.splice(index, 0, item); will insert item into arr at the specified index (deleting 0 items first,
        setFieldValue("items", items);

    }

    nulifyErrors(values,setFieldValue) {
        /*let givenAtErrors = this.state.givenAtErrors;
        let returnAtErrors = this.state.returnAtErrors;
        let timeErrors = this.state.timeErrors;
        givenAtErrors = returnAtErrors = timeErrors = null;*/
        /*this.setState({
            givenAtErrors: null,
            returnAtErrors: null,
            timeErrors:null
        })*/
        /*let givenAtErrors = values.givenAtErrors;
        let returnAtErrors = values.returnAtErrors;
        let timeErrors = values.timeErrors;*/
        setFieldValue("givenAtErrors", null);
        setFieldValue("returnAtErrors", null);
        setFieldValue("timeErrors", null);
    }
   

    render() {
        console.log("rendering");

        let { items, firstId, lastId, count, givenAtErrors, returnAtErrors, timeErrors, deletedIds } = this.state;
        //let givenAtErrors= [];
        //let returnAtErrors= [];
        //let deletedIds = [];
        let minDate = this.state.items[0] && new Date(this.state.items[0].givenAt);
        let length = this.state.items.length;
        let maxDate = (length > 0 && this.state.items[length - 1].returnedAt && new Date(this.state.items[length - 1].returnedAt))|| new Date();
        console.log("min = " + minDate);
        return (
            <Formik
                initialValues={{ items, firstId, lastId, count, givenAtErrors, returnAtErrors, timeErrors , deletedIds}}
                    onSubmit={this.onSubmit}
                    validateOnChange={false}
                    validateOnBlur={false}
                    validate={this.validate}
                    enableReinitialize={true}
            >
                {({ setFieldValue, values }) => (                       
                            <Form>
                                <div className="pt-2 px-2 mx-3 d-flex flex-wrap ">
                                    <div >
                                        <button className="button btn-mybtn" style={{ padding: ".3rem 1.8rem .4rem 1.8rem" }} type="submit">Save changes</button>
                                        <button className="button btn-delete" style={{ padding: ".3rem 1.8rem .4rem 1.8rem" }} onClick={() => { }
                                        }>Cancel</button>
                                    </div>
                                    <div className=" d-inline-flex justify-content-end flex-grow-1 mr-5">
                                        <label className="pager-label mr-3">showing&nbsp;{1}-{this.state.count}&nbsp; of &nbsp; {this.state.count}</label>
                                        <label className="pager-label mr-3">for &nbsp;{this.getFilteredInventoty()}</label></div>
                        </div>
                        {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
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
                        {console.log("values.givenAtErrors instanceof Array = " + (values.givenAtErrors instanceof Array))
                        }
                        {console.log("this.state.givenAtErrors = " + JSON.stringify(this.state.givenAtErrors))
                        }
                        {/* <p style={{ fontSize: "75%", marginLeft:"15px" }}> ps : profiles without user name will be added to your profile </p>*/}
                            {
                                values.items &&
                                values.items.map((item, i) =>
                                <fieldset key={item.id || i} className="d-flex align-items-top pb-1 pt-1 w100">
                                    {//console.log("item.id = " + (item.id))
                                    }
                                    {//console.log("JSON this.state.first = " + JSON.stringify(this.state.first))
                                    }
                                    {//console.log(" this.state.first = " + (this.state.first))
                                    }
                                    {//console.log("typof this.state.first = " + (typeof this.state.first))
                                    }
                                    {//console.log("item.id == this.state.first = " + (item.id == this.state.first))
                                    }
                                        <div className="inline" style={{ width: "30px" }}>{i + 1}-</div>
                                    <div className="inline wl">
                                            {/* <label>user&nbsp;</label>*/}
                                            <CustomSelect
                                                className={"inline inline-3"}
                                                items={this.state.users}
                                                value={values.items[i].userId}
                                                onChange={(selected) => setFieldValue(`items.${i}.userId`, selected.value)}
                                        />                                        
                                    </div>
                                    {/*console.log("this.state.givenAtErrors = " + JSON.stringify(this.state.givenAtErrors))*/
                                    }
                                    
                                    <div className="inline w20">
                                            {/*<label className="mb-1">given at&nbsp;</label>*/}
                                            <DatePicker className="form-control inline-2"
                                                dateFormat="dd MMMM yyyy"
                                                locale="en-GB"
                                               // maxDate={values.dateCreatedBefore}
                                            disabled={item.id == firstId}
                                            minDate={minDate}
                                            maxDate={maxDate}
                                            selected={(values.items[i].givenAt && new Date(values.items[i].givenAt)) || null}
                                            onChange={date => setFieldValue(`items.${i}.givenAt`, date)} />
                                        <ErrorMessage name={`${i}.givenAt`} component="div"
                                            className="alert alert-warning mbt-01 mr-3 p-1 pl-2" /> 
                                        {values.givenAtErrors instanceof Array
                                            && values.givenAtErrors[i]
                                            &&
                                            <div className="alert alert-warning mbt-01 mr-3 p-1 pl-2">
                                            {values.givenAtErrors[i]}
                                            </div>
                                        }
                                        </div>
                                    <div className="inline w20">
                                            {/* <label className="mb-1">returned at&nbsp;</label>*/}
                                            <DatePicker className="form-control inline-2"
                                                dateFormat="dd MMMM yyyy"
                                                locale="en-GB"
                                                // maxDate={values.dateCreatedBefore}
                                            disabled={item.id == lastId}
                                            selected={(values.items[i].returnedAt && new Date(values.items[i].returnedAt))|| null}
                                            onChange={date => setFieldValue(`items.${i}.returnedAt`, date)} />
                                        <ErrorMessage name={`items.${i}.returnedAt`} component="div"
                                            className="alert alert-warning mbt-01 mr-3 p-1 pl-2"/>
                                        {values.returnAtErrors instanceof Array
                                            && values.returnAtErrors[i]
                                            &&
                                            <div className="alert alert-warning mbt-01 mr-3 p-1 pl-2">
                                                {values.returnAtErrors[i]}
                                            </div>
                                        }
                                        {values.timeErrors instanceof Array
                                        && values.timeErrors[i]
                                        &&
                                            <div className="alert alert-warning mr-3 mbt-01 p-1 pl-2">
                                            {values.timeErrors[i]}
                                        </div>
                                    }
                                    </div>

                                   
                                    <div className="inline">
                                       
                                        <button className="btn btn-mybtn btn-delete m-0 ml-1" type="button"
                                            disabled={item.id == this.state.firstId || item.id == this.state.lastId}
                                            onClick={() => {
                                                this.onprofileRemove(i, values, setFieldValue);
                                            }}><i class="fa fa-close ml-auto">
                                            </i></button>
                                       
                                        {
                                            values.items.length - 1 != i &&
                                            <button className="btn btn-mybtn m-0 ml-1" type="button"
                                                onClick={() => {
                                                    this.onprofileAdd(i, values, setFieldValue);
                                                }}><i class="fa fa-plus ml-auto">
                                                </i></button>
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