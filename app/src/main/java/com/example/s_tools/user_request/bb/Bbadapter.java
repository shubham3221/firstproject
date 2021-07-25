package com.example.s_tools.user_request.bb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;

import java.util.List;

public class Bbadapter extends RecyclerView.Adapter<Bbadapter.Myviewholder> {
    Context context;
    List<String> list;

    public Bbadapter(Context context, List<String> items) {
        this.context=context;
        this.list=items;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.bb_layout, parent, false);
        return new Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        holder.title.setText("Play Quality "+(position+1));
    }

    @Override
    public int getItemCount() {
        if (list==null){
            return 0;
        }
        return list.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        private TextView title;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.bbtitle);
        }
    }
    String getVideoThumbnailUrl(String id) {
        return "https://lh3.googleusercontent.com/d/"+id+"=w1920-h1080";
    }


}
