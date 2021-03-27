import React, { Component } from 'react';
import CountryDataService from '../service/CountryDataService';
import PaginationComponent from './PaginationComponent';
import CityFilter from './Filters/CityFilter';
import '../myStyles/Style.css';

class ListCitiesComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],
            pager: null,
            filter: null,
            search: window.location.search || '',           
            message: null,
           

        }
        this.refresh = this.refresh.bind(this)
        this.deleteClicked = this.deleteClicked.bind(this)
        // this.deleteChildClicked = this.deleteChildClicked.bind(this)
        this.updateClicked = this.updateClicked.bind(this)
        this.addClicked = this.addClicked.bind(this)
        this.csvLink = React.createRef();
    }

    componentDidMount() {
        console.log("version = " + React.version);

        this.refresh();
    }

    refresh() {
        console.log("search = " + this.state.search);
        CountryDataService.retrieveAll(this.state.search)
            .then(
                response => {
                    console.log("response = " + JSON.stringify(response));
                    this.setState({
                        items: response.data.items || response.data.daoitems,
                        pager: response.data.pager,
                        filter: response.data.filter,
                       
                    });
                }).catch((error) => {
                    this.setState({
                        errormsg: '' + error == 'Error: Request failed with status code 401' ? 'need to login again !!!' : '' + error
                    })
                })
    }

    addClicked() {
        
    }
    updateClicked(id) {
        
    }
    deleteClicked(id) {
       
    }
    togglemsgbox = () => {
        this.setState({ message: null })
    }


    render() {
      
        return (
            <div className="px-3">

                {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                {this.state.filter && <CityFilter {...this.state.filter} />}
               
                <div className="border">
                    <div className="panel-heading">
                        <h5 className="panel-title p-2 d-inline-flex">
                            <strong> Countries</strong>
                        </h5>
                       
                    </div>
                    <div className="p-1">
                        <div className=" pt-3 px-2 mx-3 d-flex flex-wrap">
                            <div>
                                <button className="btn btn-mybtn px-5  " onClick={this.addClicked}>Add New</button>
                                
                            </div>
                            {this.state.pager && <PaginationComponent {...this.state.pager} />}
                        </div>
                        {
                            this.state.message &&
                            <div className="alert alert-success d-flex">{this.state.message}
                                <i class="fa fa-close ml-auto pr-3 pt-1" onClick={this.togglemsgbox}></i>
                            </div>
                        }
                       
                            <table className="table border-bottom my-table" >
                                <thead>
                                    <tr>
                                        <th scope="col">name</th>
                                        <th scope="col" >zone</th>
                                        <th scope="col" >country</th>
                                        <th >currency</th>
                                        <th scope="col">Update &emsp;&nbsp; Delete</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {
                                        this.state.items.map(
                                            item =>
                                                <tr>
                                                    <td>{item.name}</td>
                                                    <td>
                                                        {item.timeZone}
                                                    </td>
                                                    <td >{item.country.name}</td>
                                                    <td >{item.country.currency}</td>
                                                    <td><button className="btn btn-mybtn mr-1" onClick={() => this.updateClicked(item.id)}>Update</button>
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

export default ListCitiesComponent