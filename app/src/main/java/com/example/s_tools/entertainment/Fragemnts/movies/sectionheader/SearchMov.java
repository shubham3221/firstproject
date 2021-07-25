package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.Detail_Act.ScFragment;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MoviesPosts;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies;
import com.example.s_tools.tools.CustomScrollingIconChange;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.ToastMy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.alexbykov.nopaginate.paginate.NoPaginate;

public class SearchMov extends AppCompatActivity implements ScFragment.HideToolbar {
    public int mpage=1;
    public static final String SORRY_TOO_SHORT="Sorry! Too Short";
    EditText editText;
    ImageView cutbtn;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    AllAdapter adapter;
    NoPaginate scrolling;
    List<MoviesPosts> list=new ArrayList<>();
    LinearLayout linearLayout, searchEdittext;
    private boolean loading=false;
    CardView cardView;
    ImageView bgimage;
    private int totalPost=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_testing);
        init();

        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (textView.getText().length() >= 4) {
                    editText.setFocusableInTouchMode(true);
                    bgimage.setBackground(null);
                    cutbtn.setVisibility(View.GONE);
                    editText.setCursorVisible(false);
                    editText.setTextColor(getResources().getColor(R.color.white_hint));
                    int count=getSupportFragmentManager().getBackStackEntryCount();
                    if (count != 0) {
                        while (count != 0) {
                            getSupportFragmentManager().popBackStack();
                            count-=1;
                        }
                    }
                    loading=false;
                    mpage=1;
                    totalPost=0;
                    list.clear();
                    if (scrolling != null) {
                        scrolling.unbind();
                    }
                    scrolling=NoPaginate.with(recyclerView).setOnLoadMoreListener(() -> {
                        if (!loading) {
                            loading=true;
                            fetch(editText.getText().toString(), mpage);
                        }

                    }).setCustomLoadingItem(new CustomScrollingIconChange()).build();
                } else {
                    ToastMy.errorToast(SearchMov.this, SORRY_TOO_SHORT, ToastMy.LENGTH_SHORT);
                }
            }
            return false;
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    if (adapter == null) {
                        setupRecycler();
                    }
                    cutbtn.setVisibility(View.VISIBLE);
                } else {
                    cutbtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        editText.setOnClickListener(view -> {
            editText.setCursorVisible(true);
            editText.setTextColor(getResources().getColor(R.color.white));
        });
        cutbtn.setOnClickListener(view -> {
            editText.setText("");
            cutbtn.setVisibility(View.GONE);
        });
        Picasso.get().load("https://imagetot.com/images/2020/11/21/508594b11c312d518c7416e3ae3a5c4a.jpg").resize(500, 500).into(bgimage);
    }

    private void setupRecycler() {
        adapter=new AllAdapter(true, list, SearchMov.this);
        gridLayoutManager=new GridLayoutManager(SearchMov.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    private void fetch(String toString, int page) {
        MyMovies.getSearchMovies(SearchMov.this, toString, 10, page, model -> {
            if (model == null) {
                scrolling.setNoMoreItems(true);
                return;
            }
            for (int i=0; i < model.getPosts().size(); i++) {
                if (!model.getPosts().get(i).getCategories().isEmpty()) {
                    if (Cvalues.Category == 41) {
                        list.add(model.getPosts().get(i));
                        totalPost+=1;
                    } else {
                        if (model.getPosts().get(i).getCategories().get(0).getParent() == 36)
                            list.add(model.getPosts().get(i));
                        totalPost+=1;
                    }

                }
            }
            if (mpage == 1) {
                if (totalPost == 0) {
                    ToastMy.successToast(SearchMov.this, totalPost + " result found!", ToastMy.LENGTH_SHORT);
                } else {
                    ToastMy.successToast(SearchMov.this, "got " + totalPost + " result!", ToastMy.LENGTH_SHORT);
                }
            }
            adapter.notifyDataSetChanged();
            mpage++;
            if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == model.getCount_total() - 1) {
                scrolling.setNoMoreItems(true);
            }
            loading=false;
        });
    }


    private void init() {
        linearLayout=findViewById(R.id.searchmovlayout);
        editText=findViewById(R.id.searchedit);
        cutbtn=findViewById(R.id.cutbtn);
        recyclerView=findViewById(R.id.searchrecy);
        cardView=findViewById(R.id.expendedcarf);
        searchEdittext=findViewById(R.id.searchLl);
        cardView=findViewById(R.id.hidecard);
        bgimage=findViewById(R.id.bgimage);
    }


    @Override
    public void hideToolbar() {
        searchEdittext.setVisibility(View.GONE);
    }

    @Override
    public void showToolbar() {
        searchEdittext.setVisibility(View.VISIBLE);
    }
}