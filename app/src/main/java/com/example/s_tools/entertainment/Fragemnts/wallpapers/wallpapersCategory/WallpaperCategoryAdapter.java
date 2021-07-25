package com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.WallpaperFragment_1;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallpaperOneModel;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_two.WallpaperFragment_2;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_two.Wallpapers_2_Model;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class WallpaperCategoryAdapter extends RecyclerView.Adapter<WallpaperCategoryAdapter.MyViewHolder>{
    public static final String STRING="https://www.367labs.a2hosted.com/casual/";
    public static final String TAG="mtag";
    public static final String CATEGORY="category";
    public static final String TITLE="title";
    public static final String TWO="two";
    Context context;
    Picasso picasso = Picasso.get();
    List<WallpaperOneModel> one;
    public static final String[] onenames= {"Popular","Celebration","Love","Quotes","People","Flowers","Cute","Creative","Fantasy","Gradient","Graphics","Sci-fi","Space"};

    List<Wallpapers_2_Model> two;
    public static final String[] twonames= {"Popular","Abstract","Amoled","Animal","Cartoon & Funny","Dark & Horror"
            ,"Love & Hearts","Flower","Game","Girl","Minimal","Movie","Space","Superhero","Other"};

    public WallpaperCategoryAdapter(Context context, List<WallpaperOneModel> list, List<Wallpapers_2_Model> list2) {
        this.context=context;
        this.one=list;
        this.two=list2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.custom_dialog_recycleradapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (one!=null){
            picasso.load(one.get(position).getUrl()).
                    transform(new BlurTransformation(context,1,7)).resize(250,250).into(holder.imageView);
            holder.textView.setText(onenames[position]);

        }else if (!two.isEmpty()){
            picasso.load(STRING+two.get(position).getThumbUrl()).resize(250,250)
                    .transform(new BlurTransformation(context,1,7)) .into(holder.imageView);
            holder.textView.setText(twonames[position]);
        }
    }

    @Override
    public int getItemCount() {
        if (one==null){
            return twonames.length;
        }
        return onenames.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.category_images_recycler);
            textView = itemView.findViewById(R.id.category_text_recycler);

            itemView.setOnClickListener(v -> {
                if (one!=null){
                    gotoFragmentOne(getAdapterPosition());
                }else {
                    gotoFragmentTwo(getAdapterPosition());
                }


            });
        }
    }

    private void gotoFragmentTwo(int position) {
        WallpaperFragment_2 someFragment=new WallpaperFragment_2();
        Bundle args=new Bundle();
//        args.putSerializable(TWO, (Serializable) two);
        args.putString(CATEGORY,twonames[position]);
        someFragment.setArguments(args);

        FragmentTransaction fragmentTransaction=((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mcontainer, someFragment); // give your fragment container id in first parameter
        fragmentTransaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        fragmentTransaction.commit();
    }

    private void gotoFragmentOne(int position) {
        WallpaperFragment_1 one=new WallpaperFragment_1();
        Bundle args=new Bundle();
        args.putInt(CATEGORY,position);
        args.putString(TITLE,onenames[position]);
        one.setArguments(args);

        FragmentTransaction fragmentTransaction=((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mcontainer, one); // give your fragment container id in first parameter
        fragmentTransaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        fragmentTransaction.commit();
    }
}
