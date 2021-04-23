import React, { Component } from 'react';
import CountryDataService from '../service/CountryDataService';
import PaginationComponent from './PaginationComponent';
import CityFilter from './Filters/CityFilter';
import '../myStyles/Style.css';
import CountryInnerComponent from './CountryInnerComponent';
import CityInnerComponent from './CityInnerComponent';
import { Link, Route, withRouter } from 'react-router-dom';
import Function from './Shared/Function';


class ListCountriesComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],
            pager: null,
            filter: {},
            filterKey: 0,
            search: window.location.search || '',
            message: null,
            countryUpdateShow: {
                country: {}, show: false
            },
            cityUpdateShow: {
                city: {}, show: false
            },
        }
        this.refresh = this.refresh.bind(this)
        this.deleteClicked = this.deleteClicked.bind(this)        
       // this.updateClicked = this.updateClicked.bind(this)
        this.addClicked = this.addClicked.bind(this)
        this.csvLink = React.createRef();
        this.searchLink = React.createRef();
    }

    componentDidMount() {
        console.log("version = " + React.version);

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
        CountryDataService.retrieveAll(newSearch)
            .then(
                response => {
                    console.log("response = " + JSON.stringify(response));
                    this.setState({
                        items: response.data.items || response.data.daoitems,
                        pager: response.data.pager,
                        filter: this.getfilter(response.data.filter),
                        filterKey: this.state.filterKey + 1

                    });
                }).catch((error) => {
                   /* this.setState({
                        errormsg: '' + error == 'Error: Request failed with status code 401' ? 'need to login again !!!' : '' + error
                    })*/
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
            newfilter.countries = filter.countries;
            newfilter.cities = filter.cities;
            newfilter.zones = filter.zones;
            newfilter.currencies = filter.currencies;
            return newfilter
        }
    }

    showError(msg, time) {
        // let time = 10;
        time = time ? time : 10;
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

    addClicked() {

    }
    updateClickedInner = (item) => {
        /* let messages = this.state.message;
         if (messages != null )
             messages = null;*/
        item = item || {};
        let show = this.state.countryUpdateShow;
        show.show = !show.show;
        if (show.show == true) {
           
            show.country = JSON.parse(JSON.stringify(item))
           
        } else {
            show.error = ''
        }
        this.setState({
            countryUpdateShow: show,
            message: null,
            pdmessage: [],
            ddmessage: []
        });
    }
    updateClickedInnerChild = (item) => {
        /* let messages = this.state.message;
         if (messages != null )
             messages = null;*/
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
    deleteClicked(id) {
        CountryDataService.delete(id)
            .then(response => {                    
                    this.setState({
                        message: `Delete successful`,                       
                    })
                    this.refresh()
            }).catch(error => {
               
            this.setState({
                        errormsg: "can't delete item with associated records !!!" 
                    })
            })
    }
    deleteChildClicked(id) {
        CountryDataService.deleteChild(id)
            .then(response => {
                this.setState({
                    message: `Delete successful`,
                })
                this.refresh()
            }).catch(error => {
                this.setState({
                    errormsg: "can't delete item  with associated records !!!"
                })
            })
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

        return (
            <div className="px-3 pt-3">

                <Link ref={this.searchLink} to={`${url}${this.state.search}`}></Link>
                <Route path={`${url}/:search`}>
                    <p></p>
                </Route>
                {this.state.countryUpdateShow && this.state.countryUpdateShow.show == true &&
                    <CountryInnerComponent
                    countryUpdateShow={this.state.countryUpdateShow}
                    updateClickedInner={() => this.updateClickedInner(null)}
                    setMessage={(value) => this.setState({ message: value })}
                    refresh={()=>this.refresh()}
                    
                    />}
                {this.state.cityUpdateShow && this.state.cityUpdateShow.show == true &&
                    <CityInnerComponent
                       cityUpdateShow={this.state.cityUpdateShow}
                        updateClickedInnerChild={() => this.updateClickedInnerChild(null)}
                        setMessage={(value) => this.setState({ message: value })}
                        refresh={() => this.refresh()}
                    />}
                {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                {this.state.filter && <CityFilter {...this.state.filter}
                    key={this.state.filterKey}
                    onNewSearch={(search) =>
                        this.updateSearch(search)
                    }/>}

                <div className="border">
                    <div className="panel-heading">
                        <h5 className="panel-title p-2 d-inline-flex">
                            <strong> Countries</strong>
                        </h5>

                    </div>
                    <div className="p-1">
                        <div className=" pt-3 px-2 mx-3 d-flex flex-wrap">
                            <div>
                                <button className="btn btn-mybtn px-5  " onClick={()=>this.updateClickedInner({})}>Add New</button>

                            </div>
                            {this.state.pager && <PaginationComponent {...this.state.pager}                              
                                onNewSearch={(search) =>
                                    this.updateSearch(search)
                                }/>}
                        </div>
                        {
                            this.state.message &&
                            <div className="alert alert-success d-flex">{this.state.message}
                                <i class="fa fa-close ml-auto pr-3 pt-1" onClick={this.togglemsgbox}></i>
                            </div>
                        }

                        {this.state.items.map(
                                         (item) =>
                                            <div className="panel-body">
                            <table className="table border table-s">
                                <tbody>
                                                         <tr >
                                                             <th>code : {item.code}</th>
                                        <th className="wl pl-5"
                                            > Name : {item.name} </th>
                                        <th className="wl pl-3"                                           
                                                             > Currency : {item.currency}</th>
                                                             <th>phone code : {item.phoneCode}</th>
                                        <th className="d-flex justify-content-end mr-1">
                                                                 <button className="btn btn-mybtn mr-1" onClick={() => this.updateClickedInner(item)}>Update</button>
                                            <button className="btn btn-mybtn btn-delete" onClick={() => this.deleteClicked(item.id)}>Delete</button>
                                        </th>
                                    </tr>
                                    <tr>
                                        <td colspan="3">
                                                                 <table className="ml-5 mb-3 x-Table" style={{ width: '60%' }}>
                                                <tr><td>city</td>
                                                    <td className="pl-5">time zone</td>
                                                                         <td style={{ width: '173px', padding: '.2rem .5rem' }}>
                                                        <button className="btn btn-mybtn pull-right" style={{ padding: '.15rem .6rem' }}
                                                                                 onClick={() => this.updateClickedInnerChild({ "countryId": `${item.id}`})}>add one</button>
                                                    </td></tr>
                                                {item.cities&&item.cities.map((c, i) =>
                                                    <tr>
                                                        <td >{c.name}</td>
                                                        <td className="pl-5 pb-3"
                                                            > {c.timeZone}
                                                        </td>
                                                        <td><button className="btn btn-mybtn mr-1" onClick={() => {
                                                            this.updateClickedInnerChild(c)
                                                        }}>Update</button>
                                                            <button className="btn btn-mybtn btn-delete"
                                                                onClick={() => this.deleteChildClicked(c)}>
                                                                Delete</button></td>
                                                    </tr>
                                                )}
                                            </table>
                                        </td>
                                    </tr>
                                </tbody>
                                                 </table>
                                             </div>
                                                 )}
                    </div>
                </div>
            </div>
        )
    }
}

export default ListCountriesComponent