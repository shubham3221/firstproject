package com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.Tab_1;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.squareup.picasso.Picasso;

import java.util.List;

class Tab1_Adapter extends RecyclerView.Adapter<Tab1_Adapter.MyViewHolder> {
    public static final String TAG="mtag";
    List<String> list;
    Context context;
    private ConstraintSet set = new ConstraintSet();

    public Tab1_Adapter(List<String> list, Context context) {
        this.list=list;
        this.context=context;
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.movietab1_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (!list.get(position).isEmpty()){
            Picasso.get().load(list.get(position).split("\\?")[0])
                    .placeholder(R.drawable.ic_baseline_image_24).resize(200,300).noFade().centerInside()
                    .into(holder.imageView);
        }else {
            Picasso.get().load(R.drawable.noimage).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        @RequiresApi(api=Build.VERSION_CODES.M)
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movietab1_imgview);
            itemView.setOnClickListener(v -> {
                PhotoFullPopupWindow popupWindow = new PhotoFullPopupWindow(context, R.layout.popup_photo_full,
                        imageView, list.get(getAdapterPosition()).split("\\?")[0], null);
                popupWindow.download.setVisibility(View.VISIBLE);
            });
        }
    }
}
