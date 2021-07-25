package com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.Tab_3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import static com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.ImagesActivity.TAG;

public class Tab3_Fragment extends Fragment {
    public static final String PAGE_Query="&p=";
    public static final String STRING="https://www.ecosia.org/images?q=";
    public static final String THUMBNAIL_LINK_WRAPPER="image-thumbnail__link-wrapper";
    View view;
    private RecyclerView recyclerView;
    private boolean loading = true;
    private int page=1;
    Document doc;
    Tab3_Adapter adapter;
    Elements elementClass=new Elements();
    List<String> fullimg= new ArrayList<>();
    private NoPaginate scrolling;

    public Tab3_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.tab3_layout, container, false);
        recyclerView = view.findViewById(R.id.tab3_recyclerview);
        new MyAsyncTask().execute(Cvalues.query);
        return view;
    }
    private class MyAsyncTask extends AsyncTask<String,String, List<String>> {
        public static final String QUERY_FIRST="https://www.ecosia.org/images?p=0&q=";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            fullimg.clear();
            try {

                doc = Jsoup.connect(QUERY_FIRST +strings[0]).
                        get();
                elementClass=doc.getElementsByClass(THUMBNAIL_LINK_WRAPPER);

                if (!elementClass.isEmpty()){
                    for (int i=0; i<elementClass.size(); i++){
                        fullimg.add(elementClass.get(i).getElementsByAttribute("href").get(0).attr("href"));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> urls) {
            super.onPostExecute(urls);
            adapter = new Tab3_Adapter(fullimg,getActivity());
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

            manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            recyclerView.setLayoutManager(manager);
            recyclerView.setItemAnimator(null);
            recyclerView.setAdapter(adapter);

            scrolling=NoPaginate.with(recyclerView).setOnLoadMoreListener(() -> {
                //http or db request
                if (loading) {
                    loading=false;
                    fetchNextPage(page);
                }
            }).setCustomLoadingItem(new CustomScrollingIconChange()).build();
        }
    }
    private void fetchNextPage(int nextPage)  {
        new Thread(() -> {
            try {
                doc = Jsoup.connect(STRING + Cvalues.query+PAGE_Query+nextPage).
                        get();
                elementClass=doc.getElementsByClass(THUMBNAIL_LINK_WRAPPER);

                if (!elementClass.isEmpty()){
                    for (int i=0; i<elementClass.size(); i++){
                        fullimg.add(elementClass.get(i).attr("href"));
                    }
                }
                page++;
                loading = true;
                getActivity().runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                Log.e(TAG, "fetchNextPage: "+e.getLocalizedMessage() );
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