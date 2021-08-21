package com.example.s_tools.tools.retrofitcalls

import android.content.Context
import android.content.SharedPreferences
import com.securepreferences.SecurePreferences
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import com.example.s_tools.MyWebService
import com.example.s_tools.Splash_login_reg.CheckCookieValid
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies
import com.example.s_tools.tools.ToastMy
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MoviesPosts
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

object MySharedPref {
    const val activity = "@5363372399S"
    const val SYSTEMCR_XML = "\$Systemcr.xml"
    const val PostFilename = "\$Systeml.xml"
    const val COOKIE = "cookie"
    const val CHATID = "chatid"
    const val USERNAME = "username"
    const val PASSWORD_string = "password"
    const val ID = "id"
    const val MSG_TOKEN = "msgToken"
    const val FIRSTNAME = "firstname"
    const val EMAIL = "email"
    const val CHATBLOCKED = "chatblocked"
    const val MOVIERUSHURL = "movierushurl"
    const val GETWALL_1 = "getwall1"
    const val GETWALL_2 = "getwall2"
    const val YOUTUBETOMP_3 = "youtubetomp3"
    const val RECENT = "recentmovies"
    const val NETFLIX = "netflixmovies"
    const val BOLLYWOOD = "bollywoodmovies"
    const val HOLLYWOOD = "hollywoodmovies"
    const val OTHERS = "othersmovies"
    const val SLIDERIMAGES = "sliderimages"
    const val ISVERSIONOUT = "isversionout"
    const val YOUTUBEPART_1 = "youtubepart1"
    const val YOUTUBEPART_2 = "youtubepart2"
    @JvmStatic
    fun putLoginData(
        context: Context?,
        id: Int,
        cookie: String?,
        username: String?,
        firstname: String?,
        email: String?,
        password: String?
    ) {
        val sharedPreferences: SharedPreferences =
            SecurePreferences(context, activity, SYSTEMCR_XML)
        val editor = sharedPreferences.edit()
        editor.putInt(ID, id)
        editor.putString(COOKIE, cookie)
        editor.putString(USERNAME, username)
        editor.putString(FIRSTNAME, firstname)
        editor.putString(EMAIL, email)
        editor.putString(PASSWORD_string, password)
        editor.apply()
    }

    fun isValidCookie(context: Context?, callback: ApiCallback) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        val cookie = sharedPreferences.getString(COOKIE, null)
        if (cookie == null) {
            callback.onResponse(false)
            return
        }
        val myWebService = MyWebService.retrofit.create(MyWebService::class.java)
        myWebService.isCookieValid(cookie)!!.enqueue(object : Callback<CheckCookieValid?> {
            override fun onResponse(
                call: Call<CheckCookieValid?>,
                response: Response<CheckCookieValid?>
            ) {
                try {
                    val valid = response.body()!!.isValid
                    callback.onResponse(valid)
                } catch (e: Exception) {
                    callback.onResponse(false)
                }
            }

            override fun onFailure(call: Call<CheckCookieValid?>, t: Throwable) {
                if (t.message!!.contains(MyMovies.CU_MA)) {
                    ToastMy.errorToast(context, "Check Connection.", ToastMy.LENGTH_SHORT)
                } else {
                    ToastMy.errorToast(
                        context,
                        t.message!!.replace(MyMovies.CU_MA, ""),
                        ToastMy.LENGTH_SHORT
                    )
                }
                callback.onResponse(false)
            }
        })
    }

    @JvmStatic
    fun getName(context: Context?): String? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getString(FIRSTNAME, "Hi Guest!")
    }

    @JvmStatic
    fun putName(context: Context?, name: String?) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        val editor = sharedPreferences.edit()
        editor.putString(FIRSTNAME, name)
        editor.apply()
    }

    @JvmStatic
    fun isSharedPrefnull(context: Context?): Boolean {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        val cookie = sharedPreferences.getString(COOKIE, null) ?: return true
        return false
    }
    @JvmStatic
    fun getCookiee(context: Context?): String? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getString(COOKIE, null) ?: return null
    }

    @JvmStatic
    fun clearLogin(context: Context?) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        sharedPreferences.edit().clear().apply()
    }

    @JvmStatic
    fun putVersioninfo(context: Context?, isOut: Boolean) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        sharedPreferences.edit().putBoolean(ISVERSIONOUT, isOut).apply()
    }

    @JvmStatic
    fun isVersionOut(context: Context?): Boolean {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getBoolean(ISVERSIONOUT, false)
    }
    @JvmStatic
    fun putTokenMessages(context: Context?, msgToken: String?) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        sharedPreferences.edit().putString(MSG_TOKEN, msgToken).apply()
    }

    @JvmStatic
    fun getTokenMessages(context: Context?): String? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getString(MSG_TOKEN, null)
    }
    @JvmStatic
    fun getSenderID(context: Context?): Int {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getInt(ID, 0)
    }

    @JvmStatic
    fun getUsername(context: Context?): String? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getString(USERNAME, null)
    }

    @JvmStatic
    fun getPassword(context: Context?): String? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getString(PASSWORD_string, null)
    }

    fun putChatBlock(context: Context?, yesorno: String?) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        sharedPreferences.edit().putString(CHATBLOCKED, yesorno).apply()
    }

    fun getChatBlockStatus(context: Context?): String? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getString(CHATBLOCKED, null)
    }

    fun getChatid(context: Context?): Int {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getInt("chatid", 0)
    }

    @JvmStatic
    fun putChatid(context: Context?, chatid: Int) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        sharedPreferences.edit().putInt("chatid", chatid).apply()
    }

    //movie
    @JvmStatic
    fun getMovieUrl(context: Context?): String? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getString(MOVIERUSHURL, null)
    }

    @JvmStatic
    fun putMovieUrl(context: Context?, url: String?) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        sharedPreferences.edit().putString(MOVIERUSHURL, url).apply()
    }

    //wallpaper
    @JvmStatic
    fun getwall1(context: Context?): String? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getString(GETWALL_1, null)
    }

    @JvmStatic
    fun getwall2(context: Context?): String? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getString(GETWALL_2, null)
    }

    @JvmStatic
    fun putwall1(context: Context?, url: String?) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        sharedPreferences.edit().putString(GETWALL_1, url).apply()
    }

    @JvmStatic
    fun putwall2(context: Context?, url: String?) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        sharedPreferences.edit().putString(GETWALL_2, url).apply()
    }

    @JvmStatic
    fun getyoutubemp3(context: Context?): String? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getString(YOUTUBETOMP_3, null)
    }

    @JvmStatic
    fun putyoutubemp3(context: Context?, url: String?) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        sharedPreferences.edit().putString(YOUTUBETOMP_3, url).apply()
    }

    @JvmStatic
    fun getyoutubeVideo_part1(context: Context?): String? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getString(YOUTUBEPART_1, null)
    }

    @JvmStatic
    fun putyoutubeVideo_part1(context: Context?, url: String?) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        sharedPreferences.edit().putString(YOUTUBEPART_1, url).apply()
    }

    @JvmStatic
    fun getyoutubeVideo_part2(context: Context?): String? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        return sharedPreferences.getString(YOUTUBEPART_2, null)
    }

    @JvmStatic
    fun putyoutubeVideo_part2(context: Context?, url: String?) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        sharedPreferences.edit().putString(YOUTUBEPART_2, url).apply()
    }

    @JvmStatic
    fun getMoviesList(context: Context?, category: String?): List<MoviesPosts>? {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        val gson = Gson()
        val json = sharedPreferences.getString(category, null)
        val type = object : TypeToken<List<MoviesPosts?>?>() {}.type
        return gson.fromJson(json, type)
    }

    @JvmStatic
    fun putMoviesList(context: Context?, list: List<MoviesPosts?>?, category: String?) {
        val sharedPreferences: SharedPreferences = SecurePreferences(
            context, activity,
            SYSTEMCR_XML
        )
        val gson = Gson()
        val json = gson.toJson(list)
        sharedPreferences.edit().putString(category, json).apply()
    }

    interface ApiCallback {
        fun onResponse(success: Boolean)
    }
}