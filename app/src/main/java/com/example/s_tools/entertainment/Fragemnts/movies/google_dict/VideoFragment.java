package com.example.s_tools.entertainment.Fragemnts.movies.google_dict;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.s_tools.R;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.user_request.UserRequestActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdLockScreen;
import cn.jzvd.JzvdStd;

public class VideoFragment extends Fragment {
    View view;
    AdView adView;
    JzvdLockScreen player;
    String link,title,thumbnnail;
    public static boolean isOpen;


    public VideoFragment() {
        // Required empty public constructor
    }
    public VideoFragment(String title,String url,String thumbnnail){
        this.title=title;
        this.link=url;
        this.thumbnnail=thumbnnail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        view= inflater.inflate(R.layout.fragment_video, container, false);
        player=(JzvdLockScreen) view.findViewById(R.id.player);
        player.setUp(link,title!=null? title:"",Jzvd.SCREEN_NORMAL);
        if (thumbnnail!=null){
            Picasso.get().load(thumbnnail)
                    .into(player.posterImageView);
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> adsetup(),200);
        return view;
    }
    private void adsetup() {
        adView=new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Cvalues.banr);
        adView=view.findViewById(R.id.adView);
        adView.setVisibility(View.VISIBLE);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        isOpen=true;
    }

    @Override
    public void onDetach() {
        isOpen=false;
        super.onDetach();
    }
}