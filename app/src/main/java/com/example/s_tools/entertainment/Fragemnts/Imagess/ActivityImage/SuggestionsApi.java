package com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage;

import com.example.s_tools.tools.retrofitcalls.InterceptorRetrofit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.Collections;

import okhttp3.CertificatePinner;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.s_tools.MyWebService.PINS;

interface SuggestionsApi {
    String core_url = "http://suggestqueries.google.com/complete/";
   // String core_url = "https://startpage.com/do/";
    ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256).build();
    String PATTERN= "drakeet.me";
    OkHttpClient client = new OkHttpClient.Builder().connectionSpecs(Collections.singletonList(spec))
            .certificatePinner(new CertificatePinner.Builder().add(PATTERN, PINS).build()).build();
    Gson gson = new GsonBuilder().setLenient().create();

    //interceptor
    //Creating retrofit Object
    Retrofit retrofit=new Retrofit.Builder().baseUrl(core_url).client(InterceptorRetrofit.getInterceptor()).addConverterFactory(GsonConverterFactory.create(gson)).build();

    @GET("search?client=firefox&hl=en")
    Call<JsonArray> getSuggestions(@Query("q") String query);

//    @GET("suggest?limit=5&lang=english&format=json")
//    Call<JsonArray> getSuggestions(@Query("query") String query);
}
