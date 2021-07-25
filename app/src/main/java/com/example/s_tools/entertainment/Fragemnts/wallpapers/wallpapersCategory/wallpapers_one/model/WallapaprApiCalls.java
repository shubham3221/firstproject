package com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model;

import android.content.Context;
import android.widget.Toast;

import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_two.Wallpapers_2_Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WallapaprApiCalls {
    public interface ApiCallback {
        void onResponse(boolean success, List<WallpaperOneModel> previews);
    }
    public interface ApiCallback2 {
        void onResponse(boolean success,List<Wallpapers_2_Model> models);
    }
    public static void wall1Api(Context context,int category,int page,ApiCallback callback){
        WallpapersApi api = WallpapersApi.retrofitWall1.create(WallpapersApi.class);
        api.wall1apicall(category,page).enqueue(new Callback<WallpapersModel>() {
            @Override
            public void onResponse(Call<WallpapersModel> call, Response<WallpapersModel> response) {
                if (response.body() != null){
                    callback.onResponse(true,response.body().previews);
                }else {
                    Toast.makeText(context, "No Data Received", Toast.LENGTH_SHORT).show();
                    callback.onResponse(false,null);
                }

            }

            @Override
            public void onFailure(Call<WallpapersModel> call, Throwable t) {
                callback.onResponse(false,null);
                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void wall2api(Context context,String type,int offset,ApiCallback2 callback){
        WallpapersApi api = WallpapersApi.retrofitWall2.create(WallpapersApi.class);
        api.fetchCategoryWalls(type,offset).enqueue(new Callback<List<Wallpapers_2_Model>>() {
            @Override
            public void onResponse(Call<List<Wallpapers_2_Model>> call, Response<List<Wallpapers_2_Model>> response) {
                if (response.body() != null){
                    callback.onResponse(true,response.body());
                }else {
                    Toast.makeText(context, "No Data Received", Toast.LENGTH_SHORT).show();
                    callback.onResponse(false,null);
                }
            }

            @Override
            public void onFailure(Call<List<Wallpapers_2_Model>> call, Throwable t) {
                callback.onResponse(false,null);
                Toast.makeText(context, "Server Error.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void wall2FetchPopularpost(Context context,int offset,ApiCallback2 callback){
        WallpapersApi api = WallpapersApi.retrofitWall2.create(WallpapersApi.class);
        api.fetchPopular(offset).enqueue(new Callback<List<Wallpapers_2_Model>>() {
            @Override
            public void onResponse(Call<List<Wallpapers_2_Model>> call, Response<List<Wallpapers_2_Model>> response) {
                if (response.body() != null){
                    callback.onResponse(true,response.body());
                }else {
                    Toast.makeText(context, "No Data Received", Toast.LENGTH_SHORT).show();
                    callback.onResponse(false,null);
                }
            }

            @Override
            public void onFailure(Call<List<Wallpapers_2_Model>> call, Throwable t) {
                callback.onResponse(false,null);
                Toast.makeText(context, "Server Error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
