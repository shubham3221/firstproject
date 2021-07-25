package com.example.s_tools.entertainment.Fragemnts.movies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.example.s_tools.MainActivity3;
import com.example.s_tools.R;
import com.example.s_tools.testing.DownloadActivity;
import com.example.s_tools.tools.ImageViewerListner;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.common.extensions.ImageViewKt;
import com.stfalcon.imageviewer.listeners.OnImageChangeListener;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.example.s_tools.tools.Cvalues.ISUNIQUEPATH;


public class Dashboard_Google extends AppCompatActivity {
    public static final String TAG="//";
    List<String> images=new ArrayList<>();
    ViewPager viewPager;
//    ImageViewerListner listner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_google);
        images.add("http://i.imgur.com/CqmBjo5.jpg");
        images.add("http://i.imgur.com/zkaAooq.jpg");
        images.add("http://i.imgur.com/0gqnEaY.jpg");

        int i=0;
        int b=0;
        Log.e(TAG, "onCreate: " + i);

        b=i++;
        Log.e(TAG, "onCreate: " + b);

        b=++i;
        Log.e(TAG, "onCreate: " + b);

    }

    public void gotodownlaoder(View view) {
//        startActivityForResult(new Intent(Dashboard_Google.this, MainActivity3.class),101);
        startActivity(new Intent(Dashboard_Google.this, MainActivity3.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " );
    }
}