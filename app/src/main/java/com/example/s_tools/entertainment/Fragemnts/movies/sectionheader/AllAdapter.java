package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader;

import android.content.Context;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.Detail_Act.DetailFragment;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MoviesPosts;
import com.example.s_tools.tools.GetDate;
import com.squareup.picasso.Picasso;

import java.util.List;


class AllAdapter extends RecyclerView.Adapter<AllAdapter.MyViewHolder> {
    public List<MoviesPosts> col_one_list;
    Context context;
    boolean fromSearch;

    public AllAdapter(boolean fromSearch, List<MoviesPosts> col_one_list, Context context) {
        this.fromSearch=fromSearch;
        this.col_one_list=col_one_list;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.sec_recycler_mov, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(col_one_list.get(position).getCustom_fields().getMainimage().get(0)).placeholder(R.drawable.ic_baseline_image_24)
                .resize(500,500).noFade().centerInside()
//                .fit()
//                .resize(250,320).placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
//                .transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
//                .centerInside()
//                .transform(new BlurTransformation(context,10,1))
//                .transform(new Croptransformation(500,540, Croptransformation.GravityHorizontal.CENTER, Croptransformation.GravityVertical.BOTTOM))
                .into(holder.imageView);

        if (col_one_list.get(position).getTitle()!=null){
            holder.title_txt.setText(col_one_list.get(position).getTitle());
        }if (col_one_list.get(position).getCustom_fields().getRating()!=null){
            holder.rating.setText(col_one_list.get(position).getCustom_fields().getRating().get(0));
        }else {
            holder.rating.setVisibility(View.GONE);
        }if (col_one_list.get(position).getCustom_fields().getGen()!=null){
            holder.genres.setVisibility(View.VISIBLE);
            holder.genres.setText(col_one_list.get(position).getCustom_fields().getGen().get(0));
        }
        holder.date_txt.setText("Uploaded: " + GetDate.covertTimeToTextForWebsite(col_one_list.get(position).getDate()));

    }

    @Override
    public int getItemCount() {
        return col_one_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title_txt, date_txt,rating,genres;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.sec_movie_img);
            title_txt=itemView.findViewById(R.id.sec_title);
            date_txt=itemView.findViewById(R.id.sec_date);
            rating=itemView.findViewById(R.id.rating);
            genres=itemView.findViewById(R.id.genres);

            itemView.setOnClickListener(view -> {
                if (fromSearch) {
                    DetailFragment someFragment=new DetailFragment(col_one_list.get(getAdapterPosition()), col_one_list.get(getAdapterPosition()).getId());
                    someFragment.setEnterTransition(new Fade());
                    someFragment.setExitTransition(new Fade());
                    FragmentTransaction fragmentTransaction=((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.searchContainer, someFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    DetailFragment someFragment=new DetailFragment(col_one_list.get(getAdapterPosition()), col_one_list.get(getAdapterPosition()).getId());
                    someFragment.setEnterTransition(new Fade());
                    someFragment.setExitTransition(new Fade());
                    FragmentTransaction fragmentTransaction=((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.allfragcontaitner, someFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            });
        }
    }
}
