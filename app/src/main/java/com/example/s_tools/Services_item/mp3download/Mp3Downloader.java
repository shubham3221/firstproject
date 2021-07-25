package com.example.s_tools.Services_item.mp3download;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.Splash_after_reg;
import com.example.s_tools.testing.DownloadActivity;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.example.s_tools.tools.retrofitcalls.VC;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import okhttp3.CertificatePinner;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.example.s_tools.testing.DownloadActivity.INTENTDOWNLINK;

interface Mp3inter {
    String core_url="https://x2convert.com/ajax2/";
    String PINS="sha256/gGOcYKAwzEaUfun6YdxZvFSQq/x2lF/R8UizDFofveY=";
    ConnectionSpec spec=new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256).build();
    String PATTERN="evrsh.blogpsotcom";
    OkHttpClient client=new OkHttpClient.Builder().connectionSpecs(Collections.singletonList(spec)).certificatePinner(new CertificatePinner.Builder().add(PATTERN, PINS).build()).build();
    //Creating retrofit Object
    Retrofit retrofit=new Retrofit.Builder().baseUrl(core_url).
            client(client).addConverterFactory(GsonConverterFactory.create()).build();
    @GET
    Call<JsonObject> getPremiumPost(@Url() String url);
}

public class Mp3Downloader extends AppCompatActivity {
    public static final String TAG="//";
    public static final String YOUTUBE_COM_V_7_WATCH_V="https://www.320youtube.com/v7/watch?v=";
    public static final String BTN_BTN_SUCCESS_BTN_LG="btn btn-success btn-lg";
    public static final String REGEX="http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
    public static final String JS="javascript:(function(){" + "l=document.getElementsByClassName('btn btn-lg btn-success')[0];" + "l.click();" + "})()";
    public static final String YOUTUBE_TO_MP_3_MUSIC="https://x2convert.com/download-youtube-to-mp3-music";
    public static final String GET_FILE_ASHX_LINKINFO="https://x2convert.com/ajax2/getFile.ashx?linkinfo=";
    public static final String LANG_EN_OPTION_COUNTRY_IN="&lang=en&option=&country=IN/";
    public static final String ELEMENT_BY_ID_TXT_LINK_VALUE="javascript:document.getElementById('txtLink').value='";
    public static final String DOCUMENT_GET_ELEMENT_BY_ID_BTN_GET_CLICK="';document.getElementById('btnGet').click()";
    public static final String MORE_THEN_30_SECONDS="it won't take more then 30 seconds.";
    public static final String TEXT_SHADOW_1="text-shadow-1";
    private static final String GETTING_HASH_KEY="Getting Hash Key";
    String fullUrl="https://www.youtube.com/watch?v=";
    ImageView img;
    CardView fetch;
    LinearLayout container;
    EditText editText;
    TextView fetch_txt, title, length;
    Button d1, d2, paste;
    ProgressBar progressBar;
    String link320;
    String link128;
    Toolbar toolbar;
    String youtubeUrl;
    WebView webView;
    KProgressHUD progressHUD;
    RecyclerView recyclerView;
    List<Model> list;
    Adapter adapter;
    boolean running=false;
    RadioButton server1, server2;
    boolean Converting= true;
    private int count=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3_downloader);
        init();
        new Handler().postDelayed(this::adsetup,200);

        fetch.setOnClickListener(v -> {
            if (editText.getText().toString().length() > 6) {
                running=true;
                youtubeUrl=editText.getText().toString();
                if (youtubeUrl.contains("music")){
                    youtubeUrl =youtubeUrl.replace("music", "www");
                }
                progressBar.setVisibility(View.VISIBLE);
                Log.e(TAG, "onCreate: "+fullUrl+getVideoIdFromYoutubeUrl(youtubeUrl) );
                new Fetchdata(Mp3Downloader.this).extract(fullUrl+getVideoIdFromYoutubeUrl(youtubeUrl), true, false);
            } else {
                ToastMy.errorToast(Mp3Downloader.this, Cvalues.INVALID_URL, ToastMy.LENGTH_SHORT);
            }
        });

        paste.setOnClickListener(v -> {
            android.content.ClipboardManager clipboard=(android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // If it does contain data, decide if you can handle the data.
            if (!(clipboard.hasPrimaryClip())) {
            } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                // since the clipboard has data but it is not plain text

            } else {
                //since the clipboard contains plain text.
                ClipData.Item item=clipboard.getPrimaryClip().getItemAt(0);

                // Gets the clipboard as text.
                editText.setText(item.getText().toString());
            }
        });

    }

    private void server2() {
        progressHUD.show();
        progressHUD.setProgress(10);
        progressHUD.setLabel(GETTING_HASH_KEY);
        progressHUD.setDetailsLabel(MORE_THEN_30_SECONDS);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                    runOnUiThread(this::webviewSetup);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            private void webviewSetup() {
                webView=new WebView(getApplicationContext());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webView.loadUrl(YOUTUBE_TO_MP_3_MUSIC);
                webView.setWebViewClient(new WebViewClient() {
                    boolean reloaded=false;

                    @RequiresApi(api=Build.VERSION_CODES.M)
                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                        progressHUD.dismiss();
                        Toast.makeText(Mp3Downloader.this, Cvalues.TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                        super.onReceivedHttpError(view, request, errorResponse);
                        progressHUD.dismiss();
                        Toast.makeText(Mp3Downloader.this, Cvalues.TRY_AGAIN, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        progressHUD.setProgress(20);
                        progressHUD.setLabel(Cvalues.CONVERTING);
                        progressHUD.setDetailsLabel("Hold On");
                        new Thread(() -> {
                            while (Converting){
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(() -> {
                                    if (Converting){
                                        if (count==80){
                                            Converting = false;
                                        }
                                        progressHUD.setProgress(count++);
                                    }
                                });
                            }
                        }).start();
                        view.evaluateJavascript(runJavaScript(youtubeUrl), s -> {
                            if (reloaded) {
                                Mp3inter mp3inter=Mp3inter.retrofit.create(Mp3inter.class);
                                mp3inter.getPremiumPost(GET_FILE_ASHX_LINKINFO + youtubeUrl + LANG_EN_OPTION_COUNTRY_IN).enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        Converting= false;
                                        try {
                                            progressHUD.setLabel("Converted Successful");
                                            progressHUD.setDetailsLabel(Cvalues.ALMOST_FINISH);
                                            String message=response.body().get("Message").getAsString();
                                            link128=message;
                                            progressHUD.setProgress(80);
                                            finalcall();
                                        } catch (Exception e) {
                                            progressHUD.dismiss();
                                            ToastMy.errorToast(Mp3Downloader.this, Cvalues.TRY_AGAIN, ToastMy.LENGTH_SHORT);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        Log.e(TAG, "failed:1 "+t.getMessage() );
                                        ToastMy.errorToast(Mp3Downloader.this, Cvalues.TRY_AGAIN, ToastMy.LENGTH_SHORT);
                                        progressHUD.dismiss();
                                    }
                                });
                            } else {
                                reloaded=true;
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void adsetup() {
        AdView adView=new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(Cvalues.banr);
        adView=findViewById(R.id.adView);
        adView.setVisibility(View.VISIBLE);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    class Fetchdata extends YouTubeExtractor {
        public Fetchdata(@NonNull Context con) {
            super(con);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            link128=null;
            link320=null;
        }


        @Override
        public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
            Log.e(TAG, "onExtractionComplete: "+vMeta+" files "+ytFiles );
            if (vMeta != null) {
                runOnUiThread(() -> {
                    fetch_txt.setText("Fetch");
                    container.setVisibility(View.VISIBLE);
                    Picasso.get().load(vMeta.getHqImageUrl()).placeholder(R.drawable.ic_baseline_image_24).into(img);
                    title.setText(vMeta.getTitle());
                    length.setText(vMeta.getVideoLength() / 60 + " min");
                    progressHUD=KProgressHUD.create(Mp3Downloader.this).setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE).setLabel(Cvalues.CONVERTING).setCancellable(false).setDimAmount(0.1f).setMaxProgress(100).show();
                    if (server1.isChecked()){
                        server2();
                    }else {
                        ToastMy.errorToast(Mp3Downloader.this,"Server Down! Choose Another Server",ToastMy.LENGTH_LONG);
                    }
                });
            } else {
                ToastMy.errorToast(Mp3Downloader.this, Cvalues.UNABLE, ToastMy.LENGTH_LONG);
            }
            progressBar.setVisibility(View.GONE);
            running=false;
        }

        @Override
        protected void onPostExecute(SparseArray<YtFile> ytFiles) {
            super.onPostExecute(ytFiles);
            Log.e(TAG, "onPostExecute: " );
        }
    }

    private void checkVersion(String videoId,String title) {
        if (MySharedPref.getyoutubemp3(Mp3Downloader.this) == null) {
            VC.check(Mp3Downloader.this, (success, updateinfo) -> {
                if (success) {
                    down_320(videoId,title);
                } else {
                    Toast.makeText(this, "Server Error: Please Try Again!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    running=false;
                    progressHUD.dismiss();
                }
            });
        } else {
            Log.e(TAG, "checkVersion: " );
            down_320(videoId,title);
//            fetchMp3();
        }
    }

    private void fetchMp3() {
        new Thread(() -> {
            try {
                Document document=Jsoup.connect(MySharedPref.getyoutubemp3(Mp3Downloader.this) + getVideoIdFromYoutubeUrl(youtubeUrl)).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36").referrer("http://www.google.com").ignoreContentType(true).get();
                Elements elementsByAttribute=document.body().getElementsByAttribute(Cvalues.HREF);
                String text=document.getElementsByClass(TEXT_SHADOW_1).text();

                list=new ArrayList<>();
                for (int i=0; i < elementsByAttribute.size(); i++) {
                    Attributes attributes=elementsByAttribute.get(i).attributes();
                    if (elementsByAttribute.get(i).toString().contains("download") && text.contains("MP3")) {
                        list.add(new Model(attributes.get(Cvalues.HREF), text.split("MP3")[i + 1]));
                    }
                }
//                adapter=new Adapter(Mp3Downloader.this, list);

                runOnUiThread(() -> {
                    if (list.isEmpty()) {
                        ToastMy.errorToast(Mp3Downloader.this, "Try Again!", ToastMy.LENGTH_SHORT);
                    }
                    running=false;
                    progressBar.setVisibility(View.GONE);
                    progressHUD.dismiss();
                    recyclerView.setVisibility(View.VISIBLE);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(Mp3Downloader.this,LinearLayoutManager.VERTICAL,false));
                    recyclerView.setLayoutManager(new GridLayoutManager(Mp3Downloader.this, 2));
                    recyclerView.setAdapter(adapter);
                });
            } catch (Exception e) {
                ToastMy.errorToast(Mp3Downloader.this, Cvalues.OFFLINE, ToastMy.LENGTH_SHORT);
                e.printStackTrace();
            }
        }).start();
    }

    private void init() {
        paste=findViewById(R.id.pastedata);
        img=findViewById(R.id.mp3img);
        editText=findViewById(R.id.mp3_edit);
        progressBar=findViewById(R.id.mp3progress);
        container=findViewById(R.id.downloadContainer);
        fetch=findViewById(R.id.mp3get);
        toolbar=findViewById(R.id.mp3toolbar);
        fetch_txt=findViewById(R.id.fetchmp3);
        title=findViewById(R.id.titleofvid);
        length=findViewById(R.id.lvid);
        recyclerView=findViewById(R.id.listview);
        server1=findViewById(R.id.server1);
        server2=findViewById(R.id.server2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public String getVideoIdFromYoutubeUrl(String url) {
        String videoId=null;
        Pattern pattern=Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(url);
        if (matcher.find()) {
            videoId=matcher.group(1);
        }
        return videoId;
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

    private void down_320(String url,String title) {
        progressHUD.setLabel(Cvalues.CONVERTING);
        new Thread(() -> {
            int min=1;
            while (progressHUD.isShowing()) {
                try {
                    Thread.sleep(200);
                    int finalMin=min;
                    runOnUiThread(() -> {
                        progressHUD.setProgress(finalMin);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                min+=1;
            }
        }).start();

        new Thread(() -> {
            try {
                Document document=Jsoup.connect(MySharedPref.getyoutubemp3(Mp3Downloader.this) + url).get();
                runOnUiThread(() -> progressHUD.setProgress(99));
                Elements byAttribute=document.getElementsByAttribute("action");
                Log.e(TAG, "down_320:2 "+byAttribute.attr("action"));
                link320=byAttribute.attr("action");
                list=new ArrayList<>();
                if (link320.contains("320/")){
                    link128=link320;
                    String replace=link128.replace("320/", "128/");
                    list.add(new Model(replace, "MP3 128KBPS"));
                    list.add(new Model(link320, "MP3 320KBPS"));
                }else if (link320.contains("128/")){
                    list.add(new Model(link320, "MP3 128KBPS"));
                }

                adapter=new Adapter(Mp3Downloader.this, list,title);
                runOnUiThread(() -> {
                    if (list.isEmpty()){
                        ToastMy.errorToast(Mp3Downloader.this, Cvalues.UNABLE,ToastMy.LENGTH_SHORT);
                    }
                    progressHUD.dismiss();
                    running=false;
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new GridLayoutManager(Mp3Downloader.this, 2));
                    recyclerView.setAdapter(adapter);
//                    Intent intent=new Intent(Mp3Downloader.this, DownloadActivity.class);
//                    intent.putExtra(INTENTDOWNLINK, downLink320);
//                    startActivity(intent);
//                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Log.e(TAG, "down_320:error "+e.getMessage() );
                    progressHUD.dismiss();
                    ToastMy.errorToast(Mp3Downloader.this, Cvalues.TRY_AGAIN, ToastMy.LENGTH_SHORT);
                });
            }
        }).start();
    }

    void finalcall() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressHUD.dismiss();
                Toast.makeText(Mp3Downloader.this, Cvalues.TRY_AGAIN, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                progressHUD.dismiss();
                Toast.makeText(Mp3Downloader.this, Cvalues.TRY_AGAIN, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressHUD.setProgress(90);
                view.evaluateJavascript(JS, s -> {
                    progressHUD.setLabel("Starting Download");
                    progressHUD.setDetailsLabel("");
                });

            }
        });
        webView.loadUrl(link128);
        webView.setDownloadListener((s, s1, s2, s3, l) -> {
            progressHUD.dismiss();
            ToastMy.successToast(Mp3Downloader.this, "Downloading...", ToastMy.LENGTH_SHORT);
            Intent intent=new Intent(Mp3Downloader.this, DownloadActivity.class);
            intent.putExtra(INTENTDOWNLINK, s);
            startActivity(intent);
            webView.destroy();
        });
    }


    String runJavaScript(String url) {
        return ELEMENT_BY_ID_TXT_LINK_VALUE + url + DOCUMENT_GET_ELEMENT_BY_ID_BTN_GET_CLICK;

    }
}