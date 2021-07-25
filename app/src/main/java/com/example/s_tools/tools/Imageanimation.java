package com.example.s_tools.tools;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.s_tools.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.example.s_tools.tools.GetDate.TAG;

public class Imageanimation {
    Overlayview overlayview;
    public StfalconImageViewer<String> show;
    boolean firstClick=true;
    public static String DOWNLOADURL;
    Lock object;

    public void showImage(Context context, List<String> downloadUrls, int position, ImageView mimageView) {
        object=new ReentrantLock();
        overlayview=new Overlayview(context);

        show=new StfalconImageViewer.Builder<String>(context, downloadUrls, (imageView, imageUrl) -> {
                object.lock();
                Picasso.get().load(imageUrl).placeholder(mimageView.getDrawable()).noFade().into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (overlayview != null) {
                            overlayview.dismissProgressbar();
                            DOWNLOADURL=downloadUrls.get(show.currentPosition());
                            if (!firstClick) {
                                return;
                            }
                            overlayview.changeText(position+1 + " / " + downloadUrls.size());
                            object.unlock();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        overlayview.changeText("Error");
                        overlayview.dismissProgressbar();
                        object.unlock();
                    }
                });
        }).withDismissListener(() -> {
            firstClick=true;
            overlayview=null;
            show=null;
            DOWNLOADURL=null;
            object=null;
        }).withImageChangeListener(position1 -> {
//            overlayview.progressBar.setVisibility(View.VISIBLE);
            DOWNLOADURL=null;
            firstClick=false;
            overlayview.changeText(position1+1 + " / " + downloadUrls.size());
        }).withStartPosition(position).withTransitionFrom(mimageView).withBackgroundColor(context.getResources().getColor(R.color.black)).withOverlayView(overlayview).withHiddenStatusBar(false).show(true);
    }

    public void updateImages(List<String> urls) {
        if (show != null) {
            show.updateImages(urls);
        }
    }
}
