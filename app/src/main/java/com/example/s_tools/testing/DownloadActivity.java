package com.example.s_tools.testing;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.DownloadTask;
import com.download.library.Extra;
import com.download.library.ResourceRequest;
import com.example.s_tools.R;
import com.example.s_tools.Splash_after_reg;
import com.example.s_tools.TinyDB;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.Kbtomb;
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Url;

import static com.example.s_tools.tools.Cvalues.DOWNLOAD_ALREADY_EXISTS;
import static com.example.s_tools.tools.Cvalues.DOWNLOAD_MODULE_IS_RUNNING_WAIT;
import static com.example.s_tools.tools.Cvalues.FILE_IN_UNIQUE_FOLDER;
import static com.example.s_tools.tools.Cvalues.ISUNIQUEPATH;
import static com.example.s_tools.tools.Cvalues.LINK_EXPIRED;
import static com.example.s_tools.tools.Cvalues.STATUS_OFF;
import static com.example.s_tools.tools.Cvalues.STATUS_ON;
import static com.example.s_tools.tools.Cvalues.SUCCESS;
import static com.example.s_tools.tools.Cvalues.WILL_BE_CREATE_IN_DOWNLOAD_DICTIONARY;


public class DownloadActivity extends AppCompatActivity implements Clicklistner {
    public static final String DOWNLOADLIST="downloadlist";
    public static final String INTENTDOWNLINK="intentdownloadlink";
    public static final String FILENAME="filename";
    public static final String FILEEXTENSION="filextension";

    RecyclerView recyclerView;
    TestingAdapter adapter;
    static List<Downloadmodel> modelList=new ArrayList<>();
    TinyDB tinyDB;
    Toolbar toolbar;
    Thread thread;
    boolean process=true;
    boolean stopThread=false;
    static List<DownloadTask> taskList=new ArrayList<>();
    LinearLayout nodownload;
    String filename, extension;
    LinearLayout linearLayout;
    private boolean activitydestroy=false;
    Thread adthread;
    public String mPath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_testing);
        init();
        versioncheck();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!activitydestroy) {
                adsetup();
            }
        }, 1000);
        if (PhotoFullPopupWindow.isStoragePermissionGranted(DownloadActivity.this)) {
            String downloadLink=getIntent().getStringExtra(INTENTDOWNLINK);
            filename=getIntent().getStringExtra(FILENAME);
            Log.e("//", "onCreate: " + filename);
            extension=getIntent().getStringExtra(FILEEXTENSION);
            if (downloadLink == null) {
                new MyasyncTask().execute("");
            } else {
                new MyasyncTask().execute(downloadLink);
            }
        } else {
            nodownload.setVisibility(View.VISIBLE);
            Toast.makeText(this, Cvalues.STORAGE_PERMISSION_NOT_GRANTED, Toast.LENGTH_LONG).show();
        }

    }

    private void versioncheck() {
        new Thread(() -> {
            if (MySharedPref.isVersionOut(DownloadActivity.this)) {
                runOnUiThread(() -> {
                    DownloadImpl.getInstance().pausedTasksTotals();
                    ToastMy.successToast(DownloadActivity.this, "App Update Found!", ToastMy.LENGTH_LONG);
                    startActivity(new Intent(DownloadActivity.this, Splash_after_reg.class));
                    finishAffinity();
                });
            }
        }).start();
    }

    private void adsetup() {
        AdView adView=new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(Cvalues.banr);
        adView=findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setVisibility(View.VISIBLE);

    }

    private void removeItem(int pos) {
        for (int i=0; i < modelList.size(); i++) {
            if (modelList.get(i).getStatus() == DownloadTask.STATUS_PENDDING || modelList.get(i).getStatus() == DownloadTask.STATUS_DOWNLOADING || modelList.get(i).getStatus() == DownloadTask.STATUS_PAUSING) {
                ToastMy.errorToast(DownloadActivity.this, DOWNLOAD_MODULE_IS_RUNNING_WAIT, ToastMy.LENGTH_LONG);
                return;
            }
        }
        modelList.remove(pos);
        adapter.notifyDataSetChanged();
        saveModel();
    }

    @Override
    public void cancelitemClicked(int pos) {
        ToastMy.successToast(DownloadActivity.this, Cvalues.CANCELLED, ToastMy.LENGTH_SHORT);
        DownloadImpl.getInstance().cancel(modelList.get(pos).getUrl());
        modelList.get(pos).setUrl("");
        modelList.get(pos).setStatus(1006); //cancelled
        saveModel();
        adapter.notifyItemChanged(pos);
    }

    @Override
    public void pauseitemClicked(int pos) {
        if (process) {
            process=false;
            if (modelList.get(pos).getStatus() == DownloadTask.STATUS_ERROR || modelList.get(pos).getStatus() == DownloadTask.STATUS_PAUSED) {
                if (taskList.isEmpty() || !DownloadImpl.getInstance().exist(modelList.get(pos).getUrl())) {
                    if (modelList.get(pos).getUrl() != null) {
                        startDownload(modelList.get(pos).getUrl(), pos);
                    } else {
                        ToastMy.successToast(DownloadActivity.this, "File Downloaded Successful", ToastMy.LENGTH_SHORT);
                        removeItem(pos);
                    }
                } else {
                    boolean resume=DownloadImpl.getInstance().resume(modelList.get(pos).getUrl());
                    if (resume) {
                        modelList.get(pos).setStatus(DownloadTask.STATUS_PENDDING);
                        adapter.notifyItemChanged(pos);
                        threadRefresh(pos);
                    } else {
                        startDownload(modelList.get(pos).getUrl(), pos);
                    }
                }

            } else if (modelList.get(pos).getStatus() == DownloadTask.STATUS_DOWNLOADING || modelList.get(pos).getStatus() == DownloadTask.STATUS_PENDDING) {
                DownloadTask pause=DownloadImpl.getInstance().pause(modelList.get(pos).getUrl());
                modelList.get(pos).setStatus(pause.getStatus());
                adapter.notifyItemChanged(pos);
                new Thread(() -> {
                    while (pause.isPausing()) {
                        modelList.get(pos).setStatus(pause.getStatus());
                    }
                    modelList.get(pos).setStatus(pause.getStatus());
                    runOnUiThread(() -> {
                        adapter.notifyItemChanged(pos);
                        saveModel();
                    });
                }).start();

            }
            saveModel();
            process=true;
        }
    }

    class MyasyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tinyDB=new TinyDB(DownloadActivity.this);
        }

        @Override
        protected String doInBackground(String... strings) {
            adapter=new TestingAdapter(DownloadActivity.this, modelList, DownloadActivity.this);
            modelList.clear();
            modelList.addAll(retrieveModel());
            if (taskList.isEmpty()) {
                for (int i=0; i < modelList.size(); i++) {
                    if (modelList.get(i).getStatus() == DownloadTask.STATUS_DOWNLOADING) {
                        modelList.get(i).setStatus(DownloadTask.STATUS_PAUSED); //paused
                        saveModel();
                    } else if (modelList.get(i).getStatus() == DownloadTask.STATUS_PAUSING) {
                        modelList.get(i).setStatus(DownloadTask.STATUS_PAUSED); //paused
                        saveModel();
                    } else if (modelList.get(i).getStatus() == DownloadTask.STATUS_PENDDING) {
                        modelList.get(i).setStatus(DownloadTask.STATUS_PAUSED); //paused
                        saveModel();
                    }
                }
            }
            runOnUiThread(() -> adapter.notifyDataSetChanged());
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            recyclerView.setItemAnimator(null);
            LinearLayoutManager manager=new LinearLayoutManager(DownloadActivity.this);
            manager.setReverseLayout(true);
            manager.setStackFromEnd(true);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            startThinDownload(s);
        }
    }

    private void startThinDownload(String url) {
        if (!url.isEmpty() && modelList.isEmpty()) {
            Downloadmodel model;
            model=new Downloadmodel(url, getFileName(url, filename), 0, DownloadTask.STATUS_PENDDING, "-/-", "");
            modelList.add(model);
            adapter.notifyDataSetChanged();
            saveModel();
            startDownload(url, modelList.size() - 1);
        } else if (url.isEmpty()) {
            for (int i=0; i < modelList.size(); i++) {
                if (DownloadTask.STATUS_DOWNLOADING == modelList.get(i).getStatus() || DownloadTask.STATUS_PENDDING == modelList.get(i).getStatus()) {
                    threadRefresh(i);
                }
            }
            if (tinyDB.getListString(DOWNLOADLIST).isEmpty()) {
                nodownload.setVisibility(View.VISIBLE);
            }
        } else {
            Log.e("//", "first: ");
            for (int i=0; i < modelList.size(); i++) {
                if (DownloadTask.STATUS_DOWNLOADING == modelList.get(i).getStatus() || DownloadTask.STATUS_PENDDING == modelList.get(i).getStatus()) {
                    threadRefresh(i);
                }
            }
            for (int i=0; i < modelList.size(); i++) {
                if (modelList.get(i).getUrl().equals(url)) {
                    Toast.makeText(this, DOWNLOAD_ALREADY_EXISTS, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Downloadmodel model;
            model=new Downloadmodel(url, getFileName(url, filename), 0, DownloadTask.STATUS_PENDDING, "-/-", "");
            modelList.add(model);
            adapter.notifyDataSetChanged();
            saveModel();
            startDownload(url, modelList.size() - 1);
        }
    }

    private void threadRefresh(int pos) {
        thread=new Thread(() -> {
            try {
                if (modelList.get(pos).getStatus() == DownloadTask.STATUS_PENDDING) {
                    while (modelList.get(pos).getStatus() == DownloadTask.STATUS_PENDDING) {
                    }
                }
                while (!stopThread && modelList.get(pos).getStatus() == DownloadTask.STATUS_DOWNLOADING) {
                    Thread.sleep(1000);
                    runOnUiThread(() -> {
                        modelList.set(pos, retrieveModel().get(pos));
                        adapter.notifyItemChanged(pos);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        thread.start();
    }

    private void startDownload(String url, int pos) {
        Log.e("//", "startDownload: " + new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), modelList.get(pos).getTitle()).getName());
        KProgressHUD kProgressHUD=KProgressHUD.create(DownloadActivity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Fetching...").setDimAmount(0.2f).setCancellable(false).show();
        ResourceRequest resourceRequest=DownloadImpl.getInstance().with(getApplicationContext()).url(url).target(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), modelList.get(pos).getTitle())).setForceDownload(true).setRetry(2).setUniquePath(tinyDB.getBoolean(ISUNIQUEPATH)).setOpenBreakPointDownload(true).setParallelDownload(true);
        taskList.add(resourceRequest.getDownloadTask());

        resourceRequest.enqueue(new DownloadListenerAdapter() {
            @Override
            public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, Extra extra) {
                super.onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra);
                modelList.get(pos).setStatus(resourceRequest.getDownloadTask().getStatus());
                modelList.get(pos).setTitle(modelList.get(pos).getTitle());
                kProgressHUD.dismiss();
            }

            @Override
            public void onProgress(String url, long downloaded, long length, long usedTime) {
                super.onProgress(url, downloaded, length, usedTime);
                modelList.get(pos).setProgress((int) (downloaded * 100.0F / length));
//                modelList.get(pos).setFile_size(Kbtomb.formatFileSize(downloadedBytes) + "/" + Kbtomb.formatFileSize(totalBytes));
//                modelList.get(pos).setFile_size(getBytesDownloaded((int) (downloadedBytes * 100.0F / totalBytes), totalBytes));
                if (length < 0) {
                    modelList.get(pos).setFile_size(Kbtomb.formatFileSize(downloaded));
                } else {
                    modelList.get(pos).setFile_size(Kbtomb.formatFileSize(downloaded) + " / " + Kbtomb.formatFileSize(length));
                }
                runOnUiThread(() -> adapter.notifyItemChanged(pos));
                saveModel();
            }

            @Override
            public boolean onResult(Throwable throwable, Uri path, String url, Extra extra) {
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
                return true;
            }

            @Override
            public void onDownloadStatusChanged(Extra extra, int status) {
                super.onDownloadStatusChanged(extra, status);
                if (status == DownloadTask.STATUS_ERROR) {
                    if (kProgressHUD.isShowing()) {
                        kProgressHUD.dismiss();
                    }
                }
                if (status == DownloadTask.STATUS_SUCCESSFUL) {
                    if (extra.getMimetype().contains(Cvalues.TEXT_HTML)) {
                        modelList.get(pos).setUrl("");
                        modelList.get(pos).setStatus(DownloadTask.STATUS_CANCELED);
                        modelList.get(pos).setTitle(LINK_EXPIRED);
                        adapter.notifyItemChanged(pos);
                        boolean removeItem=true;
                        for (int i=0; i < modelList.size(); i++) {
                            if (modelList.get(pos).getStatus() == DownloadTask.STATUS_ERROR || modelList.get(pos).getStatus() == DownloadTask.STATUS_PAUSED || modelList.get(pos).getStatus() == DownloadTask.STATUS_PENDDING || modelList.get(pos).getStatus() == DownloadTask.STATUS_DOWNLOADING || modelList.get(pos).getStatus() == DownloadTask.STATUS_PAUSING) {
                                removeItem=false;
                            }
                        }
                        if (removeItem) {
                            modelList.remove(pos);
                            adapter.notifyDataSetChanged();
                            ToastMy.errorToast(DownloadActivity.this, LINK_EXPIRED, ToastMy.LENGTH_LONG);
                        }
                        saveModel();
                        return;
                    }
//
                    File file=new File(resourceRequest.getDownloadTask().getFile().getPath());
                    File ww=new File(file.getParent() + "/" + modelList.get(pos).getTitle());
                    boolean b=file.renameTo(ww);
                    Log.e("//", "onDownloadStatusChanged: file renamed? " + b);


                    Log.e("//", "onDownloadStatusChanged: " + resourceRequest.getDownloadTask().getFile().getPath());
//                    if (!b) {
//                        File yy=new File(file.getPath().concat(modelList.get(pos).getTitle().replaceAll("[\\\\/:*?\"<>|]", "")));
//                        boolean b1=file.renameTo(yy);
//                        path = ww.getPath();
//                    }


                    if (modelList.get(pos).getFile_size().contains("/")) {
                        modelList.get(pos).setFile_size(modelList.get(pos).getFile_size().split("/")[0]);
                    }
                    modelList.get(pos).setUrl("");
                    modelList.get(pos).setPath(resourceRequest.getDownloadTask().getFile().getPath());
                }
                modelList.get(pos).setStatus(status);
                adapter.notifyItemChanged(pos);
                saveModel();
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{resourceRequest.getDownloadTask().getFile().getPath()}, null, (path1, uri) -> {

                });

            }
        });
    }

    public void saveModel() {
        ArrayList<String> arrayList=new ArrayList<>();
        for (int i=0; i < modelList.size(); i++) {
            arrayList.add(new Gson().toJson(modelList.get(i)));
        }
        tinyDB.putListString(DOWNLOADLIST, arrayList);
    }

    public List<Downloadmodel> retrieveModel() {
        List<Downloadmodel> downloadmodels=new ArrayList<>();
        ArrayList<String> list=tinyDB.getListString(DOWNLOADLIST);
        if (!list.isEmpty()) {
            Gson gson=new Gson();
            for (int i=0; i < list.size(); i++) {
                downloadmodels.add(gson.fromJson(list.get(i), Downloadmodel.class));
            }
            return downloadmodels;
        }
        return downloadmodels;
    }

    private String getBytesDownloaded(int progress, long totalBytes) {
        //Greater than 1 MB
        long bytesCompleted=(progress * totalBytes) / 100;
        if (totalBytes >= 1000000) {
            return ("" + (String.format("%.1f", (float) bytesCompleted / 1000000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000000)) + "MB");
        }
        if (totalBytes >= 1000) {
            return ("" + (String.format("%.1f", (float) bytesCompleted / 1000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000)) + "Kb");

        } else {
            return ("" + bytesCompleted + "/" + totalBytes);
        }
    }

    private void init() {
        linearLayout=findViewById(R.id.downloadbaner);
        recyclerView=findViewById(R.id.testingrecyclerview);
        toolbar=findViewById(R.id.serviceToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Downloads");
        nodownload=findViewById(R.id.nodownload);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.cleardownload:
                clearDownloadList();
                break;
            case R.id.upath:
                dialog_Update();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialog_Update() {
        Dialog update_dialog=new Dialog(DownloadActivity.this);
        update_dialog.setContentView(R.layout.askforupdateonoff);
        Button yes=update_dialog.findViewById(R.id.yesupdate);
        Button no=update_dialog.findViewById(R.id.noupdate);
        TextView textView=update_dialog.findViewById(R.id.errorText);
        TextView title=update_dialog.findViewById(R.id.titlePopup);
        textView.setText(FILE_IN_UNIQUE_FOLDER);
        title.setText(STATUS_OFF);
        title.setTextColor(Color.GRAY);
        if (tinyDB.getBoolean(ISUNIQUEPATH)) {
            textView.setText(WILL_BE_CREATE_IN_DOWNLOAD_DICTIONARY);
            title.setText(STATUS_ON);
            title.setTextColor(Color.GREEN);
        }
        update_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        update_dialog.show();
        yes.setOnClickListener(v -> {
            tinyDB.putBoolean(ISUNIQUEPATH, true);
            update_dialog.dismiss();
            ToastMy.successToast(DownloadActivity.this, SUCCESS, ToastMy.LENGTH_SHORT);
        });
        no.setOnClickListener(v -> {
            tinyDB.putBoolean(ISUNIQUEPATH, false);
            update_dialog.dismiss();
        });
    }

    private void clearDownloadList() {
        for (int i=0; i < modelList.size(); i++) {
            if (modelList.get(i).getStatus() == DownloadTask.STATUS_PENDDING || modelList.get(i).getStatus() == DownloadTask.STATUS_DOWNLOADING || modelList.get(i).getStatus() == DownloadTask.STATUS_PAUSING) {
                ToastMy.errorToast(DownloadActivity.this, DOWNLOAD_MODULE_IS_RUNNING_WAIT, ToastMy.LENGTH_LONG);
                return;
            }
        }
        taskList.clear();
        tinyDB.remove(DOWNLOADLIST);
        modelList.clear();
        adapter.notifyDataSetChanged();
        if (tinyDB.getListString(DOWNLOADLIST).isEmpty()) {
            nodownload.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        activitydestroy=true;
        super.onDestroy();
        if (thread != null) {
            stopThread=true;
        }
    }

    private String getFileName(String url, String title) {
        String characterFilter="[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
        if (title != null) {
            return title.replaceAll(characterFilter, "");
        }

        String guessFileName=URLUtil.guessFileName(url, "", getMimeType(url));

        return guessFileName.replaceAll(characterFilter, "").replaceAll("(?i)com", "buddy").replaceAll("(?i)movies", "buddy");
    }

    public String getMimeType(String url) {
        String type=null;
        String extension=MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type=MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }


}