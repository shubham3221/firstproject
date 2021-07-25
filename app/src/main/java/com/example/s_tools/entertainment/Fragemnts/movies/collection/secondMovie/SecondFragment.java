package com.example.s_tools.entertainment.Fragemnts.movies.collection.secondMovie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.MovieDetail.MovieItemClickListner;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.MoviesApiCalls;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.MoviesModel;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.secondMovie.thirdCollection.ApiCalls;
import com.example.s_tools.tools.CustomScrollingIconChange;
import com.example.s_tools.tools.JsoupParser;
import com.example.s_tools.tools.ToastMy;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import ru.alexbykov.nopaginate.paginate.NoPaginate;


public class SecondFragment extends Fragment {
    RecyclerView recyclerView;
    View view;
    List<MoviesModel> list;
    RecyclerAdapter adapter;
    private boolean isPostLoading=true;
    private int page=1;
    MovieItemClickListner listner;
    NoPaginate scrolling;
    LinearLayout linearLayout;
    AutoCompleteTextView editText;
    private int searchPage=1;
    private String searchQuery;
    private boolean isSearching=true;
    private KProgressHUD kProgressHUD;

    public SecondFragment() {
        // Required empty public constructor
    }

    public SecondFragment(MovieItemClickListner itemClickListner) {
        this.listner=itemClickListner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_collection_three, container, false);
        recyclerView=view.findViewById(R.id.col3_recycler);
        editText=(AppCompatAutoCompleteTextView) view.findViewById(R.id.edit_search);
        linearLayout=view.findViewById(R.id.bannerView_col3);
        new MyAsyncTask().execute();
        return view;
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list=new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            adapter=new RecyclerAdapter(list, getActivity());
            publishProgress();
            getPosts(page);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            RecycleClick.addTo(recyclerView).setOnItemClickListener((recyclerView, i, view) -> {
                View viewById=view.findViewById(R.id.sec_movie_img);
                listner.onmovieClick(list.get(i), (ImageView) viewById);
            });


            //search
            editText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    editText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    if (v.getText().length() < 2) {
                        Toast.makeText(getActivity(), "Too Short.", Toast.LENGTH_SHORT).show();
                    } else if (!isPostLoading) {
                        kProgressHUD=KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Searching...").setAutoDismiss(true).setDimAmount(0.5f)
                                .setCancellable(false).show();
                        scrolling.setNoMoreItems(true);
                        searchPage=1;
                        searchQuery=v.getText().toString();
                        list.clear();
                        adapter.notifyDataSetChanged();
                        searchApi(searchQuery, searchPage);
                    } else {
                        Toast.makeText(getActivity(), "Please Wait...", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            });

            //scrolling
            scrolling=NoPaginate.with(recyclerView).setOnLoadMoreListener(() -> {

                if (!isPostLoading) {
                    isPostLoading=true;
                    getPosts(page);
                }

            }).setCustomLoadingItem(new CustomScrollingIconChange()).build();

        }
    }

    private void getPosts(int mPage) {
        ApiCalls.getPostsMovieRush(getActivity(), mPage, (success, modelList) -> {
            if (success) {
                try {
                    for (MoviesModel m : modelList) {
                        m.setMainimg(JsoupParser.parseMainImg(m.getContent().getRendered()));
                        Toast.makeText(getActivity(), ""+m.getMainimg(), Toast.LENGTH_LONG).show();
                        list.add(m);
                    }
                    adapter.notifyDataSetChanged();
                    page++;
                    isPostLoading=false;
                } catch (Exception e) {
                    ToastMy.errorToast(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG);
                }
            } else {
                isPostLoading=false;
                scrolling.setNoMoreItems(true);
            }
        });
    }

    private void searchApi(String mquery, int mpage) {
        MoviesApiCalls.SearchIt(getActivity(), mquery, 2, mpage, (success, modelList, isComplete) -> {
            if (success) {
                kProgressHUD.setDetailsLabel("Found "+list.size()+" Result.");
                modelList.setMainimg(JsoupParser.parseMainImg(modelList.getContent().getRendered()));
                list.add(modelList);
                adapter.notifyDataSetChanged();
            }else if (isComplete){
                kProgressHUD.dismiss();
            }
        });
    }



    @Override
    public void onDetach() {
        super.onDetach();
        scrolling.unbind();
    }
}