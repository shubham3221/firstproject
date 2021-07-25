//package com.example.s_tools;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.s_tools.tools.Kbtomb;
//import com.squareup.picasso.Picasso;
//
//import org.wlf.filedownloader.DownloadFileInfo;
//import org.wlf.filedownloader.FileDownloader;
//import org.wlf.filedownloader.base.Status;
//
//import java.util.List;
//
//class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.MyViewHolder> {
//    Context context;
//    List<DownloadFileInfo> list;
//
//    public DownloadAdapter(Context context, List<DownloadFileInfo> list) {
//        this.context=context;
//        this.list=list;
//    }

//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View inflate=LayoutInflater.from(context).inflate(R.layout.download_adapter, parent, false);
//        return new MyViewHolder(inflate);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.status.setText(getstatus(list.get(position).getStatus()));
//        holder.title.setText(list.get(position).getFileName());
//        holder.extra.setText(Kbtomb.formatFileSize(list.get(position).getDownloadedSizeLong()));
//        switch (list.get(position).getStatus()) {
//            case Status.DOWNLOAD_STATUS_PAUSED:
//                if (list.get(position).getDownloadedSizeLong() <= 0) {
//                    holder.progressBar.setIndeterminate(true);
//                } else {
//                    holder.progressBar.setIndeterminate(false);
//                    holder.progressBar.setProgress((int) (list.get(position).getDownloadedSizeLong() * 100.0F / list.get(position).getFileSizeLong()));
//                }
//                holder.status.setTextColor(context.getResources().getColor(R.color.yellow));
//                holder.imageView.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
//                break;
//            case Status.DOWNLOAD_STATUS_PREPARING:
//                if (list.get(position).getDownloadedSizeLong() <= 0) {
//                    holder.progressBar.setIndeterminate(true);
//                } else {
//                    holder.progressBar.setIndeterminate(false);
//                    holder.progressBar.setProgress((int) (list.get(position).getDownloadedSizeLong() * 100.0F / list.get(position).getFileSizeLong()));
//                }
//                holder.status.setTextColor(context.getResources().getColor(R.color.yellow));
//                holder.imageView.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
//                break;
//
//            case Status.DOWNLOAD_STATUS_COMPLETED:
//                holder.status.setTextColor(context.getResources().getColor(R.color.switch_on_master));
//                holder.progressBar.setVisibility(View.GONE);
//                holder.cancel.setVisibility(View.GONE);
//                if (list.get(position).getFileName().endsWith(".mp4")) {
//                    Picasso.get().load(R.drawable.mp4_png).into(holder.imageView);
//                } else if (list.get(position).getFileName().endsWith(".mp3") || list.get(position).getFileName().endsWith("m4a")) {
//                    Picasso.get().load(R.drawable.mp3_png2).into(holder.imageView);
//                } else if (list.get(position).getFileName().endsWith(".jpg") || list.get(position).getFileName().endsWith(".jpeg") || list.get(position).getFileName().endsWith(".png") || list.get(position).getFileName().endsWith(".gif")) {
//                    Picasso.get().load(R.drawable.jpg_png).into(holder.imageView);
//                } else if (list.get(position).getFileName().endsWith(".mp4")) {
//                    Picasso.get().load(R.drawable.mp4_png).into(holder.imageView);
//                } else if (list.get(position).getFileName().endsWith(".mkv")) {
//                    Picasso.get().load(R.drawable.mp4_png).into(holder.imageView);
//                } else {
//                    Picasso.get().load(R.drawable.unknown_png).into(holder.imageView);
//                }
//                break;
//            case Status.DOWNLOAD_STATUS_DOWNLOADING:
//                holder.imageView.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
//                holder.progressBar.setVisibility(View.VISIBLE);
//                holder.status.setTextColor(Color.parseColor("#008EFF"));
//                if (list.get(position).getFileSizeLong() < 0) {
//                    holder.extra.setText(Kbtomb.formatFileSize(list.get(position).getDownloadedSizeLong()));
//                } else {
//                    holder.extra.setText(Kbtomb.formatFileSize(list.get(position).getDownloadedSizeLong()) + " / " + Kbtomb.formatFileSize(list.get(position).getFileSizeLong()));
//                }
//                break;
//            case Status.DOWNLOAD_STATUS_ERROR:
//                holder.progressBar.setVisibility(View.GONE);
//                holder.cancel.setVisibility(View.GONE);
//                Picasso.get().load(R.drawable.ic_baseline_refresh_24).into(holder.imageView);
//                break;
//            case Status.DOWNLOAD_STATUS_FILE_NOT_EXIST:
//                holder.progressBar.setVisibility(View.GONE);
//                holder.status.setTextColor(context.getResources().getColor(R.color.colorProtectionLow));
//                holder.cancel.setVisibility(View.GONE);
//                Picasso.get().load(R.drawable.close_black_btn).into(holder.imageView);
//                break;
//        }
//
//    }
//
//    private String getstatus(int status) {
//        switch (status) {
//            case Status.DOWNLOAD_STATUS_COMPLETED:
//                return "Download Completed";
//            case Status.DOWNLOAD_STATUS_ERROR:
//                return "Error";
//            case Status.DOWNLOAD_STATUS_FILE_NOT_EXIST:
//                return "file not exists";
//            case Status.DOWNLOAD_STATUS_PAUSED:
//                return "Paused";
//            case Status.DOWNLOAD_STATUS_DOWNLOADING:
//                return "Downloading";
//            case Status.DOWNLOAD_STATUS_PREPARED:
//                return "Starting Download";
//            case Status.DOWNLOAD_STATUS_PREPARING:
//                return "Preparing";
//            case Status.DOWNLOAD_STATUS_RETRYING:
//                return "Retrying";
//            case Status.DOWNLOAD_STATUS_UNKNOWN:
//                return "Unknown Error";
//            case Status.DOWNLOAD_STATUS_WAITING:
//                return "Waiting";
//        }
//        return "Error";
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//        TextView title, cancel, status, extra;
//        ProgressBar progressBar;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView=itemView.findViewById(R.id.image);
//            title=itemView.findViewById(R.id.title);
//            cancel=itemView.findViewById(R.id.cancel);
//            status=itemView.findViewById(R.id.download_status);
//            extra=itemView.findViewById(R.id.extra);
//            progressBar=itemView.findViewById(R.id.progressBar);
//            imageView.setOnClickListener(v -> {
//                if (list.get(getAdapterPosition()).getStatus() == Status.DOWNLOAD_STATUS_DOWNLOADING) {
//                    FileDownloader.pause(list.get(getAdapterPosition()).getUrl());
//                } else if (list.get(getAdapterPosition()).getStatus() != Status.DOWNLOAD_STATUS_COMPLETED || list.get(getAdapterPosition()).getStatus() != Status.DOWNLOAD_STATUS_ERROR || list.get(getAdapterPosition()).getStatus() == Status.DOWNLOAD_STATUS_PAUSED) {
//                    Intent intent=new Intent(context, YourService.class);
//                    context.startService(intent);
//
//                    FileDownloader.start(list.get(getAdapterPosition()).getUrl());
//                }
//            });
//            cancel.setOnClickListener(v -> {
//                FileDownloader.delete(list.get(getAdapterPosition()).getUrl(), true, null);
//                notifyDataSetChanged();
//            });
//        }
//    }
//
//    private String getBytesDownloaded(int progress, long totalBytes) {
//        //Greater than 1 MB
//        long bytesCompleted=(progress * totalBytes) / 100;
//        if (totalBytes >= 1000000) {
//            return ("" + (String.format("%.1f", (float) bytesCompleted / 1000000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000000)) + "MB");
//        }
//        if (totalBytes >= 1000) {
//            return ("" + (String.format("%.1f", (float) bytesCompleted / 1000)) + "/" + (String.format("%.1f", (float) totalBytes / 1000)) + "Kb");
//
//        } else {
//            return ("" + bytesCompleted + "/" + totalBytes);
//        }
//    }
//}
