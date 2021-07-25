package com.example.s_tools.entertainment.Fragemnts.movies.collection.secondMovie;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.MovieDetail.MovieDetailActivity;
import com.example.s_tools.entertainment.Fragemnts.movies.MovieDetail.MovieItemClickListner;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.CollectionConstants;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.FirstCollection.CollectionOneRecyclerAdapter;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.MoviesApiCalls;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.MoviesModel;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.MyWebMovies;
import com.example.s_tools.tools.JsoupParser;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class oldCollectionTwo extends AppCompatActivity implements MovieItemClickListner {
    RecyclerView recyclerView;
    CollectionOneRecyclerAdapter recyclerAdapter;
    List<MoviesModel> list;
    Toolbar toolbar;
    MenuItem item;
    AppBarLayout appBarLayout;
    ProgressBar progressBar;
    private boolean issearching = false;
    int page = 2;
    int searchPage = 1;
    String searchQuery="";
    LinearLayout linearLayout;
    int retrying = 0;
    private boolean loading = true;
    private NestedScrollView scrollView;
    private boolean acitivityRunning = true;
    private SharedPreferences preferences;
    private Type type;
    private Gson gson;
    private GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_one);
        init();
        linearLayout = findViewById(R.id.bannerView_collection_one);
        new MyAsyncTask().execute();
    }


    private void getPosts(int page) {
        progressBar.setVisibility(View.VISIBLE);
        MyWebMovies services = MyWebMovies.Movierush.create(MyWebMovies.class);
        services.FirstPost(40,page).enqueue(new Callback<List<MoviesModel>>() {
            @Override
            public void onResponse(Call<List<MoviesModel>> call, Response<List<MoviesModel>> response) {
                try {
                    new Thread(() -> {
                        for (MoviesModel m : response.body()) {
                            m.setMainimg(JsoupParser.parseMainImg(m.getContent().getRendered()));
                            recyclerAdapter.col_one_list.add(m);
                        }
                        runOnUiThread(() -> {
                            recyclerAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            loading = true;
                        });
                    }).start();


                }catch (Exception e){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(oldCollectionTwo.this, "Server Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<MoviesModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                if (retrying == 5){
                    retrying = 0;
                    if (acitivityRunning){
                        AlertDialog.Builder builder = new AlertDialog.Builder(oldCollectionTwo.this);
                        builder.setTitle("Caution!!!");
                        builder.setMessage("Retried 5 times but server did not respond." +
                                ",Solution:- Please use VPN for access this collection");
                        builder.setNegativeButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                        builder.show();
                    }
                    return;
                }
                getPosts(1);
                retrying++;
            }
        });
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView_collection_one);
        toolbar = findViewById(R.id.mytoolbar_collection_one);
        progressBar =  findViewById(R.id.coll_1_progressbar_movieslist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All");
        appBarLayout = (AppBarLayout) findViewById(R.id.mappbarlayout);
        appBarLayout = new AppBarLayout(oldCollectionTwo.this);
        scrollView = findViewById(R.id.nestedscviww);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_catagory,menu);
        item = menu.findItem(R.id.searchview_c_1);
//        item2 = menu.findItem(R.id.category_col_1);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty() || query.length()<2){
                    Toast.makeText(oldCollectionTwo.this, "Too Short.", Toast.LENGTH_SHORT).show();
                }else {
                    searchPage = 1;
                    issearching = true;
                    searchQuery = query;
                    list.clear();
                    searchApi(query,searchPage);
                    searchPage++;
                    searchView.onActionViewCollapsed();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
    private void searchApi(String mquery,int mpage){
        Toast.makeText(this, "searching...", Toast.LENGTH_LONG).show();
        MoviesApiCalls.SearchIt(oldCollectionTwo.this, mquery, 2,mpage, (success, modelList, isComplete) -> {
            if (success){
                new Thread(() -> {
                    modelList.setMainimg(JsoupParser.parseMainImg(modelList.getContent().getRendered()));
                    recyclerAdapter.col_one_list.add(modelList);
                    runOnUiThread(() -> {
                        recyclerAdapter.notifyDataSetChanged();
                    });
                }).start();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onmovieClick(MoviesModel model, ImageView imageView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(CollectionConstants.MOVIE_NAME,model.getTitle().getRendered());
        intent.putExtra(CollectionConstants.MOVIE_CONTENT,model.getContent().getRendered());
        intent.putExtra(CollectionConstants.MAIN_IMAGE,model.getMainimg());
        intent.putExtra(CollectionConstants.COVER,model.getMainimg());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(oldCollectionTwo.this
                ,imageView, CollectionConstants.ELEMENT_NAME);
        startActivity(intent,options.toBundle());
    }

    @Override
    public void onmovieClick(List<MoviesModel> modelList, ImageView imageView) {

    }
    private class MyAsyncTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... strings) {
            getPosts(1);
            publishProgress();
            recyclerAdapter = new CollectionOneRecyclerAdapter(oldCollectionTwo.this, list, oldCollectionTwo.this);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            manager = new GridLayoutManager(oldCollectionTwo.this,3);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(recyclerAdapter);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                        .getScrollY()));

                if (diff == 0) {
                    if (loading) {
                        int visibleItemCount=manager.getChildCount();
                        int totalItemCount=manager.getItemCount();
                        int pastVisiblesItems = manager.findFirstVisibleItemPosition();
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            if (issearching){
                                searchApi(searchQuery,searchPage);
                                return;
                            }
                            getPosts(page);
                            page++;
                        }
                    }
                }
            });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        acitivityRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}