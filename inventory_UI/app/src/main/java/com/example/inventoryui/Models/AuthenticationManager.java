package com.example.inventoryui.Models;

import android.app.Application;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationManager extends Application {

    private User LoggedUser;
    private String authToken;



    public String getAuthToken() {
        return authToken;
    }



   public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User getLoggedUser() {
        return LoggedUser;
    }

    public void setLoggedUser(User loggedUser) {

            LoggedUser = loggedUser;
            if(this.LoggedUser!=null&& this.LoggedUser.getRole().equals(Role.ROLE_Mol))startOksse();
    }



    public void logout(){

        setLoggedUser(null);
        setAuthToken(null);

    }

    private void startOksse(){



        ServerSentEvent.Listener listener=new ServerSentEvent.Listener() {
            @Override
            public void onOpen(ServerSentEvent sse, Response response) {
                // When the channel is opened
            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                // When a message is received
                output(id+" "+event+" "+message);
               // output(event);
               // output(message);
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

        String path="http://192.168.1.2:8080/test";
        Request request = new Request.Builder().url(path).build();
        OkSse okSse = new OkSse();
        ServerSentEvent sse = okSse.newServerSentEvent(request, listener);



    }

    private void output(final String txt) {

        Log.i("Test",txt+"\n\n");
       /* runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });*/
    }


}
