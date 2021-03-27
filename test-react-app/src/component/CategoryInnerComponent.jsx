import React, { Component } from 'react';
import CategoryDataService from '../service/CategoryDataService';
import '../myStyles/Style.css';
import CustomSelect from './Filters/CustomSelect';

class CategoryInnerComponent extends Component {
    constructor(props) {
        super(props)
        this.state =
            {
            categoryUpdateShow: props.categoryUpdateShow,
           
            }
    }

    saveUpdated = () => {
        let item = this.state.categoryUpdateShow.category;
        item.name = item.name?item.name.trim():item.name;
        if (!item.name || item.name == "undefined" || !item.productType) {
            let show = this.state.categoryUpdateShow;
            show.error = "All fields are required";
            this.setState({ categoryUpdateShow: show })
        } else {
            CategoryDataService.save(item)
                .then(response => {
                    let msg = this.state.categoryUpdateShow.category.id && this.state.categoryUpdateShow.category.id > 0 ? `Update successful` : `Save successful`;
                    this.props.updateClickedInner(null);
                    this.props.setMessage(msg);
                    this.props.refresh();
                }).catch(error => {
                    let errormsg = error.response && error.response.data ?
                        error.response.data.message ? error.response.data.message : error.response.data : error + '';
                    let show = this.state.categoryUpdateShow;
                    show.error = errormsg;
                    this.setState({ categoryUpdateShow: show })
                })
        }
       

    }

    onProductTypeChange(value) {
        let c = this.state.categoryUpdateShow;
        if (c.category.productType === value.target.value) return;
        c.category.productType = value.target.value;
        this.setState({
            categoryUpdateShow: c
        })
    }
    onNameChange(value) {
        let c = this.state.categoryUpdateShow;
        c.category.name = value.target.value;
        this.setState({
            categoryUpdateShow: c
        })
    }

    render() {
        return (
            <>
                <div className={this.state.categoryUpdateShow.show ? "overlay d-block" : "d-none"}></div>
                <div className={this.state.categoryUpdateShow.show ? "modal d-block" : "d-none"} style={{ width: "40%", height: "60%" }}>
                    <span class="close" onClick={() => this.props.updateClickedInner(null)}>&times;</span>
                    <h2>{this.state.categoryUpdateShow.category.id && this.state.categoryUpdateShow.category.id > 0 ? "update" : "add"} category</h2>
                    {this.state.categoryUpdateShow.error && this.state.categoryUpdateShow.error.length > 1 &&
                        <div className="alert alert-warning d-flex">{this.state.categoryUpdateShow.error}
                            <i class="fa fa-close ml-auto pr-3 pt-1"
                                onClick={() => {
                                    let show = this.state.categoryUpdateShow;
                                    show.error = '';
                                    this.setState({ categoryUpdateShow: show })
                                }}>
                            </i>
                        </div>}

                   
                    <h6 className={this.state.categoryUpdateShow.error && this.state.categoryUpdateShow.error.length > 1 ?
                        "ml-5" : "mt-5 ml-5"}>name :</h6>
                    <input type="text" className="form-control" value={this.state.categoryUpdateShow.category.name }
                        onChange={(value) => {
                            this.onNameChange(value)
                        }} />
                    <div className="pr-2 mr-2 mt-3">
                        <h6 className="px-5">product type :</h6>
                        <input
                            className="" type="radio"
                            value='LTA' checked={this.state.categoryUpdateShow.category.productType == 'LTA'}
                            onChange={(value) => {
                                this.onProductTypeChange(value)
                            }}
                        /><span className=" pl-1" >LTA</span>
                        <input
                            className="" type="radio"
                            value='STA' checked={this.state.categoryUpdateShow.category.productType == 'STA'}
                            onChange={(value) => {
                                this.onProductTypeChange(value)
                            }}
                        /><span className="pl-1" >STA</span>
                    </div>

                    <button className="btn btn-mybtn px-5" onClick={this.saveUpdated}>Save</button>
                    <button className="btn btn-mybtn btn-delete px-5" onClick={() => this.props.updateClickedInner(null)}>Cancel</button>
                </div>
            </>
        )
    }
}

export default CategoryInnerComponent