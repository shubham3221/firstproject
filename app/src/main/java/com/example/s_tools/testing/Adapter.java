package com.example.s_tools.testing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.download.library.DownloadTask;
import com.example.s_tools.BuildConfig;
import com.example.s_tools.R;
import com.example.s_tools.TinyDB;
import com.example.s_tools.tools.ToastMy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


class TestingAdapter extends RecyclerView.Adapter<TestingAdapter.MyViewHolder> {
    Context context;
    List<Downloadmodel> list;
    TinyDB tinyDB;
    Clicklistner clickListner;


    public TestingAdapter(Context context, List<Downloadmodel> list, Clicklistner clickListner) {
        this.context=context;
        this.list=list;
        this.tinyDB=new TinyDB(context);
        this.clickListner=clickListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.testingcustomlayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        switch (list.get(position).getStatus()) {
            case DownloadTask.STATUS_CANCELED:
                holder.status.setTextColor(Color.parseColor("#ffff7043")); //red
                holder.status.setText("Cancelled");
                holder.cancel.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
                holder.pauseImage.setBackgroundResource(R.drawable.ic_baseline_close_24);
                break;

            case DownloadTask.STATUS_DOWNLOADING:
                holder.status.setText("Running");
                holder.status.setTextColor(Color.parseColor("#008EFF"));
                holder.cancel.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.VISIBLE);
                if (list.get(position).getProgress() < 1) {
                    holder.progressBar.setIndeterminate(true);
                } else {
                    holder.progressBar.setIndeterminate(false);
                }
                holder.progressBar.setProgress(list.get(position).getProgress());
                holder.pauseImage.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);
                break;

            case DownloadTask.STATUS_PENDDING:
                //status
                holder.status.setText("Pending");
                holder.status.setTextColor(context.getResources().getColor(R.color.white_hint));
                //instruction
                //percent
                holder.percent.setText("-/-");
                //cancel
                holder.cancel.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.progressBar.setIndeterminate(true);
                holder.pauseImage.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);
                break;

            case DownloadTask.STATUS_PAUSED:
                //status
                holder.status.setText("Paused");
                holder.status.setTextColor(context.getResources().getColor(R.color.yellow));
                //instruction
                //percent
                holder.percent.setText(list.get(position).getFile_size());
                //cancel
                holder.cancel.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.progressBar.setProgress(list.get(position).getProgress());
                holder.progressBar.setIndeterminate(false);
                holder.pauseImage.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
                break;


            case DownloadTask.STATUS_PAUSING:
                //status
                holder.status.setText("Pausing...");
                holder.status.setTextColor(context.getResources().getColor(R.color.yellow));
                //instruction
                //percent
                holder.percent.setText(list.get(position).getFile_size());
                //cancel
                holder.cancel.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.progressBar.setProgress(list.get(position).getProgress());
                holder.progressBar.setIndeterminate(true);
                holder.pauseImage.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
                break;

            case DownloadTask.STATUS_SUCCESSFUL:
                //status
                holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                holder.status.setText("Successful");
                holder.status.setTextColor(context.getResources().getColor(R.color.switch_on_master));
//                holder.status.setTextColor(context.getResources().getColor(R.color.cloud_setup_microsoft_sign_in_button_background));
                //instruction
                //cancel
                holder.cancel.setVisibility(View.GONE);
                //progress
                holder.progressBar.setVisibility(View.GONE);
                holder.pauseImage.setBackgroundResource(0);
                if (list.get(position).getTitle().endsWith(".mp3") || list.get(position).getTitle().endsWith("m4a")) {
                    Picasso.get().load(R.drawable.mp3_png2).into(holder.pauseImage);
                } else if (list.get(position).getTitle().endsWith(".jpg") || list.get(position).getTitle().endsWith(".jpeg") || list.get(position).getTitle().endsWith(".png") || list.get(position).getTitle().endsWith(".gif")) {
                    Picasso.get().load(R.drawable.jpg_png).into(holder.pauseImage);
                } else if (list.get(position).getTitle().endsWith(".mp4") || list.get(position).getTitle().endsWith(".mkv")) {
                    Picasso.get().load(R.drawable.mp4_png).into(holder.pauseImage);
                }else if (list.get(position).getTitle().endsWith(".text") || list.get(position).getTitle().endsWith(".txt")) {
                    Picasso.get().load(R.drawable.text).into(holder.pauseImage);
                } else {
                    Picasso.get().load(R.drawable.unknown_png).into(holder.pauseImage);
                }
                break;
            case DownloadTask.STATUS_ERROR:
                holder.status.setText("error");
                holder.status.setTextColor(Color.parseColor("#ffff7043")); //red
                holder.cancel.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
                holder.pauseImage.setBackgroundResource(R.drawable.ic_baseline_refresh_24);
                break;

            default:
                holder.status.setText("Something went wrong");
                holder.status.setTextColor(Color.parseColor("#ffff7043")); //red
                holder.cancel.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.GONE);
                holder.pauseImage.setBackgroundResource(R.drawable.ic_baseline_refresh_24);
                break;
        }
        holder.title.setText(list.get(position).getTitle());
        holder.percent.setText(list.get(position).getFile_size());
    }

    @Override
    public int getItemCount() {
        if (list.isEmpty()) {
            return 0;
        }
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView pauseImage;
        TextView title, status, percent, cancel;
        ProgressBar progressBar;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pauseImage=itemView.findViewById(R.id.pausedl);
            title=itemView.findViewById(R.id.filetitle);
            status=itemView.findViewById(R.id.statusdl);
            percent=itemView.findViewById(R.id.sizedl);
            progressBar=itemView.findViewById(R.id.progressdl);
            cancel=itemView.findViewById(R.id.cancelTextview);
            linearLayout=itemView.findViewById(R.id.downloadcardview);
            cancel.setOnClickListener(v -> {
                cancel.setVisibility(View.GONE);
                clickListner.cancelitemClicked(getAdapterPosition());
            });
            pauseImage.setOnClickListener(v -> {
                clickListner.pauseitemClicked(getAdapterPosition());
            });
            itemView.setOnClickListener(view -> {
                if (list.get(getAdapterPosition()).getStatus() == DownloadTask.STATUS_SUCCESSFUL) {
                    ToastMy.successToast(context, "Please Wait...", ToastMy.LENGTH_SHORT);
                    String path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + list.get(getAdapterPosition()).getTitle();
                    File file=new File(list.get(getAdapterPosition()).getPath());
                    String extension=MimeTypeMap.getFileExtensionFromUrl(file.getPath());
                    String type=MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);


//                    Intent intent = new Intent(Intent.ACTION_VIEW)//
//                            .setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
//                                            FileProvider.getUriForFile(context,context.getPackageName() + ".provider", file) : Uri.fromFile(file),
//                                    "image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


//                    Intent baseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("file:///storage/6632-6133/dcef8d0a33de61fff33f609af18e3f24.jpg"));
//                    baseIntent.setType(type);
//
//                    if (baseIntent.resolveActivity(context.getPackageManager()) != null) {
//                        context.startActivity(baseIntent);
//                    }
//                    else {
//                        ToastMy.errorToast(context.getApplicationContext(), "File unable to open", Toast.LENGTH_LONG);
//                    }

//                    Uri uri =  Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + list.get(getAdapterPosition()).getTitle()));
//                    Uri uri=FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                    Uri uri=FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                    Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    String mime="*/*";
//                    String mime=type;
//                    MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
//                    if (mimeTypeMap.hasExtension(mimeTypeMap.getFileExtensionFromUrl(uri.toString()))) {
//                        mime=mimeTypeMap.getMimeTypeFromExtension(mimeTypeMap.getFileExtensionFromUrl(uri.toString()));
//                    }
//                    intent.setDataAndType(uri, type);
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        ToastMy.errorToast(context.getApplicationContext(), "File unable to open", Toast.LENGTH_LONG);
                    }
                }
            });
        }

    }
}

