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

  /*  <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>*/
      
}

export default App;
