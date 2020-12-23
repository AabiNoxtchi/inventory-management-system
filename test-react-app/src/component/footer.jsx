import React, { Component } from 'react'


class FooterComponent extends Component {
    render() {

        return (
            <div className="pt-3 px-3">

                <hr />
                <footer>
                    <p >&copy; {new Date().getFullYear()} - Inventory Management System</p>
                </footer>
            </div>



        )
    }
}

export default FooterComponent