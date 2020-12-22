import React, { Component } from 'react';
import Select from 'react-select';


class PaginationComponent extends Component {
    constructor(props) {
        super(props)
        this.state = {
            selectedOption: { value: `${props.itemsPerPage}`, label: `${props.itemsPerPage}` }
        }

        this.onPageClicked = this.onPageClicked.bind(this)
        this.onCountChange = this.onCountChange.bind(this)

    }

    onCountChange = selectedOption => {

        this.setState({ selectedOption: selectedOption })
        let path = window.location.pathname;
        let search = window.location.search;
        let newPath = ``;

        if (search.length < 1) {

            newPath = path + `?${this.props.prefix}.itemsPerPage=${selectedOption.value}`;

        }
        else {
            while (search.charAt(0) === '?') {
                search = search.substring(1);
            }
            let searchItems = search.split('&');

            for (let i = 0; i < searchItems.length; i++) {

                if (searchItems[i].startsWith(this.props.prefix))
                    continue
                else
                    if (i === searchItems.length - 1)
                        newPath += searchItems[i]
                    else
                        newPath += searchItems[i] + '&'

            }
            newPath = path + '?' + newPath + this.props.prefix + '.itemsPerPage=' + selectedOption.value;

        }

        window.location.href = newPath;
    }

    onPageClicked(pageNumber) {

        if(pageNumber < 0)  pageNumber=0;
        let path = window.location.pathname;
        let search = window.location.search;
        let newPath = ``;

        if (search.length < 1) {

            newPath = path + `?${this.props.prefix}.page=${pageNumber}`;

        }
        else {
            while (search.charAt(0) === '?') {
                search = search.substring(1);
            }
            let searchItems = search.split('&');

            for (let i = 0; i < searchItems.length; i++) {

                if (searchItems[i].startsWith(this.props.prefix + '.page'))
                    continue
                else
                    if (i === searchItems.length-1)
                        newPath += searchItems[i]
                    else
                        newPath += searchItems[i]+'&'

            }
            newPath = path + '?' + newPath + this.props.prefix + '.page=' + pageNumber;
           
        }
        window.location.href = newPath;
    }


    render() {

        const options = [
            { value: '5', label: '5'},
            { value: '10', label: '10' },
            { value: '20', label: '20' },
            { value: '50', label: '50' },
        ];


        const { selectedOption } = this.state;

       const numrows = this.props.pagesCount;

        const pageNumbers = [];
        for (let i = 1; i <= numrows; i++) {
            pageNumbers.push(<li key={i} class="page-item"><a class="page-link" href="#" onClick={() => this.onPageClicked(i-1)}>{i}</a></li>);
        }

        return (

            <div class="row ">
                <div className="col-12 d-inline-flex justify-content-end ">
                   

                    <div className="form-group pl-5 col-2">
                       
                            <div className="pl-3 flex-grow-1">
               
                    <Select
                        value={selectedOption}
                        onChange={this.onCountChange}
                        options={options}
                        placeholder={"showing..."}

                                />
                            </div>
                   
                    </div>
               
                    <div class="form-inline">
                    <nav aria-label="Page navigation example" >               
                    <ul class="pagination">
                            <li class="page-item"><a class="page-link" aria-label="Previous" href="#" onClick={() => this.onPageClicked(this.props.page - 1)} >
                                <span aria-hidden="true">&laquo;</span>
                                <span class="sr-only">Previous</span>
                                </a></li>
                            <ul class="pagination">
                            {
                                pageNumbers
                            }
                            </ul>
                            <li class="page-item"><a class="page-link" aria-label="Next" href="#" onClick={() => this.onPageClicked(this.props.page + 1)}>
                                <span aria-hidden="true">&raquo;</span>
                                <span class="sr-only">Next</span>
                                </a></li>
                    </ul>
                    </nav>
                    </div>

                  </div>               
            </div>
           
            )
    }
}

export default PaginationComponent