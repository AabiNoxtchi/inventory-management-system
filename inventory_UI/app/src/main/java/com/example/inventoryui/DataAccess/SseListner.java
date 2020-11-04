package com.example.inventoryui.DataAccess;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.WorkerThread;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.inventoryui.R;
import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

import okhttp3.Request;
import okhttp3.Response;

public class SseListner {

    private ServerSentEvent.Listener listener;
    private ServerSentEvent sse;
    private String authToken;
    Context context;
    private String Discarded_Channel_Id = "auto_discarded_products";
    private boolean isCreated_Discarded_Channel_Id = false;
    private int notification_id=0;

    public SseListner(Context context,String authToken){
        this.context=context;
        this.authToken=authToken;
    }


    public void startOksse(){


        listener=new ServerSentEvent.Listener() {
            @Override
            public void onOpen(ServerSentEvent sse, Response response) {
                // When the channel is opened
            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {

                createNotification(event,message);

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

        String path="http://192.168.1.2:8080/manager/products";
        Request request = new Request.Builder().url(path).addHeader("Authorization","Bearer "+authToken).build();
        OkSse okSse = new OkSse();
        sse = okSse.newServerSentEvent(request, listener);

    }

    public void close(){
        if(sse!=null)
            sse.close();
    }

    private void output(final String txt) {

        Log.i("Test",txt+"\n\n");
    }

    private void createNotification(String event, String message) {

        if(!isCreated_Discarded_Channel_Id)
           createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, Discarded_Channel_Id)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(event)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(notification_id, builder.build());
        notification_id++;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "inventory_ui"; //getString(R.string.channel_name);
            String description = "description"; //getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            if(!isCreated_Discarded_Channel_Id)
                isCreated_Discarded_Channel_Id = true;
            NotificationChannel channel = new NotificationChannel(Discarded_Channel_Id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }




}
