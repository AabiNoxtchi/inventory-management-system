import React, { Component } from 'react';
import ProductDetailDataService from '../service/ProductDetailDataService';
import PaginationComponent from './PaginationComponent';
import ProductDetailFilter from './Filters/ProductDetailFilter';
import '../myStyles/Style.css';
import { CSVLink } from "react-csv";
import ProductDetailInnerComponent from './ProductDetailInnerComponent';

import AuthenticationService from '../service/AuthenticationService'

const headers = [
    { label: "inventory number", key: "inventoryNumber" },
    { label: "discarded", key: "isDiscarded" },
    { label: "available", key: "isAvailable" }
    
];

const userRole = AuthenticationService.getLoggedUerRole();

class ListProductDetails extends Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],
            message: null,
            pager: null,
            filter: null,
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
    }

    componentDidMount() {
        this.refresh();
    }

    refresh() {
        ProductDetailDataService.retrieveAll(this.state.search)
            .then(
            response => {
               // console.log("response.data = " + response.data);
              //  console.log("response.data = " + JSON.stringify(response.data));
                    this.setState({
                        items: response.data.items || response.data.daoitems,
                        pager: response.data.pager,
                        filter: response.data.filter
                });
               // console.log("items = " + JSON.stringify(this.state.items));
                }
        ).catch(error => {
            this.setState({ errormsg: '' + error })
        })
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
            //console.log("error.response.data = " + error.response.data);
            this.setState({ errormsg:''+error })
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

    render() {
        const data = this.state.items;
        const dataAll = '';
        return (
            <div className="px-3 pt-3">

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
                {this.state.filter && <ProductDetailFilter {...this.state.filter} />}
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
                            {this.state.pager && <PaginationComponent {...this.state.pager} />}
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
                                                <td><button className="btn btn-mybtn mr-1" onClick={() => this.updateClicked(item, i)}>Update</button>
                                                    <button className="btn btn-mybtn btn-delete" onClick={() => this.deleteClicked(item.id)}>Delete</button></td>
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