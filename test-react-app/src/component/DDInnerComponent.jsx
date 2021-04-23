import React, { Component } from 'react';
import DeliveryDetailDataService from '../service/DeliveryDetailDataService';
import '../myStyles/Style.css';
import CustomSelect from './Filters/CustomSelect';

class DDInnerComponent extends Component {
    constructor(props) {
        super(props)
        this.state =
            {
            ddUpdateShow: props.ddUpdateShow ,
                items: props.items,
                products: props.products,
            ddmessage: props.ddmessage,
            producterror: null,
            productNums: [],
            numErrors:null,
            quantity:0
            }
    }

   
    saveUpdateddd = () => {
        let show = this.state.ddUpdateShow;
        if (this.state.producterror != null) {            
            return
        }
        let x = this.state.ddUpdateShow.x;
        let y = this.state.ddUpdateShow.y;
        let items = this.state.items;
        let item = items[x].deliveryDetails[y];
        let productNums = this.state.productNums;
        
        let productId = (show.dd && show.dd.productId)||null;
        let price = (show.dd && show.dd.pricePerOne)||null;

        let isError = false;
        if (!price || !productId) {
            show.error = "required fields can't be empty !!!";
            this.setState({ ddUpdateShow: show });
            isError=true
        } else if (item && productId == item.productId && price == item.pricePerOne) {
            show.error = "delivery detail hasn't changed";
            this.setState({ ddUpdateShow: show });
            isError = true
        } else if (productNums.length < 0) {
            show.error = "delivery detail must have inventories !!!";
            this.setState({ ddUpdateShow: show });
            isError = true
        } else if (productNums.length > 0) {
            let numErrors = this.state.numErrors;
            numErrors = [];
            for (let i = 0; i < productNums.length; i++) {
                if (productNums[i].name.length < 1) {
                    numErrors[i] = 'required field !!!'; 
                    isError = true
                }
                else if (productNums[i].name.length < 4) {
                    numErrors[i] = 'too short !!!';
                    isError = true
                }
                else {                    
                    for (let j = 0; j < i; j++) {
                        if (productNums[i].name == productNums[j].name) {
                            numErrors[i] = 'same number exists !!!';
                            isError = true
                        }
                    }                    
                }
            }
            this.setState({ numErrors: numErrors })
        }
        if (!isError){
            let item = show.dd;
            item.deliveryId = items[x].id;
            item.productNums = productNums;
            DeliveryDetailDataService.save(item)
                .then((response) => {
                     item.id = item.id || response.data;
                    item.quantity = this.state.quantity;
                    y = (y || items[x].deliveryDetails.length );
                    
                    items[x].deliveryDetails[y] = item;
                    let total = 0;
                    items[x].deliveryDetails.map(i => total = total + (i.pricePerOne * i.quantity));
                    items[x].total = items[x].deliveryDetails.map(i => i.pricePerOne * i.quantity)
                   
                    this.props.setItems(items);
                    this.props.updateChildClicked(null);
                    let msg = this.state.ddmessage;
                    msg[x] = "update successful";
                    this.props.setddMessage(msg);
                   
                }).catch((error) => {
                    show.error = "errors occured : " + error + " !!!";
                    this.setState({
                        ddUpdateShow: show,
                        numErrors: error.response.data
                    })
                })
        }
    }

    isProductPresent = (value) => {
        let isPresent = false;

        let x = this.state.ddUpdateShow.x;
        let y = this.state.ddUpdateShow.y;
        let items = this.state.items;        
        let dds = items[x].deliveryDetails;

        dds.map((dd, i) => {
            if ((y != null && y != i && dd.productId == value) || (y == null && dd.productId == value) )
                isPresent = true
        });

        return isPresent
    }

    onProductChange = (selected) => {               
        let ddUpdateShow = this.state.ddUpdateShow;
        if (ddUpdateShow.dd == null) ddUpdateShow.dd = {}
        ddUpdateShow.dd.productId = selected.value;
        ddUpdateShow.dd.productName = selected.label;
        this.setState({
            ddUpdateShow: ddUpdateShow,
            producterror: this.isProductPresent(selected.value) ? 'product already exist in this delivery !!!' : null
        })
    }

    onAddQuantity() {
        const productNums = this.state.productNums;
        let quantity = this.state.quantity +1;        
        productNums.push({ value: '', name: '' });
        this.setState({
            productNums: productNums,
            quantity: quantity
        })    
    }

    onChangeProductNums(e, i) {
        let productNums = this.state.productNums || [];        
        productNums[i].name = e.target.value;
        this.setState({ productNums: productNums })
    }

    deleteNums(i) {
        let list = this.state.productNums;
        list.splice(i, 1);
        let quantity = this.state.quantity - 1; 
        this.setState({
            productNums: list,
            quantity: quantity
        })  
    }

    render() {
        return (
            <>               
                <div className={this.state.ddUpdateShow.show ? "overlay d-block" : "d-none"}></div>
                <div className={this.state.ddUpdateShow.show ? "modal d-block" : "d-none"}
                    style={{ width: !this.state.ddUpdateShow.dd || !this.state.ddUpdateShow.dd.id ? "85%" : "50%" , height: "60%" }}>
                    <span class="close pt-3" onClick={() => this.props.updateChildClicked(null)}>&times;</span>
                    <h2>{this.state.ddUpdateShow.dd ? "update" : "add"} delivery detail </h2>                    
                    <div className="d-flex align-items-top  mb-3">
                    <div className={
                            (!this.state.ddUpdateShow.dd || !this.state.ddUpdateShow.dd.id) ? "inline w40" : ""}>
                            {this.state.ddUpdateShow.error && this.state.ddUpdateShow.error.length > 1 &&
                                <div className="alert alert-warning d-flex">{this.state.ddUpdateShow.error}
                                    <i class="fa fa-close ml-auto pr-3 pt-1"
                                        onClick={() => {
                                            let show = this.state.ddUpdateShow;
                                            show.error = '';
                                            this.setState({ ddUpdateShow: show })
                                        }}>
                                    </i>
                                </div>}
                    <h6 className={this.state.ddUpdateShow.error && this.state.ddUpdateShow.error.length > 1 ?
                        "ml-5" : "mt-5 ml-5"}>product : </h6>
                    <CustomSelect
                        className={(!this.state.ddUpdateShow.dd || !this.state.ddUpdateShow.dd.id) ?
                                 "w90" : this.state.producterror ? "mb-0" : ""}
                        items={this.state.products}
                        value={(this.state.ddUpdateShow.dd && this.state.ddUpdateShow.dd.productId)}                        
                        onChange={(selected) => this.onProductChange(selected)}
                    />
                    {this.state.producterror &&
                        <div className="alert alert-warning d-flex in-error">{this.state.producterror}
                            <i class="fa fa-close ml-auto pr-3 pt-1"
                            onClick={() => {
                                let err = this.state.producterror;
                                err = null;
                                this.setState({ producterror: err })
                                }}>
                            </i>
                        </div>}
                    <h6 className="ml-5">price per one :</h6>
                            <input type="number" className="form-control"
                                value={this.state.ddUpdateShow.dd ? this.state.ddUpdateShow.dd.pricePerOne : ''} min="0" onChange={(value) => {
                        let show = this.state.ddUpdateShow;
                        if (show.dd == null) show.dd = {}
                        show.dd.pricePerOne = value.target.value;
                        this.setState({ ddUpdateShow: show })
                        }} />
                        <button className="btn btn-mybtn p-x-5 ml-5" onClick={this.saveUpdateddd}>Save</button>
                        <button className="btn btn-mybtn btn-delete px-5 ml-5" onClick={() => this.props.updateChildClicked(null)}>Cancel</button>
                    </div>
                    {(!this.state.ddUpdateShow.dd || !this.state.ddUpdateShow.dd.id) &&
                            <div className="w60 scrollable500">                            
                            <button className="btn btn-mybtn px-5 ml-5 mt-1 mb-1"
                                onClick={() => { this.onAddQuantity() }}>Add inventory&nbsp;
                                <i class="fa fa-angle-double-right" aria-hidden="true"></i></button>
                            {this.state.productNums.map((num, i) =>
                                        <div key={i} className="ml-1" >
                                        <label>number {i + 1} :&nbsp;</label>
                                        <input
                                            value={this.state.productNums[i].name}
                                            type="text"
                                            className={'form-control inline w60 m-0 p-1'}
                                            onChange={e => { this.onChangeProductNums(e, i) }} />
                                        {<button className="btn btn-mybtn btn-delete m-0 ml-1" type="button"
                                            onClick={() => {
                                                this.deleteNums(i)
                                            }}>Delete</button>
                                        }
                                        {
                                            this.state.numErrors && this.state.numErrors instanceof Array
                                            && this.state.numErrors[i] &&
                                            <div className="alert alert-warning d-inline ml-1">
                                                {this.state.numErrors[i]}
                                            </div>
                                        }
                                    </div>
                                )}
                        </div>
                        }
                    </div>
                </div>
            </>
        )
    }
}
export default DDInnerComponent