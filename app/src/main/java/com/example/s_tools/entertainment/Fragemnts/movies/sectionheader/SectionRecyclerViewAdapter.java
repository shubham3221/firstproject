package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader;

import android.app.Activity;
import android.content.Context;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.TinyDB;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MySection;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.GetDate;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.List;


public class SectionRecyclerViewAdapter extends RecyclerView.Adapter<SectionRecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<MySection> sectionList;
    ItemRecyclerViewAdapter adapter;
    private InterstitialAd mInterstitialAd;
    private boolean updated=false;
    private boolean adLoaded=false;
    public static int lastId=0;
    TinyDB tinyDB;


    public SectionRecyclerViewAdapter(InterstitialAd interstitialAd, List<MySection> modelList, Context context) {
        this.sectionList=modelList;
        this.context=context;
        mInterstitialAd=interstitialAd;
        tinyDB=new TinyDB(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.section_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.sectionLabel.setText(sectionList.get(position).getSectionName());
        if (position == 0) {
            holder.updateBtn.setVisibility(View.VISIBLE);
            holder.lastupdate.setVisibility(View.VISIBLE);
            if (sectionList.get(0).getMoviesPosts().get(0).getDate() != null) {
                if (updated) {
                    holder.lastupdate.setTextColor(context.getResources().getColor(R.color.success_color2));
                    holder.lastupdate.setText("Updated: " + GetDate.covertTimeToTextForWebsite(sectionList.get(0).getMoviesPosts().get(0).getDate()));
                } else {
                    holder.lastupdate.setText("Last Upload: "+GetDate.covertTimeToTextForWebsite(sectionList.get(0).getMoviesPosts().get(0).getDate()));
                }
            } else {
                holder.lastupdate.setText("...");
            }
            holder.itemRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            adapter=new ItemRecyclerViewAdapter(context, sectionList.get(position).getMoviesPosts(), 36);
        } else {
            holder.itemRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.updateBtn.setVisibility(View.GONE);
            holder.lastupdate.setVisibility(View.GONE);
            adapter=new ItemRecyclerViewAdapter(context, sectionList.get(position).getMoviesPosts(), 0);
        }
        holder.itemRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView sectionLabel;
        private TextView showAllButton, lastupdate, updateBtn;
        private final RecyclerView itemRecyclerView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionLabel=itemView.findViewById(R.id.section_label);
            showAllButton=itemView.findViewById(R.id.section_show_all_button);
            lastupdate=itemView.findViewById(R.id.lastupdate);
            updateBtn=itemView.findViewById(R.id.updatenow);
            itemRecyclerView=itemView.findViewById(R.id.item_recycler_view);

            itemRecyclerView.setNestedScrollingEnabled(false);
            showAllButton.setOnClickListener(view -> {
                AllFragment someFragment=new AllFragment(sectionList.get(getAdapterPosition()).getId());
                someFragment.setEnterTransition(new Fade());
                someFragment.setExitTransition(new Fade());
                FragmentTransaction fragmentTransaction=((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mycontainer, someFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                    AllFragment someFragment=new AllFragment(mInterstitialAd, sectionList.get(getAdapterPosition()).getId(), true);
//                    someFragment.setEnterTransition(new Fade());
//                    someFragment.setExitTransition(new Fade());
//                    FragmentTransaction fragmentTransaction=((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.mycontainer, someFragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                } else {
//                    AllFragment someFragment=new AllFragment(mInterstitialAd, sectionList.get(getAdapterPosition()).getId(), false);
//                    someFragment.setEnterTransition(new Fade());
//                    someFragment.setExitTransition(new Fade());
//                    FragmentTransaction fragmentTransaction=((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.mycontainer, someFragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
//                }
            });

            updateBtn.setOnClickListener(view -> {
                update();
            });
        }

        void update() {
            updateBtn.setVisibility(View.GONE);
            Toast.makeText(context, Cvalues.UPDATING, Toast.LENGTH_SHORT).show();
            lastupdate.setTextColor(context.getResources().getColor(R.color.white_hint));
            lastupdate.setText("Last Update: Updating...");


            InterstitialAd.load(context,Cvalues.vid, new AdRequest.Builder().build()
                    , new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;
                    showAdd();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.e("//", "onAdFailedToLoad: "+loadAdError.getMessage() );
                    mInterstitialAd = null;
                    lastupdate.setTextColor(context.getResources().getColor(R.color.error_text));
                    lastupdate.setText("Ad not showing. Try Again!");
                    updateBtn.setVisibility(View.VISIBLE);
                }
            });
        }

        private void showAdd() {
            if (mInterstitialAd != null) {
                mInterstitialAd.show((Activity) context);
                MyMovies.getMovies(context, 36, 10, 1, moviesPosts -> {
                    if (moviesPosts != null) {
                        ToastMy.successToast(context, "Updated",ToastMy.LENGTH_SHORT);
                        lastId=sectionList.get(0).getMoviesPosts().get(0).getId();
                        notifyItemChanged(0);
                        updated=true;
                        sectionList.get(0).setMoviesPosts(moviesPosts);
                        MySharedPref.putMoviesList(context, moviesPosts, MySharedPref.RECENT);
                    } else {
                        lastupdate.setTextColor(context.getResources().getColor(R.color.error_text));
                        lastupdate.setText(Cvalues.SERVER_OFFLINE);
                    }
                    updateBtn.setVisibility(View.VISIBLE);

                });
            } else {
                lastupdate.setTextColor(context.getResources().getColor(R.color.error_text));
                lastupdate.setText("Ad not showing. Try Again!");
                updateBtn.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

    }
}
