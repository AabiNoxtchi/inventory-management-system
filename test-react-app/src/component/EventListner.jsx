import React, { Component } from 'react';
import { EventSourcePolyfill } from 'event-source-polyfill';
import authHeader from '../service/AuthHeader';

class EventListner extends Component {

    constructor(props) {
        super(props)
        // ... super, state, and columns ...

        //this.eventSource = new EventSource(, { withCredentials: true });
       // let EventSource = EventSourcePolyfill;
       // this.eventSource = new EventSourcePolyfill(`http://localhost:8080/api/inventory/manager/subscribe`, {
         //   headers: authHeader()
        //});
        this.eventSource = new EventSource("http://localhost:8080/api/inventory/manager/subscribe");
    }

   

    componentDidMount() {
        console.log("component did mount");
       //this.eventSource.onmessage = e => { console.log("msg from sse = "); }
        this.eventSource.onmessage = e =>
            console.log("msg from sse = ");//this.updateFlightState(JSON.parse(e.data));
    }
       

    render() {
       
            

           
        
        return (
            
            <>
                </>
            )
    }
}

export default EventListner