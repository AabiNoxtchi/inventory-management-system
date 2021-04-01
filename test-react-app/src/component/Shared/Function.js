
class Function {


    getErrorMsg(error) {

        /* let msg = error.response && typeof error.response.data == 'string' ? error.response.data :
                        error.response.data.message ? error.response.data.message : error;*/
        /* let errormsg = error.response && error.response.data ?
                       error.response.data.message ? error.response.data.message : error.response.data : error + '';*/
        /* let msg = error.response && typeof error.response.data == 'string' ? error.response.data :
               error.response.data.message ? error.response.data.message : error;*/
        /* let errormsg = error.response && error.response.data ?
                        error.response.data.message ? error.response.data.message : error.response.data : error + '';*/
        /* let msg = "" + error.response && typeof error.response.data == 'string' ?
                   error.response.data : error.response.data.errors ?
                       error.response.data.errors[0].defaultMessage : error.response.data.message ?
                           error.response.data.message : error;*/
        // let errormsg = error.response && error.response.data ? error.response.data.message : error + '';
        //"errors occured : " + error + " !!!";
        /* let errormsg = error.response && error.response.data ?
             error.response.data.message ? error.response.data.message : error.response.data : error + '';*/
        /* let errormsg = error.response && error.response.data ?
             error.response.data.message ? error.response.data.message : error.response.data : error + '';*/
        /* let errormsg = error.response && error.response.data ?
                       error.response.data.message ? error.response.data.message : error.response.data : error + '';*/

        let msg = '' + error == 'Error: Request failed with status code 401' ? 'need to login again !!!' :
            error.response && error.response.data && typeof error.response.data == 'string' ?
            error.response.data : error.response && error.response.data && typeof error.response.data == 'object' ?
                error.response.data.errors ?
                    error.response.data.errors[0].defaultMessage : error.response.data.message != null ?
                        error.response.data.message :
                       /* error.response && error.response.data && typeof error.response.data == 'object'
                        && error.response.data.message == null ?*/ 'errors found !!!' : error+'';

       /* let msg = "" + error &&
            // error.response && error.response.data && typeof error.response.data == 'string' ?
            //error.response.data :
            error.response && error.response.data ?
            error.response.data.errors ?
                error.response.data.errors[0].defaultMessage : error.response.data.message ?
                    error.response.data.message :
                    error : 'something went wrong !!!';*/
        console.log("msg = " + msg);
        return msg;
    }

    getDate() { // new date 
        //let g = new Date();
      /*  let date = new Date();
       date =  date.setHours(date.getHours() - date.getTimezoneOffset() / 60);
        //console.log("g after turn = " + g);*/

         let date = new Date();       
        date.setHours(date.getHours() - date.getTimezoneOffset() / 60);
        console.log("date after turn = " + date);
        return date;
    }

    convertDate(date) { // to string 
        if (date && date != 'undefined') {
            // let date = 
            date = (new Date(date));
            date.setHours(date.getHours() - date.getTimezoneOffset() / 60);
            date = date.toISOString();
            date = date.substring(0, date.indexOf('T'))
            return date;
        } else return '';
    }

    /*showError(time) {
       /* let time = 10;
        this.setState({
            errormsg: msg,
        })*/
    /*    this.myInterval = setInterval(() => {
            time = time - 1;
            if (time == 0) {
                return true;
                clearInterval(this.myInterval)
            }
        }, 1000)
    }

    componentWillUnmount() {
        clearInterval(this.myInterval)
    }*/
}

export default new Function()