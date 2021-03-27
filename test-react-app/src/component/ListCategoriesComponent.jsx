import React, { Component } from 'react';
import CategoryDataService from '../service/CategoryDataService';
import PaginationComponent from './PaginationComponent';
import CategoryFilter from './Filters/CategoryFilter';
import '../myStyles/Style.css';
import CategoryInnerComponent from './CategoryInnerComponent'

class ListCategoriesComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],
            pager: null,
            filter: null,
            search: window.location.search || '',
            message: null,
            categoryUpdateShow: {
                category: {}, show: false
            },


        }
        this.refresh = this.refresh.bind(this)
        this.deleteClicked = this.deleteClicked.bind(this)      
        
    }

    componentDidMount() {       
        this.refresh();
    }

    refresh() {        
        CategoryDataService.retrieveAll(this.state.search)
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

   
    updateClickedInner = (item) => {
        /* let messages = this.state.message;
         if (messages != null )
             messages = null;*/
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
                msg = msg.indexOf("ConstraintViolationException")>-1 ? "can't delete item with associated records !!!" : msg
                this.setState({
                    errormsg: msg
                })
            })

    }
    togglemsgbox = () => {
        this.setState({ message: null })
    }


    render() {

        return (
            <div className="px-3">
                {this.state.categoryUpdateShow && this.state.categoryUpdateShow.show == true &&
                    <CategoryInnerComponent
                        categoryUpdateShow={this.state.categoryUpdateShow}
                        updateClickedInner={() => this.updateClickedInner(null)}
                        setMessage={(value) => this.setState({ message: value})}
                        refresh={() => this.refresh()}
                    />}

                {this.state.errormsg && <div className="alert alert-warning">{this.state.errormsg}</div>}
                {this.state.filter && <CategoryFilter {...this.state.filter} />}

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
                                   
                                    <th scope="col" >type</th>
                                    <th scope="col">name</th>
                                    <th scope="col">Update &emsp;&nbsp; Delete</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    this.state.items.map(
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