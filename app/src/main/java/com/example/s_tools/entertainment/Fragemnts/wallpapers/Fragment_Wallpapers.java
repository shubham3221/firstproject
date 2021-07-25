package com.example.s_tools.entertainment.Fragemnts.wallpapers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallapaprApiCalls;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallpaperOneModel;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallpapersApi;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallpapersModel;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_two.Wallpapers_2_Model;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.example.s_tools.tools.retrofitcalls.VC;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Wallpapers extends Fragment {
    ProgressBar progressBar;
    RecyclerView recyclerView;
    List<WallpaperOneModel> list;
    List<Wallpapers_2_Model> list2;
    Fragment_Wall_RecyclerAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    public static final String TAG="mtag";
    public static String wall1;
    public static String wall2;
    View view;

    public Fragment_Wallpapers() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_test_wallpapers, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.fragment_wall_recycler);
        refreshLayout=view.findViewById(R.id.wallpaper_pullToRefresh);
        progressBar=view.findViewById(R.id.fragment_wallpapers_progressbar);
        if (MySharedPref.getwall1(getActivity()) != null) {
            new MyAsyncTask().execute();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            ToastMy.successToast(getActivity(), Cvalues.PLEASE_WAIT,ToastMy.LENGTH_SHORT);
            VC.check(getActivity(), (success, updateinfo) -> {
                if (success) {
                    new MyAsyncTask().execute();
                }
            });
        }


        return view;
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            list=new ArrayList<>();
            list2=new ArrayList<>();
            adapter=new Fragment_Wall_RecyclerAdapter(getActivity(), list, list2);
            wall1=MySharedPref.getwall1(getActivity());
            wall2=MySharedPref.getwall2(getActivity());

            WallapaprApiCalls.wall1Api(getActivity(), 12, 1, (success, previews) -> {
                if (success) {
                    list.addAll(previews);
                    publishProgress();
                    progressBar.setVisibility(View.GONE);
                }
            });
            WallapaprApiCalls.wall2FetchPopularpost(getActivity(), 0, (success, models) -> {
                if (success) {
                    list2.addAll(models);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            });

            //refresh
            refreshLayout.setOnRefreshListener(() -> {
                if (!list.isEmpty()) {
                    list.clear();
                    WallpapersApi api2=WallpapersApi.retrofitWall1.create(WallpapersApi.class);
                    api2.wall1apicall(12, 1).enqueue(new Callback<WallpapersModel>() {
                        @Override
                        public void onResponse(Call<WallpapersModel> call, Response<WallpapersModel> response) {
                            if (response.body() != null) {
                                list.addAll(response.body().getPreviews());
                                adapter.notifyDataSetChanged();
                                refreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onFailure(Call<WallpapersModel> call, Throwable t) {
                            Toast.makeText(getActivity(), "Server Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
        }
    }
}