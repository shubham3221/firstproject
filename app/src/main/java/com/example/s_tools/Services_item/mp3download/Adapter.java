package com.example.s_tools.Services_item.mp3download;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.testing.DownloadActivity;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.example.s_tools.tools.ToastMy;

import java.util.List;

import static com.example.s_tools.testing.DownloadActivity.INTENTDOWNLINK;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewholder> {
    Context context;
    List<Model> list;
    String title;

    public Adapter(Context context, List<Model> list,String title) {
        this.context=context;
        this.list=list;
        this.title = title;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mp3_custom,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        holder.size.setText(list.get(position).size);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewholder extends RecyclerView.ViewHolder {
        LinearLayout download;
        Button size;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            size = itemView.findViewById(R.id.qualitytext);
            download = itemView.findViewById(R.id.downloamm);
            size.setOnClickListener(view -> {
                if (PhotoFullPopupWindow.isStoragePermissionGranted(context)){
                    Intent intent=new Intent(context, DownloadActivity.class);
                    intent.putExtra(INTENTDOWNLINK, list.get(getAdapterPosition()).getUrl());
                    intent.putExtra(DownloadActivity.FILENAME, title);
                    context.startActivity(intent);
                }else {
                    ToastMy.errorToast(context, Cvalues.STORAGE_PERMISSION_NOT_GRANTED, ToastMy.LENGTH_LONG);
                }

            });
        }
    }
}
