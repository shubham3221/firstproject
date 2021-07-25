package com.example.s_tools.entertainment.Fragemnts.movies.MovieDetail;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.CollectionConstants;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.UniversalActivity;
import com.example.s_tools.tools.JsoupParser;
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;


@Keep
public class MovieDetailActivity extends AppCompatActivity implements FullScreenIamgeListner {
    public static final String VIDEO_AD_NOT_SHOWING="video ad failed to show.";
    public static final String HTTP="http://";
    public static final String MOVIESRUSH="(?i)moviesrush";
    public static final String SOUTHFREAK="(?i)southfreak";
    private String moviname;
    private String image;
    private String cover;
    private String contentHtml;
    private String content;
    public static final String TAG="mtag";
    TextView title, desc;
    ImageView btw_img, cover_img;
    ScrollView nestedscviw;
    RecyclerView recyclerView;
    MovieScreenshotAdapter adapter;
    CardView imageRecyclerCardView, dialogcardview;
    ProgressBar progressBar, progressbardownload;
    LinearLayout downloadlinkcontent;

    LinearLayout linearLayout;

    private String downloadLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        init();
        new MyAsyncTask().execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    private void init() {
        nestedscviw=(ScrollView) findViewById(R.id.nestedscviw);
        title=(TextView) findViewById(R.id.titleMov);
        btw_img=(ImageView) findViewById(R.id.animate_img);
        recyclerView=findViewById(R.id.imagesofMoviesRecycler);
        desc=(TextView) findViewById(R.id.desc);
        cover_img=(ImageView) findViewById(R.id.top_image);
        imageRecyclerCardView=findViewById(R.id.imageRecyclerCardView);
        progressBar=findViewById(R.id.screenshotProgress);
        linearLayout=findViewById(R.id.bannerView_moviedetail);
    }

    private void main_cover_bg_imageLoad() {
        //main img
        Picasso.get().load(image)
//                .resize(250,320)
//                .transform(new Croptransformation(500,500, Croptransformation.GravityHorizontal.CENTER, Croptransformation.GravityVertical.BOTTOM))
                .into(btw_img);
        //cover img
        Picasso.get().load(cover).transform(new BlurTransformation(this, 10, 1))
//                .resize(250,320)
                .into(cover_img, new Callback() {
                    @Override
                    public void onSuccess() {
                        //set animation of cover image
                        cover_img.setAnimation(AnimationUtils.loadAnimation(MovieDetailActivity.this, R.anim.cover_img_animation));
                    }

                    @Override
                    public void onError(Exception e) {
                        cover_img.setImageResource(R.drawable.noimage);
                    }
                });

        //background img
        ImageView img=new ImageView(this);
        Picasso.get().load(image).transform(new BlurTransformation(this, 10, 10)).resize(250, 320).into(img, new Callback() {
            @Override
            public void onSuccess() {
                nestedscviw.setBackground(img.getDrawable());
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void setupAds() {
    }

    //recyclerAdapterClick_interface
    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public void onScreenshotClick(ImageView imageView, String urlImage) {
        new PhotoFullPopupWindow(MovieDetailActivity.this, R.layout.popup_photo_full, imageView, urlImage, null);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //asynctask
    private class MyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            moviname=getIntent().getStringExtra(CollectionConstants.MOVIE_NAME);
            cover=getIntent().getExtras().getString(CollectionConstants.COVER);
            image=getIntent().getExtras().getString(CollectionConstants.MAIN_IMAGE);
            contentHtml=getIntent().getExtras().getString(CollectionConstants.MOVIE_CONTENT);
            publishProgress();
            List<String> images=JsoupParser.parseAllImagesExpectMain(contentHtml);
            String replace="";
            adapter=new MovieScreenshotAdapter(MovieDetailActivity.this, images, progressBar, MovieDetailActivity.this);
            if (UniversalActivity.collection == 1) {
                replace=contentHtml.replaceAll(SOUTHFREAK, "BUDDY");
            } else if (UniversalActivity.collection == 2) {
                replace=movieRushSetup();
            } else {
                replace=thirdMovie(contentHtml);
            }
            return replace;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            main_cover_bg_imageLoad();
        }

        @Override
        protected void onPostExecute(String s) {
            setTextViewHTML(desc, s);
            title.setText(moviname);
            recyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapter);
            setupAds();
//            recyclerView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });
        }

    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start=strBuilder.getSpanStart(span);
        int end=strBuilder.getSpanEnd(span);
        int flags=strBuilder.getSpanFlags(span);
        ClickableSpan clickable=new ClickableSpan() {
            public void onClick(View view) {
                downloadLink=span.getURL();
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    protected void setTextViewHTML(TextView textView, String html) {
        CharSequence sequence=Html.fromHtml(html.replaceAll("<img.+?>", ""));
        SpannableStringBuilder strBuilder=new SpannableStringBuilder(sequence);
        URLSpan[] urls=strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        strBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#bcbcbc")), 0, strBuilder.length(), 0);
        textView.setText(strBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }


    String movieRushSetup() {
        String replace=contentHtml.replaceAll(MOVIESRUSH, "BUDDY");
//            String[] split=replace.split("<p><em>");
//            if (!replace.contains("<p><em>")){
//                contentHtml = replace;
//            }else {
//                contentHtml = split[0];
//            }
        return replace;

    }

    String thirdMovie(String mcontentHtml) {
        String[] split=mcontentHtml.split("<p><em>");
        String[] finalString;
        if (!mcontentHtml.contains("<p><em>")) {
            contentHtml=mcontentHtml;
        } else {
            contentHtml=split[0];
        }
        if (contentHtml.equalsIgnoreCase("share post")) {
            Log.e(TAG, "thirdMovie: it containes");
            finalString=contentHtml.split("Share post");
        } else {
            return contentHtml;
        }
        return finalString[0];

    }


}