package com.example.s_tools.entertainment.uper_slider;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.s_tools.R;
import com.example.s_tools.tools.GetDate;
import com.example.s_tools.user_request.UserRequestActivity;
import com.example.s_tools.user_request.bb.OnlineModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class SliderPage_Adapter extends PagerAdapter {

    private final Context context;
    private final List<OnlineModel> list;

    public SliderPage_Adapter(Context context, List<OnlineModel> list) {
        this.context=context;
        this.list=list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.slide_items,container,false);

        ImageView imageView = v.findViewById(R.id.slider_image);
        TextView textView = v.findViewById(R.id.slide_title);
        TextView dateadded = v.findViewById(R.id.animationn2);
        ConstraintLayout view = v.findViewById(R.id.clayout);
        if (!list.isEmpty()){
            Picasso.get().load(list.get(position).getImageurl()).resize(300,300).centerInside()
                    .into(imageView);

            textView.setText(list.get(position).getTitle());
            ImageView img=new ImageView(context);
            dateadded.setText("Added: "+GetDate.covertTimeToTextForWebsite(list.get(position).getDate()));
            Picasso.get().load(list.get(position).getImageurl()).transform(new BlurTransformation(context, 1, 18)).resize(70, 70).into(img, new Callback() {
                @Override
                public void onSuccess()
                {
                    view.setBackground(img.getDrawable());
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        imageView.setOnClickListener(v1 -> {
            Intent intent = new Intent(context, UserRequestActivity.class);
            intent.putExtra("directdownloadlink",list.get(position).getUrl());
            intent.putExtra("title",list.get(position).getTitle());
            context.startActivity(intent);
        });
        container.addView(v);
        return v;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
