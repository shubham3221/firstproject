package com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallapaprApiCalls;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallpaperOneModel;
import com.example.s_tools.tools.CustomScrollingIconChange;
import com.example.s_tools.tools.Imageanimation;
import com.example.s_tools.tools.Overlayview;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.ArrayList;
import java.util.List;

import ru.alexbykov.nopaginate.paginate.NoPaginate;

public class WallpaperFragment_1 extends Fragment {
    public static final String CATEGORY="category";
    public static final String TITLE="title";
    public static final String TAG="//";
    View view;
    RecyclerView recyclerView;
    private List<WallpaperOneModel> one;
    private Wall_Adapter1 adapter;
    //    private GridLayoutManager manager;
    private GridLayoutManager manager;
    private int page=1;
    private boolean loading=false;
    int adapterPosition, category;
    private String title;
    NoPaginate build;
    public List<String> downloadUrls=new ArrayList<>();
    Imageanimation imageanimation;
    private Overlayview overlayview;
    private StfalconImageViewer<String> show;

    public WallpaperFragment_1() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_wallpaper1, container, false);
        recyclerView=view.findViewById(R.id.fragment_recycler_wall1);
        manager=new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(manager);
        imageanimation=new Imageanimation();

        adapterPosition=getArguments().getInt(CATEGORY);
        title=getArguments().getString(TITLE);
        switch (adapterPosition) {
            case 0:
                category=12;
                new MyTask().execute(category);
                break;
            case 1:
                category=34;
                new MyTask().execute(category);
                break;
            case 2:
                category=29;
                new MyTask().execute(category);
                break;
            case 3:
                category=56;
                new MyTask().execute(category);
                break;
            case 4:
                category=80;
                new MyTask().execute(category);
                break;
            case 5:
                category=30;
                new MyTask().execute(category);
                break;
            case 6:
                category=40;
                new MyTask().execute(category);
                break;
            case 7:
                category=32;
                new MyTask().execute(category);
                break;
            case 8:
                category=31;
                new MyTask().execute(category);
                break;
            case 9:
                category=99;
                new MyTask().execute(category);
                break;
            case 10:
                category=83;
                new MyTask().execute(category);
                break;
            case 11:
                category=171;
                new MyTask().execute(category);
                break;
            case 12:
                category=73;
                new MyTask().execute(category);
                break;
            default:
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    class MyTask extends AsyncTask<Integer, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            one=new ArrayList<>();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            adapter=new Wall_Adapter1(getActivity(), one);
            publishProgress();
            WallapaprApiCalls.wall1Api(getActivity(), integers[0], page, (success, previews) -> {
                if (success) {
                    one.addAll(previews);
                    adapter.notifyDataSetChanged();
                    page++;
                    loading=true;
                    addDownloadLinks(previews);
                } else {
                    if (build != null) {
                        build.setNoMoreItems(true);
                    }
                }
            });
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
                    fetchNextPage(category, page);
                }
            }).setCustomLoadingItem(new CustomScrollingIconChange()).build();

            RecycleClick.addTo(recyclerView).setOnItemClickListener((recyclerView, i, view) -> {
                imageanimation.showImage(getActivity(),downloadUrls,i,view.findViewById(R.id.search_imgview));
            });
        }
    }

    private void addDownloadLinks(List<WallpaperOneModel> previews) {
        for (WallpaperOneModel url : previews) {
            downloadUrls.add(url.getUrl());
        }
        imageanimation.updateImages(downloadUrls);
    }

    private void fetchNextPage(int wallCategory, int nextPage) {
        WallapaprApiCalls.wall1Api(getActivity(), wallCategory, nextPage, (success, previews) -> {
            if (success) {
                one.addAll(previews);
                adapter.notifyDataSetChanged();
                page++;
                loading=true;
                addDownloadLinks(previews);
            } else {
                build.setNoMoreItems(true);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (build != null) {
            build.unbind();
        }
    }
}