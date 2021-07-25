package com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallbacks {

    public static final String TAG = "mtag";

    public interface ApiCallback {
        void onResponse(boolean success,List<MoviesModel> modelList);
    }
    public static void south(Context context,int perPage,int pageNumber, ApiCallback apiCallback){
        MyWebMovies serverone = MyWebMovies.South.create(MyWebMovies.class);
        serverone.FirstPost(perPage,pageNumber).enqueue(new Callback<List<MoviesModel>>() {
            @Override
            public void onResponse(Call<List<MoviesModel>> call, Response<List<MoviesModel>> response) {
                if (response.body()!=null){
                    apiCallback.onResponse(true,response.body());
                }else {
                    apiCallback.onResponse(false,null);
                    Toast.makeText(context, "no response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MoviesModel>> call, Throwable t) {
                Toast.makeText(context, "server failed to respond.", Toast.LENGTH_SHORT).show();
                apiCallback.onResponse(false,null);
            }
        });
    }
    public static void moviesrush(Context context,int perPage,int pageNumber, ApiCallback apiCallback){
        MyWebMovies serverone = MyWebMovies.Movierush.create(MyWebMovies.class);
        serverone.FirstPost(perPage,pageNumber).enqueue(new Callback<List<MoviesModel>>() {
            @Override
            public void onResponse(Call<List<MoviesModel>> call, Response<List<MoviesModel>> response) {
                if (response.body()!=null){
                    apiCallback.onResponse(true,response.body());
                }else {
                    apiCallback.onResponse(false,null);
                    Toast.makeText(context, "no response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MoviesModel>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage() );
                Toast.makeText(context, "server failed to respond.", Toast.LENGTH_SHORT).show();
                apiCallback.onResponse(false,null);
            }
        });
    }
}
