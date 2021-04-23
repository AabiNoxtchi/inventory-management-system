
class Function {


    getErrorMsg(error) {

        let msg = '' + error == 'Error: Request failed with status code 401' ? 'Error : Unauthorized, need to login again !!!' :
            error.response && error.response.data && typeof error.response.data == 'string' ?
            error.response.data : error.response && error.response.data && typeof error.response.data == 'object' ?
                error.response.data.errors ?
                    error.response.data.errors[0].defaultMessage : error.response.data.message != null ?
                        error.response.data.message :
                        'errors found !!!' : error + '';

        if (typeof msg == 'string' && msg.startsWith('Failed to convert value'))
            msg = 'try again with correct values !!!'
        msg = typeof msg == 'string' && msg.indexOf("ConstraintViolationException") > -1 ? "error !!!" : msg
        msg = typeof msg == 'string' && msg.indexOf("Forbidden") > -1 ? "Error: Unauthorized !!!" : msg

        return msg;
    }

    getDate() { 

        let date = new Date();       
        date.setHours(date.getHours() - date.getTimezoneOffset() / 60);
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
   
}

export default new Function()