package com.example.s_tools.chatting;

import com.example.s_tools.JwtModel;
import com.example.s_tools.NetworkHelper;
import com.example.s_tools.chatting.messageModel.MessageModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyWebChat {
    String generateTokenUrl="https://hiddenstool.cu.ma/wp-json/jwt-auth/v1/";
    String messageURL="https://hiddenstool.cu.ma/wp-json/buddypress/v1/";
    String token="";


    Gson gson = new GsonBuilder().setLenient().create();


    //Creating retrofit Object
    Retrofit hwttoken=new Retrofit.Builder().baseUrl(generateTokenUrl).
            client(NetworkHelper.InterLog.getIntercept()).addConverterFactory(GsonConverterFactory.create()).build();

    Retrofit buddymsg=new Retrofit.Builder().baseUrl(messageURL).
            client(NetworkHelper.InterLog.getIntercept()).addConverterFactory(GsonConverterFactory.create()).build();



    @FormUrlEncoded
    @POST("token")
    Call<JwtModel> getToken(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("messages/")
    Call<List<MessageModel>> getFirstMessage(@Header("Authorization") String auth, @Field("recipients") int recep, @Field("message") String message
            );

    @FormUrlEncoded
    @POST("messages/")
    Call<List<MessageModel>> getThreadMessage(@Header("Authorization") String auth, @Field("recipients") int recep
            , @Field("message") String message, @Field("id") int id
            );

    @FormUrlEncoded
    @POST("messages")
    Call<List<MessageModel>> sendMessage(@Header("Authorization") String auth, @Field("recipients") int recep
            , @Field("message") String message
            );


    @GET("messages/{id}")
    Call<List<MessageModel>> getThread(@Header("Authorization") String auth, @Path("id") int id);

    @GET("messages/{id}")
    Call<Object> isChatidAvailable(@Header("Authorization") String auth, @Path("id") int id);

    @GET("messages/")
    Call<JsonArray> isHwtExpired(@Header("Authorization") String auth, @Query("user_id") int id);

}
