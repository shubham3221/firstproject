package com.example.s_tools.main_activity

import com.example.s_tools.tools.retrofitcalls.MySharedPref.isSharedPrefnull
import com.example.s_tools.tools.retrofitcalls.VC.check
import com.example.s_tools.tools.retrofitcalls.MySharedPref.getName
import com.example.s_tools.tools.retrofitcalls.MySharedPref.clearLogin
import com.example.s_tools.main_activity.mainactivity_toolbar_api.Changename.nameChange
import com.example.s_tools.tools.retrofitcalls.MySharedPref.putName
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.animation.Animation
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.app.Dialog
import android.app.ProgressDialog
import com.example.s_tools.tools.Typewriter
import com.example.s_tools.premium.Dashboard_1
import com.kaopiz.kprogresshud.KProgressHUD
import com.example.s_tools.TinyDB
import com.example.s_tools.main_activity._model.MyAdapter
import android.os.Bundle
import com.example.s_tools.R
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import com.example.s_tools.tools.retrofitcalls.VC
import com.example.s_tools.entertainment.VideoActivity
import android.content.Intent
import android.graphics.Color
import com.example.s_tools.testing.DownloadActivity
import com.example.s_tools.chatting.ChatKaro
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.example.s_tools.main_activity.MainActivity
import com.example.s_tools.tools.Validation
import com.example.s_tools.main_activity.mainactivity_toolbar_api.Changename
import com.example.s_tools.tools.Cvalues
import androidx.recyclerview.widget.GridLayoutManager
import com.chootdev.recycleclick.RecycleClick
import com.example.s_tools.Splash_login_reg.SplashScreen
import com.example.s_tools.main_activity._model.Model
import java.util.*

class MainActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var imp_msg1: TextView? = null
    var imp_msg2: TextView? = null
    var imp_msg3: TextView? = null
    var news: TextView? = null
    var imageView: ImageView? = null
    var movedown: Animation? = null
    var a: ObjectAnimator? = null
    var animationset: AnimatorSet? = null
    var back_pressed: Long = 0
    private val mActionBarToolbar: Toolbar? = null
    var typewriter: Typewriter? = null
    private val animationStarted = false
    private val mHandler = Handler()
    var progressDialog: ProgressDialog? = null
    var models: MutableList<Model>? = null
    var dashboard_1: Dashboard_1? = null

    //    ShineButton button;
    var show: KProgressHUD? = null
    var tinyDB: TinyDB? = null
    var myAdapter: MyAdapter? = null
    var messageCount = 0
    var messageCountView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //        button=findViewById(R.id.po_image2);
//        button.init(this);
        tinyDB = TinyDB(this@MainActivity)
        //afterLogin();
//        SharedPreferences sharedPreferences =new SecurePreferences(MainActivity.this,MySharedPref.activity,
//                SYSTEMCR_XML);
//        sharedPreferences.edit().remove(MySharedPref.RECENT).apply();
//        sharedPreferences.edit().remove(MySharedPref.NETFLIX).apply();
//        sharedPreferences.edit().remove(MySharedPref.BOLLYWOOD).apply();
//        sharedPreferences.edit().remove(MySharedPref.HOLLYWOOD).apply();
//        sharedPreferences.edit().remove(MySharedPref.OTHERS).apply();
//        ChangeStatusBarColor.changeStatusbarColor(MainActivity.this, R.color.colorBackgroundAboutLight);
        //autotype example
//         typewriter = new Typewriter(this);
//        imp_msg1.setCharacterDelay(100);
//        imp_msg1.animateText("y for everyohe stefjsnjfasdfasdfsdfasefjsnjfasdfasdfsdfase fnei");
        getid()
        MyAsyncTask().execute()


//        MySharedPref.isValid_checkCookie(MainActivity.this, new MySharedPref.ApiCallback() {
//            @Override
//            public void onResponse(boolean success) {
//                if (success){
//                    Log.e(TAG, "onResponse: success" );
//                }else {
//                    GotoLoginFragment();
//                }
//            }
//        });
    }

    private fun afterLogin() {
        if (!isSharedPrefnull(this@MainActivity)) {
            Thread {
                val randomNum = Random().nextInt(10 - 0 + 1)
                if (randomNum == 0) {
                    check(this@MainActivity) { success: Boolean?, updateinfo: String? -> }
                }
            }.start()
            autoUpdateDialog()
        }
    }

    private fun autoUpdateDialog() {
        val isautoupdateyes = tinyDB!!.getInt(VideoActivity.AUTOUPDATE)
        if (isautoupdateyes == 1) {
            show = KProgressHUD.create(this@MainActivity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Updating...")
                .setCancellable(true).setDimAmount(0.5f).show()
            //updateChat();
        }
    }

    private fun updateChat() {
        //chat
        if (messageCountView != null) {
//            ChatViewModel.getMessageCount(MainActivity.this,MySharedPref.getTokenMessages(MainActivity.this), MySharedPref.getChatid(MainActivity.this), (success, size) -> {
//                if (success) {
//                    if (tinyDB.getInt(CHATMESSAGECOUNT) != size) {
////                        NotificationHelper.showChatnotifiaction(MainActivity.this);
//                        tinyDB.putInt(CHATMESSAGECOUNT, size - tinyDB.getInt(CHATMESSAGECOUNT));
//                        messageCountView.setVisibility(View.VISIBLE);
//                        messageCountView.setText(String.valueOf(tinyDB.getInt(CHATMESSAGECOUNT)));
//                    }
//                }
//            });
        }
    }

    private fun gettitleBarName() {
        val name = getName(this@MainActivity)
        supportActionBar!!.title = name
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboardmenu, menu)
        if (isSharedPrefnull(this@MainActivity)) {
            supportActionBar!!.subtitle = ""
            val item = menu.findItem(R.id.menusetting)
            item.isVisible = false
        }
        val menuItem = menu.findItem(R.id.menusms2).actionView
        val download = menu.findItem(R.id.downloadmanager).actionView
        download.setOnClickListener { view: View? ->
            val intent = Intent(this@MainActivity, DownloadActivity::class.java)
            //                intent.putExtra("intentdownloadlink","https://imagetot.com/images/2020/10/21/dcef8d0a33de61fff33f609af18e3f24.jpg");
            startActivity(intent)
        }
        messageCountView = menuItem.findViewById<View>(R.id.my_badge) as TextView
        val imageView = menuItem.findViewById<ImageView>(R.id.smsmenuimg)
        imageView.setOnClickListener { view: View? ->
            if (!isSharedPrefnull(this@MainActivity)) {
                startActivity(Intent(this@MainActivity, ChatKaro::class.java))
                messageCountView!!.visibility = View.GONE
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menusetting) {
            val dialog = Dialog(this@MainActivity)
            dialog.setContentView(R.layout.mainactivity_setting_pop_changename)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val changename = dialog.findViewById<Button>(R.id.changenamebtn)
            val textView = dialog.findViewById<TextView>(R.id.titlePopup)
            val submit_changename = dialog.findViewById<TextView>(R.id.submit_changename)
            val editText = dialog.findViewById<EditText>(R.id.enterName)
            val progressbar = dialog.findViewById<ProgressBar>(R.id.chnagenameProgressbar)
            val cardView: CardView = dialog.findViewById(R.id.login_btn)
            val changenameImage = dialog.findViewById<ImageView>(R.id.changenameimg)
            val logoutLayout = dialog.findViewById<LinearLayout>(R.id.logoutview)
            val logout = dialog.findViewById<Button>(R.id.logoutbtn)
            logout.setOnClickListener { v: View? ->
                clearLogin(this@MainActivity)
                startActivity(Intent(this@MainActivity, MainActivity::class.java))
                finish()
            }
            changename.setOnClickListener { v: View? ->
                changenameImage.visibility = View.GONE
                logoutLayout.visibility = View.GONE
                textView.text = "Change Name"
                changename.visibility = View.GONE
                editText.visibility = View.VISIBLE
                cardView.visibility = View.VISIBLE
            }
            cardView.setOnClickListener { v: View? ->
                cardView.isEnabled = false
                submit_changename.text = "Please Wait.."
                val isvalidName =
                    Validation.isvalidName(editText.text.toString(), this@MainActivity)
                if (isvalidName) {
                    progressbar.visibility = View.VISIBLE
                    nameChange(
                        this@MainActivity,
                        editText.text.toString(),
                        object : MySharedPref.ApiCallback {
                            override fun onResponse(success: Boolean) {
                                if (!success) {
                                    progressbar.visibility = View.GONE
                                    Toast.makeText(
                                        this@MainActivity,
                                        Cvalues.SESSION_EXPIRED,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(
                                        Intent(
                                            this@MainActivity,
                                            SplashScreen::class.java
                                        )
                                    )
                                    finish()
                                } else {
                                    progressbar.visibility = View.GONE
                                    mActionBarToolbar!!.title = editText.text.toString()
                                    putName(this@MainActivity, editText.text.toString())
                                    Toast.makeText(
                                        this@MainActivity,
                                        Cvalues.SUCCESS,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog.dismiss()
                                }
                            }
                        })
                } else {
                    cardView.isEnabled = true
                    submit_changename.text = "Submit"
                }
            }
            dialog.show()
        }
        return false
    }

    private fun impmsgshowing() {
//        news.setText("Please Read This");
//        imp_msg1.setText("\u25CF" + " Upcomming Updates Only On evrsh.blogspot.com");
//        imp_msg2.setText("\u25CF" + " Any Query? Message Us.");
//        imp_msg3.setText("\u25CF" + " Use VPN if Something Don't Work (ex: Initializing,Movies,Generate Token)");
//         blinkText();
//        imageView.setColorFilter(ActivityCompat.getColor(MainActivity.this, android.R.color.white));
//        movedown =AnimationUtils.loadAnimation(MainActivity.this,R.anim.dashboard_imp_news);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    Thread.sleep(500);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
////        imp_msg1.startAnimation(movedown);
////
////                            imp_msg2.startAnimation(movedown);
////
////
////                            imp_msg3.startAnimation(movedown);
////
//                            //imp_msg3.setText("~ now stuff enjnfjsnjfasdfasdfsdfasefjsnjfasdfasdfsdfase fneinfei efnjenfef efjenfe fee fefnefjsnjfasdfasdfsdfasefjsnjfasdfasdfsdfase fneinfei efnjenfef efjenfe fee fefnefjsnjfasdfasdfsdfasefjsnjfasdfasdfsdfase fneinfei efnjenfef efjenfe fee fefnefjsnjfasdfasdfsdfasefjsnjfasdfasdfsdfase fneinfei efnjenfef efjenfe fee fefnefjsnjfasdfasdfsdfasefjsnjfasdfasdfsdfase fneinfei efnjenfef efjenfe fee fefnand submit yveryone.");
//
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    private fun getid() {
        recyclerView = findViewById(R.id.recycle)
        //        mActionBarToolbar=findViewById(R.id.mainactivity_toolbar);
//        setSupportActionBar(mActionBarToolbar);
//        dashboard_1=new Dashboard_1();
    }

    private inner class MyAsyncTask : AsyncTask<String?, String?, String?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            //  gettitleBarName();
            //  impmsgshowing();
        }

        protected override fun doInBackground(vararg strings: String?): String? {
            models = ArrayList()
            models!!.add(
                Model(
                    R.drawable.ic_baseline_refresh_24,
                    "Premium Posts",
                    "Tools,Premium Accounts,Earn Money etc"
                )
            )
            models!!.add(
                Model(
                    R.drawable.entertainment3,
                    "Entertainment",
                    "Movies,Videos,Wallpapers,Images etc"
                )
            )
            models!!.add(
                Model(
                    R.drawable.settings,
                    "Services",
                    "Token,Youtube,Instagram Downloader etc"
                )
            )
            //            models.add(new Model(R.drawable.gift,  "User Requests", "Requested Thing Goes Here!"));
            return null
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            recyclerView!!.layoutManager = GridLayoutManager(this@MainActivity, 3)
            myAdapter = MyAdapter(models)
            recyclerView!!.adapter = myAdapter
            RecycleClick.addTo(recyclerView)
                .setOnItemClickListener { recyclerView: RecyclerView?, i: Int, view: View? ->
                    if (i == 0) {
                        if (isSharedPrefnull(this@MainActivity)) {
                            startActivity(Intent(this@MainActivity, SplashScreen::class.java))
                        } else {
                            startActivity(Intent(this@MainActivity, Dashboard_1::class.java))
                        }
                    }
                    if (i == 1) {
                        startActivity(Intent(this@MainActivity, VideoActivity::class.java))
                    }
                    if (i == 2) {
//                    startActivity(new Intent(MainActivity.this, ServiceActivity.class));
                    }
                    if (i == 3) {
//                    startAppAd.showAd();

//                    Toast.makeText(MainActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(MainActivity.this,SplashScreen.class));
                        val murl = "https://i.imgur.com/CnCMxBn.jpg"
                        val intent = Intent(this@MainActivity, DownloadActivity::class.java)
                        intent.putExtra("intentdownloadlink", murl)
                        startActivity(intent)
                    }
                }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    } //    public void adSetup() {
    //        new Thread(() -> {
    //            if (!UnityAds.isInitialized()){
    //                UnityAds.initialize(MainActivity.this,GAME_ID, MODE);
    //            }
    //            StartAppSDK.init(MainActivity.this, APP_ID, false);
    //            StartAppSDK.setUserConsent (MainActivity.this, "pas",
    //                    System.currentTimeMillis(),
    //                    true);
    //            StartAppSDK.setTestAdsEnabled(ADS_ENABLED);
    //            StartAppAd.disableAutoInterstitial();
    //            StartAppAd.disableSplash();
    //        }).start();
    //    }
}