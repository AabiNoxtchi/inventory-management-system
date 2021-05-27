import React, { Component } from 'react';
import ProductDetailDataService from '../service/ProductDetailDataService';
import UserProfileDataService from '../service/UserProfileDataService'
import '../myStyles/Style.css';
import DatePicker from "react-datepicker";
import CustomSelect from './Filters/CustomSelect';
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
                lastSearch: '?freeInventory=true&isDiscarded=false&econdition=Available',            
                selectedDate: null,
                filteredcount: '',
                selectedPds: [],
                changedPd: null,
                ChangedUser: null,
                freeInventory: true,
                notDiscarded: true,
                available: true,
                withUser: !(props.filter.userId == null || props.filter.userId == undefined || props.filter.userId == 'undefined'),
            }
        this.refresh = this.refresh.bind(this)
    }

    componentDidMount() {     
        if (this.state.profileShow.x) return;
        ProductDetailDataService.retrieveAllNumbers(this.state.lastSearch)
            .then(response => {              
                this.setState({
                    filteredNumbers: response.data,
                    filteredcount: response.data.length
                })

                if (this.state.profileShow.x == null && this.state.filter.productDetailId) {
                    let selected = this.getFilteredInventoty();                   
                    if (selected == null || selected == 'undefined') return;
                    let show = this.state.profileShow;
                    show.profile.inventoryNumber = selected ? selected.name : '';
                    show.profile.productDetailId = selected ? selected.value : null;
                    this.setState({ profileShow: show })                   
                }
            }).catch(() => { })
    }

    getFilteredInventoty() {//original
        let filteredInventory = (this.state.filter.productDetailId && this.state.filteredNumbers.length>0) ?           
            this.state.filteredNumbers.find(f => f.value == this.state.filter.productDetailId)
            : null;
        return filteredInventory
    }

    getFilteredUser() {//original
        let x = this.state.profileShow.x;
        let filteredUser = x != null ?
            { "value": this.state.items[x].userId, "label": this.state.items[x].userName }
            : (this.state.filter.userId) ? this.state.filter.userNames.find(n => n.value == this.state.filter.userId)
            : null;
        return filteredUser
    }

    refresh(search) {
        ProductDetailDataService.retrieveAllNumbers(search)
            .then(response => {
                this.setState({
                    filteredNumbers: response.data,
                    filteredcount: response.data.length
                })
            }).catch(() => { })
    }

    saveUpdated = () => {
        let pdlist = this.state.selectedPds;
        let show = this.state.profileShow;
        let withUser = this.state.withUser;
        let item = show.profile;
        let x = this.state.profileShow.x;
        let original = x != null ? this.state.items[x] : null;
       
        if (x==null && withUser && pdlist.length < 1) {
            show.error = 'must at least select 1 inventory !!!'
            this.setState({ profileShow: show })
        }
        else if (!item.userId || item.userId == 'undefined') {
            show.error = 'user not selected !!!'
            this.setState({ profileShow: show })
        }
        else if (x == null && !withUser  && (!item.inventoryNumber || item.inventoryNumber == "undefined" || item.inventoryNumber.trim().length < 1)) {
            show.error = "inventory not selected !!!";
            this.setState({ profileShow: show })
        }
        else if (!withUser && x != null && original == null) {
            show.error = "original item not found !!!";
            this.setState({ profileShow: show })
        } else if ( x != null && original.userId == item.userId && original.productDetailId == item.productDetailId &&
            ( (!item.profileDetail && !original.profileDetail) || (item.profileDetail && (item.paidPlus == 0 || item.paidPlus == undefined)))) {
            show.error = "item hasn't changed !!!";
            this.setState({ profileShow: show })
        }      
        else {         
            let date = (item.givenAt && new Date(item.givenAt)) || new Date();
           date.setHours(date.getHours() - date.getTimezoneOffset() / 60);          
            item.givenAt = date;
            if (x == null && withUser) {
                let ids = [];                
                pdlist.map(pd => ids.push(pd.value));                
                item.productDetailIds = ids;
               }
          UserProfileDataService.save(item).then(
                response => {
                    let msg = 
                        pdlist && pdlist.length > 0 ? '' + pdlist.length + ' items have been given to ' + this.state.profileShow.profile.userName + "'s " :
                            this.state.profileShow.profile.userName + "'s profile has been updated successfully ";
                    this.props.updateClicked();
                    this.props.refresh();
                    this.props.setMessage(msg);                  
                }).catch(error => {
                    let msg = Function.getErrorMsg(error);                  
                    show.error = 'error : ' + msg;
                    this.setState({ profileShow: show })
                   this.showError();                    
                })
        }

    }

    showError(msg) {
        let time = 8;      
        this.myInterval = setInterval(() => {
            time = time - 1;
            if (time == 0) {
                let show = this.state.profileShow;
                show.error = null;
                this.setState({
                    profileShow: show
                })
                clearInterval(this.myInterval)
            }
        }, 1000)
    }

    componentWillUnmount() {
        clearInterval(this.myInterval)
    }

    onProductChange = (selected) => {
        this.setState({ selectedProductId: selected.value })
        this.getNewSearch('productId', selected.value);
    }

    onFilterfreeInventoryChange(value) {
        let freeInventory = this.state.freeInventory;
        freeInventory = !freeInventory;
        this.setState({ freeInventory: freeInventory })
        this.getNewSearch('freeInventory', value.target.value);
    }

    onFilteravailableChange(value) {
        let available = this.state.available;
        this.setState({ available: !available })
        this.getNewSearch('isAvailable', value.target.value);
    }
    onFilternotDiscardedChange(value) {
        let discarded = this.state.notDiscarded;
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
         newPath = '?' + newPath
        this.setState({ lastSearch: newPath })
        this.refresh(newPath)

    }
    oninventoryAdd = (selected) => {
        if (selected == null) return;
        if (selected.name == '') return;
        if (this.state.profileShow.x == null && this.state.withUser) {
            let selectedPds = this.state.selectedPds;
            selectedPds.push({ name: selected.label, value: selected.value });
            this.setState({ selectedPds: selectedPds })
            this.getNewSearch('notIn')
        } else {
            let up = this.state.profileShow;
            up.profile.inventoryNumber = selected.label;
            up.profile.productDetailId = selected.value;
            this.setState({
                profileShow: up,
                changedPd:true
            })
        }
    }

    onUserChange(selected) {
        let up = this.state.profileShow;
        let x = up.x;
        if (x == null || x < 0) {
            let found = this.state.filter.userNames.find(n => n.value == selected.value);
            if (found && found.filterBy) {
                up.error = "user is deleted, can't assign him new inventories !!! ";
                this.setState({ profileShow: up })
                this.showError();
                return;
            }
        }
        up.profile.userName = selected.label;
        up.profile.userId = selected.value;
        this.setState({
            profileShow: up,
            changedUser: true
        })
    }

    returnOriginalPd() {
        let up = this.state.profileShow;
        let original = this.getFilteredInventoty()
        up.profile.inventoryNumber = original == null ? '' : original.name;
        up.profile.productDetailId = original == null ? null : original.value;
        this.setState({
            profileShow: up,
            changedPd:null
        })
    }

    returnOriginalUser() {
        let up = this.state.profileShow;
        let original = this.getFilteredUser()
        up.profile.userId = (original == null) ? null : original.value;
        up.profile.userName = (original == null) ? '...' : original.label;
        this.setState({
            profileShow: up,
            changedUser: null
        })
    }

    oninventoryRemove (i) {
        let selectedPds = this.state.selectedPds;
        selectedPds.splice(i, 1);
        this.setState({ selectedPds: selectedPds })
        this.getNewSearch('notIn')
    }

    onFilterDateChange = (date) => {
       if (this.isDateEqual(this.state.selectedDate, date)) return; 
       if(date) {
            date = date.toISOString();
            date = date.substring(0, date.indexOf('T'))
        }        
        this.setState({ selectedDate: date });
        this.getNewSearch('dateCreatedAfter', date);      
        
    }

    onDateGivenChange = (date) => {
        let profileShow = this.state.profileShow;
        profileShow.profile.givenAt = date;
        this.setState({
            profileShow: profileShow
        })
    }
    onDateReturnedChange = (date) => {
        let profileShow = this.state.profileShow;
        profileShow.profile.returnedAt = date;
        this.setState({
            profileShow: profileShow
        })
    }

    isDateEqual = (date1, date2) => {
        if (date1 == date2) return true       
        else {
            date1 = new Date(date1);
            date2 = new Date(date2);           
            if (date1.getFullYear() === date2.getFullYear() &&
                date1.getMonth() === date2.getMonth() &&
                date1.getDate() === date2.getDate())
                return true
        }
        return false
    }
    

    render() {
     let height = (this.state.profileShow.x == null) ? "70%" : this.state.profileShow.profile.profileDetail ? "65%" : "52%";
        return (
            <>               
                <div className={this.state.profileShow.show ? "overlay d-block" : "d-none"}></div>
                <div className={this.state.profileShow.show ? "modal d-block " : "d-none"} style={{ width: "80%", height: height , overflow: "visible" }} >

                    <div className="">
                        <span class="close pt-2" onClick={() => this.props.updateClicked(null)}>&times;</span>
                        <h3 className="inline pt-3 pl-3 pb-1">{this.state.profileShow.x != null ? 'Update ' : 'Add '}
                            profile &emsp;({this.state.profileShow.profile.userName})</h3>
                        {this.state.profileShow.x == null && 
                            <div className="inline pull-right mt-5 mr-5 move-top top-c">
                                <div className="mr-5">
                            <label className="">
                                    <input className="m-0" type="radio" value={!this.state.freeInventory}
                                        checked={this.state.freeInventory}                                    
                                    />
                                    <span style={{ fontSize: "80%", paddingLeft: "3px", marginRight: "10px" }}>free inventory</span></label>
                            <label className="">
                                <input className="m-0" type="radio" value={!this.state.available}
                                    checked={this.state.available}                                   
                                    />
                                    <span style={{ fontSize: "80%", paddingLeft: "3px", marginRight: "10px" }}>available</span></label>                
                            <label className="mr-5">
                                <input className="m-0" type="radio" value={!this.state.notDiscarded}
                                    checked={this.state.notDiscarded}
                                   />
                                    <span style={{ fontSize: "80%", paddingLeft: "3px", marginRight: "10px" }}>not discarded</span></label>
                            </div>
                        </div>
                        }                        
                    </div>
                    {this.state.profileShow.x == null &&
                        <div className="mt-0 inline w100">
                            <div className="border pt-1 b-r r-c foo"> {/*********************** small filter **************************/}
                                <label className="move-top top-l" style={{ fontSize: "80%" }}>filter for inventory select</label>
                            <div className="inline w40 pr-1">
                                <h6 className="ml-1 pl-1">product </h6>
                                    <CustomSelect
                                        className={"inline w100 ml-0"}
                                    items={this.state.filter.productNames}
                                    value={this.state.selectedProductId}
                                        onChange={(selected) => this.onProductChange(selected)}
                                    />
                                </div>
                                <div className="inline w20">
                                <h6 className="ml-1 pl-1">created after</h6>
                                    <div className="inline w100">
                                        <DatePicker
                                            className="form-control w100 p-2 ml-0"
                                            dateFormat="dd MMMM yyyy"
                                        locale="en-GB"
                                        maxDate={new Date()}
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
                                    <h6 className="inline ml-1 pl-1">select inventory&nbsp;</h6>
                                    <CustomSelect
                                        className={"inline w100 ml-0"}
                                        items={this.state.filteredNumbers}
                                        onChange={(selected) => this.oninventoryAdd(selected)}
                                    />
                                </div>
                        </div>
                    </div>
                        }                  
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
                    <h6 >date given </h6> 
                                <div>
                                    <DatePicker
                                        className="form-control inline-2-5 p-2"
                                        dateFormat="dd MMMM yyyy"
                                        locale="en-GB"
                                        maxDate={(this.state.profileShow.x != null && new Date(this.state.profileShow.profile.givenAt)) ||
                                            new Date()}
                                        minDate={(this.state.profileShow.x != null && new Date(this.state.profileShow.profile.givenAt)) ||
                                            new Date()}
                                        selected={(this.state.profileShow.x != null && new Date(this.state.profileShow.profile.givenAt)) ||
                                            new Date()}
                                        onChange={date => {
                                            this.onDateGivenChange(date);
                                        }}
                                    />
                                     
                    </div>
                    </div>
                            <div className="inline m-0 px-5">
                        <h6 >date returned </h6>
                                <div>
                                    <DatePicker
                                        className="form-control inline-2-5 p-2"
                                        dateFormat="dd MMMM yyyy"
                                        locale="en-GB"
                                        disabled
                                        selected={(this.state.profileShow.x != null && new Date(this.state.profileShow.profile.returnedAt)) || null}
                                    />
                            
                        </div>
                            </div>
                            {
                                this.state.profileShow.x == null &&
                                    <>
                                <div className="mt-5 d-flex align-items-center" >
                                <button className="btn btn-mybtn p-x-5 mt-0" onClick={this.saveUpdated}>Save</button>
                                <button className="btn btn-mybtn btn-delete px-5 mt-0" onClick={() => this.props.updateClicked(null)}>Cancel</button>
                            </div>                        
                                    <p style={{ fontSize: "80%" }}> ps : changing date will change previous records as well and may cause lose of data </p>
                                    </>
                                    }
                        
                        </div> {/*************** form right over  **************/}

                        <div className="inline w60">{/*************** form left **************/}
                           
                            <h6 className="required-field ml-0">selected inventory </h6>
                            {this.state.profileShow.x == null && this.state.withUser && this.state.selectedPds &&
                               this.state.selectedPds.map((pd, i) =>
                                <div>
                                    <label className="required-field">{i + 1}&nbsp;</label>
                                    <input value={pd.name} className='form-control inline w80 m-0 ml-1 p-2 pl-3' />

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
                                />{this.state.changedPd && <i class="fa fa-undo ml-1" onClick={() => { this.returnOriginalPd()}}>
                                </i>}
                                {this.state.profileShow.x==null &&
                                    <p style={{ fontSize: "70%" }}> ps : selecting inventory from filter will change current inventory </p>}                                
                                <h6 className="mt-1 ml-0 required-field">select user </h6>
                                <CustomSelect
                                    className={"b-r inline w80 m-0 p-0"}
                                    items={this.props.usersToGive}
                                    value={this.state.profileShow.profile.userId}
                                    onChange={(selected) => this.onUserChange(selected)}
                                />{this.state.changedUser && <i class="fa fa-undo ml-1" onClick={() => { this.returnOriginalUser() }}>
                                </i>}
                                </>
                            }
                            
                        </div>{/**********  form left over **********************/}
                    </div>{/*************** form over ********************/}

                    {this.state.profileShow.x != null &&
                        <>
                        {this.state.profileShow.profile.profileDetail != null &&
                            <div className="mt-3 px-5">
                                <h6>owings :</h6>
                                <div className="d-flex alighn-items-top">
                                    <div className="w20">
                                        <p>created at : </p><p>{this.state.profileShow.profile.profileDetail.createdAt}</p>
                                    </div>
                                    <div className="w20">
                                        <p>modified at : </p><p>{this.state.profileShow.profile.profileDetail.modifiedAt}</p>
                                    </div>
                                    <div className="w20">
                                        <p>owed amount : </p><p>{new Intl.NumberFormat("en-GB", {
                                            style: "currency",
                                            currency: "BGN",
                                            maximumFractionDigits: 2
                                        }).format(this.state.profileShow.profile.profileDetail.owedAmount)}</p>
                                    </div>
                                    <div className="w20">
                                        <p>paid amount : </p><p>{new Intl.NumberFormat("en-GB", {
                                            style: "currency",
                                            currency: "BGN",
                                            maximumFractionDigits: 2
                                        }).format(this.state.profileShow.profile.profileDetail.paidAmount)}</p>
                                </div>
                                {!this.state.profileShow.profile.profileDetail.cleared &&
                                    <div className="w20 d-flex alighn-items-top">
                                    <p>paid plus : </p>
                                    <input className="inline m-0 ml-2 p-1 form-control px100"
                                        min="0"
                                        max={this.state.profileShow.profile.profileDetail.owedAmount - this.state.profileShow.profile.profileDetail.paidAmount}
                                        value={this.state.profileShow.profile.paidPlus}
                                        onChange={(value) => {
                                            let amount = value.target.value;
                                            let owed = (this.state.profileShow.profile.profileDetail.owedAmount - this.state.profileShow.profile.profileDetail.paidAmount).toFixed(2);
                                           //debugger
                                            if (Number(amount) > Number(owed))
                                                amount = owed
                                            else if (amount <= 0) amount = "";
                                                
                                            let profileShow = this.state.profileShow;
                                            profileShow.profile.paidPlus = amount;
                                            this.setState({ profileShow: profileShow })
                                            }}
                                            type="number" />
                                    </div>
                                }
                                {this.state.profileShow.profile.profileDetail.cleared &&

                                    <div className="wxs">
                                        <p className="p-0">cleared :</p>
                                        <i class="fa fa-check ml-1" />
                                    </div>}
                                {this.state.profileShow.profile.profileDetail.cleared &&
                                       <button className="button btn-delete m-1 ml-3" onClick={() => {
                                        let show = this.state.profileShow;
                                        show.profile.paidPlus = null;
                                        show.profile.profileDetail = null;
                                        this.setState({
                                            profileShow: show,
                                            rememberToSave:true})
                                          }}>delete owings
                                        </button>
                                }                                
                                </div>
                            </div>}
                      
                            <div className="mt-3" >
                                <button className="btn btn-mybtn p-x-5 m-0 ml-5" onClick={this.saveUpdated}>Save</button>
                            <button className="btn btn-mybtn btn-delete px-5 m-0 ml-5 mr-5" onClick={() => this.props.updateClicked(null)}>Cancel</button>
                            {this.state.rememberToSave && <p className="inline ml-5" style={{ fontSize: "70%" }}>
                                ps : don't forget to save otherwise your changes won't be affected </p>}
                        </div>
                        <p style={{ fontSize: "80%" }}> ps : changing date will change previous records as well and may cause lose of data </p>                           
                        </>}                   
                    </div>
            </>
        )
    }
}

export default UserProfileInnerComponent