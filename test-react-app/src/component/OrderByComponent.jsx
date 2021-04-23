import React, { Component } from 'react';
import '../myStyles/Style.css';

class OrderByComponent extends Component {

    render() {
        let sameName = this.props.orderBy.name == this.props.name;
    return(
        <>
        {sameName && this.props.orderBy.direction == "asc" ?
                (< i class="fa fa-caret-up ml-2 hoverable" style={{ color: sameName ? "#224047" : "" }}
                    onClick={this.props.onClick} />) :
                (< i class="fa fa-caret-down ml-2 hoverable" style={{ color: sameName ? "#224047" : "" }}
                    onClick={this.props.onClick} />)
            }
       </>
   )}
}

export default OrderByComponent