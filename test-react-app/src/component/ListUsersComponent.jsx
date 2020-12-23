import React, { Component } from 'react';
import UserDataService from '../service/UserDataService';
import PaginationComponent from './PaginationComponent';
import UserFilter from './Filters/UserFilter';
import '../myStyles/Style.css';
import { CSVLink } from "react-csv";

const headers = [
    { label: "First Name", key: "firstName" },
    { label: "Last Name", key: "lastName" },
    { label: "User Name", key: "userName" },
    { label: "Email", key: "email" }
];

class ListUsersComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],
            message: null,
            pager: null,
            filter: null,
            search: window.location.search || '',
            alldata: [],
           /* csvReport: {
                data: [],
                headers: [],
                filename: 'Report.csv'
            }*/

        }
        this.refresh = this.refresh.bind(this)
        this.deleteClicked = this.deleteClicked.bind(this)
        this.updateClicked = this.updateClicked.bind(this)
        this.addClicked = this.addClicked.bind(this)
       // this.getSearchAll = this.getSearchAll.bind(this)
       // this.downloadReport = this.downloadReport.bind(this)
        this.csvLink = React.createRef();
    }

    componentDidMount() {
        this.refresh();
    }

    refresh() {
        console.log('state.search = ' + this.state.search);
       
        UserDataService.retrieveAll(this.state.search)
            .then(
                response => {
                    console.log(' listUsers : pager.pagescount = ' + response.data.pager.pagesCount);
                    console.log('listUsers : pager.itemsPerPage = ' + response.data.pager.itemsPerPage);
                    this.setState({
                        items: response.data.items,
                        pager: response.data.pager,
                        filter: response.data.filter
                    });
                   
                    
                }
            )
    }

    downloadReport = () => {
        console.log('in fetch data');

        let newSearch = this.getSearchAll();

        UserDataService.retrieveAll(newSearch)
            .then(response => {
            console.log('items length = '+response.data.items.length);
                this.setState({ alldata: response.data.items }); 
                // click the CSVLink component to trigger the CSV download
                this.csvLink.current.link.click()
           
        })
    }

    getSearchAll = () => {
        let search = this.state.search;
        search.replace(/\s+/g, '');
        console.log('state search = ' + this.state.search);
        console.log('search length = ' + search.length);
        let newSearch = '';
        let searchItems = search.split('&');
        console.log('serachitems.length = ' + searchItems.length);
        for (let i = 0; i < searchItems.length; i++) {
            console.log('searchitems[i] = ' + searchItems[i]);
            console.log('searchitems[i].length =' + searchItems[i].length);
            if (searchItems[i].length < 1 || searchItems[i].startsWith('Pager.itemsPerPage'))
                continue
            else
                newSearch += searchItems[i] + '&'
        }
        newSearch = '?' + newSearch;
        console.log('newsearch length = ' + newSearch.length);
        if (newSearch.length > 1) newSearch += '&'
        newSearch += 'Pager.itemsPerPage=2147483647';
        console.log('new search = ' + newSearch);
        return newSearch;
    }

    /*downloadReport = (event, done) => {
        console.log('in download');
        // API call to get data
        let search = this.state.search;
        let newSearch = '';
        let searchItems = search.split('&');
        for (let i = 0; i < searchItems.length; i++) {

            if (searchItems[i].startsWith('Pager.itemsPerPage'))
                continue
            else
                newSearch += searchItems[i] + '&'

        }
        newSearch = '?' + newSearch + newSearch.length > 0 ? '&' : '' + 'Pager.itemsPerPage=' + 2147483647;
        console.log('new search = ' + newSearch);
        UserDataService.retrieveAll(newSearch)
            .then(
            response => {
                const objReport = {
                    filename: 'Clue_Mediator_Report_Async.csv',
                    headers: headers,
                    data: response.data.items
                };
                this.setState({ csvReport: objReport }, () => {
                    console.log('state data =' + this.state.csvReport.data);
                    done();
                });
                   
                })
   
    }*/

    deleteClicked(id) {
       UserDataService.delete(id)
            .then(
                response => {
                    this.setState({ message: `Delete successful` })
                    this.refresh()
                }
            )

    }

    updateClicked(id) {
        console.log('update ' + id)
        this.props.history.push(`/users/${id}`)
    }

    addClicked() {
        this.props.history.push(`/users/-1`)
    }

    render() {
      

        const data = this.state.items;
        const dataAll = '';
        return (
            <div className="px-3">
                    {this.state.filter && <UserFilter {...this.state.filter} />}
                    <div className="border">
                    <div className="panel-heading">
                            <h6 className="panel-title pull-left p-2">
                                <strong> Users</strong>
                            </h6>
                    </div>
                <div className="p-1">
                            <div className="row pt-3 px-2 mx-3">
                                <div className="col-6">
                                    <button className="btn btn-mybtn px-5  " onClick={this.addClicked}>Add New</button>
                               
                                <CSVLink
                                    className="btn btn-mybtn px-3 ml-2"
                                    data={data} headers={headers} filename={"users-page.csv"}
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
                                    filename={"users-all-test.csv"}
                                    className="hidden"
                                    headers={headers}
                                    ref={this.csvLink}
                                    target="_blank"
                                />
                                                                
                                </div>                              
                                    {this.state.pager && <PaginationComponent {...this.state.pager} />}                              
                            </div>
                   
                    {this.state.message && <div class="alert alert-success">{this.state.message}</div>}
                   
                        <table className="table border-bottom my-table" style={{ width: '100%' }}>
                        <thead>
                            <tr>
                                <th scope="col">first name</th>
                                <th scope="col">last name</th>
                                <th scope="col">user name</th>
                                <th scope="col">email</th>
                                <th scope="col"></th>                                   
                            </tr>
                        </thead>
                        <tbody>
                                {
                                    this.state.items.map(
                                    item =>
                                        <tr scope="row" key={item.id}>
                                            <td>{item.firstName}</td>
                                            <td>{item.lastName}</td>
                                            <td>{item.userName}</td>
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

export default ListUsersComponent