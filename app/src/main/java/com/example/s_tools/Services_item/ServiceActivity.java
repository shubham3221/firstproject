package com.example.s_tools.Services_item;//package com.example.s_tools.Services_item;
//package com.example.s_tools.Services_item;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.cardview.widget.CardView;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MenuItem;
////import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.chootdev.recycleclick.RecycleClick;
//import com.example.s_tools.R;
//import com.example.s_tools.Services_item.inst.InstFragment;
//import com.example.s_tools.Services_item.songs.SongActivity;
//import com.example.s_tools.Splash_login_reg.SplashScreen;
//import com.example.s_tools.Services_item.VidDownloader.VideoDownloader;
//import com.example.s_tools.Services_item.mp3download.Mp3Downloader;
//import com.example.s_tools.entertainment.VideoActivity;
//import com.example.s_tools.tools.EV;
//import com.example.s_tools.tools.Placementsid;
//import com.example.s_tools.tools.ToastMy;
//import com.example.s_tools.tools.Typewriter;
//import com.example.s_tools.tools.retrofitcalls.MySharedPref;
//import com.kaopiz.kprogresshud.KProgressHUD;
//import com.unity3d.ads.IUnityAdsListener;
//import com.unity3d.ads.UnityAds;
//import com.unity3d.services.banners.BannerErrorInfo;
//import com.unity3d.services.banners.BannerView;
//import com.unity3d.services.banners.UnityBannerSize;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//
//public class ServiceActivity extends AppCompatActivity {
//    public static final String TOKEN="Generate Token";
//    public static final String SEARCH_DOWNLOAD="Song Search & Download";
//    public static final String UNDER_DEVELOPMENT="Under Development";
//    public static final String TAG="mtag";
//    public static final String TOKEN_GENERATED_SUCCESSFULLY="Token Generated Successfully";
//    public static final String TOKEN_GENERATED_CLICK_TO_COPY="Token Generated!!! Click to copy.";
//    public static final String CONNECTING="Connecting...use VPN if doesn't connect within 10 seconds";
//    RecyclerView recyclerView;
//    List<String> list;
//    Toolbar toolbar;
//    LinearLayout linearLayout;
//    BannerView bannerView;
//    MyUnityadsBanner banner;
//    RecyclerAdapter adapter;
//    MyVideoAds videoAds;
//    TextView tokenText, clickheretocopy;
//    ImageView copy_paste_imageview;
//    CardView cardView;
//    Typewriter generateText, clicktocopy;
//    private String mtoken="";
//    private boolean isadready=true;
//    String[] descriptionData={"Connecting", "Connected", "Run Ad", "Confirm"};
//    private boolean btnclick=false;
//    private boolean bannerloaded=false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_services);
//        init();
////        ChangeStatusBarColor.changeStatusbarColor(ServiceActivity.this,R.color.colorPrimaryDarker);
//        new MyAsyncTask().execute();
//
////getdata();
//
//
//    }
//
//    private class MyAsyncTask extends AsyncTask<String, String, String> {
//
//        public static final String YOUTUBE_TO_MP_3="Youtube to MP3";
//        public static final String VIDEO_DOWNLOADER="Youtube Video Downloader";
//        public static final String INSTAGRAM_DOWNLOADER="Instagram Downloader";
//
//        @Override
//        protected String doInBackground(String... strings) {
//            list=new ArrayList<>();
//            list.add(TOKEN);
//            list.add(SEARCH_DOWNLOAD);
//            list.add(YOUTUBE_TO_MP_3);
//            list.add(VIDEO_DOWNLOADER);
//            list.add(INSTAGRAM_DOWNLOADER);
//            adapter=new RecyclerAdapter(ServiceActivity.this, list);
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            recyclerView.setLayoutManager(new GridLayoutManager(ServiceActivity.this, 2));
//            recyclerView.setAdapter(adapter);
//            RecycleClick.addTo(recyclerView).setOnItemClickListener((recyclerView, i, view) -> {
//                if (i == 0) {
//                    generateToken();
//                }
//                if (i == 1) {
//                    startActivity(new Intent(ServiceActivity.this, SongActivity.class));
//                }
//                if (i == 2) {
//                    startActivity(new Intent(ServiceActivity.this, Mp3Downloader.class));
//
//                    //getdataa(ServiceActivity.this);
////                    String s1=Encryptevalues.encryptAndEncode(MySharedPref.getCookiee(ServiceActivity.this));
////                    Log.e(TAG, "onPostExecute: "+s1 );
//                }
//                if (i == 3) {
//                    startActivity(new Intent(ServiceActivity.this, VideoDownloader.class));
//                }
//                if (i == 4) {
//                    cardView.setVisibility(View.GONE);
//                    InstFragment someFragment=new InstFragment();
//                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.add(R.id.serviceContainer, someFragment); // give your fragment container id in first parameter
//                    fragmentTransaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                    fragmentTransaction.commit();
//                }
//            });
//        }
//    }
//
//    private void adSetup() {
//        banner=new MyUnityadsBanner();
//        bannerView=new BannerView(ServiceActivity.this, Placementsid.banner, UnityBannerSize.getDynamicSize(ServiceActivity.this));
//        videoAds=new MyVideoAds();
//        bannerView.setListener(banner);
//        UnityAds.addListener(videoAds);
//    }
//
//
//    private void generateToken() {
//        adSetup();
//        UnityAds.load(Placementsid.REWARDED_VIDEO);
//        btnclick=true;
//        KProgressHUD progressHUD=KProgressHUD.create(ServiceActivity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setAutoDismiss(true).setDimAmount(0.5f).setDetailsLabel("Initializing...").setCancellable(true).show();
//        new Thread(() -> {
//            while (!UnityAds.isInitialized()) {
//                try {
//                    Thread.sleep(1000);
//                    UnityAds.initialize(ServiceActivity.this, VideoActivity.U_GAME_ID, VideoActivity.MODE);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            progressHUD.dismiss();
//            runOnUiThread(() -> {
//                if (UnityAds.isInitialized()) {
//                    if (bannerloaded) {
//                        UnityAds.show(ServiceActivity.this, Placementsid.REWARDED_VIDEO);
//                    } else {
//                        bannerView.load();
//                    }
//                }
//            });
//        }).start();
////        String substring=UUID.randomUUID().toString().substring(0,8);
////        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
////            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
////            clipboard.setText(substring);
////        } else {
////            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
////            android.content.ClipData clip = android.content.ClipData.newPlainText(substring, substring);
////            clipboard.setPrimaryClip(clip);
////            Toast.makeText(this, ""+substring, Toast.LENGTH_SHORT).show();
////        }
//    }
//
//    class MyUnityadsBanner implements BannerView.IListener {
//        @Override
//        public void onBannerLoaded(BannerView bannerView) {
//            bannerloaded=true;
//            linearLayout.removeAllViews();
//            linearLayout.addView(bannerView);
//            if (btnclick) {
//                if (UnityAds.getPlacementState(Placementsid.REWARDED_VIDEO) == UnityAds.PlacementState.READY) {
//                    UnityAds.show(ServiceActivity.this, Placementsid.REWARDED_VIDEO);
//                } else {
//                    bannerView.load();
//                }
//            }
//        }
//
//        @Override
//        public void onBannerClick(BannerView bannerView) {
//
//        }
//
//        @Override
//        public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
//            Log.e(TAG, "onBannerFailedToLoad: " + bannerErrorInfo.errorMessage);
//        }
//
//        @Override
//        public void onBannerLeftApplication(BannerView bannerView) {
//            bannerView.destroy();
//        }
//    }
//
//    class MyVideoAds implements IUnityAdsListener {
//
//        public static final String COPIED="Copied";
//        public static final String GENERATING_TOKEN="Generating Token...";
//        public static final String CONNECTED="CONNECTED";
//        public static final String EXPIRED_PLEASE_LOGIN_AGAIN="Session Expired! Please login again.";
//
//        @Override
//        public void onUnityAdsReady(String s) {
//
//        }
//
//        @Override
//        public void onUnityAdsStart(String s) {
////            String substring=UUID.randomUUID().toString().substring(0, 8);
//            String val=MySharedPref.getCookiee(ServiceActivity.this);
//            ServiceActivityCall.updateUserMeta(ServiceActivity.this, val, val, (success, token) -> {
//                if (success) {
//                    mtoken=EV.encryptAndEncode(token);
//                } else {
//                    Dialog dialog=new Dialog(ServiceActivity.this);
//                    dialog.setContentView(R.layout.logout_dialog);
//                    ImageView button=dialog.findViewById(R.id.loginbtnPopup);
//                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    dialog.show();
//                    button.setOnClickListener(v -> {
//                        startActivity(new Intent(ServiceActivity.this, SplashScreen.class));
//                        finishAffinity();
//                    });
////                    ToastMy.errorToast(ServiceActivity.this, EXPIRED_PLEASE_LOGIN_AGAIN, ToastMy.LENGTH_LONG);
//                }
//            });
//        }
//
//        @Override
//        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
//            if (finishState == UnityAds.FinishState.COMPLETED) {
//                isadready=false;
//                if (!mtoken.isEmpty()) {
//                    cardView.setVisibility(View.VISIBLE);
//                    cardView.setOnClickListener(v -> {
//                        android.content.ClipboardManager clipboard=(android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                        android.content.ClipData clip=android.content.ClipData.newPlainText(mtoken, mtoken);
//                        clipboard.setPrimaryClip(clip);
//                        ToastMy.successToast(ServiceActivity.this, COPIED, ToastMy.LENGTH_LONG);
//                    });
//                    tokenText.setText(mtoken);
//                    clickheretocopy.setText(TOKEN_GENERATED_CLICK_TO_COPY);
//                }
//            }
//        }
//
//        @Override
//        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
//            if (unityAdsError == UnityAds.UnityAdsError.AD_BLOCKER_DETECTED){
//                Log.e(TAG, "onUnityAdsError: "+s );
//            }
//            if (unityAdsError == UnityAds.UnityAdsError.NOT_INITIALIZED || unityAdsError == UnityAds.UnityAdsError.INITIALIZE_FAILED) {
//                UnityAds.initialize(ServiceActivity.this, VideoActivity.U_GAME_ID, VideoActivity.MODE);
//            }
//        }
//    }
//
//    private void init() {
//        cardView=findViewById(R.id.mcardview);
//        clickheretocopy=findViewById(R.id.genText);
//        copy_paste_imageview=findViewById(R.id.mimage);
//        tokenText=findViewById(R.id.mtext);
//        recyclerView=findViewById(R.id.sf_recyclerview);
//        linearLayout=findViewById(R.id.serbanner);
//        toolbar=(Toolbar) findViewById(R.id.serviceToolbar);
//        generateText=(Typewriter) findViewById(R.id.serText);
//        clicktocopy=(Typewriter) findViewById(R.id.clicktocopy);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        UnityAds.removeListener(videoAds);
//        if (bannerView != null) {
//            bannerView.destroy();
//        }
//    }
//}