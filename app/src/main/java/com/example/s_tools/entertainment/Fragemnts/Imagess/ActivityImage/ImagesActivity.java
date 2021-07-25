package com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.s_tools.R;
import com.example.s_tools.TinyDB;
import com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.Tab_1.RecentClick;
import com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.Tab_1.Tab1_Fragment;
import com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.Tab_2.Tab2_Fragment;
import com.example.s_tools.tools.Cvalues;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements RecentClick {
    public static final String TAG="mtag";
    public static final String KEY="key";
    private TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerImagesAdapter viewPagerAdpater;
    AppCompatAutoCompleteTextView editText;
    TinyDB tinyDB;
    List<String> suggestions =new ArrayList<>();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images_activity);
        init();
        new Thread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.suggestion_dialog, R.id.autoCompleteItem,suggestions);
            viewPager.setSaveFromParentEnabled(false);
            viewPagerAdpater= new ViewPagerImagesAdapter(getSupportFragmentManager());
            viewPagerAdpater.addFragment(new RecentQueryFragment(this)," ");
            runOnUiThread(() -> {
                viewPager.setAdapter(viewPagerAdpater);
                editText.setThreshold(2);//will start working from first character
                editText.setAdapter(adapter);

            });


            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (editText.getText().toString().length() > 2){
                        progressBar.setVisibility(View.VISIBLE);
                        SuggestionClass.getSuggestions(editText.getText().toString(), (success, jsonArray) -> {
                            if (success){
                                adapter.clear();
                                for (int i = 0; i<jsonArray.get(1).getAsJsonArray().size(); i++){
                                    suggestions.add(jsonArray.get(1).getAsJsonArray().get(i).getAsString());
                                    adapter.add(jsonArray.get(1).getAsJsonArray().get(i).getAsString());
                                    adapter.getFilter().filter(editText.getText().toString());
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        }).start();

//            RecentQueryFragment someFragment=new RecentQueryFragment();
////                Bundle args=new Bundle();
////        args.putSerializable(TWO, (Serializable) two);
////                someFragment.setArguments(args);
//
//                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.add(R.id.imageViewpager, someFragment); // give your fragment container id in first parameter
//                fragmentTransaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                fragmentTransaction.commit();


        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                editText.clearFocus();
                editText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                ArrayList<String> data=tinyDB.getListString(KEY);
                    data.add(editText.getText().toString());
                    tinyDB.putListString(KEY,data);
                   Cvalues.query =  editText.getText().toString();
                    new MyAsyncTask_Tab_Show().execute(editText.getText().toString());

                return true;
            }
            return false;
        });
    }

    private void init() {
        tabLayout=findViewById(R.id.mytab);
        viewPager=findViewById(R.id.imageViewpager);
        editText =(AppCompatAutoCompleteTextView) findViewById(R.id.edit_search);
        tinyDB = new TinyDB(ImagesActivity.this);
        progressBar = findViewById(R.id.progressbar_suggestions);
    }

    @Override
    public void recentItemClicked(String s) {
        editText.setText(s);
        editText.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
        progressBar.setVisibility(View.GONE);
    }


    private class MyAsyncTask_Tab_Show extends AsyncTask<String,String,String> {

        public static final String DB_1="Database-1";
        public static final String DB_2="Database-2";
//        private static final String DB_3="Database-3";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            viewPagerAdpater = new ViewPagerImagesAdapter(getSupportFragmentManager());
            viewPagerAdpater.addFragment(new Tab1_Fragment(), DB_1);
            viewPagerAdpater.addFragment(new Tab2_Fragment(), DB_2);
//            viewPagerAdpater.addFragment(new Tab3_Fragment(), DB_3);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            viewPager.setAdapter(viewPagerAdpater);
            tabLayout.setupWithViewPager(viewPager);
        }
    }



    public static class RecentQueryFragment extends Fragment {
        public RecyclerView recyclerView;
        Button clear;
        TinyDB tinyDB;
        RecentQueryAdapter adapter;
        ImagesActivity activity;

        public RecentQueryFragment(ImagesActivity imagesActivity) {
            this.activity = imagesActivity;
        }
        public RecentQueryFragment() {
            // Empty
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            clear = rootView.findViewById(R.id.recentClearbtn);
            recyclerView = rootView.findViewById(R.id.queryRecycler);
            tinyDB = new TinyDB(getActivity());
            new MyAsyncTask_Fetch_RecentData().execute();
            return rootView;
        }
        private class MyAsyncTask_Fetch_RecentData extends AsyncTask<String,String,ArrayList>{

            @Override
            protected ArrayList doInBackground(String... strings) {
                ArrayList<String> data = null;
                data=tinyDB.getListString(KEY);

                adapter = new RecentQueryAdapter(getActivity(),data,activity);
                return data;
            }

            @Override
            protected void onPostExecute(ArrayList arrayList) {
                super.onPostExecute(arrayList);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                recyclerView.setAdapter(adapter);

                if (arrayList!=null && !arrayList.isEmpty()){
                    clear.setVisibility(View.VISIBLE);
                    clear.setOnClickListener(v -> {
                        tinyDB.clear();
                        adapter.list.clear();
                        adapter.notifyDataSetChanged();
                        clear.setVisibility(View.GONE);
                    });
                }
            }
        }
    }
}