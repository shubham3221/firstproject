package com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel;

import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.SectionsFragment;
import com.example.s_tools.tools.retrofitcalls.InterceptorRetrofit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;

import okhttp3.CertificatePinner;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyWebMovies {
    String mainUrl = "https://katmoviehd.nu/wp-json/wp/v2/posts/";
    String allPost = "categories=7&per_page=10&page=1";


    ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256).build();
    String PATTERN= "drakeet.me";
    String PINS="sha256/gGOcYKAwzEaUfun6YdxZvFSQq/x2lF/R8UizDFofveY=";
    String SOUTH = "https://southfreak.casa/wp-json/wp/v2/";
    String MRush = SectionsFragment.movieurl;

    String MOVIESTIMES = "https://movies4time.com/wp-json/wp/v2/";
    OkHttpClient client = new OkHttpClient.Builder().connectionSpecs(Collections.singletonList(spec))
            .certificatePinner(new CertificatePinner.Builder().add(PATTERN, PINS).build()).build();
    Gson gson = new GsonBuilder().setLenient().create();

    //interceptor
    //Creating retrofit Object
    Retrofit South=new Retrofit.Builder().baseUrl(SOUTH).client(InterceptorRetrofit.getInterceptor()).addConverterFactory(GsonConverterFactory.create()).build();
    Retrofit Movierush=new Retrofit.Builder().baseUrl(MRush).client(InterceptorRetrofit.getInterceptor()).addConverterFactory(GsonConverterFactory.create()).build();
    Retrofit MoviesTime=new Retrofit.Builder().baseUrl(MOVIESTIMES).client(InterceptorRetrofit.getInterceptor()).addConverterFactory(GsonConverterFactory.create()).build();
    String POSTS="posts/";

    //    @GET("posts/")
//    Call<List<MovieSvrOneModel>> getOneMovies(@Query("per_page") int perpage, @Query("page") int pageNumber);
    @GET("posts/")
    Call<List<MoviesModel>> collectionOneMovies(@Query("categories") int categories
            , @Query("per_page") int perpage, @Query("page") int pageNumber);
    @GET("posts/")
    Call<List<MoviesModel>> FirstPost(@Query("per_page") int perPage,
                                      @Query("page") int page);
    @GET("posts/")
    Call<List<MoviesModel>> spinnerPosts(@Query("categories") int categories,@Query("per_page") int perPage,
                                      @Query("page") int page);

    @GET("search/")
    Call<List<SearchApiModel>> searchResult(@Query("search") String searchKeyword,@Query("per_page") int perPage,@Query("page") int page);


    @GET("posts/{id}")
    Call<MoviesModel> searchMovieList(@Path("id") int id);



}
