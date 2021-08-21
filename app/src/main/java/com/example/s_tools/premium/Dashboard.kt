package com.example.s_tools.premium

import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.s_tools.MyWebService
import com.example.s_tools.R
import com.example.s_tools.TinyDB
import com.example.s_tools.premium.model.Post
import com.example.s_tools.premium.model.PremiumModel
import com.example.s_tools.tools.Cvalues
import com.example.s_tools.tools.ToastMy
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kaopiz.kprogresshud.KProgressHUD
import com.securepreferences.SecurePreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.alexbykov.nopaginate.paginate.NoPaginate
import java.lang.reflect.Type
import java.util.ArrayList

class Dashboard : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var customAdapter: Adapter? = null
    var list: MutableList<Post>? = null
    var toolbar: Toolbar? = null
    var preferences: SharedPreferences? = null
    var type: Type? = null
    var gson: Gson? = null
    var kProgressHUD: KProgressHUD? = null
    var tinyDB: TinyDB? = null
    var i = 0
    private val scrolling: NoPaginate? = null
    private var loading = false
    private var page = 2
    private val totalItemCount = 0
    private val lastVisibleItem = 0
    var scrollView: NestedScrollView? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_dashboard_1)
        init()
        kProgressHUD =
            KProgressHUD.create(this@Dashboard).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(Cvalues.PLEASE_WAIT)
                .setAutoDismiss(true) //                            .setDetailsLabel("Downloading data")
                .setCancellable(true).show()
        setupAds()
        MyAsyncTask().execute()
    }

    private fun init() {
        scrollView = findViewById<NestedScrollView>(R.id.nested)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        toolbar = findViewById<View>(R.id.mytoolbar) as Toolbar?
        setSupportActionBar(toolbar)
        toolbar?.setSubtitle(Cvalues.PLEASE_WAIT)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

//        ChangeStatusBarColor.changeStatusbarColor(Dashboard.this, R.color.colorPrimaryDark);

        //if (!CheckRootAccess.isRootedDevice(this)){
        // }
    }

    fun getposts() {
        Dialing.getData(
            this@Dashboard,
            1,
            15
        ) { success, body ->
            if (success) {
                list!!.addAll(body?.getPosts()!!)
                tinyDB?.putInt(POSTID, list!![0].getId())
                val gson = Gson()
                val json: String = gson.toJson(list)
                val editor: SharedPreferences.Editor = preferences?.edit()!!
                editor.putString(LIST, json).apply()
                customAdapter!!.notifyDataSetChanged()
                toolbar!!.subtitle = UPDATED
                kProgressHUD?.dismiss()
            } else {
                kProgressHUD?.setDetailsLabel(Cvalues.OFFLINE)
                kProgressHUD?.dismiss()
                toolbar?.setSubtitle(Cvalues.OFFLINE)
            }
        }
    }

    private fun setupAds() {
        Thread {
            try {
                Thread.sleep(200)
                runOnUiThread(Runnable {
                    var adView = AdView(this)
                    adView.setAdSize(AdSize.BANNER)
                    adView.setAdUnitId(Cvalues.banr)
                    adView = findViewById<AdView>(R.id.adView)
                    val adRequest = AdRequest.Builder().build()
                    adView.loadAd(adRequest)
                })
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
    }

    private inner class MyAsyncTask : AsyncTask<String?, String?, String?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            list = ArrayList<Post>()
            tinyDB = TinyDB(this@Dashboard)
        }

        protected override fun doInBackground(vararg params: String?): String? {
            preferences =
                SecurePreferences(this@Dashboard, MySharedPref.activity, MySharedPref.PostFilename)
            type = object : TypeToken<List<Post?>?>() {}.getType()
            gson = Gson()
            if (MySharedPref.isVersionOut(this@Dashboard)) {
                ToastMy.errorToast(
                    this@Dashboard,
                    "New Update Found! Please Restart App",
                    ToastMy.LENGTH_LONG
                )
            } else {
                if (preferences!!.contains(LIST)) {
                    list!!.addAll(
                        gson!!.fromJson<Collection<Post>>(
                            preferences!!.getString(LIST, null),
                            type
                        )
                    )
                    publishProgress("dismiss")
                    UpdatePosts()
                } else {
                    publishProgress()
                    getposts()
                }
            }
            return null
        }

        protected override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            customAdapter = if (list!!.isEmpty()) {
                Adapter(list, this@Dashboard, 0)
            } else {
                Adapter(list, this@Dashboard, list!![0].getId())
            }
            if (values != null) {
                kProgressHUD?.dismiss()
            }
            recyclerView?.setNestedScrollingEnabled(false)
            recyclerView?.setLayoutManager(LinearLayoutManager(this@Dashboard))
            recyclerView?.setAdapter(customAdapter)
            toolbar?.setSubtitle(Cvalues.UPDATING)
            scrollView?.getViewTreeObserver()?.addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {
                val view = scrollView?.getChildCount()?.minus(1)?.let { scrollView?.getChildAt(it) } as View
                if (view.bottom - (scrollView?.getHeight()!! + scrollView!!
                        ?.getScrollY()) == 0
                ) {
                    if (!loading) {
                        loading = true
                        kProgressHUD?.setLabel("Loading more posts")
                        kProgressHUD?.show()
                        fetchPosts(page)
                    }
                }
            })
        }

        private fun fetchPosts(mpage: Int) {
            Dialing.getData(
                this@Dashboard,
                mpage,
                15
            ) { success, body ->
                if (success) {
                    kProgressHUD?.dismiss()
                    list!!.addAll(body?.getPosts()!!)
                    customAdapter!!.notifyDataSetChanged()
                    page++
                    loading = false
                } else {
                    kProgressHUD?.setLabel(Cvalues.ERROR)
                    kProgressHUD?.dismiss()
                    toolbar?.setSubtitle(Cvalues.OFFLINE)
                }
            }
        }
    }

    private fun UpdatePosts() {
        Dialing.getData(
            this@Dashboard,
            1,
            15
        ) { success, body ->
            if (success) {
                Thread {
                    val newList: List<Post> = ArrayList<Post>(body?.getPosts())
                    tinyDB?.putInt(POSTID, newList[0].getId())
                    if (newList[0].getId() != list!![0].getId()) {
                        val gson = Gson()
                        val json: String = gson.toJson(newList)
                        val editor: SharedPreferences.Editor = preferences!!.edit()
                        editor.putString(LIST, json).apply()
                        list!!.clear()
                        list!!.addAll(newList)
                    } else {
                        for (i in newList.indices) {
                            if (list!![i].getModified() != newList[i].getModified()) {
                                list!![i] = newList[i]
                            }
                        }
                        val gson = Gson()
                        val json: String = gson.toJson(newList)
                        val editor: SharedPreferences.Editor = preferences!!.edit()
                        editor.putString(LIST, json).apply()
                    }
                    runOnUiThread {
                        customAdapter!!.yes = false
                        customAdapter!!.notifyDataSetChanged()
                        toolbar!!.subtitle = UPDATED
                        ToastMy.successToast(this@Dashboard, "Updated", ToastMy.LENGTH_SHORT)
                    }
                }.start()
            } else {
                toolbar?.setSubtitle(Cvalues.OFFLINE)
            }
        }
    }

    //optionmneu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        getMenuInflater().inflate(R.menu.searchbarmenu, menu)
        val item = menu.findItem(R.id.searchview)
        val searchView = item.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty() || query.length < 2) {
                    Toast.makeText(this@Dashboard, Cvalues.INCORRECT_FORMAT, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    kProgressHUD?.show()
                    val myWebService: MyWebService =
                        MyWebService.retrofit.create(MyWebService::class.java)
                    myWebService.searchPosts(query)?.enqueue(object : Callback<PremiumModel?> {
                        override fun onResponse(
                            call: Call<PremiumModel?>,
                            response: Response<PremiumModel?>
                        ) {
                            list!!.clear()
                            val body: PremiumModel? = response.body()
                            for (post in body?.getPosts()!!) {
                                if (post.getCategories() != null) {
                                    for (c in post.getCategories()) {
                                        if (c.id == 3) {
                                            list!!.add(post)
                                        }
                                    }
                                }
                            }
                            if (list!!.isEmpty()) {
                                Toast.makeText(
                                    this@Dashboard,
                                    Cvalues.NO_RESULT_FOUND,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            customAdapter!!.notifyDataSetChanged()
                            kProgressHUD?.dismiss()
                        }

                        override fun onFailure(call: Call<PremiumModel?>, t: Throwable) {
                            kProgressHUD?.dismiss()
                        }
                    })
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                customAdapter!!.filter.filter(newText)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val LIST = "list"
        const val UPDATED = "❤Updated"
        const val POSTID = "postid"
    }
}