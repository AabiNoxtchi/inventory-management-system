import React, { Component } from 'react';
import UserProfileDataService from '../service/UserProfileDataService';
import PaginationComponent from './PaginationComponent';
import UserProfileFilter from './Filters/UserProfileFilter';
import '../myStyles/Style.css';
import { CSVLink } from "react-csv";
import { Link, Route, withRouter} from 'react-router-dom'

import AuthenticationService from '../service/AuthenticationService';
import CustomSelect from './Filters/CustomSelect';
import UserProfileInnerComponent from './UserProfileInnerComponent';
import TimelineInnerComponent from './TimelineInnerComponent';
import Function from './Shared/Function';
import OrderByComponent from './OrderByComponent';
import DeleteAllInnerComponent from './DeleteAllInnerComponent'
const headers = [
    { label: "UserName", key: "userName" },
    { label: "product", key: "productName" },
    { label: "Inventory Number", key: "inventoryNumber" },
    { label: "Given at", key: "givenAt" },
    { label: "Returned at", key: "returnedAt" },   
];
class ListUserProfilesComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],
            message: null,
            pager: null,
            filter: {},
            search: window.location.search || '',
            searchBackUp:'',
            alldata: [],
            showdts: [],
            i: null,
            selectedUserId: null,
            profileShow: {
                profile: {}, show: false
            },
            timeline: {
                show:false
            },
            filterKey: 0,
            orderBy: { name: '', direction: '' },
            usersToGive: []
        }
        this.refresh = this.refresh.bind(this)
        this.deleteClicked = this.deleteClicked.bind(this)
        this.updateClicked = this.updateClicked.bind(this)
        this.csvLink = React.createRef();
        this.searchLink = React.createRef();
        this.child = React.createRef();
        this.filter = React.createRef();
    }

    componentDidMount() {
        this.refresh();
    }

    refresh(search) {
        if (search == null) search = this.state.search;
        UserProfileDataService.retrieveAll(search)
            .then(
                response => {                   
                    this.setState({
                        items: response.data.items || response.data.daoitems || [],
                        pager: response.data.pager,
                    });
                    this.getFilter(response.data.filter)
            }).catch((error) => {
                let msg = Function.getErrorMsg(error);
                this.showError(msg, 5);              
            })
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

    getFilter(newFilter ) {
        if (this.state.filterKey == 0 || !newFilter.filtersSet) {
            if (newFilter.usersToGive && newFilter.usersToGive.length > 0) {
                let usersToGive = this.state.usersToGive;
                usersToGive = JSON.parse(JSON.stringify(newFilter.usersToGive));
                usersToGive.splice(0, 1);
                this.setState({ usersToGive: usersToGive })
            }
            this.setState({
                filter:newFilter,
                filterKey: this.state.filterKey + 1,
            })
        }      
            if (newFilter.filtersSet) {
                let filter = this.state.filter;
                newFilter.userNames = filter.userNames;
                newFilter.productNames = filter.productNames;
                newFilter.inventoryNumbers = filter.inventoryNumbers;
                this.setState({ filter: newFilter })
            }           
    } 

    downloadReport = () => {
        let newSearch = this.getSearchAll();
        UserProfileDataService.retrieveAll(newSearch)
            .then(response => {
                let data = response.data.items||response.data.daoitems;
                data = this.getFormattedData(data);                   
                this.setState({ alldata:data });
                this.csvLink.current.link.click()
            })
    }

    getFormattedData = (data) => {        
        data = data && data.map(row => ({
            ...row, givenAt: new Intl.DateTimeFormat("en-GB", {
                month: "numeric",
                day: "2-digit",
                year: "numeric",
            }).format(new Date(row.givenAt))
            , returnedAt: row.returnedAt ? new Intl.DateTimeFormat("en-GB", {
                month: "numeric",
                day: "2-digit",
                year: "numeric",
            }).format(new Date(row.returnedAt)) : null
        }));
        return data;
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
        newSearch += '&Filter.LongView=true';
        return newSearch;
    }

    deleteClicked(id) {
        UserProfileDataService.delete(id)
            .then(() => {
                    this.setState({ message: `Delete successful` })
                    this.refresh()
                }
        ).catch(error => {
            let msg = Function.getErrorMsg(error)
            this.showError(msg, 4);           
        })
    }

    deleteAllbefore(date, id) {
        UserProfileDataService.deleteAllBefore(date, id)
            .then(
                response => {
                    this.setState({ message: response.data + ` profiles have been Deleted successfully` })
                    this.refresh()
                }
        ).catch(error => {
            let msg = Function.getErrorMsg(error)
            this.showError(msg, 4);          
            })
    }

    updateTimeline(value) {
        if (this.state.timeline.show == value) return;        
        let timeline = this.state.timeline;
        timeline.show = value;
        this.setState({
            timeline: timeline,
            message: null,
            timeLineFilter: value ? JSON.parse(JSON.stringify(this.state.filter)) : null
        });
        if (value) {
            let backUp = this.state.search;
            this.setState({
                searchBackUp: backUp,               
            })
            this.updateLink('')
        }
        if (!value) {            
            this.setState({
                search: this.state.searchBackUp,
                filterKey: this.state.filterKey + 1,
            }, () => { this.refresh(); this.searchLink.current.click() })          
        }
    }

    updateClicked(item, x) {
        let show = this.state.profileShow;
        show.show = !show.show;
        if (show.show == true) {
            show.profile = JSON.parse(JSON.stringify(item))
            show.x = x;
        } else {
            show.error = ''
        }
        this.setState({
            profileShow: show,
            message: null,           
        });
    }

    addClicked (name) {
        let userId = this.state.filter.userId || null;
        let userName = userId == null || userId == 'undefined' ? '...' : name;
        let item = {
            userId: userId,
            userName: userName
        }
        this.updateClicked(item,null)
    }

    cancelToGive = () => {
        this.setState({
            i: null,
            selectedUserId: null
        })
    }

    saveToGive = (item) => {      
        let g = Function.getDate();
        let itemToSend = {
            previousId: item.id,
            userId: this.state.selectedUserId || null,
            productDetailId: item.productDetailId,
            givenAt: g,           
        }

        UserProfileDataService.save(itemToSend).then(
            () => {              
                let name = this.state.selectedUserId;
                this.setState({
                    message: name != null ? 'product given successfully ' : 'product returned successfully ',
                    selectedUserId: null,
                    i: null,                    
                })               
               this.refresh();
            }).catch(error => {
                let msg = Function.getErrorMsg(error);
                this.showError(msg, 5);
                this.setState({
                    selectedUserId: null,
                    i: null,
                })
            })
    }

    togglemsgbox = () => {
        this.setState({ message: null })
    }

    onFilterSearchChange(search) {
        this.setState({ search: search })
        this.refresh()
    }

    getOrderedList(name) {        
        let search = this.state.search;
        let newSearch = ``;
        if (search.length > 1) {
            while (search.charAt(0) === '?') {
                search = search.substring(1);
            }
            let searchItems = search.split('&');
            for (let i = 0; i < searchItems.length; i++) {
                if (searchItems[i].startsWith('Pager.itemsPerPage='))
                    newSearch += searchItems[i] + '&'
                else if (searchItems[i].startsWith('Pager')) continue;
                else if (searchItems[i].startsWith('OrderBy')) continue;               
                else 
                    newSearch += searchItems[i] + '&'
            }
        }      
        let direction = '';
        if (this.state.orderBy.name == name) {
            direction = this.state.orderBy.direction == 'asc' ? 'desc' : 'asc';
        } else {            
            direction = 'asc';
        }
        newSearch += 'OrderBy.' + name + '=' + direction;//;
        let orderBy = { name: name, direction: direction }
        this.setState({ orderBy: orderBy })
        newSearch = '?' + newSearch;
        this.updateSearch(newSearch);
    }

    updateLink(newSearch) {
        this.setState({ search: newSearch },
            () => this.searchLink.current.click())
    }

    updateSearch(newSearch) {      
        this.updateLink(newSearch);
        if (newSearch.indexOf('Filter.filtersSet') < 0) {
            newSearch += newSearch.length > 1 ? '&' : newSearch.length == 0 ? '?' : '';
            newSearch += 'Filter.filtersSet=true'
            }
        this.refresh(newSearch);
    }

    render() {

        const { match } = this.props;
        const url = match.url;
      
        const userRole = AuthenticationService.getLoggedUerRole();
        const userName = AuthenticationService.getLoggedUerName();
        const data = this.getFormattedData(this.state.items);

        const filteredUserTitle = userRole == 'ROLE_Employee' || (this.state.filter != null && this.state.filter.myProfile) ? 'my profile'
            : this.state.filter != null && this.state.filter.userId != null ?
                this.state.filter.userNames.find(n => n.value == this.state.filter.userId).name
                : this.state.filter != null && this.state.filter.allUser ? 'users' : null;

        const deletedUser = this.state.filter != null && this.state.filter.userId != null &&
            this.state.filter.userNames.find(n => n.value == this.state.filter.userId).filterBy != null;
        
      
        return (
            <div className="px-3 pt-3">               
                <Link ref={this.searchLink} to={`${url}${this.state.search}`}></Link>
                <Route path={`${url}/:search`}>
                    <p></p>
                </Route>
                { <div className={this.state.i != null ? "overlay d-block" : "d-none"}></div>}
                {this.state.profileShow.profile && this.state.profileShow.show == true &&
                    <UserProfileInnerComponent                       
                        profileShow={this.state.profileShow}
                        items={this.state.items}
                        filter={this.state.filter}
                        usersToGive={this.state.usersToGive}
                        message={this.state.message}
                        updateClicked={() => this.updateClicked(null)}
                        refresh={() => this.refresh()}
                        setMessage={(value) => this.setState({ message: value })}
                    />}
                {this.state.filter && <UserProfileFilter {...this.state.filter}
                    key={this.state.filterKey}
                    userRole={userRole}
                    timeline={this.state.timeline}
                    onSearch={(filter) => {
                        if (this.state.timeline.show == true) {
                            this.child.current.getNewFilter(filter);
                        }
                    }}
                    search={this.state.search}
                    onNewSearch={(search) =>
                        this.updateSearch(search)
                    }/>}
                <div className="border">
                    <div className="panel-heading">
                        <h5 className="panel-title p-2 d-inline-flex">
                            <strong> User Profiles</strong>
                            {filteredUserTitle != null && !this.state.timeline.show && <span> &emsp;(&nbsp;{filteredUserTitle}&nbsp;)</span>}                            
                        </h5>
                        {userRole == 'ROLE_Mol' &&                           
                            <div className=" pull-right mr-5" style={{ position: "relative" }}>                            
                                <input className="mx-1 m-2 mt-3" type="radio" 
                                    checked={this.state.timeline.show == false}
                                    onClick={() => this.state.timeline.show ? this.updateTimeline(false) : {}}
                                />Profiles View
                                    <input id="btnTimeLine" className="mx-1 m-2 mt-3 ml-5" type="radio"
                                        checked={this.state.timeline.show == true}
                                        disabled={false}
                                        onClick={
                                            () => !this.state.timeline.show ? this.updateTimeline(true) : {}} />Update Timeline  
                                <p className="timelinenote " style={{ fontSize: "65%" }}> select inventory and timeline to activate this button </p>
                            </div>
                        }                       
                    </div>                   
                    <div className="p-1">
                        {!this.state.timeline.show &&
                        <div className=" pt-3 px-2 mx-3 d-flex flex-wrap ">                            
                                <><div >
                                {userRole == 'ROLE_Mol' && !deletedUser &&
                                    <button className="btn btn-mybtn px-5  " onClick={() => {                                       
                                    this.addClicked(filteredUserTitle)
                                }}>Add New</button>}
                                <CSVLink
                                    className="btn btn-mybtn px-3 ml-2"
                                    data={data} headers={headers} filename={"user-profiles-page.csv"}
                                    asyncOnClick={true}
                                    onClick={() => {}}
                                >Download this page
                                </CSVLink>
                                <button className="btn btn-mybtn px-3 ml-2" onClick={this.downloadReport}>Download All</button>
                                <CSVLink
                                    data={this.state.alldata}
                                    filename={"user-profiles-all.csv"}
                                    className="hidden"
                                    headers={headers}
                                    ref={this.csvLink}
                                    target="_blank"
                                />
                            </div>
                                {this.state.pager && <PaginationComponent {...this.state.pager}
                                    search={this.state.search}
                                    onNewSearch={(search)=>
                                        this.updateSearch(search)
                                    } />}</> </div>}
                        {userRole == 'ROLE_Mol' && this.state.timeline.show &&
                            <TimelineInnerComponent
                            ref={this.child}
                            filter={this.state.timeLineFilter}
                            updateLink={(search) => this.updateLink(search)}
                            updateTimeline={(value) => this.updateTimeline(value)}
                            setMessage={(msg) => { console.log("msg = "+msg);this.setState({ message: msg }) }}
                            refresh={() => {
                                this.updateTimeline(false); 
                            }}/>}
                        {!this.state.timeline.show &&
                            <>
                            {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                            {this.state.message && <div className="alert alert-success d-flex">{this.state.message}
                                {this.state.servermsg && <p> server msg = {this.state.servermsg}</p>}
                                <i class="fa fa-close ml-auto pr-3 pt-1" onClick={this.togglemsgbox}></i></div>}
                            <table className="table border-bottom my-table">
                                <thead>
                                    <tr>
                                        {userRole == 'ROLE_Mol' && <th className="">user
                                        <OrderByComponent name="userName" orderBy={this.state.orderBy} onClick={() => this.getOrderedList("userName")}/> 
                                            </th>}
                                        <th>product
                                        <OrderByComponent name="productName" orderBy={this.state.orderBy} onClick={() => this.getOrderedList("productName")}/>
                                           </th>
                                        <th>inventory number
                                        <OrderByComponent name="inventoryNumber" orderBy={this.state.orderBy} onClick={() => this.getOrderedList("inventoryNumber")}/>
                                       </th>
                                        <th className="">given at
                                         <OrderByComponent name="givenAt" orderBy={this.state.orderBy} onClick={() => this.getOrderedList("givenAt")}/>
                                            </th>
                                        <th>returned at
                                         <OrderByComponent name="returnedAt" orderBy={this.state.orderBy} onClick={() => this.getOrderedList("returnedAt")}/>
                                           </th>                                       
                                        {userRole == 'ROLE_Mol' && <th>Update &emsp;&nbsp; Delete
                                            {!this.state.filter.userId && !this.state.filter.allUser && !this.state.filter.myProfile && !this.state.filter.current &&
                                                <>< i class="fa fa-caret-down ml-2 hoverable"
                                                onClick={() => { this.setState({ showDeleteAll: !this.state.showDeleteAll }) }} />
                                                {this.state.showDeleteAll &&
                                                    <DeleteAllInnerComponent
                                                    items={this.state.filter.inventoryNumbers}
                                                    cancel={() => this.setState({ showDeleteAll: null })}
                                                    deleteAll={(date, id) => {
                                                        this.setState({ showDeleteAll: null }); this.deleteAllbefore(date, id)
                                                    }}/>}
                                                </>}</th>}
                                    </tr>
                                </thead>
                                <tbody>
                                    {this.state.items.map(
                                            (item, i) =>
                                                <>
                                                <tr key={item.id} >
                                                    {userRole == 'ROLE_Mol' && <td className={this.state.i == i ? "above-row border-r" : ""}>{item.userName}</td>}
                                                        <td className={this.state.i == i ? "above-row " : ""}>{item.productName}</td>
                                                        <td className={this.state.i == i ? "above-row" : ""}>
                                                            <p className="hoverable"
                                                                onClick={() =>
                                                                    this.props.history.push(`productdetails?Filter.id=${item.productDetailId}`)}>
                                                                {item.inventoryNumber}</p></td>
                                                    <td className={this.state.i == i ? "above-row" : ""}>{new Intl.DateTimeFormat("en-GB", {
                                                        month: "long",
                                                        day: "2-digit",
                                                        year: "numeric",
                                                    }).format(new Date(item.givenAt))}</td>                                                       
                                                        {item.profileDetail != null &&
                                                            <td className="hoverable"
                                                                onClick={() => {
                                                                    let showdts = this.state.showdts;
                                                                    if (showdts == undefined) showdts = [];
                                                                    showdts[i] = showdts[i] ? false : true;
                                                                    this.setState({ showdts: showdts })
                                                                }}><i class={this.state.showdts && this.state.showdts[i] ? "fa fa-angle-double-up" : "fa fa-angle-double-down"}
                                                                    aria-hidden="true"></i></td>
                                                        }
                                                        {item.profileDetail == null && item.condition != 'Available' &&
                                                            <td className="">{item.condition}</td>}
                                                            {item.profileDetail == null && item.condition == 'Available' &&
                                                            <td className=
                                                                {userRole == 'ROLE_Mol' && item.returnedAt == null ?
                                                                    this.state.i == i ? "above-row border-l with-btn" : "with-btn" : ""}>
                                                                {item.returnedAt != null &&
                                                                    new Intl.DateTimeFormat("en-GB", {
                                                                        month: "long",
                                                                        day: "2-digit",
                                                                        year: "numeric",
                                                                    }).format(new Date(item.returnedAt))}
                                                                {item.returnedAt == null && userRole == 'ROLE_Mol' && userName != item.userName &&
                                                                    <button className="btn btn-mybtn f-r"
                                                                        onClick={() => this.saveToGive(item, false)}>return</button>}
                                                                {item.returnedAt == null && userRole == 'ROLE_Mol' && userName == item.userName &&
                                                                    (this.state.i == null || this.state.i != i) &&
                                                                    <button className="btn btn-mybtn f-r"
                                                                        onClick={() => { console.log("give to clicked"); this.setState({ i: i }) }}>give to</button>}
                                                                {item.returnedAt == null && userRole == 'ROLE_Mol' && userName == item.userName && this.state.i == i &&
                                                                    <>
                                                                        <div className=
                                                                            {"inline d-flex above-label"}>
                                                                            <label>select&nbsp;user&nbsp;</label>
                                                                            <CustomSelect
                                                                                defaultMenuIsOpen={true}
                                                                            className={"inline inline-3 above-select"}
                                                                            items={this.state.usersToGive}
                                                                            value={this.state.selectedUserId}
                                                                                onChange={(selected) => { this.setState({ selectedUserId: selected.value }) }}
                                                                            />
                                                                        </div>
                                                                        <button className="btn btn-mybtn mr-1 above-btn" onClick={() => this.saveToGive(item, true)}>
                                                                            <i className="fa fa-save"></i></button>
                                                                        <button className="btn btn-mybtn btn-delete above-btn" onClick={() => this.cancelToGive()}>
                                                                            <i className="fa fa-close"></i></button>
                                                                    </>
                                                                }
                                                                {
                                                                    item.returnedAt == null &&
                                                                    userRole != 'ROLE_Mol' && '-'}</td>}                                                   
                                                    {userRole == 'ROLE_Mol' && <td><button className="btn btn-mybtn mr-1" onClick={() => this.updateClicked(item, i)}>Update</button>
                                                            <button className="btn btn-mybtn btn-delete"
                                                                disabled={item.userName == userName}
                                                                onClick={() => {
                                                                    if (window.confirm
                                                                        ('Are you sure ?\ndeleting a profile will just reassign it to your profile\ndo you wish to proceed ? '))
                                                                        this.deleteClicked(item.id)
                                                                }}>Delete</button></td>}
                                                    </tr>
                                                    {item.profileDetail != null && this.state.showdts[i] &&
                                                        <tr className="bold-border-bottom">
                                                        {userRole == 'ROLE_Mol' && <td></td>}                                                       
                                                        <td colSpan="4">
                                                            <div className="d-flex align-items-top">
                                                                <div className="inline wxs"><label>owings : </label></div>
                                                             <div className="inline w20">
                                                                <p>created At : </p>
                                                                <p>{item.profileDetail.createdAt}</p>
                                                            </div>
                                                            <div className="inline w20">
                                                                <p>modified At : </p>
                                                                <p>{item.profileDetail.modifiedAt}</p>
                                                            </div>
                                                            <div className="inline w20">
                                                                <p>owed Amount : </p>
                                                                    <p> {new Intl.NumberFormat("en-GB", {
                                                                        style: "currency",
                                                                        currency: "BGN",
                                                                        maximumFractionDigits: 2
                                                                    }).format(item.profileDetail.owedAmount)}</p>
                                                            </div>
                                                            <div className="inline w20">
                                                                <p>paid Amount : </p>
                                                                    <p>{new Intl.NumberFormat("en-GB", {
                                                                        style: "currency",
                                                                        currency: "BGN",
                                                                        maximumFractionDigits: 2
                                                                    }).format(item.profileDetail.paidAmount)}</p>
                                                                </div>
                                                                <div className="inline wxs">
                                                                    <p className="p-0">cleared :</p>
                                                                    {item.profileDetail.cleared ? < i class="fa fa-check ml-1" />
                                                                    : < i class="fa fa-false ml-1" />}</div>
                                                            </div>
 
                                                        </td>
                                                        {userRole == 'ROLE_Mol' && <td></td>}
                                                        </tr>}
                                                    </>
                                        )}
                                </tbody>                                
                            </table></>}
                    </div>
                </div>
            </div>

        )
    }
}

export default withRouter(ListUserProfilesComponent)