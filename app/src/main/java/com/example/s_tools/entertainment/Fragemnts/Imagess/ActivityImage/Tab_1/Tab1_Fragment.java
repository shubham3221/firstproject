package com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.Tab_1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.s_tools.R;
import com.example.s_tools.tools.CustomScrollingIconChange;
import com.example.s_tools.tools.Cvalues;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.alexbykov.nopaginate.paginate.NoPaginate;


public class Tab1_Fragment extends Fragment {
    public static final String HTTPS_WWW_BING_COM_IMAGES_ASYNC_Q="https://www.bing.com/images/async?q=";
    public static final String IMG_MIMG_VIMGLD="img.mimg.vimgld";
    public static final String DATA_SRC="data-src";
    View view;
    private RecyclerView recyclerView;
    private boolean loading = true;
    private int page=60;
    Tab1_Adapter adapterFragment;
    Document doc;
    Elements element;
    List<String> list  =new ArrayList<>();
    NoPaginate scrolling;

    public Tab1_Fragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.tab1fragment, container, false);
        recyclerView = view.findViewById(R.id.tab1);
        //



        new MyAsyncTask().execute(Cvalues.query);

        return view;
    }
    private class MyAsyncTask extends AsyncTask<String,String,List<String>> {
        public static final String QUERY_FIRST="https://www.bing.com/images/async?first=30&q=";
        public static final String IMG_MIMG_VIMGLD="img.mimg.vimgld";
        private int visibleItemCount;
        private int totalItemCount;
        private int pastVisiblesItems;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list.clear();
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            try {
                doc = Jsoup.connect(QUERY_FIRST +strings[0]).
                        get();
                 element=doc.select(IMG_MIMG_VIMGLD);
                for (int i=0; i<element.size(); i++){
                    list.add(element.get(i).absUrl(DATA_SRC));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            adapterFragment = new Tab1_Adapter(strings,getActivity());
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            recyclerView.setItemAnimator(null);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapterFragment);


            ((StaggeredGridLayoutManager)recyclerView.getLayoutManager()).invalidateSpanAssignments();
            scrolling=NoPaginate.with(recyclerView).setOnLoadMoreListener(() -> {
                //http or db request
                if (loading) {
                    loading=false;
                    fetchNextPage(page);
                }
            }).setCustomLoadingItem(new CustomScrollingIconChange()).build();
            if (strings.isEmpty()){
                scrolling.unbind();
            }


            // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
           //
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
//                    if (dy > 0) {
//                        ((StaggeredGridLayoutManager)recyclerView.getLayoutManager()).invalidateSpanAssignments();
//                        visibleItemCount = manager.getChildCount();
//                        totalItemCount = manager.getItemCount();
//                        pastVisiblesItems = manager.findFirstVisibleItemPositions(null)[0];
//
//                        if (loading) {
//                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                                loading = false;
//                                fetchNextPage(page);
//                            }
//                        }
//                    }
//                }
//            });
        }

    }
    private void fetchNextPage(int nextPage)  {
        new Thread(() -> {
            try {
                element.clear();
                doc = Jsoup.connect(HTTPS_WWW_BING_COM_IMAGES_ASYNC_Q +Cvalues.query+"&first="+nextPage).
                        get();
                element=doc.select(IMG_MIMG_VIMGLD);
                for (int i=0; i<element.size(); i++){
                    adapterFragment.list.add(element.get(i).absUrl(DATA_SRC));
                }
                page+=30;
                loading = true;
                getActivity().runOnUiThread(() -> {
                    adapterFragment.notifyDataSetChanged();
                });

            } catch (IOException e) {
                e.printStackTrace();
            }



        }).start();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (scrolling!=null){
            scrolling.unbind();
        }
    }
}