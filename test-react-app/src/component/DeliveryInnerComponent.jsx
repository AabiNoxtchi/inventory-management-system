import React, { Component } from 'react';
import DeliveryDataService from '../service/DeliveryDataService';
import '../myStyles/Style.css';
import DatePicker from "react-datepicker";
import CustomSelect from './Filters/CustomSelect';
import Function from './Shared/Function'

class DeliveryInnerComponent extends Component {
    constructor(props) {
        super(props)
        this.state =
            {
                deliveryUpdateShow: props.deliveryUpdateShow,
                items: props.items,
                message: props.message,
                suppliers: props.suppliers
            }
    }

    saveUpdated = () => {
        let x = this.state.deliveryUpdateShow.x;
        let items = this.state.items;
        let item = items[x];

        let show = this.state.deliveryUpdateShow;
        let supplierId = show.delivery.supplierId;
        let date = show.delivery.date;      

        if (!date || !supplierId) {
            show.error = "required fields can't be empty !!!";
            this.setState({ deliveryUpdateShow: show })
        } else if (supplierId == item.supplierId && this.isDateEqual(date, item.date)) {
            show.error = "delivery hasn't changed";
            this.setState({ deliveryUpdateShow: show })
        } else {
           
            let item = show.delivery;
            DeliveryDataService.save(item)
                .then((response) => {
                    items[x] = item;
                    this.props.setItems(items);
                    this.props.updateClickedInner(null);
                    let msg = this.state.message;
                    msg = "update successful";
                    this.props.setMessage(msg);
                 }).catch((error) => {
                    let msg = Function.getErrorMsg(error);
                     show.error = msg;
                    this.setState({ deliveryUpdateShow: show })
                })
        }

    }

    isDateEqual = (date1, date2) => {
       
        if (typeof date1 === 'string' && date1 == date2) return true
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

    onDateChanged = (date) => {
        let deliveryUpdateShow = this.state.deliveryUpdateShow;
        deliveryUpdateShow.delivery.date = date;
        this.setState({
            deliveryUpdateShow: deliveryUpdateShow
        })
    }

    onSupplierChange = (selected) => {
        let deliveryUpdateShow = this.state.deliveryUpdateShow;
        deliveryUpdateShow.delivery.supplierId = selected.value;
        deliveryUpdateShow.delivery.supplierName = selected.label;
        this.setState({
            deliveryUpdateShow: deliveryUpdateShow
        })
    }

    render() {
        return (
            <>
                <div className={this.state.deliveryUpdateShow.show ? "overlay d-block" : "d-none"}></div>
                <div className={this.state.deliveryUpdateShow.show ? "modal d-block" : "d-none"} style={{ width: "50%", height:"73%" }}>
                    <span class="close" onClick={() => this.props.updateClickedInner(null)}>&times;</span>
                    <h2>update delivery </h2>
                    {this.state.deliveryUpdateShow.error && this.state.deliveryUpdateShow.error.length > 1 &&
                        <div className="alert alert-warning d-flex">{this.state.deliveryUpdateShow.error}
                        <i class="fa fa-close ml-auto pr-3 pt-1"
                            onClick={() => {
                                let show = this.state.deliveryUpdateShow;
                                show.error = '';
                                this.setState({ deliveryUpdateShow: show })
                            }}>
                        </i>
                    </div>}
                    <h6 className={this.state.deliveryUpdateShow.error &&
                        this.state.deliveryUpdateShow.error.length > 1 ? "ml-5" : "mt-5 ml-5"}>number :</h6>
                    <input value={this.state.deliveryUpdateShow.delivery.number} disabled />
                    <h6 className="ml-5">date :</h6>
                    <div>
                    <DatePicker
                        className="form-control"                            
                        dateFormat="dd MMMM yyyy"
                        locale="en-GB"
                        selected={(this.state.deliveryUpdateShow.delivery.date && new Date(this.state.deliveryUpdateShow.delivery.date)) }
                        onChange={date => { this.onDateChanged(date);}} />
                    </div>
                    <h6 className="ml-5">supplier :</h6>                    
                    <CustomSelect
                        items={this.state.suppliers}
                        value={this.state.deliveryUpdateShow.delivery.supplierId}
                        onChange={(selected) => this.onSupplierChange(selected)}
                        />
                    <button className="btn btn-mybtn p-x-5" onClick={this.saveUpdated}>Save</button>
                    <button className="btn btn-mybtn btn-delete px-5" onClick={() => this.props.updateClickedInner(null)}>Cancel</button>
                </div>
            </>
        )
    }
}

export default DeliveryInnerComponent