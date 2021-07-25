package com.example.s_tools.Splash_login_reg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;

import com.example.s_tools.R;
import com.example.s_tools.Splash_login_reg.RegisterApiCall.RegisterNewAccount;
import com.example.s_tools.Splash_login_reg.loginapi_call.LoginApiCall;
import com.example.s_tools.entertainment.VideoActivity;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.Validation;
import com.example.s_tools.tools.retrofitcalls.VC;
import com.kaopiz.kprogresshud.KProgressHUD;

public class SplashScreen extends AppCompatActivity {
    public static final int STARTUP_DELAY = 300;
    public static final int ITEM_DELAY = 300;
    public static final String TAG="mtag";
    public static final String INVALID_NAME="Invalid Name";
    public static final String INVALID_USERNAME="Invalid username";
    public static final String WAIT="Please wait..";
    public static final String REGISTER="REGISTER";
    public static final String LOGIN="LOGIN";
    EditText name,username,email,pass;
    ScrollView scrollView;
    ImageView logo;
    Button register,back_btn;
    CardView login,registerok;
    TextView registerHint,nootwelcomemsg,statusemail,statuspass,logintxt,registertxt;
    LinearLayout hintLayout;
    ProgressBar loginprogress,registerprogress;
    TextView taphere;
    View line;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
        setContentView(R.layout.splashscreen_bg);
        init();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        animate();

        new Handler().postDelayed(this::setupListner,1000);
    }

    private void setupListner() {
        login.setOnClickListener(v -> {
            statuspass.setText("");
            if (pass.getText().toString().isEmpty() || pass.getText().toString().contains(" ")){
                statuspass.setText("Not Valid Password");
                return;
            }else if (email.getText().toString().isEmpty() ||!Validation.isValidEmailId(email.getText().toString(), SplashScreen.this)){
                return;
            }
            logintxt.setText(WAIT);
            loginprogress.setVisibility(View.VISIBLE);
            hideKeyboard();
            LoginApiCall.login(SplashScreen.this, email.getText().toString(), pass.getText().toString(), success -> {
                loginprogress.setVisibility(View.GONE);
                if (!success){
                    logintxt.setText(LOGIN);
                }else {
                    putAppData();
                }
            });
        });


        //successfull register
        registerok.setOnClickListener(v -> {

            boolean validPassword=Validation.isValidPassword(pass.getText().toString(), SplashScreen.this);
            boolean validEmailId=Validation.isValidEmailId(email.getText().toString().trim(), SplashScreen.this);


            if (validEmailId && validPassword){
                if(name.getText().toString().isEmpty() || name.getText().toString().length() < 3 || name.getText().toString().contains(" ")){
                    Toast.makeText(SplashScreen.this, INVALID_NAME, Toast.LENGTH_SHORT).show();
                    registertxt.setText(REGISTER);
                    registerprogress.setVisibility(View.GONE);
                    return;
                }
                if (username.getText().toString().isEmpty() || username.getText().toString().length() < 4 || username.getText().toString().contains(" ")){
                    Toast.makeText(SplashScreen.this, INVALID_USERNAME, Toast.LENGTH_SHORT).show();
                    registertxt.setText(REGISTER);
                    registerprogress.setVisibility(View.GONE);
                    return;
                }
                hideKeyboard();
                registertxt.setText(WAIT);
                registerprogress.setVisibility(View.VISIBLE);

                //register api call
                RegisterNewAccount.registerNew(SplashScreen.this, name.getText().toString(), username.getText().toString(), email.getText().toString(), pass.getText().toString(), null, new RegisterNewAccount.ApiCallback() {
                    @Override
                    public void onResponse(boolean success) {
                        if (!success){
                            registertxt.setText(REGISTER);
                            registerprogress.setVisibility(View.GONE);
                        }else {
                            LoginApiCall.login(SplashScreen.this, email.getText().toString(), pass.getText().toString(), success1 -> {
                                if (success){
                                    ToastMy.successToast(SplashScreen.this,"Registered Successful", ToastMy.LENGTH_SHORT);
                                    startActivity(new Intent(SplashScreen.this, VideoActivity.class));
                                    finish();
                                }
                            });
                        }
                    }
                });
            }

        });

        //register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                animate();
                line.setVisibility(View.VISIBLE);
                registerHint.setVisibility(View.VISIBLE);
                logo.setVisibility(View.GONE);
                nootwelcomemsg.setVisibility(View.GONE);
                name.setVisibility(View.VISIBLE);
                hintLayout.setVisibility(View.GONE);
                registerok.setVisibility(View.VISIBLE);
                back_btn.setVisibility(View.VISIBLE);
                register.setVisibility(View.GONE);
                login.setVisibility(View.GONE);
                username.setVisibility(View.VISIBLE);
                logo.getLayoutParams().height = 230;
                logo.getLayoutParams().width = 230;
            }
        });
        //back btn
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line.setVisibility(View.GONE);
                registerHint.setVisibility(View.GONE);
                logo.setVisibility(View.VISIBLE);
                nootwelcomemsg.setVisibility(View.VISIBLE);
                name.setVisibility(View.GONE);
                hintLayout.setVisibility(View.VISIBLE);
                registerok.setVisibility(View.GONE);
                back_btn.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
                username.setVisibility(View.GONE);
                logo.getLayoutParams().height = 260;
                logo.getLayoutParams().width = 260;
            }
        });
        taphere.setOnClickListener(view -> email.setText(email.getText().append("@gmail.com")));
    }

    private void putAppData() {
        KProgressHUD kProgressHUD=KProgressHUD.create(SplashScreen.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please Wait...").setDimAmount(0.5f)
                .setCancellable(false).show();
        VC.check(SplashScreen.this, (success, updateinfo) -> {
            if (success){
                kProgressHUD.dismiss();
                startActivity(new Intent(SplashScreen.this,VideoActivity.class));
                finish();
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    private void init() {
        line = findViewById(R.id.lineview);
        taphere = findViewById(R.id.taphere);
        registerHint = findViewById(R.id.registerhint);
        name = (EditText)findViewById(R.id.name_edittext);
        username = (EditText)findViewById(R.id.username_editext);
        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);

        statuspass = (TextView) findViewById(R.id.status_password);
        nootwelcomemsg = (TextView) findViewById(R.id.noot_welcome_you);
        hintLayout = (LinearLayout)findViewById(R.id.hint_linearLayout);

        scrollView = (ScrollView)findViewById(R.id.mscrollView);
        logo = (ImageView)findViewById(R.id.img_logo);
        register = (Button)findViewById(R.id.register_btn);
        registerok = (CardView) findViewById(R.id.register_ok);
        back_btn = (Button)findViewById(R.id.back_btn_registrationform);

        login = (CardView)findViewById(R.id.login_btn);
        loginprogress = findViewById(R.id.login_ok_progressbar);
        registerprogress=findViewById(R.id.register_ok_progressbar);
        logintxt = findViewById(R.id.login_txt);
        registertxt= findViewById(R.id.register_ok_txt);
    }
    private void animate() {
        ImageView logoImageView = (ImageView) findViewById(R.id.img_logo);
        ViewGroup container = (ViewGroup) findViewById(R.id.container);

        ViewCompat.animate(logoImageView)
                .translationY(-150)
                .setStartDelay(STARTUP_DELAY)
                .setDuration(1000).setInterpolator(
                new DecelerateInterpolator(2.0f)).start();

        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            if (!(v instanceof Button)) {
                viewAnimator = ViewCompat.animate(v)
                        .translationY(0).alpha(1)

                        .setStartDelay((ITEM_DELAY)+200)
                        .setDuration(500);
            } else {
                viewAnimator = ViewCompat.animate(v)
                        .scaleY(1).scaleX(1)
                        .setStartDelay((ITEM_DELAY)+400)
                        .setDuration(500);
            }

            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        }
    }
}
