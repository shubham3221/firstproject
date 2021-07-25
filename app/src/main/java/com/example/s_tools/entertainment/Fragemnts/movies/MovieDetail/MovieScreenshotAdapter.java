package com.example.s_tools.entertainment.Fragemnts.movies.MovieDetail;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

class MovieScreenshotAdapter extends RecyclerView.Adapter<MovieScreenshotAdapter.ViewHolder>{
    public static final String TAG = "mtag";
    Context context;
    List<String> list;
    ProgressBar progressBar;
    FullScreenIamgeListner listner;

    public MovieScreenshotAdapter(Context context, List<String> list, ProgressBar progressBar,FullScreenIamgeListner listner) {
        this.context=context;
        this.list=list;
        this.progressBar = progressBar;
        this.listner = listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.movie_images_col1,parent,false);
        progressBar.setVisibility(View.VISIBLE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.isEmpty()){
            progressBar.setVisibility(View.GONE);
            progressBar.invalidate();
            Log.e(TAG, "onBindViewHolder: return" );
            return;
        }
        Picasso.get().load(list.get(position)).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                progressBar.invalidate();
                Log.e(TAG, "onSuccess: " );
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                progressBar.invalidate();
                Log.e(TAG, "onError: " );
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.movieimagescustomlayout);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onScreenshotClick(imageView,list.get(getAdapterPosition()));
                }
            });
        }
    }
}
