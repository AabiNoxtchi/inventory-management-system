import React, { Component } from 'react';
import Select from 'react-select';
import '../myStyles/Paging.css'


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
        if (selectedOption.value == this.state.selectedOption.value) return;
        //this.setState({ selectedOption: selectedOption })
        let path = window.location.pathname;
        let search = window.location.search; //this.props.search || window.location.search;
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
                   /* if (i === searchItems.length - 1)
                        newPath += searchItems[i]
                    else*/
                        newPath += searchItems[i] + '&'
            }
            newPath = path + '?' + newPath + this.props.prefix + '.itemsPerPage=' + selectedOption.value;
        }
        window.location.href = newPath;
    }

    onPageClicked(pageNumber) {
        if (pageNumber == this.props.page) return;
        if(pageNumber < 0)  pageNumber=0;
        let path = window.location.pathname;
        let search = window.location.search; //this.props.search || window.location.search;
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

        const current = this.props.page;
        const { selectedOption } = this.state;
        const numrows = this.props.pagesCount;
        const pageNumbers = [];
        if (numrows < 10)
            for (let i = 1; i <= numrows; i++) {
                pageNumbers.push(<li key={i} class={current + 1 == i ? "page-item active" : "page-item"}> <a class="page-link" href="#" onClick={() => this.onPageClicked(i - 1)}>{i}</a></li >);
            }
        else {
            for (let i = 1; i <= 4; i++) {
                pageNumbers.push(<li key={i} class={current + 1 == i ? "page-item active" : "page-item"}><a class="page-link" href="#" onClick={() => this.onPageClicked(i - 1)}>{i}</a></li>);
            }
            if(current + 1 > 4 && current + 1 < numrows-3 )
                pageNumbers.push(<li key={current + 1} class="page-item active" ><a class="page-link" href="#" onClick={() => this.onPageClicked(current + 1)}>{current + 1}</a></li>)
                
            for (let i = numrows - 3; i <= numrows; i++) {
                pageNumbers.push(<li key={i} class={current + 1 == i ? "page-item active" : "page-item"}><a class="page-link" href="#" onClick={() => this.onPageClicked(i - 1)}>{i}</a></li>);
            }
        }
      
        let begining = this.props.page * this.props.itemsPerPage;
        let ending = Number(this.props.page+1) * Number(this.props.itemsPerPage);       
        begining = this.props.itemsCount >= begining + 1 ? begining += 1 : 0;
        ending = this.props.itemsCount < ending ? ending = this.props.itemsCount : ending;

        return (            
            <div className=" d-inline-flex justify-content-end flex-grow-1">
                <label className="pager-label">showing&nbsp;{begining}-{ending}&nbsp; of &nbsp; {this.props.itemsCount}</label>               
                <Select
                    className="select"
                    value={selectedOption}
                    onChange={this.onCountChange}
                    options={options}
                    placeholder={"showing..."}
                />
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
            )
    }
}

export default PaginationComponent