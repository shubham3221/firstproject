package com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.Tab_3;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.squareup.picasso.Picasso;

import java.util.List;

class Tab3_Adapter extends RecyclerView.Adapter<Tab3_Adapter.MyViewHolder> {
    List<String> fullimg;
    Context context;

    public Tab3_Adapter(List<String> fullimg, Context context) {
        this.context=context;
        this.fullimg = fullimg;
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.tabadapter3_custom_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!fullimg.get(position).isEmpty()){
            Picasso.get().load(fullimg.get(position))
                    .placeholder(R.drawable.ic_baseline_image_24).resize(250,300).noFade().centerInside()
                    .into(holder.imageView);
        }else {
            Picasso.get().load(R.drawable.noimage).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return fullimg.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        @RequiresApi(api=Build.VERSION_CODES.M)
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tab3_img);
            itemView.setOnClickListener(v -> {
                PhotoFullPopupWindow popupWindow = new PhotoFullPopupWindow(context, R.layout.popup_photo_full,
                        imageView, fullimg.get(getAdapterPosition()), null);
                popupWindow.download.setVisibility(View.VISIBLE);
            });
        }
    }
}
