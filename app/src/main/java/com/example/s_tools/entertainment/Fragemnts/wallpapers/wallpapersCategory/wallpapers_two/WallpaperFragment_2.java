package com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_two;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallapaprApiCalls;
import com.example.s_tools.tools.CustomScrollingIconChange;

import java.util.ArrayList;
import java.util.List;

import ru.alexbykov.nopaginate.paginate.NoPaginate;

public class WallpaperFragment_2 extends Fragment {
    public static final String TAG="mtag";
    public static final String CATEGORY="category";
    public static final String POPULAR="Popular";
    View view;
    RecyclerView recyclerView;
    private List<Wallpapers_2_Model> two;
    private Wall_Adapter2 adapter;
    private GridLayoutManager manager;
    private int offset = 0;
    private int popularoffset = 0;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    String type;
    NoPaginate build;



    public WallpaperFragment_2() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_wallpaper2, container, false);
        recyclerView = view.findViewById(R.id.fragment_recycler_wall2);
        type = getArguments().getString(CATEGORY);
        manager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(manager);

//        two = (List<Wallpapers_2_Model>)getArguments().getSerializable("two");
        new MyTask().execute(type);

        return view;
    }


    private void fetchNextPage(String type,int moffset) {
        WallapaprApiCalls.wall2api(getActivity(),type, moffset, (success, models) -> {
            if (success) {
                two.addAll(models);
                adapter.notifyDataSetChanged();
                offset+=30;
                loading = true;
            }else {
                build.setNoMoreItems(true);
            }
        });
    }
    private void fetchNextPopularPage(int mpopularoffset) {
        WallapaprApiCalls.wall2FetchPopularpost(getActivity(), mpopularoffset, (success, models) -> {
            if (success){
                two.addAll(models);
                adapter.notifyDataSetChanged();
                popularoffset+=30;
                loading = true;
            }
            else {
                build.setNoMoreItems(true);
            }
        });
    }
    private class MyTask extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            two = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... strings) {
            adapter = new Wall_Adapter2(getActivity(),two);
            publishProgress();
            if (type.contains(POPULAR)){
                WallapaprApiCalls.wall2FetchPopularpost(getActivity(), popularoffset, (success, models) -> {
                    if (success){
                        two.addAll(models);
                        popularoffset+=30;
                        adapter.notifyDataSetChanged();
                    }
                });
            }else {
                WallapaprApiCalls.wall2api(getActivity(),strings[0], 0, (success, models) -> {
                    if (success) {
                        two.addAll(models);
                        offset+=30;
                        adapter.notifyDataSetChanged();

                    }
                });
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            build=NoPaginate.with(recyclerView).setOnLoadMoreListener(() -> {
                if (loading) {
                    loading=false;
                    if (type.contains(POPULAR)){
                        fetchNextPopularPage(popularoffset);
                    }else {
                        fetchNextPage(type,offset);
                    }
                }
            }).setCustomLoadingItem(new CustomScrollingIconChange()).build();

//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
//                    if (dy > 0) {
//                        visibleItemCount = manager.getChildCount();
//                        totalItemCount = manager.getItemCount();
//                        pastVisiblesItems = manager.findFirstVisibleItemPosition();
//
//                        if (loading) {
//                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                                loading = false;
//                                if (type.contains(POPULAR)){
//                                    fetchNextPopularPage(popularoffset);
//                                }else {
//                                    fetchNextPage(type,offset);
//                                }
//                            }
//                        }
//                    }
//                }
//            });
        }
    }
}