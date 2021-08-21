package com.example.s_tools.chatting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.s_tools.JwtModel;
import com.example.s_tools.MyWebService;
import com.example.s_tools.R;
import com.example.s_tools.Splash_login_reg.SplashScreen;
import com.example.s_tools.chatting.messageModel.MessageModel;
import com.example.s_tools.main_activity.mainactivity_toolbar_api.ChangeNameModel;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kotlin.jvm.Synchronized;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies.CU_MA;
import static com.example.s_tools.tools.Cvalues.ERROR;
import static com.example.s_tools.tools.Cvalues.STATUS;

public class ChatkaroApi {

    public static final String OK="ok";
    public static final String NO="no";
    public static final String CHATID="chatid";
    static MyWebChat myWebChat=MyWebChat.buddymsg.create(MyWebChat.class);
    static Lock lock = new ReentrantLock();

    public interface ApiCallback {
        void onResponse(boolean success, String token);
    }

    public interface ApiCallback2 {
        void onResponse(boolean success);
    }

    public interface ApiCallbackcheck {
        void onResponse(boolean authReq, boolean invalidtoken);
    }

    public interface ApiCallbackChatBlocked {
        void onResponse(boolean success, String ischatBlocked);
    }

    public interface ApiCallbackchatid {
        void onResponse(boolean success, int chatid);
    }

    public interface ApiCallbackMessage {
        void onResponse(boolean success, List<MessageModel> model);
    }

    public interface ApiCallbackMessage2 {
        void onResponse(boolean success, List<MessageModel> model, int pos);
    }

    public interface ApiCallbackchatCount {
        void onResponse(boolean success, int count);
    }

    public static void getToken(Context context, String username, String password, ApiCallback apiCallback) {
        MyWebChat webChat=MyWebChat.hwttoken.create(MyWebChat.class);
        webChat.getToken(username, password).enqueue(new Callback<JwtModel>() {
            @Override
            public void onResponse(Call<JwtModel> call, Response<JwtModel> response) {
                if (response.body() != null && response.body().getToken() != null) {
                    apiCallback.onResponse(true, response.body().getToken());
                }
            }

            @Override
            public void onFailure(Call<JwtModel> call, Throwable t) {
                ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                apiCallback.onResponse(false, null);
            }
        });
    }

    public static void firstMessage(Context context, int pos, String token, String message, ApiCallbackMessage2 apiCallback) {
        myWebChat.getFirstMessage("Bearer " + token, 1, message).enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                try {
                    if (response.body() != null && response.body().get(0) != null) {
                        apiCallback.onResponse(true, response.body(), pos);
                    } else {
                        apiCallback.onResponse(true, null, pos);
                    }
                } catch (Exception e) {
                    apiCallback.onResponse(false, null, pos);
                }
            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                apiCallback.onResponse(false, null, pos);

            }
        });
    }

    public static void getThreadMessage(int position, String token, String message, int id, ApiCallbackMessage2 apiCallback) {
        myWebChat.getThreadMessage("Bearer " + token, 1, message, id).enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
//                try {
//                    if (response.body().get(0)!=null){
//                        apiCallback.onResponse(true,response.body(),position);
//                    }else {
//                        Log.e(TAG, "onResponse: one" );
//                        apiCallback.onResponse(false,null,position);
//                    }
//                }catch (Exception e){
//                    Log.e(TAG, "onResponse:catch "+e.getMessage() );
//                    apiCallback.onResponse(false,null,position);
//                }
                if (response.body().get(0) != null) {
                    apiCallback.onResponse(true, response.body(), position);
                } else {
                    apiCallback.onResponse(false, null, position);
                }

            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                apiCallback.onResponse(false, null, position);
            }
        });
    }
    @Synchronized
    public void sendMessage(Context context, String token, String message, int chatid, int position,@NonNull ApiCallbackMessage2 apiCallback) {
        myWebChat.getThreadMessage("Bearer " + token, 1, message, chatid).enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                if (response.body() != null) {
                    apiCallback.onResponse(true, response.body(), position);
                } else {
                    MySharedPref.putTokenMessages(context, null);
                    apiCallback.onResponse(false, null, position);
                }

            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                apiCallback.onResponse(false, null, position);
            }
        });
    }

    public void troubleShooting(Context context, String token, int chatid, ApiCallback apiCallback) {
        myWebChat.isHwtExpired("Bearer " + token, MySharedPref.getSenderID(context)).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (!response.isSuccessful()) {
                    MyWebChat webChat=MyWebChat.hwttoken.create(MyWebChat.class);
                    webChat.getToken(MySharedPref.getUsername(context), MySharedPref.getPassword(context)).enqueue(new Callback<JwtModel>() {
                        @Override
                        public void onResponse(Call<JwtModel> call, Response<JwtModel> response) {
                            if (response.body() != null && response.body().getToken() != null) {
                                MySharedPref.putTokenMessages(context, response.body().getToken());
                                apiCallback.onResponse(true, null);
                            }
                        }

                        @Override
                        public void onFailure(Call<JwtModel> call, Throwable t) {
                            apiCallback.onResponse(false,null);
                            ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                        }
                    });
                } else {
                    MyWebService myWebService=MyWebService.Companion.getRetrofit().create(MyWebService.class);
                    myWebService.deleteMetaUser(MySharedPref.getCookiee(context), "chatid", String.valueOf(chatid)).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.body() != null) {
                                if (response.body().get(STATUS).getAsString().contains(OK)) {
                                    MySharedPref.putChatid(context, 0);
                                    apiCallback.onResponse(true, null);
                                } else if (response.body().get(STATUS).getAsString().contains(ERROR)) {
                                    Dialog dialog=new Dialog(context);
                                    dialog.setContentView(R.layout.logout_dialog);
                                    ImageView button=dialog.findViewById(R.id.loginbtnPopup);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.show();
                                    button.setOnClickListener(v -> {
                                        context.startActivity(new Intent(context, SplashScreen.class));
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            if (t.getMessage().contains(CU_MA)) {
                                ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
                            } else {
                                ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                            }

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                apiCallback.onResponse(false,null);
                if (t.getMessage().contains(CU_MA)) {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
                } else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                }

            }
        });
//
    }

    public static void getThread(Context context, String token, int chatid, ApiCallbackMessage apiCallback) {
        myWebChat.getThread("Bearer " + token, chatid).enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                try {
                    if (response.body() != null) {
                        apiCallback.onResponse(true, response.body());
                    } else {
                        apiCallback.onResponse(true, null);
                    }
//                    else {
//                        Log.e(TAG, "onResponse:212 " );
//                        myWebChat.isHwtExpired("Bearer " + token, MySharedPref.getSenderID(context)).enqueue(new Callback<JsonObject>() {
//                            @Override
//                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                                if (!response.isSuccessful()) {
//                                    Log.e(TAG, "onResponse: htw token expired");
//                                    MyWebChat webChat=MyWebChat.hwttoken.create(MyWebChat.class);
//                                    webChat.getToken(MySharedPref.getUsername(context), MySharedPref.getPassword(context)).enqueue(new Callback<JwtModel>() {
//                                        @Override
//                                        public void onResponse(Call<JwtModel> call, Response<JwtModel> response) {
//                                            if (response.body() != null && response.body().getToken() != null) {
//                                                Log.e(TAG, "onResponse: hwt new generated");
//                                                MySharedPref.putTokenMessages(context, response.body().getToken());
//                                                apiCallback.onResponse(true, null);
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<JwtModel> call, Throwable t) {
//                                            Log.e(TAG, "onFailure:1 "+t.getMessage() );
//                                            ToastMy.errorToast(context, t.getMessage().replace(CU_MA,""),ToastMy.LENGTH_SHORT);
//                                        }
//                                    });
//                                } else {
//                                    MyWebService myWebService=MyWebService.retrofit.create(MyWebService.class);
//                                    myWebService.deleteMetaUser(MySharedPref.getCookiee(context), "chatid", String.valueOf(chatid)).enqueue(new Callback<JsonObject>() {
//                                        @Override
//                                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                                            if (response.body() != null) {
//                                                if (response.body().get(STATUS).getAsString().contains(OK)) {
//
//                                                    MySharedPref.putChatid(context,0);
//                                                    apiCallback.onResponse(true, null);
//                                                } else if (response.body().get(STATUS).getAsString().contains(ERROR)) {
//                                                    Dialog dialog=new Dialog(context);
//                                                    dialog.setContentView(R.layout.logout_dialog);
//                                                    ImageView button=dialog.findViewById(R.id.loginbtnPopup);
//                                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                                                    dialog.show();
//                                                    button.setOnClickListener(v -> {
//                                                        context.startActivity(new Intent(context, SplashScreen.class));
//                                                    });
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<JsonObject> call, Throwable t) {
//                                            Log.e(TAG, "onFailure:2 "+t.getMessage() );
//                                            if (t.getMessage().contains(CU_MA)){
//                                                ToastMy.errorToast(context, "Check Connection.",ToastMy.LENGTH_SHORT);
//                                            }else {
//                                                ToastMy.errorToast(context, t.getMessage().replace(CU_MA,""),ToastMy.LENGTH_SHORT);
//                                            }
//
//                                        }
//                                    });
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<JsonObject> call, Throwable t) {
//                                if (t.getMessage().contains(CU_MA)){
//                                    ToastMy.errorToast(context, "Check Connection.",ToastMy.LENGTH_SHORT);
//                                }else {
//                                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA,""),ToastMy.LENGTH_SHORT);
//                                }
//
//                            }
//                        });
////
//                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                if (t.getMessage().contains(CU_MA)) {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
                } else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                }
                apiCallback.onResponse(false, null);
            }
        });
    }

    public static void deleteChatid_Token(Context context, String token, int chatid, ApiCallbackcheck apiCallback) {
        myWebChat.isChatidAvailable("Bearer " + token, chatid).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    if (response.body().toString().contains("bp_rest_authorization_required")) {
                        MyWebService myWebService=MyWebService.Companion.getRetrofit().create(MyWebService.class);
                        myWebService.deleteMetaUser(MySharedPref.getCookiee(context), "chatid", String.valueOf(chatid)).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.body().get("deleted").getAsBoolean()) {
                                    apiCallback.onResponse(true, false);
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                if (t.getMessage().contains(CU_MA)) {
                                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
                                } else {
                                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                                }


                            }
                        });
                    } else if (response.body().toString().contains("jwt_auth_invalid_token")) {
                        MyWebChat webChat=MyWebChat.hwttoken.create(MyWebChat.class);
                        webChat.getToken(MySharedPref.getUsername(context), MySharedPref.getPassword(context)).enqueue(new Callback<JwtModel>() {
                            @Override
                            public void onResponse(Call<JwtModel> call, Response<JwtModel> response) {
                                if (response.body() != null && response.body().getToken() != null) {
                                    MySharedPref.putTokenMessages(context, response.body().getToken());
                                    apiCallback.onResponse(false, true);
                                }
                            }

                            @Override
                            public void onFailure(Call<JwtModel> call, Throwable t) {
                                if (t.getMessage().contains(CU_MA)) {
                                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
                                } else {
                                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
            }
        });
    }

//    public static void checkMetaVar(Context context, ApiCallbackChatBlocked apiCallback) {
//        MyWebService myWebService=MyWebService.retrofit.create(MyWebService.class);
//        myWebService.getUserMeta(MySharedPref.getCookiee(context)).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                try {
//                    String status=response.body().get(STATUS).getAsString();
//
//                    if (status.contains(OK)) {
//                        if (response.body().get(ISCHATBLOCK) == null) {
//                            myWebService.updateUserMeta(MySharedPref.getCookiee(context), ISCHATBLOCK, NO).enqueue(new Callback<ChangeNameModel>() {
//                                @Override
//                                public void onResponse(Call<ChangeNameModel> call, Response<ChangeNameModel> response) {
//                                    if (response.body().getStatus().contains(OK)) {
//                                        MySharedPref.putChatBlock(context, NO);
//                                        apiCallback.onResponse(true, NO);
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ChangeNameModel> call, Throwable t) {
//                                    if (t.getMessage().contains(CU_MA)) {
//                                        ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
//                                    } else {
//                                        ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
//                                    }
//                                }
//                            });
//                        } else {
//                            MySharedPref.putChatBlock(context, response.body().get(ISCHATBLOCK).getAsString());
//                            apiCallback.onResponse(true, response.body().get(ISCHATBLOCK).getAsString());
//                        }
//                    } else if (status.contains(ERROR)) {
//                        apiCallback.onResponse(false, null);
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                if (t.getMessage().contains(CU_MA)) {
//                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
//                } else {
//                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
//                }
//            }
//        });
//    }

    public void checkMetaforChatID(Context context, ApiCallbackchatid apiCallback) {
        MyWebService myWebService=MyWebService.Companion.getRetrofit().create(MyWebService.class);
        myWebService.getUserMeta2(MySharedPref.getCookiee(context)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    String status=response.body().get(STATUS).getAsString();

                    if (status.contains(OK)) {
                        if (response.body().has(CHATID)) {
                            apiCallback.onResponse(true, response.body().get(CHATID).getAsInt());
                        } else {
                            apiCallback.onResponse(true, 0);
                        }
                    }else {
                        apiCallback.onResponse(false,-1);
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t.getMessage().contains(CU_MA)) {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
                } else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                }

            }
        });
    }

    public static void putchatid_Meta(Context context, int chatid, ApiCallback2 apiCallback) {
        MyWebService myWebService=MyWebService.Companion.getRetrofit().create(MyWebService.class);
        myWebService.updateUserMeta3(MySharedPref.getCookiee(context), "chatid", String.valueOf(chatid)).enqueue(new Callback<ChangeNameModel>() {
            @Override
            public void onResponse(Call<ChangeNameModel> call, Response<ChangeNameModel> response) {
                try {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals(OK)) {
                            apiCallback.onResponse(true);
                        } else {
                            apiCallback.onResponse(false);
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<ChangeNameModel> call, Throwable t) {
                if (t.getMessage().contains(CU_MA)) {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
                } else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                }

            }
        });
    }

    public static void getMessageCount(Context context, String token, int chatid, ApiCallbackchatCount apiCallback) {
        myWebChat.getThread("Bearer " + token, chatid).enqueue(new Callback<List<MessageModel>>() {
            @Override
            public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                if (response.body() != null) {
                    try {
                        if (response.body().get(0) != null) {
                            apiCallback.onResponse(true, response.body().get(0).getMessages().size());
                        }
                    } catch (Exception e) {
                    }
                }

            }

            @Override
            public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                if (t.getMessage().contains(CU_MA)) {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT);
                } else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA, ""), ToastMy.LENGTH_SHORT);
                }
            }
        });

    }


}
