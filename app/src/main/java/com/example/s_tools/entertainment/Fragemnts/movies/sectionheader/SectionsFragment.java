package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.Dashboard_Google;
import com.example.s_tools.entertainment.Fragemnts.movies.google_dict.Google_Activity;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MoviesPosts;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MySection;
import com.example.s_tools.premium.model.Category;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.example.s_tools.user_request.UserRequestActivity;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;


public class SectionsFragment extends Fragment {
    private boolean update=false;
    RecyclerView recyclerView;
    List<MySection> sections;
    Context context;
    View view;
    public static String movieurl;
    CardView adult;
    private InterstitialAd interstitialAd;

    SectionRecyclerViewAdapter adapterRecycler;
    KProgressHUD progressHUD;

    public SectionsFragment(Context context) {
        this.context=context;
    }

    public SectionsFragment() {
    }

    public SectionsFragment(boolean update, InterstitialAd mInterstitialAd) {
        this.interstitialAd=mInterstitialAd;
        this.update=update;
    }

    public SectionsFragment(InterstitialAd mInterstitialAd) {
        this.interstitialAd=mInterstitialAd;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_test__movies, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.vidRecycler1);
        adult=view.findViewById(R.id.cadview3);
        progressHUD=KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Loading...").setCancellable(true)
//                .setDimAmount(0.6f)
                .show();
        adult.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), Google_Activity.class));
//            startActivity(new Intent(getActivity(), Dashboard_Google.class));
//            Dialog update_dialog=new Dialog(getActivity());
//            update_dialog.setContentView(R.layout.adultdlog);
//
//            Button yes=update_dialog.findViewById(R.id.enter);
//            EditText editText=update_dialog.findViewById(R.id.edittext);
//            update_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
//            update_dialog.setCancelable(true);
//            update_dialog.show();
//
//            yes.setOnClickListener(v1 -> {
//                if (editText.getText().toString().equalsIgnoreCase("mbuddy")) {
//                    update_dialog.dismiss();
//                    AllFragment someFragment=new AllFragment(41);
//                    someFragment.setEnterTransition(new Fade());
//                    someFragment.setExitTransition(new Fade());
//                    FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.mycontainer, someFragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                }
//            });
        });
        new MyAsyncTask().execute();
        return view;
    }

    private class MyAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            movieurl=MySharedPref.getMovieUrl(getActivity());
        }

        @Override
        protected String doInBackground(String... strings) {
            sections=new ArrayList<>();
            sections.add(new MySection(36, "Recent", demoData()));
            sections.add(new MySection(37, "Netflix & Amazon", demoData2()));
            sections.add(new MySection(39, "Bollywood", demoData2()));
            sections.add(new MySection(40, "Hollywood", demoData2()));
            adapterRecycler=new SectionRecyclerViewAdapter(interstitialAd, sections, getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapterRecycler);
            if (update) {
                updateFromMain();
            } else {
                firstTimeFetchingMovies();
            }
        }

        private void firstTimeFetchingMovies() {
            new Thread(() -> {
                if (MySharedPref.getMoviesList(getActivity(), MySharedPref.RECENT) == null) {
                    getPosts(36, 0);
                } else {
                    new Thread(() -> getsavedList(36, 0)).start();
                }
                if (MySharedPref.getMoviesList(getActivity(), MySharedPref.NETFLIX) == null) {
                    getPosts(37, 1);
                } else {
                    new Thread(() -> getsavedList(37, 1)).start();

                }
                if (MySharedPref.getMoviesList(getActivity(), MySharedPref.BOLLYWOOD) == null) {
                    getPosts(39, 2);
                } else {
                    new Thread(() -> getsavedList(39, 2)).start();

                }
                if (MySharedPref.getMoviesList(getActivity(), MySharedPref.HOLLYWOOD) == null) {
                    getPosts(40, 3);
                } else {
                    new Thread(() -> getsavedList(40, 3)).start();

                }
            }).start();
        }

        private void updateFromMain() {
            getPosts(36, 0);
            getPosts(37, 1);
            getPosts(39, 2);
            getPosts(40, 3);
        }

        private void getsavedList(int category, int pos) {
            List<MoviesPosts> movies=null;
            switch (category) {
                case 36:
                    movies=MySharedPref.getMoviesList(getActivity(), MySharedPref.RECENT);
                    break;
                case 37:
                    movies=MySharedPref.getMoviesList(getActivity(), MySharedPref.NETFLIX);
                    break;
                case 39:
                    movies=MySharedPref.getMoviesList(getActivity(), MySharedPref.BOLLYWOOD);
                    break;
                case 40:
                    movies=MySharedPref.getMoviesList(getActivity(), MySharedPref.HOLLYWOOD);
                    break;
            }
            sections.get(pos).setMoviesPosts(movies);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapterRecycler.notifyItemChanged(pos);
                    if (progressHUD.isShowing()) {
                        progressHUD.dismiss();
                    }
                });
            }
        }

        private void getPosts(int category, int pos) {
            MyMovies.getMovies(getActivity(), category, category == 36 ? 10 : 6, 1, movies -> {
                if (movies != null) {
                    sections.get(pos).setMoviesPosts(movies);
                    getActivity().runOnUiThread(() -> {
                        switch (category) {
                            case 36:
                                MySharedPref.putMoviesList(getActivity(), movies, MySharedPref.RECENT);
                                adapterRecycler.notifyItemChanged(0);
                                break;
                            case 37:
                                MySharedPref.putMoviesList(getActivity(), movies, MySharedPref.NETFLIX);
                                adapterRecycler.notifyItemChanged(1);
                                break;
                            case 39:
                                MySharedPref.putMoviesList(getActivity(), movies, MySharedPref.BOLLYWOOD);
                                adapterRecycler.notifyItemChanged(2);
                                break;
                            case 40:
                                MySharedPref.putMoviesList(getActivity(), movies, MySharedPref.HOLLYWOOD);
                                adapterRecycler.notifyItemChanged(3);
                                progressHUD.dismiss();
                                break;
                        }
                    });
                }
            });
        }

        List<MoviesPosts> demoData() {
            List<MoviesPosts> list=new ArrayList<>();
            List<Category> category=new ArrayList<>();
            category.add(new Category(36));
            for (int i=0; i < 2; i++) {
                list.add(new MoviesPosts(1));
            }
            return list;
        }

        List<MoviesPosts> demoData2() {
            List<MoviesPosts> list=new ArrayList<>();
            for (int i=0; i < 4; i++) {
                list.add(new MoviesPosts(1));
            }
            return list;
        }

    }
}
//refresh
//            refreshLayout.setColorSchemeColors(Color.BLACK);
//            refreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.yellow));