package com.example.s_tools.Services_item.VidDownloader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.download.library.ResourceRequest;
import com.example.s_tools.NotificationHelper;
import com.example.s_tools.R;
import com.example.s_tools.testing.DownloadActivity;
import com.example.s_tools.tools.Kbtomb;
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.example.s_tools.tools.PlaceHolderDrawableHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;

import static com.example.s_tools.testing.DownloadActivity.FILENAME;
import static com.example.s_tools.testing.DownloadActivity.INTENTDOWNLINK;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    Context context;
    List<VideoMeta> list;
    List<String> downloadlinks;
    NotificationHelper notificationHelper;
    private String mTitle;


    public RecyclerAdapter(Context context, List<VideoMeta> videoMeta, List<String> downloadlinks) {
        this.context=context;
        this.list=videoMeta;
        this.downloadlinks=downloadlinks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.vdrecyclercustom, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(list.get(0).getHqImageUrl()).placeholder(R.drawable.ic_baseline_image_24).into(holder.imageView);
        holder.length.setText(list.get(0).getVideoLength() / 60 + " min");
        holder.auther.setText(String.valueOf(list.get(0).getAuthor()));
        holder.title.setText(list.get(0).getTitle());

//        holder.download1.setText(downloadlinks.get(0).getFormat().getExt());
//        holder.download1.setText(downloadlinks.get(1).getFormat().getExt());
//        holder.download1.setText(downloadlinks.get(2).getFormat().getExt());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public static final String PLEASE_WAIT_DOWNLOADING="Please Wait, Downloading...";
        ImageView imageView;
        TextView title, auther, length;
        Button download1, download2;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.thumbvid);
            title=itemView.findViewById(R.id.titlevid);
            auther=itemView.findViewById(R.id.authervid);
            length=itemView.findViewById(R.id.lengthvid);
            download1=itemView.findViewById(R.id.downloadbtn1);
            download2=itemView.findViewById(R.id.downloadbtn2);
            download1.setOnClickListener(v -> {
                if (PhotoFullPopupWindow.isStoragePermissionGranted(context)) {
                    Intent intent=new Intent(context, DownloadActivity.class);
                    intent.putExtra(INTENTDOWNLINK, downloadlinks.get(0));
                    intent.putExtra(DownloadActivity.FILENAME,list.get(0).getTitle()+".mp4");
                    context.startActivity(intent);
                }
            });
            download2.setOnClickListener(v -> {
                if (PhotoFullPopupWindow.isStoragePermissionGranted(context)) {
                    Intent intent=new Intent(context, DownloadActivity.class);
                    intent.putExtra(INTENTDOWNLINK, downloadlinks.get(1));
                    intent.putExtra(FILENAME,list.get(0).getTitle()+".mp4");
                    context.startActivity(intent);
                }
            });
        }
    }

    void downloadmanager(String url) {
//        notificationHelper.addNotification();
        ResourceRequest resourceRequest=DownloadImpl.getInstance().with(context).target(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).setUniquePath(false).setForceDownload(true).url(url);
        resourceRequest.enqueue(new DownloadListenerAdapter() {
            @Override
            public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, Extra extra) {
                super.onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra);
                String characterFilter="[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
                mTitle=list.get(0).getTitle().replaceAll(characterFilter, "");
//
            }

            @Override
            public void onProgress(String url, long downloaded, long length, long usedTime) {
                super.onProgress(url, downloaded, length, usedTime);
//                            Log.e(TAG, "onProgress: "+Kbtomb.getFileSize(downloaded) );
                notificationHelper.startDownload(String.valueOf((int) (downloaded * 100.0F / length)) + "% , " + Kbtomb.getFileSize(downloaded) + " / " + Kbtomb.getFileSize(length), (int) (downloaded * 100.0F / length), false, 235);
            }

            @Override
            public boolean onResult(Throwable throwable, Uri path, String url, Extra extra) {
                if (throwable == null) {
                    File to=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mTitle);
                    notificationHelper.notifResult(true, "file saved in: Download folder", 235);
                    resourceRequest.getDownloadTask().getFile().renameTo(to);
                } else {
                    notificationHelper.notifResult(false, "", 235);
                }
                return super.onResult(throwable, path, url, extra);
            }
        });
    }

}
