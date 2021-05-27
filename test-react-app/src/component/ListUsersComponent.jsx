import React, { Component } from 'react';
import UserDataService from '../service/UserDataService';
import PaginationComponent from './PaginationComponent';
import UserFilter from './Filters/UserFilter';
import '../myStyles/Style.css';
import { CSVLink } from "react-csv";
import Function from './Shared/Function';
import { Link, Route } from 'react-router-dom'

import AuthenticationService from '../service/AuthenticationService'

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
            filter: {},
            filterKey: 0,
            search: window.location.search || '',
            alldata: [],
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

    componentDidUpdate(prevProps) {
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
        if (!newSearch) newSearch = this.state.search;
        UserDataService.retrieveAll(newSearch)
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
            let msg = Function.getErrorMsg(error);
            this.showError(msg, 5);           
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
            newfilter.firstNames = filter.firstNames;
            newfilter.lastNames = filter.lastNames;
            newfilter.userNames = filter.userNames;
            newfilter.emails = filter.emails;
            newfilter.cities = filter.cities;
            newfilter.countries = filter.countries;          
            return newfilter
        }
    }

    showError(msg, time) {
        time = time ? time : 10;
        this.setState({
            errormsg: msg,
        })
        this.myInterval = setInterval(() => {
            time = time - 1;
            if (time == 0) {
                this.setState(() => ({
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
        UserDataService.retrieveAll(newSearch)
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
        UserDataService.delete(id)
            .then(() => {
                this.setState({ message: `Delete successful` })
                this.refresh()
            }).catch(error => {
                let msg = Function.getErrorMsg(error);
                this.setState({
                    errormsg: msg
                })
            })
    }

    updateClicked(id) {
        this.props.history.push(`/users/${id}`)
    }

    addClicked() {
        this.props.history.push(`/users/-1`)
    }

    togglemsgbox = () => {
        this.setState({ message: null})
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

        const userRole = AuthenticationService.getLoggedUerRole();
        const data = this.state.items;
        const dataAll = '';
        return (
            <div className="px-3 pt-3">
                <Link ref={this.searchLink} to={`${url}${this.state.search}`}></Link>
                <Route path={`${url}/:search`}>
                    <p></p>
                </Route>
                {this.state.filter && <UserFilter {...this.state.filter}
                    key={this.state.filterKey}
                    userRole={userRole}
                    onNewSearch={(search) =>
                        this.updateSearch(search)
                    }/>}
                <div className="border">
                    <div className="panel-heading">
                            <h5 className="panel-title p-2 pb-3">
                                <strong> Users</strong>
                            </h5>
                    </div>
                    <div className="p-1">
                        <div className=" pt-3 px-2 mx-3 d-flex flex-wrap ">
                                <div >
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
                                    filename={"users-all.csv"}
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
                        {this.state.message && <div className="alert alert-success d-flex">{this.state.message}<i class="fa fa-close ml-auto pr-3 pt-1"
                            onClick={this.togglemsgbox}></i></div>}
                        {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                      
                    <table className= "table border-bottom my-table">
                        <thead>
                            <tr>
                                <th>first name</th>
                                <th>last name</th>
                                <th>user name</th>
                                    <th>email</th>
                                    {console.log("userRole  = " + (userRole))}
                                    {console.log("userRole === 'ROLE_Admin'  = "+(userRole === 'ROLE_Admin' ))}
                                    {userRole === 'ROLE_Admin' && <th> city </th>}
                                    {userRole === 'ROLE_Admin' && <th> last active </th>}

                                    {userRole =='ROLE_Mol' && <th>profiles</th>}
                                <th>Update &emsp;&nbsp; Delete</th>                                                                     
                            </tr>
                        </thead>
                        <tbody>
                                {this.state.items.map(
                                    item =>
                                        <tr key={item.id}>
                                            <td>{item.firstName}</td>
                                            <td>{item.lastName}</td>
                                            <td>{item.userName}</td>
                                            <td>{item.email}</td>
                                            {userRole === 'ROLE_Admin' && <td> {item.countryName}/{item.cityName} </td>}
                                            {userRole === 'ROLE_Admin' && <td> {item.lastActive}</td>}
                                            {userRole == 'ROLE_Mol' && <td className="hoverable"
                                                onClick={() => {
                                                    this.props.history.push(`/userProfiles?Filter.userId=${item.id}`);
                                                }}>&nbsp;>></td>}
                                            {item.deleted && <td>
                                                &nbsp;&nbsp;deleted&emsp;&nbsp;&nbsp;</td>}
                                            {!item.deleted && <td>
                                                <button className="btn btn-mybtn mr-1" onClick={() => this.updateClicked(item.id)}>Update</button>
                                                <button className="btn btn-mybtn btn-delete" onClick={() => this.deleteClicked(item.id)}>Delete</button></td>}
                                        </tr>
                                )}
                        </tbody>
                            </table>
                </div>
                </div>
            </div>
            
        )
    }
}

export default ListUsersComponent