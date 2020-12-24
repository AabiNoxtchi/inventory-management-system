import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css'

class ProductFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            all: props.all,
            name: props.name,
            names: props.names,
            productType: props.productType,
            productTypes: props.productTypes,
            amortizationPercentMoreThan: props.amortizationPercentMoreThan,
            amortizationPercentLessThan: props.amortizationPercentLessThan,
            totalCountMoreThan: props.totalCountMoreThan,
            totalCountLessThan: props.totalCountLessThan,
            prefix: props.prefix,
           
        }

        this.onSubmit = this.onSubmit.bind(this)
        this.resetForm = this.resetForm.bind(this)
        this.MAref = React.createRef();
        this.DMAref = React.createRef();
    }

    onSubmit(values) {

        let path = window.location.pathname;
        let search = window.location.search;
        let newPath = ``;

        if (search.length > 1) {
            while (search.charAt(0) === '?') {
                search = search.substring(1);
            }
            let searchItems = search.split('&');
            for (let i = 0; i < searchItems.length; i++) {

                if (searchItems[i].startsWith('Pager.itemsPerPage='))
                    newPath += searchItems[i] + '&'
            }
        }

        let prefix = this.state.prefix;
        Object.entries(values).map(([key, value]) => {
            console.log('field value =' + value);
            console.log('field key =' + key);
            if (!key.endsWith("s") && value && value.length > 0) {
                newPath += prefix + '.' + key + '=' + value + '&'
            }

        })
        newPath = newPath.substring(0, newPath.length - 1);
        newPath = path + '?' + newPath;
        console.log('newPath =' + newPath);

       // window.location.href = newPath;
    }

    resetForm() {
        this.setState({
            all: '',
            name: '',
            productType: '',
            amortizationPercentMoreThan: '',
            amortizationPercentLessThan: '',
            totalCountMoreThan: '',
            totalCountLessThan:''
        });
        console.log('in reset form ');
    }

   /* togglecheckbox = (name) => {
        if (name === 'DMA') {
            this.MAref.current.checked = false
        }
        else if (name === 'MA') {
            this.DMAref.current.checked = false
        }
    }*/

    handleCheckedElement = (event) => {
        if (event.target.value === this.state.productType) {
            this.setState({
                productType: ''
            })
        } else {
            this.setState({
                productType: event.target.value
            })
        }
       
    }

    render() {

        let { all, name, names, productType, productTypes, amortizationPercentMoreThan, amortizationPercentLessThan, totalCountMoreThan, totalCountLessThan } = this.state

        for (let i = 0; i < productTypes.length; i++) {
            console.log('productType = ' + productTypes[i])
        }

        return (

            <Formik
                initialValues={{ all, name, names, productType, productTypes, amortizationPercentMoreThan, amortizationPercentLessThan, totalCountMoreThan, totalCountLessThan }}
                onSubmit={this.onSubmit}
                enableReinitialize={true}
            >
                {({ props, setFieldValue }) => (
                    <Form className="filter-form">
                        <fieldset >
                            <div className="inline">
                                <label>name&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-1-5"}
                                    items={names}
                                    value={name}
                                    onChange={(selected) => setFieldValue("name", selected.value)}
                                />
                            </div>
                            <div className="inline px-2">
                                <label>product type :</label>
                                {                                     
                                    productTypes.map((type) =>
                                        <label>                                           
                                            <Field // ref={type.name === 'MA' ? this.MAref : this.DMAref}
                                                className="mx-2" type="checkbox" name="productType"
                                                value={type.value} checked={type.name === this.state.productType}
                                                onClick={this.handleCheckedElement} />
                                            {type.name}
                                    </label>)
                                }
                            </div>
                            {//this.state.productType !== 'MA' &&
                                <div className={this.state.productType === 'MA' ? "inline px-2 invisible" : "inline px-2"}>
                                    <label>amortization :&nbsp;</label>
                                    <label>more than&nbsp;</label>
                                    <Field className="form-control inline inline-50px" type="number" min="0" max="100" name="amortizationPercentMoreThan" />%
                                <label className="pl-2">less than&nbsp;</label>
                                    <Field className="form-control inline inline-50px" type="number" min="0" max="100" name="amortizationPercentLessThan" />%
                            </div>
                            }
                            {//this.state.productType !== 'MA' &&
                                <div className={this.state.productType === 'MA' ? "inline px-2 invisible" : "inline px-2"}>
                                    <label>total :&nbsp;</label>
                                    <label>more than&nbsp;</label>
                                    <Field className="form-control inline inline-50px" type="number" min="0" name="totalCountMoreThan" />
                                    <label className="pl-2">less than&nbsp;</label>
                                    <Field className="form-control inline inline-50px" type="number" min="0" name="totalCountLessThan" />
                                </div>
                            }
                            <div className="inline">
                                <button className="button" type="submit">Search</button>
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

export default ProductFilter