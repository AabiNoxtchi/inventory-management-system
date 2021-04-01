import React from 'react'
import Select from 'react-select'
import './Filter.css'

export default ({ onChange, items, value, className, defaultMenuIsOpen, disabled , name , log}) => {

    const options = []

    const getOptions = (items) => {
        if (items == null) return;
        if (options.length < 1) {
           // console.log("item options i = " + JSON.stringify(items));
            for (let i = 0; i < items.length; i++) {
              
                //console.log("`${items[i][name].name}` = " + `${items[i][name].name}`);
                //console.log("`${items[i][name].name}` = " + `${items[i][name].name}`);
                options.push(
                    { value: `${items[i].value || items[i].id}`, label: `${items[i].name || (items[i][name] && items[i][name].name) || '...'}` })
            }
           
        }
       // console.log("options = "+JSON.stringify(options))
        return options
    }
    
    const defaultValue = (options, value) => {
        //console.log('value = ' + value + ' option.value = ' + option.value);
        //console.log('value= option.value = ' + (option.value == value));
       // console.log("value = " + value);
        //if (log) {
           // console.log("value = " + value);
           // options.map((o) => {
            //    if (o.value == 'undefined') {
            ///        console.log("o.value = " + o.value);
            //        console.log("o.value == value = " + (o.value == value))
            //    }
           // })
       // }
        return options ? options.find(option => option.value == value) : ""
    }

    return (
       
                <Select
            className={className}
            options={getOptions(items)}
            value={defaultValue(options, value) || 'undefined'}
            onChange={value => onChange(value)}
            placeholder={"..."}
            autosize={true}
            clearable={true}
            isSearchable={true}
            defaultMenuIsOpen={defaultMenuIsOpen}
            isDisabled={disabled}
            

                />
       
        )
}