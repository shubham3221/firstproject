package com.example.s_tools.entertainment.Fragemnts.movies.google_dict

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.s_tools.entertainment.Fragemnts.movies.google_dict.upperRecyclerview.SliderGoogleAdapter
import com.kaopiz.kprogresshud.KProgressHUD
import android.os.Bundle
import com.example.s_tools.R
import com.example.s_tools.MyWebService
import com.google.gson.JsonObject
import com.example.s_tools.tools.ToastMy
import com.example.s_tools.tools.Cvalues
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jzvd.Jzvd
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentTransaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class Google_Activity : AppCompatActivity(), LoadingprogressListner {
    var backbtn: CardView? = null
    var progressBar: ProgressBar? = null
    var lastupdate: TextView? = null
    var info: TextView? = null
    var thread: Thread? = null
    private var fragmentTransaction: FragmentTransaction? = null
    var fragment: GoogleFragment? = null
    var upper_nav_recyclerview: RecyclerView? = null
    var sliderGoogleAdapter: SliderGoogleAdapter? = null
    private var progress1 = 2
    private val i = 0
    private var show: KProgressHUD? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adactivity)
        init()
        backbtn!!.setOnClickListener { v: View? -> finish() }
        geturl()

//        Adfragment adfragment=new Adfragment();
        uperRecyclerSetup()
    }

    private fun geturl() {
        show = KProgressHUD.create(this@Google_Activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Checking...")
            .setCancellable(false).setDimAmount(0.5f).show()
        val myWebService = MyWebService.retrofit.create(MyWebService::class.java)
        myWebService.checkVersion()!!.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                show!!.dismiss()
                if (response.body() != null) {
                    if (response.body()!!["status"].asString == "ok") {
                        val id = response.body()!!.getAsJsonObject("page")
                            .getAsJsonObject("custom_fields")["googledriveurl"].asJsonArray[0].asString
                        fragment = GoogleFragment(
                            upper_nav_recyclerview,
                            sliderGoogleAdapter,
                            lastupdate,
                            info,
                            id
                        )
                        //        fragment=new GoogleFragment(upper_nav_recyclerview,uppernavAdapter,lastupdate,progressBar,"1bBITkR6l1jwPM4gv1PR1OkquVpBZJ70C");
                        fragment!!.retainInstance = true
                        fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction!!.replace(R.id.adcontainer, fragment!!)
                        fragmentTransaction!!.commit()
                    } else {
                        ToastMy.errorToast(
                            this@Google_Activity,
                            Cvalues.OFFLINE,
                            Toast.LENGTH_SHORT
                        )
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                show!!.dismiss()
                ToastMy.errorToast(this@Google_Activity, Cvalues.OFFLINE, Toast.LENGTH_SHORT)
                finish()
            }
        })
    }

    private fun uperRecyclerSetup() {
        upper_nav_recyclerview!!.layoutManager =
            LinearLayoutManager(this@Google_Activity, RecyclerView.HORIZONTAL, false)
        sliderGoogleAdapter = SliderGoogleAdapter(this, ArrayList())
        upper_nav_recyclerview!!.adapter = sliderGoogleAdapter
    }

    private fun init() {
        progressBar = findViewById(R.id.updatenow)
        backbtn = findViewById(R.id.backbtn)
        lastupdate = findViewById(R.id.lastupdate_adlt)
        upper_nav_recyclerview = findViewById(R.id.navi_recy)
        info = findViewById(R.id.info)
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            if (VideoFragment.isOpen) {
                supportFragmentManager.popBackStack()
                return
            }
            if (progressBar!!.visibility == View.VISIBLE) {
                hideProgress(50)
            }
            supportFragmentManager.popBackStack()
            val fragment =
                supportFragmentManager.fragments[supportFragmentManager.fragments.size - 2] as GoogleFragment
            lastupdate!!.text = fragment.listSize.toString()+" files"

            if (sliderGoogleAdapter!!.items.size >= 1) {
                sliderGoogleAdapter!!.items.removeAt(sliderGoogleAdapter!!.items.size - 1)
                sliderGoogleAdapter?.notifyItemChanged(sliderGoogleAdapter!!.items.size)
            }
        }
    }

    override fun showProgress() {
        progress1 = 1
        thread = Thread {
            var i = 0
            //            while (i < 35 && progressBar.getVisibility() != View.GONE) {
            while (i < 35 && progress1 != 0) {
                i++
                try {
                    Thread.sleep(60)
                    runOnUiThread {
                        Log.e("//", "showProgress: 1st")
                        if (progress1 != 0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                progressBar!!.setProgress(progress1++, true)
                            } else {
                                progressBar!!.progress = progress1++
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            i = 0
            //            while (i < 35 && progressBar.getVisibility() != View.GONE) {
            while (i < 35 && progress1 != 0) {
                i++
                try {
                    Thread.sleep(800)
                    runOnUiThread {
                        Log.e("//", "showProgress: 2nd")
                        if (progress1 != 0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                progressBar!!.setProgress(progress1++, true)
                            } else {
                                progressBar!!.progress = progress1++
                            }
                        }
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        thread!!.start()
    }

    override fun hideProgress(time: Int) {
        if (progressBar!!.visibility == View.VISIBLE) {
            Log.e("//", "hideProgress: ")
            thread!!.interrupt()
            progress1 = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar!!.setProgress(100, true)
            } else {
                progressBar!!.progress = 100
            }
            Handler(Looper.getMainLooper()).postDelayed({
                progressBar!!.visibility = View.GONE
                val manimation =
                    AnimationUtils.loadAnimation(this@Google_Activity, R.anim.blink_text)
                progressBar!!.startAnimation(manimation)
                manimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            progressBar!!.setProgress(0, true)
                        } else {
                            progressBar!!.progress = 0
                        }
                        manimation.reset()
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
            }, time.toLong())
        }
    }

    override fun visibleProgress() {
        progressBar!!.visibility = View.VISIBLE
    }

    override fun setprogress(progress: Int) {
        progressBar!!.progress = progress
    }
}