import React, { Component } from 'react';
import ProductDetailDataService from '../service/ProductDetailDataService';
import '../myStyles/Style.css';
import { Link, withRouter } from 'react-router-dom'
import Function from './Shared/Function'

class ProductDetailInnerComponent extends Component {
    constructor(props) {
        super(props)
        this.state =
            {
                pdUpdateShow: props.pdUpdateShow,               
                message: props.message,
                filter: props.filter,                
            }
         }

    saveUpdatedPd = () => {

        let x = this.state.pdUpdateShow.x; 
        let number = this.state.pdUpdateShow.pd ? this.state.pdUpdateShow.pd.inventoryNumber.trim() : null;
        let econdition = this.state.pdUpdateShow.pd ? this.state.pdUpdateShow.pd.econdition : null;
        let discarded = this.state.pdUpdateShow.pd ? this.state.pdUpdateShow.pd.discarded : null;

        let previousItem = this.props.items[x];

        if (number == null || econdition == null || discarded == null) {
            let show = this.state.pdUpdateShow;
            show.error = "All fields are required";
            this.setState({ pdUpdateShow: show })
        } else if (number.length == 0) {
            let show = this.state.pdUpdateShow;
            show.error = "number can't be empty";
            this.setState({ pdUpdateShow: show })
        } else if (number == previousItem.inventoryNumber && econdition == previousItem.econdition && discarded == previousItem.discarded) {
            let show = this.state.pdUpdateShow;
            show.error = "item hasn't changed";
            this.setState({ pdUpdateShow: show })
        }   else {
            let item = {
                inventoryNumber: number, econdition: econdition, discarded: discarded,
                id: this.state.pdUpdateShow.pd.id, deliveryDetailId: this.state.pdUpdateShow.pd.deliveryDetailId                             
            }
          
            ProductDetailDataService.save(item)
                .then(response => {                   
                    let items = this.props.items;
                    items[x].inventoryNumber = number;
                    items[x].econdition = econdition;
                    items[x].discarded = discarded;

                   this.props.updateClicked(null);
                    let message = this.state.message;                   
                    message = `Update successful`;
                    this.props.setItems(items);
                    this.props.setMessage(message);                   

                }).catch(error => { 
                    let errormsg = Function.getErrorMsg(error);
                    let show = this.state.pdUpdateShow;
                    show.error = errormsg;
                    this.setState({ pdUpdateShow: show })                    
                })
        }
    }


    onNumberChange = (value) => {
        let show = this.state.pdUpdateShow;
        if (show.pd == null) show.pd = {};
        show.pd.inventoryNumber = value.target.value;
        show.error = null;
        this.setState({ pdUpdateShow: show })
    }

    onConditionChange = (value) => {
        let show = this.state.pdUpdateShow;
        if (show.pd == null) show.pd = {};
        show.pd.econdition = value.target.value;
        show.error = null;
        this.setState({
            pdUpdateShow: show,
            })
         }

    onDiscardedChange = (value) => {       
        let show = this.state.pdUpdateShow;
        if (show.pd == null) show.pd = {};
        show.pd.discarded = (value.target.value == 'true');
        show.error = null;
        this.setState({ pdUpdateShow: show })
    }

    render() {
        return (
            <>
                <div className={this.state.pdUpdateShow.show ? "overlay d-block" : "d-none"}></div>
                <div className={this.state.pdUpdateShow.show ? "modal d-block" : "d-none"} style={{ width: "60%", height: "65%", overflow:"auto" }}>
                    <span class="close" onClick={() => this.props.updateClicked(null)}>&times;</span>
                    <h2>update inventory</h2>
                    {this.state.pdUpdateShow.error && this.state.pdUpdateShow.error.length > 1 &&
                        <div className="alert alert-warning d-flex">{this.state.pdUpdateShow.error}
                            <i class="fa fa-close ml-auto pr-3 pt-1"
                                onClick={() => {
                                    let show = this.state.pdUpdateShow;
                                    show.error = '';
                                    this.setState({ pdUpdateShow: show })
                                }}>
                            </i>
                        </div>}
                    <h6 className={this.state.pdUpdateShow.error && this.state.pdUpdateShow.error.length > 1 ? "ml-5" : "mt-5 ml-5"}>number : </h6>
                    <input type="text" className="form-control" value={this.state.pdUpdateShow.pd && this.state.pdUpdateShow.pd.inventoryNumber}
                        onChange={(value) => {
                            this.onNumberChange(value)                       
                    }} />                   
                    <div className="  mt-3 inline w50">
                        <h6 className="pl-5">condition :</h6>
                        <div className="pl-4">
                        {this.state.filter && this.state.filter.econditions && this.state.filter.econditions.map( (condition) => 
                            <>
                            <input
                                className="" type="checkbox"
                                    value={condition.value} checked={this.state.pdUpdateShow.pd.econdition == condition.value}
                                    onChange={(value) => {
                                        this.onConditionChange(value)
                                    }}
                                /> <span className="pl-1" >{condition.name}</span></>
                            )}
                        </div>                       
                    </div>
                    {  <div className="pr-2 mr-2 mt-3 inline w40">
                        <h6 className=" px-5">discarded :</h6>
                        <input
                            className="" type="checkbox"
                            value={true} checked={this.state.pdUpdateShow.pd.discarded == true}
                            onChange={(value) => {
                                this.onDiscardedChange(value)
                                 }}
                        /><span className="pl-1" >Discarded</span>
                        <input
                            className="" type="checkbox" 
                            value={false} checked={this.state.pdUpdateShow.pd.discarded == false}
                            onChange={(value) => {
                                this.onDiscardedChange(value)
                                }}
                        /><span className="pl-1" >Alive</span>
                    </div>}                   
                    <div>
                        <button className="btn btn-mybtn p-x-5 " onClick={this.saveUpdatedPd}>Save</button>
                        <button className="btn btn-mybtn btn-delete px-5 " onClick={() => this.props.updateClicked(null)}>Cancel</button></div>
                    <p style={{ fontSize: "80%" }}>ps : to update price or date you must visit the origin of the <Link
                        to={`/deliveries?Filter.number=${this.state.pdUpdateShow.pd.deliveryNumber}&deliveryView=DeliveryDetailView`}>delivery</Link> </p>
                </div>                
            </>
        )
    }
}
export default ProductDetailInnerComponent