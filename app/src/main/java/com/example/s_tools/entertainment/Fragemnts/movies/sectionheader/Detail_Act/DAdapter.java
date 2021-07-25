package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.Detail_Act;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

class DAdapter extends RecyclerView.Adapter<DAdapter.ViewHolder> {
    Context context;
    String[] list;
    ProgressBar progressBar;
    boolean success=true;

    public DAdapter(Context context, String[] list, ProgressBar progressBar) {
        this.context=context;
        this.list=list;
        this.progressBar = progressBar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.imagelayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list[position]!=null && !list[position].isEmpty()){
            Picasso.get().load(list[position].trim()).resize(500,500).centerInside().into(holder.imageView, new Callback() {
                @Override
                public void onSuccess() {
                    if (success){
                        progressBar.setVisibility(View.GONE);
                        success=false;
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list!=null){
            return list.length;
        }
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.mimage);
            imageView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api=Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    new PhotoFullPopupWindow(context, R.layout.popup_photo_full, imageView, list[getAdapterPosition()].trim(), null);
                }
            });
        }
    }

}
