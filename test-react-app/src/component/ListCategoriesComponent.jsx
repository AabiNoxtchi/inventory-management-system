import React, { Component } from 'react';
import CategoryDataService from '../service/CategoryDataService';
import PaginationComponent from './PaginationComponent';
import CategoryFilter from './Filters/CategoryFilter';
import '../myStyles/Style.css';
import CategoryInnerComponent from './CategoryInnerComponent';
import { Link, Route, withRouter } from 'react-router-dom';
import Function from './Shared/Function';

class ListCategoriesComponent extends Component {
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
        CategoryDataService.retrieveAll(newSearch)
            .then(
                response => {
                    this.setState({
                        items: response.data.items || response.data.daoitems,
                        pager: response.data.pager,
                        filter: this.getfilter(response.data.filter),
                        filterKey: this.state.filterKey + 1
                    });
            }).catch((error) => {
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
            newfilter.names = filter.names;
            newfilter.productTypes = filter.productTypes;            
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

   
    updateClickedInner = (item) => {
      
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
        CategoryDataService.delete(id)
            .then(() => {
                this.setState({
                    message: `Delete successful`,
                })
                this.refresh()
            }).catch(error => {

                let msg = Function.getErrorMsg(error);
                this.showError(msg, 5);
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
                    <CategoryInnerComponent
                        categoryUpdateShow={this.state.categoryUpdateShow}
                        updateClickedInner={() => this.updateClickedInner(null)}
                        setMessage={(value) => this.setState({ message: value})}
                        refresh={() => this.refresh()}
                    />}
                {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                {this.state.filter && <CategoryFilter {...this.state.filter}
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
                        {this.state.message &&
                            <div className="alert alert-success d-flex">{this.state.message}
                                <i class="fa fa-close ml-auto pr-3 pt-1" onClick={this.togglemsgbox}></i>
                            </div>
                        }
                        <table className="table border-bottom my-table" >
                            <thead>
                                <tr>                                   
                                    <th scope="col" >type</th>
                                    <th scope="col">name</th>
                                    <th scope="col">Update &emsp;&nbsp; Delete</th>
                                </tr>
                            </thead>
                            <tbody>
                                {this.state.items.map(
                                        item =>
                                            <tr>
                                                <td>
                                                    {item.productType}
                                                </td>
                                                <td>{item.name}</td>
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

export default ListCategoriesComponent