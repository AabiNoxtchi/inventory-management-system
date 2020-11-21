package com.example.inventoryui.Services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

import okhttp3.Request;
import okhttp3.Response;

public class SseListner {

    private static final String TAG="MyActivity_sseListner";
    private ServerSentEvent.Listener listener;
    private ServerSentEvent sse;
    private String authToken;
    private Context context;
    private SseHandler sseHandler;

    private static SseListner instance;

    public String BASE_URL = "http://192.168.1.4:8080/api/inventory/manager/products";

    private SseListner(Context context, String authToken) {
        this.context = context;
        this.authToken=authToken;
    }

    public static synchronized SseListner getInstance(Context context,String authToken) {
        if (instance == null) {
            instance = new SseListner(context, authToken);
        }
        return instance;
    }


    public void startOksse(){
        listener=new ServerSentEvent.Listener() {
            @Override
            public void onOpen(ServerSentEvent sse, Response response) {
                // When the channel is opened
            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                output(event+" : "+message);
                handle(event,message);

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
                // Channel closed
            }

            @Override
            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                return null;
            }
        };

        String path=  BASE_URL ;
        Request request = new Request.Builder().url(path).addHeader("Authorization","Bearer "+authToken).build();
        OkSse okSse = new OkSse();
        sse = okSse.newServerSentEvent(request, listener);

    }

    public void close(){
        if(sse!=null)
            sse.close();
    }

    private void output(final String txt) {
        Log.i(TAG, txt);
    }

    private void handle(String event,String message){
        if(sseHandler==null)
             sseHandler = SseHandler.getInstance(context);
        sseHandler.handle(event,message);
    }
}
