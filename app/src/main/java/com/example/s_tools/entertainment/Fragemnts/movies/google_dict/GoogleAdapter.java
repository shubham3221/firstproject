package com.example.s_tools.entertainment.Fragemnts.movies.google_dict;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.s_tools.R;
import com.example.s_tools.tools.GetDate;
import com.example.s_tools.tools.Kbtomb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


class GoogleAdapter extends RecyclerView.Adapter<GoogleAdapter.MyViewholder> implements Filterable {
    Context context;
    List<Googlemodel> list;
    List<Googlemodel> tempList;
    String searchText;
    int type;

    public GoogleAdapter(Context context, List<Googlemodel> adltModel,int type) {
        this.context=context;
        this.list=adltModel;
        this.type=type;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if (type==GoogleFragment.GRID){
            view=LayoutInflater.from(context).inflate(R.layout.grid_layout_google, parent, false);
        }else if (type==GoogleFragment.COMPACT){
            view=LayoutInflater.from(context).inflate(R.layout.compact_layout, parent, false);
        }else {
            view=LayoutInflater.from(context).inflate(R.layout.google_adapter, parent, false);
        }
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        if (!list.isEmpty()) {
            if (list.get(position).getMimeType().contains("folder")) {
                holder.size.setVisibility(View.GONE);
                holder.mimeImage.setVisibility(View.GONE);
                holder.imageView.setImageResource(R.drawable.folder);
            } else {
                if (list.get(position).getMimeType().contains("zip")) {
                    holder.mimeImage.setVisibility(View.GONE);
                    holder.imageView.setImageResource(R.drawable.zip);
                } else if (list.get(position).getFileExtension().equals("mkv") | list.get(position).getMimeType().contains("video")) {
                    holder.mimeImage.setVisibility(View.VISIBLE);
                    holder.mimeImage.setImageResource(R.drawable.videoplay);
                    if (list.get(position).getThumbnailLink() == null) {
                        holder.imageView.setImageResource(R.drawable.video);
                    } else {
                        Glide.with(context.getApplicationContext()).load(list.get(position).getThumbnailLink()).transition(withCrossFade()).apply(new RequestOptions().placeholder(R.drawable.video)).into(holder.imageView);
                    }
                } else if (list.get(position).getMimeType().contains("photo") || list.get(position).getMimeType().contains("png")) {
                    holder.mimeImage.setVisibility(View.VISIBLE);
                    holder.mimeImage.setImageResource(R.drawable.photo);
                    Glide.with(context.getApplicationContext()).load(list.get(position).getThumbnailLink()).apply(new RequestOptions().placeholder(R.drawable.photo2)).into(holder.imageView);
                } else if (list.get(position).getMimeType().contains("text")) {
                    holder.mimeImage.setVisibility(View.GONE);
                    holder.imageView.setImageResource(R.drawable.text);
                } else if (list.get(position).getMimeType().contains("file")) {
                    holder.mimeImage.setVisibility(View.GONE);
                    holder.imageView.setImageResource(R.drawable.file);
                } else if (list.get(position).getMimeType().contains("document")) {
                    holder.mimeImage.setVisibility(View.GONE);
                    holder.imageView.setImageResource(R.drawable.document);
                } else if (list.get(position).getMimeType().contains("audio")) {
                    holder.mimeImage.setVisibility(View.GONE);
                    holder.imageView.setImageResource(R.drawable.mp3);
                } else {
                    holder.mimeImage.setVisibility(View.GONE);
                    holder.imageView.setImageResource(R.drawable.file);
                }
            }


            if (list.get(position).getSize() != null) {
                holder.size.setVisibility(View.VISIBLE);
                holder.size.setText(Kbtomb.getFileSize(Long.parseLong(list.get(position).getSize())));
            }
            holder.meta.setText(GetDate.covertTimeToTextForGoogle(list.get(position).getModifiedTime()));

            //Highlight Text
            if (searchText != null && searchText.length() > 0) {
                int startPos=list.get(position).getName().toLowerCase().indexOf(searchText.toLowerCase());
                Log.e("//", "startpos: " + startPos + "  " + list.get(position).getName());
                int endPos=searchText.length() + startPos;
                Log.e("//", "endpos: " + endPos);
                Spannable spanString=Spannable.Factory.getInstance().newSpannable(list.get(position).getName());
                spanString.setSpan(new ForegroundColorSpan(Color.GREEN), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.name.setText(spanString);
            } else {
                holder.name.setText(list.get(position).getName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewholder extends RecyclerView.ViewHolder {
        ImageView imageView, mimeImage;
        TextView name, meta, size;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgvid);
            name=itemView.findViewById(R.id.nametext);
            meta=itemView.findViewById(R.id.metatext);
            size=itemView.findViewById(R.id.size);
            mimeImage=itemView.findViewById(R.id.mime);
        }
    }

    //MARK:- search posts
    @Override
    public Filter getFilter() {
        return premiumfilter;
    }

    private Filter premiumfilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (tempList == null) {
                tempList=new ArrayList<>();
                tempList.addAll(list);
            }
            List<Googlemodel> filteredList=new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(tempList);
            } else {
                String filter=constraint.toString().toLowerCase().trim();
                if (tempList.isEmpty()) {
                    tempList.addAll(list);
                }
                for (Googlemodel model : tempList) {

                    if (model.getName().toLowerCase().contains(filter)) {
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
            list.addAll((Collection<? extends Googlemodel>) results.values);
            searchText=constraint.toString();
            notifyDataSetChanged();
        }
    };
}
