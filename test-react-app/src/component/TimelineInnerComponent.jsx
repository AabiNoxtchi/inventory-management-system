import React, { Component } from 'react';
import '../myStyles/Style.css';
import DatePicker from "react-datepicker";
import CustomSelect from './Filters/CustomSelect';
import UserProfileDataService from '../service/UserProfileDataService';


class TimelineInnerComponent extends Component {
    constructor(props) {
        super(props)

        this.state =
            {
            items: [], 
            pager:props.pager,
            filter: props.filter,                             
            lastSearch: ''
            
            }
        this.refresh = this.refresh.bind(this)
    }

   

        
           

    refresh(search) {
        UserProfileDataService.retrieve(search)
            .then(
                response => {                   
                    this.setState({
                        items: response.data.items || response.data.daoitems,
                        pager: response.data.pager,
                    });
                }
            ).catch((error) => {
                this.setState({
                    errormsg: '' + error == 'Error: Request failed with status code 401' ? 'need to login again !!!' : 
                        error.response && typeof error.response.data == 'string' ? error.response.data :
                            error.response.data.message ? error.response.data.message : error
                })
            })
    }
   

    render() {
        return (
            <>
                
               

                   
            </>
        )
    }
}

export default TimelineInnerComponent