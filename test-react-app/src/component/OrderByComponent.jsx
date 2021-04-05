import React, { Component } from 'react';
import '../myStyles/Style.css';

class OrderByComponent extends Component {
    render() {

    return(
    <>
        {
            this.props.orderBy.name == this.props.name && this.props.orderBy.direction == "asc" ?
                    (< i class="fa fa-caret-up ml-2 hoverable" onClick={this.props.onClick}/>) : //this.getOrderedList("inventoryNumber")} />) :
                (< i class="fa fa-caret-down ml-2 hoverable" onClick={this.props.onClick} />)
            }
            </>
   ) }
}

export default OrderByComponent