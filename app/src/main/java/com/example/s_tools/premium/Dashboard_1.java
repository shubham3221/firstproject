package com.example.s_tools.premium;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.MyWebService;
import com.example.s_tools.R;
import com.example.s_tools.TinyDB;
import com.example.s_tools.premium.model.Category;
import com.example.s_tools.premium.model.Post;
import com.example.s_tools.premium.model.PremiumModel;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
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


public class Dashboard_1 extends AppCompatActivity {
    public static final String LIST="list";
    public static final String UPDATED="❤ Updated";
    public static final String POSTID="postid";
    private RecyclerView recyclerView;
//    CustomAdapterDashboard customAdapter;
    List<Post> list;
    Toolbar toolbar;
    MyWebService myWebService;
    Fragment fragment;
    SharedPreferences preferences;
    Type type;
    Gson gson;
    KProgressHUD kProgressHUD;
    TinyDB tinyDB;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_1);
        init();
        kProgressHUD=KProgressHUD.create(Dashboard_1.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(Cvalues.PLEASE_WAIT).setAutoDismiss(true)
//                            .setDetailsLabel("Downloading data")
                .setCancellable(true).show();
        new MyAsyncTask().execute();
    }

    private void init() {
        recyclerView=findViewById(R.id.recyclerView);
        toolbar=(Toolbar) findViewById(R.id.mytoolbar);
//        linearLayout=findViewById(R.id.premiumbannerView);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle(Cvalues.PLEASE_WAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ChangeStatusBarColor.changeStatusbarColor(Dashboard_1.this,R.color.statusbarcolor_premium);

        //if (!CheckRootAccess.isRootedDevice(this)){
        // }
    }


    public void getposts() {
        Dialing.getData(Dashboard_1.this,1,15, (success, body) -> {
            if (success) {
                list.addAll(body.getPosts());
                tinyDB.putInt(POSTID, list.get(0).getId());
                Gson gson=new Gson();
                String json=gson.toJson(list);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString(LIST, json).apply();
//                customAdapter.notifyDataSetChanged();
                toolbar.setSubtitle(UPDATED);
                kProgressHUD.dismiss();
            } else {
                kProgressHUD.setDetailsLabel(Cvalues.OFFLINE);
                kProgressHUD.dismiss();
                toolbar.setSubtitle(Cvalues.SERVER_OFFLINE);
            }

        });
    }
    private class MyAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list=new ArrayList<>();
            tinyDB=new TinyDB(Dashboard_1.this);
        }

        @Override
        protected String doInBackground(String... strings) {
                preferences=new SecurePreferences(Dashboard_1.this, MySharedPref.activity, MySharedPref.PostFilename);
                type=new TypeToken<List<Post>>() {}.getType();
                gson=new Gson();
//                customAdapter=new CustomAdapterDashboard(list, Dashboard_1.this);
                if (preferences.contains(LIST)) {
                    list.addAll(gson.fromJson(preferences.getString(LIST, null), type));
                    publishProgress("dismiss");
                    UpdatePosts();
                } else {
                    publishProgress();
                    getposts();
                }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values!=null){
                kProgressHUD.dismiss();
            }
            super.onProgressUpdate(values);
            recyclerView.setLayoutManager(new LinearLayoutManager(Dashboard_1.this));
//            recyclerView.setAdapter(customAdapter);
            toolbar.setSubtitle(Cvalues.UPDATING);
        }

    }

    private void UpdatePosts() {
        Dialing.getData(Dashboard_1.this,1,15, (success, body) -> {
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
                    }else {
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
//                        customAdapter.notifyDataSetChanged();
                        toolbar.setSubtitle(UPDATED);
                    });
                }).start();
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
                    Toast.makeText(Dashboard_1.this, Cvalues.INCORRECT_FORMAT, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Dashboard_1.this, Cvalues.NO_RESULT_FOUND, Toast.LENGTH_SHORT).show();
                            }
//                            customAdapter.notifyDataSetChanged();
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
//                customAdapter.getFilter().filter(newText);
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

//    class MyUnityadsBanner implements BannerView.IListener {
//        @Override
//        public void onBannerLoaded(BannerView bannerView) {
//            linearLayout.removeAllViews();
//            linearLayout.addView(bannerView);
//        }
//
//        @Override
//        public void onBannerClick(BannerView bannerView) {
//
//        }
//
//        @Override
//        public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
//            Log.e(Cvalues.TAG, "onBannerFailedToLoad: " + bannerErrorInfo.errorMessage);
//        }
//
//        @Override
//        public void onBannerLeftApplication(BannerView bannerView) {
//            bannerView.destroy();
//        }
//    }

}