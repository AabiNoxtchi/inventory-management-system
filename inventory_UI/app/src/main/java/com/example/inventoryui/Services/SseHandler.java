package com.example.inventoryui.Services;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.inventoryui.Activities.HomeMainActivityMol;
import com.example.inventoryui.Activities.Inventory.InventoriesMainActivity;
import com.example.inventoryui.Activities.MainActivity;
import com.example.inventoryui.Models.AuthenticationManager;
import com.example.inventoryui.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SseHandler {

    Map<String, Boolean> eventChannels = new HashMap<String,Boolean>(){{

        put("amortized",false);
        put("UpdatedAmortizations",false);
        put("AllDiscarded",false);
        put("EmptyDeliveries",false);
        put("Message",false);
    }};
    Map<String, String> titles = new HashMap<String,String>(){{

        put("amortized","amortized");
        put("UpdatedAmortizations", "Updated Amortizations");
        put("AllDiscarded", "All Discarded");
        put("EmptyDeliveries", "Empty Deliveries");
        put("Message","Message");
    }};
    Map<String, Class> eventClasses = new HashMap<String, Class>(){{

        put("amortized",InventoriesMainActivity.class);
        put("UpdatedAmortizations",InventoriesMainActivity.class);
        put("AllDiscarded", HomeMainActivityMol.class);
        put("EmptyDeliveries",HomeMainActivityMol.class);
        put("Message", MainActivity.class);

    }};

    private static int notification_id = 0;

    private Context context;
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

    public void handle(String event, String message){
        if(eventClasses.get(event) == null)return;

        final String newMsg = prepareMsg(event, message);

        if( ((AuthenticationManager) context.getApplicationContext()).isForground() ){

            if(AuthenticationManager.getActiveActivity()==null)
                delay(1000);

            if(AuthenticationManager.getActiveActivity()==null){
                createNotification(event, newMsg, message);
            }else {

                Activity activity = AuthenticationManager.getActiveActivity();
                if( event.equalsIgnoreCase("amortized")) {
                    if (activity.getClass().getName()
                            .equals(InventoriesMainActivity.class.getName())) {
                        ((InventoriesMainActivity) activity).updateUifromThread(event, newMsg, message);

                    } else {
                         InventoriesMainActivity.takeEventMsg(event, newMsg, message);
                    }
                }else if(event.equalsIgnoreCase("UpdatedAmortizations")){
                    if (activity.getClass().getName()
                            .equals(InventoriesMainActivity.class.getName())) {
                        ((InventoriesMainActivity) activity).refresh(event, newMsg, message);

                    } else {
                       Toast.makeText(context,
                               "total amortization for some inventories has been updated .",
                               Toast.LENGTH_LONG).show();
                    }
                }else if(event.equalsIgnoreCase("EmptyDeliveries")){
                    showDialogAlert(event,  newMsg);
                }
                else if(event.equalsIgnoreCase("AllDiscarded")){
                    showDialogAlert(event,  newMsg);
                }
            }
        }else{
            createNotification(event, newMsg, message);
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

        int count = 0;
        List<String> ids;
        switch (event){
            case "amortized":
                 ids = Arrays.asList(message.split(","));
                count = ids!=null?ids.size():0;
                return count+" inventories were fully "+event;
            case "UpdatedAmortizations":
                 ids = Arrays.asList(message.split(","));
                count = ids!=null?ids.size():0;
                return count+" inventories were fully updated";
            case "AllDiscarded":
                ids = Arrays.asList(message.split(","));
                count = ids!=null?ids.size():0;
                return count+" deliveries contain all discarded inventories. if you wish to delete them";
            case "EmptyDeliveries":
                ids = Arrays.asList(message.split(","));
                count = ids!=null?ids.size():0;
                return count+" deliveries are empty. if you wish to delete them";
            case "Message":
                return message;
                default:
                    return null;
        }

    }

    private void createNotification(String event, String newMsg, String message) {

        if (event.equalsIgnoreCase("message") ||
                event.equalsIgnoreCase("UpdatedAmortizations"))return;

      Boolean eventChannel = eventChannels.get(event);
       if(eventChannel == null || eventChannel.equals(false)) {
            createNotificationChannel(event);
            eventChannels.put(event, true);
        }

        Intent resultIntent = new Intent(context, eventClasses.get(event));
        resultIntent.putExtra(event, message);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, event)
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //targets API >= 26 //

            CharSequence name = event;
            String description = event;

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(event, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showDialogAlert(final String event, final String newMsg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setTitle(titles.get(event)).setMessage(newMsg)
               .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

}
