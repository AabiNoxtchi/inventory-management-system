import React, { Component } from 'react';
import ProductDataService from '../service/ProductDataService';
import PaginationComponent from './PaginationComponent';
import ProductFilter from './Filters/ProductFilter';
import '../myStyles/Style.css';
import { CSVLink } from "react-csv";

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
            filter: null,
            search: window.location.search || '',
            alldata: [],
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
       ProductDataService.retrieveAll(this.state.search)
            .then(
           response => {
               console.log("items = " + JSON.stringify(response));
                    this.setState({
                        items: response.data.items || response.data.daoitems,
                        pager: response.data.pager,
                        filter: response.data.filter
                    });
                }
            )
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
            )
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

    render() {
        const data = this.state.items;
        const dataAll = '';
        return (
            <div className="px-3">
                {this.state.filter && <ProductFilter {...this.state.filter} />}
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
                            {this.state.pager && <PaginationComponent {...this.state.pager} />}
                        </div>
                        {this.state.message && <div className="alert alert-success d-flex">{this.state.message}<i class="fa fa-close ml-auto pr-3 pt-1" onClick={this.togglemsgbox}></i></div>}

                        <table className="table border-bottom my-table">
                            <thead>
                                <tr>
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
                                        item =>
                                            <tr scope="row" key={item.id}>
                                                <td className="hoverable"
                                                    onClick={() => {
                                                        this.props.history.push(`/productDetails?Filter.productId=${item.id}`)
                                                    }}>{item.name}</td>
                                                <td>{item.userCategory.category.productType}</td>
                                                <td>{ item.userCategory.category.name }</td>

                                                <td>{item.userCategory.category.productType =='LTA'? item.userCategory.amortizationPercent + '%' : '-' }</td>
                                                <td>{item.total ? item.total : '0'}</td>
                                                <td><button className="btn btn-mybtn mr-1" onClick={() => this.updateClicked(item.id)}>Update</button>
                                                    <button className="btn btn-mybtn btn-delete" onClick={() => this.deleteClicked(item.id)}>Delete</button></td>
                                            </tr>
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