package com.example.s_tools.premium;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.MyWebService;
import com.example.s_tools.R;
import com.example.s_tools.Splash_after_reg;
import com.example.s_tools.TinyDB;
import com.example.s_tools.premium.model.Category;
import com.example.s_tools.premium.model.Post;
import com.example.s_tools.premium.model.PremiumModel;
import com.example.s_tools.tools.ChangeStatusBarColor;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.securepreferences.SecurePreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.alexbykov.nopaginate.paginate.NoPaginate;


public class Dashboard extends AppCompatActivity {
    public static final String LIST="list";
    public static final String UPDATED="❤Updated";
    public static final String POSTID="postid";
    private RecyclerView recyclerView;
    Adapter customAdapter;
    List<Post> list;
    Toolbar toolbar;
    SharedPreferences preferences;
    Type type;
    Gson gson;
    KProgressHUD kProgressHUD;
    TinyDB tinyDB;
    int i=0;
    private NoPaginate scrolling;
    private boolean loading = false;
    private int page = 2;
    private int totalItemCount;
    private int lastVisibleItem;
    NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.activity_dashboard_1);
        init();
        kProgressHUD=KProgressHUD.create(Dashboard.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(Cvalues.PLEASE_WAIT).setAutoDismiss(true)
//                            .setDetailsLabel("Downloading data")
                .setCancellable(true).show();
        setupAds();
        new MyAsyncTask().execute();
    }

    private void init() {
        scrollView = findViewById(R.id.nested);
        recyclerView=findViewById(R.id.recyclerView);
        toolbar=(Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(Cvalues.PLEASE_WAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        ChangeStatusBarColor.changeStatusbarColor(Dashboard.this, R.color.colorPrimaryDark);

        //if (!CheckRootAccess.isRootedDevice(this)){
        // }
    }


    public void getposts() {
        Dialing.getData(Dashboard.this,1,15, (success, body) -> {
            if (success) {
                list.addAll(body.getPosts());
                tinyDB.putInt(POSTID, list.get(0).getId());
                Gson gson=new Gson();
                String json=gson.toJson(list);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString(LIST, json).apply();
                customAdapter.notifyDataSetChanged();
                toolbar.setSubtitle(UPDATED);
                kProgressHUD.dismiss();
            } else {
                kProgressHUD.setDetailsLabel(Cvalues.OFFLINE);
                kProgressHUD.dismiss();
                toolbar.setSubtitle(Cvalues.OFFLINE);
            }

        });
    }

    private void setupAds() {
        new Thread(() -> {
            try {
                Thread.sleep(200);
                runOnUiThread(() -> {
                    AdView adView=new AdView(this);
                    adView.setAdSize(AdSize.BANNER);
                    adView.setAdUnitId(Cvalues.banr);
                    adView=findViewById(R.id.adView);
                    AdRequest adRequest=new AdRequest.Builder().build();
                    adView.loadAd(adRequest);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
    }


    private class MyAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list=new ArrayList<>();
            tinyDB=new TinyDB(Dashboard.this);
        }

        @Override
        protected String doInBackground(String... strings) {
            preferences=new SecurePreferences(Dashboard.this, MySharedPref.activity, MySharedPref.PostFilename);
            type=new TypeToken<List<Post>>() {}.getType();
            gson=new Gson();
            if (MySharedPref.isVersionOut(Dashboard.this)){
                ToastMy.errorToast(Dashboard.this,"New Update Found! Please Restart App",ToastMy.LENGTH_LONG);
            }else {
                if (preferences.contains(LIST)) {
                    list.addAll(gson.fromJson(preferences.getString(LIST, null), type));
                    publishProgress("dismiss");
                    UpdatePosts();
                } else {
                    publishProgress();
                    getposts();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (list.isEmpty()){
                customAdapter=new Adapter(list, Dashboard.this,0);
            }else {
                customAdapter=new Adapter(list, Dashboard.this,list.get(0).getId());
            }
            if (values!=null){
                kProgressHUD.dismiss();
            }
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard.this));
            recyclerView.setAdapter(customAdapter);
            toolbar.setSubtitle(Cvalues.UPDATING);


            scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);

                if ((view.getBottom() - (scrollView.getHeight() + scrollView
                        .getScrollY())) == 0) {
                    if (!loading){
                        loading = true;
                        kProgressHUD.setLabel("Loading more posts");
                        kProgressHUD.show();
                        fetchPosts(page);
                    }

                }
            });
        }

        private void fetchPosts(int mpage) {
            Dialing.getData(Dashboard.this,mpage,15, (success, body) -> {
                if (success) {
                    kProgressHUD.dismiss();
                    list.addAll(body.getPosts());
                    customAdapter.notifyDataSetChanged();
                    page++;
                    loading = false;
                } else {
                    kProgressHUD.setLabel(Cvalues.ERROR);
                    kProgressHUD.dismiss();
                    toolbar.setSubtitle(Cvalues.OFFLINE);
                }
            });
        }

    }

    private void UpdatePosts() {
        Dialing.getData(Dashboard.this,1,15, (success, body) -> {
            if (success) {
                new Thread(() -> {
                    List<Post> newList=new ArrayList<>(body.getPosts());
                    tinyDB.putInt(POSTID, newList.get(0).getId());

                    if (newList.get(0).getId() != list.get(0).getId()) {
                        Gson gson=new Gson();
                        String json=gson.toJson(newList);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString(LIST, json).apply();
                        list.clear();
                        list.addAll(newList);
                    }
                    else {
                        for (int i=0; i<newList.size(); i++){
                            if (!list.get(i).getModified().equals(newList.get(i).getModified())){
                                list.set(i,newList.get(i));
                            }
                        }
                        Gson gson=new Gson();
                        String json=gson.toJson(newList);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString(LIST, json).apply();
                    }
                    runOnUiThread(() -> {
                        customAdapter.yes = false;
                        customAdapter.notifyDataSetChanged();
                        toolbar.setSubtitle(UPDATED);
                        ToastMy.successToast(Dashboard.this,"Updated",ToastMy.LENGTH_SHORT);
                    });
                }).start();
            }else {
                toolbar.setSubtitle(Cvalues.OFFLINE);
            }
        });
    }

    //optionmneu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.searchbarmenu, menu);
        MenuItem item=menu.findItem(R.id.searchview);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty() || query.length() < 2) {
                    Toast.makeText(Dashboard.this, Cvalues.INCORRECT_FORMAT, Toast.LENGTH_SHORT).show();
                } else {
                    kProgressHUD.show();
                    MyWebService myWebService=MyWebService.retrofit.create(MyWebService.class);
                    myWebService.searchPosts(query).enqueue(new Callback<PremiumModel>() {
                        @Override
                        public void onResponse(Call<PremiumModel> call, Response<PremiumModel> response) {
                            list.clear();
                            PremiumModel body=response.body();
                            for (Post post : body.getPosts()) {
                                if (post.getCategories() != null) {
                                    for (Category c : post.getCategories()) {
                                        if (c.getId() == 3) {
                                            list.add(post);
                                        }
                                    }
                                }

                            }
                            if (list.isEmpty()) {
                                Toast.makeText(Dashboard.this, Cvalues.NO_RESULT_FOUND, Toast.LENGTH_SHORT).show();
                            }
                            customAdapter.notifyDataSetChanged();
                            kProgressHUD.dismiss();
                        }

                        @Override
                        public void onFailure(Call<PremiumModel> call, Throwable t) {
                            kProgressHUD.dismiss();
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}