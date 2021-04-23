import React, { Component } from 'react';
import CountryDataService from '../service/CountryDataService';
import '../myStyles/Style.css';
import CustomSelect from './Filters/CustomSelect';
import Function from './Shared/Function'


class CountryInnerComponent extends Component {
    constructor(props) {
        super(props)
        this.state =
            {
            countryUpdateShow: props.countryUpdateShow,
            original: JSON.parse(JSON.stringify(props.countryUpdateShow.country)),
                allCountries: [],               
                currencies: [],
                allPhoneCodes: []
            }
    }

    componentDidMount() {
        this.refresh()
    }

    refresh(search) {
       CountryDataService.retrieve(this.state.countryUpdateShow.country.id||-1)
            .then(response => {
               this.setState({
                    allCountries: response.data.allCountries||[],
                    currencies: response.data.currencies||[],
                    allPhoneCodes: response.data.allPhoneCodes||[]
                })
           }).catch(error => {
               let msg = Function.getErrorMsg(error);
               let show = this.state.categoryUpdateShow;
               show.error = msg;
               this.setState({ categoryUpdateShow: show })
           })
    }

    saveUpdated = () => {

        let item = this.state.countryUpdateShow.country;
        let original = this.state.original;

        if (!item.name || item.name == "undefined" || !item.currency ||
            item.currency == "undefined" || !item.phoneCode || item.phoneCode == 'undefined') {
            let show = this.state.countryUpdateShow;
            show.error = "All fields are required";
            this.setState({ pdUpdateShow: show })
        } else if (original.name == item.name && original.currency == item.currency &&
            original.phoneCode == item.phoneCode) {

            let show = this.state.countryUpdateShow;
            show.error = "item hasn't changed !!!";
            this.setState({ pdUpdateShow: show })
        } else {
            CountryDataService.save(item)
                .then(response => {
                    let msg = this.state.countryUpdateShow.country.id && this.state.countryUpdateShow.country.id > 0 ? `Update successful` : `Save successful`;
                    this.props.updateClickedInner(null);
                    this.props.setMessage(msg);
                    this.props.refresh();
                }).catch(error => {
                   let msg = Function.getErrorMsg(error);
                    let show = this.state.countryUpdateShow;
                    show.error = msg;
                    this.setState({ countryUpdateShow: show })
                })
        }       
    }

    onNameChange(selected) {
        let c = this.state.countryUpdateShow;
        c.country.name = selected.label; 
        c.country.code = selected.value;
        this.setState({
            countryUpdateShow:c          
        })
    }
    onCurrencyChange(selected) {
        let c = this.state.countryUpdateShow;
        c.country.currency = selected.value;
        this.setState({
            countryUpdateShow: c
        })
    }
    onPhoneCodeChange(selected) {
        let c = this.state.countryUpdateShow;
        c.country.phoneCode = selected.value;
        this.setState({
            countryUpdateShow: c
        })
    }

    render() {
        return (
            <>
                <div className={this.state.countryUpdateShow.show ? "overlay d-block" : "d-none"}></div>
                <div className={this.state.countryUpdateShow.show ? "modal d-block" : "d-none"} style={{ width: "40%", height: "78%" }}>
                    <span class="close" onClick={() => this.props.updateClickedInner(null)}>&times;</span>
                    <h2>{this.state.countryUpdateShow.country.id && this.state.countryUpdateShow.country.id > 0 ? "update" : "add"} country</h2>
                    {this.state.countryUpdateShow.error && this.state.countryUpdateShow.error.length > 1 &&
                        <div className="alert alert-warning d-flex">{this.state.countryUpdateShow.error}
                            <i class="fa fa-close ml-auto pr-3 pt-1"
                                onClick={() => {
                                    let show = this.state.countryUpdateShow;
                                    show.error = '';
                                    this.setState({ countryUpdateShow: show })
                                }}>
                            </i>
                        </div>}
                    <h6 className={this.state.countryUpdateShow.error && this.state.countryUpdateShow.error.length > 1 ?
                        "required-field" : "mt-5 required-field"}>name</h6>
                    <CustomSelect
                        items={this.state.allCountries}
                        value={this.state.countryUpdateShow.country.code}
                        onChange={(selected) => this.onNameChange(selected)}
                    />
                    <div className="w-25 inline pl-3">
                        <h6 className="pl-3 required-field">code</h6>
                        <p className="ml-4 border-bottom">{this.state.countryUpdateShow.country.code}</p>
                    </div>
                    {<div className="w20 inline"></div>
                    }
                    <div className="w-50 inline">
                        <h6 className="required-field">phone code</h6>
                    <CustomSelect
                            items={this.state.allPhoneCodes}
                            value={this.state.countryUpdateShow.country.phoneCode}
                        onChange={(selected) => this.onPhoneCodeChange(selected)}
                        />
                    </div>
                    <h6 className="required-field">currency</h6>
                    <CustomSelect
                        items={this.state.currencies}
                        value={this.state.countryUpdateShow.country.currency}
                        onChange={(selected) => this.onCurrencyChange(selected)}
                    />
                    <button className="btn btn-mybtn p-x-5" onClick={this.saveUpdated}>Save</button>
                    <button className="btn btn-mybtn btn-delete px-5" onClick={() => this.props.updateClickedInner(null)}>Cancel</button>
                </div>
            </>
        )
    }
}

export default CountryInnerComponent