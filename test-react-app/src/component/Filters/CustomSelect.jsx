import React from 'react'
import Select from 'react-select'


export default ({ onChange, items, value }) => {

    const options = []

    const getOptions = (items) => {

        if (options.length < 1) {
            for (let i = 0; i < items.length; i++) {
                options.push(
                    { value: `${items[i].value}`, label: `${items[i].name}` })
            }

        }
        return options
    }
    
    const defaultValue = (options, value) => {
        return options ? options.find(option=>option.value === value) : ""
    }

   

    return (
       
       
            <Select                
                options={getOptions(items)}
                value={defaultValue(options, value)}
                onChange={value => onChange(value)}
                placeholder={"..."}
                autosize={true}
                clearable={true}
                isSearchable={true}
            />

           

      
        
        )
}