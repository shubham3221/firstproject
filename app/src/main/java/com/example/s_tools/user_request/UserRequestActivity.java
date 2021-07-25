package com.example.s_tools.user_request;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.example.s_tools.R;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.user_request.bb.Bbadapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdLockScreen;
import cn.jzvd.JzvdStd;


public class UserRequestActivity extends AppCompatActivity {
    public static final String CLICK_TO_PLAY="Success. Click to play";
    public static final String TAG="//";
    RecyclerView recyclerView;
    Bbadapter adapter;
    TextView status;

    AdListner adListener=new AdListner();
    AdView adView;
    String anonUrl;
    List<String> streamingLinks=new ArrayList<>();
    JzvdLockScreen player;
    String mtitle;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    ImageView onoffImg;
    ProgressBar progressBar;
    ConstraintLayout upperlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);
        player=(JzvdLockScreen) findViewById(R.id.player);
        init();
        String anonlink=getIntent().getStringExtra("directdownloadlink");
        String googleSteamingLink=getIntent().getStringExtra("google_steaming_link");
        mtitle=getIntent().getStringExtra("title");
        String direct=getIntent().getStringExtra("direct");
        if (anonlink != null && direct == null) {
            anonUrl=anonlink;
            new MyasyncTask_MOVIES().execute();
        } else if (direct != null) {
            upperlayout.setVisibility(View.GONE);
            player.setVisibility(View.VISIBLE);
            player.setUp(direct, mtitle != null ? mtitle : "");
        }else if (googleSteamingLink!=null){
            upperlayout.setVisibility(View.GONE);
            player.setVisibility(View.VISIBLE);
            player.setUp(googleSteamingLink,mtitle!=null? mtitle:"");
            Picasso.get().load(getIntent().getStringExtra("thumbnail")!=null? getIntent().getStringExtra("thumbnail"):"")
                    .into(player.posterImageView);
        }


        new Handler(Looper.getMainLooper()).postDelayed(this::adsetup, 150);

    }

//    private boolean checkSystemWritePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Settings.System.canWrite(UserRequestActivity.this)) return true;
//            else openAndroidPermissionsMenu();
//        }
//        return false;
//    }
//
//    private void openAndroidPermissionsMenu() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Intent intent=new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//            intent.setData(Uri.parse("package:" + getPackageName()));
//            startActivity(intent);
//        }
//    }

    class MyasyncTask_MOVIES extends AsyncTask<Void, Void, Void> {
        private String zippyGen(String url) {
            Document document=null;
            try {
                document=Jsoup.connect(url).userAgent(Cvalues.USER_AGENT).referrer("http://www.google.com/").ignoreContentType(true).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String attr=document.select("video").select("source").attr("src");
            if (attr.startsWith("/")) {
                return "https:" + attr.trim();
            }
            return attr.trim();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Document document=null;
            try {
                document=Jsoup.connect(anonUrl).userAgent(Cvalues.USER_AGENT).referrer("http://www.google.com/").ignoreContentType(true).get();
                String attr=document.getElementById("download-wrapper").getElementsByClass("btn btn-primary btn-block").attr("href");
                String attr2=document.getElementById("download-wrapper").getElementsByClass("btn btn-primary btn-block btn-download-quality").attr("href");
                String attr3=document.getElementById("download-wrapper").getElementsByClass("btn btn-primary btn-block btn-download-quality").attr("href");
                String attr4=document.getElementById("download-wrapper").getElementsByClass("btn btn-primary btn-block btn-download-quality2").attr("href");

                if (attr != null) {
                    streamingLinks.add(attr);
                }
                if (!attr2.isEmpty()) {
                    streamingLinks.add(attr);
                }
                if (!attr3.isEmpty()) {
                    streamingLinks.add(attr);
                }
                if (!attr4.isEmpty()) {
                    streamingLinks.add(attr);
                }
                adapter=new Bbadapter(UserRequestActivity.this, streamingLinks);
            } catch (IOException e) {
                runOnUiThread(() -> {
                    status.setText(e.getMessage().replace("anon", ""));
                });
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            boolean b=checkSystemWritePermission();
            progressBar.setVisibility(View.GONE);
            if (!streamingLinks.isEmpty()) {
                Animation animation=AnimationUtils.loadAnimation(UserRequestActivity.this, R.anim.bounce_img);
                status.startAnimation(animation);
                status.setText("Available.");
            } else {
                status.setTextColor(getResources().getColor(R.color.yellow));
                return;
            }


//            String url="http://play.g3proxy.lecloud.com/vod/v2/MjUxLzE2LzgvbGV0di11dHMvMTQvdmVyXzAwXzIyLTExMDc2NDEzODctYXZjLTE5OTgxOS1hYWMtNDgwMDAtNTI2MTEwLTE3MDg3NjEzLWY1OGY2YzM1NjkwZTA2ZGFmYjg2MTVlYzc5MjEyZjU4LTE0OTg1NTc2ODY4MjMubXA0?b=259&mmsid=65565355&tm=1499247143&key=f0eadb4f30c404d49ff8ebad673d3742&platid=3&splatid=345&playid=0&tss=no&vtype=21&cvid=2026135183914&payff=0&pip=08cc52f8b09acd3eff8bf31688ddeced&format=0&sign=mb&dname=mobile&expect=1&tag=mobile&xformat=super";
            String url="https://www.pexels.com/video/1699798/download/";
            recyclerView.setLayoutManager(new GridLayoutManager(UserRequestActivity.this, 2));
            recyclerView.setAdapter(adapter);
            RecycleClick.addTo(recyclerView).setOnItemClickListener((recyclerView, i, view) -> {
                player.setVisibility(View.VISIBLE);
                player.setUp(streamingLinks.get(i), mtitle != null ? mtitle : "",Jzvd.SCREEN_NORMAL);
//                player.setUp(url, mtitle != null ? mtitle : "", Jzvd.SCREEN_NORMAL);
                player.titleTextView.setVisibility(View.GONE);
//                int width=Jzvd.WIDTH;
//                int height=Jzvd.HEIGHT;
//                int ratio=width/height;
//
//                player.heightRatio=(Math.abs(ratio - 4/3) < Math.abs(ratio - 16/9)) ?3:9;
//                player.widthRatio=(Math.abs(ratio - 4/3) < Math.abs(ratio - 16/9)) ?4:16;
                player.startVideo();


//                MxVideoPlayerWidget videoPlayerWidget = (MxVideoPlayerWidget) findViewById(R.id.mpw_video_player);
//                videoPlayerWidget.startPlay(streamingLinks.get(i), MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "video name");
//                videoPlayerWidget.startPlay(url, MxVideoPlayer.SCREEN_LAYOUT_NORMAL, "video name");


                //standalone player
//                VideoInfo videoInfo = new VideoInfo(url)
//                        .setTitle("test video") //config title
//                        .setAspectRatio(VideoInfo.AR_MATCH_PARENT) //aspectRatio
//                        .setShowTopBar(true)//show mediacontroller top bar
//                        .setPortraitWhenFullScreen(true);//portrait when full screen
//                GiraffePlayer.play(UserRequestActivity.this, videoInfo);

//                tcking.github.com.giraffeplayer2.VideoView videoView =(tcking.github.com.giraffeplayer2.VideoView) findViewById(R.id.video_view);
//                videoView.getVideoInfo().setBgColor(Color.BLACK).setAspectRatio(VideoInfo.AR_MATCH_PARENT)
//                        .setFullScreenAnimation(true)
//                .setTitle(mtitle != null? mtitle:"");
//                videoView.setVideoPath(streamingLinks.get(i).replaceAll(" ","%20")).getPlayer().start();
//                videoView.setVideoPath(url).getPlayer().start();


//                AndExoPlayerView andExoPlayerView=findViewById(R.id.andExoPlayerView);
//                andExoPlayerView.setSource(streamingLinks.get(i));
//                andExoPlayerView.setPlayWhenReady(true);
//                andExoPlayerView.setResizeMode(EnumResizeMode.FIT);
//                andExoPlayerView.setShowControllers(true);
//                andExoPlayerView.setVisibility(View.VISIBLE);

            });


        }
    }


    private void init() {
        progressBar=findViewById(R.id.check_progressbar);
        appBarLayout=findViewById(R.id.urAppbar);
        toolbar=findViewById(R.id.urToolbar);
        onoffImg=findViewById(R.id.onlineoff);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.bbrecycler);
        status=findViewById(R.id.status);
        upperlayout=findViewById(R.id.uperlayout);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
//        if (Jzvd.backPress()) {
//            return;
//        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    class AdListner extends AdListener {

        @Override
        public void onAdImpression() {
            super.onAdImpression();
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
        }

        @Override
        public void onAdFailedToLoad(LoadAdError loadAdError) {
            super.onAdFailedToLoad(loadAdError);
            status.setText(Cvalues.OFFLINE);
            status.setTextColor(getResources().getColor(R.color.unused_color));
        }
    }

    private void adsetup() {
        adView=new AdView(UserRequestActivity.this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Cvalues.banr);
        adView=findViewById(R.id.adView);
        adView.setVisibility(View.VISIBLE);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(adListener);
    }

}