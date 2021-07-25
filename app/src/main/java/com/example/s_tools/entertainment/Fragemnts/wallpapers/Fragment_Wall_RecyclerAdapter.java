package com.example.s_tools.entertainment.Fragemnts.wallpapers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.WallpaperCategorys;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallpaperOneModel;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_two.Wallpapers_2_Model;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;


class Fragment_Wall_RecyclerAdapter extends RecyclerView.Adapter<Fragment_Wall_RecyclerAdapter.MyViewHolder> {
    public static final String STRING="https://www.367labs.a2hosted.com/casual/";
    Context context;
    List<WallpaperOneModel> list;
    List<Wallpapers_2_Model> list2;

    public Fragment_Wall_RecyclerAdapter(Context context, List<WallpaperOneModel> list, List<Wallpapers_2_Model> list2) {
        this.context=context;
        this.list=list;
        this.list2 = list2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
View v =LayoutInflater.from(context).inflate(R.layout.custom_fragment_wall_recycler,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getUrl()).resize(200,350).placeholder(R.drawable.ic_baseline_image_24).centerInside()
                .into(holder.one);
        Picasso.get().load(list.get(position+1).getUrl()).resize(200,350).placeholder(R.drawable.ic_baseline_image_24).centerInside()
                .into(holder.two);
        Picasso.get().load(list.get(position+2).getUrl()).resize(200,350).placeholder(R.drawable.ic_baseline_image_24).centerInside()
                .into(holder.three);
        Picasso.get().load(list.get(position+3).getUrl()).resize(200,350).placeholder(R.drawable.ic_baseline_image_24).centerInside()
                .into(holder.four);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView one,two,three,four , gradientupper,gradientbottom;
        Button gotocol1,getGotocol2;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            one = itemView.findViewById(R.id.wall_bg1);
            two = itemView.findViewById(R.id.wall_bg2);
            three = itemView.findViewById(R.id.wall_bg3);
            four = itemView.findViewById(R.id.wall_bg4);
            gradientupper = itemView.findViewById(R.id.imgupGradient);
            gradientbottom = itemView.findViewById(R.id.imglowGradient);
            cardView = itemView.findViewById(R.id.hiddencardview);
            gotocol1 = itemView.findViewById(R.id.gotocoll1);
            getGotocol2 = itemView.findViewById(R.id.gotocol2);

            itemView.setOnClickListener(v -> {
                cardView.setVisibility(View.VISIBLE);
                gradientbottom.setVisibility(View.VISIBLE);
                gradientupper.setVisibility(View.VISIBLE);

            });

            gotocol1.setOnClickListener(v -> {
                Intent i=new Intent(context, WallpaperCategorys.class);
                Bundle args = new Bundle();
                args.putSerializable("one",(Serializable)list);
                i.putExtra("one",args);
                context.startActivity(i);
            });

            getGotocol2.setOnClickListener(v -> {
                Intent i=new Intent(context, WallpaperCategorys.class);
                Bundle args = new Bundle();
                args.putSerializable("two",(Serializable)list2);
                i.putExtra("two",args);
                context.startActivity(i);


            });
        }
    }
}
