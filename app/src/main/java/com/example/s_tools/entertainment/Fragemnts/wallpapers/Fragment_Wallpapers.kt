package com.example.s_tools.entertainment.Fragemnts.wallpapers

import android.os.AsyncTask
import com.example.s_tools.tools.retrofitcalls.MySharedPref.getwall1
import com.example.s_tools.tools.retrofitcalls.VC.check
import com.example.s_tools.tools.retrofitcalls.MySharedPref.getwall2
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallpaperOneModel
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_two.Wallpapers_2_Model
import com.example.s_tools.entertainment.Fragemnts.wallpapers.Fragment_Wall_RecyclerAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.s_tools.R
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import com.example.s_tools.tools.ToastMy
import com.example.s_tools.tools.Cvalues
import com.example.s_tools.tools.retrofitcalls.VC
import com.example.s_tools.entertainment.Fragemnts.wallpapers.Fragment_Wallpapers
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallapaprApiCalls
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallpapersApi
import com.example.s_tools.entertainment.Fragemnts.wallpapers.wallpapersCategory.wallpapers_one.model.WallpapersModel
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class Fragment_Wallpapers : Fragment() {
    var progressBar: ProgressBar? = null
    var recyclerView: RecyclerView? = null
    var list: MutableList<WallpaperOneModel>? = null
    var list2: MutableList<Wallpapers_2_Model>? = null
    private var adapter: Fragment_Wall_RecyclerAdapter? = null
    var refreshLayout: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_test_wallpapers, container, false)
        recyclerView = view.findViewById<View>(R.id.fragment_wall_recycler) as RecyclerView
        refreshLayout = view.findViewById(R.id.wallpaper_pullToRefresh)
        progressBar = view.findViewById(R.id.fragment_wallpapers_progressbar)
        if (getwall1(requireActivity()) != null) {
            MyAsyncTask().execute()
        } else {
            progressBar!!.setVisibility(View.VISIBLE)
            ToastMy.successToast(requireActivity(), Cvalues.PLEASE_WAIT, ToastMy.LENGTH_SHORT)
            check(requireActivity()) { success: Boolean, updateinfo: String? ->
                if (success) {
                    MyAsyncTask().execute()
                }
            }
        }
        return view
    }

    private inner class MyAsyncTask : AsyncTask<String?, String?, String?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            progressBar!!.visibility = View.VISIBLE
        }

        protected override fun doInBackground(vararg params: String?): String? {
            list = ArrayList()
            list2 = ArrayList()
            adapter = Fragment_Wall_RecyclerAdapter(activity, list, list2)
            wall1 = getwall1(activity)
            wall2 = getwall2(activity)
            WallapaprApiCalls.wall1Api(
                activity,
                12,
                1
            ) { success: Boolean, previews: List<WallpaperOneModel>? ->
                if (success) {
                    list!!.addAll(previews!!)
                    publishProgress()
                    progressBar!!.visibility = View.GONE
                }
            }
            WallapaprApiCalls.wall2FetchPopularpost(
                activity,
                0
            ) { success: Boolean, models: List<Wallpapers_2_Model>? ->
                if (success) {
                    list2!!.addAll(models!!)
                    adapter!!.notifyDataSetChanged()
                    progressBar!!.visibility = View.GONE
                }
            }

            //refresh
            refreshLayout!!.setOnRefreshListener {
                if (!list!!.isEmpty()) {
                    list!!.clear()
                    val api2 = WallpapersApi.retrofitWall1.create(
                        WallpapersApi::class.java
                    )
                    api2.wall1apicall(12, 1).enqueue(object : Callback<WallpapersModel?> {
                        override fun onResponse(
                            call: Call<WallpapersModel?>,
                            response: Response<WallpapersModel?>
                        ) {
                            if (response.body() != null) {
                                list!!.addAll(response.body()!!.previews)
                                adapter!!.notifyDataSetChanged()
                                refreshLayout!!.isRefreshing = false
                            }
                        }

                        override fun onFailure(call: Call<WallpapersModel?>, t: Throwable) {
                            Toast.makeText(activity, "Server Error!", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
            return null
        }

        protected override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            recyclerView!!.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerView!!.adapter = adapter
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            adapter!!.notifyDataSetChanged()
        }
    }

    companion object {
        const val TAG = "mtag"
        @JvmField
        var wall1: String? = null
        @JvmField
        var wall2: String? = null
    }
}