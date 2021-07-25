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
import com.example.s_tools.tools.ToastMy;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.List;

class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {
    private final Context context;
    private final List<MoviesPosts> list;
    private final int category;


    public ItemRecyclerViewAdapter( Context context, List<MoviesPosts> posts, int sectionCategory) {
        this.context=context;
        this.list=posts;
        this.category=sectionCategory;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (category == 36) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_layout, parent, false);
            return new ItemViewHolder(view);
        }
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_layout2, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (list.get(position).getId() != 1) {
            if (list.get(position).getCustom_fields().getMainimage() != null) {
                Picasso.get().load(list.get(position).getCustom_fields().getMainimage().get(0).trim()).resize(600, 600).placeholder(R.color.colorPrimaryDarker).centerInside().into(holder.img);
            }
            if (list.get(position).getCustom_fields().getRating() != null) {
                holder.rating.setText(list.get(position).getCustom_fields().getRating().get(0));
            } else {
                holder.rating.setVisibility(View.GONE);
            }
            if (list.get(position).getTitle() != null) {
//                holder.genres.setText(list.get(position).getCustom_fields().getGen().get(0));
                holder.genres.setText(list.get(position).getTitle());
            }
            if (category == 36 && SectionRecyclerViewAdapter.lastId != 0) {
                if (list.get(position).getId() != SectionRecyclerViewAdapter.lastId) {
                    holder.newtag.setVisibility(View.VISIBLE);
                } else {
                    SectionRecyclerViewAdapter.lastId=0;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView img;
        private final TextView rating;
        private final TextView genres;
        private final TextView newtag;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imgview);
            rating=itemView.findViewById(R.id.ratingtxt);
            genres=itemView.findViewById(R.id.genres);
            newtag=itemView.findViewById(R.id.newText);
            img.setOnClickListener(view -> {
                if (list.get(0).getDate() != null) {
                    DetailFragment someFragment=new DetailFragment(list.get(getAdapterPosition()), list.get(getAdapterPosition()).getId());
                    someFragment.setEnterTransition(new Fade());
                    someFragment.setExitTransition(new Fade());
                    FragmentTransaction fragmentTransaction=((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.mycontainer, someFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    ToastMy.errorToast(context, "Fetching Data. Please Wait", ToastMy.LENGTH_SHORT);
                }
            });
        }

    }

}
