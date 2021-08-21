package com.example.s_tools.premium

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.s_tools.premium.model.Post
import com.example.s_tools.MyWebService
import android.content.SharedPreferences
import android.os.AsyncTask
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import com.example.s_tools.TinyDB
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.s_tools.R
import com.example.s_tools.tools.Cvalues
import com.example.s_tools.premium.Dialing
import com.example.s_tools.premium.model.PremiumModel
import com.example.s_tools.premium.Dashboard_1
import com.securepreferences.SecurePreferences
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import com.google.gson.reflect.TypeToken
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.ArrayList

class Dashboard_1 : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null

    //    CustomAdapterDashboard customAdapter;
    var list: MutableList<Post>? = null
    var toolbar: Toolbar? = null
    var myWebService: MyWebService? = null
    var fragment: Fragment? = null
    var preferences: SharedPreferences? = null
    var type: Type? = null
    var gson: Gson? = null
    var kProgressHUD: KProgressHUD? = null
    var tinyDB: TinyDB? = null
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_1)
        init()
        kProgressHUD =
            KProgressHUD.create(this@Dashboard_1).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(Cvalues.PLEASE_WAIT)
                .setAutoDismiss(true) //                            .setDetailsLabel("Downloading data")
                .setCancellable(true).show()
        MyAsyncTask().execute()
    }

    private fun init() {
        recyclerView = findViewById(R.id.recyclerView)
        toolbar = findViewById<View>(R.id.mytoolbar) as Toolbar
        //        linearLayout=findViewById(R.id.premiumbannerView);
        setSupportActionBar(toolbar)
        toolbar!!.subtitle = Cvalues.PLEASE_WAIT
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //ChangeStatusBarColor.changeStatusbarColor(Dashboard_1.this,R.color.statusbarcolor_premium);

        //if (!CheckRootAccess.isRootedDevice(this)){
        // }
    }

    fun getposts() {
        Dialing.getData(this@Dashboard_1, 1, 15) { success,body ->
            if (success) {
                list!!.addAll(body!!.posts)
                tinyDB!!.putInt(POSTID, list!![0].id)
                val gson = Gson()
                val json = gson.toJson(list)
                val editor = preferences!!.edit()
                editor.putString(LIST, json).apply()
                //                customAdapter.notifyDataSetChanged();
                toolbar!!.subtitle = UPDATED
                kProgressHUD!!.dismiss()
            } else {
                kProgressHUD!!.setDetailsLabel(Cvalues.OFFLINE)
                kProgressHUD!!.dismiss()
                toolbar!!.subtitle = Cvalues.SERVER_OFFLINE
            }
        }
    }

    private inner class MyAsyncTask : AsyncTask<String?, String?, String?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            list = ArrayList()
            tinyDB = TinyDB(this@Dashboard_1)
        }

        protected override fun doInBackground(vararg params: String?): String? {
            preferences = SecurePreferences(
                this@Dashboard_1,
                MySharedPref.activity,
                MySharedPref.PostFilename
            )
            type = object : TypeToken<List<Post?>?>() {}.type
            gson = Gson()
            //                customAdapter=new CustomAdapterDashboard(list, Dashboard_1.this);
            if (preferences!!.contains(LIST)) {
                list!!.addAll(gson!!.fromJson(preferences!!.getString(LIST, null), type))
                publishProgress("dismiss")
                UpdatePosts()
            } else {
                publishProgress()
                getposts()
            }
            return null
        }

        protected override fun onProgressUpdate(vararg values: String?) {
            if (values != null) {
                kProgressHUD!!.dismiss()
            }
            super.onProgressUpdate(*values)
            recyclerView!!.layoutManager = LinearLayoutManager(this@Dashboard_1)
            //            recyclerView.setAdapter(customAdapter);
            toolbar!!.subtitle = Cvalues.UPDATING
        }
    }

    private fun UpdatePosts() {
        Dialing.getData(this@Dashboard_1, 1, 15) { success, body ->
            if (success) {
                Thread {
                    val newList: List<Post> = ArrayList(body?.posts!!)
                    tinyDB!!.putInt(POSTID, newList[0].id)
                    if (newList[0].id != list!![0].id) {
                        val gson = Gson()
                        val json = gson.toJson(newList)
                        val editor = preferences!!.edit()
                        editor.putString(LIST, json).apply()
                        list!!.clear()
                        list!!.addAll(newList)
                    } else {
                        for (i in newList.indices) {
                            if (list!![i].modified != newList[i].modified) {
                                list!![i] = newList[i]
                            }
                        }
                        val gson = Gson()
                        val json = gson.toJson(newList)
                        val editor = preferences!!.edit()
                        editor.putString(LIST, json).apply()
                    }
                    runOnUiThread {
//                        customAdapter.notifyDataSetChanged();
                        toolbar!!.subtitle = UPDATED
                    }
                }.start()
            }
        }
    }

    //optionmneu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.searchbarmenu, menu)
        val item = menu.findItem(R.id.searchview)
        val searchView = item.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty() || query.length < 2) {
                    Toast.makeText(this@Dashboard_1, Cvalues.INCORRECT_FORMAT, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    kProgressHUD!!.show()
                    val myWebService = MyWebService.retrofit.create(MyWebService::class.java)
                    myWebService.searchPosts(query)!!.enqueue(object : Callback<PremiumModel?> {
                        override fun onResponse(
                            call: Call<PremiumModel?>,
                            response: Response<PremiumModel?>
                        ) {
                            list!!.clear()
                            val body = response.body()
                            for (post in body!!.posts) {
                                if (post.categories != null) {
                                    for (c in post.categories) {
                                        if (c.id == 3) {
                                            list!!.add(post)
                                        }
                                    }
                                }
                            }
                            if (list!!.isEmpty()) {
                                Toast.makeText(
                                    this@Dashboard_1,
                                    Cvalues.NO_RESULT_FOUND,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            //                            customAdapter.notifyDataSetChanged();
                            kProgressHUD!!.dismiss()
                        }

                        override fun onFailure(call: Call<PremiumModel?>, t: Throwable) {
                            kProgressHUD!!.dismiss()
                        }
                    })
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
//                customAdapter.getFilter().filter(newText);
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    } //    class MyUnityadsBanner implements BannerView.IListener {

    //        @Override
    //        public void onBannerLoaded(BannerView bannerView) {
    //            linearLayout.removeAllViews();
    //            linearLayout.addView(bannerView);
    //        }
    //
    //        @Override
    //        public void onBannerClick(BannerView bannerView) {
    //
    //        }
    //
    //        @Override
    //        public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
    //            Log.e(Cvalues.TAG, "onBannerFailedToLoad: " + bannerErrorInfo.errorMessage);
    //        }
    //
    //        @Override
    //        public void onBannerLeftApplication(BannerView bannerView) {
    //            bannerView.destroy();
    //        }
    //    }
    companion object {
        const val LIST = "list"
        const val UPDATED = "❤ Updated"
        const val POSTID = "postid"
    }
}