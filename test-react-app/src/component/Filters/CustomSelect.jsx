import React from 'react'
import Select from 'react-select'
import './Filter.css'

export default ({ onChange, items, value, className, defaultMenuIsOpen, disabled , name }) => {

    const options = []

    const getOptions = (items) => {
        if (items == null) return;
        if (options.length < 1) {          
            for (let i = 0; i < items.length; i++) {
                options.push(
                    { value: `${items[i].value || items[i].id}`, label: `${items[i].name || (items[i][name] && items[i][name].name) || '...'}` })
            }
           
        }
        return options
    }
    
    const defaultValue = (options, value) => {      
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