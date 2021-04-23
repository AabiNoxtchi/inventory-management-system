import React, { Component } from 'react';
import ProductDataService from '../service/ProductDataService';
import PaginationComponent from './PaginationComponent';
import ProductFilter from './Filters/ProductFilter';
import '../myStyles/Style.css';
import { CSVLink } from "react-csv";
import { Link, Route, withRouter } from 'react-router-dom';
import Function from './Shared/Function';


const headers = [
    { label: "Name", key: "name" },
    { label: "product Type", key: "productType" },
    { label: "amortization Percent", key: "amortizationPercent" },
    { label: "sub-Category", key: "subCategory" }
];

class ListProductsComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],
            message: null,
            pager: null,
            filter: {},
            filterKey: 0,
            search: window.location.search || '',
            alldata: [],
            showdts: [],
        }
        this.refresh = this.refresh.bind(this)
        this.deleteClicked = this.deleteClicked.bind(this)
        this.updateClicked = this.updateClicked.bind(this)
        this.addClicked = this.addClicked.bind(this)
        this.csvLink = React.createRef();
        this.searchLink = React.createRef();
    }

    componentDidMount() {
        this.refresh();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {

        if (this.props.location.search != prevProps.location.search) {

            let newSearch = this.props.location.search;

            if (this.state.filter)
                if (newSearch.indexOf('Filter.filtersSet') < 0) {
                    newSearch += newSearch.length > 1 ? '&' : newSearch.length == 0 ? '?' : '';
                    newSearch += 'Filter.filtersSet=true'
                }
            this.refresh(newSearch);

        }
    }

    refresh(newSearch) {
        console.log("refreshing*************************************")
        if (!newSearch) newSearch = this.state.search;
       ProductDataService.retrieveAll(newSearch)
            .then(
           response => {              
                    this.setState({
                        items: response.data.items || response.data.daoitems,
                        pager: response.data.pager,
                        filter: this.getfilter(response.data.filter),
                        filterKey: this.state.filterKey + 1
                    });
                }
        ).catch((error) => {
            console.log("error = ")
            let msg = Function.getErrorMsg(error);
            this.showError(msg, 5)
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

            newfilter.names = filter.names;
            newfilter.productTypes = filter.productTypes;
            newfilter.userCategories = filter.userCategories;           
            return newfilter
        }
    }

    showError(msg, time) {
        time = time || 10;
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


    downloadReport = () => {
        let newSearch = this.getSearchAll();
        ProductDataService.retrieveAll(newSearch)
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
        ProductDataService.delete(id)
            .then(
                response => {
                    this.setState({ message: `Delete successful` })
                    this.refresh()
                }
        ).catch(error => {           
            let msg = Function.getErrorMsg(error);           
            this.setState({
                errormsg: msg
            })
        })
    }

    updateClicked(id) {
        this.props.history.push(`/products/${id}`)
    }

    addClicked() {
        this.props.history.push(`/products/-1`)
    }

    togglemsgbox = () => {
        this.setState({ message: null })
    }  

    updateLink(newSearch) {
        this.setState({ search: newSearch },
            () => this.searchLink.current.click())
    }

    updateSearch(newSearch) {
        this.updateLink(newSearch);
    }


    render() {

        const { match } = this.props;
        const url = match.url;

        const data = this.state.items;
        const dataAll = '';
        return (
            <div className="px-3 pt-3">
                <Link ref={this.searchLink} to={`${url}${this.state.search}`}></Link>
                <Route path={`${url}/:search`}>
                    <p></p>
                </Route>
               
                {this.state.filter && <ProductFilter {...this.state.filter}

                    key={this.state.filterKey}
                    onNewSearch={(search) =>
                        this.updateSearch(search)
                    }/>}
                <div className="border">
                    <div className="panel-heading">
                        <h5 className="panel-title p-2 pb-3">
                            <strong> Products</strong>
                        </h5>
                    </div>
                    <div className="p-1">
                        <div className=" pt-3 px-2 mx-3 d-flex flex-wrap">
                            <div>
                                <button className="btn btn-mybtn px-5  " onClick={this.addClicked}>Add New</button>
                                <CSVLink
                                    className="btn btn-mybtn px-3 ml-2"
                                    data={data} headers={headers} filename={"products-page.csv"}
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
                                    filename={"products-all.csv"}
                                    className="hidden"
                                    headers={headers}
                                    ref={this.csvLink}
                                    target="_blank"
                                />
                            </div>
                            {this.state.pager && <PaginationComponent {...this.state.pager}
                                onNewSearch={(search) =>
                                    this.updateSearch(search)
                                }/>}
                        </div>
                        {this.state.errormsg && <div className="alert alert-warning d-flex">{this.state.errormsg}
                            <i class="fa fa-close ml-auto pr-3 pt-1" onClick={() => this.setState({ errormas: null })}></i></div>}
                       
                        {this.state.message && <div className="alert alert-success d-flex">{this.state.message}
                            <i class="fa fa-close ml-auto pr-3 pt-1" onClick={this.togglemsgbox}></i></div>}

                        <table className="table border-bottom my-table">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th scope="col">name</th>
                                    <th scope="col" className="ws">product Type</th>
                                    <th scope="col">category</th>
                                  
                                    <th scope="col" className="ws">amortization</th>
                                    <th scope="col" className="ws">total</th>
                                    <th scope="col">Update &emsp;&nbsp; Delete</th>
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
                                                <td className="hoverable"
                                                    onClick={() => {
                                                        this.props.history.push(`/productdetails?Filter.productId=${item.id}`)
                                                    }}>{item.name}</td>
                                                <td>{item.userCategory.category.productType}</td>
                                                <td>{ item.userCategory.category.name }</td>

                                                <td>{item.userCategory.category.productType =='LTA'? item.userCategory.amortizationPercent + '%' : '-' }</td>
                                                <td>{item.total ? item.total : '0'}</td>
                                                <td><button className="btn btn-mybtn mr-1" onClick={() => this.updateClicked(item.id)}>Update</button>
                                                    <button className="btn btn-mybtn btn-delete" onClick={() => this.deleteClicked(item.id)}>Delete</button></td>
                                                </tr>
                                                {this.state.showdts && this.state.showdts[i] &&
                                                    <tr >
                                                        <td></td>
                                                    <td colSpan="1">description :&nbsp;</td>
                                                    <td colSpan="4" className="pt-3 pb-3 bold-border-bottom">
                                                        {item.description}
                                                    </td>
                                                    <td></td>
                                                        

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

export default ListProductsComponent