package com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallpaperOneModel;

import java.util.ArrayList;
import java.util.List;


class Wall_Adapter1 extends RecyclerView.Adapter<Wall_Adapter1.MyViewholderWall1> {
    public static final String TAG="ok";
    Context context;
    List<WallpaperOneModel> list;
    List<String> images=new ArrayList<>();

    public Wall_Adapter1(Context context, List<WallpaperOneModel> list) {
        this.context=context;
        this.list=list;
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @NonNull
    @Override
    public MyViewholderWall1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.search_layout_images, parent, false);
        return new MyViewholderWall1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholderWall1 holder, int position) {
//        Picasso.get().load(list.get(position).getUrl()).placeholder(R.drawable.ic_baseline_image_24)
//                .resize(300,400).centerCrop().noFade().into(holder.imageView);
        Glide.with(context).load(list.get(position).getUrl()).apply(new RequestOptions().override(300, 400)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewholderWall1 extends RecyclerView.ViewHolder {
        ImageView imageView;

        @RequiresApi(api=Build.VERSION_CODES.M)
        public MyViewholderWall1(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.search_imgview);
            itemView.setOnClickListener(v -> {
                String replace=list.get(getAbsoluteAdapterPosition()).getUrl().replace("ico_v", "pic_v");
//                PhotoFullPopupWindow popupWindow = new PhotoFullPopupWindow(context, R.layout.popup_photo_full, imageView, replace, null);
//                popupWindow.download.setVisibility(View.VISIBLE);
                Log.e(TAG, "MyViewholder: ");
                if (images.isEmpty()) {
                    String replace2=list.get(getAbsoluteAdapterPosition()).getUrl().replace("ico_v", "pic_v");
                    images.add("replace2");
                }

//
//                new StfalconImageViewer.Builder<>(context, images, new ImageLoader<String>() {
//                    @Override
//                    public void loadImage(ImageView imageView, String imageUrl) {
//                        Glide.with(context).load(replace).into(imageView);
//                    }
//                }).withStartPosition(getAbsoluteAdapterPosition())
//                        .withTransitionFrom(imageView)
//                        .withBackgroundColor(context.getResources().getColor(R.color.black))
//                        .withOverlayView(new Myview(context).addView())
//                        .withHiddenStatusBar(false)
//
//                        .show(true);

            });
        }
    }
//    class Myview  {
//        Context context;
//        View view;
//        ImageView imageView;
//
//        public Myview(@NotNull Context context) {
//            super(context);
//        }
////        Myview(Context context){
////            this.context=context;
////            view=LayoutInflater.from(context).inflate(R.layout.view_poster_overlay,null,false);
////            imageView=view.findViewById(R.id.posterOverlayDeleteButton);
////            imageView.setOnClickListener(v -> Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show());
////        }
//
//        View addView(){
//                        imageView=view.findViewById(R.id.posterOverlayDeleteButton);
//            view=LayoutInflater.from(context).inflate(R.layout.view_poster_overlay,this,false);
//            return view;
//
//        }
//    }

}
