package com.example.s_tools.Services_item.inst;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.s_tools.NetworkHelper;
import com.example.s_tools.R;
import com.example.s_tools.Services_item.mp3download.Mp3Downloader;
import com.example.s_tools.Splash_after_reg;
import com.example.s_tools.testing.DownloadActivity;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.example.s_tools.tools.ToastMy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.Collections;

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
import retrofit2.http.Headers;
import retrofit2.http.Url;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.example.s_tools.testing.DownloadActivity.INTENTDOWNLINK;

interface MyRetrofit {
    String PINS="sha256/gGOcYKAwzEaUfun6YdxZvFSQq/x2lF/R8UizDFofveY=";


    ConnectionSpec spec=new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256).build();
    String PATTERN="evrsh.blogpsotcom";
    OkHttpClient client=new OkHttpClient.Builder().connectionSpecs(Collections.singletonList(spec))
            .certificatePinner(new CertificatePinner.Builder().add(PATTERN, PINS).build()).build();

    //Creating retrofit Object


    Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.instagram.com/").
            client(NetworkHelper.InterLog.getIntercept()).addConverterFactory(GsonConverterFactory.create())
            .build();

    @Headers({
//            "Accept: application/json",
            "Accept: */*",
            "User-Agent: "+ Cvalues.USER_AGENT
    })
    @GET()
    Call<JsonObject> getPost(@Url String url);
}

public class InstFragment extends Fragment {
    public static final String PLEASE_SELECT_TYPE="Please select type";
    public static final String INVALID_URL="invalid url";
    ProgressBar progressBar;
    CardView fetch;
    TextView download;
    View view;
    Button paste;
    EditText editText;
    RadioButton radioImage, radioVideo;
    LinearLayout linearLayout;
    ImageView imageView;
    public static String murl="";

    public InstFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_inst, container, false);
        init();
        loadad();
        paste.setOnClickListener(v -> {
            editText.setText("");
            android.content.ClipboardManager clipboard=(android.content.ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            //since the clipboard contains plain text.
            ClipData.Item item=clipboard.getPrimaryClip().getItemAt(0);

            // Gets the clipboard as text.
            editText.setText(item.getText().toString());
        });
        fetch.setOnClickListener(view -> {
            if (editText.getText().toString().length() > 12) {
                if (radioImage.isChecked() || radioVideo.isChecked()) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (editText.getText().toString().contains("?")) {
                        murl=editText.getText().toString() + "&__a=1";
                    } else {
                        murl=editText.getText().toString() + "?__a=1";
                    }
                    ApiRequest.getImageAndVid((success, image, video) -> {
                        if (success) {
                            linearLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Picasso.get().load(image).placeholder(R.drawable.ic_baseline_image_24).into(imageView);
                            if (video!=null){
                                download.setText("Download Video");
                            }
                            download.setOnClickListener(view1 -> {
                                Intent intent=new Intent(getActivity(), DownloadActivity.class);
                                if (video == null) {
                                    intent.putExtra(INTENTDOWNLINK, image);
                                } else {
                                    intent.putExtra(INTENTDOWNLINK, video);
                                }
                                startActivity(intent);
                            });
                            imageView.setOnClickListener(view12 -> {
                                PhotoFullPopupWindow popupWindow=new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, imageView, image, null);
                                popupWindow.download.setVisibility(View.GONE);
                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            ToastMy.errorToast(getActivity(), "Sorry! Item is not public.", ToastMy.LENGTH_LONG);
                        }
                    });
                } else {
                    ToastMy.errorToast(getActivity(), PLEASE_SELECT_TYPE, ToastMy.LENGTH_SHORT);
                }
            } else {
                ToastMy.errorToast(getActivity(), INVALID_URL, ToastMy.LENGTH_SHORT);
            }

        });
        return view;
    }

    private void loadad() {
        AdView adView=new AdView(getActivity());

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(Cvalues.banr);
        adView=view.findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void init() {
        progressBar=view.findViewById(R.id.instprogress);
        fetch=view.findViewById(R.id.instget);
        paste=view.findViewById(R.id.inspastedata);
        editText=view.findViewById(R.id.ins_edit);
        radioVideo=view.findViewById(R.id.radiovid);
        radioImage=view.findViewById(R.id.radioimage);
        linearLayout=view.findViewById(R.id.resultl);
        download=view.findViewById(R.id.downloadImage);
        imageView=view.findViewById(R.id.imageRes);
    }

    public static class ApiRequest {

        public static final String GRAPHQL="graphql";
        public static final String SHORTCODE_MEDIA="shortcode_media";
        public static final String DISPLAY_URL="display_url";
        public static final String IS_VIDEO="is_video";
        public static final String VIDEO_URL="video_url";

        interface Apicallvid_img {
            void onResponse(boolean success, String image, String video);
        }

        interface ApicallAlbum {
            void onResponse(boolean success, String image, String video);
        }

        public static void getImageAndVid(Apicallvid_img myapicallback) {
            MyRetrofit myRetrofit=MyRetrofit.retrofit.create(MyRetrofit.class);
            myRetrofit.getPost(murl).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        if (response.code() == 200) {
                            String image=response.body().getAsJsonObject(GRAPHQL).getAsJsonObject(SHORTCODE_MEDIA).get(DISPLAY_URL).getAsString();
                            boolean isVideo=response.body().getAsJsonObject(GRAPHQL).getAsJsonObject(SHORTCODE_MEDIA).get(IS_VIDEO).getAsBoolean();
                            if (isVideo) {
                                String video=response.body().getAsJsonObject(GRAPHQL).getAsJsonObject(SHORTCODE_MEDIA).get(VIDEO_URL).getAsString();
                                myapicallback.onResponse(true, image, video);
                            } else {
                                myapicallback.onResponse(true, image, null);
                            }

                        } else {
                            myapicallback.onResponse(false, null, null);
                        }
                    } catch (Exception e) {
                        myapicallback.onResponse(false, null, null);
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    myapicallback.onResponse(false, null, null);
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        murl=null;
    }
}