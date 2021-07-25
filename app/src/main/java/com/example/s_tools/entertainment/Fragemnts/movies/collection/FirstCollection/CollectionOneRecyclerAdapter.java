package com.example.s_tools.entertainment.Fragemnts.movies.collection.FirstCollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.MovieDetail.MovieItemClickListner;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.MoviesModel;
import com.example.s_tools.tools.PlaceHolderDrawableHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CollectionOneRecyclerAdapter extends RecyclerView.Adapter<CollectionOneRecyclerAdapter.Viewholder> {
    public static final String TAG = "mtag";
    MovieItemClickListner listner;
    public List<MoviesModel> col_one_list;
    Context context;
    private static final int MAX_WIDTH = 768;
    private static final int MAX_HEIGHT = 400;

    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));


    public CollectionOneRecyclerAdapter(Context context , List<MoviesModel> col_one_list, MovieItemClickListner listner ) {
        this.context = context;
        this.col_one_list = col_one_list;
        this.listner = listner;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.search_layout_images,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Picasso.get().load(col_one_list.get(position).getMainimg())
                .resize(250,320).placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
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
                Picasso.get().load(R.drawable.noimage).resize(150,200)
                        .into(holder.imageView);
            }
        });

       // holder.title_txt.setText(col_one_list.get(position).getTitle().getRendered());
       // holder.date_txt.setText("modified: "+GetDate.getdate(col_one_list.get(position).getModified()));


    }


    @Override
    public int getItemCount() {
        return col_one_list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title_txt,date_txt;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView  = itemView.findViewById(R.id.search_imgview);
           // title_txt = itemView.findViewById(R.id.txt_c_1);
            //date_txt = itemView.findViewById(R.id.date_coll_1);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listner.onmovieClick(col_one_list.get(getAdapterPosition()),imageView);
                    }catch (Exception e){
                        Toast.makeText(context, "Please Wait..", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }
}
