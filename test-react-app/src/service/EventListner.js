


//const eventSource = new EventSource("http://localhost:8080/api/inventory/manager/subscribe");
class EventListner {


    subscribe() {
       let eventSource = new EventSource("http://localhost:8080/api/inventory/manager/subscribe");



        //console.log("component did mount");
        //this.eventSource.onmessage = e => { console.log("msg from sse = "); }
        eventSource.onmessage = e =>
            console.log("msg from sse = ");//this.updateFlightState(JSON.parse(e.data));
    }
       
}

export default new EventListner()