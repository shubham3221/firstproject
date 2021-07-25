package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.Detail_Act;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MoviesPosts;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//cloud_setup_pages_text
public class DetailFragment extends Fragment {
    //    CardView loadImageCardView;
    ImageView mainImage;
    RecyclerView scRecyclerview;
    DAdapter adapter;
    TextView rating, title, genres, description,download;
    Button gotoscFragment;
    View view;
    MoviesPosts post;
    int postid;
    InterstitialAd minterstitialAd;
    View ratingGroup, genresGroup, des_layout;
    ProgressBar progressBar,download_progressbar;

    public DetailFragment() {
    }

    public DetailFragment(MoviesPosts moviesPosts, int id) {
        this.post=moviesPosts;
        this.postid=id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.detail_layout_2, container, false);
//        if (post.getCategories().get(0).getId() == 41) {
//            view=inflater.inflate(R.layout.detail_layout_2, container, false);
//        } else {
//            view=inflater.inflate(R.layout.fragment_detail_search, container, false);
//        }
        init();

        if (post != null) {
            if (post.getId() != 1) new MyasyncTask().execute();
        }
        return view;
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    private void setData() {
        if (post.getCustom_fields().getMainimage() != null) {

            Picasso.get().load(post.getCustom_fields().getMainimage().get(0).trim()).resize(600, 600).centerInside().into(mainImage, new Callback() {
                @Override
                public void onSuccess() {
                    if (getActivity() != null)
                        mainImage.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.cover_img_animation));
                }


                @Override
                public void onError(Exception e) {

                }
            });


//            if (post.getCategories().get(0).getId() == 41) {
//                Picasso.get().load(post.getCustom_fields().getMainimage().get(0).trim()).resize(600, 600).centerInside().into(mainImage);
//                ImageView cover_img=view.findViewById(R.id.top_image);
//                Picasso.get().load(post.getCustom_fields().getMainimage().get(0).trim()).transform(new BlurTransformation(getActivity(), 10, 1)).resize(300, 300).into(cover_img, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        //set animation of cover image
//                        if (getActivity() != null)
//                            cover_img.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.cover_img_animation));
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        cover_img.setImageResource(R.drawable.noimage);
//                    }
//                });
//            }
            mainImage.setOnClickListener(v -> {
                new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, mainImage, post.getCustom_fields().getMainimage().get(0).trim(), null);
            });
        }
    }

    class MyasyncTask extends AsyncTask<String, String, String> {
        String[] split;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setData();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            if (post.getCustom_fields().getScreenshots() != null) {
                List<String> screenshots=post.getCustom_fields().getScreenshots();
                String s=screenshots.get(0);
                split=s.split(",");
            } else {
                getActivity().runOnUiThread(() -> {
//                    loadImageCardView.setVisibility(View.GONE);
                    gotoscFragment.setVisibility(View.GONE);
                });
            }
            adapter=new DAdapter(getActivity(), split, progressBar);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (post.getCustom_fields().getReview() != null) {
                description.setText(post.getCustom_fields().getReview().get(0));
            }
            if (post.getTitle()!=null){
                title.setText(post.getTitle());
            }
            if (post.getCustom_fields().getRating() != null) {
                rating.setText(post.getCustom_fields().getRating().get(0));
            } else {
                ratingGroup.setVisibility(View.GONE);
            }
            if (post.getCustom_fields().getGen() != null) {
                genres.setText(post.getCustom_fields().getGen().get(0));
            } else {
                genresGroup.setVisibility(View.GONE);
            }
            scRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            scRecyclerview.setAdapter(adapter);



            download.setOnClickListener(view -> {
                download.setClickable(false);
                download.setText("Getting Links..");
                download.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                download_progressbar.setVisibility(View.VISIBLE);


                InterstitialAd.load(getActivity(), Cvalues.inter, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        minterstitialAd=interstitialAd;
                        download.clearAnimation();
                        if (interstitialAd != null && getActivity() != null) {
                            download.setText("download");
                            download_progressbar.setVisibility(View.GONE);
                            download.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_cloud_download_24,0,0,0);
                            minterstitialAd.show(getActivity());
                            download_progressbar.setVisibility(View.GONE);
                            start_activity();
                        } else {
                            minterstitialAd=null;
                        }
                        download.setClickable(true);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        if (getActivity()!=null){
                            download_progressbar.setVisibility(View.GONE);
                            download.setText("Ad failed to load! Try again");
                            download.setCompoundDrawablesWithIntrinsicBounds(R.drawable.attention1,0,0,0);
                        }
                        minterstitialAd=null;
                        download.setClickable(true);
                    }
                });
            });
            gotoscFragment.setOnClickListener(view -> {
                ScFragment someFragment=new ScFragment(split);
                someFragment.setEnterTransition(new Fade());
                someFragment.setExitTransition(new Fade());
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.detailcontainer, someFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                des_layout.setVisibility(View.VISIBLE);
                des_layout.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.popup_enter));
            }, 220);

        }
    }

    private void start_activity() {
        minterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdDismissedFullScreenContent() {
                if (getActivity()!=null){
                    Intent intent=new Intent(getActivity(), LActivity.class);
                    intent.putExtra("id", postid);
                    if (post.getCustom_fields().getDrive720() != null) {
                        intent.putExtra("drive720", new ArrayList<>(post.getCustom_fields().getDrive720()));
                    }
                    if (post.getCustom_fields().getDrive1080() != null) {
                        intent.putStringArrayListExtra("drive1080", new ArrayList<>(post.getCustom_fields().getDrive1080()));
                    }
                    if (post.getCustom_fields().getOtherlinks() != null) {
                        intent.putStringArrayListExtra("other", new ArrayList<>(post.getCustom_fields().getOtherlinks()));
                    }
                    if (post.getCustom_fields().getWatchonline() != null) {
                        intent.putStringArrayListExtra("watchonline", new ArrayList<>(post.getCustom_fields().getWatchonline()));
                    }
                    startActivity(intent);
                    minterstitialAd=null;
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when fullscreen content failed to show.
                Log.d("TAG", "The ad failed to show.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                minterstitialAd = null;
            }
        });
    }

    private void init() {
        download=view.findViewById(R.id.download_txt);
        download_progressbar=view.findViewById(R.id.downloadprogressbar);
//        loadImageCardView=view.findViewById(R.id.imagecardview);
        ratingGroup=view.findViewById(R.id.ratingGrounp);
        genresGroup=view.findViewById(R.id.genresGrounp);
        des_layout=view.findViewById(R.id.mlayout);
        progressBar=view.findViewById(R.id.progressBar2);
        mainImage=view.findViewById(R.id.mainimage);
        title=view.findViewById(R.id.titleMov);
        scRecyclerview=view.findViewById(R.id.scRecyclerview);
        description=view.findViewById(R.id.descmov);
        gotoscFragment=view.findViewById(R.id.seeallsc);
        rating=view.findViewById(R.id.rating);
        genres=view.findViewById(R.id.genres);


    }
}