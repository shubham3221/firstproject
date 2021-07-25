package com.example.s_tools.entertainment.Fragemnts.movies.collection;

import android.content.Context;
import android.widget.Toast;

import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.MoviesModel;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.MyWebMovies;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.SearchApiModel;
import com.example.s_tools.tools.ToastMy;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesApiCalls {

    private static int retrying=0;

    public interface ApiCallback {
        void onResponse(boolean success, List<MoviesModel> moviesModelList);
    }

    public interface ApiCallbackSerarch {
        void onResponse(boolean success, MoviesModel moviesModelList, boolean isComplete);
    }

    public static void getFirstPosts(Context context, int page, ApiCallback apiCallback) {
        MyWebMovies services=MyWebMovies.South.create(MyWebMovies.class);
        services.FirstPost(40, page).enqueue(new Callback<List<MoviesModel>>() {
            @Override
            public void onResponse(Call<List<MoviesModel>> call, Response<List<MoviesModel>> response) {
                try {
                    if (response.body() != null) {
                        apiCallback.onResponse(true, response.body());
                    }

                } catch (Exception e) {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<MoviesModel>> call, Throwable t) {
                if (retrying == 10) {
                    return;
                }
                getFirstPosts(context, page, apiCallback);
                Toast.makeText(context, "---Server Failed---\n  Retrying: " + retrying + "/10", Toast.LENGTH_SHORT).show();
                retrying++;
            }
        });
    }

    public static void getSpinnerPosts(Context context, int category, int page, ApiCallback apiCallback) {
        MyWebMovies services=MyWebMovies.South.create(MyWebMovies.class);
        services.spinnerPosts(category, 40, page).enqueue(new Callback<List<MoviesModel>>() {
            @Override
            public void onResponse(Call<List<MoviesModel>> call, Response<List<MoviesModel>> response) {
                try {
                    if (response.body() != null) {
                        apiCallback.onResponse(true, response.body());
                    }

                } catch (Exception e) {
                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<MoviesModel>> call, Throwable t) {
                Toast.makeText(context, "Please Retry", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void SearchIt(Context context, String searchKeyword, int collection, int page, ApiCallbackSerarch callback) {
        MyWebMovies moviesServices=null;
        if (collection == 1) {
            moviesServices=MyWebMovies.South.create(MyWebMovies.class);
        } else if (collection == 2) {
            moviesServices=MyWebMovies.Movierush.create(MyWebMovies.class);
        } else if (collection == 3) {
            moviesServices=MyWebMovies.MoviesTime.create(MyWebMovies.class);
        }
        MyWebMovies finalMoviesServices=moviesServices;
        moviesServices.searchResult(searchKeyword, 50, page).enqueue(new Callback<List<SearchApiModel>>() {
            @Override
            public void onResponse(Call<List<SearchApiModel>> call, Response<List<SearchApiModel>> response) {
                if (response.body() == null) {
                    Toast.makeText(context, "Error establishing a database connection", Toast.LENGTH_SHORT).show();
                    callback.onResponse(false, null, false);
                    return;
                } else if (response.body().isEmpty()) {
                    ToastMy.errorToast(context, "not found", ToastMy.LENGTH_SHORT);
                    callback.onResponse(false, null, true);
                    return;
                }
//                new Thread(() -> {
                List<SearchApiModel> responseData=new ArrayList<>();
                responseData.addAll(response.body());
                for (int i=0; i < responseData.size(); i++) {
                    finalMoviesServices.searchMovieList(responseData.get(i).getId()).enqueue(new Callback<MoviesModel>() {
                        @Override
                        public void onResponse(Call<MoviesModel> call, Response<MoviesModel> response) {
                            if (response.body() != null) {
                                callback.onResponse(true, response.body(), false);
                            }

                        }

                        @Override
                        public void onFailure(Call<MoviesModel> call, Throwable t) {
                            ToastMy.errorToast(context, t.getMessage(), ToastMy.LENGTH_SHORT);
                        }
                    });
                }
                callback.onResponse(false, null, true);
//                }).start();
            }

            @Override
            public void onFailure(Call<List<SearchApiModel>> call, Throwable t) {
                ToastMy.errorToast(context, "timeout", ToastMy.LENGTH_LONG);
                callback.onResponse(false, null, false);
            }
        });
    }
}
