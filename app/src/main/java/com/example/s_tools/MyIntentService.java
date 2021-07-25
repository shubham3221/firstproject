//package com.example.s_tools;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Environment;
//import android.os.IBinder;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.JobIntentService;
//import androidx.core.app.NotificationCompat;
//
//import com.download.library.DownloadImpl;
//import com.download.library.DownloadListener;
//import com.download.library.DownloadStatusListener;
//import com.download.library.DownloadTask;
//import com.download.library.DownloadingListener;
//import com.download.library.Extra;
//
//
//import org.jetbrains.annotations.NotNull;
//
//import java.io.File;
//import java.util.List;
//
//
//public class MyIntentService extends Service implements Fetch {
//    public static final String FOREGROUND_ID="FOREGROUND_CHANNEL_ID";
//    public static final String CHANNEL_ID="OREO_CHANNEL_ID";
//    public static final String TAG="//";
//    NotificationCompat.Builder notification;
//    NotificationManager manager;
//    String url;
//
//
//    private Notification get_notification() {
//        return new NotificationCompat.Builder(this, "channal_1").setSmallIcon(R.drawable.ic_baseline_arrow_circle_down_24).setContentTitle("service running").setContentText("message").setDefaults(NotificationCompat.DEFAULT_ALL).setPriority(NotificationCompat.PRIORITY_HIGH).build();
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.e(TAG, "onCreate: ");
//        manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
//                .setDownloadConcurrentLimit(3)
//                .build();
//        Fetch.Impl.getInstance(fetchConfiguration).addListener(this,true);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Fetch.Impl.getDefaultInstance().removeListener(this);
//        stopSelf();
//        Log.e("//", "onDestroy: ");
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
//        Log.e(TAG, "onStartCommand: ");
////        if (intent!=null){
////            if (intent.hasExtra("url")){
////                url=intent.getStringExtra("url");
////                String title=intent.getStringExtra("title");
////                DownloadImpl.getInstance().with(getApplicationContext()).url(url)
////                        .target(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), getFileName(url,title)))
////                        .setForceDownload(true).setRetry(2)
////                        .setOpenBreakPointDownload(true).setParallelDownload(true)
////                        .enqueue(this);
////                show_broadcast();
////            }
////        }
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//
//
//    private void show_broadcast(String filename,int id) {
//        String title=null;
//        if (filename.length() > 20) {
//            if (filename.contains(".")) {
//                title=filename.substring(filename.lastIndexOf("."));
//            }
//        }
//        Intent intent=new Intent(this, DownloadTesting.class);
//        PendingIntent contentIntent=PendingIntent.getActivity(this, 0, intent, 0);
//
//        Intent pause=new Intent(this, MyBroadcast.class);
//        pause.setAction("pause");
//        pause.putExtra("url", "murl");
//        PendingIntent pause_intent=PendingIntent.getBroadcast(this, 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Intent cancel=new Intent(this, MyBroadcast.class);
//        cancel.setAction("cancel");
//        cancel.putExtra("id", id);
//        cancel.putExtra("url", "murl");
//        PendingIntent cancel_intent=PendingIntent.getBroadcast(this, 0, cancel, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        notification=new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_baseline_arrow_downward_24).setContentTitle(title == null ? filename : filename.substring(0, 19) + ".." + title).setContentText("Waiting").setContentIntent(contentIntent).setSound(null).setNotificationSilent().setVibrate(null).addAction(R.drawable.ic_baseline_play_arrow_24, "pause", pause_intent).addAction(R.drawable.ic_baseline_close_24, "cancel", cancel_intent).setAutoCancel(true).
//                setDefaults(NotificationCompat.PRIORITY_LOW).setPriority(NotificationCompat.PRIORITY_LOW).setOnlyAlertOnce(true);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "download Channal", NotificationManager.IMPORTANCE_DEFAULT));
//        }
//
//        manager.notify(id, notification.build());
//    }
//
//
//
//    private String getFileName(String url, String title) {
//        final Uri uri=Uri.parse(url);
//        final String fileName=uri.getLastPathSegment();
//        String characterFilter="[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
//        if (title != null) {
//            return title.replaceAll(characterFilter, "");
//        }
//        String replaceAll=fileName.replaceAll(characterFilter, "");
//        if (replaceAll.contains(".com") && replaceAll.length() > 11) {
//            return replaceAll.substring(10);
//        }
//
//        return replaceAll;
//    }
//
//    @Override
//    public void onAdded(@NotNull Download download) {
//        Log.e(TAG, "onAdded: " );
//        show_broadcast(download.getFile(),download.getId());
//    }
//
//    @Override
//    public void onCancelled(@NotNull Download download) {
//
//    }
//
//    @Override
//    public void onCompleted(@NotNull Download download) {
//
//    }
//
//    @Override
//    public void onDeleted(@NotNull Download download) {
//
//    }
//
//    @Override
//    public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {
//
//    }
//
//    @Override
//    public void onPaused(@NotNull Download download) {
//
//    }
//
//    @Override
//    public void onProgress(@NotNull Download download, long l, long l1) {
//        if (notification==null){
//            show_broadcast(download.getFile(),download.getId());
//        }
//        notification.setContentText("downloading");
//        notification.setProgress(100, download.getProgress(), false);
//        manager.notify(13, notification.build());
//    }
//
//    @Override
//    public void onQueued(@NotNull Download download, boolean b) {
//        Log.e(TAG, "onQueued: " );
//    }
//
//    @Override
//    public void onRemoved(@NotNull Download download) {
//
//    }
//
//    @Override
//    public void onResumed(@NotNull Download download) {
//
//    }
//
//    @Override
//    public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {
//        Log.e(TAG, "onStarted: " );
//    }
//
//    @Override
//    public void onWaitingNetwork(@NotNull Download download) {
//        Log.e(TAG, "onWaitingNetwork: " );
//    }
//
//
//    @Override
//    public void onError(@NotNull Download download, @NotNull Error error, @org.jetbrains.annotations.Nullable Throwable throwable) {
//        Log.e(TAG, "onError: "+error.toString() );
//    }
//}