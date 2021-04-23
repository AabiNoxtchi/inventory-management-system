
class Functions {

    convertDate(date) { // to string 

        if (!date || date == 'undefined') return null;

            date = (new Date(date));
            date.setHours(date.getHours() - date.getTimezoneOffset() / 60);
            date = date.toISOString();
            date = date.substring(0, date.indexOf('T'))

            return date;       
    }

    getRandom() {       
           let uuid = ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, c =>
                (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
        );

        return uuid;
    }

    getSubmitPath(path, search, prefix, values, onNewSearch, history) {
       
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

       
        Object.entries(values).map(([key, value]) => {

            if (!key.endsWith("s") && value && value != 'undefined') {
                if ((values.productType === 'STA' && key === 'amortizationPercentMoreThan') ||
                    (values.productType === 'STA' && key === 'amortizationPercentLessThan') ||
                    (key === 'maxmore') ||
                    (key === 'minless') ||
                    (key === 'maxtotal') ||
                    (key === 'mintotal')) { }
                else if (key.startsWith('date')) {
                    value = (new Date(value)).toISOString();
                    value = value.substring(0, value.indexOf('T'))                   
                    newPath += prefix + '.' + key + '=' + value + '&'
                }
                else if (key.startsWith('phoneNumber')) {
                    if (value.startsWith('+')) value = value.replace('+', '%2B');
                    newPath += prefix + '.' + key + '=' + value + '&'
                }
                else if (key == 'ddcnumber') {
                    newPath += prefix + '.' + 'dDCnumber' + '=' + value + '&'
                }
                else if (key == 'givenAfter' || key == 'returnedBefore') {
                        value = this.convertDate(value);//(new Date(value)).toISOString();
                        newPath += prefix + '.' + key + '=' + value + '&'
                    }
                else { newPath += prefix + '.' + key + '=' + value + '&' }
            }
        })
        newPath = newPath.substring(0, newPath.length - 1);
        newPath = '?' + newPath;
        newPath = onNewSearch ? newPath : path + newPath;
       onNewSearch ? onNewSearch(newPath) : history ? history.push(newPath) : window.location.href = newPath;
    }
                   

}

export default new Functions()