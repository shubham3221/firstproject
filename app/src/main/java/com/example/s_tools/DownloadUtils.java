package com.example.s_tools;//package com.example.s_tools;
//
//import android.net.Uri;
//import android.os.Environment;
//import android.webkit.URLUtil;
//
//import com.download.library.DownloadTask;
//import com.thin.downloadmanager.DefaultRetryPolicy;
//import com.thin.downloadmanager.DownloadRequest;
//import com.thin.downloadmanager.ThinDownloadManager;
//
//public class DownloadUtils {
//    public static ThinDownloadManager manager = new ThinDownloadManager();
//    public static Uri getDownloadUri(String url) {
//        return Uri.parse(url);
//    }
//
//    public static Uri downloadLocation(String url) {
//        return Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + URLUtil.guessFileName(url, null, null));
//    }
//
//    public static DownloadRequest getDownloadRequest(String url) {
//        Uri downloadUri=Uri.parse(url);
//        String downloadPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//        String fileName=URLUtil.guessFileName(url, null, null);
//        Uri destinationUri=Uri.parse(downloadPath + "/" + fileName);
//
//        return new DownloadRequest(downloadUri).setRetryPolicy(new DefaultRetryPolicy()).setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.HIGH).setDownloadResumable(true);
//    }
//
//
//    public static String getStatus(int query) {
//        switch (query) {
//            case ThinDownloadManager.ERROR_DOWNLOAD_CANCELLED:
//                return "Download Cancelled";
//            case ThinDownloadManager.ERROR_CONNECTION_TIMEOUT_AFTER_RETRIES:
//                return "Timeout";
//            case ThinDownloadManager.ERROR_DOWNLOAD_SIZE_UNKNOWN:
//                return "File Size Unknown";
//            case ThinDownloadManager.ERROR_FILE_ERROR:
//                return "File Error";
//            case ThinDownloadManager.ERROR_HTTP_DATA_ERROR:
//                return "Connection Error";
//            case ThinDownloadManager.ERROR_MALFORMED_URI:
//                return "MailFormed Uri Error";
//            case ThinDownloadManager.ERROR_TOO_MANY_REDIRECTS:
//                return "Too Many Redirect";
//            case ThinDownloadManager.ERROR_UNHANDLED_HTTP_CODE:
//                return "Unhandled Request";
//            case ThinDownloadManager.STATUS_CONNECTING:
//                return "Connecting";
//            case ThinDownloadManager.STATUS_FAILED:
//                return "Failed";
//            case ThinDownloadManager.STATUS_NOT_FOUND:
//                return "Not Found";
//            case ThinDownloadManager.STATUS_PENDING:
//                return "Pending";
//            case ThinDownloadManager.STATUS_RETRYING:
//                return "Retrying";
//            case ThinDownloadManager.STATUS_RUNNING:
//                return "Running";
//            case ThinDownloadManager.STATUS_STARTED:
//                return "Started";
//            case ThinDownloadManager.STATUS_SUCCESSFUL:
//                return "Successful";
//            default:
//                return "Error";
//        }
//    }
//}
