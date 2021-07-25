package com.example.s_tools.Services_item.VidDownloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;

import java.util.List;

class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.Myviewholder> {
    List<VideoModel> videoModel;
    Context context;

    public RecyclerAdapter2(List<VideoModel> videoModel, Context context) {
        this.videoModel=videoModel;
        this.context=context;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.video_downloader, parent, false);
        return new Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        holder.type.setText(videoModel.get(position).getType());
        holder.size.setText(videoModel.get(position).getSize());
        holder.quality.setText(videoModel.get(position).getQuality());
    }

    @Override
    public int getItemCount() {
        return videoModel.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        TextView type,quality,size,convert;
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            quality = itemView.findViewById(R.id.quality);
            size = itemView.findViewById(R.id.size);
            convert = itemView.findViewById(R.id.convert);
        }
    }
}
