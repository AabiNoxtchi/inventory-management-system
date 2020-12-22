import React, { Component } from 'react';
import UserDataService from '../service/UserDataService';
import PaginationComponent from './PaginationComponent';
import UserFilter from './Filters/UserFilter'

class ListUsersComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            items: [],
            message: null,
            pager: null,
            filter: null,
            search: window.location.search//this.props.match.params.search
        }
        this.refresh = this.refresh.bind(this)
        this.deleteClicked = this.deleteClicked.bind(this)
        this.updateClicked = this.updateClicked.bind(this)
        this.addClicked = this.addClicked.bind(this)
    }

    componentDidMount() {
        this.refresh();
    }

    refresh() {
        console.log('state.search = ' + this.state.search);
        if (this.state.search === undefined) {
            this.setState({search : ''})
        }
        UserDataService.retrieveAll(this.state.search)
            .then(
                response => {
                    console.log(' listUsers : pager.pagescount = ' + response.data.pager.pagesCount);
                    console.log('listUsers : pager.itemsPerPage = ' + response.data.pager.itemsPerPage);
                    this.setState({
                        items: response.data.items,
                        pager: response.data.pager,
                        filter: response.data.filter
                    });
                   
                    
                }
            )
    }

    deleteClicked(id) {
       UserDataService.delete(id)
            .then(
                response => {
                    this.setState({ message: `Delete successful` })
                    this.refresh()
                }
            )

    }

    updateClicked(id) {
        console.log('update ' + id)
        this.props.history.push(`/users/${id}`)
    }

    addClicked() {
        this.props.history.push(`/users/-1`)
    }

    render() {
        return (
            <div className="px-3 pt-2">
            <div className="pb-3">

                {this.state.filter && <UserFilter {...this.state.filter} />}
                <div className="pt-3">
                    <h3 className="pl-5">All Users</h3>
                </div>
               
                <div className="p-3">
                <div className="p-3 border">

                   
                    {this.state.message && <div class="alert alert-success">{this.state.message}</div>}
                   
                        <table className="table border-bottom" style={{ width: '100%' }}>
                        <thead>
                            <tr>
                                <th scope="col">first name</th>
                                <th scope="col">last name</th>
                                <th scope="col">user name</th>
                                <th scope="col">email</th>
                                    <th scope="col" style={{ width: '9%' }}></th>
                                    <th scope="col" style={{ width: '9%' }}></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.items.map(
                                    item =>
                                        <tr scope="row" key={item.id}>
                                            <td>{item.firstName}</td>
                                            <td>{item.lastName}</td>
                                            <td>{item.userName}</td>
                                            <td>{item.email}</td>
                                            <td><button className="btn btn-success" onClick={() => this.updateClicked(item.id)}>Update</button></td>
                                            <td><button className="btn btn-warning" onClick={() => this.deleteClicked(item.id)}>Delete</button></td>
                                        </tr>
                                )
                            }
                        </tbody>

                        </table>
                        <div className="row pt-3 px-2 mx-3">
                            <div className="col-2">
                                <button className="btn btn-success px-5 " onClick={this.addClicked}>Add New</button>
                            </div>
                            <div className="col-10">
                                {this.state.pager && <PaginationComponent {...this.state.pager} />}
                            </div>
                        </div>
                </div>
                

               </div>

                </div>
            </div>
        )
    }
}

export default ListUsersComponent