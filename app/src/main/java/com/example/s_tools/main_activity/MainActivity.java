package com.example.s_tools.main_activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.example.s_tools.R;
import com.example.s_tools.Splash_login_reg.SplashScreen;
import com.example.s_tools.TinyDB;
import com.example.s_tools.chatting.ChatKaro;
import com.example.s_tools.chatting.ChatkaroApi;
import com.example.s_tools.entertainment.VideoActivity;
import com.example.s_tools.main_activity._model.Model;
import com.example.s_tools.main_activity._model.MyAdapter;
import com.example.s_tools.main_activity.mainactivity_toolbar_api.Changename;
import com.example.s_tools.premium.Dashboard_1;
import com.example.s_tools.testing.DownloadActivity;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.Typewriter;
import com.example.s_tools.tools.Validation;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.example.s_tools.tools.retrofitcalls.VC;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.s_tools.entertainment.VideoActivity.AUTOUPDATE;
import static com.example.s_tools.tools.Cvalues.CHATMESSAGECOUNT;
import static com.example.s_tools.tools.Cvalues.SUCCESS;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView imp_msg1, imp_msg2, imp_msg3, news;
    ImageView imageView;
    Animation movedown;
    ObjectAnimator a;
    AnimatorSet animationset;
    long back_pressed;
    private Toolbar mActionBarToolbar;
    Typewriter typewriter;
    private boolean animationStarted=false;
    private Handler mHandler=new Handler();
    ProgressDialog progressDialog;
    List<Model> models;
    Dashboard_1 dashboard_1;
    //    ShineButton button;
    KProgressHUD show;
    TinyDB tinyDB;
    MyAdapter myAdapter;
    int messageCount=0;
    TextView messageCountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        button=findViewById(R.id.po_image2);
//        button.init(this);
        tinyDB=new TinyDB(MainActivity.this);
        //afterLogin();
//        SharedPreferences sharedPreferences =new SecurePreferences(MainActivity.this,MySharedPref.activity,
//                SYSTEMCR_XML);
//        sharedPreferences.edit().remove(MySharedPref.RECENT).apply();
//        sharedPreferences.edit().remove(MySharedPref.NETFLIX).apply();
//        sharedPreferences.edit().remove(MySharedPref.BOLLYWOOD).apply();
//        sharedPreferences.edit().remove(MySharedPref.HOLLYWOOD).apply();
//        sharedPreferences.edit().remove(MySharedPref.OTHERS).apply();
//        ChangeStatusBarColor.changeStatusbarColor(MainActivity.this, R.color.colorBackgroundAboutLight);
        //autotype example
//         typewriter = new Typewriter(this);
//        imp_msg1.setCharacterDelay(100);
//        imp_msg1.animateText("y for everyohe stefjsnjfasdfasdfsdfasefjsnjfasdfasdfsdfase fnei");

        getid();
        new MyAsyncTask().execute();


//        MySharedPref.isValid_checkCookie(MainActivity.this, new MySharedPref.ApiCallback() {
//            @Override
//            public void onResponse(boolean success) {
//                if (success){
//                    Log.e(TAG, "onResponse: success" );
//                }else {
//                    GotoLoginFragment();
//                }
//            }
//        });


    }

    private void afterLogin() {
        if (!MySharedPref.isSharedPrefnull(MainActivity.this)) {
            new Thread(() -> {
                int randomNum = new Random().nextInt((10 - 0)+1) ;
                if (randomNum==0){
                    VC.check(MainActivity.this, (success, updateinfo) -> {
                    });
                }
            }).start();
            autoUpdateDialog();
        }
    }

    private void autoUpdateDialog() {
        int isautoupdateyes=tinyDB.getInt(AUTOUPDATE);
        if (isautoupdateyes == 1) {
            show=KProgressHUD.create(MainActivity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Updating...").setCancellable(true).setDimAmount(0.5f).show();
            //updateChat();
        }

    }

    private void updateChat() {
        //chat
        if (messageCountView != null) {
            ChatkaroApi.getMessageCount(MainActivity.this,MySharedPref.getTokenMessages(MainActivity.this), MySharedPref.getChatid(MainActivity.this), (success, size) -> {
                if (success) {
                    if (tinyDB.getInt(CHATMESSAGECOUNT) != size) {
//                        NotificationHelper.showChatnotifiaction(MainActivity.this);
                        tinyDB.putInt(CHATMESSAGECOUNT, size - tinyDB.getInt(CHATMESSAGECOUNT));
                        messageCountView.setVisibility(View.VISIBLE);
                        messageCountView.setText(String.valueOf(tinyDB.getInt(CHATMESSAGECOUNT)));
                    }
                }
            });
        }

    }

    private void gettitleBarName() {
        String name=MySharedPref.getName(MainActivity.this);
        getSupportActionBar().setTitle(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboardmenu, menu);
        if (MySharedPref.isSharedPrefnull(MainActivity.this)) {
            getSupportActionBar().setSubtitle("");
            MenuItem item=menu.findItem(R.id.menusetting);
            item.setVisible(false);
        }
        View menuItem=menu.findItem(R.id.menusms2).getActionView();
        View download=menu.findItem(R.id.downloadmanager).getActionView();
        download.setOnClickListener(view -> {
            Intent intent=new Intent(MainActivity.this, DownloadActivity.class);
//                intent.putExtra("intentdownloadlink","https://imagetot.com/images/2020/10/21/dcef8d0a33de61fff33f609af18e3f24.jpg");
            startActivity(intent);
        });
        messageCountView=(TextView) menuItem.findViewById(R.id.my_badge);
        ImageView imageView=menuItem.findViewById(R.id.smsmenuimg);
        imageView.setOnClickListener(view -> {
            if (!MySharedPref.isSharedPrefnull(MainActivity.this)){
                startActivity(new Intent(MainActivity.this, ChatKaro.class));
                messageCountView.setVisibility(View.GONE);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menusetting) {
            Dialog dialog=new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.mainactivity_setting_pop_changename);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Button changename=dialog.findViewById(R.id.changenamebtn);
            TextView textView=dialog.findViewById(R.id.titlePopup);
            TextView submit_changename=dialog.findViewById(R.id.submit_changename);
            EditText editText=dialog.findViewById(R.id.enterName);
            ProgressBar progressbar=dialog.findViewById(R.id.chnagenameProgressbar);
            CardView cardView=dialog.findViewById(R.id.login_btn);
            ImageView changenameImage=dialog.findViewById(R.id.changenameimg);

            LinearLayout logoutLayout=dialog.findViewById(R.id.logoutview);
            Button logout=dialog.findViewById(R.id.logoutbtn);

            logout.setOnClickListener(v -> {
                MySharedPref.clearLogin(MainActivity.this);
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            });

            changename.setOnClickListener(v -> {
                changenameImage.setVisibility(View.GONE);
                logoutLayout.setVisibility(View.GONE);
                textView.setText("Change Name");
                changename.setVisibility(View.GONE);
                editText.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);
            });
            cardView.setOnClickListener(v -> {
                cardView.setEnabled(false);
                submit_changename.setText("Please Wait..");

                boolean isvalidName=Validation.isvalidName(editText.getText().toString(), MainActivity.this);
                if (isvalidName) {
                    progressbar.setVisibility(View.VISIBLE);
                    Changename.nameChange(MainActivity.this, editText.getText().toString(), new MySharedPref.ApiCallback() {
                        @Override
                        public void onResponse(boolean success) {
                            if (!success) {
                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, Cvalues.SESSION_EXPIRED, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, SplashScreen.class));
                                finish();
                            } else {
                                progressbar.setVisibility(View.GONE);
                                mActionBarToolbar.setTitle(editText.getText().toString());
                                MySharedPref.putName(MainActivity.this, editText.getText().toString());
                                Toast.makeText(MainActivity.this,SUCCESS, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                        }
                    });
                } else {
                    cardView.setEnabled(true);
                    submit_changename.setText("Submit");
                }
            });
            dialog.show();
        }
        return false;
    }

    private void impmsgshowing() {
//        news.setText("Please Read This");
//        imp_msg1.setText("\u25CF" + " Upcomming Updates Only On evrsh.blogspot.com");
//        imp_msg2.setText("\u25CF" + " Any Query? Message Us.");
//        imp_msg3.setText("\u25CF" + " Use VPN if Something Don't Work (ex: Initializing,Movies,Generate Token)");
//         blinkText();
//        imageView.setColorFilter(ActivityCompat.getColor(MainActivity.this, android.R.color.white));
//        movedown =AnimationUtils.loadAnimation(MainActivity.this,R.anim.dashboard_imp_news);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    Thread.sleep(500);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////        imp_msg1.startAnimation(movedown);
////
////                            imp_msg2.startAnimation(movedown);
////
////
////                            imp_msg3.startAnimation(movedown);
////
//                            //imp_msg3.setText("~ now stuff enjnfjsnjfasdfasdfsdfasefjsnjfasdfasdfsdfase fneinfei efnjenfef efjenfe fee fefnefjsnjfasdfasdfsdfasefjsnjfasdfasdfsdfase fneinfei efnjenfef efjenfe fee fefnefjsnjfasdfasdfsdfasefjsnjfasdfasdfsdfase fneinfei efnjenfef efjenfe fee fefnefjsnjfasdfasdfsdfasefjsnjfasdfasdfsdfase fneinfei efnjenfef efjenfe fee fefnefjsnjfasdfasdfsdfasefjsnjfasdfasdfsdfase fneinfei efnjenfef efjenfe fee fefnand submit yveryone.");
//
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }

    private void getid() {
        recyclerView=findViewById(R.id.recycle);
//        mActionBarToolbar=findViewById(R.id.mainactivity_toolbar);
//        setSupportActionBar(mActionBarToolbar);
//        dashboard_1=new Dashboard_1();
    }


    private class MyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  gettitleBarName();
            //  impmsgshowing();
        }

        @Override
        protected String doInBackground(String... strings) {
            models=new ArrayList<>();
            models.add(new Model(R.drawable.ic_baseline_refresh_24, "Premium Posts", "Tools,Premium Accounts,Earn Money etc"));
            models.add(new Model(R.drawable.entertainment3,  "Entertainment", "Movies,Videos,Wallpapers,Images etc"));
            models.add(new Model(R.drawable.settings,  "Services", "Token,Youtube,Instagram Downloader etc"));
//            models.add(new Model(R.drawable.gift,  "User Requests", "Requested Thing Goes Here!"));
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,3));
            myAdapter=new MyAdapter(models);
            recyclerView.setAdapter(myAdapter);

            RecycleClick.addTo(recyclerView).setOnItemClickListener((recyclerView, i, view) -> {
                if (i == 0) {
                    if (MySharedPref.isSharedPrefnull(MainActivity.this)) {
                        startActivity(new Intent(MainActivity.this, SplashScreen.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, Dashboard_1.class));
                    }
                }
                if (i == 1) {
                    startActivity(new Intent(MainActivity.this, VideoActivity.class));
                }
                if (i == 2) {
//                    startActivity(new Intent(MainActivity.this, ServiceActivity.class));
                }
                if (i == 3) {
//                    startAppAd.showAd();

//                    Toast.makeText(MainActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(MainActivity.this,SplashScreen.class));
                    String murl="https://i.imgur.com/CnCMxBn.jpg";
                    Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
                    intent.putExtra("intentdownloadlink",murl);
                    startActivity(intent);
                }
            });
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
//    public void adSetup() {
//        new Thread(() -> {
//            if (!UnityAds.isInitialized()){
//                UnityAds.initialize(MainActivity.this,GAME_ID, MODE);
//            }
//            StartAppSDK.init(MainActivity.this, APP_ID, false);
//            StartAppSDK.setUserConsent (MainActivity.this, "pas",
//                    System.currentTimeMillis(),
//                    true);
//            StartAppSDK.setTestAdsEnabled(ADS_ENABLED);
//            StartAppAd.disableAutoInterstitial();
//            StartAppAd.disableSplash();
//        }).start();
//    }

}