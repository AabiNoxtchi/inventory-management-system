import React, { Component } from 'react'


class FooterComponent extends Component {
    render() {

        return (
            <div className="p-5">

                <hr />
                <footer>
                    <p >&copy; {new Date().getFullYear()} - Inventory Management System</p>
                </footer>
            </div>



        )
    }
}

export default FooterComponent