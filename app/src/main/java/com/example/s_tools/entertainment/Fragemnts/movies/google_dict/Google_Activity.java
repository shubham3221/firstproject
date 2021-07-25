package com.example.s_tools.entertainment.Fragemnts.movies.google_dict;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s_tools.MyWebService;
import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.google_dict.upperRecyclerview.SliderGoogleAdapter;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.ToastMy;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import cn.jzvd.Jzvd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

interface LoadingprogressListner {
    void showProgress();

    void hideProgress(int time);

    void visibleProgress();

    void setprogress(int progress);
}
public class Google_Activity extends AppCompatActivity implements LoadingprogressListner {
    CardView backbtn;
    ProgressBar progressBar;
    TextView lastupdate, info;
    Thread thread;

    private FragmentTransaction fragmentTransaction;
    GoogleFragment fragment;
    RecyclerView upper_nav_recyclerview;
    SliderGoogleAdapter sliderGoogleAdapter;
    private int progress=2;
    private int i=0;
    private KProgressHUD show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adactivity);
        init();


        backbtn.setOnClickListener(v -> {
            finish();
        });

        geturl();

//        Adfragment adfragment=new Adfragment();
        uperRecyclerSetup();

    }

    private void geturl() {
        show=KProgressHUD.create(Google_Activity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Checking...").setCancellable(false).setDimAmount(0.5f).show();
        MyWebService myWebService=MyWebService.retrofit.create(MyWebService.class);
        myWebService.checkVersion().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                show.dismiss();
                if (response.body()!=null){
                    if (response.body().get("status").getAsString().equals("ok")) {
                        String id=response.body().getAsJsonObject("page").getAsJsonObject("custom_fields").get("googledriveurl").getAsJsonArray().get(0).getAsString();
                        fragment=new GoogleFragment(upper_nav_recyclerview, sliderGoogleAdapter, lastupdate, info, id);
//        fragment=new GoogleFragment(upper_nav_recyclerview,uppernavAdapter,lastupdate,progressBar,"1bBITkR6l1jwPM4gv1PR1OkquVpBZJ70C");
                        fragment.setRetainInstance(true);
                        fragmentTransaction=getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.adcontainer, fragment);
                        fragmentTransaction.commit();
                    }else{
                        ToastMy.errorToast(Google_Activity.this, Cvalues.OFFLINE, Toast.LENGTH_SHORT);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                show.dismiss();
                ToastMy.errorToast(Google_Activity.this, Cvalues.OFFLINE, Toast.LENGTH_SHORT);
                finish();
            }
        });
    }

    private void uperRecyclerSetup() {
        upper_nav_recyclerview.setLayoutManager(new LinearLayoutManager(Google_Activity.this, RecyclerView.HORIZONTAL, false));
        sliderGoogleAdapter=new SliderGoogleAdapter(this, new ArrayList<>());
        upper_nav_recyclerview.setAdapter(sliderGoogleAdapter);
    }

    private void init() {
        progressBar=findViewById(R.id.updatenow);
        backbtn=findViewById(R.id.backbtn);
        lastupdate=findViewById(R.id.lastupdate_adlt);
        upper_nav_recyclerview=findViewById(R.id.navi_recy);
        info=findViewById(R.id.info);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        int count=getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            if (VideoFragment.isOpen) {
                getSupportFragmentManager().popBackStack();
                return;
            }
            if (progressBar.getVisibility()==View.VISIBLE){
                hideProgress(50);
            }
            getSupportFragmentManager().popBackStack();
            GoogleFragment fragment=(GoogleFragment) getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 2);
            lastupdate.setText(fragment.list.size() + " files");
            if (sliderGoogleAdapter.items.size() >= 1) {
                sliderGoogleAdapter.items.remove(sliderGoogleAdapter.items.size() - 1);
                sliderGoogleAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showProgress() {
        progress=1;
        thread=new Thread(() -> {
            int i=0;
//            while (i < 35 && progressBar.getVisibility() != View.GONE) {
            while (i < 35 && progress!=0) {
                i++;
                try {
                    Thread.sleep(60);
                    runOnUiThread(() -> {
                        Log.e("//", "showProgress: 1st" );
                        if (progress != 0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                progressBar.setProgress(progress++, true);
                            } else {
                                progressBar.setProgress(progress++);
                            }
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i=0;
//            while (i < 35 && progressBar.getVisibility() != View.GONE) {
            while (i < 35 && progress!=0) {
                i++;
                try {
                    Thread.sleep(800);
                    runOnUiThread(() -> {
                        Log.e("//", "showProgress: 2nd" );
                        if (progress != 0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                progressBar.setProgress(progress++, true);
                            } else {
                                progressBar.setProgress(progress++);
                            }
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void hideProgress(int time) {
        if (progressBar.getVisibility()==View.VISIBLE){
            Log.e("//", "hideProgress: " );
            thread.interrupt();
            progress=0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress(100, true);
            } else {
                progressBar.setProgress(100);
            }
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                progressBar.setVisibility(View.GONE);
                Animation manimation=AnimationUtils.loadAnimation(Google_Activity.this, R.anim.blink_text);
                progressBar.startAnimation(manimation);
                manimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            progressBar.setProgress(0, true);
                        } else {
                            progressBar.setProgress(0);
                        }
                        manimation.reset();

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }, time);
        }
    }

    @Override
    public void visibleProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void setprogress(int progress) {
        progressBar.setProgress(progress);
    }
}