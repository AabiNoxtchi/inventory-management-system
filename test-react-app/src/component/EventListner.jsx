import { EventSourcePolyfill } from 'event-source-polyfill';
import authHeader from '../service/AuthHeader';
import React, { useEffect, useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import { Link, withRouter } from 'react-router-dom'

function EventListner() {
   
    const [listening, setListening] = useState(false);   
    let eventSource = undefined;

    const ToastWithLink = (title, msg, link) => (
          <div>
              <h5>{title}</h5>
           <Link to={link}>{msg}</Link>
        </div>
    );

    const getListString = (list) => {
        let str = list.toString();
        str = str.substring(1, str.length - 1)
        return str
    }

    useEffect(() => {
        if (!listening) {
            var EventSource = EventSourcePolyfill; 
            eventSource = new EventSource("http://localhost:8080/api/inventory/manager/subscribeWebClient", { headers: authHeader() });

            eventSource.onopen = (event) => {                
            }

            eventSource.onmessage = (event) => {              
                toast(event.data, { position: "bottom-right", hideProgressBar: true, autoClose: false});              
            }

            //keep alive pings
            eventSource.addEventListener("keepalive", (event) => {               
            })

            //Amortized
            eventSource.addEventListener("amortized", (event) => {
                toast(ToastWithLink("Inventories", "fully amortized inventories",
                    "/productdetails?Filter.amortized=true&Filter.ids=" + getListString(event.data)),
                    { position: "bottom-right", autoClose: false });
            })

            // UpdatedAmortizations
            eventSource.addEventListener("updatedamortizations", (event) => {
                toast(ToastWithLink("Inventories", "total amortization updated for inventories",
                    "/productdetails?Filter.updated=true&Filter.ids=" + getListString(event.data)),
                    { position: "bottom-right", autoClose: false });
            })

            //AllDiscarded
            eventSource.addEventListener("alldiscarded", (event) => {
                toast(ToastWithLink("Deliveries", "all discarded deliveries",
                    "/deliveries?Filter.discarded=true&Filter.ids=" + getListString(event.data)),
                    { position: "bottom-right", autoClose: false });
            })

            //EmptyDeliveries
            eventSource.addEventListener("emptydeliveries", (event) => {
                toast(ToastWithLink("Deliveries", "empty deliveries",
                    "/deliveries?Filter.empty=true&Filter.ids=" + getListString(event.data)),
                    { position: "bottom-right", autoClose: false });
            })

            //cityRequest
            eventSource.addEventListener("cityrequest", (event) => {
                toast(ToastWithLink("City Request",
                    "new Register with city Request", "/pendingrequests?refresh"),
                    { position: "bottom-right", autoClose: false });
            })

            eventSource.onerror = (event) => {
                if (event.target.readyState === EventSource.CLOSED) {
                }
            }

            setListening(true);
        }

        return () => {
            eventSource.close();
            console.log("eventsource closed")
        }

    }, [])
  

    return (
        <></>
    );
  
}

export default EventListner;
