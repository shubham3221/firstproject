package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.Detail_Act;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.testing.DownloadActivity;
import com.example.s_tools.user_request.UserRequestActivity;

import java.util.List;

class BottomsheetAdapter extends RecyclerView.Adapter<BottomsheetAdapter.Myviewholder> {
    Context context;
    List<String> list;
    String title;

    public BottomsheetAdapter(Context context, List<String> list, String title) {
        this.context=context;
        this.list=list;
        this.title=title;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.bottom_layout, parent, false);
        return new Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        holder.textView.setText(title +" "+(position+1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        TextView textView,watch,download;
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.mpart);
            watch = itemView.findViewById(R.id.watch);
            download = itemView.findViewById(R.id.download);
            watch.setOnClickListener(v -> {
                Intent intent = new Intent(context, UserRequestActivity.class);
                intent.putExtra("direct",list.get(getAdapterPosition()).trim());
                context.startActivity(intent);
            });
            download.setOnClickListener(v -> {
                Intent intent = new Intent(context, DownloadActivity.class);
                intent.putExtra(DownloadActivity.INTENTDOWNLINK,list.get(getAdapterPosition()).trim());
                context.startActivity(intent);
            });
        }
    }
}
