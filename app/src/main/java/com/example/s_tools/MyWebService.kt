package com.example.s_tools

import com.example.s_tools.premium.model.PremiumModel
import com.example.s_tools.tools.retrofitcalls.NonceModel
import com.example.s_tools.Splash_login_reg.LoginModel
import com.example.s_tools.Splash_login_reg.Registration_Model
import com.example.s_tools.main_activity.mainactivity_toolbar_api.ChangeNameModel
import com.google.gson.JsonObject
import com.example.s_tools.Splash_login_reg.CheckCookieValid
import com.example.s_tools.entertainment.Fragemnts.movies.google_dict.Modelclass
import okhttp3.ConnectionSpec
import okhttp3.TlsVersion
import okhttp3.OkHttpClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import com.example.s_tools.Splash_after_reg
import com.example.s_tools.NetworkHelper
import okhttp3.CertificatePinner
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface MyWebService {
    var userMeta2: String

    //main premium (id means catagory)
    @GET(get_catagory_post)
    fun getPremiumPost(
        @Query(ID) id: Int,
        @Query(COUNT) count: Int,
        @Query("page") page: Int
    ): Call<PremiumModel?>?

    //getnonce
    @FormUrlEncoded
    @POST(GET_NONCE)
    fun getnonce(
        @Field(CONTROLLER) controller: String?,
        @Field(METHOD) method: String?
    ): Call<NonceModel?>?

    //search
    @POST(SEARCHIT)
    fun searchPosts(@Query(SEARCH) search: String?): Call<PremiumModel?>?

    //login
    @FormUrlEncoded
    @POST(USER_GENERATE_AUTH_COOKIE)
    fun loginApi(
        @Field(EMAIL) email: String?,
        @Field(PASSWORD) password: String?
    ): Call<LoginModel?>?

    //    Call<LoginModel> loginApi(@Field(EMAIL) String email, @Field(PASSWORD) String password , @Field(SECONDS) int seconds);
    //register post
    @FormUrlEncoded
    @POST(REGISTRATION)
    fun userRegiseration(
        @Field(FIRST_NAME) firstname: String?,
        @Field(USERNAME) username: String?,
        @Field(
            EMAIL
        ) email: String?,
        @Field(USER_PASS) pass: String?,
        @Field(DESCRIPTION) description: String?,
        @Field(
            NONCE
        ) nonce: String?,
        @Field(SECONDS) second: Int,
        @Field(NOTIFY) notify: String?
    ): Call<Registration_Model?>?

    //change name
    @FormUrlEncoded
    @POST(USER_UPDATE_USER_META)
    fun updateUserMeta(
        @Field(COOKIE) cookie: String?, @Field(META_KEY) key: String?, @Field(
            META_VALUE
        ) value: String?
    ): ChangeNameModel?
    //change name

    @FormUrlEncoded
    @POST(USER_UPDATE_USER_META)
    fun updateUserMeta3(
        @Field(COOKIE) cookie: String?, @Field(META_KEY) key: String?, @Field(
            META_VALUE
        ) value: String?
    ): Call<ChangeNameModel?>

    //change name
    @FormUrlEncoded
    @POST(USER_UPDATE_USER_META)
    fun updateUserMeta2(
        @Field(COOKIE) cookie: String?, @Field(META_KEY) key: String?, @Field(
            META_VALUE
        ) value: String?
    ): Call<ChangeNameModel?>?

    @POST(PAGE)
    fun checkVersion(): Call<JsonObject?>?

    @FormUrlEncoded
    @POST(USER_VALIDATE_AUTH_COOKIE)
    fun isCookieValid(@Field(COOKIE) cookie: String?): Call<CheckCookieValid?>?

    @FormUrlEncoded
    @POST(USER_META_VARS)
    fun updateUserMetaValue(
        @Field(COOKIE) cookie: String?,
        @Field(TOKEN) value: String?
    ): Call<JsonObject?>?

    @FormUrlEncoded
    @POST(GET_USER_META_VARS)
    suspend fun getUserMeta(@Field(COOKIE) cookie: String?): JsonObject?
    @FormUrlEncoded
    @POST(GET_USER_META_VARS)
    fun getUserMeta2(@Field(COOKIE) cookie: String?): Call<JsonObject?>

    @FormUrlEncoded
    @POST(GET_USER_META_VARS)
    fun getUserMetaWithCall(@Field(COOKIE) cookie: String?): Call<JsonObject?>?

    @FormUrlEncoded
    @POST(DELETE_META)
    fun deleteMetaUser(
        @Field(COOKIE) cookie: String?, @Field(META_KEY) key: String?, @Field(
            META_VALUE
        ) value: String?
    ): Call<JsonObject?>?

    //    @GET("?q=%27{folderid}%27+in+parents&key={key}&pageSize=400")
    //    Call<MyModel> getFiles(@Path("folderid") String folderid, @Path("key") String key);
    //    @GET("files?q=1_xdvfNDAvlFB_5hnF0RPwz9H56u33PQv%20in%20parents&key=AIzaSyAqZV8HLqax1m5MOPK4Ix4mjvJh6k14DXI&supportsAllDrives=true&supportsTeamDrives=true&includeItemsFromAllDrives=true&fields=files(thumbnailLink,name,id,fileExtension,size,webContentLink,mimeType,createdTime,modifiedTime),nextPageToken&orderBy=modifiedTime,folder&pageSize=500")
    //    Call<MyModel> getFiles();
    @GET("files?key=AIzaSyAqZV8HLqax1m5MOPK4Ix4mjvJh6k14DXI&supportsAllDrives=true&supportsTeamDrives=true&includeItemsFromAllDrives=true&fields=files(thumbnailLink,name,id,fileExtension,size,webContentLink,mimeType,createdTime,modifiedTime),nextPageToken&orderBy=folder,modifiedTime desc,name&pageSize=100")
    fun getFiles(@Query("q") q: String?): Call<Modelclass?>?

    @GET("files?key=AIzaSyAqZV8HLqax1m5MOPK4Ix4mjvJh6k14DXI&supportsAllDrives=true&supportsTeamDrives=true&includeItemsFromAllDrives=true&fields=files(thumbnailLink,name,id,fileExtension,size,webContentLink,mimeType,createdTime,modifiedTime),nextPageToken&orderBy=folder,modifiedTime desc,name&pageSize=100")
    fun getNextPage(
        @Query("q") q: String?,
        @QueryMap map: Map<String?, String?>?
    ): Call<Modelclass?>?

    @GET("files?key=AIzaSyAqZV8HLqax1m5MOPK4Ix4mjvJh6k14DXI&supportsAllDrives=true&supportsTeamDrives=true&includeItemsFromAllDrives=true&fields=files(thumbnailLink,name,id,fileExtension,size,webContentLink,mimeType,createdTime,modifiedTime),nextPageToken&orderBy=folder,modifiedTime desc,name&pageSize=100")
    fun getSearchGoogle(
        @Query("q") q: String?,
        @QueryMap map: Map<String?, String?>?
    ): Call<Modelclass?>?

    companion object {
        const val REGISTRATION = "user/register"
        const val SEARCHIT =
            "get_search_results/?exclude=comment_status,comment_count,title,tags,slug,comments,author,content,url,status,comments"
        const val get_catagory_post =
            "get_category_posts/?exclude=comment_status,comment_count,title,tags,slug,comments,author,content,url,status,categories,comments,attachment"
        const val PINS = "sha256/gGOcYKAwzEaUfun6YdxZvFSQq/x2lF/R8UizDFofveY="
        const val Premium = "posts?per_page=100"
        const val g = "https://www.googleapis.com/drive/v3/"
        val spec: ConnectionSpec =
            ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).cipherSuites(
                TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
            ).build()
        const val PATTERN = "mbuddy.blogspot.com"
        val client: OkHttpClient = OkHttpClient.Builder().connectionSpecs(listOf(spec)).certificatePinner(
            CertificatePinner.Builder().add(
                PATTERN, PINS
            ).build()
        ).build()
        val gson = GsonBuilder().setLenient().create()

        //Creating retrofit Object

        val retrofit = Retrofit.Builder().baseUrl(Splash_after_reg.weburl)
            .client(NetworkHelper.InterLog.getIntercept())
            .addConverterFactory(GsonConverterFactory.create()).build()
        val google = Retrofit.Builder().baseUrl(g).client(NetworkHelper.InterLog.getIntercept())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
        const val GET_NONCE = "get_nonce"
        const val PAGE =
            "get_page/?id=237&exclude=type,slug,url,status,title,title_plain,date,modified,categories,tags,author,comments,attachments"
        const val USER_META_VARS = "user/update_user_meta_vars/"
        const val GET_USER_META_VARS = "user/get_user_meta/"
        const val DELETE_META = "user/delete_user_meta/"
        const val ID = "id"
        const val COUNT = "count"
        const val METHOD = "method"
        const val CONTROLLER = "controller"
        const val SEARCH = "search"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val SECONDS = "seconds"
        const val FIRST_NAME = "first_name"
        const val USERNAME = "username"
        const val USER_PASS = "user_pass"
        const val DESCRIPTION = "description"
        const val NONCE = "nonce"
        const val NOTIFY = "notify"
        const val USER_UPDATE_USER_META = "user/update_user_meta"
        const val COOKIE = "cookie"
        const val META_KEY = "meta_key"
        const val META_VALUE = "meta_value"
        const val USER_VALIDATE_AUTH_COOKIE = "user/validate_auth_cookie"
        const val TOKEN = "token"
        const val USER_GENERATE_AUTH_COOKIE = "user/generate_auth_cookie"
    }
}