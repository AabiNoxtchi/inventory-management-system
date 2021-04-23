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

  /*const [listening, setListening] = useState(false);
    const [data, setData] = useState([]);
    let eventSource = undefined;

    useEffect(() => {
        if (!listening) {
            eventSource = new EventSource("http://localhost:8080/api/inventory/manager/subscribe");

            eventSource.onopen = (event) => {
                console.log("connection opened")
            }

            eventSource.onmessage = (event) => {
                console.log("result", event.data);
                setData(old => [...old, event.data])
            }

            eventSource.onerror = (event) => {
                console.log(event.target.readyState)
                if (event.target.readyState === EventSource.CLOSED) {
                    console.log('eventsource closed (' + event.target.readyState + ')')
                }
                eventSource.close();
            }

            setListening(true);
        }

        return () => {
            eventSource.close();
            console.log("eventsource closed")
        }

    }, [])

    return (
        <div className="">
            <header className="App-header">
                Received Data
        {data.map(d =>
                    <span key={d}>{d}</span>
                )}
            </header>
        </div>
    );*/

      
}

export default App;
