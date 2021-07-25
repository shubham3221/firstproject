package com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallpaperOneModel;
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_two.Wallpapers_2_Model;

import java.util.List;

public class WallpaperCategorys extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<WallpaperOneModel> one;
    private List<Wallpapers_2_Model> two;
    WallpaperCategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_category_layout);
        init();

        Intent intent = getIntent();
        if (intent.hasExtra("one")){
            Bundle args = intent.getBundleExtra("one");
            one =(List<WallpaperOneModel>) args.getSerializable("one");

        }else {
            Bundle args = intent.getBundleExtra("two");
            two =(List<Wallpapers_2_Model>) args.getSerializable("two");
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new WallpaperCategoryAdapter(this,one,two);
        recyclerView.setAdapter(adapter);
    }


    private void init() {
        recyclerView = findViewById(R.id.category_recyclerview);
        toolbar = findViewById(R.id.wallpaperToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}