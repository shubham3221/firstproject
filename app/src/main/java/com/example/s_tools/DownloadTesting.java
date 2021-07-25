//package com.example.s_tools;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.NotificationCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.chootdev.recycleclick.RecycleClick;
//import com.download.library.DownloadListenerAdapter;
//import com.example.s_tools.entertainment.VideoActivity;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//import org.wlf.filedownloader.DownloadFileInfo;
//import org.wlf.filedownloader.FileDownloadConfiguration;
//import org.wlf.filedownloader.FileDownloader;
//import org.wlf.filedownloader.base.Status;
//import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener;
//
//import java.io.File;
//import java.util.List;
//
//
//public class DownloadTesting extends AppCompatActivity {
//    public static final String TAG = "//";
//    String title, url;
//    public static String DownloadUrl = "url";
//    public static String DownloadTitle = "title";
//    DownloadAdapter adapter;
//    RecyclerView recyclerView;
//    boolean activity_running = true;
//    DownloadManagerPro downloadManagerPro;
//    long id;
//    NotificationManager manager;
//    Context context;
//    TextView textView;
//    List<DownloadFileInfo> downloadFiles;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_download_testing);
//        recyclerView = findViewById(R.id.recycle);
//        title = getIntent().getStringExtra(DownloadTitle);
//        textView = findViewById(R.id.download);
//        context = this;
//        url = "https://get.videolan.org/vlc/3.0.11/win32/vlc-3.0.11-win32.exe";
//        String url2 = "http://speedtest.ftp.otenet.gr/files/test1Gb.db";
//        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        init(path.toString());
//        String title = getIntent().getStringExtra("title");
//
//        downloadFiles = FileDownloader.getDownloadFiles();
//        adapter = new DownloadAdapter(context, downloadFiles);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
//        linearLayoutManager.setReverseLayout(true);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(adapter);
//
////
////
////
//        if (getIntent().getStringExtra("url") != null) {
//            Intent intent = new Intent(DownloadTesting.this, YourService.class);
//            startService(intent);
//
//
//            FileDownloader.detect(getIntent().getStringExtra("url"), new OnDetectBigUrlFileListener() {
//                @Override
//                public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) {
//                    // here to change to custom fileName, saveDir if needed
//                    if (title != null) {
//                        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
//                        FileDownloader.createAndStart(url, path.toString(), title.replaceAll(characterFilter, ""));
//                    } else {
//                        FileDownloader.start(url);
//                    }
//                }
//
//                @Override
//                public void onDetectUrlFileExist(String url) {
//                    Toast.makeText(DownloadTesting.this, "file already exists", Toast.LENGTH_SHORT).show();
//                    // continue to download
//                    FileDownloader.start(url);
//                    Log.e(TAG, "onDetectUrlFileExist: ");
//                }
//
//                @Override
//                public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) {
////                FileDownloader.createAndStart(url, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(),"vlc.exe");
//                    Log.e(TAG, "onDetectUrlFileFailed:11 " + failReason.getMessage());
//                }
//            });
//        }
//
//
////        Uri downloadUri=Uri.parse(url);
////        Uri destinationUri=Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/vlc.exe");
//        // Enabling database for resume support even after the application is killed:
//    }
//
//    private void init(String path) {
////        FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder(this);
//////        builder.configFileDownloadDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
//////                "FileDownloader");
////        builder.configFileDownloadDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
////        builder.configFileDownloadDir(path); // config the download path
////        builder.configDownloadTaskSize(3);
////        builder.configRetryDownloadTimes(2);
////        builder.configDebugMode(false);
////        builder.configConnectTimeout(10000); // 25s
////        FileDownloadConfiguration configuration = builder.build(); // build FileDownloadConfiguration with the builder
////        FileDownloader.init(configuration);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(DownloadFileInfo event) {
//        if (event != null) {
//            downloadFiles = FileDownloader.getDownloadFiles();
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    public void start_download(View view) {
//        Log.e(TAG, "start_download: " );
//        Intent intent = new Intent(DownloadTesting.this, YourService.class);
//        startService(intent);
//        FileDownloader.detect(url, new OnDetectBigUrlFileListener() {// create a custom new download
//            @Override
//            public void onDetectNewDownloadFile(String url, String fileName, String saveDir, long fileSize) {
//                // here to change to custom fileName, saveDir if needed
//                downloadFiles = FileDownloader.getDownloadFiles();
//                adapter.notifyDataSetChanged();
//                FileDownloader.start(url);
////                FileDownloader.createAndStart(url, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), "vlc.exe");
//                Log.e(TAG, "onDetectNewDownloadFile: ");
//            }
//
//            @Override
//            public void onDetectUrlFileExist(String url) {
//                // continue to download
//                DownloadFileInfo downloadFile = FileDownloader.getDownloadFile(url);
//                if (downloadFile.getStatus() == Status.DOWNLOAD_STATUS_DOWNLOADING || downloadFile.getStatus() == Status.DOWNLOAD_STATUS_PREPARING || downloadFile.getStatus() == Status.DOWNLOAD_STATUS_PREPARED) {
//                    Toast.makeText(DownloadTesting.this, "already running", Toast.LENGTH_SHORT).show();
//                } else {
//                    FileDownloader.start(url);
//                }
//                Log.e(TAG, "onDetectUrlFileExist: ");
//            }
//
//            @Override
//            public void onDetectUrlFileFailed(String url, DetectBigUrlFileFailReason failReason) {
//                // error occur, see failReason for details
//                Log.e(TAG, "onDetectUrlFileFailed: " + failReason.getMessage());
//            }
//        });
//
//    }
//
//    private void show_broadcast() {
//        Intent intent = new Intent(this, DownloadTesting.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        Intent actionIntent = new Intent(this, MyBroadcast.class);
//        actionIntent.putExtra("msg", "hello");
//        PendingIntent action_intent_pending = PendingIntent.getBroadcast(this, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification notification = new NotificationCompat.Builder(this, "channal_1").setSmallIcon(R.drawable.ic_baseline_arrow_circle_down_24).setContentTitle("title").setContentText("message").setContentIntent(contentIntent).addAction(R.drawable.ic_baseline_play_arrow_24, "send broadcast ", action_intent_pending).setDefaults(NotificationCompat.DEFAULT_ALL).setPriority(NotificationCompat.PRIORITY_HIGH).setOnlyAlertOnce(true).build();
//
//        manager.notify(13, notification);
//    }
//
//    private void start_service() {
//        //start service
//        Intent intent = new Intent(this, VideoActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
////        Intent actionIntent = new Intent(this, MyIntentService.class);
////        actionIntent.putExtra("msg", "hello");
////        PendingIntent action_intent_pending = PendingIntent.getService(this, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
////        Notification notification = new NotificationCompat.Builder(this, "channal_1").setSmallIcon(R.drawable.ic_baseline_arrow_circle_down_24).setContentTitle("title").setContentText("message").setContentIntent(contentIntent).addAction(R.drawable.ic_baseline_play_arrow_24, "start service", action_intent_pending).addAction(R.drawable.ic_baseline_play_arrow_24, "action 2", null).addAction(R.drawable.ic_baseline_play_arrow_24, "action 3", null).setDefaults(NotificationCompat.DEFAULT_ALL).setPriority(NotificationCompat.PRIORITY_DEFAULT).setOnlyAlertOnce(true).build();
////
////        manager.notify(12, notification);
//    }
//
//    private void show_simple() {
//        Intent intent = new Intent(this, VideoActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        Intent actionIntent = new Intent(this, DownloadTesting.class);
//        actionIntent.putExtra("msg", "hello");
//        PendingIntent action_intent_pending = PendingIntent.getActivity(this, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification notification = new NotificationCompat.Builder(this, "channal_1").setSmallIcon(R.drawable.ic_baseline_arrow_circle_down_24).setContentTitle("title").setContentText("message").setContentIntent(contentIntent).addAction(R.drawable.ic_baseline_play_arrow_24, "action 1", action_intent_pending).addAction(R.drawable.ic_baseline_play_arrow_24, "action 2", null).addAction(R.drawable.ic_baseline_play_arrow_24, "action 3", null).setDefaults(NotificationCompat.DEFAULT_ALL).setPriority(NotificationCompat.PRIORITY_HIGH).setOnlyAlertOnce(true).build();
//
//        manager.notify(11, notification);
//    }
//
//    private String getFilePath(String url, String title) {
//        final Uri uri = Uri.parse(url);
//        final String fileName = uri.getLastPathSegment();
//        if (title != null) {
//            return (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + title);
//        }
//        return (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + fileName);
//    }
//
//    private String getFileName(String url, String title) {
//        final Uri uri = Uri.parse(url);
//        final String fileName = uri.getLastPathSegment();
//        String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
//        if (title != null) {
//            return title.replaceAll(characterFilter, "");
//        }
//        String replaceAll = fileName.replaceAll(characterFilter, "");
//        if (replaceAll.contains(".com") && replaceAll.length() > 11) {
//            return replaceAll.substring(10);
//        }
//
//        return replaceAll;
//    }
//}