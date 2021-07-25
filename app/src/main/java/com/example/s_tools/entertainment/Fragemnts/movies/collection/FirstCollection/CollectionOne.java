package com.example.s_tools.entertainment.Fragemnts.movies.collection.FirstCollection;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
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


public class CollectionOne extends AppCompatActivity implements MovieItemClickListner {
    private static final String TAG="mtag";
    RecyclerView recyclerView;
    CollectionOneRecyclerAdapter recyclerAdapter;
    List<MoviesModel> list;
    Toolbar toolbar;
    MenuItem item;
    AppBarLayout appBarLayout;
    ProgressBar progressBar;
    private boolean issearching = false;
    private int page = 2;
    int searchPage = 1;
    String searchQuery="";
//    BannerView bannerView;
    LinearLayout linearLayout;
    int categoryPosts;
    boolean isSpinnerPosts = false;
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
//        UnityAds.initialize(CollectionOne.this, VideoActivity.U_GAME_ID, VideoActivity.MODE);
        linearLayout = findViewById(R.id.bannerView_collection_one);
        new MyAsyncTask().execute();
        spinner();
        getSupportActionBar().setTitle("All");
    }
    private void getPosts(int page) {
        progressBar.setVisibility(View.VISIBLE);
        MyWebMovies services=MyWebMovies.South.create(MyWebMovies.class);
        services.FirstPost(40, page).enqueue(new Callback<List<MoviesModel>>() {
            @Override
            public void onResponse(Call<List<MoviesModel>> call, Response<List<MoviesModel>> response) {
                try {
                        for (MoviesModel m : response.body()) {
                            m.setMainimg(JsoupParser.parseMainImg(m.getContent().getRendered()));
                            recyclerAdapter.col_one_list.add(m);
                            runOnUiThread(() -> {
                                recyclerAdapter.notifyDataSetChanged();
                            });
                        }
                    loading = true;
                    runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                        });


                }catch (Exception e){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CollectionOne.this, "Server Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<MoviesModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                if (retrying == 5){
                    retrying = 0;
                    if (acitivityRunning){
                        Dialog dialog=new Dialog(CollectionOne.this);
                        dialog.setContentView(R.layout.movietimeout);
                        Button button=dialog.findViewById(R.id.dismissbtn);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                        dialog.show();
                        button.setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                    }
                    return;
                }
                getPosts(1);
                retrying++;
            }
        });
    }
    private void getSpinnerPost(int category,int page) {
//        bannerView.load();
        progressBar.setVisibility(View.VISIBLE);
        MoviesApiCalls.getSpinnerPosts(CollectionOne.this, category, page, (success, moviesModelList) -> {
            new Thread(() -> {
                for (MoviesModel m : moviesModelList) {
                    m.setMainimg(JsoupParser.parseMainImg(m.getContent().getRendered()));
                    recyclerAdapter.col_one_list.add(m);
                }
                loading = true;
                runOnUiThread(() -> {
                    recyclerAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                });
            }).start();
        });
    }

    private void spinner() {
        Spinner spinOrderType = (Spinner) findViewById(R.id.spinner_order_type);
        spinOrderType.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<String> categories = new ArrayList<String>();
            categories.add("Action");
            categories.add("Adult");
            categories.add("Adventure");
            categories.add("Animation");
            categories.add("Bollywood");
            categories.add("Dual Audio");
            categories.add("Hindi");
            categories.add("Hindi dubbed");
            categories.add("Hollywood");
            categories.add("Horror");
            categories.add("imdb top 250");
            categories.add("Punjabi");
            categories.add("Reality shows");
            categories.add("Sci-fi");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CollectionOne.this,
                    R.layout.spinner_layout, categories);
            runOnUiThread(() -> {
                dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
                spinOrderType.setAdapter(dataAdapter);
                spinOrderType.setSelected(false);
            });
        }).start();
        spinOrderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (recyclerAdapter.col_one_list.isEmpty()){
                    return;
                }
                recyclerAdapter.col_one_list.clear();
                page=1;
                isSpinnerPosts = true;
                String item = parent.getItemAtPosition(position).toString();
                switch (position){
                    case 0:
                        categoryPosts= CollectionConstants.ActionCat;
                        getSpinnerPost(CollectionConstants.ActionCat,page);
                        break;
                    case 1:
                        categoryPosts= CollectionConstants.adultcat;
                        getSpinnerPost(CollectionConstants.adultcat,page);
                        getSupportActionBar().setTitle(item);
                        break;
                    case 2:
                        getSupportActionBar().setTitle(item);
                        categoryPosts= CollectionConstants.adventurecat;
                        getSpinnerPost(CollectionConstants.adventurecat,page);
                        break;
                    case 3:
                        getSupportActionBar().setTitle(item);
                        categoryPosts= CollectionConstants.animationCat;
                        getSpinnerPost(CollectionConstants.animationCat,page);
                        break;
                    case 4:
                        getSupportActionBar().setTitle(item);
                        categoryPosts= CollectionConstants.bollywoodcat;
                        getSpinnerPost(CollectionConstants.bollywoodcat,page);
                        break;
                    case 5:
                        getSupportActionBar().setTitle(item);
                        categoryPosts= CollectionConstants.dualaudiocat;
                        getSpinnerPost(CollectionConstants.dualaudiocat,page);
                        break;
                    case 6:
                        getSupportActionBar().setTitle(item);
                        categoryPosts= CollectionConstants.hindicat;
                        getSpinnerPost(CollectionConstants.hindicat,page);
                        break;
                    case 7:
                        getSupportActionBar().setTitle(item);
                        categoryPosts= CollectionConstants.hindidubbedcat;
                        getSpinnerPost(CollectionConstants.hindidubbedcat,page);
                        break;
                    case 8:
                        getSupportActionBar().setTitle(item);
                        categoryPosts= CollectionConstants.hollywoodcat;
                        getSpinnerPost(CollectionConstants.hollywoodcat,page);
                        break;
                    case 9:
                        getSupportActionBar().setTitle(item);
                        categoryPosts= CollectionConstants.horrorcat;
                        getSpinnerPost(CollectionConstants.horrorcat,page);
                        break;
                    case 10:
                        getSupportActionBar().setTitle(item);
                        categoryPosts= CollectionConstants.imdbtop50cat;
                        getSpinnerPost(CollectionConstants.imdbtop50cat,page);
                        break;
                    case 11:
                        getSupportActionBar().setTitle(item);
                        categoryPosts= CollectionConstants.punjabicat;
                        getSpinnerPost(CollectionConstants.punjabicat,page);
                        break;
                    case 12:
                        getSupportActionBar().setTitle(item);
                        categoryPosts= CollectionConstants.realityshowcat;
                        getSpinnerPost(CollectionConstants.realityshowcat,page);
                        break;
                    case 13:
                        getSupportActionBar().setTitle(item);
                        categoryPosts= CollectionConstants.scificat;
                        getSpinnerPost(CollectionConstants.scificat,page);
                        break;
                    default:
                        Toast.makeText(CollectionOne.this, ".", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void init() {
        recyclerView = findViewById(R.id.recyclerView_collection_one);
        toolbar = (Toolbar)findViewById(R.id.mytoolbar_collection_one);
        progressBar =  findViewById(R.id.coll_1_progressbar_movieslist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All");
        appBarLayout = (AppBarLayout) findViewById(R.id.mappbarlayout);
        appBarLayout = new AppBarLayout(CollectionOne.this);
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
                    Toast.makeText(CollectionOne.this, "Too Short.", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "searchng...", Toast.LENGTH_LONG).show();
        MoviesApiCalls.SearchIt(CollectionOne.this, mquery,1,mpage, (success, modelList, isComplete) -> {
            if (success){
                new Thread(() -> {
                    modelList.setMainimg(JsoupParser.parseMainImg(modelList.getContent().getRendered()));
                    recyclerAdapter.col_one_list.add(modelList);
                    runOnUiThread(() -> {
                        recyclerAdapter.notifyDataSetChanged();
                    });
                }).start();
                if (isComplete){
                    loading = true;
                }

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
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CollectionOne.this
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
            recyclerAdapter = new CollectionOneRecyclerAdapter(CollectionOne.this, list, CollectionOne.this);
            publishProgress();
            getPosts(1);
//            bannerView = new BannerView(CollectionOne.this,Placementsid.banner, UnityBannerSize.getDynamicSize(CollectionOne.this));
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            manager = new GridLayoutManager(CollectionOne.this,3);
            recyclerView.setItemAnimator(null);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(recyclerAdapter);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            bannerView.setListener(banner);
//            bannerView.load();

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
                            if (isSpinnerPosts){
                                getSpinnerPost(categoryPosts,page);
                                page++;
//                                bannerView.load();
                                return;
                            }
                            getPosts(page);
                            page+=1;
//                            bannerView.load();
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
//        bannerView.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (bannerView != null)
//        bannerView.load();
    }

}