import React, { Component } from 'react';
import ProductDetailDataService from '../service/ProductDetailDataService';
import PaginationComponent from './PaginationComponent';
import ProductDetailFilter from './Filters/ProductDetailFilter';
import '../myStyles/Style.css';
import { CSVLink } from "react-csv";
import ProductDetailInnerComponent from './ProductDetailInnerComponent';
//import AuthenticationService from '../service/AuthenticationService';
import Function from './Shared/Function';
import { Link, Route, withRouter } from 'react-router-dom'

import AuthenticationService from '../service/AuthenticationService'

const headers = [
    { label: "inventory number", key: "inventoryNumber" },
    { label: "discarded", key: "isDiscarded" },
    { label: "available", key: "isAvailable" }
    
];



class ListProductDetails extends Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],
            message: null,
            pager: null,
            filter: {},
            filterkey: 0,
            search: window.location.search || '',
            alldata: [],
            showdts: [],
            pdUpdateShow: {
                pd: {}, show: false
            },
        }
        this.refresh = this.refresh.bind(this)
        this.deleteClicked = this.deleteClicked.bind(this)
        this.updateClicked = this.updateClicked.bind(this)
        this.addClicked = this.addClicked.bind(this)
        this.csvLink = React.createRef();
        this.searchLink = React.createRef();
    }

    componentDidMount() {
        console.log(" did mount *************************")
        this.refresh();
    }

    //shouldComponentUpdate() { }
    componentDidUpdate(prevProps, prevState, snapshot) {
        // Check to see if the "applied" flag got changed (NOT just "set")

        console.log("this.props.location = " + JSON.stringify(this.props.location));
        console.log("previousprops.location = " + JSON.stringify(prevProps.location));
        console.log("this.state.search = " + this.state.search);
        
        console.log("this.props.location.search != prevProps.location.search = " + (this.props.location.search != prevProps.location.search));
        if (this.props.location.search != prevProps.location.search) {

            let newSearch = this.props.location.search;
            if (this.state.filter)
            if (newSearch.indexOf('Filter.filtersSet') < 0) {
                newSearch += newSearch.length > 1 ? '&' : newSearch.length == 0 ? '?' : '';
                newSearch += 'Filter.filtersSet=true'
            }

            console.log("newSearch = " + newSearch);

            this.refresh(newSearch);
            //this.setState({ search: this.props.location.search },
            //()=>this.refresh())
        }

        //if (this.props.location.state.applied && !prevProps.location.state.applied) {
            
        //}
    }

    refresh(newSearch) {
        console.log("refreshing *******product details *****************");
        console.log("this.state.search = " + this.state.search)

        if (!newSearch) newSearch = this.state.search;
        ProductDetailDataService.retrieveAll(newSearch)
            .then(
            response => {
               // console.log("response.data = " + response.data);
              //  console.log("response.data = " + JSON.stringify(response.data));
                    this.setState({
                        items: response.data.items || response.data.daoitems,
                        pager: response.data.pager,
                        filter: this.getfilter(response.data.filter) , //.filtersSet ? this.state.filter : response.data.filter//this.state.filter == null ? response.data.filter : this.state.filter
                        filterkey: response.data.filter.filtersSet ? this.state.filterkey : this.state.filtersSet+1
                    })
                console.log("filter.deliveryId = " + JSON.stringify(response.data.filter.deliveryId));
               // console.log("items = " + JSON.stringify(this.state.items));
                }
        ).catch(error => {
            this.setState({ errormsg: '' + error })
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
            newfilter.inventoryNumbers = filter.inventoryNumbers;
            //newfilter.productTypes = filter.productTypes;
            return newfilter//this.state.filter
        }
    }

    downloadReport = () => {
        let newSearch = this.getSearchAll();
        ProductDetailDataService.retrieveAll(newSearch)
            .then(response => {
                this.setState({ alldata: response.data.items });
                this.csvLink.current.link.click()
            })
    }

    getSearchAll = () => {
        let search = this.state.search;
        search.replace(/\s+/g, '');
        let newSearch = '';
        let searchItems = search.split('&');
        for (let i = 0; i < searchItems.length; i++) {
            if (searchItems[i].length < 1 || searchItems[i].startsWith('Pager.itemsPerPage'))
                continue
            else
                newSearch += searchItems[i] + '&'
        }
        newSearch = '?' + newSearch;
        if (newSearch.length > 1) newSearch += '&'
        newSearch += 'Pager.itemsPerPage=2147483647';
        return newSearch;
    }

    deleteClicked(id) {
        ProductDetailDataService.delete(id)
            .then(
                response => {
                    this.setState({ message: `Delete successful` })
                    this.refresh()
            }
        ).catch(error => {
            console.log("error = " + JSON.stringify(error));

            let msg = Function.getErrorMsg(error);
            //console.log("error.response.data = " + error.response.data);
            this.setState({ errormsg:msg })
        })
    }

    updateClicked(item, x) {
       
            /* let messages = this.state.message;
             if (messages != null )
                 messages = null;*/

            let show = this.state.pdUpdateShow;
            show.show = !show.show;
            if (show.show == true) {
                show.pd = JSON.parse(JSON.stringify(item))
                show.x = x;
            } else {
                show.error = ''
            }
            this.setState({
                pdUpdateShow: show,
                message: null,
                pdmessage: [],
                ddmessage: []
        });

        
        
    }

    addClicked() {
        this.props.history.push(`/productdetails/-1`)
    }

    togglemsgbox = () => {
        this.setState({ message: null })
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
      /*  if (newSearch.indexOf('Filter.filtersSet') < 0) {
            newSearch += newSearch.length > 1 ? '&' : newSearch.length == 0 ? '?' : '';
            newSearch += 'Filter.filtersSet=true'
        }

        console.log("newSearch = " + newSearch);*/
        // this.setState({ search: newSearch } )

       // this.refresh(newSearch);
    }

    render() {
        const { match } = this.props;
        const url = match.url;

        const userRole = AuthenticationService.getLoggedUerRole();
        const data = this.state.items;
        const dataAll = '';
        return (
            <div className="px-3 pt-3">
                <Link ref={this.searchLink} to={`${url}${this.state.search}`}></Link>
                <Route path={`${url}/:search`}>
                    <p></p>
                </Route>
                {this.state.pdUpdateShow && this.state.pdUpdateShow.show == true &&
                    <ProductDetailInnerComponent
                        pdUpdateShow={this.state.pdUpdateShow}
                    items={this.state.items}
                    filter={this.state.filter}
                        message={this.state.message}
                       // suppliers={this.state.filter.suppliers}
                        updateClicked={() => this.updateClicked(null)}
                        setItems={(value) => this.setState({ items: value })}
                        setMessage={(value) => this.setState({ message: `update successful` })}
                    //setdeliveryUpdateShow={(value) => this.setState({ deliveryUpdateShow: value })} 
                    />}
                {userRole == 'ROLE_Mol' && this.state.filter && <ProductDetailFilter {...this.state.filter} history={this.props.history}
                    key={this.state.filterkey}
                    onNewSearch={(search) =>
                    this.updateSearch(search)
                } />}
                <div className="border">
                    <div className="panel-heading">
                        <h5 className="panel-title p-2">
                            <strong> Inventory</strong>
                        </h5>
                    </div>
                    <div className="p-1">
                        <div className=" pt-3 px-2 mx-3 d-flex flex-wrap">
                            <div>
                                {/*<button className="btn btn-mybtn px-5  " onClick={this.addClicked}>Add New</button>*/}
                                <CSVLink
                                    className="btn btn-mybtn px-3 ml-2"
                                    data={data} headers={headers} filename={"product-details-page.csv"}
                                    asyncOnClick={true}
                                    onClick={() => {
                                        console.log("You click the link");
                                    }}
                                >
                                    Download this page
                                </CSVLink>
                                <button className="btn btn-mybtn px-3 ml-2" onClick={this.downloadReport}>Download All</button>
                                <CSVLink
                                    data={this.state.alldata}
                                    filename={"product-details-all.csv"}
                                    className="hidden"
                                    headers={headers}
                                    ref={this.csvLink}
                                    target="_blank"
                                />
                            </div>
                            {this.state.pager && <PaginationComponent {...this.state.pager} history={this.props.history}
                                onNewSearch={(search) =>
                                    this.updateSearch(search)
                                }/>}
                        </div>
                        {this.state.message && <div className="alert alert-success d-flex">{this.state.message}
                            <i class="fa fa-close ml-auto pr-3 pt-1" onClick={this.togglemsgbox}></i></div>}
                        {this.state.errormsg && <div className="alert alert-warning d-flex">{this.state.errormsg}
                            <i class="fa fa-close ml-auto pr-3 pt-1" onClick={() => this.setState({ errormas: null })}></i></div>}
                       

                        <table className="table border-bottom my-table" style={{ width: '100%' }}>
                            <thead>
                                <tr>
                                    <th scope="col" ></th>
                                    <th scope="col">inventory number</th>
                                   
                                    <th scope="col">product name</th>
                                   
                                    <th>date created</th> 
                                    <th scope="col" >price</th>
                                    <th scope="col" className="wxxs">discarded</th>
                                    <th scope="col" className="wxxs">condition</th>
                                    {userRole == 'ROLE_Mol' && <th className="wxxs">profiles</th>}
                                     
                                    {/*<th >amortization</th>
                                    <th >total amortization</th>
                                     <th >delivery</th>
                                   */}
                                    <th ></th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.items.map(
                                        (item, i) =>
                                            <>
                                                <tr scope="row" key={item.id}>
                                                    <td className="hoverable"
                                                        onClick={() => {
                                                            let showdts = this.state.showdts;
                                                            if (showdts == undefined) showdts = [];
                                                            showdts[i] = showdts[i] ? false : true;
                                                            this.setState({ showdts: showdts })
                                                        }}><i class={this.state.showdts && this.state.showdts[i] ? "fa fa-angle-double-up" : "fa fa-angle-double-down"}
                                                            aria-hidden="true"></i></td>
                                                <td>{item.inventoryNumber}</td>                                                
                                                <td>{item.productName}</td>                                               
                                                <td>{new Intl.DateTimeFormat("en-GB", {
                                                    month: "long",
                                                    day: "2-digit",
                                                    year: "numeric", 
                                                    }).format(new Date(item.dateCreated))}</td>
                                                    <td>{new Intl.NumberFormat("en-GB", {
                                                        style: "currency",
                                                        currency: "BGN",
                                                        maximumFractionDigits: 2
                                                    }).format(item.price)}</td>
                                                <td>{item.discarded ? 'yes' : 'no'}</td>
                                                <td>{item.econdition}</td>
                                                {userRole == 'ROLE_Mol' && <td className="hoverable"
                                                    onClick={() => {
                                                        this.props.history.push(`/userprofiles?Filter.productDetailId=${item.id}`);
                                                        }}><i class="fa fa-angle-double-right" aria-hidden="true"></i></td>}
                                                    {userRole == 'ROLE_Mol' &&
                                                        <td><button className="btn btn-mybtn mr-1" onClick={() => this.updateClicked(item, i)}>Update</button>
                                                            <button className="btn btn-mybtn btn-delete" onClick={() => this.deleteClicked(item.id)}>Delete</button></td>}
                                                    {userRole != 'ROLE_Mol' && <td></td>}
                                            </tr>
                                                {this.state.showdts && this.state.showdts[i] &&
                                                    <tr >                                                    
                                                    <td colSpan="2" className="pt-3 pb-3 bold-border-bottom"></td>
                                                    <td colSpan="1" className="pt-3 pb-3 bold-border-bottom">
                                                        delivery number : &nbsp;
                                                    {item.deliveryNumber}</td>
                                                    <td colSpan="1" className="pt-3 pb-3 bold-border-bottom">type :&nbsp;{item.productType}</td> 
                                                    <td colSpan="1" className="pt-3 pb-3 bold-border-bottom">                                                   
                                                        amortization : &nbsp;
                                                    {item.amortizationPercent}&nbsp;%</td>                                                   
                                                    <td colSpan="4" className="pt-3 pb-3 bold-border-bottom">                                                                                                                
                                                    total amortization : &nbsp;
                                                    {new Intl.NumberFormat("en-GB", {
                                                    style: "currency",
                                                    currency: "BGN",
                                                    maximumFractionDigits: 2
                                                    }).format(item.totalAmortization)}                                                       
                                                    &emsp;&emsp;&emsp;                                                               
                                                    </td>
                                                    
                                                    
                                                    </tr>}                                                
                                               </>
                                    )
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        )
    }
}

export default ListProductDetails