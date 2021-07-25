package com.example.s_tools.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;


import java.io.File;
import java.io.IOException;


public class DownloadUtils {

    @NonNull
    public static String getMimeType(@NonNull final Context context, @NonNull final Uri uri) {
        final ContentResolver cR = context.getContentResolver();
        final MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));
        if (type == null) {
            type = "*/*";
        }
        return type;
    }

    public static void deleteFileAndContents(final File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                final File[] contents = file.listFiles();
                if (contents != null) {
                    for (final File content : contents) {
                        deleteFileAndContents(content);
                    }
                }
            }
            file.delete();
        }
    }

//    @NonNull
//    public static String getETAString(@NonNull final Context context, final long etaInMilliSeconds) {
//        if (etaInMilliSeconds < 0) {
//            return "";
//        }
//        int seconds = (int) (etaInMilliSeconds / 1000);
//        long hours = seconds / 3600;
//        seconds -= hours * 3600;
//        long minutes = seconds / 60;
//        seconds -= minutes * 60;
//        if (hours > 0) {
//            return context.getString(R.string.download_eta_hrs, hours, minutes, seconds);
//        } else if (minutes > 0) {
//            return context.getString(R.string.download_eta_min, minutes, seconds);
//        } else {
//            return context.getString(R.string.download_eta_sec, seconds);
//        }
//    }

//    @NonNull
//    public static String getDownloadSpeedString(@NonNull final Context context, final long downloadedBytesPerSecond) {
//        if (downloadedBytesPerSecond < 0) {
//            return "";
//        }
//        double kb = (double) downloadedBytesPerSecond / (double) 1000;
//        double mb = kb / (double) 1000;
//        final DecimalFormat decimalFormat = new DecimalFormat(".##");
//        if (mb >= 1) {
//            return context.getString("mb", decimalFormat.format(mb));
//        } else if (kb >= 1) {
//            return context.getString("kb", decimalFormat.format(kb));
//        } else {
//            return context.getString("byte", downloadedBytesPerSecond);
//        }
//    }

    public static File createFile(String filePath) {
        final File file = new File(filePath);
        if (!file.exists()) {
            final File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static int getProgress(long downloaded, long total) {
        if (total < 1) {
            return -1;
        } else if (downloaded < 1) {
            return 0;
        } else if (downloaded >= total) {
            return 100;
        } else {
            return (int) (((double) downloaded / (double) total) * 100);
        }
    }
    public static void openFile(Context context, String path, Uri providerUri) {
        if (path == null)
            return;
        path = Uri.parse(path).getPath().toLowerCase();
        //Uri uri = Uri.fromFile(new File(path));
        Uri uri = providerUri;
        if (providerUri == null)
            try {
                uri = FileProvider.getUriForFile(context
                        , context.getPackageName() + ".fileProvider"
                        , new File(path));
            } catch (Exception e) {
                return;
            }
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (path.contains(".doc")
                || path.contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (path.contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (path.contains(".ppt") || path.contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (path.contains(".xls") || path.contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (path.contains(".zip") || path.contains(".rar")) {
            // zip file
            intent.setDataAndType(uri, "application/zip");
        } else if (path.contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (path.contains(".wav") || path.contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (path.contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (path.contains(".jpg") || path.contains(".jpeg") || path.contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (path.contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (path.contains(".3gp")
                || path.contains(".mpg")
                || path.contains(".mpeg")
                || path.contains(".mpe")
                || path.contains(".mp4")
                || path.contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    private void file_rename(){
        String characterFilter="[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";

        File file=new File("path"+File.separator+"title".replaceAll(characterFilter,""));
        if (file.exists()){
            for (int num = 0; file.exists(); num++) {
                file = new File("path"+File.separator+"("+num+")"+"title".replaceAll(characterFilter,""));
            }
        }
    }
}
