import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import CustomSelect from './CustomSelect';

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
                <div className="px-3">
                <Formik
                        initialValues={{ all, firstNames, firstName, lastNames, lastName, userNames, userName, emails, email }}
                        onSubmit={this.onSubmit}                       
                        enableReinitialize={true}
                   
                >
                    {({ props, setFieldValue }) => (
                        <Form >
                            <fieldset className="border">
                                <div className="row pt-3 pb-3">
                                 <div className="col-12">
                                 <div className="form-inline">

                                    <div className="form-group col-3">                                   
                                    <label className="pr-3">first name :</label>
                                    <div className="flex-grow-1">
                                    <CustomSelect   
                                        items={firstNames}
                                        value={firstName}
                                        onChange={(selected) => setFieldValue("firstName", selected.value)}
                                    />
                                    </div>
                                    </div>

                                    <div className="form-group col-3">
                                    <label className="pr-3">last name :</label>
                                    <div className="flex-grow-1">
                                    <CustomSelect
                                        items={lastNames}
                                        value={lastName}
                                        onChange={(selected) => setFieldValue("lastName", selected.value)}
                                    />
                                    </div>
                                    </div>
                                   
                                    <div className="form-group col-3">   
                                    <label className="pr-3">user name :</label>
                                    <div className="flex-grow-1">
                                                    <CustomSelect  
                                                        name="userNames"
                                       items={userNames}
                                        value={userName}
                                        onChange={(selected) => setFieldValue("userName", selected.value)}
                                    />
                                    </div>
                                    </div>
                                       
                                   <div className="form-group col-3">  
                                   <label className="pr-3">email :</label>
                                   <div className="flex-grow-1">
                                   <CustomSelect
                                      items={emails}
                                      value={email}
                                      onChange={(selected) =>setFieldValue("email", selected.value)}
                                   />
                                   </div>
                                   </div>

                                 </div>
                                 </div>
                                </div>    
                                <div className="col-12 d-inline-flex justify-content-end pr-5">
                                    <button className="btn btn-success px-5 m-3" type="submit">Search</button>
                                    <button className=" btn btn-warning px-5 m-3" type="button" onClick={this.resetForm}>reset</button>
                                </div>
                            </fieldset>
                         </Form>
                       )
                    }
                 </Formik>
           </div>           
        )
    }
}

export default UserFilter