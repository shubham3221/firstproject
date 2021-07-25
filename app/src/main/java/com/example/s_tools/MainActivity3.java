package com.example.s_tools;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.s_tools.entertainment.Fragemnts.movies.Dashboard_Google;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.Typewriter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity3 extends AppCompatActivity {

    private RewardedAd rewardedAd;
    Typewriter typewriter;
    Typewriter textView;
    Button button;
    private boolean activity = true;
    private int i=29;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        textView = findViewById(R.id.textView3);
        button = findViewById(R.id.btnthnak);
        typewriter = new Typewriter(this);
        textView.setCharacterDelay(80);
        textView.animateText("Support My Work By Watching Ads. This will help for continue our service. ");

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity3.this, Dashboard_Google.class);
//                setResult(RESULT_OK,intent);
            }
        });

        setupad();
        new Handler().postDelayed(this::setupad2,400);

        InterstitialAd.load(this,Cvalues.inter, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                mInterstitialAd.show(MainActivity3.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });

        new Thread(() -> {
            while (activity){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> {
                    if (i==0){
                        activity = false;
                        button.setText("thank you");
                        return;

                    }
                    button.setText(String.valueOf(i));
                    i-=1;
                });
            }
        }).start();

    }
    void setupad(){
        AdView adView=new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(Cvalues.banr);
        adView=findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
    void setupad2(){
        AdView adView=new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(Cvalues.banr);
        adView=findViewById(R.id.adView2);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = false;
    }
}