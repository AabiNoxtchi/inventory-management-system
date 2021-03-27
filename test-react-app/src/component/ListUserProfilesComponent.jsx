import React, { Component } from 'react';
import UserProfileDataService from '../service/UserProfileDataService';
import PaginationComponent from './PaginationComponent';
import UserProfileFilter from './Filters/UserProfileFilter';
import '../myStyles/Style.css';
import { CSVLink } from "react-csv";

import AuthenticationService from '../service/AuthenticationService';
import CustomSelect from './Filters/CustomSelect';
import UserProfileInnerComponent from './UserProfileInnerComponent';
import TimelineInnerComponent from './TimelineInnerComponent';



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
            filter: null,
            search: window.location.search || '',
            alldata: [],
            i: null,
            selectedUserId: null,
            profileShow: {
                profile: {}, show: false
            },
            timeline: {
                show:false
            }
        }
        this.refresh = this.refresh.bind(this)
        this.deleteClicked = this.deleteClicked.bind(this)
        this.updateClicked = this.updateClicked.bind(this)
       // this.addClicked = this.addClicked.bind(this)
        this.csvLink = React.createRef();
    }

    componentDidMount() {
       
        this.refresh();
    }

    refresh() {
        UserProfileDataService.retrieveAll(this.state.search)
            .then(
            response => {
                    //console.log("response data = "+JSON.stringify(response.data));
                    this.setState({
                        items: response.data.items || response.data.daoitems,
                        pager: response.data.pager,
                        filter: response.data.filter,
                         i: null,
                        selectedUserId: null,
                       
                    });
            }
        ).catch((error) => {
            this.setState({
                errormsg: '' + error == 'Error: Request failed with status code 401' ? 'need to login again !!!' : '' + error
            })
            })
    }

    

    downloadReport = () => {
        let newSearch = this.getSearchAll();
        UserProfileDataService.retrieveAll(newSearch)
            .then(response => {
                let data = response.data.items ;
                data = data.map(row => ({
                    ...row, givenAt: new Intl.DateTimeFormat("en-GB", {
                        month: "numeric",
                        day: "2-digit",
                        year: "numeric",
                    }).format(new Date(row.givenAt))
                    , returnedAt: new Intl.DateTimeFormat("en-GB", {
                        month: "numeric",
                        day: "2-digit",
                        year: "numeric",
                    }).format(new Date(row.returnedAt))
                }));
                this.setState({ alldata:data });
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
        UserProfileDataService.delete(id)
            .then(
                response => {
                    this.setState({ message: `Delete successful` })
                    this.refresh()
                }
        ).catch(error => {
            let msg = error.response && typeof error.response.data == 'string' ? error.response.data : error;
            this.setState({
                errormsg: msg
            })
        })
    }

   /* updateClicked(id) {
        this.props.history.push(`/userprofiles/${id}`)
    }*/
    updateTimeline(value) {
        if (this.state.timeline.show == value) return;
        if (value == true && this.state.filter&&(!this.state.filter.productDetailId || !this.state.filter.givenAfter) ) {
            //let msg = error.response && typeof error.response.data == 'string' ? error.response.data : error;
            this.setState({
                errormsg: `to update time line search for inventory first (by selecting inventory number from above search) ,
                                and at least limit the timeline given after, thank you!!!`
            })
            return;
        }       
        let timeline = this.state.timeline;
        timeline.show = value;
        this.setState({
            timeline: timeline,
            message: null,
        });

        /*let filter = this.state.filter;
        if (filter.userId != null || filter.myProfile != null) {

            let search = `userprofiles?timelineView=true&Filter.productDetailId=` + filter.productDetailId;
            search += filter.givenAfter != null ? `&Filter.givenAfter=` + filter.givenAfter : ``;
            search += filter.returnedBefore != null ? `&Filter.returnedBefore=` + filter.returnedBefore:``;
            window.location.href = search;
        }*/
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

    /*addforUserClicked = () => {
        let item = {

        }
    }*/

    addClicked (name) {
        //this.props.history.push(`/userprofiles/-1`)
        console.log("in add clicked");
        console.log("name = "+name);
        let item = {
            userId: this.state.filter.userId || null,
            userName: name
        }
        this.updateClicked(item,null)
    }

    cancelToGive = () => {
        console.log("canceltogive");
        this.setState({
            i: null,
            selectedUserId: null
        })
    }

    saveToGive = (item, giving) => {
        console.log("save to give clicked");

        let g = new Date();       
        g.setHours(g.getHours() - g.getTimezoneOffset() / 60);
        console.log("g after turn = " + g);

        let itemToSend = {
            previousId: item.id,
            userId: this.state.selectedUserId || null,
            productDetailId: item.productDetailId,
            givenAt: g,
           
            /*new Intl.DateTimeFormat("en-GB", {
                month: "numeric",
                day: "2-digit",
                year: "numeric",
            }).format(new Date())*/
           /* returnedAt: new Intl.DateTimeFormat("en-GB", {
                month: "numeric",
                day: "2-digit",
                year: "numeric",
            }).format(new Date())*/
        }

        console.log("itemto send = " + JSON.stringify(itemToSend));

        UserProfileDataService.save(itemToSend).then(
            response => {
                console.log("response = " + response.data);
                this.setState({ message: this.state.selectedUserId != null ? 'product given successfully ' : 'product returned successfully '})
               
               this.refresh();
            }).catch(error => {
                console.log("error = " + error);
                console.log("error.response = " + error.response)
            })
    }


    togglemsgbox = () => {
        this.setState({ message: null })
    }

   

    render() {

        const userRole = AuthenticationService.getLoggedUerRole();
        const userName = AuthenticationService.getLoggedUerName();
        //const data = this.state.items;
        const data = this.state.items.map(row => ({
            ...row, givenAt: new Intl.DateTimeFormat("en-GB", {
                month: "numeric",
                day: "2-digit",
                year: "numeric",
            }).format(new Date(row.givenAt))
            , returnedAt: new Intl.DateTimeFormat("en-GB", {
                month: "numeric",
                day: "2-digit",
                year: "numeric",
            }).format(new Date(row.returnedAt))
        })); 
        const dataAll = '';

        const filteredUserTitle = userRole == 'ROLE_Employee' || (this.state.filter != null && this.state.filter.myProfile != null) ? 'my profile'
            : this.state.filter != null && this.state.filter.userId != null ?
                this.state.filter.userNames.find(n => n.value == this.state.filter.userId).name
                : null;
        
        console.log("filteredUserTitle = " + filteredUserTitle);

        return (
            <div className="px-3">
                <div className={this.state.i != null ? "overlay d-block" : "d-none"}></div>
                {
                    this.state.profileShow.profile && this.state.profileShow.show == true &&
                    <UserProfileInnerComponent
                        profileShow={this.state.profileShow}
                    items={this.state.items}
                    filter={this.state.filter}
                        message={this.state.message}
                        // suppliers={this.state.filter.suppliers}
                        updateClicked={() => this.updateClicked(null)}
                        refresh={() => this.refresh()}
                        setMessage={(value) => this.setState({ message: `update successful` })}
                    //setdeliveryUpdateShow={(value) => this.setState({ deliveryUpdateShow: value })} 
                    />}


                {this.state.filter && <UserProfileFilter {...this.state.filter} userRole={userRole} timeline={this.state.timeline} />}
                <div className="border">
                    <div className="panel-heading">
                        <h5 className="panel-title p-2 d-inline-flex">
                            <strong> User Profiles</strong>
                            {filteredUserTitle != null && <span> &emsp;(&nbsp;{filteredUserTitle}&nbsp;)</span>}
                            
                        </h5>

                        {userRole == 'ROLE_Mol' &&
                           
                            <div className=" pull-right mr-5" style={{ position: "relative" }}>
                            
                                <input className="mx-1 m-2 mt-3" type="radio" 
                                    checked={this.state.timeline.show == false}
                                    onClick={() => this.state.timeline.show? this.updateTimeline(false) : {}}
                                />Profiles View
                       
                           
                                
                                    <input id="btnTimeLine" className="mx-1 m-2 mt-3 ml-5" type="radio"
                                        checked={this.state.timeline.show == true}
                                        disabled={!this.state.filter ||(this.state.filter && !(this.state.filter.productDetailId != null && this.state.filter.givenAfter != null))}

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
                                {userRole == 'ROLE_Mol' && //this.state.filter && this.state.filter.userId &&
                                    <button className="btn btn-mybtn px-5  " onClick={() => {
                                        //if (filteredUserTitle == null) this.updateClicked(null, 100)
                                        this.addClicked(filteredUserTitle)
                                    }
                                    }>Add New</button>}
                                <CSVLink
                                    className="btn btn-mybtn px-3 ml-2"
                                    data={data} headers={headers} filename={"user-profiles-page.csv"}
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
                                    filename={"user-profiles-all.csv"}
                                    className="hidden"
                                    headers={headers}
                                    ref={this.csvLink}
                                    target="_blank"
                                />
                            </div>
                                {this.state.pager && <PaginationComponent {...this.state.pager} />}</> </div>
                        }

                        {userRole == 'ROLE_Mol' && this.state.timeline.show &&
                            <TimelineInnerComponent
                            filter={this.state.filter}
                            updateTimeline={(value) => this.updateTimeline(value)}
                            setMessage={(msg) => { console.log("msg = "+msg);this.setState({ message: msg }) }}
                            refresh={() => { this.updateTimeline(false); this.refresh() }}

                    />}
                        {!this.state.timeline.show &&
                            <>
                            {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                            {this.state.message && <div className="alert alert-success d-flex">{this.state.message}
                                <i class="fa fa-close ml-auto pr-3 pt-1" onClick={this.togglemsgbox}></i></div>}
                            <table className="table border-bottom my-table">
                                <thead>
                                    <tr>
                                        {userRole == 'ROLE_Mol' && <th>user</th>}
                                        <th>product</th>
                                        <th>inventory number</th>
                                        <th>given at</th>
                                        <th>returned at</th>
                                        {/*userRole == 'ROLE_Mol' && this.state.filter && this.state.filter.myProfile &&
                                        <th className="with-btn"> give to </th>*/}
                                        <th>Update &emsp;&nbsp; Delete</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {
                                        this.state.items.map(
                                            (item, i) =>
                                                <tr key={item.id} >
                                                    {userRole == 'ROLE_Mol' && <td className={this.state.i == i ? "above-row border-r" : ""}>{item.userName}</td>}
                                                    <td className={this.state.i == i ? "above-row " : ""}>{item.productName}</td>
                                                    <td className={this.state.i == i ? "above-row" : ""}>{item.inventoryNumber}</td>
                                                    <td className={this.state.i == i ? "above-row" : ""}>{new Intl.DateTimeFormat("en-GB", {
                                                        month: "long",
                                                        day: "2-digit",
                                                        year: "numeric",
                                                    }).format(new Date(item.givenAt))}</td>
                                                    <td className=
                                                        {userRole == 'ROLE_Mol' && item.returnedAt == null ?
                                                            this.state.i == i ? "above-row border-l with-btn" : "with-btn" : ""}>{/*
                                                    this.state.filter && this.state.filter.userId && item.returnedAt == null*/}

                                                        {item.returnedAt != null
                                                            ?
                                                            new Intl.DateTimeFormat("en-GB", {
                                                                month: "long",
                                                                day: "2-digit",
                                                                year: "numeric",
                                                            }).format(new Date(item.returnedAt))
                                                            :
                                                            userRole == 'ROLE_Mol' && userName != item.userName
                                                                ?
                                                                <button className="btn btn-mybtn f-r"
                                                                    onClick={() => this.saveToGive(item, false)}>return</button>
                                                                :
                                                                (this.state.i == null || this.state.i != i) && item.returnedAt == null
                                                                    ? <button className="btn btn-mybtn f-r"
                                                                        onClick={() => { console.log("give to clicked"); this.setState({ i: i }) }}>give to</button>
                                                                    : this.state.i == i
                                                                        ?
                                                                        <>
                                                                            <div className=
                                                                                {"inline d-flex above-label "
                                                        /*this.state.i == i ? "inline above-label d-flex " : "inline above-label d-flex visible-n"*/}>
                                                                                <label>select&nbsp;user&nbsp;</label>
                                                                                <CustomSelect
                                                                                    defaultMenuIsOpen={true}
                                                                                    className={"inline inline-3 above-select"}
                                                                                    items={this.state.filter.userNames}
                                                                                    value={''}
                                                                                    onChange={(selected) => { this.setState({ selectedUserId: selected.value }) }}
                                                                                />

                                                                            </div>
                                                                            <button className="btn btn-mybtn mr-1 above-btn" onClick={() => this.saveToGive(item, true)}>
                                                                                <i className="fa fa-save"></i></button>
                                                                            <button className="btn btn-mybtn btn-delete above-btn" onClick={() => this.cancelToGive()}>
                                                                                <i className="fa fa-close"></i></button>

                                                                        </>
                                                                        : '-'}</td>
                                                    {/*userRole == 'ROLE_Mol' && this.state.filter && this.state.filter.myProfile &&
                                                    <td className={this.state.i == i ? "above-row with-btn border-l" : "with-btn"}>
                                                        {(this.state.i == null || this.state.i != i) && item.returnedAt == null && <button className="btn btn-mybtn"
                                                        onClick={() => { console.log("give to clicked");this.setState({ i: i }) }}>give to</button>}
                                                    {this.state.i == i &&
                                                        <>
                                                        
                                                        <div className=
                                                            {"inline d-flex above-label "
                                                        //this.state.i == i ? "inline above-label d-flex " : "inline above-label d-flex visible-n"
                                                        }>
                                                            <label>select&nbsp;user&nbsp;</label>                                                           
                                                                <CustomSelect
                                                                    defaultMenuIsOpen={true} 
                                                                className={"inline inline-3 above-select"}
                                                            items={this.state.filter.userNames}
                                                                value={''}
                                                                onChange={(selected) => { this.setState({ selectedUserId: selected.value }) }}
                                                            />
                                                           
                                                            </div>
                                                            <button className="btn btn-mybtn mr-1 above-btn" onClick={()=>this.saveToGive(item ,true)}>
                                                                <i className="fa fa-save"></i></button>
                                                            <button className="btn btn-mybtn btn-delete above-btn" onClick={()=>this.cancelToGive()}>
                                                                <i className="fa fa-close"></i></button>
                                                        
                                                       
                                                        </>
                                                    }</td>*/}
                                                    <td><button className="btn btn-mybtn mr-1" onClick={() => this.updateClicked(item, i)}>Update</button>
                                                        <button className="btn btn-mybtn btn-delete"
                                                            disabled={item.userName == userName} onClick={() => this.deleteClicked(item.id)}>Delete</button></td>
                                                </tr>
                                        )
                                    }
                                </tbody>
                                
                            </table></>}

                    </div>
                </div>
            </div>

        )
    }
}

export default ListUserProfilesComponent