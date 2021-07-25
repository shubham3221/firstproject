//package com.example.s_tools.tools;
//
//import android.app.ActivityOptions;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.view.View;
//
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.core.content.ContextCompat;
//import androidx.core.view.GravityCompat;
//
//import com.example.s_tools.R;
//import com.example.s_tools.Splash_login_reg.SplashScreen;
//import com.example.s_tools.chatting.ChatKaro;
//import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.SearchMov;
//import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.SectionsFragment;
//import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MoviesPosts;
//import com.example.s_tools.entertainment.VideoActivity;
//import com.example.s_tools.entertainment.uper_slider.SliderPage_Adapter;
//import com.example.s_tools.premium.Dashboard;
//import com.example.s_tools.testing.DownloadActivity;
//import com.example.s_tools.tools.retrofitcalls.MySharedPref;
//import com.example.s_tools.tools.retrofitcalls.VC;
//import com.example.s_tools.user_request.bb.OnlineModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//class videoactivitycode {
//    private class MyAsyncTask_Add_Fragments extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... strings) {
////            setup_ad();
//            if (MySharedPref.getMoviesList(VideoActivity.this, MySharedPref.RECENT) != null) {
//                new Thread(() -> {
//                    List<OnlineModel> onlineModels=new ArrayList<>();
//                    List<MoviesPosts> moviesList=MySharedPref.getMoviesList(VideoActivity.this, MySharedPref.RECENT);
//                    for (int i=0; i < moviesList.size(); i++) {
//                        if (moviesList.get(i).getCustom_fields().getOtherlinks() != null) {
//                            String[] split=moviesList.get(i).getCustom_fields().getOtherlinks().get(0).split(",");
//                            for (String link : split) {
//                                if (link.contains("anonfiles")) {
//                                    onlineModels.add(new OnlineModel(link.trim(), moviesList.get(i).getTitle().trim(), moviesList.get(i).getCustom_fields().getMainimage().get(0).trim()));
//                                }
//                            }
//                        }
//                    }
//                    if (!onlineModels.isEmpty()) {
//                        upperSliderAdapter=new SliderPage_Adapter(VideoActivity.this, onlineModels);
//                        publishProgress();
//                    }
//
//                }).start();
//
//            }
//            if (tinyDB.getLong("lastcheck") == 0) {
//                new Thread(() -> {
//                    VC.check_v(VideoActivity.this, (success, updateinfo) -> {
//                        if (success) {
//                            tinyDB.putLong("lastcheck", System.currentTimeMillis());
//                        }
//                    });
//                }).start();
//            } else {
//                new Thread(() -> {
//                    long now=System.currentTimeMillis();
//                    long diff=now - tinyDB.getLong("lastcheck");
//                    if (diff >= (3600000 * 24)) {
//                        VC.check_v(VideoActivity.this, (success, updateinfo) -> {
//                            if (success) {
//                                tinyDB.putLong("lastcheck", now);
//                            }
//                        });
//                    }
//                }).start();
//            }
//            sectionsFragment=new SectionsFragment(mInterstitialAd);
//            fragmentTransaction=getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.add(R.id.mviewpager, sectionsFragment);
//
//            drawerToggle=new ActionBarDrawerToggle(VideoActivity.this, drawerLayout, R.string.open, R.string.close);
//            drawerLayout.addDrawerListener(drawerToggle);
//            drawerToggle.syncState();
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//            viewPagerUp.setVisibility(View.VISIBLE);
//            indicator.setupWithViewPager(viewPagerUp, false);
//            viewPagerUp.setAdapter(upperSliderAdapter);
//        }
//
//        @Override
//        protected void onPostExecute(Void s) {
//            searchBar.setOnClickListener(view -> {
//                Intent intent=new Intent(VideoActivity.this, SearchMov.class);
//                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(VideoActivity.this, searchBar, "cardview");
//                startActivity(intent, options.toBundle());
//            });
//            navigtionDrawer();
//            fragmentTransaction.commit();
//            premium.setOnClickListener(view -> {
//                startActivity(new Intent(VideoActivity.this, Dashboard.class));
//            });
//            chatkaro.setOnClickListener(view -> {
//                if (!MySharedPref.isSharedPrefnull(VideoActivity.this)) {
//                    if (messageCountView.getText().length() >= 1) {
//                        messageCountView.setText("");
//                        messageCountView.setBackground(ContextCompat.getDrawable(VideoActivity.this, R.drawable.sms2));
//                    }
//                    startActivity(new Intent(VideoActivity.this, ChatKaro.class));
//                } else {
//                    startActivity(new Intent(VideoActivity.this, SplashScreen.class));
//                }
//            });
//            downloadActivity.setOnClickListener(view -> {
//                startActivity(new Intent(VideoActivity.this, DownloadActivity.class));
//            });
//            openDrawerTool.setOnClickListener(view -> {
//                drawerLayout.openDrawer(GravityCompat.START);
//            });
//            updateMoviesBtn.setOnClickListener(view -> {
////                updateAll();
//            });
//        }
//
//
//    }
//
//}
