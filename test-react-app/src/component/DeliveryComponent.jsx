import React, { Component } from 'react';
import { Formik, Form, Field, FieldArray, ErrorMessage } from 'formik';
import DeliveryDataService from '../service/DeliveryDataService';
import '../myStyles/Style.css';
import CustomSelect from './Filters/CustomSelect';
import DatePicker from "react-datepicker";

class DeliveryComponent extends Component {
    constructor(props) {
        super(props)
       this.state = {
            id: this.props.match.params.id,
            deliveryView: this.props.match.params.deliveryView || '',
            number: '',
            date: '',
            supplierId: '',
            suppliers: [],
            deliveryDetailEditVMs: [],
            products: [],
            index: '',
            ddEditVMerror: {},           
        }
        this.onSubmit = this.onSubmit.bind(this)
        this.validate = this.validate.bind(this)
        this.cancelForm = this.cancelForm.bind(this)
    }

    componentDidMount() {
        DeliveryDataService.retrieve(this.state.id)
            .then(response => {
                 this.setState({
                    supplierId: response.data.supplierId || '',  
                    number: response.data.number, 
                    date: response.data.date || new Date(),
                    suppliers: response.data.suppliers,  
                    deliveryDetailEditVMs: response.data.deliveryDetailEditVMs || [],
                    products: response.data.products || []                    
                });
            })
    }

   // onSubmit = {(values, { setFieldValue })
    onSubmit(values, actions) {
       // formikBag.setFieldValue("EnumErrors", null);
        //console.log("in submit delivery");
       // console.log("in submit delivery values.date = " + values.date);//JSON.stringify(values));
       // console.log("in submit actions!=null = " + (formikBag!=null));//JSON.stringify(values));
        console.log("in submit actions = " + JSON.stringify(actions));
       // console.log("in submit values = " + JSON.stringify(values));
        let item = {
            id: this.state.id,
            date: values.date,//.toISOString(),
            number: values.number,
            supplierId: values.supplierId,
            deliveryDetailEditVMs: values.deliveryDetailEditVMs,
            deletedDetailsIds: values.deleteddds,
            targetDate: values.targetDate
        };
        console.log("in submit delivery item = " + JSON.stringify(item));
        DeliveryDataService.save(item)
            .then((response) => {
                console.log("delivery submit response = " + response.data);
               let path = this.state.deliveryView.length > 0 ? '/deliveries?deliveryView=DeliveryDetailView' : '/deliveries';
               this.props.history.push(path)
            }).catch((error) => {
               // preventDefault();
              //  console.log("error.response.data= " + error.response.data);
               // console.log("delivery submit error= " + error);
                //console.log("delivery submit error= " + JSON.stringify(error));
              /*  this.setState({
                    numErrors: error.response.data //arr
                })*/

               /* let errors = values.EnumErrors;
                errors = error.response.data;
                actions.setFieldValue("EnumErrors", errors);*/
               // setErrors

                //values.EnumErrors = error.response.data;
                actions.setFieldValue('EnumErrors', error.response.data);
               // return values
                //console.log("after error submit delivery values.supllier.id = " + values.supplierId);//JSON.stringify(values));
            })
        //console.log("after submit delivery values.supllier.id = " + values.supplierId);//JSON.stringify(values));
    }

    validate(values) {
        console.log("validating : values.supplierId = " + values.supplierId);
        let errors = {}  
        if (!values.date) {
            errors.date = 'date required field !!!'
        }
        if (this.state.id < 0 && (!values.supplierId || values.supplierId == 'undefined')) {
            errors.supplierId = 'required field !!!'
        }       
        if (values.deliveryDetailEditVMs.length < 1) {
            errors.deliveryDetailEditVMs = 'delivery must have products !!!'
        }
        console.log("in validate delivery values errors = " + JSON.stringify(errors));
        return errors
    }

    cancelForm() {
        window.history.back();
    }

    togglemsgbox = () => {
        this.setState({ ddEditVMerror: null })
    }

    onChangeQuantity(e, values, setFieldValue) {
        console.log("in on count change ");
        const productNums = [...values.ddEditVM.productNums];
        const quantity = e.target.value || 0;
        const previousQuantity = parseInt(values.ddEditVM.quantity || '0');
     if (previousQuantity < quantity) {
        for (let i = previousQuantity; i < quantity; i++) {
            productNums.push({ value: '', name: '' });
        }
     } else {
        for (let i = previousQuantity; i >= quantity; i--) {
            let num = productNums.splice(i, 1);
            if (num[0] !== undefined && values.ddEditVM.id !== '') {
               if ( num[0].value !== '') {
                    let deletedNums = values.ddEditVM.deletedNums || [];
                    deletedNums.push(num[0].value);
                    setFieldValue("ddEditVM.deletedNums", deletedNums);                   
                } else {
                    let updatedNums = values.ddEditVM.updatedProductNums || [];
                    let index = updatedNums.findIndex(x => x.value == '-' + i);
                    if (index > -1) updatedNums.splice(index, 1);
                    setFieldValue("ddEditVM.updatedProductNums", updatedNums);
                }
            }
        }}
        setFieldValue("ddEditVM.productNums", productNums);
        setFieldValue("ddEditVM.quantity", e.target.value);
    }

    onUpdateProductNums(e, id, values, setFieldValue) {
        let updatedProductNums = values.ddEditVM.updatedProductNums || [];        
        let index = updatedProductNums.findIndex(x => x.value == id);
         if (index < 0)
            updatedProductNums.push({ value: id, name: e.target.value });
        else
            updatedProductNums[index].name = e.target.value;

        setFieldValue(`ddEditVM.updatedProductNums`, updatedProductNums)
    }

    resetddEditVMForm(values, setFieldValue) {
        let VM = values.ddEditVM;
        VM = {
            id: '', productId: 'undefined', productName: '', quantity: '', pricePerOne: '', productNums: []
        };
        setFieldValue("ddEditVM", VM);
        setFieldValue("index", '');
        setFieldValue("ddEditVMerror", {});
        setFieldValue("productNumErrors", []);
    }

    render() {
       
        let { id, number, date, supplierId, suppliers, deliveryDetailEditVMs, products, index, ddEditVMerror} = this.state ;
        let ddEditVM = { id: '', productId: '', productName: '', quantity: '', pricePerOne: '', productNums: [], updatedProductNums: [], deletedNums: []};  
        let deleteddds = [];    
        let productNumErrors = []; // client side validation
        let EnumErrors = null;//[];// from server

        console.log("rendering");
        return (
            <div className="container pt-5">
                {this.state.id > 0 ? <h3 className="mb-3"> Update Delivery</h3> : <h3 className="mb-3"> Add New Delivery </h3>}
                <Formik
                    initialValues={{
                        id, number, date, supplierId, suppliers, deliveryDetailEditVMs, products, index, ddEditVMerror, ddEditVM, deleteddds
                        , productNumErrors, EnumErrors
                    }}
                    //onSubmit={this.onSubmit}
                    // onSubmit={(values, { setFieldValue }) => this.onSubmit(values, { setFieldValue })}
                    onSubmit={async (values, actions) => {

                       // await new Promise(resolve => setTimeout(resolve, 500));
                        this.onSubmit(values,actions)
                       /* actions.setFieldValue("email", values.email);
                        actions.setFieldValue("name", "");
                        actions.setSubmitting(false);*/
                    }}
                    validateOnChange={true}
                    validateOnBlur={true}
                    validate={this.validate}
                    enableReinitialize={true}
                >
                    {({ dirty, isSubmitting, touched, setFieldValue, values }) => (
                            <Form>
                                <Field className="form-control" type="text" name="id" hidden />
                                <fieldset className="form-group">
                                    <label>number</label>
                                    <Field className="form-control inline-2-5" type="text" name="number" disabled
                                    />                                   
                                </fieldset>
                                <fieldset className="d-flex align-items-top">
                                <fieldset className="d-inline-block">
                                    <label className="required-field">date</label>
                                    <div>
                                    <DatePicker
                                            className="form-control inline-2-5"
                                            dateFormat="dd MMMM yyyy"
                                            locale="en-GB"
                                            selected={(values.date && new Date(values.date))}
                                            maxDate={new Date()}
                                            onChange={date => {
                                               // console.log("items.x.date = " + values.date);
                                                //console.log("date changed = " + date);
                                                setFieldValue("date", date);
                                                //console.log("values.date = "+values.date);
                                            }} />                                   
                                        </div>
                                        <ErrorMessage name="date" component="div"
                                            className="alert alert-warning mbt-01" />
                                </fieldset>                                   
                                    <fieldset className="d-inline-block px-5">
                                    <label className="required-field">supplier</label>
                                    <div>
                                        {console.log("values.supplierId" + values.supplierId)}
                                    <CustomSelect
                                                id="supplierId"
                                                name="supplierId"
                                                className={"d-inline-block inline-4"}
                                                items={suppliers}
                                                value={values.supplierId}
                                                onChange={(value) => {
                                                    setFieldValue("supplierId", value.value);                                                    
                                                }}/>    
                                        </div>  
                                        <ErrorMessage name="supplierId" component="div"
                                            className="alert alert-warning mbt-01" />
                                    </fieldset>
                                </fieldset>
                            {/* ********************************************************  */}

                            <div className="mt-5"><h6>add/update products</h6>
                            <fieldset className="d-flex align-items-top  mb-3">                               
                                  <div className="d-flex align-items-top">
                                    <div className="d-inline px-3">
                                            <label>product :&nbsp;</label>
                                            {console.log("values.ddEditVM.productId" + values.ddEditVM.productId) }
                                        <CustomSelect
                                            className={"d-inline-block inline-2-5"}
                                            items={products}
                                            value={values.ddEditVM.productId}
                                            onChange={(selected) => {   
                                                console.log("selected product id = " + selected.value);
                                                setFieldValue("ddEditVM.productName",selected.label);
                                                setFieldValue("ddEditVM.productId",selected.value);
                                            }}/>
                                        {
                                            values.ddEditVMerror.name &&
                                            <div className="alert alert-warning d-flex mbt-01">{values.ddEditVMerror.name}
                                                <i class="fa fa-close ml-auto pr-3 pt-1"
                                                    onClick={() => setFieldValue("ddEditVMerror.name", null)}></i></div>
                                        }
                                    </div>
                                    <div className="d-inline px-3">
                                        <label className="mb-1">quantity :&nbsp;</label>
                                            <Field className="form-control d-inline p-1 inline-100px" type="number" min="0" name="ddEditVM.quantity"
                                                onChange={e => {                                                                                                 
                                                    this.onChangeQuantity(e, values, setFieldValue)
                                                }}/>
                                        {
                                            values.ddEditVMerror.quantity &&
                                                <div className="alert alert-warning d-flex mbt-01">{values.ddEditVMerror.quantity}
                                                <i class="fa fa-close ml-auto pr-3 pt-1"
                                                    onClick={() => setFieldValue("ddEditVMerror.quantity", null)}></i></div>
                                        }
                                    </div>
                                    <div className="d-inline px-3">
                                        <label className="mb-1">unit price :&nbsp;</label>
                                        <Field className="form-control d-inline p-1 inline-100px" type="number" min="0" name="ddEditVM.pricePerOne" />
                                        {
                                            values.ddEditVMerror.pricePerOne &&
                                                <div className="alert alert-warning d-flex mbt-01">{values.ddEditVMerror.pricePerOne}
                                                <i class="fa fa-close ml-auto pr-3 pt-1"
                                                    onClick={() => setFieldValue("ddEditVMerror.pricePerOne", null)}></i></div>
                                        }
                                    </div>
                                  </div>                                   
                                  <div>
                                    <button className="btn btn-mybtn mr-1" type="button" onClick={() => {                                           
                                        let iserror = false;                                       
                                            if (values.ddEditVM.productId == 'undefined' || values.ddEditVM.productId == '') {
                                                setFieldValue("ddEditVMerror.name", 'required field !!!');
                                                iserror = true
                                            }
                                            else {
                                            values.deliveryDetailEditVMs.map((x, y) => {
                                               if (x.productName == values.ddEditVM.productName && (values.index === '' || (values.index !== '' && values.index !== y))) {
                                                    iserror = true;                                                   
                                                    let ddErrorName = values.ddEditVMerror.name;
                                                    ddErrorName = values.ddEditVM.productName + ' already exist\'s in the list!!!';
                                                    setFieldValue("ddEditVMerror.name", ddErrorName);
                                                }
                                            });
                                            }
                                            if (values.ddEditVM.pricePerOne == '') {
                                                setFieldValue("ddEditVMerror.pricePerOne", 'required field !!!');
                                                iserror = true
                                            }
                                            if (values.ddEditVM.quantity == '') {
                                                setFieldValue("ddEditVMerror.quantity", 'required field !!!');
                                                iserror = true
                                            }
                                            if (values.ddEditVM.quantity > 0) {
                                                let ddErrorNums = values.productNumErrors;
                                                for (let i = 0; i < values.ddEditVM.quantity; i++) {
                                                    if (values.ddEditVM.productNums[i].name.length < 1) {                                                        
                                                         ddErrorNums[i] = 'required field !!!';                                                      
                                                        iserror = true;                                                       
                                                    }
                                                    else if (values.ddEditVM.productNums[i].name.length < 4) {
                                                        ddErrorNums[i] = 'too short !!!';
                                                        iserror = true;
                                                    }
                                                    else {
                                                        let isSame = false;
                                                        for (let j = 0; j < i; j++) {
                                                            if (values.ddEditVM.productNums[i].name == values.ddEditVM.productNums[j].name) {
                                                                isSame = true;
                                                                iserror = true;
                                                            }
                                                        }
                                                        ddErrorNums[i] = isSame ? 'same number exists !!!' : '';
                                                    }                                                 
                                                }
                                                setFieldValue("productNumErrors", ddErrorNums);
                                            }
                                        if(!iserror) {
                                            if (values.index === '') {
                                                let list = values.deliveryDetailEditVMs;
                                                list.push(values.ddEditVM);
                                                  setFieldValue("deliveryDetailEditVMs", list)
                                            }
                                            else {
                                                let list = values.deliveryDetailEditVMs;                                              
                                                list[values.index] = values.ddEditVM; // edited;
                                                setFieldValue("deliveryDetailEditVMs", list);
                                            }                                           
                                           
                                            this.resetddEditVMForm(values, setFieldValue)
                                        }

                                    }}>{values.index !== '' ? "Update" : "Add"}</button>
                                        <button className="btn btn-mybtn btn-delete" type="button" onClick={() => {
                                            this.resetddEditVMForm(values, setFieldValue)                                            
                                        }}>reset</button>
                                  </div>
                                </fieldset>                               
                                <fieldset className="mb-1">
                                    {
                                        values.ddEditVM.productNums &&
                                        values.ddEditVM.productNums.map((num, i) => 
                                            <div key={num.value || i} className="ml-3">                                               
                                                <label className="required-field">inventory number {i + 1} :&nbsp;</label>
                                                <Field
                                                    name={`ddEditVM.productNums.${i}.name`}
                                                    type="text"
                                                    className={'form-control d-inline-block inline-4'}
                                                    onChange={e => {                                                      
                                                        if (values.ddEditVM.id !== '') this.onUpdateProductNums(e, num.value || '-'+i, values, setFieldValue)
                                                        setFieldValue(`ddEditVM.productNums.${i}.name`, e.target.value);                                                       
                                                    }}/>  
                                                {                                                   
                                                    <button className="btn btn-mybtn btn-delete mb-1 ml-1" type="button"
                                                        onClick={() => {
                                                            let list = values.ddEditVM.productNums;                                                           
                                                            list.splice(i, 1)
                                                            setFieldValue("ddEditVM.productNums", list)
                                                           // if (num.value !== '') {
                                                            if (values.ddEditVM.id !== '') {
                                                                let updatedProductNums = values.ddEditVM.updatedProductNums || [];
                                                                let index = updatedProductNums.findIndex(x => x.value === num.value || x.value === `-${i}` );
                                                               
                                                                if (index > -1) {
                                                                    console.log("index = " + index);
                                                                    updatedProductNums.splice(index, 1)
                                                                    setFieldValue("ddEditVM.updatedProductNums", updatedProductNums)
                                                                }/* else {
                                                                    index = updatedProductNums.findIndex(x => x.value === `-${i}`)
                                                                    console.log("index = " + index);
                                                                    updatedProductNums.splice(index, 1)
                                                                    setFieldValue("ddEditVM.updatedProductNums", list)
                                                                }  */                                                        
                                                                let deleted = values.ddEditVM.deletedNums || [];
                                                                deleted.push(num.value)
                                                                setFieldValue("ddEditVM.deletedNums", deleted)
                                                                }
                                                            setFieldValue("ddEditVM.quantity", values.ddEditVM.quantity - 1)
                                                        }}>Delete</button>
                                                }
                                                {/*this.values&&
                                                    this.values.numErrors && this.values.numErrors instanceof Array
                                                    && values.index !== '' && this.values.numErrors[values.index]
                                                    && this.values.numErrors[values.index][i] &&
                                                    <div className="alert alert-warning d-inline ml-1">                                                       
                                                        {this.values.numErrors[values.index][i]}
                                                    </div>
                                                */}
                                                {/*
                                                    this.state.numErrors && this.state.numErrors instanceof Array
                                                    && values.index !== '' && this.state.numErrors[values.index]
                                                    && this.state.numErrors[values.index][i] &&
                                                    <div className="alert alert-warning d-inline ml-1">                                                       
                                                        {this.state.numErrors[values.index][i]}
                                                    </div>
                                                */}
                                                {
                                                    values.EnumErrors && values.EnumErrors instanceof Array
                                                    && values.index !== '' && values.EnumErrors[values.index]
                                                    && values.EnumErrors[values.index][i] &&
                                                    <div className="alert alert-warning d-inline ml-1">                                                       
                                                        {values.EnumErrors[values.index][i]}
                                                    </div>
                                                }
                                                {
                                                    values.productNumErrors[i] &&
                                                    <div className="alert alert-warning d-inline ml-1">                                                        
                                                        {
                                                            values.productNumErrors[i] 
                                                        }
                                                        <i class="fa fa-close ml-3 pt-1"
                                                                onClick={() => setFieldValue(`productNumErrors.${i}`, null)}></i>                                                       
                                                    </div>
                                                }                                                  
                                            </div>
                                        )}
                                </fieldset>
                            </div>
                                {/*************************************************************************/}

                                <fieldset>
                                    <ErrorMessage name="deliveryDetailEditVMs" component="div"
                                        className="alert alert-warning mbt-01" />
                                </fieldset>
                                {/*
                                    this.state.numErrors &&
                                    <div className="alert alert-warning d-flex mbt-01">Errors found, save not successful !!!</div>
                            */}
                            {
                                values.EnumErrors &&
                                <div className="alert alert-warning d-flex mbt-01">Errors found, save not successful !!!</div>
                            }     
                            <div className="mt-3 "><h6 className="required-field">products</h6>
                                {// <p>enum errors</p>
                                }
                                {//values.EnumErrors && <p>values.EnumErrors = {JSON.stringify(values.EnumErrors)}</p>
                                }
                                {//console.log("JSON.stringify(values.EnumErrors) = " + values.EnumErrors ? JSON.stringify(values.EnumErrors):"null") 
                                }
                                {//console.log("1 " + Array.isArray(values.numErrors))
                                }
                                {//values.numErrors && <p>values.numErrors instanceof Array =  { Array.isArray(values.numErrors)?"true":"false"}</p>
                                }
                                {//this.values && this.values.numErrors && <p>this.values.numErrors instanceof Array = {Array.isArray(this.values.numErrors) ? "true" : "false"}</p>
                                }
                                    <table className="table x-Table">
                                        <tbody>
                                            <tr>
                                                <td>Product</td>
                                                <td>Quantity</td>
                                                <td>Unit Price</td>
                                            <td style={{ width: '173px' , padding: '.35rem .5rem' }}></td>
                                             </tr>
                                    {
                                            deliveryDetailEditVMs.map((dd, index) =>
                                                <>
                                            <tr key={index} className={values.index === index ? "table-active" : ""}>
                                                    <td>{dd.productName}</td>
                                                    <td>{dd.quantity}</td>
                                                    <td>{new Intl.NumberFormat("en-GB", {
                                                        style: "currency",
                                                        currency: "BGN",
                                                        maximumFractionDigits: 2
                                                    }).format(dd.pricePerOne)}</td>
                                                        <td style={{ width: '173px' , padding: '.35rem .5rem' }}>
                                                            <button className="btn btn-mybtn mr-1" type="button" onClick={() => { 
                                                    console.log("index = " + index);                                                  
                                                        setFieldValue("index", index);
                                                        setFieldValue("ddEditVM", dd)
                                                    }}>Update</button>
                                                    <button className="btn btn-mybtn btn-delete" type="button"
                                                        onClick={() => {
                                                            let list = values.deliveryDetailEditVMs;                                                            
                                                            list.splice(index, 1)
                                                            setFieldValue("deliveryDetailEditVMs", list)                                                           
                                                            if (dd.id > 0) {
                                                                let deleted = values.deleteddds;
                                                                deleted.push(dd.id)
                                                                setFieldValue("deleteddds", deleted)                                                                
                                                            }
                                                            if (values.index == index) {
                                                                this.resetddEditVMForm(values, setFieldValue)
                                                            }                                                           
                                                        }}>Delete</button>
                                                        </td>
                                                        {
                                                            values.EnumErrors && values.EnumErrors[index] &&
                                                            <td style={{ width: '4%', padding: '.75rem' }}>
                                                            <div className="alert alert-warning d-inline p-2">
                                                                <i class="fa fa-bomb "
                                                                    onClick={() => { }}></i>
                                                                </div>
                                                            </td>
                                                        }
                                                       
                                                        {/*
                                                            values.numErrors && values.numErrors[index]
                                                           // || (values.numErrors && deliveryDetailEditVMs.length == 1 && index == 0 )
                                                            &&
                                                            <td style={{ width: '4%', padding: '.75rem' }}>
                                                            <div className="alert alert-warning d-inline p-2">
                                                                <i class="fa fa-bomb "
                                                                    onClick={() => { }}></i>
                                                                </div>
                                                            </td>
                                                       */ }
                                                       
                                                        {/*
                                                            this.state.numErrors && this.state.numErrors[index] &&
                                                            <td style={{ width: '4%', padding: '.75rem' }}>
                                                            <div className="alert alert-warning d-inline p-2">
                                                                <i class="fa fa-bomb "
                                                                    onClick={() => { }}></i>
                                                                </div>
                                                            </td>
                                                        */}
                                                </tr>
                                                
                                                    </>
                                    )}
                                        </tbody>
                                    </table>                                                  
                                <div className="mt-5 ml-3">
                                    {isSubmitting ? console.log("isSubmitting = true") : console.log("isSubmitting = false")}
                                    {!dirty ? console.log("!dirty = true") : console.log("dirty")}
                                    <button className="btn btn-mybtn p-x-5" disabled={!dirty||isSubmitting} type="submit">Save</button>
                                    <button className="btn btn-mybtn btn-delete px-5 ml-5" type="button" onClick={this.cancelForm}>Cancel</button>
                                </div>
                            </div>
                            </Form>
                        )
                    }
                </Formik>
            </div>
        )
    }
}

export default DeliveryComponent