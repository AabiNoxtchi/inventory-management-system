import React, { Component } from 'react';
import CountryDataService from '../service/CountryDataService';
import '../myStyles/Style.css';
import CustomSelect from './Filters/CustomSelect';

class CountryInnerComponent extends Component {
    constructor(props) {
        super(props)
        this.state =
            {
                countryUpdateShow: props.countryUpdateShow,
                allCountries: [],               
                currencies: [],
                allPhoneCodes: []
            }
    }

    componentDidMount() {
        this.refresh()
    }

    refresh(search) {
        // console.log("search = " + search);
        CountryDataService.retrieve(this.state.countryUpdateShow.country.id||-1)
            .then(response => {
                console.log("got response = " + JSON.stringify(response));
                this.setState({
                    allCountries: response.data.allCountries||[],
                    currencies: response.data.currencies||[],
                    allPhoneCodes: response.data.allPhoneCodes||[]
                })
                // console.log("data length = " + this.state.filteredNumbers.length);
            }).catch(error =>
                console.log("error = " + error)
            )
    }

    saveUpdated = () => {

        let item = this.state.countryUpdateShow.country;
        if (!item.name || item.name == "undefined" || !item.currency || item.currency == "undefined") {
            let show = this.state.countryUpdateShow;
            show.error = "All fields are required";
            this.setState({ pdUpdateShow: show })       
        } else {
            CountryDataService.save(item)
                .then(response => {
                    let msg = this.state.countryUpdateShow.country.id && this.state.countryUpdateShow.country.id > 0 ? `Update successful` : `Save successful`;
                    this.props.updateClickedInner(null);
                    this.props.setMessage(msg);
                    this.props.refresh();
                }).catch(error => {
                    let errormsg = error.response && error.response.data ?
                        error.response.data.message ? error.response.data.message : error.response.data : error + '';
                    let show = this.state.countryUpdateShow;
                    show.error = errormsg;
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
        c.country.currency = selected.label;
        this.setState({
            countryUpdateShow: c
        })
    }
    onPhoneCodeChange(selected) {
        let c = this.state.countryUpdateShow;
        c.country.phoneCode = selected.label;
        this.setState({
            countryUpdateShow: c
        })
    }

    render() {
        return (
            <>
                <div className={this.state.countryUpdateShow.show ? "overlay d-block" : "d-none"}></div>
                <div className={this.state.countryUpdateShow.show ? "modal d-block" : "d-none"} style={{ width: "40%", height: "70%" }}>
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
                        "ml-5" : "mt-5 ml-5"}>name :</h6>
                    <CustomSelect
                        items={this.state.allCountries}
                        value={this.state.countryUpdateShow.country.name}
                        onChange={(selected) => this.onNameChange(selected)}
                    />
                    <div className="w-25 inline">
                    <h6 className="ml-5">code</h6>
                        <p className="ml-5 border-bottom">{this.state.countryUpdateShow.country.code}</p>
                    </div>
                    <div className="w20 inline"></div>
                    <div className="w-50 inline">
                    <h6 className="ml-5">phone code</h6>
                    <CustomSelect
                        items={this.state.allPhoneCodes}
                        value={this.state.countryUpdateShow.country.phoneCode}
                        onChange={(selected) => this.onPhoneCodeChange(selected)}
                        />
                    </div>
                    <h6 className="ml-5">currency :</h6>
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