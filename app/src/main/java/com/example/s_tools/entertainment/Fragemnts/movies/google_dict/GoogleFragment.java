package com.example.s_tools.entertainment.Fragemnts.movies.google_dict;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chootdev.recycleclick.RecycleClick;
import com.example.s_tools.MyWebService;
import com.example.s_tools.R;
import com.example.s_tools.TinyDB;
import com.example.s_tools.entertainment.Fragemnts.movies.google_dict.upperRecyclerview.SliderGoogleAdapter;
import com.example.s_tools.testing.DownloadActivity;
import com.example.s_tools.tools.GetDate;
import com.example.s_tools.tools.Imageanimation;
import com.example.s_tools.tools.Kbtomb;
import com.example.s_tools.tools.ReadTextWeb;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoogleFragment extends Fragment {
    public static final String TAG="//";
    public static final String LIMIT_REACHED="Server Limit Reached!";
    public static final String REFRESHING="refreshing...";
    public static final int DEFAULTT=0;
    public static final int GRID=1;
    public static final int COMPACT=2;
    public static final String LAYOUT="layout";
    public static final String SORTING="sorting";
    public static final int TIME=250;
    RecyclerView recyclerView, slider;
    GoogleAdapter adapter;
    View view;
    List<Googlemodel> list;
    List<Googlemodel> tempList;
    TextView lastupdate, info;
    String id;
    ImageView searchBtn, cutBtn, reloadBtn, list_compact_btn, sortingBtn,goto_download_activity_btn;
    SliderGoogleAdapter sliderGoogleAdapter;
    View mGroup;
    EditText editText;
    private TextWatcher textWatcher;
    LoadingprogressListner loadingprogressListner;
    SwipeRefreshLayout swipeRefreshLayout;
    NestedScrollView nestedScrollView;
    private boolean loading=false;
    private boolean hasNextPage=false;
    TinyDB tinyDB;
    Imageanimation imageanimation;

    public GoogleFragment() {
        // Required empty public constructor
    }

    public GoogleFragment(RecyclerView upper_nav_recyclerview, SliderGoogleAdapter sliderGoogleAdapter, TextView lastupdate, TextView info, String id) {
        this.lastupdate=lastupdate;
        this.sliderGoogleAdapter=sliderGoogleAdapter;
        this.slider=upper_nav_recyclerview;
        this.id=id;
        this.info=info;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api=Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        view=inflater.inflate(R.layout.fragment_google, container, false);
        init();
        loadingprogressListner=(LoadingprogressListner) getActivity();
        imageanimation=new Imageanimation();

        list=new ArrayList<>();
        tempList=new ArrayList<>();
        tinyDB=new TinyDB(getActivity());
        loadingprogressListner.visibleProgress();
        loadingprogressListner.showProgress();
        swipeRefreshLayout.setEnabled(false);
//        swipeRefreshLayout.setColorSchemeResources(R.color.Grey_700, R.color.info_color, R.color.yellow);

        recyclerSetup();
        recyclerClickListner();
        longCLickLsitner();
        reloadBtnListner();
        compactview_popup();
        sortingBtnClicked();
        gotoDownloadActivity();


        getPost(id);
        searchBtn.setOnClickListener(v -> {
            swipeRefreshLayout.setEnabled(false);
            if (editText.getVisibility() == View.VISIBLE) {
                swipeRefreshLayout.setEnabled(true);
                editText.setText("");
                editText.setVisibility(View.GONE);
                mGroup.setVisibility(View.VISIBLE);
                mGroup.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.cover_img_animation));
                cutBtn.setVisibility(View.GONE);
                searchBtn.setImageResource(R.drawable.ic_baseline_search_24);
                hideKeyboard();
                editText.removeTextChangedListener(textWatcher);
            } else {
                editText.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.cover_img_animation));
                editText.setVisibility(View.VISIBLE);
                mGroup.setVisibility(View.INVISIBLE);
                editText.requestFocus();
                searchBtn.setImageResource(R.drawable.ic_baseline_close_24);
                showKeyboard();
                edittext_listner();
            }
        });
        return view;
    }


    private void recyclerSetup() {
        if (tinyDB.getInt(LAYOUT) == DEFAULTT) {
            default_recycler_setup();
        } else if (tinyDB.getInt(LAYOUT) == GRID) {
            grid_recyclerSetup();
        } else if (tinyDB.getInt(LAYOUT) == COMPACT) {
            compact_recycler_setup();
        }
    }

    private void default_recycler_setup() {
        adapter=new GoogleAdapter(getActivity(), list, DEFAULTT);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void getPost(String id) {
        MyWebService myWebService=MyWebService.google.create(MyWebService.class);
        myWebService.getFiles("'" + id + "' in parents").enqueue(new Callback<Modelclass>() {
            @Override
            public void onResponse(Call<Modelclass> call, Response<Modelclass> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!list.isEmpty()) {
                        list.clear();
                    }
                    list.addAll(response.body().getFiles());
                    tempList.addAll(response.body().getFiles());

                    sorting_setup();


                    setup_refreshListner();
                    if (response.body().getNextPageToken() != null) {
                        lastUpdateSettext(list.size() + "+ files");
                        hasNextPage=true;
                        setup_nextPageListner(response.body().getNextPageToken());
                    } else {
                        lastUpdateSettext(list.size() + " files");
                    }

                } else if (response.code() == 400) {
                    Toast.makeText(getActivity(), LIMIT_REACHED, Toast.LENGTH_LONG).show();
                }
                loadingprogressListner.hideProgress(TIME);
            }

            @Override
            public void onFailure(Call<Modelclass> call, Throwable t) {
                setup_refreshListner();
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                loadingprogressListner.hideProgress(TIME);
                lastUpdateSettext(t.getMessage());
                lastupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), ""+t.getLocalizedMessage().toLowerCase().replace("google","mbuddy"), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void sorting_setup() {
        if (tinyDB.getInt(SORTING) == 0) {
            adapter.notifyDataSetChanged();
//            recyclerView.scheduleLayoutAnimation();
        } else if (tinyDB.getInt(SORTING) == 1) {
            az_setup();
        } else if (tinyDB.getInt(SORTING) == 2) {
            za_setup();
        } else if (tinyDB.getInt(SORTING) == 3) {
            older_setup();
        } else if (tinyDB.getInt(SORTING) == 4) {
            newer_setup();
        }
        info_setup();
    }

    private void recyclerClickListner() {
        lastupdate.setVisibility(View.GONE);
        info.setVisibility(View.GONE);
        RecycleClick.addTo(recyclerView).setOnItemClickListener((recyclerView, i, view) -> {
            if (list.get(i).getMimeType().contains("folder")) {
                sliderGoogleAdapter.items.add(list.get(i).getName());
                GoogleFragment adfragment=new GoogleFragment(slider, sliderGoogleAdapter, lastupdate, info, list.get(i).getId());
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                adfragment.setRetainInstance(true);
                fragmentTransaction.add(R.id.adcontainer, adfragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                sliderGoogleAdapter.notifyDataSetChanged();
                slider.scrollToPosition(sliderGoogleAdapter.items.size() - 1);
            } else if (list.get(i).getMimeType().contains("video") | list.get(i).getFileExtension().equals("mkv")) {
                gotoVideoFragment(i);
            } else if (list.get(i).getMimeType().contains("photo") || list.get(i).getMimeType().contains("png")) {
                List<String> images=new ArrayList<>();
                for (Googlemodel googlemodel : list) {
                    images.add(getDownloadlink(googlemodel.getId()));
                }
                imageanimation.showImage(getActivity(),images,i,view.findViewById(R.id.imgvid));
//                new StfalconImageViewer.Builder<>(getActivity(), images, new ImageLoader<String>() {
//                    @Override
//                    public void loadImage(ImageView imageView, String imageUrl) {
//                        Glide.with(getActivity().getApplicationContext()).load(imageUrl).into(imageView);
//                    }
//                }).withTransitionFrom(view.findViewById(R.id.imgvid)).withStartPosition(i).show(true);

            } else showDownloadOptionMenu(i, list.get(i).getMimeType().contains("text"), true);
        });
    }


    private void gotoVideoFragment(int i) {
        VideoFragment adfragment=new VideoFragment(list.get(i).getName(), getDownloadlink(list.get(i).getId()), list.get(i).getThumbnailLink());
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_open_exit, R.anim.fragment_open_enter, R.anim.fragment_open_exit);
        fragmentTransaction.add(R.id.adcontainer, adfragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void showTextDialog(String mtext) {
        Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.text_show_layout);
        TextView text=dialog.findViewById(R.id.text);
        text.setText(mtext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void setup_nextPageListner(String token) {
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                if (!loading && hasNextPage) {
                    loading=true;
                    lastUpdateSettext("Loading...");
                    getNextPageData(token);
                }
            }
        });
    }

    private void getNextPageData(String token) {
        Map<String, String> map=new HashMap<>();
        map.put("pageToken", token);
        MyWebService.google.create(MyWebService.class).getNextPage("'" + id + "' in parents", map).enqueue(new Callback<Modelclass>() {
            @Override
            public void onResponse(Call<Modelclass> call, Response<Modelclass> response) {
                api_setup(response, false);
                loading=false;
            }

            @Override
            public void onFailure(Call<Modelclass> call, Throwable t) {
                if (getActivity() != null) {
                    lastUpdateSettext(t.getMessage());
                    loading=false;
                }
            }
        });
    }

    private void setup_refreshListner() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            lastUpdateSettext(REFRESHING);
            MyWebService myWebService=MyWebService.google.create(MyWebService.class);
            myWebService.getFiles("'" + id + "' in parents").enqueue(new Callback<Modelclass>() {
                @Override
                public void onResponse(Call<Modelclass> call, Response<Modelclass> response) {
                    api_setup(response, true);
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<Modelclass> call, Throwable t) {
                    onfailed(t);
                }
            });
        });
    }

    private void onfailed(Throwable t) {
        if (getActivity() != null) {
            lastUpdateSettext(t.getMessage());
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void api_setup(Response<Modelclass> response, boolean from_refresh) {
        if (response.isSuccessful() && getActivity() != null && response.body() != null && response.body().getFiles() != null) {
            if (from_refresh) {
                list.clear();
                list.addAll(response.body().getFiles());
                tempList.clear();
            } else {
                list.addAll(response.body().getFiles());
            }
            tempList.addAll(response.body().getFiles());
            if (response.body().getNextPageToken() != null) {
                lastUpdateSettext(list.size() + "+ files");
                hasNextPage=true;
            } else {
                lastUpdateSettext(list.size() + " files");
                hasNextPage=false;
            }
            sorting_setup();
        } else if (response.code() == 400 && getActivity() != null) {
            lastUpdateSettext(LIMIT_REACHED);
        } else if (getActivity() != null && response.body().getFiles() == null) {
            lastUpdateSettext("not found");
        }
    }

    private void longCLickLsitner() {
        RecycleClick.addTo(recyclerView).setOnItemLongClickListener((recyclerView, i, view) -> {
            if (!list.get(i).getMimeType().contains("folder")) {
                showDownloadOptionMenu(i, false, false);
            }
            return false;
        });
    }

    private void edittext_listner() {
        textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    adapter.getFilter().filter(s.toString().trim());
                    cutBtn.setVisibility(View.VISIBLE);
                } else {
                    adapter.getFilter().filter(s);
                    cutBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText.addTextChangedListener(textWatcher);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //searching whole
                lastUpdateSettext("searching...");
                searchWhole(editText.getText().toString());
//                editText.removeTextChangedListener(textWatcher);
                return true;
            }
            return false;
        });
        cutBtn.setOnClickListener(v -> {
            editText.setText("");
            cutBtn.setVisibility(View.GONE);
        });
    }

    private void searchWhole(String name) {
        swipeRefreshLayout.setRefreshing(true);
        MyWebService myWebService=MyWebService.google.create(MyWebService.class);
        myWebService.getFiles("'" + id + "' in parents and name contains '" + name + "' and trashed = false").enqueue(new Callback<Modelclass>() {
            @Override
            public void onResponse(Call<Modelclass> call, Response<Modelclass> response) {
                api_setup(response, true);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Modelclass> call, Throwable t) {
                onfailed(t);
            }
        });

    }

    void hideKeyboard() {
        InputMethodManager keyboard=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    void showKeyboard() {
        InputMethodManager keyboard=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void lastUpdateSettext(String msg) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (getActivity() != null) {
                info_setup();
                lastupdate.setVisibility(View.VISIBLE);
                lastupdate.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fadein));
                lastupdate.setText(msg);
            }
        }, 130);

    }

    private void info_setup() {
        if (tinyDB.getInt(SORTING) == 1) {
            info.setVisibility(View.VISIBLE);
            info.setText("Order by: A-Z");
        } else if (tinyDB.getInt(SORTING) == 2) {
            info.setVisibility(View.VISIBLE);
            info.setText("Order by: Z-A");
        } else if (tinyDB.getInt(SORTING) == 3) {
            info.setVisibility(View.VISIBLE);
            info.setText("Order by: Older First");
        } else if (tinyDB.getInt(SORTING) == 4) {
            info.setVisibility(View.VISIBLE);
            info.setText("Order by: New First");
        } else {
            info.setVisibility(View.GONE);
        }
    }

    private void init() {
        recyclerView=view.findViewById(R.id.namesrecyclerview);
        searchBtn=view.findViewById(R.id.searchicon);
        mGroup=view.findViewById(R.id.bottomgroup);
        cutBtn=view.findViewById(R.id.cutbtn);
        editText=view.findViewById(R.id.searchbox);
        swipeRefreshLayout=view.findViewById(R.id.swipe);
        nestedScrollView=view.findViewById(R.id.nested);
        reloadBtn=view.findViewById(R.id.item2);
        list_compact_btn=view.findViewById(R.id.item3);
        sortingBtn=view.findViewById(R.id.item4);
        goto_download_activity_btn=view.findViewById(R.id.item5);
    }

    void reloadBtnListner() {
        reloadBtn.setOnClickListener(v -> {
            lastUpdateSettext(REFRESHING);
            list.clear();
            adapter.notifyDataSetChanged();

            getPost(id);
        });
    }

    private void showDownloadOptionMenu(int position, boolean isText, boolean wanttodownload) {
        Dialog dialog=new Dialog(getActivity(), R.style.jz_style_dialog_progress);
        dialog.setContentView(R.layout.google_popup_menu);
        TextView title=dialog.findViewById(R.id.title);
        TextView open=dialog.findViewById(R.id.open);
        TextView download=dialog.findViewById(R.id.download);
        TextView properties=dialog.findViewById(R.id.properties);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        title.setText(list.get(position).getName());
        open.setOnClickListener(v14 -> {
            if (list.get(position).getMimeType().contains("video")) {
                gotoVideoFragment(position);
            } else if (isText) {
                KProgressHUD show=KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please Wait...").setCancellable(false).setDimAmount(0.5f).show();
                new Thread(() -> {
                    List<String> text=new ReadTextWeb().getTextFromWeb(getDownloadlink(list.get(position).getId()));
                    if (text != null) {
                        getActivity().runOnUiThread(() -> {
                            show.dismiss();
                            showTextDialog(text.get(0));
                        });
                    } else {
                        getActivity().runOnUiThread(() -> {
                            show.dismiss();
                            Toast.makeText(getActivity(), "Can't Download File.", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).start();
            } else if (wanttodownload) {
                Intent intent=new Intent(getActivity(), DownloadActivity.class);
                intent.putExtra(DownloadActivity.INTENTDOWNLINK, getDownloadlink(list.get(position).getId()));
                intent.putExtra(DownloadActivity.FILENAME, list.get(position).getName());
                intent.putExtra(DownloadActivity.FILEEXTENSION, list.get(position).getFileExtension());
                startActivity(intent);
            }
        });

        download.setOnClickListener(v1 -> {
            if (isText | wanttodownload) {
                Intent intent=new Intent(getActivity(), DownloadActivity.class);
                intent.putExtra(DownloadActivity.INTENTDOWNLINK, getDownloadlink(list.get(position).getId()));
                intent.putExtra(DownloadActivity.FILENAME, list.get(position).getName());
                intent.putExtra(DownloadActivity.FILEEXTENSION, list.get(position).getFileExtension());
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show();
            }
        });
        properties.setOnClickListener(v12 -> {
            dialog.dismiss();
            Dialog dialog1=new Dialog(getActivity());
            dialog1.setContentView(R.layout.google_popup_detail);

            TextView name=dialog1.findViewById(R.id.name);
            TextView mime=dialog1.findViewById(R.id.mime);
            TextView time=dialog1.findViewById(R.id.time);
            TextView filesize=dialog1.findViewById(R.id.filesize);
            TextView hasthumnail=dialog1.findViewById(R.id.hasthumnail);
            TextView extenstion=dialog1.findViewById(R.id.extenstion);

            name.setText("Name:  " + list.get(position).getName());
            mime.setText("Mime Type:  " + list.get(position).getMimeType());
            time.setText("Modified Time:  " + GetDate.covertTimeToTextForWebsite(list.get(position).getModifiedTime()));
            filesize.setText(list.get(position).getSize() != null ? "Size:  " + Kbtomb.getFileSize(Long.parseLong(list.get(position).getSize())) : "Size:  " + "?");
            hasthumnail.setText(list.get(position).getThumbnailLink() != null ? "Has Thumbnail:  Yes" : "Has Thumbnail:  No");
            extenstion.setText("File Extension:  " + list.get(position).getFileExtension());

            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.show();
        });
    }

    void compactview_popup() {
        list_compact_btn.setOnClickListener(v -> {


            Dialog dialog=new Dialog(getActivity(), R.style.jz_style_dialog_progress);
            dialog.setContentView(R.layout.popup_menu_list);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            TextView grid=dialog.findViewById(R.id.one);
            TextView compact=dialog.findViewById(R.id.two);
            TextView normal=dialog.findViewById(R.id.three);

            int layout=tinyDB.getInt(LAYOUT);
            if (layout == DEFAULTT) { //default
                normal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_view_list_24, 0, R.drawable.ic_baseline_check_circle_24, 0);
            } else if (layout == GRID) { //grid
                grid.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_view_grid_24, 0, R.drawable.ic_baseline_check_circle_24, 0);
            } else if (layout == COMPACT) { //compact
                compact.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_format_list_compact_24, 0, R.drawable.ic_baseline_check_circle_24, 0);
            }
            grid.setOnClickListener(v1 -> {
                tinyDB.putInt(LAYOUT, GRID);
                dialog.dismiss();
                grid_recyclerSetup();

            });

            compact.setOnClickListener(v12 -> {
                tinyDB.putInt(LAYOUT, COMPACT);
                dialog.dismiss();
                compact_recycler_setup();

            });

            normal.setOnClickListener(v13 -> {
                tinyDB.putInt(LAYOUT, DEFAULTT);
                dialog.dismiss();
                default_recycler_setup();
            });


        });

    }

    private void compact_recycler_setup() {
        adapter=new GoogleAdapter(getActivity(), list, COMPACT);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void grid_recyclerSetup() {
        adapter=new GoogleAdapter(getActivity(), list, GRID);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void sortingBtnClicked() {
        sortingBtn.setOnClickListener(v -> {
            TinyDB tinyDB=new TinyDB(getActivity());

            Dialog dialog=new Dialog(getActivity(), R.style.jz_style_dialog_progress);
            dialog.setContentView(R.layout.sorting_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            TextView defaultt=dialog.findViewById(R.id.defaultt);
            TextView az=dialog.findViewById(R.id.one);
            TextView za=dialog.findViewById(R.id.two);
            TextView older=dialog.findViewById(R.id.three);
            TextView newer=dialog.findViewById(R.id.four);

            int sorting=tinyDB.getInt(SORTING);
            if (sorting == 0) {
                defaultt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);
            } else if (sorting == 1) {
                az.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);
            } else if (sorting == 2) {
                za.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);
            } else if (sorting == 3) {
                older.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);
            } else if (sorting == 4) {
                newer.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);
            }
            defaultt.setOnClickListener(v12 -> {
                tinyDB.putInt(SORTING, 0);
                list.clear();
                list.addAll(tempList);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
                info_setup();
            });

            az.setOnClickListener(v1 -> {
                tinyDB.putInt(SORTING, 1);
                az_setup();
                dialog.dismiss();
                info_setup();
            });
            za.setOnClickListener(v1 -> {
                tinyDB.putInt(SORTING, 2);
                za_setup();
                dialog.dismiss();
                info_setup();
            });
            older.setOnClickListener(v1 -> {
                tinyDB.putInt(SORTING, 3);

                older_setup();
                dialog.dismiss();
                info_setup();
            });
            newer.setOnClickListener(v1 -> {
                tinyDB.putInt(SORTING, 4);

                newer_setup();
                dialog.dismiss();
                info_setup();
            });
        });
    }

    private void newer_setup() {
        new Thread(() -> {
            List<Googlemodel> googlemodelList=new ArrayList<>(list);
            ArrayList<Long> d=new ArrayList<>();
            for (Googlemodel datelong : list) {
                d.add(GetDate.getTimeLong(datelong.getModifiedTime()));
            }
            list.clear();
            for (int i=0; i < d.size(); i++) {
                for (int j=i+1; j < d.size(); j++) {
                    if (d.get(i) <= d.get(j)) {
                        long a=d.get(i);
                        d.set(i, d.get(j));
                        d.set(j, a);
                    }
                }
                for (int l=0; l<d.size(); l++){
                    if (GetDate.getTimeLong(googlemodelList.get(l).getModifiedTime()).equals(d.get(i))) {
                        list.add(googlemodelList.get(l));
                    }
                }

            }
            if (getActivity()!=null){
                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        }).start();


    }

    private void older_setup() {
        new Thread(() -> {
            List<Googlemodel> googlemodelList=new ArrayList<>(list);
            ArrayList<Long> d=new ArrayList<>();
            for (Googlemodel datelong : list) {
                d.add(GetDate.getTimeLong(datelong.getModifiedTime()));
            }
            list.clear();
            for (int i=0; i < d.size(); i++) {
                for (int j=i+1; j < d.size(); j++) {
                    if (d.get(i) >= d.get(j)) {
                        long a=d.get(i);
                        d.set(i, d.get(j));
                        d.set(j, a);
                    }
                }
                for (int l=0; l<d.size(); l++){
                    if (GetDate.getTimeLong(googlemodelList.get(l).getModifiedTime()).equals(d.get(i))) {
                        list.add(googlemodelList.get(l));
                    }
                }

            }
            if (getActivity()!=null){
                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        }).start();


    }

    private void za_setup() {
        Collections.sort(list, (o1, o2) -> o2.getName().compareToIgnoreCase(o1.getName()));
        adapter.notifyDataSetChanged();
    }

    private void az_setup() {
        Collections.sort(list, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        adapter.notifyDataSetChanged();
    }

    String getDownloadlink(String id) {
        return "https://www.googleapis.com/drive/v3/files/" + id + "?alt=media&key=AIzaSyAqZV8HLqax1m5MOPK4Ix4mjvJh6k14DXI";
    }
    void gotoDownloadActivity(){
        goto_download_activity_btn.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(),DownloadActivity.class);
            startActivity(intent);
        });
    }
}