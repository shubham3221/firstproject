package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MoviesPosts;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies;
import com.example.s_tools.tools.CustomScrollingIconChange;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import ru.alexbykov.nopaginate.paginate.NoPaginate;

public class AllFragment extends Fragment {
    public static final String UNABLE_TO_LOAD="Unable To Load!";
    public static final String USING_SWIPE="Check Connection. Please Refresh Using Swipe!";
    public int PERPAGE=12;
    RecyclerView recyclerView;
    AllAdapter adapter;
    View view;
    NoPaginate scrolling;
    int category;
    List<MoviesPosts> list=new ArrayList<>();
    private boolean loading=false;
    private int page=1;
    private boolean isFirstTime=true;
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean detach=false;

    public AllFragment() {
    }


    public AllFragment(int id) {
        this.category=id;
        if (id==41){
            Cvalues.Category=41;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_all, container, false);
        recyclerView=view.findViewById(R.id.allrecy);
        swipeRefreshLayout=view.findViewById(R.id.swipe);
        adapter=new AllAdapter(false, list, getActivity());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);
        scrollingItem();
        swipeListner();
        return view;
    }

    private void swipeListner() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            if (scrolling != null) {
                scrolling.unbind();
            }
            list.clear();
            adapter.notifyDataSetChanged();
            page=1;
            ToastMy.successToast(getActivity(),Cvalues.UPDATING,ToastMy.LENGTH_SHORT);
            scrollingItem();
        });
    }

    private void scrollingItem() {
        scrolling=NoPaginate.with(recyclerView).setOnLoadMoreListener(() -> {
            if (!loading) {
                loading=true;
                MyMovies.getMovies(getActivity(), category, PERPAGE, page, moviesPosts -> {
                    if (moviesPosts == null) {
                        scrolling.setNoMoreItems(true);
                        ToastMy.errorToast(getActivity(), UNABLE_TO_LOAD, ToastMy.LENGTH_SHORT);
                        return;
                    } else if (moviesPosts.isEmpty()) {
                        scrolling.setNoMoreItems(true);
                        return;
                    }
                    if (isFirstTime && !detach) {
                        new Thread(() -> {
                            List<MoviesPosts> posts=new ArrayList<>();
                            for (int i=0; i < moviesPosts.size(); i++) {
                                if (category==36){
                                    if (i <= 9) {
                                        posts.add(moviesPosts.get(i));
                                    }
                                }else {
                                    if (i <= 5) {
                                        posts.add(moviesPosts.get(i));
                                    }
                                }
                            }
                            switch (category) {
                                case 36:
                                    MySharedPref.putMoviesList(getActivity(), posts, MySharedPref.RECENT);
                                    break;
                                case 37:
                                    MySharedPref.putMoviesList(getActivity(), posts, MySharedPref.NETFLIX);
                                    break;
                                case 39:
                                    MySharedPref.putMoviesList(getActivity(), posts, MySharedPref.BOLLYWOOD);
                                    break;
                                case 40:
                                    MySharedPref.putMoviesList(getActivity(), posts, MySharedPref.HOLLYWOOD);
                                    break;
                            }
                            isFirstTime=false;
                        }).start();
                    }
                    list.addAll(moviesPosts);
                    adapter.notifyDataSetChanged();
                    page++;
                    loading=false;

                });
            }
        }).setCustomLoadingItem(new CustomScrollingIconChange()).build();
    }

    @Override
    public void onDetach() {
        Cvalues.Category=0;
        detach=true;
        super.onDetach();

    }
}