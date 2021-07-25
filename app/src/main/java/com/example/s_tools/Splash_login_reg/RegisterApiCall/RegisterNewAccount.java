package com.example.s_tools.Splash_login_reg.RegisterApiCall;

import android.content.Context;
import android.widget.Toast;

import com.example.s_tools.MyWebService;
import com.example.s_tools.Splash_login_reg.Registration_Model;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.NonceModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterNewAccount {

    public static final String HIDDENSTOOL_MA_CU="hiddenstool.ma.cu";

    public interface ApiCallback {
        void onResponse(boolean success);
    }

    public static void registerNew(Context context,String name,String username,String email
    ,String password,String desc,ApiCallback callback){
        //getting nonce
        final MyWebService myWebService=MyWebService.retrofit.create(MyWebService.class);
        myWebService.getnonce("user","register").enqueue(new Callback<NonceModel>() {
            @Override
            public void onResponse(Call<NonceModel> call, Response<NonceModel> response) {
                try {
                    final NonceModel body=response.body();
                    //after getting nonce
                    if (body.getStatus().contains("error")){
                        Toast.makeText(context, "Error Code = 2 , Contact to Admin For Query", Toast.LENGTH_SHORT).show();
                        callback.onResponse(false);
                        return;
                    }
         //registeration
                    myWebService.userRegiseration(name,username, email,password, desc,body.getNonce(),2,"no")
                            .enqueue(new Callback<Registration_Model>() {
                                @Override
                                public void onResponse(Call<Registration_Model> call, Response<Registration_Model> response) {
                                    final Registration_Model registrationModel=response.body();
                                    if (registrationModel.getError()==null){
                                        //success
                                        callback.onResponse(true);
                                    }else {
                                        callback.onResponse(false);
                                        Toast.makeText(context, registrationModel.getError(), Toast.LENGTH_LONG).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<Registration_Model> call, Throwable t) {
                                    if (t.getMessage().contains(MyMovies.CU_MA)){
                                        ToastMy.errorToast(context, "Check Connection.",ToastMy.LENGTH_SHORT);
                                    }else {
                                        ToastMy.errorToast(context, t.getMessage().replace(MyMovies.CU_MA,""),ToastMy.LENGTH_SHORT);
                                    }
                                    callback.onResponse(false);
                                }
                            });

                }catch (Exception e){
                    callback.onResponse(false);
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<NonceModel> call, Throwable t) {
                if (t.getMessage().contains(MyMovies.CU_MA)){
                    ToastMy.errorToast(context, "Check Connection.",ToastMy.LENGTH_SHORT);
                }else {
                    ToastMy.errorToast(context, t.getMessage().replace(MyMovies.CU_MA,""),ToastMy.LENGTH_SHORT);
                }
                callback.onResponse(false);
            }
        });

    }
}
