package com.example.s_tools.entertainment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.transition.Fade;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.s_tools.HowDownFragment;
import com.example.s_tools.MainActivity3;
import com.example.s_tools.NotificationHelper;
import com.example.s_tools.R;
import com.example.s_tools.Services_item.VidDownloader.VideoDownloader;
import com.example.s_tools.Services_item.inst.InstFragment;
import com.example.s_tools.Services_item.mp3download.Mp3Downloader;
import com.example.s_tools.Splash_after_reg;
import com.example.s_tools.Splash_login_reg.SplashScreen;
import com.example.s_tools.TinyDB;
import com.example.s_tools.chatting.ChatKaro;
import com.example.s_tools.chatting.ChatkaroApi;
import com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.ImagesActivity;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.Detail_Act.ScFragment;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.SearchMov;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.SectionsFragment;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MoviesPosts;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.Fragment_Wallpapers;
import com.example.s_tools.entertainment.uper_slider.SliderPage_Adapter;
import com.example.s_tools.premium.Dashboard;
import com.example.s_tools.premium.Dashboard_1;
import com.example.s_tools.premium.Dialing;
import com.example.s_tools.testing.DownloadActivity;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.MyAsyncTask;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.example.s_tools.tools.retrofitcalls.VC;
import com.example.s_tools.user_request.bb.OnlineModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.s_tools.tools.Cvalues.CHATMESSAGECOUNT;

public class VideoActivity extends AppCompatActivity implements ScFragment.HideToolbar {
    public static final String AUTOUPDATE="isautoupdateyes";
    public static final String ISOPENMESSAGES="isopenmessages";
    public static final String TAG="//";
    public static final String LASTCHECK="lastcheck";
    public static final String NEWPREMIUMPOST="newpremiumpost";
    private ViewPager viewPagerUp;
    private TabLayout indicator;
    private SliderPage_Adapter upperSliderAdapter;
    private SectionsFragment sectionsFragment;
    private FragmentTransaction fragmentTransaction;
    private CardView searchBar;
    private ImageView back;
    private View toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    private LinearLayout  chatkaro, downloadActivity, openDrawerTool, updateMoviesBtn;
    View premium;
    private TinyDB tinyDB;
    private TextView messageCountView, updateText;
    private InterstitialAd mInterstitialAd;
    private ImageView notify_icon_premium_post;

    private boolean adLoaded=false;
    ViewAnimator viewAnimator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        init();
        // Load the ViewAnimator and display the first layout
        viewAnimator=(ViewAnimator) findViewById(R.id.view_animator);
        CharSequence relativeTimeSpanString=DateUtils.getRelativeTimeSpanString(new Date().getTime());





        if (MySharedPref.isVersionOut(VideoActivity.this) | tinyDB.getLong("lastcheck") == 0) {
            startActivity(new Intent(VideoActivity.this, Splash_after_reg.class));
            finishAffinity();
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Splash_after_reg.fill_string();
                new MyTask().execute();
                afterLogin();
                MobileAds.initialize(this, initializationStatus -> {
                    RequestConfiguration configuration=new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("0C9EBB7EE878C7C3529A8D806DC4FFCA", "EDB30A4A00115883E90E49CAD034A12C")).build();
                    MobileAds.setRequestConfiguration(configuration);
                });
                ConstraintLayout lll;
                lll=findViewById(R.id.lll);
                viewAnimator.removeView(lll);

            }, 500);
        }
//            show=KProgressHUD.create(VideoActivity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Updating...").setCancellable(true).setDimAmount(0.5f).show();
//
    }

    class MyTask extends MyAsyncTask {
        @Override
        protected void doInBackground() {
//            setup_ad();
            if (MySharedPref.getMoviesList(VideoActivity.this, MySharedPref.RECENT) != null) {
                slider_movies();
            }else {
                new Thread(() -> {
                    boolean checking=true;
                    while (checking){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (MySharedPref.getMoviesList(VideoActivity.this,MySharedPref.RECENT)!=null){
                            checking=false;
                        }
                    }
                    slider_movies();
                }).start();
            }
            if (tinyDB.getLong(LASTCHECK) == 0) {
                new Thread(() -> {
                    VC.check_v(VideoActivity.this, (success, updateinfo) -> {
                        if (success) {
                            tinyDB.putLong(LASTCHECK, System.currentTimeMillis());
                        }
                    });
                }).start();
            } else {
                new Thread(() -> {
                    long now=System.currentTimeMillis();
                    long diff=now - tinyDB.getLong(LASTCHECK);
                    if (diff >= (3600000 * 24)) {
                        VC.check_v(VideoActivity.this, (success, updateinfo) -> {
                            if (success) {
                                tinyDB.putLong(LASTCHECK, now);
                            }
                        });
                    }
                }).start();
            }
            sectionsFragment=new SectionsFragment(mInterstitialAd);
            fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.mviewpager, sectionsFragment);

            drawerToggle=new ActionBarDrawerToggle(VideoActivity.this, drawerLayout, R.string.open, R.string.close);
            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();
        }

        private void slider_movies() {
            new Thread(() -> {
                List<OnlineModel> onlineModels=new ArrayList<>();
                List<MoviesPosts> moviesList=MySharedPref.getMoviesList(VideoActivity.this, MySharedPref.RECENT);
                for (int i=0; i < moviesList.size(); i++) {
                    if (moviesList.get(i).getCustom_fields().getOtherlinks() != null) {
                        String[] split=moviesList.get(i).getCustom_fields().getOtherlinks().get(0).split(",");
                        for (String link : split) {
                            if (link.contains("anonfiles")) {
                                if (moviesList.get(i).getCustom_fields().getMainimage() != null) {
                                    onlineModels.add(new OnlineModel(link.trim(), moviesList.get(i).getTitle().trim(), moviesList.get(i).getCustom_fields().getMainimage().get(0).trim(), moviesList.get(i).getDate()));
                                }
                            }
                        }
                    }
                }
                if (!onlineModels.isEmpty()) {
                    upperSliderAdapter=new SliderPage_Adapter(VideoActivity.this, onlineModels);
                    toBeContinue();
                }
            }).start();
        }

        @Override
        protected void pendingTask() {
            indicator.setupWithViewPager(viewPagerUp, false);
            viewPagerUp.setAdapter(upperSliderAdapter);
        }


        @Override
        protected void onPostExecute() {
            searchBar.setOnClickListener(view -> {
                Intent intent=new Intent(VideoActivity.this, SearchMov.class);
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(VideoActivity.this, searchBar, "cardview");
                startActivity(intent, options.toBundle());
            });
            navigtionDrawer();
            sectionsFragment=new SectionsFragment(mInterstitialAd);
            fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.mviewpager, sectionsFragment);
            fragmentTransaction.commit();
            premium.setOnClickListener(view -> {
                tinyDB.putBoolean(NEWPREMIUMPOST,false);
                notify_icon_premium_post.setVisibility(View.GONE);
                startActivity(new Intent(VideoActivity.this, Dashboard.class));
            });
            chatkaro.setOnClickListener(view -> {
                if (!MySharedPref.isSharedPrefnull(VideoActivity.this)) {
                    if (messageCountView.getText().length() >= 1) {
                        messageCountView.setText("");
                        messageCountView.setBackground(ContextCompat.getDrawable(VideoActivity.this, R.drawable.sms2));
                    }

                    startActivity(new Intent(VideoActivity.this, ChatKaro.class));
                } else {
                    startActivity(new Intent(VideoActivity.this, SplashScreen.class));
                }
            });
            downloadActivity.setOnClickListener(view -> {
                startActivity(new Intent(VideoActivity.this, DownloadActivity.class));
            });
            openDrawerTool.setOnClickListener(view -> {
                drawerLayout.openDrawer(GravityCompat.START);
            });
            updateMoviesBtn.setOnClickListener(view -> {
                Animation sgAnimation=AnimationUtils.loadAnimation(VideoActivity.this, R.anim.shrink);
                view.startAnimation(sgAnimation);
                updateAll();
            });
            back.setOnClickListener(view -> {
                drawerLayout.openDrawer(GravityCompat.START);
            });

        }
    }

    private void updateAll() {
        updateText.setText("Updating");
        updateMoviesBtn.setClickable(false);
        Animation animation=AnimationUtils.loadAnimation(VideoActivity.this, R.anim.rotate_view);
        updateMoviesBtn.startAnimation(animation);
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, Cvalues.inter, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull @NotNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                interstitialAd.show(VideoActivity.this);
                adLoaded=false;
                updateMoviesBtn.clearAnimation();
                ToastMy.successToast(VideoActivity.this, Cvalues.UPDATING, Toast.LENGTH_SHORT);
//                sliderImagesApi();
                sectionsFragment=new SectionsFragment(true, mInterstitialAd);
                fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mviewpager, sectionsFragment);
                fragmentTransaction.commit();
                updateMoviesBtn.setClickable(true);
                updateText.setText("Success");
                updateMoviesBtn.clearAnimation();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    updateText.setText("Update");
                    updateMoviesBtn.setClickable(true);
                }, 1000);


            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                updateText.setText("Failed");
                updateMoviesBtn.clearAnimation();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    updateText.setText("Update");
                    updateMoviesBtn.setClickable(true);
                }, 1000);

            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void navigtionDrawer() {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item1:
                    startActivity(new Intent(VideoActivity.this, ChatKaro.class));
                    break;
                case R.id.item2:
                    VC.check(VideoActivity.this, (success, updateinfo) -> {
                    });
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(VC.BLOGSPOT_COM)));
                    break;
                case R.id.item3:
                    startActivity(new Intent(VideoActivity.this, Mp3Downloader.class));
                    break;
                case R.id.item4:
                    startActivity(new Intent(VideoActivity.this, VideoDownloader.class));
                    break;
                case R.id.item5:
                    InstFragment someFragment=new InstFragment();
                    someFragment.setEnterTransition(new Fade());
                    someFragment.setExitTransition(new Fade());
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.mycontainer, someFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                case R.id.item7:
                    //4k wallper
                    Fragment_Wallpapers wallpapers=new Fragment_Wallpapers();
                    wallpapers.setEnterTransition(new Fade());
                    wallpapers.setExitTransition(new Fade());
                    FragmentTransaction fragmentTransaction2=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.add(R.id.mycontainer, wallpapers);
                    fragmentTransaction2.addToBackStack(null);
                    fragmentTransaction2.commit();
                    break;
                case R.id.item8:
                    //image search
                    startActivity(new Intent(VideoActivity.this, ImagesActivity.class));
                    break;
                case R.id.item9:
                    //image search
                    startActivity(new Intent(VideoActivity.this, MainActivity3.class));
                    break;
                case R.id.itemhowtodonwload:
                    HowDownFragment how=new HowDownFragment();
                    how.setEnterTransition(new Fade());
                    how.setExitTransition(new Fade());
                    FragmentTransaction fragmentTransaction3=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.add(R.id.mycontainer, how);
                    fragmentTransaction3.addToBackStack(null);
                    fragmentTransaction3.commit();
                    break;
            }
            drawerLayout.closeDrawers();
            return false;
        });
    }

    private void init() {
        viewPagerUp=findViewById(R.id.slider_pager);
        indicator=findViewById(R.id.indicator1);
        searchBar=findViewById(R.id.expendedcarf);
        back=findViewById(R.id.toolimg);
        premium=findViewById(R.id.ppost);
        chatkaro=findViewById(R.id.cpost);
        downloadActivity=findViewById(R.id.dpost);
        openDrawerTool=findViewById(R.id.tpost);
        updateMoviesBtn=findViewById(R.id.upost);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        messageCountView=findViewById(R.id.chatkaro);
        updateText=findViewById(R.id.update);
        tinyDB=new TinyDB(VideoActivity.this);
        notify_icon_premium_post=findViewById(R.id.notifi);
    }

    private void afterLogin() {
        if (!MySharedPref.isSharedPrefnull(VideoActivity.this)) {
            int isautoupdateyes=tinyDB.getInt(VideoActivity.AUTOUPDATE);
            if (isautoupdateyes == 1) {
                updateChat();
            }
        }
        if (tinyDB.getBoolean(NEWPREMIUMPOST)){
            notify_icon_premium_post.setVisibility(View.VISIBLE);
        }else {
            check_Premiumpost();
        }
    }
    private void check_Premiumpost() {
        Dialing.getData(VideoActivity.this,1,1, (success, body) -> {
            if (success) {
                int id=body.getPosts().get(0).getId();
                if (id!=tinyDB.getInt(Dashboard_1.POSTID)){
                    tinyDB.putBoolean(NEWPREMIUMPOST,true);
                    notify_icon_premium_post.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateChat() {
        if (!tinyDB.getBoolean(ISOPENMESSAGES)) {
            ChatkaroApi.getMessageCount(VideoActivity.this, MySharedPref.getTokenMessages(VideoActivity.this), MySharedPref.getChatid(VideoActivity.this), (success, size) -> {
                if (success) {
                    if (tinyDB.getInt(CHATMESSAGECOUNT) != size) {
                        //show notification
                        NotificationHelper helper=new NotificationHelper();
                        helper.showChatnotifiaction(VideoActivity.this);
                        tinyDB.putInt(CHATMESSAGECOUNT, size - tinyDB.getInt(CHATMESSAGECOUNT));
                        messageCountView.setBackground(ContextCompat.getDrawable(VideoActivity.this, R.drawable.shape_notification));
                        messageCountView.setText(String.valueOf(tinyDB.getInt(CHATMESSAGECOUNT)));
                        tinyDB.putBoolean(ISOPENMESSAGES, true);
                    }
                }
            });
        } else {
            messageCountView.setBackground(ContextCompat.getDrawable(VideoActivity.this, R.drawable.shape_notification));
            messageCountView.setText(String.valueOf(tinyDB.getInt(CHATMESSAGECOUNT)));
        }
    }

    @Override
    public void onBackPressed() {
        int count=getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            finishAffinity();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }


    @Override
    public void hideToolbar() {
        toolbar=findViewById(R.id.toolbarvid);
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }
}
