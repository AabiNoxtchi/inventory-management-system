import React, { Component } from 'react';
import DeliveryDataService from '../service/DeliveryDataService';
import ProductDetailDataService from '../service/ProductDetailDataService';
import PaginationComponent from './PaginationComponent';
import DeliveryFilter from './Filters/DeliveryFilter';
import '../myStyles/Style.css';
import { CSVLink } from "react-csv";
//import ListDeliveryDetailInnerComponent from './ListDeliveryDetailInnerComponent';
import InventoryNumberInnerComponent from './InventoryNumberInnerComponent';
import DeliveryInnerComponent from './DeliveryInnerComponent';
import DDInnerComponent from './DDInnerComponent';
import Function from './Shared/Function';
import { Link, Route, withRouter } from 'react-router-dom'

const headers = [
    { label: "number", key: "number" },
    { label: "date", key: "date" },
    { label: "supplier", key: "supplierName" },
    { label: "total bill", key: "total" },  
];
const headers2 = [
    { label: "delivery", key: "number" },
    { label: "date", key: "date" },
    { label: "supplier", key: "supplierName" },    
    { label: "product", key: "productName" },
    { label: "quantity", key: "quantity" },
    { label: "unit price", key: "pricePerUnit" },
];

class ListDeliveriesComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],            
            pager: null,
            filter: {},
            filterkey: 0,
            search: window.location.search || '',
            alldata: [],
            message: null,
            ddmessage: [],
            dderrormessage:[],
            ddShow: [],
            pdShow: [],
            pdmessage: [],
            pdUpdateShow: {
                pd: {}, show: false
            },
            ddUpdateShow: {
                dd: {}, show: false
            },
            
            deliveryUpdateShow: {
                delivery: {},show:false}
           
        }
        this.refresh = this.refresh.bind(this)
        this.deleteClicked = this.deleteClicked.bind(this)      
        this.updateClicked = this.updateClicked.bind(this)
        this.addClicked = this.addClicked.bind(this)
        this.csvLink = React.createRef();
        this.searchLink = React.createRef();
    }

    componentDidMount() {
        console.log("version = " + React.version);
        this.refresh();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        // Check to see if the "applied" flag got changed (NOT just "set")

        console.log("this.props.location = " + JSON.stringify(this.props.location.search));
        console.log("previousprops.location = " + JSON.stringify(prevProps.location.search));
        // console.log("this.state.search = " + this.state.search);

        console.log("this.props.location.search != prevProps.location.search = " + (this.props.location.search != prevProps.location.search));
        if (this.props.location.search != prevProps.location.search) {

            //this.setState({ search: this.props.location.search },
            // () => this.refresh())

            // this.updateSearch(this.props.location.search);
            /* this.setState({ search: this.props.location.search },
                 () => this.refresh())
         }*/
            let newSearch = this.props.location.search;
            if (this.state.filter)
                if (newSearch.indexOf('Filter.filtersSet') < 0) {
                    newSearch += newSearch.length > 1 ? '&' : newSearch.length == 0 ? '?' : '';
                    newSearch += 'Filter.filtersSet=true'
                }

            if (newSearch.indexOf("deliveryView") < 0 && this.state.deliveryView && this.state.deliveryView == 'DeliveryDetailView') {
                let view = 'deliveryView=' + this.state.deliveryView;
                newSearch += newSearch.length > 1 ? "&" + view : view;
            }
               

            console.log("newSearch = " + newSearch);
            // this.setState({ search: newSearch } )

            this.refresh(newSearch);

            //if (this.props.location.state.applied && !prevProps.location.state.applied) {

            //}
        }
    }

    refresh(newsearch) {
        console.log("refreshing ****************deliveries *****************");
        if (!newsearch) newsearch = this.state.search;
        DeliveryDataService.retrieveAll(newsearch)
            .then(
            response => {   
                console.log("refresh got response")
                this.setState({
                    items: response.data.items || response.data.daoitems,
                    pager: response.data.pager,
                    filter: this.getfilter(response.data.filter), //response.data.filter, 
                    filterkey: this.state.filterkey+1,
                   deliveryView: response.data.deliveryView || 'DeliveryView',                   
               });
           }).catch((error) => {
               this.setState({                  
                   errormsg:  ''+ error == 'Error: Request failed with status code 401' ?  'need to login again !!!' : '' + error
               })
           })
    }

    getfilter(newfilter) {
        let filter = this.state.filter;
        if (!filter)
            return newfilter
        else if (!newfilter.filtersSet) {
            return newfilter
        }
        else {

            // newfilter.deliveryNumbers = filter.deliveryNumbers;
            //newfilter.econditions = filter.econditions;
            // newfilter.productNames = filter.productNames;
            newfilter.numbers = filter.numbers;
            newfilter.products = filter.products;
            newfilter.suppliers = filter.suppliers;
            //newfilter.productTypes = filter.productTypes;
            return newfilter//this.state.filter
        }
    }

    downloadReport = () => {
        let newSearch = this.getSearchAll();
        DeliveryDataService.retrieveAll(newSearch)
            .then(response => {               
                let downloadData = [];
                let data = response.data.items || response.data.daoitems;
                data = data.map(row => ({
                    ...row, date: new Intl.DateTimeFormat("en-GB", {
                        month: "numeric",
                        day: "2-digit",
                        year: "numeric",
                    }).format(new Date(row.date))
                }));
                if (this.state.deliveryView == 'DeliveryDetailView') {
                    this.getDetailData(data, downloadData);                   
                } else {
                    downloadData = data
                }
                this.setState({
                    alldata: downloadData
                });
                this.csvLink.current.link.click()
            })
    }

    getDetailData = (data, arr) => {
        data.map(i => {
            i.deliveryDetails.map(x => {
                arr.push({
                    number: i.number,
                    date: i.date,
                    supplierName: i.supplierName,
                    productName: x.productName,
                    quantity: x.quantity,
                    pricePerUnit: x.pricePerOne
                })
            })
        })
    }

    getSearchAll = () => {
        let search = this.state.search;       
        if (search.length > 1) {
            while (search.charAt(0) === '?') {
                search = search.substring(1);
            }
        }
        search.replace(/\s+/g, '');
        let newSearch = '';
        let searchItems = search.split('&');
        for (let i = 0; i < searchItems.length; i++) {
            if (searchItems[i].length < 1 || searchItems[i].startsWith('Pager'))
                continue
            else
                newSearch += searchItems[i] + '&'
        }
        newSearch = '?' + newSearch;
        newSearch += 'Pager.itemsPerPage=2147483647';       
        return newSearch;
    }

    deleteClicked(id, x) {   
        this.nullifyErrors();
        DeliveryDataService.delete(id)
            .then(
            response => {
                let show = this.state.pdShow;
                if (this.state.items[x]) {
                    show.splice(x, 1);                   
                }
                this.setState({
                    message: `Delete successful`,
                    pdShow: show,                    
                })
                    this.refresh()
            }
        ).catch(error => {
            let msg = Function.getErrorMsg(error);           
            this.setState({ errormsg: msg })
            this.showError('errormsg', 8);
        })
    }

   

    showError(field, time) {        
        this.myInterval = setInterval(() => {
            time = time - 1;
            if (time == 0) {               
                this.setState({
                    [field]: null
                })
                clearInterval(this.myInterval)
            }
        }, 1000)
    }

    componentWillUnmount() {
        clearInterval(this.myInterval)
    }

    
    deleteChildClicked(id, x, y, parentid) {
        this.nullifyErrors();
        DeliveryDataService.deleteChild(id, parentid)
            .then(
            response => {
                let length = this.state.items[x].deliveryDetails.length;

                let show = this.state.pdShow;
                if (this.state.pdShow[x] == undefined) show[x] = [];

                /*let ddmessage = this.state.ddmessage;
                let message = this.state.message;
                let pdmessage = this.state.pdmessage;
                pdmessage = [];*/

                if (this.state.pdShow[x][y] == undefined) show[x][y] = {}
                show[x].splice(y, 1);
                let ddmessage = this.state.ddmessage;
                ddmessage[x] = `Delete successful`;
               // message = ''

                this.setState({
                    pdShow: show,
                   // pdmessage: pdmessage,
                    ddmessage: ddmessage,
                   // message: message
                })                
                    this.refresh()
            }
        ).catch(error => {           
            let errormsg = Function.getErrorMsg(error);
            let dderrormessage = this.state.dderrormessage;
            dderrormessage[x] = errormsg;
            this.setState({
                dderrormessage: dderrormessage,
            })
        })
    }

    

    /**********************************/
    
    deletePDChildClicked = (value, x, y, id) => {     
        this.nullifyErrors();
            ProductDetailDataService.delete(value)
                .then(
                    response => {                       
                        ProductDetailDataService.retrieveAllNumbers("?deliveryDetailId=" + id)
                            .then(response => {
                                let show = this.state.pdShow;
                                show[x][y].data = response.data;

                                let items = this.state.items;
                                items[x].deliveryDetails[y].quantity = items[x].deliveryDetails[y].quantity - 1;
                                items[x].total = items[x].total - items[x].deliveryDetails[y].pricePerOne;

                                let pdmessages = this.state.pdmessage;                               
                                if (pdmessages[x] == null) pdmessages[x] = [];
                                pdmessages[x][y] = `Delete successful`;

                                this.setState({
                                    items: items,
                                    pdShow: show,
                                    pdmessage: pdmessages,
                                    //message: '',
                                    //ddmessage:[]
                                })
                            })            
            }).catch(error => {               
                let errorpdmessages = this.state.errorpdmessage;
                if (errorpdmessages == null) errorpdmessages = [];
                if (errorpdmessages[x] == null) errorpdmessages[x] = [];
                errorpdmessages[x][y] = Function.getErrorMsg(error);//`error ` + error;
                this.setState({
                    errorpdmessage: errorpdmessages,
                })
            }) 
    }

    updateClicked(id) {
        this.state.deliveryView == 'DeliveryDetailView' ? 
            this.props.history.push(`/deliveries/${id}/DeliveryDetailView`) :
            this.props.history.push(`/deliveries/${id}`)
    }

    updateClickedInner = (item, x) => {
        this.nullifyErrors();
        let show = this.state.deliveryUpdateShow;
        show.show = !show.show;
        if (show.show == true) {
            show.delivery = JSON.parse(JSON.stringify(item))
            show.x = x;            
        } else {
            show.error = ''
        }
        this.setState({
            deliveryUpdateShow: show,
           // message: null,
           // pdmessage: [],
           // ddmessage:[]
        });    
    }

    

    updateChildClicked = (dd, x, y) => {
        this.nullifyErrors();
        let show = this.state.ddUpdateShow;
        show.show = !show.show;
        if (show.show == true) {
            show.dd = JSON.parse(JSON.stringify(dd))
            show.x = x;
            show.y = y;
           
        } else {
            show.error = ''
        }
        this.setState({
            ddUpdateShow: show,
           // ddmessage: [],
           // pdmessage: [],
           // message:null
        });      
    }
   
    updatePdChildClicked = (pd, x, y, i, ddid) => {
        this.nullifyErrors();
        let show = this.state.pdUpdateShow ;       
        show.show = !show.show;
        if (show.show == true) {          
            show.pd = JSON.parse(JSON.stringify(pd))
            show.x = x;
            show.y = y;
            show.i = i;
            show.ddid = ddid;
        } else {
            show.error = ''
        }
        this.setState({
            pdUpdateShow: show,
            //pdmessage: pdmessages,
           // ddmessage: [],
           // pdmessage: [],
           // message: null
        });      
    }

    nullifyErrors() {

        this.setState({
            pdmessage: [],
            ddmessage: [],
            dderrormessage: [],
            message: null,
        })

    }

    addClicked() {
        this.props.history.push(`/deliveries/-1`)
    }

    togglemsgbox = () => {
        this.setState({ message: null })
    }

    setView(value) {       
        let path = window.location.pathname;
        let newPath = ``;
        let search =  window.location.search;
        if (search.length < 1) {
            newPath =  `?deliveryView=${value.target.value}`;
        }
        else {
            while (search.charAt(0) === '?') {
                search = search.substring(1);
            }
            let searchItems = search.split('&');
            for (let i = 0; i < searchItems.length; i++) {
                if (searchItems[i].startsWith('deliveryView'))
                    continue
                else
                    newPath += searchItems[i] + '&'
            }
            newPath = /*path + */'?' + newPath + 'deliveryView=' + value.target.value;
        }
       // window.location.href = newPath;
        console.log("view path = "+newPath)
        this.updateLink(newPath)
       
    }
    
    getProductDetails( x, y, id) {      
        if (this.state.pdShow[x] == undefined || this.state.pdShow[x][y] == undefined) {
             let show = this.state.pdShow;
            if (this.state.pdShow[x] == undefined) show[x] = [];

            ProductDetailDataService.retrieveAllNumbers("?deliveryDetailId=" + id )
                .then(response => {
                    console.log("pds = " + JSON.stringify(response.data));
                    show[x][y] = {}
                    show[x][y].show = true;
                    show[x][y].data = response.data;
                this.setState({ pdShow: show });
            })
        }
        else {
            let show = this.state.pdShow;
            show[x][y].show = !show[x][y].show;
            this.setState({ pdShow: show })
        }
    }

    showdd = (x) => {
        let ddShow = this.state.ddShow;
        ddShow[x] = !ddShow[x];
        this.setState({ ddShow: ddShow })
    }

    getPageData = () => {
        const data = this.state.items.map(row => ({
            ...row, date: new Intl.DateTimeFormat("en-GB", {
                month: "numeric",
                day: "2-digit",
                year: "numeric",
            }).format(new Date(row.date))
        }));
        if (this.state.deliveryView == 'DeliveryView')
            return data;
        else {
            let downloadData = [];
            downloadData = this.getDetailData(data, downloadData);
            return downloadData;
        }
    }

    updateLink(newSearch) {
        this.setState({ search: newSearch },
            () => this.searchLink.current.click())
    }

    updateSearch(newSearch) {
        // this.setState({ search: newSearch },
        ///     () => this.searchLink.current.click()
        // )
        this.updateLink(newSearch);
       /* if (newSearch.indexOf('Filter.filtersSet') < 0) {
            newSearch += newSearch.length > 1 ? '&' : newSearch.length == 0 ? '?' : '';
            newSearch += 'Filter.filtersSet=true'
        }

        console.log("newSearch = " + newSearch);
        // this.setState({ search: newSearch } )

        this.refresh(newSearch);*/
    }

    render() {
        const { match } = this.props;
        const url = match.url;

        const data = this.state.items.map(row => ({
            ...row, date: new Intl.DateTimeFormat("en-GB", {
                month: "numeric",
                day: "2-digit",
                year: "numeric",
            }).format(new Date(row.date))
        })); 
        const data2 = [];
        if (this.state.deliveryView !== 'DeliveryView') {
           this.getDetailData(data, data2);}
       
        return (
            <div className="px-3 pt-3">

                <Link ref={this.searchLink} to={`${url}${this.state.search}`}></Link>
                <Route path={`${url}/:search`}>
                    <p></p>
                </Route>
                {console.log("rendering")}
                {this.state.pdUpdateShow && this.state.pdUpdateShow.show == true &&
                    <InventoryNumberInnerComponent
                    pdUpdateShow={this.state.pdUpdateShow}
                    pdShow={this.state.pdShow}
                    pdmessage={this.state.pdmessage}
                    updatePdChildClicked={() => this.updatePdChildClicked(null)}
                    setpdShow={(value) => this.setState({ pdShow: value })}
                    setpdMessage={(value) => this.setState({ pdmessage: value })}
                    refresh={() => this.refresh()}
                    />}
                {this.state.deliveryUpdateShow && this.state.deliveryUpdateShow.show == true &&
                    <DeliveryInnerComponent
                    deliveryUpdateShow={this.state.deliveryUpdateShow}
                    items={this.state.items}
                    message={this.state.message}
                    suppliers={this.state.filter.suppliers}
                    updateClickedInner={() => this.updateClickedInner(null)}
                    setItems={(value) => this.setState({ items: value })}
                    setMessage={(value) => this.setState({ message: `update successful` })}
                    />}
                {this.state.ddUpdateShow && this.state.ddUpdateShow.show == true &&
                    <DDInnerComponent
                        ddUpdateShow={this.state.ddUpdateShow}
                        items={this.state.items}
                        ddmessage={this.state.ddmessage}
                        products={this.state.filter.products}
                        updateChildClicked={() => this.updateChildClicked(null)}
                        setItems={(value) => this.setState({ items: value })}
                        setddMessage={(value) => this.setState({ ddmessage: value})}
                    />}               
                {this.state.filter && <DeliveryFilter {...this.state.filter}
                    search={this.state.deliveryView == 'DeliveryView' ? this.state.search : null}
                    key={this.state.filterkey}
                    onNewSearch={(search) =>
                        this.updateSearch(search)
                    }/>}
                <div className="border">
                    <div className="panel-heading">
                        <h5 className="panel-title p-2 d-inline-flex">
                            <strong> Deliveries</strong>
                        </h5>
                        <label className="mx-1 pull-right mr-5">
                            <input className="mx-1 m-2 mt-3 " type="radio" name="DeliveryView" value="DeliveryDetailView"
                                checked={this.state.deliveryView == 'DeliveryDetailView'}
                                onClick={
                                    (value) => this.state.deliveryView != 'DeliveryDetailView' ?                              
                                    this.setView(value) : {}} />Delivery&Details View
                        </label>
                        <label className="mx-1 pull-right mr-5">
                            <input className="mx-1 m-2 mt-3 " type="radio" name="DeliveryView" value="DeliveryView"
                                checked={this.state.deliveryView == 'DeliveryView'}
                                onClick={(value) => this.state.deliveryView == 'DeliveryDetailView' ? this.setView(value) : {}}
                            />Delivery View
                        </label>
                    </div>
                    <div className="p-1">
                        <div className=" pt-3 px-2 mx-3 d-flex flex-wrap">
                            <div>
                                <button className="btn btn-mybtn px-5  " onClick={this.addClicked}>Add New</button>
                                <CSVLink
                                    className="btn btn-mybtn px-3 ml-2"
                                    data={this.state.deliveryView == 'DeliveryView' ? data : data2}
                                    headers={ this.state.deliveryView == 'DeliveryView' ? headers : headers2 } filename={"deliveries-page.csv"}
                                    asyncOnClick={true}
                                    onClick={() => {}}
                                >Download this page
                                </CSVLink>
                                <button className="btn btn-mybtn px-3 ml-2" onClick={this.downloadReport}>Download All</button>
                                <CSVLink
                                    data={this.state.alldata}
                                    filename={"deliveries-all.csv"}
                                    className="hidden"
                                    headers={this.state.deliveryView == 'DeliveryView' ? headers : headers2}
                                    ref={this.csvLink}
                                    target="_blank"
                                />
                            </div>
                            {this.state.pager && <PaginationComponent {...this.state.pager}
                                search={this.state.deliveryView == 'DeliveryView' ? this.state.search : null}
                                onNewSearch={(search) =>
                                    this.updateSearch(search)
                                }/>
                            }
                        </div>
                        {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                        {
                            this.state.message &&
                            <div className="alert alert-success d-flex">{this.state.message}
                                <i class="fa fa-close ml-auto pr-3 pt-1" onClick={this.togglemsgbox}></i>
                            </div>
                        } 
                        {this.state.deliveryView == 'DeliveryDetailView' &&                            
                                    this.state.items.map(
                                        (item, x) =>
                                            <div className="panel-body">
                                                <table className="table  border table-s">
                                                    <tbody>
                                                        <tr>
                                                            <th className="wl pl-5"
                                                                onClick={() => {
                                                                   // this.showdd(x)
                                                                }}> Number : {item.number} </th>
                                                            <th className="wl pl-3"                                                               
                                                            > Date : {
                                                                new Intl.DateTimeFormat("en-GB", {
                                                                    month: "long",
                                                                    day: "2-digit",
                                                                    year: "numeric",                                                                  
                                                                }).format(new Date(item.date))
                                                            }</th>
                                                            <th className=" wl hoverable"
                                                                onClick={() => {
                                                                    this.props.history.push(`/suppliers?Filter.name=${item.supplierName}`)
                                                                }}> Supplier : {item.supplierName || '-'}</th>
                                                            <th className="d-flex justify-content-end mr-1">
                                                                <button className="btn btn-mybtn mr-1" onClick={() => this.updateClickedInner(item, x)}>Update</button>
                                                                <button className="btn btn-mybtn btn-delete" onClick={() => this.deleteClicked(item.id, x)}>Delete</button>
                                                            </th>
                                                         </tr>
                                                         <tr>
                                                            <td colspan="4">
                                                                {//this.state.ddShow && this.state.ddShow[x] &&
                                                                    <>
                                                                    {this.state.ddmessage && this.state.ddmessage[x] &&
                                                                        <div className="alert alert-success d-flex">{this.state.ddmessage[x]}
                                                                            <i class="fa fa-close ml-auto pr-3 pt-1" onClick={() => {
                                                                                let ddmessages = this.state.ddmessage;
                                                                                ddmessages[x] = null;
                                                                                this.setState({ ddmessage: ddmessages })
                                                                            }}></i>
                                                                        </div>
                                                                    }                                                                        
                                                                    {this.state.dderrormessage && this.state.dderrormessage[x] &&
                                                                        <div className="alert alert-warning d-flex">{this.state.dderrormessage[x]}
                                                                            <i class="fa fa-close ml-auto pr-3 pt-1" onClick={() => {
                                                                                let dderrormessages = this.state.dderrormessage;
                                                                                dderrormessages[x] = null;
                                                                                this.setState({ dderrormessage: dderrormessages })
                                                                            }}></i>
                                                                        </div>
                                                                    }
                                                                        <table className="table border x-Table ">
                                                                            <tbody>
                                                                                <tr>
                                                                                    <td>Product</td>
                                                                                    <td>Quantity</td>
                                                                                    <td>Unit Price</td>
                                                                                    <td>Total</td>
                                                                                <td style={{ width: '140px', padding: '.2rem .5rem' }}>
                                                                                    <button className="btn btn-mybtn pull-right" style={{ padding: '.15rem .6rem' }}
                                                                                     onClick={() => this.updateChildClicked(null, x, null)}>add one</button>
                                                                                </td>
                                                                                </tr>
                                                                                {item.deliveryDetails.map((dd, y) =>
                                                                                <>
                                                                                    <tr key={dd.id}>
                                                                                        <td><p className="hoverable"
                                                                                            onClick={() => {
                                                                                                this.props.history.push(`/products?Filter.name=${dd.productName}`)//this.getProductDetails(x, y, dd.id)
                                                                                            }}>{dd.productName}</p></td>
                                                                                        <td className="hoverable"
                                                                                            onClick={() => {
                                                                                                this.getProductDetails(x, y, dd.id)
                                                                                            }}><p>{dd.quantity}</p></td>
                                                                                        <td>{new Intl.NumberFormat("en-GB", {
                                                                                            style: "currency",
                                                                                            currency: "BGN",
                                                                                            maximumFractionDigits: 2
                                                                                        }).format(dd.pricePerOne)}</td>
                                                                                        <td>{new Intl.NumberFormat("en-GB", {
                                                                                            style: "currency",
                                                                                            currency: "BGN",
                                                                                            maximumFractionDigits: 2
                                                                                        }).format(dd.pricePerOne * dd.quantity)}  </td>
                                                                                        <td><button className="btn btn-mybtn mr-1" onClick={() => this.updateChildClicked(dd, x, y)}>Update</button>
                                                                                            <button className="btn btn-mybtn btn-delete" onClick={() => this.deleteChildClicked(dd.id, x, y, item.id)}>Delete</button></td>
                                                                                    </tr>
                                                                                    {this.state.pdShow[x] && this.state.pdShow[x][y] && this.state.pdShow[x][y].show &&
                                                                                        <tr>
                                                                                            <td colspan="5">
                                                                                                <>
                                                                                                    {this.state.pdmessage[x] && this.state.pdmessage[x][y] &&
                                                                                                        <div className="alert alert-success d-flex">{this.state.pdmessage[x][y]}
                                                                                                            <i class="fa fa-close ml-auto pr-3 pt-1"
                                                                                                                onClick={() => {
                                                                                                                    let pdmessages = this.state.pdmessage;
                                                                                                                    pdmessages[x][y] = null;
                                                                                                                    this.setState({ pdmessage: pdmessages })
                                                                                                                }}>
                                                                                                            </i>
                                                                                                        </div>
                                                                                                    }
                                                                                                    {this.state.errorpdmessage &&
                                                                                                        this.state.errorpdmessage[x] && this.state.errorpdmessage[x][y] &&
                                                                                                        <div className="alert alert-warning d-flex">{this.state.errorpdmessage[x][y]}
                                                                                                            <i class="fa fa-close ml-auto pr-3 pt-1"
                                                                                                                onClick={() => {
                                                                                                                    let errorpdmessages = this.state.errorpdmessage;
                                                                                                                    errorpdmessages[x][y] = null;
                                                                                                                    this.setState({ errorpdmessage: errorpdmessages })
                                                                                                                }}>
                                                                                                            </i>
                                                                                                        </div>
                                                                                                }
                                                                                                <table className="ml-5 mb-3" style={{ width: '80%' }}>
                                                                                                    <tr><td style={{ width: '10%' }}>number</td>
                                                                                                        <td className="pl-5">inventory number</td>
                                                                                                        <td style={{ width: '140px', padding: '.2rem .5rem' }}>
                                                                                                            <button className="btn btn-mybtn pull-right" style={{ padding: '.15rem .6rem' }}
                                                                                                                onClick={() => this.updatePdChildClicked(null, x, y, null, dd.id)}>add one</button>
                                                                                                        </td></tr>
                                                                                                    {this.state.pdShow[x][y].data.map((pd, i) =>
                                                                                                        <tr>
                                                                                                            <td >{i + 1}</td>
                                                                                                            <td className="pl-5 pb-3 hoverable"
                                                                                                                onClick={() => {
                                                                                                                    this.props.history.push(`/productDetails?Filter.id=${pd.value}`)
                                                                                                                }}>
                                                                                                                {pd.name}
                                                                                                            </td>
                                                                                                            <td><button className="btn btn-mybtn mr-1" onClick={() => {
                                                                                                                this.updatePdChildClicked(pd, x, y, i)
                                                                                                            }}>Update</button>
                                                                                                                <button className="btn btn-mybtn btn-delete"
                                                                                                                    onClick={() => this.deletePDChildClicked(pd.value, x, y, dd.id)}>
                                                                                                                    Delete</button></td>
                                                                                                        </tr>
                                                                                                    )}
                                                                                                </table>
                                                                                                    </>
                                                                                                </td>
                                                                                            </tr>
                                                                                        }
                                                                                    </>
                                                                                )}
                                                                            </tbody>
                                                                        </table>
                                                                    </>
                                                                }
                                                        <span class="pull-right" style={{ marginRight: '100px' }}><strong>Total Bill : {new Intl.NumberFormat("en-GB", {
                                                            style: "currency",
                                                            currency: "BGN",
                                                            maximumFractionDigits: 2
                                                        }).format(item.total)} </strong> {}</span>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                            )}
                        {this.state.deliveryView == 'DeliveryView' &&
                             <table className="table border-bottom my-table" >
                            <thead>
                                <tr>
                                    <th scope="col">number</th>
                                    <th scope="col" >date</th>
                                    <th scope="col" >supplier</th>
                                    <th >total</th>
                                    <th scope="col">Update &emsp;&nbsp; Delete</th>
                                </tr> 
                            </thead>
                            <tbody>
                                {
                                    this.state.items.map(
                                        item =>
                                            <tr>
                                                <td>{item.number}</td>
                                                <td>
                                                    {new Intl.DateTimeFormat("en-GB", {
                                                        month: "long",
                                                        day: "2-digit",
                                                        year: "numeric",
                                                    }).format(new Date(item.date))}
                                                </td>
                                                <td >{item.supplierName || '-'}</td>
                                                <td >{
                                                    new Intl.NumberFormat("en-GB", {
                                                        style: "currency",
                                                        currency: "BGN",
                                                        maximumFractionDigits: 2
                                                    }).format(item.total)}</td>
                                                <td><button className="btn btn-mybtn mr-1" onClick={() => this.updateClicked(item.id)}>Update</button>
                                                    <button className="btn btn-mybtn btn-delete" onClick={() => this.deleteClicked(item.id)}>Delete</button>
                                                </td>
                                            </tr>                                                
                                    )}
                            </tbody>                               
                        </table>
                       }
                    </div>
                </div>
            </div>
        )
    }
}

export default ListDeliveriesComponent