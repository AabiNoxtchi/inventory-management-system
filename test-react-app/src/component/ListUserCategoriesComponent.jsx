import React, { Component } from 'react';
import UserCategoryDataService from '../service/UserCategoryDataService';
import PaginationComponent from './PaginationComponent';
import UserCategoryFilter from './Filters/UserCategoryFilter';
import '../myStyles/Style.css';
import UserCategoryInnerComponent from './UserCategoryInnerComponent';
import Function from './Shared/Function';
import { Link, Route, withRouter } from 'react-router-dom';

class ListUserCategoriesComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],
            pager: null,
            filter: {},
            filterKey: 0,
            search: window.location.search || '',
            message: null,
            categoryUpdateShow: {
                category: {}, show: false
            },


        }
        this.refresh = this.refresh.bind(this)
        this.deleteClicked = this.deleteClicked.bind(this)
        this.searchLink = React.createRef();

    }

    componentDidMount() {
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
        UserCategoryDataService.retrieveAll(newSearch)
            .then(
                response => {
                    //console.log("response = " + JSON.stringify(response));
                    this.setState({
                        items: response.data.items || response.data.daoitems,
                        pager: response.data.pager,
                        filter: this.getfilter(response.data.filter),
                        filterKey: this.state.filterKey + 1
                    });
            }).catch((error) => {
                let msg = Function.getErrorMsg(error);
                this.showError(msg)   
                   /// this.setState({
                    //    errormsg: '' + error == 'Error: Request failed with status code 401' ? 'need to login again !!!' : '' + error
                    //})
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
            newfilter.names = filter.names;
            newfilter.productTypes = filter.productTypes;
            return newfilter
        }
    }

    showError(msg) {
        let time = 10;
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


    updateClickedInner = (item) => {
        /* let messages = this.state.message;
         if (messages != null )
             messages = null;*/
        console.log("item = " + JSON.stringify(item))
        item = item || {};
        let show = this.state.categoryUpdateShow;
        show.show = !show.show;
        if (show.show == true) {

            show.category = JSON.parse(JSON.stringify(item))

        } else {
            show.error = ''
        }
        this.setState({
            categoryUpdateShow: show,
            message: null,
        });
    }
    deleteClicked(id) {
        UserCategoryDataService.delete(id)
            .then(response => {
                this.setState({
                    message: `Delete successful`,
                })
                this.refresh()
            }).catch(error => {
                let errormsg = error.response && error.response.data ?
                    error.response.data.message ? error.response.data.message : error.response.data : error + '';

                //let errormessage = this.state.dderrormessage;
                let msg = '' + error == 'Error: Request failed with status code 401' ? 'need to login again !!!' : '' + errormsg
                msg = msg.indexOf("ConstraintViolationException") > -1 ? "can't delete item with associated records !!!" : msg
                this.setState({
                    errormsg: msg
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
                {this.state.categoryUpdateShow && this.state.categoryUpdateShow.show == true &&
                    <UserCategoryInnerComponent
                        categoryUpdateShow={this.state.categoryUpdateShow}
                        updateClickedInner={() => this.updateClickedInner(null)}
                        setMessage={(value) => this.setState({ message: value })}
                        refresh={() => this.refresh()}
                    />}

                
                {this.state.filter && <UserCategoryFilter {...this.state.filter}
                    key={this.state.filterKey}
                    onNewSearch={(search) =>
                        this.updateSearch(search)
                    }/>}

                <div className="border">
                    <div className="panel-heading">
                        <h5 className="panel-title p-2 d-inline-flex">
                            <strong> Categories</strong>
                        </h5>

                    </div>
                    <div className="p-1">
                        <div className=" pt-3 px-2 mx-3 d-flex flex-wrap">
                            <div>
                                <button className="btn btn-mybtn px-5  " onClick={() => this.updateClickedInner({})}>Add New</button>

                            </div>
                            {this.state.pager && <PaginationComponent {...this.state.pager}                               
                                onNewSearch={(search) =>
                                    this.updateSearch(search)
                                }/>}
                        </div>
                        {this.state.errormsg && <div className="alert alert-warning d-flex">{this.state.errormsg}
                            <i class="fa fa-close ml-auto pr-3 pt-1" onClick={() => this.setState({ errormsg: null })}></i></div>}
                        {
                            this.state.message &&
                            <div className="alert alert-success d-flex">{this.state.message}
                                <i class="fa fa-close ml-auto pr-3 pt-1" onClick={this.togglemsgbox}></i>
                            </div>
                        }

                        <table className="table border-bottom my-table" >
                            <thead>
                                <tr>

                                    <th scope="col" >type</th>
                                    <th scope="col">name</th>
                                    <th scope="col">amortization</th>
                                    <th scope="col">Update &emsp;&nbsp; Delete</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.items.map(
                                        item =>
                                            <tr>
                                                <td>
                                                    {item.category.productType}
                                                </td>
                                                <td>{item.category.name}</td>
                                                <td>{item.category.productType == 'LTA' ? item.amortizationPercent + '%' : '-'}</td>

                                                <td><button className="btn btn-mybtn mr-1" onClick={() => this.updateClickedInner(item)}>Update</button>
                                                    <button className="btn btn-mybtn btn-delete" onClick={() => this.deleteClicked(item.id)}>Delete</button>
                                                </td>
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

export default ListUserCategoriesComponent