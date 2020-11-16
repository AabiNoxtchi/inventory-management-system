package com.example.inventoryui.Services;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.inventoryui.Activities.Products.ProductsMainActivity;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.R;

import java.util.Arrays;
import java.util.List;

public class SseHandler {

    private static final String TAG = "MyActivity_SseHandler";

    private  String Discarded_Channel_Id = "auto_discarded_products";
    private  boolean isCreated_Discarded_Channel = false;
    private static int notification_id = 0;
    private  String discardedProductsIdsFromIntent = "discardedProductsIds";
    private  Context context;

    private static SseHandler instance;

    private SseHandler(Context context) {
        this.context = context;
    }

    public static synchronized SseHandler getInstance(Context context) {
        if (instance == null) {
            instance = new SseHandler(context);
        }
        return instance;
    }

    public void handle(String event,String message){

        final String newMsg = prepareMsg(event, message);
        if( ((AuthenticationManager) context.getApplicationContext()).isForground() ){
            if(AuthenticationManager.getActiveActivity()==null)
                delay(1000);
            if(AuthenticationManager.getActiveActivity()==null){
                createNotification(event,newMsg,message);
            }else {
                Activity activity = AuthenticationManager.getActiveActivity();
                if (activity.getClass().getName()
                        .equals(ProductsMainActivity.class.getName())) {
                    ((ProductsMainActivity)activity).updateUifromThread(event, newMsg, message);

                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AuthenticationManager.getActiveActivity(), newMsg, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }else{
            createNotification(event,newMsg,message);
        }
    }

    private void delay(long millis)  {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String prepareMsg(String event, String message) {

        List<String> ids = Arrays.asList(message.split(","));
        int count = ids!=null?ids.size():0;
        return count+" products were "+event;
    }

    private void createNotification(String event, String newMsg, String message) {
        if(event.equals("discarded") && !isCreated_Discarded_Channel) {
            createNotificationChannel(event);
        }

        Intent resultIntent = new Intent(context, ProductsMainActivity.class);
        resultIntent.putExtra(discardedProductsIdsFromIntent, message);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Discarded_Channel_Id)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(event)
                .setContentText(newMsg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        builder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(notification_id, builder.build());
        notification_id++;
    }

    private void createNotificationChannel(String event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//targets API>=26//
            CharSequence name = event;
            String description = event;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Discarded_Channel_Id, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager =context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            if(event=="discarded"&&!isCreated_Discarded_Channel)
                isCreated_Discarded_Channel = true;
        }
    }

}
