package com.example.s_tools.entertainment.Fragemnts.movies.collection.secondMovie.thirdCollection;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.TextView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.MoviesModel;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.MyWebMovies;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCalls {

    public static final String TIMEOUT="timeout";
    static int i=0;

    public interface ApiCallback {
        void onResponse(boolean success,List<MoviesModel> modelList);
    }

    public static void getPostsMovie4(Context context, int page, ApiCallback apiCallback){
        MyWebMovies services=MyWebMovies.MoviesTime.create(MyWebMovies.class);
        services.FirstPost(20, page).enqueue(new Callback<List<MoviesModel>>() {
            @Override
            public void onResponse(Call<List<MoviesModel>> call, Response<List<MoviesModel>> response) {
                try {
                    if (response.body()!=null){
                        apiCallback.onResponse(true,response.body());
                    }

                }catch (Exception e){
                    apiCallback.onResponse(false,null);
                }

            }

            @Override
            public void onFailure(Call<List<MoviesModel>> call, Throwable t) {
                    Dialog dialog=new Dialog(context);
                    dialog.setContentView(R.layout.movietimeout);
                    Button button=dialog.findViewById(R.id.dismissbtn);
                    TextView textView = dialog.findViewById(R.id.errorText);
                    textView.setText("Timeout..Use VPN to access this collection");
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                    dialog.show();
                    button.setOnClickListener(v -> {
                        dialog.dismiss();
                    });
                    apiCallback.onResponse(false,null);

            }
        });
    }
    public static void getPostsMovieRush(Context context, int page, ApiCallback apiCallback){
        MyWebMovies services=MyWebMovies.Movierush.create(MyWebMovies.class);
        services.FirstPost(20, page).enqueue(new Callback<List<MoviesModel>>() {
            @Override
            public void onResponse(Call<List<MoviesModel>> call, Response<List<MoviesModel>> response) {
                try {
                    if (response.body()!=null){
                        apiCallback.onResponse(true,response.body());
                    }

                }catch (Exception e){
                    apiCallback.onResponse(false,null);
                }

            }

            @Override
            public void onFailure(Call<List<MoviesModel>> call, Throwable t) {
                    Dialog dialog=new Dialog(context);
                    dialog.setContentView(R.layout.movietimeout);
                    Button button=dialog.findViewById(R.id.dismissbtn);
                    TextView textView = dialog.findViewById(R.id.errorText);
                    textView.setText(TIMEOUT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                    dialog.show();
                    button.setOnClickListener(v -> {
                        dialog.dismiss();
                    });
                    apiCallback.onResponse(false,null);


            }
        });
    }
}
