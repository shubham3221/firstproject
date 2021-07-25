package com.example.s_tools.entertainment.Fragemnts.movies.collection.secondMovie.thirdCollection;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.MovieDetail.MovieItemClickListner;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.MoviesApiCalls;
import com.example.s_tools.entertainment.Fragemnts.movies.collection.collectionModel.MoviesModel;
import com.example.s_tools.tools.CustomScrollingIconChange;
import com.example.s_tools.tools.JsoupParser;
import com.example.s_tools.tools.ToastMy;

import java.util.ArrayList;
import java.util.List;

import ru.alexbykov.nopaginate.paginate.NoPaginate;


public class ThirdFragment extends Fragment {
    RecyclerView recyclerView;
    View view;
    List<MoviesModel> list;
    RecyclerAdapter adapter;
    LinearLayoutManager manager;
    private boolean isPostLoading= true;
    private int page=1;
    MovieItemClickListner listner;
    NoPaginate scrolling;
    LinearLayout linearLayout;
    AutoCompleteTextView editText;
    private int searchPage = 1;
    private String searchQuery;
    private boolean isSearching= true;

    public ThirdFragment() {
        // Required empty public constructor
    }

    public ThirdFragment(MovieItemClickListner itemClickListner) {
        this.listner = itemClickListner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_collection_three, container, false);
        recyclerView = view.findViewById(R.id.col3_recycler);
        editText =(AppCompatAutoCompleteTextView) view.findViewById(R.id.edit_search);
        linearLayout = view.findViewById(R.id.bannerView_col3);

        new MyAsyncTask().execute();
        return view;
    }


    class MyAsyncTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            adapter = new RecyclerAdapter(list,getActivity());
            publishProgress();
            getPosts(page);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            manager = new LinearLayoutManager(getActivity());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            RecycleClick.addTo(recyclerView).setOnItemClickListener((recyclerView, i, view) -> {
                    View viewById=view.findViewById(R.id.img_c_1);
                    listner.onmovieClick(list.get(i), (ImageView) viewById);
            });


            //search
            editText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    editText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    if (v.getText().length()<2){
                        Toast.makeText(getActivity(), "Too Short.", Toast.LENGTH_SHORT).show();
                    }else if (!isPostLoading){
                        list.clear();
                        adapter.notifyDataSetChanged();
                        searchPage = 1;
                        isSearching= false;
                        searchQuery = v.getText().toString();
                        searchApi(searchQuery,searchPage);
                    }else {
                        ToastMy.errorToast(getActivity(),"Please Wait...", ToastMy.LENGTH_SHORT);
                    }
                    return true;
                }
                return false;
            });

            //scrolling
            scrolling=NoPaginate.with(recyclerView).setOnLoadMoreListener(() -> {
                if (!isSearching){
                    isSearching= true;
                    searchApi(searchQuery,searchPage);
                    return;
                }
                if (!isPostLoading) {
                    isPostLoading=true;
                    getPosts(page);
                }

            }).setCustomLoadingItem(new CustomScrollingIconChange()).build();

        }
    }
    private void getPosts(int mPage) {
        ApiCalls.getPostsMovie4(getActivity(), mPage, (success, modelList) -> {
            if (success){
                try {
                    for (MoviesModel m : modelList) {
                        m.setMainimg(JsoupParser.parseMainImg(m.getContent().getRendered()));
                        list.add(m);
                    }
                    adapter.notifyDataSetChanged();
                    page++;
                    isPostLoading= false;
                }catch (Exception e){
                    ToastMy.errorToast(getActivity(), "Error", Toast.LENGTH_SHORT);
                }
            }else {
                isPostLoading=false;
                scrolling.setNoMoreItems(true);
            }
        });
    }

    private void searchApi(String mquery,int mpage){
        searchPage++;
        MoviesApiCalls.SearchIt(getActivity(), mquery,3,mpage, (success, modelList, isComplete) -> {
            if (success){
                new Thread(() -> {
                    modelList.setMainimg(JsoupParser.parseMainImg(modelList.getContent().getRendered()));
                    list.add(modelList);
                        adapter.notifyDataSetChanged();
                    }).start();
                if (isComplete){
                    isSearching= false;
                }

            }else {
                if (isComplete){
                    isSearching = false;
                    scrolling.setNoMoreItems(true);
                }
            }
        });
    }






    @Override
    public void onDetach() {
        super.onDetach();
        scrolling.unbind();
    }

}