import React, { Component } from 'react';
//import DeliveryDetailDataService from '../service/DeliveryDetailDataService';
import DeliveryDataService from '../service/DeliveryDataService';
import ProductDetailDataService from '../service/ProductDetailDataService';
import '../myStyles/Style.css';
//import ProductDetailDataService from '../service/ProductDetailDataService';

class InventoryNumberInnerComponent extends Component {
    constructor(props) {
        super(props)

        this.state =
            {
            pdUpdateShow: props.pdUpdateShow,
            pdShow: props.pdShow,
            pdmessage: props.pdmessage
            }
    }

    saveUpdatedPd = () => {

        let x = this.state.pdUpdateShow.x;
        let y = this.state.pdUpdateShow.y;
        let i = this.state.pdUpdateShow.i;
        
       // console.log("i=='undefined'" + (i == 'undefined'))
        console.log("i==undefined" + (i == undefined))
        if (i == undefined) i = this.state.pdShow[x][y].data.length;
        //console.log("this.state.pdShow[x][y] = " + JSON.stringify(this.state.pdShow[x][y]));
        //console.log("this.state.pdShow[x][y].data instanceof array = " + (this.state.pdShow[x][y].data instanceof Array));
        console.log("i = " + i);
        let name = this.state.pdUpdateShow.pd ? this.state.pdUpdateShow.pd.name.trim() : null;
        if (this.state.pdUpdateShow.pd == null) {
            let show = this.state.pdUpdateShow;
            show.error = "number is required";
            this.setState({ pdUpdateShow: show })
        } else if (this.state.pdUpdateShow.pd.value &&
            name == this.state.pdShow[x][y].data[i].name) {
            let show = this.state.pdUpdateShow;
            show.error = "number hasn't changed";
            this.setState({ pdUpdateShow: show })
        } else if (name.length == 0) {
            let show = this.state.pdUpdateShow;
            show.error = "number can't be empty";
            this.setState({ pdUpdateShow: show })
        } else if (this.numberExists(name, x, y, i)){
            let show = this.state.pdUpdateShow;
            show.error = "number already exist's in this delivery";
            this.setState({ pdUpdateShow: show })
        } else {

            //console.log("submitting pd " + JSON.stringify(this.state.pdUpdateShow.pd));
            let item = {
                selectItem: {
                    name: this.state.pdUpdateShow.pd.name.trim(), value: this.state.pdUpdateShow.pd.value
                },
                deliveryDetailId: this.state.pdUpdateShow.ddid != null ? this.state.pdUpdateShow.ddid:null
            }
            console.log("submitting item " + JSON.stringify(item));
            ProductDetailDataService.updateNumber(item)
                .then(response => {
                    console.log("response = " + JSON.stringify(response));
                    this.props.updatePdChildClicked(null);

                   

                    let pdshow = this.state.pdShow;

                    if (this.state.pdUpdateShow.i == undefined) {
                        let list = pdshow[x][y].data;
                        list.push({ value: response.data, name: item.selectItem.name });
                        pdshow[x][y].data = list
                    }else
                        pdshow[x][y].data[i].name = item.selectItem.name;

                    let pdmessages = this.state.pdmessage;
                    if (pdmessages[x] == null) pdmessages[x] = [];
                    pdmessages[x][y] = `Update successful`;

                    this.props.setpdShow(pdshow);
                    this.props.setpdMessage(pdmessages);
                    if (this.state.pdUpdateShow.i == undefined) this.props.refresh()
                    
                }).catch(error => {
                    console.log("error = " + error);
                    console.log("error.response.data = " + JSON.stringify(error.response.data));
                    let errormsg = error.response && error.response.data ? error.response.data.message : error + '';
                    let show = this.state.pdUpdateShow;
                    show.error = errormsg;
                    this.setState({ pdUpdateShow: show })
                    //this.props.setpdUpdateShow( show )
                })
        }
    }

    numberExists = (name, x, y, i) => {
        let pdshow = this.state.pdShow;
        let productNums = pdshow[x][y].data;
        for (let j = 0; j < productNums.length; j++) {
            if (j == i) continue
            if (name == productNums[j].name)               
                return true           
        }  
        return false
    }

    render() {
        return (
            <>
                <div className={this.state.pdUpdateShow.show ? "overlay d-block" : "d-none"}></div>
                <div className={this.state.pdUpdateShow.show ? "modal d-block" : "d-none"} style={{ width: "50%", height: "48%" }}>
                <span class="close" onClick={() => this.props.updatePdChildClicked(null)}>&times;</span>
                <h2>update inventory number </h2>
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
                    <input type="text" className = "form-control" value={this.state.pdUpdateShow.pd && this.state.pdUpdateShow.pd.name} onChange={(value) => {
                        let show = this.state.pdUpdateShow;
                        if (show.pd == null) show.pd = {};
                    show.pd.name = value.target.value;
                    this.setState({ pdUpdateShow: show })
                }} />
                <button className="btn btn-mybtn p-x-5 " onClick={this.saveUpdatedPd}>Save</button>
                    <button className="btn btn-mybtn btn-delete px-5 " onClick={() => this.props.updatePdChildClicked(null)}>Cancel</button> 
                </div>
                </>
            )
    }

}
export default InventoryNumberInnerComponent