import React, { Component } from 'react';
import { Formik, Form, Field } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css';
import Functions from './Functions';

class CategoryFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            name: props.name,
            names: props.names,
            filteredNames: this.filter([], props.names, props.productType),
            productType: props.productType,
            productTypes: props.productTypes,
            prefix: props.prefix,
        }

        this.onSubmit = this.onSubmit.bind(this)
        this.resetForm = this.resetForm.bind(this)
    }

    onSubmit(values) {  
      
        let path = window.location.pathname;
        let search = window.location.search;
        Functions.getSubmitPath(path, search, this.state.prefix, values, this.props.onNewSearch)

       }

    resetForm() {      
        this.props.onNewSearch ?
            this.props.onNewSearch('') :
            window.location.href = window.location.pathname;
    }

    filter(subs, names, value) {
        subs = [];
        if (value == null) subs = names;
         else {
            for (let i = 0; i < names.length; i++) {

                if (names[i].filterBy == value || !names[i].value || names[i].value == 'undefined' ) {
                    subs.push(names[i])
                }
            }
        }
        return subs
    }

    render() {
       
        let { name, names, productType, productTypes, filteredNames } = this.state
        return (

            <Formik
                initialValues={{ name, names, productType, productTypes, filteredNames }}
                onSubmit={this.onSubmit}
                enableReinitialize={true}
            >
                {({ props, setFieldValue, values }) => (
                    <Form className="filter-form">
                        <fieldset >

                            <div className="inline px-2 mx-2">
                                <label>product type :</label>
                                {
                                    productTypes&&productTypes.map((type) =>
                                        <div className="inline">
                                            <Field
                                                className="mx-2" type="checkbox" name="productType"
                                                value={type.value} checked={type.name === values.productType}
                                                onChange={(value) => {
                                                   if (value.target.value == values.productType) value = null;                                                   
                                                    let subs = values.filteredNames;
                                                    subs = this.filter(subs, values.names, value ? value.target.value : null);                                                   
                                                    setFieldValue("productType", value ? value.target.value : null);
                                                    setFieldValue("filteredNames", subs);
                                                }}
                                            />
                                            {type.name}
                                        </div>
                                    )
                                }
                            </div>
                            <div className="inline">
                                <label >category&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-4"}
                                    items={values.filteredNames || filteredNames || names}
                                    value={values.name || ''}
                                    onChange={(selected) => setFieldValue("name", selected.value)}
                                />
                            </div>
                            <div className="inline">
                                <button className="button px-5" type="submit">Search</button>
                                <button className="button btn-delete" type="button" onClick={this.resetForm}>reset</button>
                            </div>
                        </fieldset>
                    </Form>
                )
                }
            </Formik>
        )
    }
}

export default CategoryFilter