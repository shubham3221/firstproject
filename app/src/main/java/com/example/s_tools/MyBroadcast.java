//package com.example.s_tools;
//
//import android.app.NotificationManager;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.util.Log;
//import android.widget.Toast;
//
//public class MyBroadcast extends BroadcastReceiver {
//    NotificationManager manager;
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (intent != null) {
//            String action = intent.getAction();
//            if (action.equals("pause")) {
//                Log.e("//", "onReceive:p ");
//                FileDownloader.pause(intent.getStringExtra("url"));
//            } else if (action.equals("resume")) {
//                Log.e("//", "onReceive:r ");
//                FileDownloader.start(intent.getStringExtra("url"));
//            } else if (action.equals("cancel")) {
//                Log.e("//", "onReceive:c ");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                }
//
//
//                FileDownloader.delete(intent.getStringExtra("url"), true, new OnDeleteDownloadFileListener() {
//                    @Override
//                    public void onDeleteDownloadFilePrepared(DownloadFileInfo downloadFileNeedDelete) {
//                    }
//
//                    @Override
//                    public void onDeleteDownloadFileSuccess(DownloadFileInfo downloadFileDeleted) {
//                        manager.cancel(intent.getIntExtra("id", 0));
//                        Toast.makeText(context, "Cancelled Successful", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onDeleteDownloadFileFailed(DownloadFileInfo downloadFileInfo, DeleteDownloadFileFailReason failReason) {
//                        manager.cancel(intent.getIntExtra("id", 0));
//                    }
//                });
//            }
//        }
//    }
//}
