package com.example.s_tools.tools;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class MyAsyncTask {
    private ExecutorService executor;
    private Handler mMainHandler;

    public MyAsyncTask() {
        executor = Executors.newFixedThreadPool(4);
        mMainHandler=new Handler(Looper.getMainLooper());
    }

    abstract protected void onPostExecute();


    abstract protected void doInBackground();

    abstract protected void pendingTask();

    public void toBeContinue(){
        mMainHandler.post(this::pendingTask);
    }


    public void execute() {
        try {
            // This will run in the background thread.
            doInBackground();

        } finally {
            // This will run in UI thread.
            mMainHandler.post(this::onPostExecute);
        }
    }
}
//public abstract class MyAsyncTask extends Thread {
//    private String[] mUrls;
//    private Handler mMainHandler;
//
//    public MyAsyncTask() {
//        // Using this handler to update UI
//        mMainHandler=new Handler(Looper.getMainLooper());
//    }
//
//    abstract protected void onPreExecute();
//
//    abstract protected void onPostExecute(String result);
//
//    abstract protected void onProgressUpdate(String result);
//
//    protected void publishProgress(final String progress) {
//        // This will run in UI thread.
//        mMainHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                onProgressUpdate(progress);
//            }
//        });
//    }
//
//    abstract protected String doInBackground(String... urls);
//
//    protected void execute(String... urls) {
//        mUrls=urls;
//        start();
//    }
//
//    @Override
//    public void run() {
//        super.run();
//        String result=null;
//        try {
//            // This will run in the background thread.
//            result=doInBackground(mUrls);
//        } finally {
//            // This will run in UI thread.
//            final String finalResult=result;
//            mMainHandler.post(() -> onPostExecute(finalResult));
//        }
//    }
//}
