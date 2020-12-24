import React, { Component } from 'react';
import SupplierDataService from '../service/SupplierDataService';
import PaginationComponent from './PaginationComponent';
import SupplierFilter from './Filters/SupplierFilter';
import '../myStyles/Style.css';
import { CSVLink } from "react-csv";

const headers = [
    { label: "Name", key: "name" },
    { label: "Phone Number", key: "phoneNumber" },
    { label: "DDC Number", key: "ddcnumber" },
    { label: "Email", key: "email" }
];

class ListSuppliersComponent extends Component {
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
        SupplierDataService.retrieveAll(this.state.search)
            .then(
                response => {
                    this.setState({
                        items: response.data.items,
                        pager: response.data.pager,
                        filter: response.data.filter
                    });
                }
            )
    }

    downloadReport = () => {
        let newSearch = this.getSearchAll();
       SupplierDataService.retrieveAll(newSearch)
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
       SupplierDataService.delete(id)
            .then(
                response => {
                    this.setState({ message: `Delete successful` })
                    this.refresh()
                }
            )
    }

    updateClicked(id) {
        this.props.history.push(`/suppliers/${id}`)
    }

    addClicked() {
        this.props.history.push(`/suppliers/-1`)
    }

    togglemsgbox = () => {
        this.setState({ message: null })
    }

    render() {
        const data = this.state.items;
        const dataAll = '';
        return (
            <div className="px-3">
                {this.state.filter && <SupplierFilter {...this.state.filter} />}
                <div className="border">
                    <div className="panel-heading">
                        <h6 className="panel-title p-2">
                            <strong> Suppliers</strong>
                        </h6>
                    </div>
                    <div className="p-1">
                        <div className=" pt-3 px-2 mx-3 d-flex flex-wrap">
                            <div>
                                <button className="btn btn-mybtn px-5  " onClick={this.addClicked}>Add New</button>
                                <CSVLink
                                    className="btn btn-mybtn px-3 ml-2"
                                    data={data} headers={headers} filename={"suppliers-page.csv"}
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
                                    filename={"suppliers-all.csv"}
                                    className="hidden"
                                    headers={headers}
                                    ref={this.csvLink}
                                    target="_blank"
                                />
                            </div>
                            {this.state.pager && <PaginationComponent {...this.state.pager} />}
                        </div>
                        {this.state.message && <div className="alert alert-success d-flex">{this.state.message}<i class="fa fa-close ml-auto pr-3 pt-1" onClick={this.togglemsgbox}></i></div>}

                        <table className="table border-bottom my-table" style={{ width: '100%' }}>
                            <thead>
                                <tr>
                                    <th scope="col">name</th>
                                    <th scope="col">phone number</th>
                                    <th scope="col">DDC number</th>
                                    <th scope="col">email</th>
                                    <th scope="col"></th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.items.map(
                                        item =>
                                            <tr scope="row" key={item.id}>
                                                <td>{item.name}</td>
                                                <td>{item.phoneNumber}</td>
                                                <td>{item.ddcnumber}</td>
                                                <td>{item.email}</td>
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

export default ListSuppliersComponent