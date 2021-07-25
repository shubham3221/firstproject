package com.example.s_tools.Splash_login_reg.loginapi_call;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.s_tools.MyWebService;
import com.example.s_tools.R;
import com.example.s_tools.Splash_login_reg.LoginModel;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginApiCall {


    public interface ApiCallback {
        void onResponse(boolean success);
    }
    public static void login(Context context,String email,String pass,final ApiCallback callback){
        MyWebService myWebService = MyWebService.retrofit.create(MyWebService.class);
        myWebService.loginApi(email,pass).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.body().getError() == null){
                    MySharedPref.putLoginData(context,response.body().getUserData().getId(),
                            response.body().getCookie(),response.body().getUserData().getUsername(),
                            response.body().getUserData().getFirstname(),response.body().getUserData().getEmail(),pass);
                    callback.onResponse(true);
                }else {
                    callback.onResponse(false);
                    if (response.body().getError().contains("password")){
                        Toast.makeText(context, "Invalid Password", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(context, "Invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                callback.onResponse(false);
                if (t.getMessage().contains("certificate")){
                    Toast.makeText(context, "Server error.Please Visit buddymy.blogspot.com", Toast.LENGTH_SHORT).show();

                }else {
                    Dialog dialog=new Dialog(context);
                    dialog.setContentView(R.layout.movietimeout);
                    Button button=dialog.findViewById(R.id.dismissbtn);
                    TextView textView = dialog.findViewById(R.id.errorText);
                    textView.setText("Unable to receive data. try after some tome");
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                    dialog.show();
                    button.setOnClickListener(v -> {
                        dialog.dismiss();
                    });
                }
            }
        });
    }
}
