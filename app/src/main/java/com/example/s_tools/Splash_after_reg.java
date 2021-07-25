package com.example.s_tools;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies;
import com.example.s_tools.entertainment.VideoActivity;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.example.s_tools.tools.retrofitcalls.VC;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.Arrays;


public class Splash_after_reg extends AppCompatActivity {
    public static final String TAG="//";
    //    public static final String APP_ID="209328409";
//    public static final String PAS="pas";

    public static String weburl="";
    TextView textView;
    ProgressBar progressBar;
    boolean downloadsuccess=false;
    String downloadpath=null;
    int retrying=0;


    int id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Splash_after_reg.this.overridePendingTransition(R.anim.enter_anim_from_bottom, R.anim.exit_enter_from_bottom);
        setContentView(R.layout.splashscreen_after_register);
        textView=findViewById(R.id.stater);
        fill_string();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }


        checkUpdate();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            MobileAds.initialize(this, initializationStatus -> {
                RequestConfiguration configuration=new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("0C9EBB7EE878C7C3529A8D806DC4FFCA","EDB30A4A00115883E90E49CAD034A12C")).build();
                MobileAds.setRequestConfiguration(configuration);
            });
        }, 200);



    }
    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.System.canWrite(Splash_after_reg.this))
                return true;
            else
                openAndroidPermissionsMenu();
        }
        return false;
    }

    private void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    public static void fill_string() {
        new Thread(() -> {
            weburl=new String(Base64.decode("aHR0cHM6Ly9oaWRkZW5zdG9vbC5jdS5tYS9tc3Rvb2wvd2ViLw==", Base64.DEFAULT));
            MyMovies.CU_MA=new String(Base64.decode("aGlkZGVuc3Rvb2wuY3UubWE=", Base64.DEFAULT));
        }).start();
        new Thread(() -> {

            String ic="Y2EtYXBwLXB1Yi0zODEwNzk0NDY1NjM2NDgyLzYyODE5MDEzODI=";
            String ic2="Y2EtYXBwLXB1Yi0zOTQwMjU2MDk5OTQyNTQ0LzEwMzMxNzM3MTI=";

            String br="Y2EtYXBwLXB1Yi0zODEwNzk0NDY1NjM2NDgyLzM4NTA2MzkxOTM=";
            String br2="Y2EtYXBwLXB1Yi0zOTQwMjU2MDk5OTQyNTQ0LzYzMDA5NzgxMTE=";

            String vd="Y2EtYXBwLXB1Yi0zODEwNzk0NDY1NjM2NDgyLzQxMTE5MzE0MDM=";
            String vd2="Y2EtYXBwLXB1Yi0zOTQwMjU2MDk5OTQyNTQ0LzUyMjQzNTQ5MTc=";

            Cvalues.inter=new String(Base64.decode(ic, Base64.DEFAULT));
            Cvalues.banr=new String(Base64.decode(br, Base64.DEFAULT));
            Cvalues.vid=new String(Base64.decode(vd, Base64.DEFAULT));
        }).start();
    }

    private boolean checkUpdate() {
        return new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (MySharedPref.isVersionOut(Splash_after_reg.this)) {
                Dialog dialog=new Dialog(Splash_after_reg.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.update_dilog);
                TextView retry=dialog.findViewById(R.id.loginbtnPopup);
                progressBar=dialog.findViewById(R.id.progressBar1);
                TextView status=dialog.findViewById(R.id.herelink);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                retry.setOnClickListener(v -> {
                    if (downloadsuccess) {
                        TinyDB tinyDB=new TinyDB(Splash_after_reg.this);
                        tinyDB.putLong("lastcheck",0);
                        MySharedPref.clearLogin(Splash_after_reg.this);
//                        Intent intent=new Intent(Intent.ACTION_VIEW);
//                        File file=new File(downloadpath);
//                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                        startActivity(intent);


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (!getPackageManager().canRequestPackageInstalls()) {
                                startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).
                                        setData(Uri.parse(String.format("package:%s", getPackageName()))), 1234);
                            }
                        }

                        installAPK(downloadpath);

//
//                        File toInstall = new File(downloadpath);
//                        if (android.os.Build.VERSION.SDK_INT >= 29) {
//                            Uri contentUri = FileProvider.getUriForFile(Splash_after_reg.this
//                                    , "package_name.fileprovider", toInstall);
//                            Intent intent2 = new Intent(Intent.ACTION_INSTALL_PACKAGE);
//                            intent2.setData(contentUri);
//                            intent2.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            startActivity(intent2);
//                        } else {
//                            Uri apkUri = Uri.fromFile(toInstall);
//                            Intent intent3 = new Intent(Intent.ACTION_VIEW);
//                            intent3.setDataAndType(apkUri, "application/vnd.android.package-archive");
//                            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent3);
//                        }
//
//                    }


                    } else {
                        if (isNetworkConnected()) {
                            retry.setVisibility(View.GONE);
                            status.setText("Getting Change Logs: ");
                            updateapp(status, retry, PhotoFullPopupWindow.isStoragePermissionGranted(Splash_after_reg.this));
                        } else {
                            status.setText("Internet is not connected! Please connect to internet and press retry button below");
                            progressBar.setVisibility(View.GONE);
                            retry.setVisibility(View.VISIBLE);
                        }
                    }
                });

                if (isNetworkConnected()) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        status.setText("Getting Change Logs: ");
                    }, 1000);
                    updateapp(status, retry, PhotoFullPopupWindow.isStoragePermissionGranted(Splash_after_reg.this));
                } else {
                    status.setText("Internet is not connected! Please connect to internet and press retry button below");
                    progressBar.setVisibility(View.GONE);
                    retry.setVisibility(View.VISIBLE);

                }
            } else {
                TinyDB tinyDB=new TinyDB(Splash_after_reg.this);
                if (tinyDB.getLong("lastcheck") == 0) {
                    PhotoFullPopupWindow.isStoragePermissionGranted(Splash_after_reg.this);
                    TextView firsttime=findViewById(R.id.setup);
                    firsttime.setVisibility(View.VISIBLE);
                    firsttime.startAnimation(AnimationUtils.loadAnimation(Splash_after_reg.this, R.anim.blink_1500));

                    VC.check_v(Splash_after_reg.this, (success, updateinfo) -> {
                        firsttime.clearAnimation();
                        firsttime.setVisibility(View.GONE);
                        if (success && updateinfo != null) {
                            checkUpdate();
                        } else if (success) {
                            tinyDB.putLong("lastcheck", System.currentTimeMillis());
                            startActivity(new Intent(Splash_after_reg.this, VideoActivity.class));
                        } else {
                            if (retrying == 0) {
                                ToastMy.errorToast(Splash_after_reg.this, "No Response. Retrying...", ToastMy.LENGTH_SHORT);
                                retrying++;
                                checkUpdate();
                            } else if (retrying == 1) {
                                ToastMy.errorToast(Splash_after_reg.this, "Again No Response. Retrying Last Time...", ToastMy.LENGTH_SHORT);
                                retrying++;
                                checkUpdate();
                            } else {
                                ToastMy.errorToast(Splash_after_reg.this, Cvalues.OFFLINE, ToastMy.LENGTH_SHORT);
                            }
                        }
                    });
                } else {
                    startActivity(new Intent(Splash_after_reg.this, VideoActivity.class));
                }
            }
        }, 1500);
    }

    private void updateapp(TextView status, TextView retry, boolean storagepermisiongranted) {
        VC.check_specific(Splash_after_reg.this, false, false, true, (success, updateinfo) -> {
            if (success) {
                String[] split=updateinfo.split(",");

                status.setText("Change Logs: ");
                for (int i=1; i < split.length; i++) {
                    status.append("\n\u25CF " + split[i]);
                }
                status.append("\n\nDownloading Started...");
                DownloadImpl.getInstance().with(Splash_after_reg.this).url(split[0].trim()).target(storagepermisiongranted ? Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) : new File(this.getExternalCacheDir(), "mbuddy.apk")).setForceDownload(true).setRetry(2).setUniquePath(true).setQuickProgress(true).setOpenBreakPointDownload(true).enqueue(new DownloadListenerAdapter() {
                    @Override
                    public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, Extra extra) {
                        super.onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onProgress(String url, long downloaded, long length, long usedTime) {
                        super.onProgress(url, downloaded, length, usedTime);
                        progressBar.setProgress((int) (downloaded * 100.0F / length));
                    }

                    @Override
                    public boolean onResult(Throwable throwable, Uri path, String url, Extra extra) {
                        if (throwable == null) {
                            progressBar.setVisibility(View.GONE);
                            status.append("\n\nTip: if you have any difficulty to install this app , then uninstall this app first then goto buddymy.blogspot.com to download from there and install latest version.\n\nDownload Completed!");
                            downloadsuccess=true;
                            downloadpath=path.getPath();
                            retry.setText("Install Now");
                            retry.setVisibility(View.VISIBLE);
                        }
                        return super.onResult(throwable, path, url, extra);
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                status.setText(Cvalues.OFFLINE);
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    void installAPK(String path){

        File file = new File(path);
        if(file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uriFromFile(getApplicationContext(), new File(path)), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                getApplicationContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Log.e("TAG", "Error in opening the file!");
            }
        }else{
            Toast.makeText(getApplicationContext(),"installing", Toast.LENGTH_LONG).show();
        }
    }
    Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

}
