package com.example.s_tools.tools;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.s_tools.R;
import com.example.s_tools.testing.DownloadActivity;

import org.jetbrains.annotations.NotNull;

public class Overlayview extends ConstraintLayout implements ImageViewerListner {
    Context context;
    View view;
    ImageView download;
    TextView textView;
//    ProgressBar progressBar;

    public Overlayview(@NotNull Context context) {
        super(context);
        this.context=context;
        view = View.inflate(context, R.layout.view_poster_overlay, this);
        download=view.findViewById(R.id.download);
        textView=view.findViewById(R.id.text);
//        progressBar=view.findViewById(R.id.progress);
        download.setOnClickListener(v -> {
            if (Imageanimation.DOWNLOADURL==null){
                Toast.makeText(context, "You can download after image load", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent=new Intent(context,DownloadActivity.class);
            intent.putExtra(DownloadActivity.INTENTDOWNLINK,Imageanimation.DOWNLOADURL);
            context.startActivity(intent);
        });
    }

    @Override
    public void changeText(String text) {
        textView.setText(text);
    }

    @Override
    public void dismissProgressbar() {
//        progressBar.setVisibility(GONE);
    }
}
