package com.example.s_tools.tools.retrofitcalls;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.s_tools.MyWebService;
import com.example.s_tools.Splash_login_reg.CheckCookieValid;
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MoviesPosts;
import com.example.s_tools.tools.ToastMy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.securepreferences.SecurePreferences;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies.CU_MA;

public class MySharedPref {

    public static final String activity="@5363372399S";
    public static final String SYSTEMCR_XML="$Systemcr.xml";
    public static final String PostFilename="$Systeml.xml";
    public static final String COOKIE="cookie";
    public static final String CHATID="chatid";
    public static final String USERNAME="username";
    public static final String PASSWORD_string="password";
    public static final String ID="id";
    public static final String MSG_TOKEN="msgToken";
    public static final String FIRSTNAME="firstname";
    public static final String EMAIL="email";
    public static final String CHATBLOCKED="chatblocked";
    public static final String MOVIERUSHURL="movierushurl";
    public static final String GETWALL_1="getwall1";
    public static final String GETWALL_2="getwall2";
    public static final String YOUTUBETOMP_3="youtubetomp3";
    public static final String RECENT="recentmovies";
    public static final String NETFLIX="netflixmovies";
    public static final String BOLLYWOOD="bollywoodmovies";
    public static final String HOLLYWOOD="hollywoodmovies";
    public static final String OTHERS="othersmovies";
    public static final String SLIDERIMAGES="sliderimages";
    public static final String ISVERSIONOUT="isversionout";
    public static final String YOUTUBEPART_1="youtubepart1";
    public static final String YOUTUBEPART_2="youtubepart2";


    public interface ApiCallback {
        void onResponse(boolean success);
    }
    public static void putLoginData(Context context,int id,String cookie,String username,String firstname,String email,String password){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity, SYSTEMCR_XML);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ID,id);
        editor.putString(COOKIE,cookie);
        editor.putString(USERNAME,username);
        editor.putString(FIRSTNAME,firstname);
        editor.putString(EMAIL,email);
        editor.putString(PASSWORD_string,password);
        editor.apply();
    }
    public static void isValidCookie(Context context, final ApiCallback callback){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        String cookie=sharedPreferences.getString(COOKIE, null);
        if (cookie==null){
            callback.onResponse(false);
            return;
        }
        MyWebService myWebService = MyWebService.retrofit.create(MyWebService.class);
        myWebService.isCookieValid(cookie).enqueue(new Callback<CheckCookieValid>() {
            @Override
            public void onResponse(Call<CheckCookieValid> call, Response<CheckCookieValid> response) {
                try {
                    boolean valid=response.body().isValid();
                    callback.onResponse(valid);
                }catch (Exception e){
                    callback.onResponse(false);
                }

            }

            @Override
            public void onFailure(Call<CheckCookieValid> call, Throwable t) {
                if (t.getMessage().contains(CU_MA)){
                    ToastMy.errorToast(context, "Check Connection.",ToastMy.LENGTH_SHORT);
                }else {
                    ToastMy.errorToast(context, t.getMessage().replace(CU_MA,""),ToastMy.LENGTH_SHORT);
                }
                callback.onResponse(false);
            }
        });
    }
    public static String getName(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        String m=sharedPreferences.getString(FIRSTNAME, "Hi Guest!");
        return m;
    }
    public static void putName(Context context,String name){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIRSTNAME,name);
        editor.apply();
    }
    public static boolean isSharedPrefnull(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        String cookie=sharedPreferences.getString(COOKIE, null);
        if (cookie==null){
            return true;
        }
        return false;
    }
    public static String getCookiee(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        String cookie=sharedPreferences.getString(COOKIE, null);
        if (cookie==null){
            return null;
        }
        return cookie;
    }

    public static void clearLogin(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        sharedPreferences.edit().clear().apply();
    }
    public static void putVersioninfo(Context context,boolean isOut){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        sharedPreferences.edit().putBoolean(ISVERSIONOUT,isOut).apply();
    } public static boolean isVersionOut(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getBoolean(ISVERSIONOUT,false);
    }

    public static void putTokenMessages(Context context, String msgToken){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        sharedPreferences.edit().putString(MSG_TOKEN,msgToken).apply();
    }
    public static String getTokenMessages(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getString(MSG_TOKEN,null);
    }
    public static int getSenderID(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getInt(ID,0);
    }
    public static String getUsername(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getString(USERNAME,null);
    }
    public static String getPassword(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getString(PASSWORD_string,null);
    }
    public static void putChatBlock(Context context,String yesorno){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        sharedPreferences.edit().putString(CHATBLOCKED,yesorno).apply();
    }
    public static String getChatBlockStatus(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getString(CHATBLOCKED,null);
    }
    public static int getChatid(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getInt("chatid",0);
    }
    public static void putChatid(Context context,int chatid){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        sharedPreferences.edit().putInt("chatid",chatid).apply();
    }

    //movie
    public static String getMovieUrl(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getString(MOVIERUSHURL,null);
    }
    public static void putMovieUrl(Context context,String url){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        sharedPreferences.edit().putString(MOVIERUSHURL,url).apply();
    }
    //wallpaper
    public static String getwall1(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getString(GETWALL_1,null);
    }
    public static String getwall2(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getString(GETWALL_2,null);
    }
    public static void putwall1(Context context,String url){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        sharedPreferences.edit().putString(GETWALL_1,url).apply();
    }
    public static void putwall2(Context context,String url){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        sharedPreferences.edit().putString(GETWALL_2,url).apply();
    }


    public static String getyoutubemp3(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getString(YOUTUBETOMP_3,null);
    }
    public static void putyoutubemp3(Context context,String url){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        sharedPreferences.edit().putString(YOUTUBETOMP_3,url).apply();
    }

    public static String getyoutubeVideo_part1(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getString(YOUTUBEPART_1,null);
    }
    public static void putyoutubeVideo_part1(Context context,String url){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        sharedPreferences.edit().putString(YOUTUBEPART_1,url).apply();
    }
    public static String getyoutubeVideo_part2(Context context){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        return sharedPreferences.getString(YOUTUBEPART_2,null);
    }
    public static void putyoutubeVideo_part2(Context context,String url){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        sharedPreferences.edit().putString(YOUTUBEPART_2,url).apply();
    }

    public static List<MoviesPosts> getMoviesList(Context context, String category){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(category,null);
        Type type = new TypeToken<List<MoviesPosts>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public static void putMoviesList(Context context, List<MoviesPosts> list, String category){
        SharedPreferences sharedPreferences =new SecurePreferences(context, activity,
                SYSTEMCR_XML);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        sharedPreferences.edit().putString(category,json).apply();
    }



}
