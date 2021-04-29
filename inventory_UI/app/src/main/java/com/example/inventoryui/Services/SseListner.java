package com.example.inventoryui.Services;

import android.content.Context;

import androidx.annotation.WorkerThread;

import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

import okhttp3.Request;
import okhttp3.Response;

public class SseListner {

    private ServerSentEvent.Listener listener;
    private ServerSentEvent sse;
    private String authToken;
    private Context context;
    private SseHandler sseHandler;

    private static SseListner instance;

    final private String url ;

    private SseListner(Context context, String authToken, String url) {

        this.context = context;
        this.authToken=authToken;
        this.url = url + "/manager/subscribeMobileClient";
    }

    public static synchronized SseListner getInstance(Context context,String authToken,String url) {
        if (instance == null) {
            instance = new SseListner(context, authToken, url);
        }
        return instance;
    }

    public void startOksse(){

        listener = new ServerSentEvent.Listener() {

            @Override
            public void onOpen(ServerSentEvent sse, Response response) {
            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
               handle(event, message);
            }

            @WorkerThread
            @Override
            public void onComment(ServerSentEvent sse, String comment) {
                // When a comment is received
            }

            @WorkerThread
            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                return true; // True to use the new retry time received by SSE
            }

            @WorkerThread
            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, Response response) {
                return true; // True to retry, false otherwise
            }

            @WorkerThread
            @Override
            public void onClosed(ServerSentEvent sse) {
                sse.close();
            }

            @Override
            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                return null;
            }
        };

        Request request = new Request.Builder().url(url).addHeader("Authorization","Bearer "+ authToken).build();
        OkSse okSse = new OkSse();
        sse = okSse.newServerSentEvent(request, listener);

    }

    public void close(){
        if(sse!=null)
            sse.close();

        instance = null;
    }


    private void handle(String event,String message){
        if(sseHandler==null)
             sseHandler = SseHandler.getInstance(context);
        sseHandler.handle(event, message);
    }

}
