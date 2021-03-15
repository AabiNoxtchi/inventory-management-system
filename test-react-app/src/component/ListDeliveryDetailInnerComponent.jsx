import React, { Component } from 'react';
//import DeliveryDetailDataService from '../service/DeliveryDetailDataService';
import DeliveryDataService from '../service/DeliveryDataService';
import ProductDetailDataService from '../service/ProductDetailDataService';
import '../myStyles/Style.css';
//import ProductDetailDataService from '../service/ProductDetailDataService';

class ListDeliveryDetailInnerComponent extends Component {
    constructor(props) {
        super(props)
        console.log("rendering items");
        this.state =
            {
            pdShow: props.pdShow,
            pdmessage: props.pdmessage,
            items: props.items,
           // pdUpdateShow: props.pdUpdateShow
           // message: props.message

            }
        
    }

    getProductDetails(x, y, id) {

        if (this.state.pdShow[x] == undefined || this.state.pdShow[x][y] == undefined) {
            let show = this.state.pdShow;
            if (this.state.pdShow[x] == undefined) show[x] = [];

            ProductDetailDataService.retrieveAllNumbers("?deliveryDetailId=" + id)
                .then(response => {
                    console.log("pds = " + JSON.stringify(response.data));
                    show[x][y] = {}
                    show[x][y].show = true;
                    show[x][y].data = response.data;
                    //this.setState({ pdShow: show });
                    this.props.setStatePdShow(show);
                })
        }
        else {
            let show = this.state.pdShow;
            show[x][y].show = !show[x][y].show;
            //this.setState({ pdShow: show })
            this.props.setStatePdShow(show);
        }
    }

    deleteChildClicked(id, x, y) {//child id
        console.log("delete child clicked ");
        let show = this.state.pdShow;
        if (this.state.items[x].deliveryDetails.length == 1) {
            show.splice(x, 1);
        } else {
            show[x].splice(y, 1);
        }
        //this.setState({ pdShow: show });
        this.props.setStatePdShow(show);
        
        DeliveryDataService.deleteChild(id)
            .then(
            response => {
                console.log("response for deleted child = " + response.data);
               /* let msg = this.state.message;
                msg = `Delete successful`;
                this.setState({ message:msg})*/
                this.props.setmessage(`Delete successful`);
                this.props.refresh();
                }
            )
    }

    deletePDChildClicked = (value, x, y, id) => {
        if (this.state.items[x].deliveryDetails[y].quantity == 1) {
           /* let show = this.state.pdShow;
            if (this.state.items[x].deliveryDetails.length == 1) {
                show.splice(x, 1);
            } else {
                show[x].splice(y, 1);
            }
            this.setState({ pdShow: show })*/
            this.deleteChildClicked(id, x, y)
        }
        else {
            ProductDetailDataService.delete(value)
                .then(
                    response => {
                        ProductDetailDataService.retrieveAllNumbers("?deliveryDetailId=" + id)
                            .then(response => {
                               // console.log("after deleting pd x="+x);
                                let show = this.state.pdShow;
                                show[x][y].data = response.data;
                                let items = this.state.items;
                                items[x].deliveryDetails[y].quantity = items[x].deliveryDetails[y].quantity - 1;
                                console.log("after deleting pd items[x].deliveryDetails[y].productName=" + items[x].deliveryDetails[y].productName);
                                console.log("after deleting pd items[x].deliveryDetails[y].quantity=" + items[x].deliveryDetails[y].quantity);
                                items[x].total = items[x].total - items[x].deliveryDetails[y].pricePerOne;
                                let pdmessages = this.state.pdmessage;
                                if (pdmessages[x] == null) pdmessages[x] = [];
                                pdmessages[x][y] = `Delete successful`;

                               /* this.setState({
                                   // items: items,
                                    //pdShow: show,
                                    pdmessage: pdmessages
                                })*/
                                this.props.setStatePdMessage(pdmessages);
                                this.props.setStatePdShow(show);
                                this.props.setStateItems(items);
                            })
                    })
        }
    }

   /* updatePdChildClicked(pd, x, y, i) {
        let show = this.state.pdUpdateShow;
        show.show = !show.show;
        if (show.show == true) {
            show.pd = JSON.parse(JSON.stringify(pd))
            show.x = x;
            show.y = y;
            show.i = i
        } else {
            show.error = ''
        }
        this.props.setStatepdUpdateShow(show);
        //this.setState({
          //  pdUpdateShow: show
        //});
    }*/

    render() {

        let item = this.props.item;
        let x = this.props.x;
        console.log("items = " + JSON.stringify(item));
        return (
            <table className="table border x-Table ">
                <tbody>
                    <tr>
                        <td>Product</td>
                        <td>Quantity</td>
                        <td>Unit Price</td>
                        <td>Total</td>
                        <td style={{ width: '140px' }}></td>
                    </tr>
                    {
                        item.deliveryDetails.map(
                            (dd, y) =>
                                <>
                                    <tr key={dd.id}>
                                        <td><p className="hoverable"
                                            onClick={() => {
                                                this.getProductDetails(x, y, dd.id)
                                            }}>{dd.productName}</p></td>
                                        <td className="hoverable"
                                            onClick={() => {
                                                this.getProductDetails(x, y, dd.id)
                                            }}><p>{dd.quantity}</p></td>
                                        <td>{new Intl.NumberFormat("en-GB", {
                                            style: "currency",
                                            currency: "BGN",
                                            maximumFractionDigits: 2
                                        }).format(dd.pricePerOne)}</td>
                                        <td>{new Intl.NumberFormat("en-GB", {
                                            style: "currency",
                                            currency: "BGN",
                                            maximumFractionDigits: 2
                                        }).format(dd.pricePerOne * dd.quantity)}</td>
                                        <td><button className="btn btn-mybtn mr-1" onClick={() => this.updateClicked(item.id)}>Update</button>
                                            <button className="btn btn-mybtn btn-delete" onClick={() => this.deleteChildClicked(dd.id, x, y)}>Delete</button></td>
                                    </tr>
                                    {
                                        this.state.pdShow[x] && this.state.pdShow[x][y] && this.state.pdShow[x][y].show
                                        &&
                                        <tr>
                                            <td colspan="5">
                                                {
                                                    this.state.pdmessage[x] && this.state.pdmessage[x][y] &&
                                                    <div className="alert alert-success d-flex">{this.state.pdmessage[x][y]}
                                                        <i class="fa fa-close ml-auto pr-3 pt-1"
                                                            onClick={() => {
                                                                let pdmessages = this.state.pdmessage;
                                                                pdmessages[x][y] = null;
                                                                this.setState({ pdmessage: pdmessages })
                                                            }}>
                                                        </i>
                                                    </div>
                                                }
                                                <table className="ml-5 mb-3" style={{ width: '80%' }}>
                                                    <tr><td style={{ width: '10%' }}>number</td>
                                                        <td className="pl-5">inventory number</td>
                                                        <td style={{ width: '140px' }}></td></tr>
                                                    {this.state.pdShow[x][y].data.map((pd, i) =>
                                                        <tr>
                                                            <td >{i + 1}</td>
                                                            <td className="pl-5 pb-3 hoverable"
                                                                onClick={() => {
                                                                    this.props.historyPush(`/productDetails?Filter.id=${pd.value}`)
                                                                }}>
                                                                {pd.name}
                                                            </td>
                                                            <td><button className="btn btn-mybtn mr-1" onClick={() => {
                                                                this.props.updatePdChildClicked(pd, x, y, i)
                                                            }}>Update</button>
                                                                <button className="btn btn-mybtn btn-delete"
                                                                    onClick={() => this.deletePDChildClicked(pd.value, x, y, dd.id)}>
                                                                    Delete</button></td>
                                                        </tr>
                                                    )}
                                                </table>
                                            </td>
                                        </tr>
                                    }
                                </>
                        )}
                </tbody>
            </table>
            )
    }
}

export default ListDeliveryDetailInnerComponent