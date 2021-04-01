import React, { Component } from 'react';
import ProductDetailDataService from '../service/ProductDetailDataService';
import UserProfileDataService from '../service/UserProfileDataService'
import '../myStyles/Style.css';
import DatePicker from "react-datepicker";
import CustomSelect from './Filters/CustomSelect';
import ProductDetailFilter from './Filters/ProductDetailFilter';
import Function from './Shared/Function'


class UserProfileInnerComponent extends Component {
    constructor(props) {
        super(props)

        this.state =
            {
                profileShow: props.profileShow,
                items: props.items,
                message: props.message,
                filter: props.filter,
            filteredNumbers: [],
           // search: '?isDiscarded=false&isAvailable=true',
            lastSearch: '?freeInventory=true&isDiscarded=false&isAvailable=true',//props.profileShow.x == null ? '?isDiscarded=false&isAvailable=true&freeInventory=true'
               // : '?isDiscarded=false&isAvailable=true&notIn=' + props.profileShow.profile.productDetailId,
            selectedDate: null,
            filteredcount: '',
            selectedPds: [],
            changedPd: null,
            ChangedUser: null,
            //allInventory:null,
            freeInventory: true,
            notDiscarded: true,
            available: true,
            withUser: !(props.filter.userId == null || props.filter.userId == undefined || props.filter.userId == 'undefined')//from user account or from main page

            }
        this.refresh = this.refresh.bind(this)
    }

    componentDidMount() {

       /* let search = this.state.lastSearch;
        search = '?freeInventory=true&isDiscarded=false&isAvailable=true';
        search = (this.state.profileShow.x == null && this.state.profileShow.profile.productDetailId) ?
            search+'&notIn=' + this.state.profileShow.profile.productDetailId
            : search;*/
        //console.log("search in mount = " + search);
       /* let show = this.state.profileShow;
        if (this.state.profileShow.x == null && this.state.filter.userId == null) {            
            //show.x = 100;
            show.profile.givenAt = new Date();
        }
        
        
        this.setState({
            profileShow: show,
           // lastSearch: search
        })*/
        //this.refresh(search)
        //console.log("item1 = " + JSON.stringify(this.state.profileShow))
        // console.log("this.state.profileShow.x == null)= " + (this.state.profileShow.x == null))
       /* console.log("component did mount = " );
        console.log("x = " + this.state.profileShow.x);
        console.log("this.state.profileShow.x == null)= " + (this.state.profileShow.x == null));
        console.log("item1 = " + JSON.stringify(this.state.profileShow))*/
      /*  if (this.state.profileShow.x == null) {
            let selected = this.getFilteredInventoty();
            // if (selected) {
            let show = this.state.profileShow;
            show.profile.inventoryNumber = selected ? selected.name : '';
            show.profile.productDetailId = selected ? selected.value : null;
            this.setState({ profileShow: show })
            // }
        }*/
       // console.log("item2 = " + JSON.stringify(this.state.profileShow))
      /****************************  this.refresh(this.state.lastSearch);
            if (this.state.profileShow.x == null && this.state.filter.productDetailId) {
                let selected = this.getFilteredInventoty();
                // if (selected) {
                //  if (this.state.filteredNumbers.find(n=>n.value == selected.))
                console.log("selected = " + selected);
                if (selected = null || selected == 'undefined') return;
                let show = this.state.profileShow;
                show.profile.inventoryNumber = selected ? selected.name : '';
                show.profile.productDetailId = selected ? selected.value : null;
                this.setState({ profileShow: show })
                // }
            }*****************************************/

        ProductDetailDataService.retrieveAllNumbers(this.state.lastSearch)
            .then(response => {
               // console.log("got response");
                console.log("this.state.filter.userId = " + this.state.filter.userId);
                console.log("this.state.withuser = " + this.state.withUser)

               
                this.setState({
                    filteredNumbers: response.data,
                    filteredcount: response.data.length
                })

                if (this.state.profileShow.x == null && this.state.filter.productDetailId) {
                    let selected = this.getFilteredInventoty();
                    // if (selected) {
                    //  if (this.state.filteredNumbers.find(n=>n.value == selected.))
                    console.log("selected = " + JSON.stringify(selected));
                    if (selected == null || selected == 'undefined') return;
                    let show = this.state.profileShow;
                    show.profile.inventoryNumber = selected ? selected.name : '';
                    show.profile.productDetailId = selected ? selected.value : null;
                    this.setState({ profileShow: show })
                    // }
                }


                // console.log("data length = " + this.state.filteredNumbers.length);
            }).catch(error =>
                console.log("error = " + error)
            )

       

       /* if (this.state.profileShow.x == null && this.state.filter.productDetailId) {
            let selected = this.getFilteredInventoty();
            // if (selected) {
          //  if (this.state.filteredNumbers.find(n=>n.value == selected.))
            console.log("selected = " + selected);
            if (selected = null || selected == 'undefined') return;
            let show = this.state.profileShow;
            show.profile.inventoryNumber = selected ? selected.name : '';
            show.profile.productDetailId = selected ? selected.value : null;
            this.setState({ profileShow: show })
            // }
        }*/


    }

    getFilteredInventoty() {//original
        let filteredInventory = (this.state.filter.productDetailId && this.state.filteredNumbers.length>0) ?
           // this.state.filter.inventoryNumbers.find(n => n.value == this.state.filter.productDetailId &&
           // ((this.state.filteredNumbers.find(f => f.value == n.value)) != 'undefined'))
            this.state.filteredNumbers.find(f => f.value == this.state.filter.productDetailId)
            : null;
        return filteredInventory
    }

    getFilteredUser() {//original
        let x = this.state.profileShow.x;
        let filteredUser = x != null ?
            { "value": this.state.items[x].userId, "label": this.state.items[x].userName }
            : (this.state.filter.userId) ? this.state.filter.users.find(n => n.value == this.state.filter.userId)
            : null;
        return filteredUser
    }

    refresh(search) {
       // console.log("search = " + search);
        ProductDetailDataService.retrieveAllNumbers(search)
            .then(response => {
                console.log("got response");
                this.setState({
                    filteredNumbers: response.data,
                    filteredcount: response.data.length
                })
               // console.log("data length = " + this.state.filteredNumbers.length);
            }).catch(error =>
                console.log("error = " + error)
                )
    }

    saveUpdated = () => {
        console.log("in save updated");
        let pdlist = this.state.selectedPds;
        let show = this.state.profileShow;
        let withUser = this.state.withUser;
        let item = show.profile;
        let x = this.state.profileShow.x;
        let original = x != null ? this.state.items[x] : null;
       // item.inventoryNumber = item.inventoryNumber ? item.inventoryNumber.trim() : item.inventoryNumber;
       // let previousItem = x!=null&&x!=100 ? this.state.items[x] : null;
       // console.log("validate item = " + JSON.stringify(item));
        //console.log("(x==null && pdlist.length < 1)  = " + (x == null && pdlist.length < 1));
        //console.log("(!item.inventoryNumber || !item.givenAt) = " + (!item.inventoryNumber || !item.givenAt));
       // console.log("(x==null && pdlist.length < 1)  = " + (x == null && pdlist.length < 1));
        if (x==null && withUser && pdlist.length < 1) {
            show.error = 'must at least select 1 inventory !!!'
            this.setState({ profileShow: show })
            console.log("error")
        }
        else if (!item.userId || item.userId == 'undefined') {
            show.error = 'user not selected !!!'
            this.setState({ profileShow: show })
        }
        else if (x == null && !withUser  && (!item.inventoryNumber || item.inventoryNumber == "undefined" || item.inventoryNumber.trim().length < 1)) {
            show.error = "inventory not selected !!!";//"all fields are required !!!"
            this.setState({ profileShow: show })
        }
        else if (!withUser && x != null && original == null) {
            show.error = "original item not found !!!";//"all fields are required !!!"
            this.setState({ profileShow: show })
        } else if (!withUser && x != null && original.userId == item.userId && original.productDetailId == item.productDetailId) {
            show.error = "item hasn't changed !!!";//"all fields are required !!!"
            this.setState({ profileShow: show })
        }
       /* else if (x != null && (!item.inventoryNumber || !item.givenAt)) {            
                show.error = "all fields are required !!!"
                this.setState({ profileShow: show })
        }*/
       /* else if (item.returnedAt != null || (item.givenAt!=null &&!item.givenAt.isDateEqual(new Date))) {
            show.error = "time can't be changed !!!"
            this.setState({ profileShow: show })
        }*/
       /* else if (previousItem != null && (
                item.productDetailId == previousItem.productDetailId && this.isDateEqual(item.givenAt, previousItem.givenAt) && (
                    (item.returnedAt == null && previousItem.returnedAt == null) || this.isDateEqual(item.returnedAt, previousItem.returnedAt))
                && item.userId == previousItem.userId)) {
                show.error = "item hasn't changed !!!";
                this.setState({ profileShow: show });
                console.log("error in comparing items");           
        } */
        else {
          //  console.log("comparison pass")
            let date = (item.givenAt && new Date(item.givenAt)) || new Date();
           // let g = new Date();
            date.setHours(date.getHours() - date.getTimezoneOffset() / 60);
           // console.log("g after turn = " + g);
          //  console.log("date = " + date);
          //  console.log("item.givenAt = " + item.givenAt);
            //console.log("typeof date " + (typeof date));
          //  date = date.toISOString();
            //console.log("date = " + date);
          //  date = date.substring(0, date.indexOf('T'))
            // console.log("date = " + date);
            item.givenAt = date;//????????????
            if (x == null && withUser) {
                let ids = [];                
                pdlist.map(pd => ids.push(pd.value));                
                item.productDetailIds = ids;
               // console.log("date2 = " + item.givenAt);
            }
            console.log("sending item = " + JSON.stringify(item));
            UserProfileDataService.save(item).then(
                response => {
                    console.log("response = " + response.data);
                    this.props.updateClicked();
                    this.props.refresh();
                    this.props.setMessage("msg");
                    //this.setState({ message: this.state.selectedUserId != null ? 'product given successfully ' : 'product returned successfully ' })

                    //this.refresh();
                }).catch(error => {
                    
                    /*let msg = error.response && typeof error.response.data == 'string' ? error.response.data :
                        error.response.data.message ? error.response.data.message : error;*/

                    let msg = Function.getErrorMsg(error);
                   // console.log("error = " + error);
                  // console.log("error.response = " + error.response);
                    console.log("json error = " + JSON.stringify(error.response));
                    show.error = 'error : ' + msg;
                    this.showError(show);
                    
                })
        }

    }

    showError(show) {
        let time = 10;
        this.setState({ profileShow: show })
        this.myInterval = setInterval(() => {
            time = time - 1;
            if (time == 0) {
                show.error = null;
                this.setState(({ show }) => ({
                    profileShow: show
                }))
                clearInterval(this.myInterval)
            }
        }, 1000)
    }

    componentWillUnmount() {
        clearInterval(this.myInterval)
    }

    onProductChange = (selected) => {

       // console.log("selected.value = " + selected.value);
        this.getNewSearch('productId', selected.value);
       // let search = this.getNewSearch('productId', selected.value);
        //this.refresh(search)

    }

    onFilterfreeInventoryChange(value) {
        let freeInventory = this.state.freeInventory;
        freeInventory = !freeInventory;
        this.setState({ freeInventory: freeInventory })

        this.getNewSearch('freeInventory', value.target.value);
    }

    onFilteravailableChange(value) {
        let available = this.state.available;
        //available = !available;
        this.setState({ available: !available })

        this.getNewSearch('isAvailable', value.target.value);

    }
    onFilternotDiscardedChange(value) {
        let discarded = this.state.notDiscarded;
        //available = !available;
        this.setState({ notDiscarded: !discarded })

        this.getNewSearch('isDiscarded', value.target.value);
    }

    getNewSearch = (name, value) => {

        let search = this.state.lastSearch;
        let newPath = ``;

        if (search.length > 1) {
            while (search.charAt(0) === '?') {
                search = search.substring(1);
            }
            let searchItems = search.split('&');
            for (let i = 0; i < searchItems.length; i++) {
                if (!searchItems[i].startsWith(name))
                    newPath += searchItems[i] + '&'               
            }
        }

      
        if (value && value != 'undefined')
            newPath += name + '=' + value;
        else {
            if (name == 'notIn') {
                let ids = ''
                this.state.selectedPds.map(pd => ids += pd.value + ',')
                ids = ids.substring(0, ids.length - 1);
                newPath+= name+'='+ids
            }
            else
                newPath = newPath.substring(0, newPath.length - 1);
        }
        //console.log('newPath =' + newPath);
         newPath = '?' + newPath
        this.setState({ lastSearch: newPath })
        this.refresh(newPath)
       // return newPath;


    }
    oninventoryAdd = (selected) => {
       // console.log("in add inventory")
        if (selected == null) return;
        if (selected.name == '') return;
        

        if (this.state.profileShow.x == null && this.state.withUser) {
            let selectedPds = this.state.selectedPds;
            selectedPds.push({ name: selected.label, value: selected.value });
            this.setState({ selectedPds: selectedPds })
            this.getNewSearch('notIn')
        } else {
          //  selectedPds[0] = { name: selected.label, value: selected.value }
            let up = this.state.profileShow;
            up.profile.inventoryNumber = selected.label;
            up.profile.productDetailId = selected.value;
            this.setState({
                profileShow: up,
               // selectedPds: selectedPds,
                changedPd:true
            })
        }
       // console.log("this.state.profile = " + JSON.stringify(this.state.profileShow));
    }

    onUserChange(selected) {
        console.log("label = " + selected.label + " value = " + selected.value);
        let up = this.state.profileShow;
        up.profile.userName = selected.label;
        up.profile.userId = selected.value;
        this.setState({
            profileShow: up,
            // selectedPds: selectedPds,
            changedUser: true
        })
    }

    returnOriginalPd() {

        let up = this.state.profileShow;
        //if (up.x == null) return;

        let original = this.getFilteredInventoty()//this.state.items[up.x];
        up.profile.inventoryNumber = original == null ? '' : original.name;
        up.profile.productDetailId = original == null ? null : original.value;
        this.setState({
            profileShow: up,
            changedPd:null
        })
    }

    returnOriginalUser() {
        let up = this.state.profileShow;
       // if (up.x == null) return;

        let original = this.getFilteredUser()//this.state.items[up.x];

        up.profile.userId = (original == null) ? null : original.value;
        up.profile.userName = (original == null) ? '...' : original.label;
        this.setState({
            profileShow: up,
            changedUser: null
        })
    }

    oninventoryRemove (i) {
      // console.log("in removeiventory")
        let selectedPds = this.state.selectedPds;
        selectedPds.splice(i, 1);
        this.setState({ selectedPds: selectedPds })
        this.getNewSearch('notIn')
    }


    onFilterDateChange = (date) => {
       // console.log("2 = " + date);
       // if (date != null) console.log("type of 2" + typeof (date));
       // console.log("this.state.selectedDate 1 = " + this.state.selectedDate);
        //if (this.state.selectedDate != null) console.log("type of 1" + typeof(this.state.selectedDate));
        

        if (this.isDateEqual(this.state.selectedDate, date)) return;
        
       
       if(date) {
            date = date.toISOString();
            date = date.substring(0, date.indexOf('T'))
        } 
        /*date = new Intl.DateTimeFormat("en-GB", {
            month: "numeric",
            day: "2-digit",
            year: "numeric",
        }).format(new Date(date));*/

      //  console.log("date form = " + date);
        this.setState({ selectedDate: date });
        this.getNewSearch('dateCreatedAfter', date);
       // let search = 
        
        
    }

    onDateGivenChange = (date) => {
       // console.log("on date change date = " + date);
        let profileShow = this.state.profileShow;
        profileShow.profile.givenAt = date;
        this.setState({
            profileShow: profileShow
        })
    }
    onDateReturnedChange = (date) => {
       // console.log("date = " + date);
        let profileShow = this.state.profileShow;
        profileShow.profile.returnedAt = date;
        this.setState({
            profileShow: profileShow
        })
    }

    isDateEqual = (date1, date2) => {
        //date2 string
        // date1 string or date 
       // if (!date1) return false;    //???????????????????????
       /* console.log("date1 = " + date1);
        console.log("typeof date1 = " + typeof date1);
        console.log(" date2 = " + date2);
        console.log("typeof date2 = " + typeof date2);*/
       
        if (date1 == date2) return true       
        else {
            date1 = new Date(date1);
            date2 = new Date(date2);
            //console.log("date1 = " + date1);
            //console.log(" date2 = " + date2);
            if (date1.getFullYear() === date2.getFullYear() &&
                date1.getMonth() === date2.getMonth() &&
                date1.getDate() === date2.getDate())
                return true
        }
        return false
    }
    

    render() {
        console.log("this.state.profileShow.x = " + (this.state.profileShow.x ))
        console.log("this.state.profileShow.x == null = " + (this.state.profileShow.x == null))
        let height = (this.state.profileShow.x == null) ? "70%" : "55%";
        return (
            <>
                {console.log("rendering")}
                <div className={this.state.profileShow.show ? "overlay d-block" : "d-none"}></div>
                <div className={this.state.profileShow.show ? "modal d-block " : "d-none"} style={{ width: "80%", height: height , overflow: "visible" }} >

                    <div className="">
                        <span class="close pt-2" onClick={() => this.props.updateClicked(null)}>&times;</span>
                        <h3 className="inline pt-3 pl-3 pb-1">{this.state.profileShow.x != null ? 'Update' : 'Add'} profile &emsp;({this.state.profileShow.profile.userName})</h3>

                        {this.state.profileShow.x == null && 
                            <div className="inline pull-right mt-5 mr-5 move-top top-c">
                                <div className="mr-5">

                            {/*  <label className="">
                                <input className="m-0 " type="radio" value={!this.state.allInventory}
                                    checked={this.state.allInventory}
                                    onClick={(value) => { }}
                                /><span style={{ fontSize: "80%", paddingLeft: "3px", marginRight: "10px"}}>all inventory</span></label>*/}
                            <label className="">
                                    <input className="m-0" type="radio" value={!this.state.freeInventory}
                                        checked={this.state.freeInventory}
                                      //  onClick={() => { }}
                                    // (value) => {this.onFilterfreeInventoryChange(value) }} 
                                    />
                                    <span style={{ fontSize: "80%", paddingLeft: "3px", marginRight: "10px" }}>free inventory</span></label>
                            <label className="">
                                <input className="m-0" type="radio" value={!this.state.available}
                                    checked={this.state.available}
                                   // onClick={
                                    // (value) => { this.onFilteravailableChange(value) }} 
                                    />
                                    <span style={{ fontSize: "80%", paddingLeft: "3px", marginRight: "10px" }}>available</span></label>
                
                            <label className="mr-5">
                                <input className="m-0" type="radio" value={!this.state.notDiscarded}
                                    checked={this.state.notDiscarded}
                                   // onClick={
                                    // (value) => { this.onFilternotDiscardedChange(value) }}
                                    />
                                    <span style={{ fontSize: "80%", paddingLeft: "3px", marginRight: "10px" }}>not discarded</span></label>
                            </div>
                        </div>
                        }
                        
                    </div>
                   
                    <div className="mt-0 inline w100">

                        {this.state.profileShow.x == null &&
                            <div className="border pt-1 b-r r-c foo"> {/*********************** small filter **************************/}
                                <label className="move-top top-l" style={{ fontSize: "80%" }}>filter for inventory select</label>

                                <div className="inline w40 pr-1">
                                    <h6>product : </h6>
                                    <CustomSelect
                                        className={"inline w100 ml-0"}
                                        items={this.state.filter.productNames}
                                        onChange={(selected) => this.onProductChange(selected)}
                                    />
                                </div>

                                <div className="inline w20">
                                    <h6 className="">created after :</h6>
                                    <div className="inline w100">
                                        <DatePicker
                                            className="form-control w100 p-2 ml-0"
                                            dateFormat="dd MMMM yyyy"
                                            locale="en-GB"
                                            isClearable
                                            placeholderText="..."
                                            selected={this.state.selectedDate && new Date(this.state.selectedDate) || null}
                                            onChange={selected => {
                                                this.onFilterDateChange(selected)
                                            }} />
                                    </div>
                                </div>

                                <span className="move-top top-r" style={{ fontSize: "80%" }}> {this.state.filteredcount} items found</span>
                                <div className="inline w40">
                                    <h6 className="inline">select inventory :&nbsp;</h6>
                                    <CustomSelect
                                        className={"inline w100 ml-0"}
                                        items={this.state.filteredNumbers}
                                        onChange={(selected) => this.oninventoryAdd(selected)}
                                    />
                                </div>

                            </div>
                        }
                    </div>
                    {/*******************    filter over  ******************/}

                    {this.state.profileShow.error && this.state.profileShow.error.length > 1 && // error div
                        <div className="alert alert-warning d-flex mx-1">{this.state.profileShow.error}
                            <i class="fa fa-close ml-auto pr-3 pt-1"
                                onClick={() => {
                                    let show = this.state.profileShow;
                                    show.error = '';
                                    this.setState({ profileShow: show })
                                }}>
                            </i>
                        </div>
                    }
                    {/***************  form  ********************/}
                    <div className={this.state.profileShow.error && this.state.profileShow.error.length > 1 ?
                        "d-flex align-items-top flex-wrap" : "mt-5 d-flex align-items-top flex-wrap"}>

                        <div className="inline w40 m-0 ">{/*************** form right **************/}

                            <div className="inline m-0 pl-5">
                    <h6 >date given :</h6> 
                    <div>
                                     <DatePicker
                                        className="form-control inline-2-5 p-2"
                            dateFormat="dd MMMM yyyy"
                                        locale="en-GB"
                                        maxDate={(this.state.profileShow.x!=null && new Date(this.state.profileShow.profile.givenAt)) ||
                                            new Date()}
                                        minDate={(this.state.profileShow.x!=null && new Date(this.state.profileShow.profile.givenAt)) ||
                                            new Date()}
                                      //  maxDate={this.state.profileShow.profile.returnedAt && new Date(this.state.profileShow.profile.returnedAt) || new Date()}
                /*isClearable
                placeholderText="select Date!"*/
                                       // selected={(this.state.profileShow.profile.givenAt && new Date(this.state.profileShow.profile.givenAt)) ||
                                         //   new Date()}
                                        selected={(this.state.profileShow.x!=null && new Date(this.state.profileShow.profile.givenAt)) ||
                                           new Date()}
                            onChange={date => {
                                this.onDateGivenChange(date);
                                //console.log("date changed = " + date);                               
                            }} />
                    </div>
                    </div>

                            <div className="inline m-0 px-5">
                        <h6 >date returned :</h6>
                        <div>
                            <DatePicker
                                            className="form-control inline-2-5 p-2"
                                            dateFormat="dd MMMM yyyy"
                                            locale="en-GB"
                                            //  minDate={(this.state.profileShow.profile.givenAt && new Date(this.state.profileShow.profile.givenAt)) ||
                                            //    !this.state.profileShow.profile.id && new Date()}
                                            // maxDate={new Date()}
                                            /* disabled={(this.state.profileShow.profile.id && this.state.profileShow.profile.id > 0 &&
                                                 this.state.profileShow.profile.returnedAt) ||
                                                 (!this.state.profileShow.profile.id && this.state.profileShow.x == 100 ) ? '' : 'disabled'}*/
                                            disabled
                                            selected={(this.state.profileShow.x!=null && new Date(this.state.profileShow.profile.returnedAt)) || null}
                                                                           // null}
                            /*isClearable
                            placeholderText="select Date!"*/
                                       // selected={(this.state.profileShow.profile.returnedAt && new Date(this.state.profileShow.profile.returnedAt)) || 
                               // null}
                               // onChange={date => {
                                   // this.onDateReturnedChange(date);
                                    //console.log("date changed = " + date);                               
                                    // }}
                                    />
                        </div>
                            </div>

                            <div className="ml-5 mt-3 d-flex justify-content-center">
                                <button className="btn btn-mybtn p-x-5" onClick={this.saveUpdated}>Save</button>
                                <button className="btn btn-mybtn btn-delete px-5" onClick={() => this.props.updateClicked(null)}>Cancel</button>
                            </div>
                        
                        </div> {/*************** form right over  **************/}

                        <div className="inline w60">{/*************** form left **************/}
                           
                            <h6 className="required-field">selected inventory :</h6>
                            {this.state.profileShow.x == null && this.state.withUser && this.state.selectedPds &&
                                this.state.selectedPds.map((pd, i) =>
                                    <div>                                        
                                    {i+1} :&nbsp;<input value={pd.name} className='form-control inline w80 m-0 p-2 pl-3' />
                                       
                                        <button className="btn btn-mybtn btn-delete m-0 ml-1" type="button"
                                            onClick={() => {
                                                this.oninventoryRemove(i);
                                            }}><i class="fa fa-close ml-auto">                                                
                                            </i></button>
                                    </div>
                                    )}
                                {(!this.state.withUser || this.state.profileShow.x != null) &&
                                <>
                                <input value={this.state.profileShow.profile.inventoryNumber || ''} className='form-control inline w90 m-0 p-2 pl-3'
                                    //onChange={() => { this.oninventoryAdd(null) }}
                                   // editable={false}
                                />{this.state.changedPd && <i class="fa fa-undo ml-1" onClick={() => { this.returnOriginalPd()}}>
                                </i>}
                                {this.state.profileShow.x==null &&
                                    <p style={{ fontSize: "70%" }}> ps : selecting inventory from filter will change current inventory </p>}
                                
                                {/*console.log("this.state.profileShow.profile.userId = " + this.state.profileShow.profile.userId)*/}
                                {/*console.log("typeof this.state.profileShow.profile.userId = " + typeof this.state.profileShow.profile.userId)*/}
                                <h6 className="mt-1 required-field">select user :</h6>
                                <CustomSelect
                                    className={"b-r inline w80 m-0 p-0"}
                                    items={this.state.filter.userNames}
                                    value={this.state.profileShow.profile.userId}
                                    onChange={(selected) => this.onUserChange(selected)}
                                />{this.state.changedUser && <i class="fa fa-undo ml-1" onClick={() => { this.returnOriginalUser() }}>
                                </i>}

                                </>
                            }

                            
                        </div>{/**********  form left over **********************/}
                    </div>{/*************** form over ********************/}
                    {this.state.profileShow.x==null &&
                        <p style={{ fontSize: "80%" }}> ps : changing date will change previous records as well and may cause lose of data </p>}
                    </div>
            </>
        )
    }
}

export default UserProfileInnerComponent