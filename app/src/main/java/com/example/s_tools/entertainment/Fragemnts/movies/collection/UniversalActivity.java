package com.example.s_tools.entertainment.Fragemnts.movies.collection;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.MovieDetail.MovieDetailActivity;
import com.example.s_tools.entertainment.Fragemnts.movies.MovieDetail.MovieItemClickListner;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.MoviesModel;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.secondMovie.SecondFragment;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.secondMovie.thirdCollection.ThirdFragment;

import java.util.List;

public class UniversalActivity extends AppCompatActivity implements MovieItemClickListner {
    public MovieItemClickListner listner;
    public static int collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_three);
        this.listner = this;

        collection=getIntent().getIntExtra("collection", 0);
        if (collection==2){
            SecondFragment someFragment=new SecondFragment(listner);
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout_c3, someFragment); // give your fragment container id in first parameter
            fragmentTransaction.commit();
        }else if (collection==3){
            ThirdFragment someFragment=new ThirdFragment(listner);
//        Bundle args=new Bundle();
////        args.putSerializable(TWO, (Serializable) two);
//        args.putString(CATEGORY,twonames[position]);
//        someFragment.setArguments(args);
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout_c3, someFragment); // give your fragment container id in first parameter
            fragmentTransaction.commit();

        }

    }

    @Override
    public void onmovieClick(MoviesModel model, ImageView imageView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(CollectionConstants.MOVIE_NAME,model.getTitle().getRendered());
        intent.putExtra(CollectionConstants.MOVIE_CONTENT,model.getContent().getRendered());
        intent.putExtra(CollectionConstants.MAIN_IMAGE,model.getMainimg());
        intent.putExtra(CollectionConstants.COVER,model.getMainimg());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(UniversalActivity.this
                ,imageView, CollectionConstants.ELEMENT_NAME);
        startActivity(intent,options.toBundle());
    }

    @Override
    public void onmovieClick(List<MoviesModel> modelList, ImageView imageView) {

    }

}