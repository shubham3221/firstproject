package com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.Tab_2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class Tab2_Fragment extends Fragment {
    public static final String PAGE_Query="&page=";
    public static final String STRING="https://search.givewater.com/serp?qc=images&page=";
    public static final String QUERY_FIRST="https://search.givewater.com/serp?qc=images&page=1&q=";

    View view;
    private RecyclerView recyclerView;
    private boolean loading = true;
    private int page=2;
    Document doc;
    Tab2_Adapter adapter;

    Elements elementClass=new Elements();
    Elements elementsTag=new Elements();
    private NoPaginate scrolling;
    List<String> urls;

    public Tab2_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.tab2_layout, container, false);
        recyclerView = view.findViewById(R.id.tab2_recycler);
        new MyAsyncTask().execute(Cvalues.query);
        return view;
    }
    private class MyAsyncTask extends AsyncTask<String,String, List<String>> {
        public static final String QUERY_FIRST="https://search.givewater.com/serp?qc=images&page=1&q=";
        private int visibleItemCount;
        private int totalItemCount;
        private int pastVisiblesItems;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            urls = new ArrayList<>();
            try {
                urls.clear();
                doc = Jsoup.connect(QUERY_FIRST +strings[0]).
                        get();
                elementClass=doc.getElementsByClass("image");
                for (int i=0; i<elementClass.size(); i++){
                    elementsTag.add(elementClass.get(i).getElementsByTag("img").first());
                }
                for (int i = 0; i<elementsTag.size(); i++){
                    urls.add(elementsTag.get(i).absUrl("src"));

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(urls);
            adapter = new Tab2_Adapter(urls,getActivity());
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            recyclerView.setLayoutManager(manager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(null);
            recyclerView.setAdapter(adapter);

            scrolling=NoPaginate.with(recyclerView).setOnLoadMoreListener(() -> {
                manager.invalidateSpanAssignments();
                //http or db request
                if (loading) {
                    loading=false;
                    fetchNextPage(page);
                }
            }).setCustomLoadingItem(new CustomScrollingIconChange()).build();
            if (urls.isEmpty()){
                scrolling.setNoMoreItems(true);
            }
        }

    }
    private void fetchNextPage(int nextPage)  {
        new Thread(() -> {
            try {
                Elements tags=new Elements();
                Document document=Jsoup.connect(STRING + nextPage + "&q=" + Cvalues.query).
                        get();
                Log.e(TAG, "fetchNextPage: "+STRING +nextPage+"&q="+ Cvalues.query );
                Elements image=document.getElementsByClass("image");
                for (int i=0; i<image.size(); i++){
                    tags.add(image.get(i).getElementsByTag("img").first());
                }

                for (int i = 0; i<tags.size(); i++){
                    urls.add(tags.get(i).absUrl("src"));
                }
                page++;
                loading = true;
                getActivity().runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                Toast.makeText(getActivity(), "unable to get response", Toast.LENGTH_SHORT).show();
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