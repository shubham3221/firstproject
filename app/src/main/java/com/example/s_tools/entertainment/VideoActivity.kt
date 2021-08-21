package com.example.s_tools.entertainment

import com.example.s_tools.premium.Dialing.getData
import androidx.appcompat.app.AppCompatActivity
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.Detail_Act.ScFragment.HideToolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.example.s_tools.entertainment.uper_slider.SliderPage_Adapter
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.SectionsFragment
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.example.s_tools.TinyDB
import com.google.android.gms.ads.interstitial.InterstitialAd
import android.os.Bundle
import com.example.s_tools.R
import android.text.format.DateUtils
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import android.content.Intent
import com.example.s_tools.Splash_after_reg
import android.os.Looper
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.RequestConfiguration
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.s_tools.entertainment.VideoActivity
import com.example.s_tools.tools.retrofitcalls.VC
import com.example.s_tools.user_request.bb.OnlineModel
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MoviesPosts
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.SearchMov
import android.app.ActivityOptions
import com.example.s_tools.premium.Dashboard
import androidx.core.content.ContextCompat
import com.example.s_tools.chatting.ChatKaro
import com.example.s_tools.testing.DownloadActivity
import androidx.core.view.GravityCompat
import android.view.animation.Animation
import com.example.s_tools.tools.Cvalues
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.example.s_tools.tools.ToastMy
import com.google.android.gms.ads.LoadAdError
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Handler
import android.transition.Fade
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.s_tools.Services_item.mp3download.Mp3Downloader
import com.example.s_tools.Services_item.VidDownloader.VideoDownloader
import com.example.s_tools.Services_item.inst.InstFragment
import com.example.s_tools.entertainment.Fragemnts.wallpapers.Fragment_Wallpapers
import com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.ImagesActivity
import com.example.s_tools.MainActivity3
import com.example.s_tools.HowDownFragment
import com.example.s_tools.Splash_login_reg.SplashScreen
import com.example.s_tools.premium.Dialing
import com.example.s_tools.premium.model.PremiumModel
import com.example.s_tools.premium.Dashboard_1
import com.example.s_tools.tools.MyAsyncTask
import com.google.android.gms.ads.AdRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class VideoActivity : AppCompatActivity(), HideToolbar {
    private var viewPagerUp: ViewPager? = null
    private var indicator: TabLayout? = null
    private var upperSliderAdapter: SliderPage_Adapter? = null
    private var sectionsFragment: SectionsFragment? = null
    private var fragmentTransaction: FragmentTransaction? = null
    private var searchBar: CardView? = null
    private var back: ImageView? = null
    private var toolbar: View? = null
    var drawerLayout: DrawerLayout? = null
    var navigationView: NavigationView? = null
    var drawerToggle: ActionBarDrawerToggle? = null
    private var chatkaro: LinearLayout? = null
    private var downloadActivity: LinearLayout? = null
    private var openDrawerTool: LinearLayout? = null
    private var updateMoviesBtn: LinearLayout? = null
    var premium: View? = null
    private var tinyDB: TinyDB? = null
    private var messageCountView: TextView? = null
    private var updateText: TextView? = null
    private val mInterstitialAd: InterstitialAd? = null
    private var notify_icon_premium_post: ImageView? = null
    private var adLoaded = false
    var viewAnimator: ViewAnimator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        init()
        // Load the ViewAnimator and display the first layout
        viewAnimator = findViewById<View>(R.id.view_animator) as ViewAnimator
        val relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(Date().time)
        when {
            tinyDB!!.getLong("lastcheck") == 0L -> {
                startActivity(Intent(this@VideoActivity, Splash_after_reg::class.java))
                finishAffinity()
            }
            MySharedPref.isVersionOut(this@VideoActivity) -> {
                startActivity(Intent(this@VideoActivity, Splash_after_reg::class.java))
                finishAffinity()
            }
            else -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    Splash_after_reg.fill_string()
                    MyTask().execute()
                    afterLogin()
                    MobileAds.initialize(this) { initializationStatus: InitializationStatus? ->
                        val configuration = RequestConfiguration.Builder().setTestDeviceIds(
                            Arrays.asList(
                                "0C9EBB7EE878C7C3529A8D806DC4FFCA",
                                "EDB30A4A00115883E90E49CAD034A12C"
                            )
                        ).build()
                        MobileAds.setRequestConfiguration(configuration)
                    }
                    val lll: ConstraintLayout = findViewById(R.id.lll)
                    viewAnimator!!.removeView(lll)
                }, 500)
            }
        }
        //            show=KProgressHUD.create(VideoActivity.this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Updating...").setCancellable(true).setDimAmount(0.5f).show();
//
    }

    internal inner class MyTask : MyAsyncTask() {
        override fun doInBackground() {
//            setup_ad();
            if (MySharedPref.getMoviesList(this@VideoActivity, MySharedPref.RECENT) != null) {
                slider_movies()
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    var checking = true
                    while (checking) {
                        delay(500)
                        if (MySharedPref.getMoviesList(
                                this@VideoActivity,
                                MySharedPref.RECENT
                            ) != null
                        ) {
                            checking = false
                        }
                    }
                    slider_movies()

                }
            }
            if (tinyDB!!.getLong(LASTCHECK) == 0L) {
                Thread {
                    VC.check_v(this@VideoActivity) { success, updateinfo ->
                        if (success) {
                            tinyDB!!.putLong(LASTCHECK, System.currentTimeMillis())
                        }
                    }
                }.start()
            } else {
                Thread {
                    val now = System.currentTimeMillis()
                    val diff = now - tinyDB!!.getLong(LASTCHECK)
                    if (diff >= 3600000 * 24) {
                        VC.check_v(this@VideoActivity) { success: Boolean, updateinfo: String? ->
                            if (success) {
                                tinyDB!!.putLong(LASTCHECK, now)
                            }
                        }
                    }
                }.start()
            }
            sectionsFragment = SectionsFragment(mInterstitialAd)
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction!!.add(R.id.mviewpager, sectionsFragment!!)
            drawerToggle = ActionBarDrawerToggle(
                this@VideoActivity,
                drawerLayout,
                R.string.open,
                R.string.close
            )
            drawerLayout!!.addDrawerListener(drawerToggle!!)
            drawerToggle!!.syncState()
        }

        private fun slider_movies() {
            Thread {
                val onlineModels: MutableList<OnlineModel> = ArrayList()
                val moviesList = MySharedPref.getMoviesList(this@VideoActivity, MySharedPref.RECENT)
                for (i in moviesList!!.indices) {
                    if (moviesList[i].custom_fields.otherlinks != null) {
                        val split = moviesList[i].custom_fields.otherlinks[0].split(",".toRegex())
                            .toTypedArray()
                        for (link in split) {
                            if (link.contains("anonfiles")) {
                                if (moviesList[i].custom_fields.mainimage != null) {
                                    onlineModels.add(
                                        OnlineModel(
                                            link.trim { it <= ' ' },
                                            moviesList[i].title.trim { it <= ' ' },
                                            moviesList[i].custom_fields.mainimage[0].trim { it <= ' ' },
                                            moviesList[i].date
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                if (!onlineModels.isEmpty()) {
                    upperSliderAdapter = SliderPage_Adapter(this@VideoActivity, onlineModels)
                    toBeContinue()
                }
            }.start()
        }

        override fun pendingTask() {
            indicator!!.setupWithViewPager(viewPagerUp, false)
            viewPagerUp!!.adapter = upperSliderAdapter
        }

        override fun onPostExecute() {
            searchBar!!.setOnClickListener { view: View? ->
                val intent = Intent(this@VideoActivity, SearchMov::class.java)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this@VideoActivity,
                    searchBar,
                    "cardview"
                )
                startActivity(intent, options.toBundle())
            }
            navigtionDrawer()
            sectionsFragment = SectionsFragment(mInterstitialAd)
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction!!.add(R.id.mviewpager, sectionsFragment!!)
            fragmentTransaction!!.commit()
            premium!!.setOnClickListener { view: View? ->
                tinyDB!!.putBoolean(NEWPREMIUMPOST, false)
                notify_icon_premium_post!!.visibility = View.GONE
                startActivity(Intent(this@VideoActivity, Dashboard::class.java))
            }
            chatkaro!!.setOnClickListener { view: View? ->
                if (!MySharedPref.isSharedPrefnull(this@VideoActivity)) {
                    if (messageCountView!!.text.length >= 1) {
                        messageCountView!!.text = ""
                        messageCountView!!.background =
                            ContextCompat.getDrawable(this@VideoActivity, R.drawable.sms2)
                    }
                    startActivity(Intent(this@VideoActivity, ChatKaro::class.java))
                } else {
                    startActivity(Intent(this@VideoActivity, SplashScreen::class.java))
                }
            }
            downloadActivity!!.setOnClickListener { view: View? ->
                startActivity(
                    Intent(
                        this@VideoActivity,
                        DownloadActivity::class.java
                    )
                )
            }
            openDrawerTool!!.setOnClickListener { view: View? ->
                drawerLayout!!.openDrawer(
                    GravityCompat.START
                )
            }
            updateMoviesBtn!!.setOnClickListener { view: View ->
                val sgAnimation = AnimationUtils.loadAnimation(this@VideoActivity, R.anim.shrink)
                view.startAnimation(sgAnimation)
                updateAll()
            }
            back!!.setOnClickListener { view: View? -> drawerLayout!!.openDrawer(GravityCompat.START) }
        }
    }

    private fun updateAll() {
        updateText!!.text = "Updating"
        updateMoviesBtn!!.isClickable = false
        val animation = AnimationUtils.loadAnimation(this@VideoActivity, R.anim.rotate_view)
        updateMoviesBtn!!.startAnimation(animation)
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, Cvalues.inter, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                super.onAdLoaded(interstitialAd)
                interstitialAd.show(this@VideoActivity)
                adLoaded = false
                updateMoviesBtn!!.clearAnimation()
                ToastMy.successToast(this@VideoActivity, Cvalues.UPDATING, Toast.LENGTH_SHORT)
                //                sliderImagesApi();
                sectionsFragment = SectionsFragment(true, mInterstitialAd)
                fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction!!.replace(R.id.mviewpager, sectionsFragment!!)
                fragmentTransaction!!.commit()
                updateMoviesBtn!!.isClickable = true
                updateText!!.text = "Success"
                updateMoviesBtn!!.clearAnimation()
                Handler(Looper.getMainLooper()).postDelayed({
                    updateText!!.text = "Update"
                    updateMoviesBtn!!.isClickable = true
                }, 1000)
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                updateText!!.text = "Failed"
                updateMoviesBtn!!.clearAnimation()
                Handler(Looper.getMainLooper()).postDelayed({
                    updateText!!.text = "Update"
                    updateMoviesBtn!!.isClickable = true
                }, 1000)
            }
        })
    }

    @SuppressLint("NonConstantResourceId")
    private fun navigtionDrawer() {
        navigationView!!.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.item1 -> startActivity(Intent(this@VideoActivity, ChatKaro::class.java))
                R.id.item2 -> {
                    VC.check(this@VideoActivity) { success, updateinfo -> }
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(VC.BLOGSPOT_COM)))
                }
                R.id.item3 -> startActivity(Intent(this@VideoActivity, Mp3Downloader::class.java))
                R.id.item4 -> startActivity(Intent(this@VideoActivity, VideoDownloader::class.java))
                R.id.item5 -> {
                    val someFragment = InstFragment()
                    someFragment.enterTransition = Fade()
                    someFragment.exitTransition = Fade()
                    val fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragmentTransaction.add(R.id.mycontainer, someFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
                R.id.item7 -> {
                    //4k wallper
                    val wallpapers = Fragment_Wallpapers()
                    wallpapers.enterTransition = Fade()
                    wallpapers.exitTransition = Fade()
                    val fragmentTransaction2 = supportFragmentManager.beginTransaction()
                    fragmentTransaction2.add(R.id.mycontainer, wallpapers)
                    fragmentTransaction2.addToBackStack(null)
                    fragmentTransaction2.commit()
                }
                R.id.item8 ->                     //image search
                    startActivity(Intent(this@VideoActivity, ImagesActivity::class.java))
                R.id.item9 ->                     //image search
                    startActivity(Intent(this@VideoActivity, MainActivity3::class.java))
                R.id.itemhowtodonwload -> {
                    val how = HowDownFragment()
                    how.enterTransition = Fade()
                    how.exitTransition = Fade()
                    val fragmentTransaction3 = supportFragmentManager.beginTransaction()
                    fragmentTransaction3.add(R.id.mycontainer, how)
                    fragmentTransaction3.addToBackStack(null)
                    fragmentTransaction3.commit()
                }
            }
            drawerLayout!!.closeDrawers()
            false
        }
    }

    private fun init() {
        viewPagerUp = findViewById(R.id.slider_pager)
        indicator = findViewById(R.id.indicator1)
        searchBar = findViewById(R.id.expendedcarf)
        back = findViewById(R.id.toolimg)
        premium = findViewById(R.id.ppost)
        chatkaro = findViewById(R.id.cpost)
        downloadActivity = findViewById(R.id.dpost)
        openDrawerTool = findViewById(R.id.tpost)
        updateMoviesBtn = findViewById(R.id.upost)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        messageCountView = findViewById(R.id.chatkaro)
        updateText = findViewById(R.id.update)
        tinyDB = TinyDB(this@VideoActivity)
        notify_icon_premium_post = findViewById(R.id.notifi)
    }

    private fun afterLogin() {
        if (!MySharedPref.isSharedPrefnull(this@VideoActivity)) {
            val isautoupdateyes = tinyDB!!.getInt(AUTOUPDATE)
            if (isautoupdateyes == 1) {
                updateChat()
            }
        }
        if (tinyDB!!.getBoolean(NEWPREMIUMPOST)) {
            notify_icon_premium_post!!.visibility = View.VISIBLE
        } else {
            check_Premiumpost()
        }
    }

    private fun check_Premiumpost() {
        getData(this@VideoActivity, 1, 1) { success: Boolean, body: PremiumModel? ->
            if (success) {
                val id = body!!.posts[0].id
                if (id != tinyDB!!.getInt(Dashboard_1.POSTID)) {
                    tinyDB!!.putBoolean(NEWPREMIUMPOST, true)
                    notify_icon_premium_post!!.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateChat() {
        if (!tinyDB!!.getBoolean(ISOPENMESSAGES)) {
//            ChatViewModel.getMessageCount(VideoActivity.this, MySharedPref.getTokenMessages(VideoActivity.this), MySharedPref.getChatid(VideoActivity.this), (success, size) -> {
//                if (success) {
//                    if (tinyDB.getInt(CHATMESSAGECOUNT) != size) {
//                        //show notification
//                        NotificationHelper helper=new NotificationHelper();
//                        helper.showChatnotifiaction(VideoActivity.this);
//                        tinyDB.putInt(CHATMESSAGECOUNT, size - tinyDB.getInt(CHATMESSAGECOUNT));
//                        messageCountView.setBackground(ContextCompat.getDrawable(VideoActivity.this, R.drawable.shape_notification));
//                        messageCountView.setText(String.valueOf(tinyDB.getInt(CHATMESSAGECOUNT)));
//                        tinyDB.putBoolean(ISOPENMESSAGES, true);
//                    }
//                }
//            });
        } else {
            messageCountView!!.background =
                ContextCompat.getDrawable(this@VideoActivity, R.drawable.shape_notification)
            messageCountView!!.text = tinyDB!!.getInt(Cvalues.CHATMESSAGECOUNT).toString()
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
            finishAffinity()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun hideToolbar() {
        toolbar = findViewById(R.id.toolbarvid)
        toolbar!!.setVisibility(View.GONE)
    }

    override fun showToolbar() {
        toolbar!!.visibility = View.VISIBLE
    }

    companion object {
        const val AUTOUPDATE = "isautoupdateyes"
        const val ISOPENMESSAGES = "isopenmessages"
        const val TAG = "//"
        const val LASTCHECK = "lastcheck"
        const val NEWPREMIUMPOST = "newpremiumpost"
    }
}