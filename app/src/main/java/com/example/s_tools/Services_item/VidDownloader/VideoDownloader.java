package com.example.s_tools.Services_item.VidDownloader;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.example.s_tools.R;
import com.example.s_tools.Services_item.mp3download.Adapter;
import com.example.s_tools.Services_item.mp3download.Model;
import com.example.s_tools.Splash_after_reg;
import com.example.s_tools.testing.DownloadActivity;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.example.s_tools.tools.retrofitcalls.VC;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.example.s_tools.testing.DownloadActivity.INTENTDOWNLINK;

public class VideoDownloader extends AppCompatActivity {
    public static final String TAG="//";
    //    private static final String FORM_URL_ENCODED="application/x-www-form-urlencoded";
    RecyclerView recyclerView;
    EditText enterurl;
    TextView textView, fetchtxt;
    ProgressBar progressBar;
    CardView fetchbtn;
    RecyclerAdapter adapter;
    RecyclerAdapter2 adapter2;
    Toolbar toolbar;
    Button paste;
    List<String> downloadlinks;
    Adapter onlineRecyclerAdapter;
    String fullUrl="https://www.youtube.com/watch?v=";

    RadioButton server1, server2;
    List<Model> list;
    LinearLayout server2Layout;
    boolean running=false;
    private KProgressHUD progressHUD;
    int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_downloader);
        init();
        loadad();
        fetchbtn.setOnClickListener(v -> {
            count=0;
            if (enterurl.getText().toString().length() > 5 && !running) {
                running=true;
//                InputMethodManager imm=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
                String url=fullUrl + getVideoIdFromYoutubeUrl(enterurl.getText().toString());
                if (url.contains("music")) {
                    url=url.replace("music", "www");
                }
                if (server1.isChecked()) {
                    progressBar.setVisibility(View.VISIBLE);
                    server2Layout.setVisibility(View.GONE);
                    new Fetchdata(VideoDownloader.this).extract(url, true, false);
                } else {
                    progressHUD=KProgressHUD.create(VideoDownloader.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Fetching...").setCancellable(false).setDimAmount(0.1f).show();
                    checkVersion();
                }
            }

        });
        paste.setOnClickListener(v -> {
            android.content.ClipboardManager clipboard=(android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // If it does contain data, decide if you can handle the data.
            if (!(clipboard.hasPrimaryClip())) {
            } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                // since the clipboard has data but it is not plain text

            } else {
            ClipData.Item item=clipboard.getPrimaryClip().getItemAt(0);
            enterurl.setText(item.getText().toString());
        }
        });
//        clear.setOnClickListener(view -> enterurl.setText(""));
    }


    class Fetchdata extends YouTubeExtractor {
        public Fetchdata(@NonNull Context con) {
            super(con);
        }

        @Override
        public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
            if (ytFiles != null) {
                List<VideoMeta> list=new ArrayList<>();
                downloadlinks=new ArrayList<>();
                list.add(vMeta);
//                downloadlinks.add(ytFiles.get(22).getUrl());
//                downloadlinks.add(ytFiles.get(18).getUrl());
                for (int i=0, itag; i < ytFiles.size(); i++) {
                    itag=ytFiles.keyAt(i);
                    YtFile ytFile=ytFiles.get(itag);
                    if (ytFile.getFormat().getAudioBitrate() != -1) {
                        downloadlinks.add(ytFile.getUrl());
                    }
                }
                adapter=new RecyclerAdapter(VideoDownloader.this, list, downloadlinks);
                publishProgress();

            } else {
                running=false;
                progressBar.setVisibility(View.GONE);
                ToastMy.errorToast(VideoDownloader.this, Cvalues.OFFLINE, ToastMy.LENGTH_LONG);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            fetchtxt.setText("Fetch");
            progressBar.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(VideoDownloader.this));
            recyclerView.setAdapter(adapter);
            running=false;

        }
    }

    private void method2() {
        new Thread(() -> {
            try {
                Log.e(TAG, "method2: " );
                publishProgress("Getting Hash Key...");
                ajexSearch(enterurl.getText().toString());
            } catch (Exception e) {
                progressHUD.dismiss();
                ToastMy.errorToast(VideoDownloader.this, Cvalues.OFFLINE, ToastMy.LENGTH_SHORT);
            }
        }).start();
    }

    private void publishProgress(String msg) {
        runOnUiThread(() -> {
            progressHUD.setLabel(msg);
        });
    }

    private void ajexSearch(String token) {
        try {
            publishProgress("Getting Download Links");
            Connection.Response response=Jsoup.connect(MySharedPref.getyoutubeVideo_part1(VideoDownloader.this).trim() + token).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36").referrer("http://www.google.com").ignoreContentType(true).method(Connection.Method.POST).header("Content-Type", "application/x-www-form-urlencoded").data("q", enterurl.getText().toString()).data("vt", "home").execute();
            Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();

            publishProgress("Decrypting Download Links");
            String body=response.body();
            JSONObject obj=new JSONObject(body);
            List<VideoModel> videoModels=inserting_data(obj);
            publishProgress("Successful");
            adapter2=new RecyclerAdapter2(videoModels, VideoDownloader.this);
            runOnUiThread(() -> {
                count=0;
                server2Layout.setVisibility(View.VISIBLE);
                fetchtxt.setText("Fetch");
                recyclerView.setLayoutManager(new LinearLayoutManager(VideoDownloader.this));
                recyclerView.setAdapter(adapter2);
                progressHUD.dismiss();
                running=false;
                adapter2.notifyDataSetChanged();
            });
            RecycleClick.addTo(recyclerView).setOnItemClickListener((recyclerView, i, view) -> {
                progressHUD.show();
                progressHUD.setLabel("Decrypting...");
                ajaxConvert(token, videoModels.get(0).getId(), videoModels.get(i).getConvertid());
            });
            adapter2.notifyDataSetChanged();
        } catch (Exception e) {
            if (e.getMessage().contains("timeout")){
                if (count==3){
                    runOnUiThread(() -> {
                        ToastMy.errorToast(VideoDownloader.this,e.getLocalizedMessage(),ToastMy.LENGTH_SHORT);
                        progressHUD.dismiss();
                    });
                }else {
                    count++;
                    ajexSearch(token);
                }
            }
        }
    }

    private void ajaxConvert(String token, String vid, String convertID) {
        new Thread(() -> {
            try {
                Connection.Response response=Jsoup.connect(MySharedPref.getyoutubeVideo_part2(VideoDownloader.this).trim() + token).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36").referrer("http://www.google.com").ignoreContentType(true).method(Connection.Method.POST).timeout(7000).header("Content-Type", "application/x-www-form-urlencoded").data("vid", vid).data("k", convertID).execute();
                String body=response.body();
                JSONObject obj=new JSONObject(body);
                Log.e(TAG, "ajaxConvert: "+obj.toString() );
                boolean dlink=obj.has("dlink");
                runOnUiThread(() -> {
                    count=0;
                    progressHUD.dismiss();
                    if (dlink) {
                        Log.e(TAG, "ajaxConvert: "+obj.optString("dlink") );
                        Intent intent=new Intent(VideoDownloader.this, DownloadActivity.class);
                        intent.putExtra(INTENTDOWNLINK, obj.optString("dlink"));
                        try {
                            intent.putExtra(DownloadActivity.FILENAME, obj.getString("title")+"."+obj.getString("ftype"));
                        } catch (JSONException e) {
                            Toast.makeText(this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    } else {
                        ToastMy.errorToast(VideoDownloader.this, Cvalues.OFFLINE, ToastMy.LENGTH_SHORT);
                    }
                });
            } catch (Exception e) {
                if (e.getMessage().contains("timeout")){
                    if (count==3){
                        count++;
                        ajaxConvert(token, vid, convertID);
                    }else {
                        runOnUiThread(() -> {
                            ToastMy.errorToast(VideoDownloader.this,e.getMessage()+". Retrying...",ToastMy.LENGTH_LONG);
                        });
                    }
                }else {
                    runOnUiThread(() -> {
                        ToastMy.errorToast(VideoDownloader.this,e.getMessage(),ToastMy.LENGTH_LONG);
                        progressHUD.dismiss();
                    });
                }
            }
        }).start();
    }

    private List<VideoModel> inserting_data(JSONObject obj) throws Exception {
        String quality1="18";
        String quality2="22";
        String quality3="133";
        String quality4="135";
        String quality5="137";
        String quality6="160";
        String mp3quality="mp3128";
        List<VideoModel> list=new ArrayList<>();

        if (obj.optJSONObject("links").optJSONObject("mp4").has(quality1)) {
            list.add(new VideoModel(obj.getString("title"), obj.getString("vid"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality1).optString("size"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality1).optString("q"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality1).optString("f"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality1).optString("k")));
        }
        if (obj.optJSONObject("links").optJSONObject("mp4").has(quality2)) {
            list.add(new VideoModel(obj.getString("title"), obj.getString("vid"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality2).optString("size"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality2).optString("q"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality2).optString("f"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality2).optString("k")));
        }
        if (obj.optJSONObject("links").optJSONObject("mp4").has(quality3)) {
            list.add(new VideoModel(obj.getString("title"), obj.getString("vid"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality3).optString("size"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality3).optString("q"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality3).optString("f"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality3).optString("k")));
        }
        if (obj.optJSONObject("links").optJSONObject("mp4").has(quality4)) {
            list.add(new VideoModel(obj.getString("title"), obj.getString("vid"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality4).optString("size"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality4).optString("q"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality4).optString("f"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality4).optString("k")));
        }
        if (obj.optJSONObject("links").optJSONObject("mp4").has(quality5)) {
            list.add(new VideoModel(obj.getString("title"), obj.getString("vid"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality5).optString("size"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality5).optString("q"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality5).optString("f"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality5).optString("k")));
        }
        if (obj.optJSONObject("links").optJSONObject("mp4").has(quality6)) {
            list.add(new VideoModel(obj.getString("title"), obj.getString("vid"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality6).optString("size"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality6).optString("q"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality6).optString("f"), obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality6).optString("k")));
        }
        if (obj.optJSONObject("links").optJSONObject("mp3").has(mp3quality)) {
            list.add(new VideoModel(obj.getString("title"), obj.getString("vid"), obj.optJSONObject("links").optJSONObject("mp3").optJSONObject(mp3quality).optString("size"), obj.optJSONObject("links").optJSONObject("mp3").optJSONObject(mp3quality).optString("q"), obj.optJSONObject("links").optJSONObject("mp3").optJSONObject(mp3quality).optString("f"), obj.optJSONObject("links").optJSONObject("mp3").optJSONObject(mp3quality).optString("k")));
        }
        return list;

    }


    private void init() {
        recyclerView=findViewById(R.id.vd_recyclerview);
        enterurl=findViewById(R.id.vdedittext);
        textView=findViewById(R.id.vdText);
        progressBar=findViewById(R.id.vdprogress);
        fetchbtn=findViewById(R.id.vdget);
        fetchtxt=findViewById(R.id.fetchtxt);
        toolbar=findViewById(R.id.vidtoolbar);
        paste=findViewById(R.id.paste2);
//        clear=findViewById(R.id.cleanvid);
        server1=findViewById(R.id.server1);
        server2=findViewById(R.id.server2);
        server2Layout=findViewById(R.id.server2layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getVideoIdFromYoutubeUrl(String url) {
        String videoId=null;
        String regex="http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern=Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(url);
        if (matcher.find()) {
            videoId=matcher.group(1);
        }
        return videoId;
    }

    private void loadad() {
        AdView adView=new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(Cvalues.banr);
        adView=findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void checkVersion() {
        if (MySharedPref.getyoutubeVideo_part1(VideoDownloader.this) == null) {
            VC.check_specific(VideoDownloader.this, true, false,false, (success, updateinfo) -> {
                if (success) {
                    method2();
                } else {
                    Toast.makeText(this, "Server Error: Please Try Again!", Toast.LENGTH_SHORT).show();
                    progressHUD.dismiss();
                }
            });
        } else {
            method2();
//            fetchMp3();
        }
        running=false;
    }

}