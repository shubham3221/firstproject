package com.example.s_tools.premium;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.premium.model.Post;
import com.example.s_tools.tools.ContentSplit;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.GetDate;
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.s_tools.tools.Cvalues.CONTENT_LOCKED;
import static com.example.s_tools.tools.Cvalues.DATE;
import static com.example.s_tools.tools.Cvalues.SUCCESS;
import static com.example.s_tools.tools.Cvalues.UNBLOCK_IT;
import static com.example.s_tools.tools.Cvalues.UNLOCKED;

class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {
    public static final String TAG="//";
    List<Post> list;
    List<Post> listfull;
    Context context;
    Dialog dialog;
    Button unlock_btn;
    ImageView closepopup, pop_img;
    TextView popUpmsg, titlePopup, herelink;
    CardView cardView;
    int pos;
    boolean isSuccess=false;
    private InterstitialAd mInterstitialAd;
    boolean yes=false;
    private int lastid;

    public Adapter(List<Post> premiumModels, Context context, int i) {
        this.list=premiumModels;
        this.context=context;
        this.listfull=new ArrayList<>();
        listfull.addAll(list);
        this.lastid=i;
    }

    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_simple_recycle, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

            holder.name.setText(Html.fromHtml(ContentSplit.makeFirstLineBold((list.get(position).getExcerpt()).replaceAll("]", "\n"))));
            if (list.get(position).getCustom_fields().getMainimage() != null) {
                Picasso.get().load(list.get(position).getCustom_fields().getMainimage().get(0).trim()).into(holder.imageView);
            }
//
            if (list.get(position).getId() != lastid) {
                if (!yes) {
                    holder.getDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_fiber_new_24, 0);
                } else {
                    holder.getDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            } else {
                yes=true;
            }
            holder.getDate.setText(Cvalues.ID + list.get(position).getId() + DATE + GetDate.covertTimeToTextForWebsite(list.get(position).getModified()) + " ");


    }

    @Override
    public int getItemCount() {
        popwindow_init();
        Log.e(TAG, "getItemCount: "+list.size() );
        return list.isEmpty() ? 0 : list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, getDate;
        ImageView imageView;
        boolean listner=true;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.premium_textview);
            imageView=itemView.findViewById(R.id.img);
            getDate=itemView.findViewById(R.id.getDate);
            name.setLinkTextColor(context.getResources().getColor(R.color.progressbarcolor_orange));
            name.setLinksClickable(false);
            name.setAutoLinkMask(Linkify.ALL);
            if (listner) {
                listner=false;
                itemView.setOnClickListener(view -> {
                    popUpmsg.setText("");

                    if (list.get(getAdapterPosition()).getCustom_fields().getLink() != null) {
                        lockedContent();
                        isSuccess=true;
                        dialog.show();
                    }
                    //Popup close
                    closepopup.setOnClickListener(v -> {
                        dialog.dismiss();
                    });

                    //Popup unblock button
                    unlock_btn.setOnClickListener(v -> {
                        if (isSuccess) {
                            unlock_btn.setText("Loading...");
                            pos=getAdapterPosition();
                            showadd();
                        }
                    });
                });
                imageView.setOnClickListener(v -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        new PhotoFullPopupWindow(context, R.layout.popup_photo_full, imageView, list.get(getAdapterPosition()).getCustom_fields().getMainimage().get(0).trim(), null);
                    }
                });
            }
        }
    }


    private void showadd() {

        InterstitialAd.load(context, Cvalues.inter, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd=interstitialAd;
                mInterstitialAd.show((Activity) context);
                popUpmsg.setVisibility(View.VISIBLE);
                herelink.setVisibility(View.VISIBLE);
                closepopup.setVisibility(View.GONE);
                isSuccess=false;
                herelink.setText("Here is Your Direct Link!!! Click on the link to open in browser or you can copy that link");
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.popupWindow_bg_unlocked_color));
                pop_img.setImageResource(R.drawable.unlocked_open_img);
                unlock_btn.setText(SUCCESS);
                titlePopup.setText(UNLOCKED);
                popUpmsg.setText(Html.fromHtml(list.get(pos).getCustom_fields().getLink().get(0)));
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                unlock_btn.setText("Ad Not Running, Click Again...");
                mInterstitialAd=null;
            }
        });
    }

    void popwindow_init() {
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.popupwindow);
        unlock_btn=(Button) dialog.findViewById(R.id.btnPopup);
        herelink=dialog.findViewById(R.id.herelink);
        closepopup=(ImageView) dialog.findViewById(R.id.closePopup);
        pop_img=(ImageView) dialog.findViewById(R.id.pop_Img);
        popUpmsg=(TextView) dialog.findViewById(R.id.msgPopup);
        titlePopup=(TextView) dialog.findViewById(R.id.titlePopup);
        cardView=(CardView) dialog.findViewById(R.id.popBG_color);
    }

    void lockedContent() {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        titlePopup.setText(CONTENT_LOCKED);
        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.popupWindow_bg_locked_color));
        unlock_btn.setText(UNBLOCK_IT);
    }


    //MARK:- search posts
    @Override
    public Filter getFilter() {
        return premiumfilter;
    }

    private Filter premiumfilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Post> filteredList=new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listfull);
            } else {
                String filter=constraint.toString().toLowerCase().trim();
                if (listfull.isEmpty()) {
                    listfull.addAll(list);
                }

                for (Post model : listfull) {

                    if (model.getExcerpt().contains(filter)) {
                        filteredList.add(model);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends Post>) results.values);
            notifyDataSetChanged();
        }
    };
}

