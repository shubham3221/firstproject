package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader;

import com.example.s_tools.Splash_after_reg;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MainModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

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

public interface MyWebM {
    String FIELDS_CATEGORIES_MODIFIED="get_category_posts/?include=custom_fields,categories,date,title";
    String GET_PAGE="get_page/?include=custom_fields";
    String SEARCH="get_search_results/?include=custom_fields,categories,date,title";
    ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256).build();
    String PATTERN= "drakeet.me";
    String PINS="sha256/gGOcYKAwzEaUfun6YdxZvFSQq/x2lF/R8UizDFofveY=";

    OkHttpClient client = new OkHttpClient.Builder().connectionSpecs(Collections.singletonList(spec))
            .certificatePinner(new CertificatePinner.Builder().add(PATTERN, PINS).build()).build();
    Gson gson = new GsonBuilder().setLenient().create();

    Retrofit retrofit=new Retrofit.Builder().baseUrl(Splash_after_reg.weburl).client(client).addConverterFactory(GsonConverterFactory.create()).build();
    String COUNT="count";
    String PAGE="page";


    @GET(FIELDS_CATEGORIES_MODIFIED)
    Call<MainModel> fetchRecent(@Query("id") int category, @Query(COUNT) int perPage, @Query(PAGE) int page);



    @GET(SEARCH)
    Call<SModel> searchResult(@Query("search") String searchKeyword, @Query(COUNT) int perPage, @Query(PAGE) int page);

    @GET(GET_PAGE)
    Call<JsonObject> getPage(@Query("id") int id);
}
