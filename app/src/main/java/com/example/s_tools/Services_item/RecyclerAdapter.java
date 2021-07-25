package com.example.s_tools.Services_item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    Context context;
    List<String> list;

    public RecyclerAdapter(Context context, List<String> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.sf_recycleradapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
        if (position==0){
            holder.cardView.getBackground().setTint(context.getResources().getColor(R.color.highlighted_text_material_light));
            Picasso.get().load(R.drawable.heart).into(holder.imageView);
        }else if (position==1){
            holder.cardView.getBackground().setTint(context.getResources().getColor(R.color.cloud_not_now_background));
            Picasso.get().load(R.drawable.thirdparty).into(holder.imageView);
        }else if (position==2){
            holder.cardView.getBackground().setTint(context.getResources().getColor(R.color.backgorundFragment));
            Picasso.get().load(R.drawable.leaked).into(holder.imageView);
        }else if (position==3){
            holder.cardView.getBackground().setTint(context.getResources().getColor(R.color.preference_fallback_accent_color));
            Picasso.get().load(R.drawable.mixed).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.sf_textview);
            imageView = itemView.findViewById(R.id.sfImageview);
            cardView = itemView.findViewById(R.id.cardView2);
        }
    }
}
