package com.example.inventoryui.DataAccess;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.WorkerThread;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.inventoryui.Activities.Products.ProductsMainActivity;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class SseListner {

    String TAG="MyActivity_sseListner";
    private ServerSentEvent.Listener listener;
    private ServerSentEvent sse;
    private String authToken;
    Context context;
    private String Discarded_Channel_Id = "auto_discarded_products";
    private boolean isCreated_Discarded_Channel_Id = false;
    private int notification_id=0;
    ArrayList<Long> productsIds;
    String discardedProductsIdsFromIntent = "discardedProductsIds";

    private ObjectMapper om ;

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
                handle(event,message);
                output(event+" : "+message);
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

    private void handle(String event,String message){


        final String newMsg=prepareMsg(event, message);

        if( ((AuthenticationManager) this.context.getApplicationContext()).isForground() )
        {
            if(AuthenticationManager.getActiveActivity()==null)
                delayOneSecond(1000);
            if(AuthenticationManager.getActiveActivity()==null){
                createNotification(event,newMsg,message);
            }else if(AuthenticationManager.getActiveActivity().getClass().getName()
                    .equals(ProductsMainActivity.class.getName())){

                ((ProductsMainActivity)AuthenticationManager.getActiveActivity()).updateUifromThread(event,newMsg,message);

            }else{
                AuthenticationManager.getActiveActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AuthenticationManager.getActiveActivity(), newMsg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }else{
            createNotification(event,newMsg,message);
        }
    }

    private void delayOneSecond(long millis)  {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String prepareMsg(String event, String message) {

        List<String> ids = Arrays.asList(message.split(","));
       /* if(om==null) om=new ObjectMapper();
        productsIds = null;
        try {
            productsIds = om.readValue(message,new TypeReference<ArrayList<Long>>(){});

        } catch (IOException e) {
            e.printStackTrace();
        }*/
       // int count = productsIds!=
        int count = ids!=null?ids.size():0;
        return count+" products were "+event;
    }

    private void createNotification(String event, String newMsg, String message) {
        if(event=="discarded"&&!isCreated_Discarded_Channel_Id)
           createNotificationChannel(event);

        Intent resultIntent = new Intent(this.context, ProductsMainActivity.class);
        resultIntent.putExtra(discardedProductsIdsFromIntent, message);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, Discarded_Channel_Id)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(event)
                .setContentText(newMsg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        builder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);

        notificationManager.notify(notification_id, builder.build());
        notification_id++;
    }

    private void createNotificationChannel(String event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = event;
            String description = event;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            if(event=="discarded"&&!isCreated_Discarded_Channel_Id)
                isCreated_Discarded_Channel_Id = true;
            NotificationChannel channel = new NotificationChannel(Discarded_Channel_Id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = this.context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
