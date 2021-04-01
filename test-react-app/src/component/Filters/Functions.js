
class Functions {
    convertDate(date) { // to string 
        if (!date || date == 'undefined') return null;
            // let date = 
            date = (new Date(date));
            date.setHours(date.getHours() - date.getTimezoneOffset() / 60);
            date = date.toISOString();
            date = date.substring(0, date.indexOf('T'))
            return date;       
    }

    getRandom() {
       // console.log("get random called");
           let uuid = ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, c =>
                (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
            );
       

      // console.log("uuid = "+uuid);
        return uuid;
    }

   
   /* getFilteredList = ( list, value) => {
        let filtered = [];
        console.log("value = "+value)
        if (value == null || value == 'undefined') filtered = list;
        for (let i = 0; i < list.length; i++) {
            console.log("list value = " + (list[i].value))
            console.log("list value == ''"+(list[i].value ==''))
            if (list[i].filterBy == value || list[i].value =='') {
                filtered.push(list[i])
            }
        }
        return filtered;
    }*/
                                       

}

export default new Functions()