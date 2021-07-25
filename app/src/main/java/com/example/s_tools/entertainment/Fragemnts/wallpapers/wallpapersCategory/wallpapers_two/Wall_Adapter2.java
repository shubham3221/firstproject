package com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_two;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Wall_Adapter2 extends RecyclerView.Adapter<Wall_Adapter2.MyViewholder> {
    public static final String STRING="https://www.367labs.a2hosted.com/casual/";
    Context context;
    List<Wallpapers_2_Model> list;

    public Wall_Adapter2(Context context, List<Wallpapers_2_Model> list) {
        this.context=context;
        this.list=list;
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.custom_wall_one_two,parent,false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        Picasso.get().load(STRING+list.get(position).getThumbUrl()).placeholder(R.drawable.ic_baseline_image_24)
                .resize(500,500).centerCrop().noFade()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        @RequiresApi(api=Build.VERSION_CODES.M)
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.wallmain_images_recycler);
            itemView.setOnClickListener(v -> {
                String replace=STRING +list.get(getAbsoluteAdapterPosition()).getImgUrl();
                PhotoFullPopupWindow popupWindow = new PhotoFullPopupWindow(context, R.layout.popup_photo_full, imageView, replace, null);
                popupWindow.download.setVisibility(View.VISIBLE);
            });
        }
    }



    //waste

    class MyTask extends AsyncTask<String,Void,Bitmap>{
        private final WeakReference<ImageView> imageViewReference;

        public MyTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            RequestCreator requestCreator=Picasso.get().load(strings[0]).resize(150, 250);
            try {
                return requestCreator.get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = imageViewReference.get();

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.ic_search_black_24dp);
                imageView.setImageDrawable(placeholder);
            }
        }
    }

    class LoadImage extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;

        public LoadImage(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {

                URL url = new URL(params[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;

            } catch (Exception e) {

                e.printStackTrace();

            }

            return null;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.ic_search_black_24dp);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != 200) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
}}