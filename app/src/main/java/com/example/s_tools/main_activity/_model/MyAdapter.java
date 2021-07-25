package com.example.s_tools.main_activity._model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Viewholder> {
    List<Model> modelList;

    public MyAdapter(List<Model> modelList) {
        this.modelList=modelList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom, parent, false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Picasso.get().load(modelList.get(position).getImage()).into(holder.img);
        holder.txt1.setText(modelList.get(position).getFirst());
        holder.txt2.setText(modelList.get(position).getSecond());
//        if (position==0){
//            holder.badge_txt_others.setVisibility(View.Go);
//        }else {
//            holder.badge_txt_others.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txt1, txt2;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imageView);
            txt1=itemView.findViewById(R.id.textView);
            txt2=itemView.findViewById(R.id.textView2);
        }
    }
}