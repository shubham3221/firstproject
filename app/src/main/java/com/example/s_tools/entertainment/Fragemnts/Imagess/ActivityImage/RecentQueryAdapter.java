package com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.Tab_1.RecentClick;

import java.util.List;

class RecentQueryAdapter extends RecyclerView.Adapter<RecentQueryAdapter.MyViewHolder>{
    Context context;
    public List<String> list;
    RecentClick recentItemClicked;

    public RecentQueryAdapter(Context context, List<String> list , RecentClick listner) {
        this.context=context;
        this.list=list;
        this.recentItemClicked = listner;
    }
    public RecentQueryAdapter(Context context, List<String> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.recent_query_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.query_text);
            if (list!=null){
                itemView.setOnClickListener(v -> {
                    recentItemClicked.recentItemClicked(list.get(getAdapterPosition()));
                });
            }
        }
    }
}
