package com.example.s_tools;

import com.example.s_tools.Splash_login_reg.CheckCookieValid;
import com.example.s_tools.Splash_login_reg.LoginModel;
import com.example.s_tools.Splash_login_reg.Registration_Model;
import com.example.s_tools.entertainment.Fragemnts.movies.google_dict.Modelclass;
import com.example.s_tools.main_activity.mainactivity_toolbar_api.ChangeNameModel;
import com.example.s_tools.premium.model.PremiumModel;
import com.example.s_tools.tools.retrofitcalls.NonceModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.Map;

import okhttp3.CertificatePinner;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MyWebService {
    String REGISTRATION="user/register";
    String SEARCHIT="get_search_results/?exclude=comment_status,comment_count,title,tags,slug,comments,author,content,url,status,comments";
    String get_catagory_post="get_category_posts/?exclude=comment_status,comment_count,title,tags,slug,comments,author,content,url,status,categories,comments,attachment";
    String PINS="sha256/gGOcYKAwzEaUfun6YdxZvFSQq/x2lF/R8UizDFofveY=";
    String Premium="posts?per_page=100";
    String g="https://www.googleapis.com/drive/v3/";


    ConnectionSpec spec=new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256).build();
    String PATTERN="mbuddy.blogspot.com";
    OkHttpClient client=new OkHttpClient.Builder().connectionSpecs(Collections.singletonList(spec)).certificatePinner(new CertificatePinner.Builder().add(PATTERN, PINS).build()).build();
    Gson gson=new GsonBuilder().setLenient().create();

    //Creating retrofit Object
    Retrofit retrofit=new Retrofit.Builder().baseUrl(Splash_after_reg.weburl).
            client(NetworkHelper.InterLog.getIntercept()).addConverterFactory(GsonConverterFactory.create()).build();
    Retrofit google=new Retrofit.Builder().baseUrl(g).
            client(NetworkHelper.InterLog.getIntercept()).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
    String GET_NONCE="get_nonce";
    String PAGE="get_page/?id=237&exclude=type,slug,url,status,title,title_plain,date,modified,categories,tags,author,comments,attachments";
    String USER_META_VARS="user/update_user_meta_vars/";
    String GET_USER_META_VARS="user/get_user_meta/";
    String DELETE_META="user/delete_user_meta/";
    String ID="id";
    String COUNT="count";
    String METHOD="method";
    String CONTROLLER="controller";
    String SEARCH="search";
    String EMAIL="email";
    String PASSWORD="password";
    String SECONDS="seconds";
    String FIRST_NAME="first_name";
    String USERNAME="username";
    String USER_PASS="user_pass";
    String DESCRIPTION="description";
    String NONCE="nonce";
    String NOTIFY="notify";
    String USER_UPDATE_USER_META="user/update_user_meta";
    String COOKIE="cookie";
    String META_KEY="meta_key";
    String META_VALUE="meta_value";
    String USER_VALIDATE_AUTH_COOKIE="user/validate_auth_cookie";
    String TOKEN="token";
    String USER_GENERATE_AUTH_COOKIE="user/generate_auth_cookie";


    //main premium (id means catagory)
    @GET(get_catagory_post)
    Call<PremiumModel> getPremiumPost(@Query(ID) int id, @Query(COUNT) int count, @Query("page") int page);

    //getnonce
    @FormUrlEncoded
    @POST(GET_NONCE)
    Call<NonceModel> getnonce(@Field(CONTROLLER) String controller, @Field(METHOD) String method);


    //search
    @POST(SEARCHIT)
    Call<PremiumModel> searchPosts(@Query(SEARCH) String search);


    //login
    @FormUrlEncoded
    @POST(USER_GENERATE_AUTH_COOKIE)
    Call<LoginModel> loginApi(@Field(EMAIL) String email, @Field(PASSWORD) String password);
//    Call<LoginModel> loginApi(@Field(EMAIL) String email, @Field(PASSWORD) String password , @Field(SECONDS) int seconds);

    //register post
    @FormUrlEncoded
    @POST(REGISTRATION)
    Call<Registration_Model> userRegiseration(@Field(FIRST_NAME) String firstname, @Field(USERNAME) String username, @Field(EMAIL) String email, @Field(USER_PASS) String pass, @Field(DESCRIPTION) String description, @Field(NONCE) String nonce, @Field(SECONDS) int second, @Field(NOTIFY) String notify);

    //change name
    @FormUrlEncoded
    @POST(USER_UPDATE_USER_META)
    Call<ChangeNameModel> updateUserMeta(@Field(COOKIE) String cookie, @Field(META_KEY) String key, @Field(META_VALUE) String value);


    @POST(PAGE)
    Call<JsonObject> checkVersion();


    @FormUrlEncoded
    @POST(USER_VALIDATE_AUTH_COOKIE)
    Call<CheckCookieValid> isCookieValid(@Field(COOKIE) String cookie);

    @FormUrlEncoded
    @POST(USER_META_VARS)
    Call<JsonObject> updateUserMetaValue(@Field(COOKIE) String cookie, @Field(TOKEN) String value);

    @FormUrlEncoded
    @POST(GET_USER_META_VARS)
    Call<JsonObject> getUserMeta(@Field(COOKIE) String cookie);

    @FormUrlEncoded
    @POST(DELETE_META)
    Call<JsonObject> deleteMetaUser(@Field(COOKIE) String cookie, @Field(META_KEY) String key, @Field(META_VALUE) String value);


//    @GET("?q=%27{folderid}%27+in+parents&key={key}&pageSize=400")
//    Call<MyModel> getFiles(@Path("folderid") String folderid, @Path("key") String key);
//    @GET("files?q=1_xdvfNDAvlFB_5hnF0RPwz9H56u33PQv%20in%20parents&key=AIzaSyAqZV8HLqax1m5MOPK4Ix4mjvJh6k14DXI&supportsAllDrives=true&supportsTeamDrives=true&includeItemsFromAllDrives=true&fields=files(thumbnailLink,name,id,fileExtension,size,webContentLink,mimeType,createdTime,modifiedTime),nextPageToken&orderBy=modifiedTime,folder&pageSize=500")
//    Call<MyModel> getFiles();
    @GET("files?key=AIzaSyAqZV8HLqax1m5MOPK4Ix4mjvJh6k14DXI&supportsAllDrives=true&supportsTeamDrives=true&includeItemsFromAllDrives=true&fields=files(thumbnailLink,name,id,fileExtension,size,webContentLink,mimeType,createdTime,modifiedTime),nextPageToken&orderBy=folder,modifiedTime desc,name&pageSize=100")
    Call<Modelclass> getFiles(@Query("q") String q);

    @GET("files?key=AIzaSyAqZV8HLqax1m5MOPK4Ix4mjvJh6k14DXI&supportsAllDrives=true&supportsTeamDrives=true&includeItemsFromAllDrives=true&fields=files(thumbnailLink,name,id,fileExtension,size,webContentLink,mimeType,createdTime,modifiedTime),nextPageToken&orderBy=folder,modifiedTime desc,name&pageSize=100")
    Call<Modelclass> getNextPage(@Query("q") String q, @QueryMap Map<String, String> map);

    @GET("files?key=AIzaSyAqZV8HLqax1m5MOPK4Ix4mjvJh6k14DXI&supportsAllDrives=true&supportsTeamDrives=true&includeItemsFromAllDrives=true&fields=files(thumbnailLink,name,id,fileExtension,size,webContentLink,mimeType,createdTime,modifiedTime),nextPageToken&orderBy=folder,modifiedTime desc,name&pageSize=100")
    Call<Modelclass> getSearchGoogle(@Query("q") String q, @QueryMap Map<String, String> map);

}
