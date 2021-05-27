import logo from './logo.svg';
import './App.css';
import AppInstructor from './component/AppInstructor';
import React, { useEffect, useState } from 'react'

function App() {

    const [userLoggedIn, setUserLoggedIn] = useState();
    return (
        <div >
            <AppInstructor
            userLoggedIn={userLoggedIn} loggedIn={(value) => setUserLoggedIn(value)} />
            
        </div>
    );
      
}

export default App;
