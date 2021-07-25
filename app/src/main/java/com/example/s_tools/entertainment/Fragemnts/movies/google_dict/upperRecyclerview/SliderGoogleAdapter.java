package com.example.s_tools.entertainment.Fragemnts.movies.google_dict.upperRecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SliderGoogleAdapter extends RecyclerView.Adapter<SliderGoogleAdapter.MyviewHolder> {
    Context context;
    public List<String> items;

    public SliderGoogleAdapter(Context context, List<String> items) {
        this.context=context;
        this.items=items;
    }

    @NonNull
    @NotNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.upper_nav_recyclerview,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SliderGoogleAdapter.MyviewHolder holder, int position) {
        if (items.isEmpty()){
            holder.textView.setText("");
        }else if (items.size()-1==position){
            holder.textView.setTextColor(context.getResources().getColor(R.color.white));
            holder.textView.setText(items.get(position));
        }else {
            holder.textView.setTextColor(context.getResources().getColor(R.color.white_tintcolor));
            holder.textView.setText(items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyviewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.item);
        }
    }
}
