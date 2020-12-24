import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';
import './Filter.css'

class UserFilter extends Component {
    constructor(props) {
        super(props)

        this.state = {
            all:props.all,
            firstNames: props.firstNames,
            firstName: props.firstName,
            lastNames: props.lastNames,
            lastName: props.lastName,
            userNames: props.userNames,
            userName: props.userName,
            emails: props.emails,
            email: props.email,
            prefix: props.prefix
        }

        this.onSubmit = this.onSubmit.bind(this)
        this.resetForm = this.resetForm.bind(this)
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
            if (!key.endsWith("s") && value && value.length>1) {
                newPath += prefix + '.' + key +'='+ value+'&'
            }
           
        })
        newPath = newPath.substring(0, newPath.length-1);
        newPath = path + '?' + newPath;
        console.log('newPath =' + newPath);

        window.location.href = newPath;
    }

    resetForm() {
        this.setState({
            all: '',
            firstName: '',
            lastName: '',
            userName: '',
            email: '',
        });
        console.log('in reset form ');
    }

    render() {  

        let { all, firstNames, firstName, lastNames, lastName, userNames, userName, emails, email } = this.state
        return (                        
               
                <Formik
                        initialValues={{ all, firstNames, firstName, lastNames, lastName, userNames, userName, emails, email }}
                        onSubmit={this.onSubmit}                       
                        enableReinitialize={true}
                >
                {({ props, setFieldValue }) => (
                    <Form className="filter-form">
                            <fieldset >
                                 <div className="inline">
                                 <label>first name&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-1-5"}
                                        items={firstNames}
                                        value={firstName}
                                        onChange={(selected) => setFieldValue("firstName", selected.value)}
                                />
                            </div>  
                            <div className="inline">
                                 <label >last name&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-1-5"}
                                        items={lastNames}
                                        value={lastName}
                                        onChange={(selected) => setFieldValue("lastName", selected.value)}
                                />
                            </div>
                                <div className="inline">
                                <label>user name&nbsp;</label>
                                <CustomSelect  
                                    className={"inline inline-2"}
                                                        name="userNames"
                                       items={userNames}
                                        value={userName}
                                        onChange={(selected) => setFieldValue("userName", selected.value)}
                                />
                            </div>
                            <div className="inline">
                                <label >email&nbsp;</label>
                                <CustomSelect
                                    className={"inline inline-2-5"}
                                      items={emails}
                                      value={email}
                                      onChange={(selected) =>setFieldValue("email", selected.value)}
                                />
                            </div>
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

export default UserFilter