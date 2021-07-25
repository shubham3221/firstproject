//package com.example.s_tools;
//
//import android.annotation.SuppressLint;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Build;
//import android.os.IBinder;
//import android.text.format.DateUtils;
//import android.util.Log;
//import android.widget.RemoteViews;
//
//import androidx.annotation.RequiresApi;
//import androidx.core.app.NotificationCompat;
//
//import com.download.library.DownloadImpl;
//import com.download.library.DownloadListener;
//import com.download.library.Extra;
//import com.example.s_tools.tools.Kbtomb;
//
//import org.greenrobot.eventbus.EventBus;
//import org.wlf.filedownloader.DownloadFileInfo;
//import org.wlf.filedownloader.FileDownloader;
//import org.wlf.filedownloader.base.Status;
//import org.wlf.filedownloader.file_download.http_downloader.HttpDownloader;
//import org.wlf.filedownloader.listener.OnRetryableFileDownloadStatusListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//import static com.example.s_tools.DownloadTesting.TAG;
//
//public class YourService extends Service implements OnRetryableFileDownloadStatusListener {
//    public static final String FOREGROUND_ID="FOREGROUND_CHANNEL_ID";
//    public static final String CHANNEL_ID="OREO_CHANNEL_ID";
//    NotificationCompat.Builder notification;
//    NotificationManager manager;
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
////        startForeground(5, get_notification());
//        // registerDownloadStatusListener
//        FileDownloader.registerDownloadStatusListener(this);
//        // continue all the download task if you hope that the service started, the download auto start too
////        FileDownloader.continueAll(fa);
//        manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    private Notification get_notification() {
//        Notification notification=new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_baseline_arrow_circle_down_24).setContentTitle("Download Service Running").setDefaults(NotificationCompat.DEFAULT_ALL).setPriority(NotificationCompat.PRIORITY_MIN).build();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel(FOREGROUND_ID);
//        }
//        return notification;
//    }
//
//
//    @Override
//    public void onFileDownloadStatusRetrying(DownloadFileInfo downloadFileInfo, int retryTimes) {
//        Log.e(TAG, "onFileDownloadStatusRetrying: " );
//        // send a notification or a broadcast if needed
////        Log.e(TAG, "onFileDownloadStatusRetrying: ");
////        notification.setContentText("Retrying: "+retryTimes+" tried");
////        notification.setProgress(100, 0, true);
////        manager.notify(downloadFileInfo.getId(), notification.build());
////        EventBus.getDefault().post(downloadFileInfo);
//    }
//
//    @Override
//    public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) {
////        Log.e(TAG, "onFileDownloadStatusWaiting: " + downloadFileInfo.getId());
////         send a notification or a broadcast if needed
////        EventBus.getDefault().post(downloadFileInfo);
//        Log.e(TAG, "onFileDownloadStatusWaiting: " );
//    }
//
//    @Override
//    public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {
//        Log.e(TAG, "onFileDownloadStatusPreparing: " );
//        // send a notification or a broadcast if needed
////        Log.e(TAG, "onFileDownloadStatusPreparing: ");
////        Intent pause=new Intent(this, MyBroadcast.class);
////        pause.setAction("pause");
////        pause.putExtra("url", downloadFileInfo.getUrl());
////        PendingIntent pause_intent=PendingIntent.getBroadcast(this, 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
////
////        Intent cancel=new Intent(this, MyBroadcast.class);
////        cancel.setAction("cancel");
////        cancel.putExtra("id", downloadFileInfo.getId());
////        cancel.putExtra("url", downloadFileInfo.getUrl());
////        PendingIntent cancel_intent=PendingIntent.getBroadcast(this, 0, cancel, PendingIntent.FLAG_UPDATE_CURRENT);
//
////
////        if (notification==null){
////            show_notification(downloadFileInfo);
////        }
////        notification.setContentText("Preparing");
////        notification.setProgress(100, 0, true);
////        manager.notify(downloadFileInfo.getId(), notification.build());
////        EventBus.getDefault().post(downloadFileInfo);
//    }
//
//    @Override
//    public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {
//        // send a notification or a broadcast if needed
////        Log.e(TAG, "onFileDownloadStatusPrepared: " + downloadFileInfo.getTempFilePath());
////        notification.setContentText("Starting Download");
////        notification.setProgress(100, 0, true);
////        manager.notify(downloadFileInfo.getId(), notification.build());
////        EventBus.getDefault().post(downloadFileInfo);
//    }
//
//    @Override
//    public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed, long remainingTime) {
////        if (downloadFileInfo.getFileSizeLong() >= 0) {
////            notification.setContentText("Remaining Time: " + DateUtils.formatElapsedTime(remainingTime==-1? 0:remainingTime));
////            notification.setProgress(100, (int) (downloadFileInfo.getDownloadedSizeLong() * 100.0F / downloadFileInfo.getFileSizeLong()), false);
////        } else {
////            notification.setProgress(100, 0, true);
////            notification.setContentText(Kbtomb.getFileSize(downloadFileInfo.getDownloadedSizeLong()));
////        }
////        manager.notify(downloadFileInfo.getId(), notification.build());
////        EventBus.getDefault().post(downloadFileInfo);
//        // send a notification or a broadcast if needed
//    }
//
//    @Override
//    public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {
//        pause_notification(downloadFileInfo);
//        stopService();
//        EventBus.getDefault().post(downloadFileInfo);
//
//    }
//
//    @SuppressLint("RestrictedApi")
//    @Override
//    public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {
//
//        RemoteViews contentView=new RemoteViews(getPackageName(), R.layout.custom_notification);
//        contentView.setImageViewResource(R.id.image, R.drawable.ic_baseline_cloud_done_24);
//        contentView.setTextViewText(R.id.title, downloadFileInfo.getFileName());
//        contentView.setTextViewText(R.id.text, "Download Completed");
//
//        notification.setContent(contentView);
//        notification.setAutoCancel(true);
//        notification.setProgress(0,0,false);
//        notification.setNotificationSilent();
//        notification.mActions.clear();
//        notification.setContentText("Download Complete");
//        notification.setSmallIcon(R.drawable.ic_baseline_cloud_done_24);
//        manager.notify(downloadFileInfo.getId(), notification.build());
//        stopService();
//        EventBus.getDefault().post(downloadFileInfo);
//        Log.e(TAG, "onFileDownloadStatusCompleted: " + downloadFileInfo.getId());
//    }
//
//    @SuppressLint("RestrictedApi")
//    @Override
//    public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason failReason) {
////        if (failReason.getType().equals(HttpDownloader.HttpDownloadException.TYPE_ETAG_CHANGED)) {
////            Log.e(TAG, "onFileDownloadStatusFailed: " + failReason.getType());
////            FileDownloader.delete(url, true, null);
////            FileDownloader.start(url);
////        } else {
////            notification.setContentText("Failed");
////            notification.mActions.clear();
////            notification.setSmallIcon(R.drawable.ic_baseline_close_24__black);
////            notification.setOngoing(false);
////            notification.setProgress(0, 0, false);
////            manager.notify(downloadFileInfo.getId(), notification.build());
////            stopService();
////        }
////        EventBus.getDefault().post(downloadFileInfo);
//        Log.e(TAG, "onFileDownloadStatusFailed: "+failReason.getType() );
//    }
//
//    private void stopService() {
//        boolean isrunning=false;
//        List<DownloadFileInfo> downloadFiles=FileDownloader.getDownloadFiles();
//        for (DownloadFileInfo info : downloadFiles) {
//            if (info.getStatus() == Status.DOWNLOAD_STATUS_DOWNLOADING || info.getStatus() == Status.DOWNLOAD_STATUS_PREPARED || info.getStatus() == Status.DOWNLOAD_STATUS_PREPARING) {
//                Log.e(TAG, "stopService: ");
//                isrunning=true;
//            }
//        }
//        if (isrunning) {
//            stopSelf();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.e(TAG, "onDestroy: ");
//        // unregisterDownloadStatusListener
//        FileDownloader.unregisterDownloadStatusListener(this);
//        // pause all the download task if you hope that the service stopped, the download auto stop too
//        FileDownloader.pauseAll();// pause all
//    }
//
//    @RequiresApi(api=Build.VERSION_CODES.O)
//    private void createNotificationChannel(String channal_id) {
//        Log.e(TAG, "createNotificationChannel: ");
//        NotificationChannel chan=new NotificationChannel(CHANNEL_ID, "Forground Channal", NotificationManager.IMPORTANCE_LOW);
//        chan.setLightColor(Color.RED);
//        chan.setSound(null, null);
//        chan.setShowBadge(false);
//        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//        NotificationManager manager1=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        manager1.createNotificationChannel(chan);
//    }
//
//    private void show_notification(DownloadFileInfo downloadFileInfo) {
//        String title=null;
//        if (downloadFileInfo.getFileName().length() > 20) {
//            if (downloadFileInfo.getFileName().contains(".")) {
//                title=downloadFileInfo.getFileName().substring(downloadFileInfo.getFileName().lastIndexOf("."));
//            }
//        }
//        Intent intent=new Intent(this, DownloadTesting.class);
//        PendingIntent contentIntent=PendingIntent.getActivity(this, 0, intent, 0);
//
//        Intent pause=new Intent(this, MyBroadcast.class);
//        pause.setAction("pause");
//        pause.putExtra("url", downloadFileInfo.getUrl());
//        PendingIntent pause_intent=PendingIntent.getBroadcast(this, 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Intent cancel=new Intent(this, MyBroadcast.class);
//        cancel.setAction("cancel");
//        cancel.putExtra("id", downloadFileInfo.getId());
//        cancel.putExtra("url", downloadFileInfo.getUrl());
//        PendingIntent cancel_intent=PendingIntent.getBroadcast(this, 0, cancel, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        notification=new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_baseline_arrow_downward_24).setContentTitle(title == null ? downloadFileInfo.getFileName() : downloadFileInfo.getFileName().substring(0, 19) + ".." + title).setContentText("Waiting").setContentIntent(contentIntent).setSound(null).setNotificationSilent().setVibrate(null).addAction(R.drawable.ic_baseline_play_arrow_24, "pause", pause_intent).addAction(R.drawable.ic_baseline_close_24, "cancel", cancel_intent).setAutoCancel(true).
//                setDefaults(NotificationCompat.PRIORITY_LOW).setPriority(NotificationCompat.PRIORITY_LOW).setOnlyAlertOnce(true);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "download Channal", NotificationManager.IMPORTANCE_DEFAULT));
//        }
//
//        manager.notify(downloadFileInfo.getId(), notification.build());
//    }
//
//    @SuppressLint("RestrictedApi")
//    private void pause_notification(DownloadFileInfo downloadFileInfo) {
//        notification.mActions.clear();
//        Intent resume=new Intent(this, MyBroadcast.class);
//        resume.setAction("resume");
//        resume.putExtra("url", downloadFileInfo.getUrl());
//        PendingIntent pause_intent=PendingIntent.getBroadcast(this, 0, resume, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Intent cancel=new Intent(this, MyBroadcast.class);
//        cancel.setAction("cancel");
//        cancel.putExtra("url", downloadFileInfo.getUrl());
//        PendingIntent cancel_intent=PendingIntent.getBroadcast(this, 0, cancel, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        notification.addAction(R.drawable.ic_baseline_play_arrow_24, "resume", pause_intent);
//        notification.addAction(R.drawable.ic_baseline_close_24, "cancel", cancel_intent);
//        notification.setContentText("Paused");
//        manager.notify(downloadFileInfo.getId(), notification.build());
//    }
//}