package com.example.s_tools

import android.Manifest
import android.app.Dialog
import com.example.s_tools.tools.retrofitcalls.MySharedPref.isVersionOut
import com.example.s_tools.tools.retrofitcalls.MySharedPref.clearLogin
import com.example.s_tools.tools.retrofitcalls.VC.check_v
import com.example.s_tools.tools.retrofitcalls.VC.check_specific
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.ProgressBar
import com.example.s_tools.R
import com.example.s_tools.Splash_after_reg
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.RequestConfiguration
import android.content.Intent
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import android.graphics.drawable.ColorDrawable
import com.example.s_tools.TinyDB
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow
import com.example.s_tools.tools.retrofitcalls.VC
import com.example.s_tools.entertainment.VideoActivity
import com.example.s_tools.tools.ToastMy
import com.example.s_tools.tools.Cvalues
import com.download.library.DownloadImpl
import com.download.library.DownloadListenerAdapter
import com.download.library.Extra
import android.net.ConnectivityManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.mymodel.MyMovies
import java.io.File
import java.util.*

class Splash_after_reg : AppCompatActivity() {
    var textView: TextView? = null
    var progressBar: ProgressBar? = null
    var downloadsuccess = false
    var downloadpath: String? = null
    var retrying = 0
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        Splash_after_reg.this.overridePendingTransition(R.anim.enter_anim_from_bottom, R.anim.exit_enter_from_bottom);
        setContentView(R.layout.splashscreen_after_register)
        textView = findViewById(R.id.stater)
        fill_string()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                1
            )
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                101
            )
        }
        checkUpdate()
        Handler(Looper.getMainLooper()).postDelayed({
            MobileAds.initialize(this) { initializationStatus: InitializationStatus? ->
                val configuration = RequestConfiguration.Builder().setTestDeviceIds(
                    Arrays.asList(
                        "0C9EBB7EE878C7C3529A8D806DC4FFCA",
                        "EDB30A4A00115883E90E49CAD034A12C"
                    )
                ).build()
                MobileAds.setRequestConfiguration(configuration)
            }
        }, 200)
    }

    private fun checkSystemWritePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this@Splash_after_reg)) return true else openAndroidPermissionsMenu()
        }
        return false
    }

    private fun openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }
    }

    private fun checkUpdate(): Boolean {
        return Handler(Looper.getMainLooper()).postDelayed({
            if (isVersionOut(this@Splash_after_reg)) {
                val dialog = Dialog(this@Splash_after_reg)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.update_dilog)
                val retry = dialog.findViewById<TextView>(R.id.loginbtnPopup)
                progressBar = dialog.findViewById(R.id.progressBar1)
                val status = dialog.findViewById<TextView>(R.id.herelink)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
                retry.setOnClickListener { v: View? ->
                    if (downloadsuccess) {
                        val tinyDB = TinyDB(this@Splash_after_reg)
                        tinyDB.putLong("lastcheck", 0)
                        clearLogin(this@Splash_after_reg)
                        //                        Intent intent=new Intent(Intent.ACTION_VIEW);
//                        File file=new File(downloadpath);
//                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//                        startActivity(intent);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (!packageManager.canRequestPackageInstalls()) {
                                startActivityForResult(
                                    Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(
                                        Uri.parse(String.format("package:%s", packageName))
                                    ), 1234
                                )
                            }
                        }
                        installAPK(downloadpath)

//
//                        File toInstall = new File(downloadpath);
//                        if (android.os.Build.VERSION.SDK_INT >= 29) {
//                            Uri contentUri = FileProvider.getUriForFile(Splash_after_reg.this
//                                    , "package_name.fileprovider", toInstall);
//                            Intent intent2 = new Intent(Intent.ACTION_INSTALL_PACKAGE);
//                            intent2.setData(contentUri);
//                            intent2.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            startActivity(intent2);
//                        } else {
//                            Uri apkUri = Uri.fromFile(toInstall);
//                            Intent intent3 = new Intent(Intent.ACTION_VIEW);
//                            intent3.setDataAndType(apkUri, "application/vnd.android.package-archive");
//                            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent3);
//                        }
//
//                    }
                    } else {
                        if (isNetworkConnected) {
                            retry.visibility = View.GONE
                            status.text = "Getting Change Logs: "
                            updateapp(
                                status,
                                retry,
                                PhotoFullPopupWindow.isStoragePermissionGranted(this@Splash_after_reg)
                            )
                        } else {
                            status.text =
                                "Internet is not connected! Please connect to internet and press retry button below"
                            progressBar!!.setVisibility(View.GONE)
                            retry.visibility = View.VISIBLE
                        }
                    }
                }
                if (isNetworkConnected) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        status.text = "Getting Change Logs: "
                    }, 1000)
                    updateapp(
                        status,
                        retry,
                        PhotoFullPopupWindow.isStoragePermissionGranted(this@Splash_after_reg)
                    )
                } else {
                    status.text =
                        "Internet is not connected! Please connect to internet and press retry button below"
                    progressBar!!.setVisibility(View.GONE)
                    retry.visibility = View.VISIBLE
                }
            } else {
                val tinyDB = TinyDB(this@Splash_after_reg)
                if (tinyDB.getLong("lastcheck") == 0L) {
                    PhotoFullPopupWindow.isStoragePermissionGranted(this@Splash_after_reg)
                    val firsttime = findViewById<TextView>(R.id.setup)
                    firsttime.visibility = View.VISIBLE
                    firsttime.startAnimation(
                        AnimationUtils.loadAnimation(
                            this@Splash_after_reg,
                            R.anim.blink_1500
                        )
                    )
                    check_v(this@Splash_after_reg) { success: Boolean, updateinfo: String? ->
                        firsttime.clearAnimation()
                        firsttime.visibility = View.GONE
                        if (success && updateinfo != null) {
                            checkUpdate()
                        } else if (success) {
                            tinyDB.putLong("lastcheck", System.currentTimeMillis())
                            startActivity(Intent(this@Splash_after_reg, VideoActivity::class.java))
                        } else {
                            if (retrying == 0) {
                                ToastMy.errorToast(
                                    this@Splash_after_reg,
                                    "No Response. Retrying...",
                                    ToastMy.LENGTH_SHORT
                                )
                                retrying++
                                checkUpdate()
                            } else if (retrying == 1) {
                                ToastMy.errorToast(
                                    this@Splash_after_reg,
                                    "Again No Response. Retrying Last Time...",
                                    ToastMy.LENGTH_SHORT
                                )
                                retrying++
                                checkUpdate()
                            } else {
                                ToastMy.errorToast(
                                    this@Splash_after_reg,
                                    Cvalues.OFFLINE,
                                    ToastMy.LENGTH_SHORT
                                )
                            }
                        }
                    }
                } else {
                    startActivity(Intent(this@Splash_after_reg, VideoActivity::class.java))
                }
            }
        }, 1500)
    }

    private fun updateapp(status: TextView, retry: TextView, storagepermisiongranted: Boolean) {
        check_specific(
            this@Splash_after_reg,
            false,
            false,
            true
        ) { success, updateinfo ->
            if (success) {
                val split = updateinfo!!.split(",".toRegex()).toTypedArray()
                status.text = "Change Logs: "
                for (i in 1 until split.size) {
                    status.append(
                        """
    
    ● ${split[i]}
    """.trimIndent()
                    )
                }
                status.append("\n\nDownloading Started...")
                DownloadImpl.getInstance().with(this@Splash_after_reg)
                    .url(split[0].trim { it <= ' ' }).target(
                        if (storagepermisiongranted) Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS
                        ) else File(this.externalCacheDir, "mbuddy.apk")
                    ).setForceDownload(true).setRetry(2).setUniquePath(true).setQuickProgress(true)
                    .setOpenBreakPointDownload(true)
                    .enqueue(object : DownloadListenerAdapter() {
                        override fun onStart(
                            url: String,
                            userAgent: String,
                            contentDisposition: String,
                            mimetype: String,
                            contentLength: Long,
                            extra: Extra
                        ) {
                            super.onStart(
                                url,
                                userAgent,
                                contentDisposition,
                                mimetype,
                                contentLength,
                                extra
                            )
                            progressBar!!.visibility = View.VISIBLE
                        }

                        override fun onProgress(
                            url: String,
                            downloaded: Long,
                            length: Long,
                            usedTime: Long
                        ) {
                            super.onProgress(url, downloaded, length, usedTime)
                            progressBar!!.progress = (downloaded * 100.0f / length).toInt()
                        }

                        override fun onResult(
                            throwable: Throwable,
                            path: Uri,
                            url: String,
                            extra: Extra
                        ): Boolean {
                            if (throwable == null) {
                                progressBar!!.visibility = View.GONE
                                status.append("\n\nTip: if you have any difficulty to install this app , then uninstall this app first then goto buddymy.blogspot.com to download from there and install latest version.\n\nDownload Completed!")
                                downloadsuccess = true
                                downloadpath = path.path
                                retry.text = "Install Now"
                                retry.visibility = View.VISIBLE
                            }
                            return true
                        }
                    })
            } else {
                progressBar!!.visibility = View.GONE
                status.text = Cvalues.OFFLINE
            }
        }
    }

    private val isNetworkConnected: Boolean
        private get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }

    fun installAPK(path: String?) {
        val file = File(path)
        if (file.exists()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(
                uriFromFile(applicationContext, File(path)),
                "application/vnd.android.package-archive"
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                applicationContext.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Log.e("TAG", "Error in opening the file!")
            }
        } else {
            Toast.makeText(applicationContext, "installing", Toast.LENGTH_LONG).show()
        }
    }

    fun uriFromFile(context: Context?, file: File?): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context!!,
                BuildConfig.APPLICATION_ID + ".provider",
                file!!
            )
        } else {
            Uri.fromFile(file)
        }
    }

    companion object {
        const val TAG = "//"

        //    public static final String APP_ID="209328409";
        //    public static final String PAS="pas";
        @JvmField
        var weburl = ""
        fun fill_string() {
            Thread {
                weburl = String(
                    Base64.decode(
                        "aHR0cHM6Ly9oaWRkZW5zdG9vbC5jdS5tYS9tc3Rvb2wvd2ViLw==",
                        Base64.DEFAULT
                    )
                )
                MyMovies.CU_MA = String(Base64.decode("aGlkZGVuc3Rvb2wuY3UubWE=", Base64.DEFAULT))
            }.start()
            Thread {
                val ic = "Y2EtYXBwLXB1Yi0zODEwNzk0NDY1NjM2NDgyLzYyODE5MDEzODI="
                val ic2 = "Y2EtYXBwLXB1Yi0zOTQwMjU2MDk5OTQyNTQ0LzEwMzMxNzM3MTI="
                val br = "Y2EtYXBwLXB1Yi0zODEwNzk0NDY1NjM2NDgyLzM4NTA2MzkxOTM="
                val br2 = "Y2EtYXBwLXB1Yi0zOTQwMjU2MDk5OTQyNTQ0LzYzMDA5NzgxMTE="
                val vd = "Y2EtYXBwLXB1Yi0zODEwNzk0NDY1NjM2NDgyLzQxMTE5MzE0MDM="
                val vd2 = "Y2EtYXBwLXB1Yi0zOTQwMjU2MDk5OTQyNTQ0LzUyMjQzNTQ5MTc="
                Cvalues.inter = String(Base64.decode(ic, Base64.DEFAULT))
                Cvalues.banr = String(Base64.decode(br, Base64.DEFAULT))
                Cvalues.vid = String(Base64.decode(vd, Base64.DEFAULT))
            }.start()
        }
    }
}