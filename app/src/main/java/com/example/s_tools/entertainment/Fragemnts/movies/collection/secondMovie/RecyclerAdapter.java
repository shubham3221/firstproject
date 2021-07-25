package com.example.s_tools.entertainment.Fragemnts.movies.collection.secondMovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.MoviesModel;
import com.example.s_tools.tools.GetDate;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    public static final String TAG = "mtag";
    public List<MoviesModel> col_one_list;
    Context context;

    public RecyclerAdapter(List<MoviesModel> col_one_list, Context context) {
        this.col_one_list=col_one_list;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.sec_recycler_mov,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(col_one_list.get(position).getMainimg())
                .placeholder(R.drawable.progress_animation)
//                .fit()
//                .resize(250,320).placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
//                .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
//                .centerInside()
//                .transform(new BlurTransformation(context,10,1))
//                .transform(new Croptransformation(500,540, Croptransformation.GravityHorizontal.CENTER, Croptransformation.GravityVertical.BOTTOM))
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(R.drawable.noimage)
                                .into(holder.imageView);
                    }
                });

        holder.title_txt.setText(col_one_list.get(position).getTitle().getRendered());
        holder.date_txt.setText("modified: "+ GetDate.covertTimeToTextForWebsite(col_one_list.get(position).getModified()));

    }

    @Override
    public int getItemCount() {
        return col_one_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title_txt,date_txt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView  = itemView.findViewById(R.id.sec_movie_img);
            title_txt = itemView.findViewById(R.id.sec_title);
            date_txt = itemView.findViewById(R.id.sec_date);
        }
    }
}
