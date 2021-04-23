import React, { Component } from 'react';
import CountryDataService from '../service/CountryDataService';
import '../myStyles/Style.css';
import CustomSelect from './Filters/CustomSelect';
import Function from './Shared/Function'


class CityInnerComponent extends Component {
    constructor(props) {
        super(props)
        this.state =
            {
            cityUpdateShow: props.cityUpdateShow,
            original: JSON.parse(JSON.stringify(props.cityUpdateShow.city)),
                countries: [],
                zones: []
            }
    }

    componentDidMount() {
        this.refresh()
    }

    refresh(search) {
         CountryDataService.retrieveChild(this.state.cityUpdateShow.city && this.state.cityUpdateShow.city.id || -1,
            this.state.cityUpdateShow.city.countryId && this.state.cityUpdateShow.city.countryId || -1)
            .then(response => {
                console.log("got response = " + JSON.stringify(response));
                this.setState({
                    countries: response.data.countries,
                    zones: response.data.zones
                })
             }).catch(error => {
                 let msg = Function.getErrorMsg(error);
                 let show = this.state.categoryUpdateShow;
                 show.error = msg;
                 this.setState({ categoryUpdateShow: show })
             })
    }


    saveUpdated = () => {

        let item = this.state.cityUpdateShow.city;
        item.name = item.name ? item.name.trim() : item.name;

        let original = this.state.original;

        if (!item.name || item.name.length < 1 || !item.timeZone || item.timeZone == "undefined" || !item.countryId || item.countryId == "undefined") {
            let show = this.state.cityUpdateShow;
            show.error = "All fields are required";
            this.setState({ cityUpdateShow: show })
        } else if (original.name == item.name && original.countryId == item.countryId && original.timeZone == item.timeZone) {
            let show = this.state.cityUpdateShow;
            show.error = "item hasn't changed !!!";
            this.setState({ cityUpdateShow: show })
        }
        else {
            CountryDataService.saveChild(item)
                .then(response => {
                    let msg = this.state.cityUpdateShow.city.id && this.state.cityUpdateShow.city.id > 0 ? `Update successful` : `Save successful`;
                    this.props.updateClickedInnerChild(null);
                    this.props.setMessage(msg);
                    if(this.props.refresh) this.props.refresh();
                    if(this.props.updatedCity) this.props.updatedCity(response.data, item);
                }).catch(error => {                   
                    let msg = Function.getErrorMsg(error);
                    console.log("error = " + msg);
                    let show = this.state.cityUpdateShow;
                    show.error = msg;
                    this.setState({ cityUpdateShow: show })
                })
        }
    }

    onNameChange(value) {
        let c = this.state.cityUpdateShow;
        c.city.name = value.target.value;//.replace(/\s/g, '');
        this.setState({
            cityUpdateShow: c
        })
    }
    onZoneChange(selected) {
        let c = this.state.cityUpdateShow;
        c.city.timeZone = selected.value;
        this.setState({
            cityUpdateShow: c
        })
    }
    onCountryChange(selected) {
        let c = this.state.cityUpdateShow;
        c.city.countryId = selected.value;
        this.setState({
            cityUpdateShow: c
        })
    }

    render() {
        return (
            <>
                <div className={this.state.cityUpdateShow.show ? "overlay d-block" : "d-none"}></div>
                <div className={this.state.cityUpdateShow.show ? "modal d-block" : "d-none"} style={{ width: "40%", height: "78%" }}>
                    <span class="close" onClick={() => this.props.updateClickedInnerChild(null)}>&times;</span>
                    <h2>{this.state.cityUpdateShow.city.id && this.state.cityUpdateShow.city.id > 0 ? "update" : "add"} city</h2>
                    {this.state.cityUpdateShow.error && this.state.cityUpdateShow.error.length > 1 &&
                        <div className="alert alert-warning d-flex">{this.state.cityUpdateShow.error}
                            <i class="fa fa-close ml-auto pr-3 pt-1"
                                onClick={() => {
                                    let show = this.state.cityUpdateShow;
                                    show.error = '';
                                    this.setState({ cityUpdateShow: show })
                                }}>
                            </i>
                        </div>}
                    <h6 className={this.state.cityUpdateShow.error && this.state.cityUpdateShow.error.length > 1 ?
                        "required-field" : "mt-5 required-field"}>country</h6>
                    <CustomSelect
                        items={this.state.countries}
                        value={this.state.cityUpdateShow.city && this.state.cityUpdateShow.city.countryId}
                        onChange={(selected) => this.onCountryChange(selected)}
                    />
                    <h6 className="required-field">city name</h6>
                    <input type="text" className="form-control pt-2 pb-2" value={this.state.cityUpdateShow.city && this.state.cityUpdateShow.city.name}
                        onChange={(value) => { this.onNameChange(value) }} />
                    <h6 className="required-field">time zone</h6>
                    <CustomSelect
                        items={this.state.zones}
                        value={this.state.cityUpdateShow.city && this.state.cityUpdateShow.city.timeZone}
                        onChange={(selected) => this.onZoneChange(selected)}
                    />
                    <button className="btn btn-mybtn p-x-5" onClick={this.saveUpdated}>Save</button>
                    <button className="btn btn-mybtn btn-delete px-5" onClick={() => this.props.updateClickedInnerChild(null)}>Cancel</button>
                </div>
            </>
        )
    }
}

export default CityInnerComponent