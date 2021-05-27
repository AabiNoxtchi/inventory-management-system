import React, { Component } from 'react';
import UserCategoryDataService from '../service/UserCategoryDataService';
import '../myStyles/Style.css';
import CustomSelect from './Filters/CustomSelect';
import Function from './Shared/Function'

class UserCategoryInnerComponent extends Component {
    constructor(props) {
        super(props)
        this.state =
            {
                categoryUpdateShow: props.categoryUpdateShow,
                names: [],
                id:null,
                categoryId: null,
                amortizationPercent: null,
                productType:null
            }
    }

    componentDidMount() {
        this.refresh()
    }

    refresh(search) {
        UserCategoryDataService.retrieve(this.state.categoryUpdateShow.category.id || -1)
            .then(response => {
                console.log("got response = " + JSON.stringify(response));
                this.setState({
                    names: response.data.names,
                    id: response.data.id,
                    categoryId: response.data.categoryId,
                    amortizationPercent: response.data.amortizationPercent,
                    productType: this.state.categoryUpdateShow.category.category && this.state.categoryUpdateShow.category.category.productType
                })
            }).catch(error => {
                let msg = Function.getErrorMsg(error);
                let show = this.state.categoryUpdateShow;
                show.error = msg;
                this.setState({ categoryUpdateShow: show })
            })
    }

    saveUpdated = () => {
        let item = { id:this.state.id, categoryId: this.state.categoryId, amortizationPercent: this.state.amortizationPercent }
        let productType = this.state.productType;
        let original = this.state.categoryUpdateShow.category;
        if (!item.categoryId || item.categoryId == "undefined" ||
            (productType === 'LTA' && (!item.amortizationPercent || item.amortizationPercent == "undefined"))) {
            let show = this.state.categoryUpdateShow;
            show.error = "All fields are required !!!";
            this.setState({ categoryUpdateShow: show })
        } else if (original.id && original.category.id == item.categoryId && original.amortizationPercent == item.amortizationPercent) {
            let show = this.state.categoryUpdateShow;
            show.error = "item hasn't changed !!!";
            this.setState({ categoryUpdateShow: show })
        } else {
            UserCategoryDataService.save(item)
                .then(response => {
                    let msg = this.state.categoryUpdateShow.category.id && this.state.categoryUpdateShow.category.id > 0 ? `Update successful` : `Save successful`;
                    this.props.updateClickedInner(null);
                    this.props.setMessage(msg);
                    this.props.refresh();
                }).catch(error => {                  
                    let msg = Function.getErrorMsg(error);
                    let show = this.state.categoryUpdateShow;
                    show.error = msg;
                    this.setState({ categoryUpdateShow: show })
                })
        }       

    }

    onamortizationChange(value) {
        value = value.target.value;
        if (value > 100) return;
        if (value < 0) return;
        let c = this.state.amortizationPercent;
        c = value;
        this.setState({
            amortizationPercent: c
        })
    }
    onCategoryChange(selected) {
        let c = this.state.categoryId;
        c = selected.value;
        let type = this.state.productType;
        type = (this.state.names.filter(x => x.value == selected.value))[0].filterBy;
        console.log("type = "+type)
        this.setState({
            categoryId: c,
            productType:type
        })
    }

    render() {
        return (
            <>
                <div className={this.state.categoryUpdateShow.show ? "overlay d-block" : "d-none"}></div>
                <div className={this.state.categoryUpdateShow.show ? "modal d-block" : "d-none"} style={{ width: "40%", height: "63%" }}>
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
                    {console.log("state product type = " + this.state.productType)}
                    <h6 className={this.state.categoryUpdateShow.error && this.state.categoryUpdateShow.error.length > 1 ?
                        "required-field" : "mt-5 required-field"}>name</h6>
                    <CustomSelect
                        items={this.state.names}
                        value={this.state.categoryId}
                        onChange={(selected) => this.onCategoryChange(selected)}
                    />
                    <h6 className="inline pl-0 pb-3 pt-3">product type</h6>{this.state.productType||'...'} 
                    <div>
                        <h6 className={`${this.state.productType == 'LTA' ? "required-field" : ""}`}>amortization</h6>
                        <input type="number" className="form-control inline p-2"
                            disabled={this.state.productType != 'LTA'}
                            value={this.state.productType!='LTA'?'':this.state.amortizationPercent
                            }
                            length="5"
                        min="0"
                            max="100"                            
                            onChange={(value) => {                               
                                this.onamortizationChange(value)
                            }} />&nbsp;%
                        </div>                    
                    <button className="btn btn-mybtn p-x-5" onClick={this.saveUpdated}>Save</button>
                    <button className="btn btn-mybtn btn-delete px-5" onClick={() => this.props.updateClickedInner(null)}>Cancel</button>
                </div>
            </>
        )
    }
}

export default UserCategoryInnerComponent