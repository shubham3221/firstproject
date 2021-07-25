package com.example.s_tools.premium;//package com.example.s_tools.premium;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.text.Html;
//import android.text.util.Linkify;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.s_tools.R;
//import com.example.s_tools.tools.GetDate;
//import com.example.s_tools.tools.Placementsid;
//import com.example.s_tools.premium.model.Post;
//import com.example.s_tools.tools.ContentSplit;
//import com.example.s_tools.tools.Cvalues;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import static com.example.s_tools.tools.Cvalues.CONTENT_LOCKED;
//import static com.example.s_tools.tools.Cvalues.DATE;
//import static com.example.s_tools.tools.Cvalues.SUCCESS;
//import static com.example.s_tools.tools.Cvalues.UNBLOCK_IT;
//import static com.example.s_tools.tools.Cvalues.UNLOCKED;
//import static com.example.s_tools.tools.Cvalues.VIDEO_AD_NOT_RUNNING;
//
//class CustomAdapterDashboard extends RecyclerView.Adapter<CustomAdapterDashboard.MyViewHolder> implements Filterable, IUnityAdsListener {
//    List<Post> list;
//    List<Post> listfull;
//    Context context;
//    Dialog dialog;
//    ImageView imageView;
//    Button unlock_btn;
//    ImageView closepopup, pop_img;
//    TextView popUpmsg, titlePopup, herelink;
//    CardView cardView;
//    int pos;
//    boolean isSuccess=false;
//    boolean firsttime=true;
//
//
//    public CustomAdapterDashboard(List<Post> premiumModels, Context context) {
//        this.list=premiumModels;
//        this.context=context;
//        this.listfull=new ArrayList<>();
//        listfull.addAll(list);
//
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_simple_recycle, parent, false);
//        return new MyViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
//
//        holder.name.setText(Html.fromHtml(ContentSplit.makeFirstLineBold((list.get(position).getExcerpt()).replaceAll("]", "\n"))));
//        if (!list.get(position).getAttachments().isEmpty()) {
//            Picasso.get().load(list.get(position).getAttachments().get(0).getUrl()).into(holder.imageView);
//        }
//
//        holder.getDate.setText(Cvalues.ID + list.get(position).getId() + DATE + GetDate.covertTimeToTextForAnother(list.get(position).getModified()));
//
//        // implement setOnClickListener event on item view.
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popUpmsg.setText("");
//
//                if (list.get(position).getCustom_fields().getLink() != null) {
//                    lockedContent();
//                    isSuccess=true;
//                    dialog.show();
//                }
//
//                //Popup close
//                closepopup.setOnClickListener(v -> {
//                    dialog.dismiss();
//                });
//
//                //Popup unblock button
//                unlock_btn.setOnClickListener(v -> {
//                    if (isSuccess) {
//                        pos=position;
//                        if (UnityAds.isInitialized()) {
//                            UnityAds.show((Activity) context, Placementsid.REWARDED_VIDEO);
//                        } else {
//                            Toast.makeText(context, "Use VPN", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                });
//            }
//
//
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        popwindow_init();
//        return list.isEmpty()? 0:list.size();
//    }
//
//    //unity video and inter ads methods started
//    @Override
//    public void onUnityAdsReady(String s) {
//
//    }
//
//    @Override
//    public void onUnityAdsStart(String s) {
//
//    }
//
//    @Override
//    public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
//        if (finishState == UnityAds.FinishState.COMPLETED) {
//            popUpmsg.setVisibility(View.VISIBLE);
//            herelink.setVisibility(View.VISIBLE);
//            isSuccess=false;
//            firsttime=false;
//            herelink.setText("Here is Your Direct Link!!! Click on the link to open in browser or you can copy that link");
//            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.popupWindow_bg_unlocked_color));
//            pop_img.setImageResource(R.drawable.unlocked_open_img);
//            unlock_btn.setText(SUCCESS);
//            titlePopup.setText(UNLOCKED);
//            popUpmsg.setText(Html.fromHtml(list.get(pos).getCustom_fields().getLink().get(0)));
//            UnityAds.load(Placementsid.INTER_VIDEO);
//        } else if (UnityAds.FinishState.ERROR == finishState) {
//            Toast.makeText(context, VIDEO_AD_NOT_RUNNING, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView name, getDate;
//        ImageView imageView;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            name=itemView.findViewById(R.id.premium_textview);
//            imageView=itemView.findViewById(R.id.img);
//            getDate=itemView.findViewById(R.id.getDate);
//            name.setLinkTextColor(context.getResources().getColor(R.color.progressbarcolor_orange));
//            name.setLinksClickable(false);
//            name.setAutoLinkMask(Linkify.ALL);
//        }
//
//    }
//
//    void popwindow_init() {
//        dialog=new Dialog(context);
//        dialog.setContentView(R.layout.popupwindow);
//        unlock_btn=(Button) dialog.findViewById(R.id.btnPopup);
//        herelink=dialog.findViewById(R.id.herelink);
//        closepopup=(ImageView) dialog.findViewById(R.id.closePopup);
//        pop_img=(ImageView) dialog.findViewById(R.id.pop_Img);
//        popUpmsg=(TextView) dialog.findViewById(R.id.msgPopup);
//        titlePopup=(TextView) dialog.findViewById(R.id.titlePopup);
//        cardView=(CardView) dialog.findViewById(R.id.popBG_color);
//    }
//
//    void lockedContent() {
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        titlePopup.setText(CONTENT_LOCKED);
//        cardView.setCardBackgroundColor(context.getResources().getColor(R.color.popupWindow_bg_locked_color));
//        unlock_btn.setText(UNBLOCK_IT);
//    }
//
//
//    //MARK:- search posts
//    @Override
//    public Filter getFilter() {
//        return premiumfilter;
//    }
//
//    private Filter premiumfilter=new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<Post> filteredList=new ArrayList<>();
//            if (constraint == null || constraint.length() == 0) {
//                filteredList.addAll(listfull);
//            } else {
//                String filter=constraint.toString().toLowerCase().trim();
//                if (listfull.isEmpty()) {
//                    listfull.addAll(list);
//                }
//
//                for (Post model : listfull) {
//
//                    if (model.getTitle_plain().toLowerCase().contains(filter)) {
//                        filteredList.add(model);
//                    }
//                }
//            }
//            FilterResults results=new FilterResults();
//            results.values=filteredList;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            list.clear();
//            list.addAll((Collection<? extends Post>) results.values);
//            notifyDataSetChanged();
//        }
//    };
//
//    @Override
//    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
//        UnityAds.removeListener(this);
//        super.onDetachedFromRecyclerView(recyclerView);
//    }
//
//    @Override
//    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
//        UnityAds.addListener(this);
//        UnityAds.load(Placementsid.REWARDED_VIDEO);
//        super.onAttachedToRecyclerView(recyclerView);
//
//
//    }
//}
