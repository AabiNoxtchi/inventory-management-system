import React, { Component } from 'react';
import PendingUserDataService from '../service/PendingUserDataService';
import PaginationComponent from './PaginationComponent';
import PendingUserFilter from './Filters/PendingUserFilter';
import '../myStyles/Style.css';

import Function from './Shared/Function';
import CityInnerComponent from './CityInnerComponent';
import CustomSelect from './Filters/CustomSelect';
import { Link, Route, withRouter } from 'react-router-dom'



class ListPendingUsersComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],
            message: null,
            pager: null,
            filter: {},
            search: window.location.search || '',
            alldata: [],
            cityUpdateShow: {
                city: {}, show: false
            },
            updatingItemId: null,
            filterkey: 0
        }
        this.refresh = this.refresh.bind(this)
        this.deleteClicked = this.deleteClicked.bind(this)
        this.updateClicked = this.updateClicked.bind(this)
        this.saveClicked = this.saveClicked.bind(this)
        this.searchLink = React.createRef();       
    }

    componentDidMount() {
        this.refresh();
    }

    componentDidUpdate() {       
        if (this.props.location.search.indexOf("refresh") > -1) {           
            this.searchLink.current.click();
            this.refresh();
        }     
    }

    refresh(newsearch) {
        if (!newsearch) newsearch = this.state.search;
        PendingUserDataService.retrieveAll(newsearch)
            .then(
                response => {
                    this.setState({
                        items: response.data.items || response.data.daoitems,
                        pager: response.data.pager,
                        filter: this.getfilter(response.data.filter),
                        filterkey : this.state.filterkey + 1
                    });
                }
            ).catch((error) => {
                let msg = Function.getErrorMsg(error);
                this.showError(msg, 5);               
            })
    }

    getfilter(newfilter) {
        if (!newfilter.filtersSet)
            return newfilter;
        else {
            newfilter.countries = this.state.filter.countries;
            return newfilter;
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

    deleteClicked(id) {
        PendingUserDataService.delete(id)
            .then(
                () => {
                    this.setState({ message: `Delete successful` })
                    this.refresh()
                }
        ).catch(error => {
            let msg = Function.getErrorMsg(error);
            this.showError(msg, 5);
        })
    }

    updateClicked(id, countryId, newCity) {
        this.setState({ updatingItemId: id });
        this.updateClickedInnerChild({ "countryId": `${countryId}`, "name": `${newCity}` })
    }

    updateItemCityId(cityId, city) {
        let filter = this.state.filter;
        let cities = filter.cities;
        cities.push({ "value": cityId, "name": city.name, "filterBy": city.countryId })
        this.setState({filter: filter})
    }

    setItemCity(selected, itemx) {
        let id = itemx.id;
        let list = this.state.items;
        let item = list.find(x => x.id == id);
        if (item == null) return;
        item.cityId = selected.value;
        item.newCity = selected.label;
        this.setState({ items: list })
    }

    updateClickedInnerChild = (item) => {
        item = item || {};
        let show = this.state.cityUpdateShow;
        show.show = !show.show;
        if (show.show == true) {
            show.city = JSON.parse(JSON.stringify(item))

        } else {
            show.error = ''
        }
        this.setState({
            cityUpdateShow: show,
            message: null,
        });
    }

    saveClicked(item) {
        PendingUserDataService.save(item)
            .then(
                () => {
                    this.setState({ message: `Save successful` })
                    this.refresh()
                }
        ).catch(error => {
            let msg = Function.getErrorMsg(error);
            this.showError(msg, 5);   
        })
    }    

    togglemsgbox = () => {
        this.setState({ message: null })
    }

    togglecitybox(i){
        this.setState({
            showcitybox: !this.state.showcitybox,
            showcityboxi: i
        })
    }

    getFilteredList(value) {
        let subs = [];
        let names = this.state.filter.cities;       
        for (let i = 0; i < names.length; i++) {
            if (names[i].filterBy == value || names[i].value == '') {
                subs.push(names[i])
            }
        }

        return subs
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
       
        return (
            <div className="px-3 pt-3">

                <Link ref={this.searchLink} to={`${url}${this.state.search}`}></Link>
                <Route path={`${url}/:search`}>
                    <p></p>
                </Route>
                {this.state.cityUpdateShow && this.state.cityUpdateShow.show == true &&
                    <CityInnerComponent
                    cityUpdateShow={this.state.cityUpdateShow}
                    updateClickedInnerChild={() => this.updateClickedInnerChild(null)}
                    setMessage={(value) => this.setState({ message: value })}
                    updatedCity={(id, updated) => this.updateItemCityId(id, updated)}
                    />}
                {this.state.filter && <PendingUserFilter {...this.state.filter}
                    key={this.state.filterkey}
                    onNewSearch={(search) =>
                        this.updateSearch(search)
                    }/>}
                <div className="border">
                    <div className="panel-heading">
                        <h5 className="panel-title p-2 pb-3">
                            <strong> Pending Requests</strong>
                        </h5>
                    </div>
                    <div className="p-1">
                        <div className=" pt-3 px-2 mx-3 d-flex flex-wrap ">                           
                            {this.state.pager && <PaginationComponent {...this.state.pager}
                                search={this.state.search}
                                onNewSearch={(search) =>
                                    this.updateSearch(search)
                                }/>}
                        </div>
                        {this.state.message && <div className="alert alert-success d-flex">{this.state.message}<i class="fa fa-close ml-auto pr-3 pt-1"
                            onClick={this.togglemsgbox}></i></div>}
                        {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                        <table className="table border-bottom my-table">
                            <thead>
                                <tr>                                  
                                    <th>user name</th>
                                    <th>email</th>
                                    <th>country</th>
                                    <th>city</th>                                   
                                    <th>Update &emsp;&nbsp; Delete</th>
                                </tr>
                            </thead>
                            <tbody>
                                {this.state.items.map(
                                        (item, i) =>
                                            <tr key={item.id}>                                                
                                                <td>{item.username}</td>
                                                <td>{item.email}</td>
                                                <td>{this.state.filter.countries.find(x => x.value == item.countryId).name}</td>
                                                <td className="w20">
                                                    <>
                                                        <div className="">
                                                    {item.newCity}
                                                            <i class="fa fa-caret-down ml-3 pr-3 pt-1" onClick={() => this.togglecitybox(i)}></i>
                                                        </div>
                                                        {this.state.showcitybox && this.state.showcityboxi == i &&
                                                            <div className="above-row hidden-select-control">
                                                            <div className="above-select">
                                                                <CustomSelect
                                                                    className="inline-3"
                                                                    items={this.state.filter.cities && this.getFilteredList(item.countryId)}
                                                                    value={item.cityId}
                                                                    defaultMenuIsOpen={true}
                                                                    onChange={(selected) => {
                                                                        if (selected.value != 'undefined' && selected.value)
                                                                            this.setItemCity(selected, item);                                                                            
                                                                    }}
                                                                />
                                                            </div>
                                                            </div>}
                                                        </>
                                                </td>                                               
                                                <td>
                                                    {item.cityId == null &&
                                                        <button className="btn btn-mybtn mr-1" onClick={() => this.updateClicked(item.id, item.countryId, item.newCity)}>Add</button>}
                                                    {item.cityId != null &&
                                                        <button className="btn btn-mybtn mr-1" onClick={() => this.saveClicked(item)}>Save</button>}
                                                    <button className="btn btn-mybtn btn-delete" onClick={() => this.deleteClicked(item.id)}>Delete</button></td>
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

export default ListPendingUsersComponent