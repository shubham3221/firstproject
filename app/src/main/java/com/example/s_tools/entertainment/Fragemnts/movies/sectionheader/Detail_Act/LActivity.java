package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.Detail_Act;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.s_tools.R;
import com.example.s_tools.Splash_after_reg;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;


public class LActivity extends AppCompatActivity {
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l);
        linearLayout = findViewById(R.id.myltoolbar);
        if (MySharedPref.isVersionOut(LActivity.this)) {
            ToastMy.successToast(LActivity.this,"Update found!",ToastMy.LENGTH_SHORT);
            startActivity(new Intent(LActivity.this, Splash_after_reg.class));
            finishAffinity();
        } else {
            LFragment someFragment=new LFragment(
                    getIntent().getIntExtra("id",0),
                    getIntent().getStringArrayListExtra("drive720"),
                    getIntent().getStringArrayListExtra("drive1080"),
                    getIntent().getStringArrayListExtra("other"),getIntent().getStringArrayListExtra("watchonline"));
            someFragment.setEnterTransition(new Fade());
            someFragment.setExitTransition(new Fade());
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.lcontainer, someFragment);
            
            fragmentTransaction.commit();
        }
    }
}