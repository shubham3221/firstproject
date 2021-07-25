package com.example.s_tools;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.example.s_tools.chatting.ChatKaro;
import com.example.s_tools.testing.DownloadActivity;

public class NotificationHelper {

   public NotificationCompat.Builder notificationBuilder;
    public NotificationManager notificationManager;


    public void addNotification(Context context,int notificationID) {

        String CHANNEL_ID="my_channel_01";
        CharSequence name="my_channel";
        String Description="This is my channel";


        notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance=NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel=new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // mChannel.enableVibration(true);
            //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(mChannel);
            }

        }


        Intent resultIntent=new Intent(context, DownloadActivity.class);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(context);
        stackBuilder.addParentStack(DownloadActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent=stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        notificationBuilder=new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(resultPendingIntent)
                .setOngoing(true).setAutoCancel(true).setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("DOWNLOADING").setContentText("0%");
        notificationBuilder.setProgress(100, 0, true);

        //Yes intent

//            Intent yesReceive = new Intent();
//            yesReceive.setAction("pause");
//            yesReceive.putExtra("id",NOTIFICATION_ID);
//            PendingIntent pendingIntentYes = PendingIntent.getBroadcast(mContext, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            Intent cancel = new Intent();
//            yesReceive.putExtra("id",NOTIFICATION_ID);
//            cancel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            cancel.setAction("cancel");
//            PendingIntent pendingIntentcancel = PendingIntent.getBroadcast(mContext, 12345, cancel, PendingIntent.FLAG_UPDATE_CURRENT);
//            notificationBuilder.addAction(0, "pause", pendingIntentYes);
//            notificationBuilder.addAction(0, "cancel", pendingIntentcancel);
        if (notificationManager != null) {
            notificationManager.notify(notificationID, notificationBuilder.build());
        }

    }
    public void showChatnotifiaction(Context context) {

        String CHANNEL_ID="my_channel_01";
        CharSequence name="my_channel";
        String Description="You have received message";


        notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance=NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel=new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // mChannel.enableVibration(true);
            //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(mChannel);
            }

        }


        Intent resultIntent=new Intent(context, ChatKaro.class);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ChatKaro.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent=stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        notificationBuilder=new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(resultPendingIntent)
                .setAutoCancel(true).setSmallIcon(R.drawable.myapp)
                .setContentTitle(Description);
        if (notificationManager != null) {
            notificationManager.notify(10001, notificationBuilder.build());
        }

    }

    public void startDownload(String status, int percentage, boolean indeterminate, int notificationID) {
        new Thread(() -> {
            if (notificationBuilder != null) {
                notificationBuilder.setContentText(status);
                notificationBuilder.setProgress(100, percentage, indeterminate);
                notificationManager.notify(notificationID, notificationBuilder.build());
            }
        }).start();

    }

    public void notifResult(boolean success, String path, int notificationID) {
        String statusText;
        int resId;
//        int resId=success ? android.R.drawable.stat_sys_download_done : android.R.drawable.stat_notify_error;
        if (success) {
            statusText="Download complete";
            resId=android.R.drawable.stat_sys_download_done;
        } else {
            statusText="failed";
            resId=android.R.drawable.stat_notify_error;
        }
        notificationBuilder.setContentTitle(path);
        notificationBuilder.setContentText(statusText);
        notificationBuilder.setSmallIcon(resId);
        notificationBuilder.setOngoing(false);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setProgress(0, 0, false);
        notificationManager.notify(notificationID, notificationBuilder.build());
    }
}