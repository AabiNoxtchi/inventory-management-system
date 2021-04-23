import React, { Component } from 'react';
import '../myStyles/Style.css';
import DatePicker from "react-datepicker";
import CustomSelect from './Filters/CustomSelect';


class DeleteAllInnerComponent extends Component {
    constructor(props) {
        super(props)

        this.state =
            {
                items: props.items
            }
    }

    convertDate(g) {
        if (g == null) return
        g = new Date(g);
        g.setHours(g.getHours() - g.getTimezoneOffset() / 60);
        return g;
    }

    getStringDate(date) {
        date = this.convertDate(date);
        date = date.toISOString();
        date = date.substring(0, date.indexOf('T'));
        return date;
    }

    checkValidInputs() {
        if (this.state.selectedDate != null)
            return true

        this.setState({ error: 'must choose date  !!!' })
        return false
    }
    render() {
        return (
            <>

                <div className="overlay d-block"></div>
                <div className="modal d-block" style={{
                    fontWeight: "normal", height: "55%"
                }}>
                   <span class="close pt-3" onClick={() => this.props.cancel()}>&times;</span>
                    <h2>Delete All before </h2>

                    
                        
                            {this.state.error &&
                                <div className="alert alert-warning d-flex">{this.state.error}
                                    <i class="fa fa-close ml-auto pr-3 pt-1"
                                        onClick={() => {
                                            
                                            this.setState({ error: null })
                                        }}>
                                    </i>
                        </div>}

                    <div className={this.state.error ?
                        "ml-5" : "mt-5 ml-5"}>
                        <h6 className="ml-1">inventory </h6>
                    <CustomSelect
                        className={"inline w90 ml-0 p-0"}
                                items={this.state.items}
                                value={(this.state.itemId)}

                            onChange={(selected) => this.setState({itemId : selected.value})}
                        />
                    </div>

                    <div className="ml-5">
                        <h6 className="ml-1 required-field">date</h6>
                        <div className="inline w70 pl-0">
                            <DatePicker
                                className="form-control w100 m-0 "
                                dateFormat="dd MMMM yyyy"
                                locale="en-GB"
                                isClearable
                                placeholderText="..."
                                maxDate={new Date()}
                                showYearDropdown
                                dropdownMode="select"
                                selected={this.state.selectedDate && new Date(this.state.selectedDate) || null}
                            onChange={date => {
                                this.setState({                                   
                                    selectedDate: this.getStringDate(date)
                                })
                                if (date && this.state.error)
                                    this.setState({error: null})
                                }} />
                        </div>
                    </div>
                   
                           

                    <button className="btn btn-mybtn p-x-5 ml-5" onClick={() => {
                        if (this.checkValidInputs())
                            if (window.confirm('Are you sure ?\nAre you sure you want to delete all items before '+this.state.selectedDate+' ? '))
                                this.props.deleteAll(this.state.selectedDate, this.state.itemId)

                    }}>Delete All</button>
                    <button className="btn btn-mybtn btn-delete px-5 ml-5" onClick={() => this.props.cancel()}>Cancel</button>
                       
                       
                   
                </div>
            </>
        )
    }

}
export default DeleteAllInnerComponent