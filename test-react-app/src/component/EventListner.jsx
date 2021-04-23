import { EventSourcePolyfill } from 'event-source-polyfill';
import authHeader from '../service/AuthHeader';
import React, { useEffect, useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import { Link, withRouter } from 'react-router-dom'

function EventListner() {
   
    const [listening, setListening] = useState(false);
    const [data, setData] = useState([]);
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
                console.log("connection opened event = " + JSON.stringify(event))
                //console.log("connection opened event.target = " + JSON.stringify(event.target))
            }

            eventSource.onmessage = (event) => {
               // console.log("result = ", event.data);
                console.log("result event = ", JSON.stringify(event));
               // console.log("result event.target = ", JSON.stringify(event.target));
                toast(event.data, { position: "bottom-right", hideProgressBar: true, autoClose: false});
               // setData(old => [...old, event.data])
            }

           /* eventSource.onkeepalive = (event) => {
                console.log("result = ", event.data);
                //console.log("result event = ", JSON.stringify(event));
              //  console.log("result event.target = ", JSON.stringify(event.target));
                toast(event.data, { position: "bottom-right", hideProgressBar: true });
                // setData(old => [...old, event.data])
            }*/

            //keep alive pings
            eventSource.addEventListener("keepalive", (event) => {
                console.log("show data = "+event.data)//(JSON.stringify(event.data)))
                //toast(event.data, { position: "bottom-right", hideProgressBar: true });
            })


            //Amortized
            eventSource.addEventListener("amortized", (event) => {
                console.log("show data = " + event.data);//(JSON.stringify(event.data)))
                toast(ToastWithLink("Amortization", "fully amortized inventories", "/productdetails?Filter.ids=" + getListString(event.data)), { position: "bottom-right", autoClose: false });
            })

            //AllDiscarded
            eventSource.addEventListener("alldiscarded", (event) => {
               // console.log("show data = " + event.data);//(JSON.stringify(event.data)))
                toast(ToastWithLink("Deliveries", "all discarded deliveries", "/deliveries?Filter.discarded=true&Filter.ids=" + getListString(event.data)), { position: "bottom-right", autoClose: false });
            })

            //EmptyDeliveries
            eventSource.addEventListener("emptydeliveries", (event) => {
                // console.log("show data = " + event.data);//(JSON.stringify(event.data)))
                toast(ToastWithLink("Deliveries", "empty deliveries", "/deliveries?Filter.empty=true&Filter.ids=" + getListString(event.data)), { position: "bottom-right", autoClose: false });
            })

            //cityRequest
            eventSource.addEventListener("cityrequest", (event) => {
                // console.log("show data = " + event.data);//(JSON.stringify(event.data)))
                toast(ToastWithLink("City Request", "new Register with city Request", "/pendingrequests?refresh"), { position: "bottom-right", autoClose: false });
            })


            eventSource.onerror = (event) => {
                // console.log("event error = "+JSON.stringify(event))
               // console.log("error :  = " + JSON.stringify(event))
                //console.log("error : error state = "+event.target.readyState)
                if (event.target.readyState === EventSource.CLOSED) {
                    console.log('eventsource not closed (' + event.target.readyState + ')')
                }
                //eventSource.close();
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

    {/* <div >
          <AppInstructor/>
      </div>*/}
    //);
}

export default EventListner;
