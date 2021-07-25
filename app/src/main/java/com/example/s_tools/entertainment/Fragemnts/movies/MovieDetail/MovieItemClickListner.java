package com.example.s_tools.entertainment.Fragemnts.movies.MovieDetail;

import android.widget.ImageView;

import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.MoviesModel;

import java.util.List;

public interface MovieItemClickListner {
    void onmovieClick(MoviesModel model, ImageView imageView);
    void onmovieClick(List<MoviesModel> modelList, ImageView imageView);
}
