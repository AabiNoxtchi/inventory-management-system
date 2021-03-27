import React, { Component } from 'react';
//import DeliveryDetailDataService from '../service/DeliveryDetailDataService';
//import DeliveryDataService from '../service/DeliveryDataService';
import ProductDetailDataService from '../service/ProductDetailDataService';
import '../myStyles/Style.css';
import { Link, withRouter } from 'react-router-dom'
//import ProductDetailDataService from '../service/ProductDetailDataService';

class ProductDetailInnerComponent extends Component {
    constructor(props) {
        super(props)

        this.state =
            {
                pdUpdateShow: props.pdUpdateShow,
               // pd: props.pd,
                message: props.message
            }
        console.log("item = " + JSON.stringify(this.state.pdUpdateShow.pd));
    }

    saveUpdatedPd = () => {

        let x = this.state.pdUpdateShow.x; 
        let number = this.state.pdUpdateShow.pd ? this.state.pdUpdateShow.pd.inventoryNumber.trim() : null;
        let available = this.state.pdUpdateShow.pd ? this.state.pdUpdateShow.pd.available : null;
        let discarded = this.state.pdUpdateShow.pd ? this.state.pdUpdateShow.pd.discarded : null;

        let previousItem = this.props.items[x];

        if (number == null || available == null || discarded == null) {
            let show = this.state.pdUpdateShow;
            show.error = "All fields are required";
            this.setState({ pdUpdateShow: show })
        } else if (number.length == 0) {
            let show = this.state.pdUpdateShow;
            show.error = "number can't be empty";
            this.setState({ pdUpdateShow: show })
        } else if (number == previousItem.inventoryNumber && available == previousItem.available && discarded == previousItem.discarded) {
            let show = this.state.pdUpdateShow;
            show.error = "item hasn't changed";
            this.setState({ pdUpdateShow: show })
        }   else {
            let item = {
                inventoryNumber: number, available: available, discarded: discarded,
                id: this.state.pdUpdateShow.pd.id, deliveryDetailId: this.state.pdUpdateShow.pd.deliveryDetailId                             
            }
            
            ProductDetailDataService.save(item)
                .then(response => {
                   
                    let items = this.props.items;
                    items[x].inventoryNumber = number;
                    items[x].available = available;
                    items[x].discarded = discarded;

                   this.props.updateClicked(null);

                    let message = this.state.message;                   
                    message = `Update successful`;

                    this.props.setItems(items);
                    this.props.setMessage(message);                   

                }).catch(error => {                    
                    let errormsg = error.response && error.response.data ?
                        error.response.data.message ? error.response.data.message : error.response.data : error + '';
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

    onAvailableChange = (value) => {      
        let show = this.state.pdUpdateShow;
        if (show.pd == null) show.pd = {};
        show.pd.available = (value.target.value == 'true');
        show.error = null;
        this.setState({ pdUpdateShow: show })
        console.log("state.pd = " + JSON.stringify(this.state.pdUpdateShow.pd));
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
                <div className={this.state.pdUpdateShow.show ? "modal d-block" : "d-none"} style={{ width: "60%", height: "73%", overflow:"auto" }}>
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
                    <div className="pr-2 mr-2 mt-3">
                        <h6 className="px-5">available :</h6>
                        <input
                            className="" type="checkbox" 
                            value={true} checked={this.state.pdUpdateShow.pd.available == true}
                            onChange={(value) => {
                                this.onAvailableChange(value)
                                }}
                        /><span className=" pl-1" >Available</span>
                                <input
                            className="" type="checkbox" 
                            value={false} checked={this.state.pdUpdateShow.pd.available == false}
                            onChange={(value) => {
                                this.onAvailableChange(value)
                                }}
                        /><span className="pl-1" >Missing</span>
                    </div>
                    <div className="pr-2 mr-2 mt-3">
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
                    </div>
                    <button className="btn btn-mybtn p-x-5 " onClick={this.saveUpdatedPd}>Save</button>
                    <button className="btn btn-mybtn btn-delete px-5 " onClick={() => this.props.updateClicked(null)}>Cancel</button>
                    <p style={{ fontSize: "80%" }}>ps : to update price or date you must visit the origin of the <Link
                        to={`/deliveries?Filter.number=${this.state.pdUpdateShow.pd.deliveryNumber}&deliveryView=DeliveryDetailView`}>delivery</Link> </p>
                </div>
                
            </>
        )
    }

}
export default ProductDetailInnerComponent