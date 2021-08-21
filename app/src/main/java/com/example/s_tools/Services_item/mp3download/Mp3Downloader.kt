package com.example.s_tools.Services_item.mp3download

import android.content.*
import com.example.s_tools.tools.retrofitcalls.MySharedPref.getyoutubemp3
import com.example.s_tools.tools.retrofitcalls.VC.check
import retrofit2.http.GET
import retrofit2.http.Url
import com.google.gson.JsonObject
import okhttp3.ConnectionSpec
import okhttp3.TlsVersion
import okhttp3.OkHttpClient
import com.example.s_tools.Services_item.mp3download.Mp3inter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.webkit.WebView
import com.kaopiz.kprogresshud.KProgressHUD
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import com.example.s_tools.R
import com.example.s_tools.Services_item.mp3download.Mp3Downloader
import com.example.s_tools.tools.ToastMy
import com.example.s_tools.tools.Cvalues
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import android.os.Build
import android.os.Handler
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceError
import android.webkit.WebResourceResponse
import android.webkit.ValueCallback
import com.google.android.gms.ads.AdView
import at.huber.youtubeExtractor.YouTubeExtractor
import android.util.SparseArray
import android.view.MenuItem
import at.huber.youtubeExtractor.YtFile
import at.huber.youtubeExtractor.VideoMeta
import com.squareup.picasso.Picasso
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import com.example.s_tools.tools.retrofitcalls.VC
import org.jsoup.Jsoup
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.s_tools.testing.DownloadActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import okhttp3.CertificatePinner
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.ArrayList
import java.util.regex.Pattern

internal interface Mp3inter {
    @GET
    fun getPremiumPost(@Url url: String?): Call<JsonObject?>

    companion object {
        const val core_url = "https://x2convert.com/ajax2/"
        const val PINS = "sha256/gGOcYKAwzEaUfun6YdxZvFSQq/x2lF/R8UizDFofveY="
        val spec: ConnectionSpec =
            ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).cipherSuites(
                TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
            ).build()
        const val PATTERN = "evrsh.blogpsotcom"
        val client: OkHttpClient = OkHttpClient.Builder().connectionSpecs(listOf(spec)).certificatePinner(
            CertificatePinner.Builder().add(
                PATTERN, PINS
            ).build()
        ).build()

        //Creating retrofit Object
        val retrofit = Retrofit.Builder().baseUrl(core_url).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}

class Mp3Downloader : AppCompatActivity() {
    var fullUrl = "https://www.youtube.com/watch?v="
    var img: ImageView? = null
    var fetch: CardView? = null
    var container: LinearLayout? = null
    var editText: EditText? = null
    var fetch_txt: TextView? = null
    var title: TextView? = null
    var length: TextView? = null
    var d1: Button? = null
    var d2: Button? = null
    var paste: Button? = null
    var progressBar: ProgressBar? = null
    var link320: String? = null
    var link128: String? = null
    var toolbar: Toolbar? = null
    var youtubeUrl: String? = null
    var webView: WebView? = null
    var progressHUD: KProgressHUD? = null
    var recyclerView: RecyclerView? = null
    var list: MutableList<Model>? = null
    var adapter: Adapter? = null
    var running = false
    var server1: RadioButton? = null
    var server2: RadioButton? = null
    var Converting = true
    private var count = 20
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mp3_downloader)
        init()
        Handler().postDelayed({ adsetup() }, 200)
        fetch!!.setOnClickListener { v: View? ->
            if (editText!!.text.toString().length > 6) {
                running = true
                youtubeUrl = editText!!.text.toString()
                if (youtubeUrl!!.contains("music")) {
                    youtubeUrl = youtubeUrl!!.replace("music", "www")
                }
                progressBar!!.visibility = View.VISIBLE
                Log.e(TAG, "onCreate: " + fullUrl + getVideoIdFromYoutubeUrl(youtubeUrl))
                Fetchdata(this@Mp3Downloader).extract(
                    fullUrl + getVideoIdFromYoutubeUrl(youtubeUrl),
                    true,
                    false
                )
            } else {
                ToastMy.errorToast(this@Mp3Downloader, Cvalues.INVALID_URL, ToastMy.LENGTH_SHORT)
            }
        }
        paste!!.setOnClickListener { v: View? ->
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            // If it does contain data, decide if you can handle the data.
            if (!clipboard.hasPrimaryClip()) {
            } else if (!clipboard.primaryClipDescription!!.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                // since the clipboard has data but it is not plain text
            } else {
                //since the clipboard contains plain text.
                val item = clipboard.primaryClip!!.getItemAt(0)

                // Gets the clipboard as text.
                editText!!.setText(item.text.toString())
            }
        }
    }

    private fun server2() {
        progressHUD!!.show()
        progressHUD!!.setProgress(10)
        progressHUD!!.setLabel(GETTING_HASH_KEY)
        progressHUD!!.setDetailsLabel(MORE_THEN_30_SECONDS)
        Thread(object : Runnable {
            override fun run() {
                try {
                    Thread.sleep(600)
                    runOnUiThread { webviewSetup() }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            private fun webviewSetup() {
                webView = WebView(applicationContext)
                webView!!.settings.javaScriptEnabled = true
                webView!!.settings.javaScriptCanOpenWindowsAutomatically = true
                webView!!.loadUrl(YOUTUBE_TO_MP_3_MUSIC)
                webView!!.webViewClient = object : WebViewClient() {
                    var reloaded = false
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    override fun onReceivedError(
                        view: WebView,
                        request: WebResourceRequest,
                        error: WebResourceError
                    ) {
                        super.onReceivedError(view, request, error)
                        progressHUD!!.dismiss()
                        Toast.makeText(this@Mp3Downloader, Cvalues.TRY_AGAIN, Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onReceivedHttpError(
                        view: WebView,
                        request: WebResourceRequest,
                        errorResponse: WebResourceResponse
                    ) {
                        super.onReceivedHttpError(view, request, errorResponse)
                        progressHUD!!.dismiss()
                        Toast.makeText(this@Mp3Downloader, Cvalues.TRY_AGAIN, Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onPageFinished(view: WebView, url: String) {
                        super.onPageFinished(view, url)
                        progressHUD!!.setProgress(20)
                        progressHUD!!.setLabel(Cvalues.CONVERTING)
                        progressHUD!!.setDetailsLabel("Hold On")
                        Thread {
                            while (Converting) {
                                try {
                                    Thread.sleep(500)
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                                runOnUiThread {
                                    if (Converting) {
                                        if (count == 80) {
                                            Converting = false
                                        }
                                        progressHUD!!.setProgress(count++)
                                    }
                                }
                            }
                        }.start()
                        view.evaluateJavascript(runJavaScript(youtubeUrl)) { s: String? ->
                            if (reloaded) {
                                val mp3inter = Mp3inter.retrofit.create(
                                    Mp3inter::class.java
                                )
                                mp3inter.getPremiumPost(GET_FILE_ASHX_LINKINFO + youtubeUrl + LANG_EN_OPTION_COUNTRY_IN)
                                    .enqueue(object : Callback<JsonObject?> {
                                        override fun onResponse(
                                            call: Call<JsonObject?>,
                                            response: Response<JsonObject?>
                                        ) {
                                            Converting = false
                                            try {
                                                progressHUD!!.setLabel("Converted Successful")
                                                progressHUD!!.setDetailsLabel(Cvalues.ALMOST_FINISH)
                                                val message = response.body()!!["Message"].asString
                                                link128 = message
                                                progressHUD!!.setProgress(80)
                                                finalcall()
                                            } catch (e: Exception) {
                                                progressHUD!!.dismiss()
                                                ToastMy.errorToast(
                                                    this@Mp3Downloader,
                                                    Cvalues.TRY_AGAIN,
                                                    ToastMy.LENGTH_SHORT
                                                )
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<JsonObject?>,
                                            t: Throwable
                                        ) {
                                            Log.e(TAG, "failed:1 " + t.message)
                                            ToastMy.errorToast(
                                                this@Mp3Downloader,
                                                Cvalues.TRY_AGAIN,
                                                ToastMy.LENGTH_SHORT
                                            )
                                            progressHUD!!.dismiss()
                                        }
                                    })
                            } else {
                                reloaded = true
                            }
                        }
                    }
                }
            }
        }).start()
    }

    private fun adsetup() {
        var adView = AdView(this)
        adView.adSize = AdSize.BANNER
        adView.adUnitId = Cvalues.banr
        adView = findViewById(R.id.adView)
        adView.visibility = View.VISIBLE
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    internal inner class Fetchdata(con: Context) : YouTubeExtractor(con) {
        override fun onPreExecute() {
            super.onPreExecute()
            progressBar!!.visibility = View.VISIBLE
            container!!.visibility = View.GONE
            recyclerView!!.visibility = View.GONE
            link128 = null
            link320 = null
        }

        public override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta?) {
            Log.e(TAG, "onExtractionComplete: $vMeta files $ytFiles")
            if (vMeta != null) {
                runOnUiThread {
                    fetch_txt!!.text = "Fetch"
                    container!!.visibility = View.VISIBLE
                    Picasso.get().load(vMeta.hqImageUrl)
                        .placeholder(R.drawable.ic_baseline_image_24).into(img)
                    title!!.text = vMeta.title
                    length!!.text =  "${vMeta.videoLength / 60} min"
                    progressHUD = KProgressHUD.create(this@Mp3Downloader)
                        .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                        .setLabel(Cvalues.CONVERTING).setCancellable(false).setDimAmount(0.1f)
                        .setMaxProgress(100).show()
                    if (server1!!.isChecked) {
                        server2()
                    } else {
                        ToastMy.errorToast(
                            this@Mp3Downloader,
                            "Server Down! Choose Another Server",
                            ToastMy.LENGTH_LONG
                        )
                    }
                }
            } else {
                ToastMy.errorToast(this@Mp3Downloader, Cvalues.UNABLE, ToastMy.LENGTH_LONG)
            }
            progressBar!!.visibility = View.GONE
            running = false
        }

        override fun onPostExecute(ytFiles: SparseArray<YtFile>) {
            super.onPostExecute(ytFiles)
            Log.e(TAG, "onPostExecute: ")
        }
    }

    private fun checkVersion(videoId: String, title: String) {
        if (getyoutubemp3(this@Mp3Downloader) == null) {
            check(this@Mp3Downloader) { success: Boolean, updateinfo: String? ->
                if (success) {
                    down_320(videoId, title)
                } else {
                    Toast.makeText(this, "Server Error: Please Try Again!", Toast.LENGTH_SHORT)
                        .show()
                    progressBar!!.visibility = View.GONE
                    running = false
                    progressHUD!!.dismiss()
                }
            }
        } else {
            Log.e(TAG, "checkVersion: ")
            down_320(videoId, title)
            //            fetchMp3();
        }
    }

    private fun fetchMp3() {
        Thread {
            try {
                val document = Jsoup.connect(
                    getyoutubemp3(this@Mp3Downloader) + getVideoIdFromYoutubeUrl(youtubeUrl)
                )
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                    .referrer("http://www.google.com").ignoreContentType(true).get()
                val elementsByAttribute = document.body().getElementsByAttribute(Cvalues.HREF)
                val text = document.getElementsByClass(TEXT_SHADOW_1).text()
                list = ArrayList()
                for (i in elementsByAttribute.indices) {
                    val attributes = elementsByAttribute[i].attributes()
                    if (elementsByAttribute[i].toString()
                            .contains("download") && text.contains("MP3")
                    ) {
                        list!!.add(
                            Model(
                                attributes[Cvalues.HREF],
                                text.split("MP3".toRegex()).toTypedArray()[i + 1]
                            )
                        )
                    }
                }
                //                adapter=new Adapter(Mp3Downloader.this, list);
                runOnUiThread {
                    if (list!!.isEmpty()) {
                        ToastMy.errorToast(this@Mp3Downloader, "Try Again!", ToastMy.LENGTH_SHORT)
                    }
                    running = false
                    progressBar!!.visibility = View.GONE
                    progressHUD!!.dismiss()
                    recyclerView!!.visibility = View.VISIBLE
                    //                    recyclerView.setLayoutManager(new LinearLayoutManager(Mp3Downloader.this,LinearLayoutManager.VERTICAL,false));
                    recyclerView!!.layoutManager = GridLayoutManager(this@Mp3Downloader, 2)
                    recyclerView!!.adapter = adapter
                }
            } catch (e: Exception) {
                ToastMy.errorToast(this@Mp3Downloader, Cvalues.OFFLINE, ToastMy.LENGTH_SHORT)
                e.printStackTrace()
            }
        }.start()
    }

    private fun init() {
        paste = findViewById(R.id.pastedata)
        img = findViewById(R.id.mp3img)
        editText = findViewById(R.id.mp3_edit)
        progressBar = findViewById(R.id.mp3progress)
        container = findViewById(R.id.downloadContainer)
        fetch = findViewById(R.id.mp3get)
        toolbar = findViewById(R.id.mp3toolbar)
        fetch_txt = findViewById(R.id.fetchmp3)
        title = findViewById(R.id.titleofvid)
        length = findViewById(R.id.lvid)
        recyclerView = findViewById(R.id.listview)
        server1 = findViewById(R.id.server1)
        server2 = findViewById(R.id.server2)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun getVideoIdFromYoutubeUrl(url: String?): String? {
        var videoId: String? = null
        val pattern = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(url)
        if (matcher.find()) {
            videoId = matcher.group(1)
        }
        return videoId
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun down_320(url: String, title: String) {
        progressHUD!!.setLabel(Cvalues.CONVERTING)
        Thread {
            var min = 1
            while (progressHUD!!.isShowing) {
                try {
                    Thread.sleep(200)
                    val finalMin = min
                    runOnUiThread { progressHUD!!.setProgress(finalMin) }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                min += 1
            }
        }.start()
        Thread {
            try {
                val document = Jsoup.connect(getyoutubemp3(this@Mp3Downloader) + url).get()
                runOnUiThread { progressHUD!!.setProgress(99) }
                val byAttribute = document.getElementsByAttribute("action")
                Log.e(TAG, "down_320:2 " + byAttribute.attr("action"))
                link320 = byAttribute.attr("action")
                list = ArrayList()
                if (link320!!.contains("320/")) {
                    link128 = link320
                    val replace = link128!!.replace("320/", "128/")
                    list!!.add(Model(replace, "MP3 128KBPS"))
                    list!!.add(Model(link320, "MP3 320KBPS"))
                } else if (link320!!.contains("128/")) {
                    list!!.add(Model(link320, "MP3 128KBPS"))
                }
                adapter = Adapter(this@Mp3Downloader, list, title)
                runOnUiThread {
                    if (list!!.isEmpty()) {
                        ToastMy.errorToast(this@Mp3Downloader, Cvalues.UNABLE, ToastMy.LENGTH_SHORT)
                    }
                    progressHUD!!.dismiss()
                    running = false
                    progressBar!!.visibility = View.GONE
                    recyclerView!!.visibility = View.VISIBLE
                    recyclerView!!.layoutManager = GridLayoutManager(this@Mp3Downloader, 2)
                    recyclerView!!.adapter = adapter
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Log.e(TAG, "down_320:error " + e.message)
                    progressHUD!!.dismiss()
                    ToastMy.errorToast(this@Mp3Downloader, Cvalues.TRY_AGAIN, ToastMy.LENGTH_SHORT)
                }
            }
        }.start()
    }

    fun finalcall() {
        webView!!.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                progressHUD!!.dismiss()
                Toast.makeText(this@Mp3Downloader, Cvalues.TRY_AGAIN, Toast.LENGTH_SHORT).show()
            }

            override fun onReceivedHttpError(
                view: WebView,
                request: WebResourceRequest,
                errorResponse: WebResourceResponse
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                progressHUD!!.dismiss()
                Toast.makeText(this@Mp3Downloader, Cvalues.TRY_AGAIN, Toast.LENGTH_SHORT).show()
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressHUD!!.setProgress(90)
                view.evaluateJavascript(JS) { s: String? ->
                    progressHUD!!.setLabel("Starting Download")
                    progressHUD!!.setDetailsLabel("")
                }
            }
        }
        webView!!.loadUrl(link128!!)
        webView!!.setDownloadListener { s: String?, s1: String?, s2: String?, s3: String?, l: Long ->
            progressHUD!!.dismiss()
            ToastMy.successToast(this@Mp3Downloader, "Downloading...", ToastMy.LENGTH_SHORT)
            val intent = Intent(this@Mp3Downloader, DownloadActivity::class.java)
            intent.putExtra(DownloadActivity.INTENTDOWNLINK, s)
            startActivity(intent)
            webView!!.destroy()
        }
    }

    fun runJavaScript(url: String?): String {
        return ELEMENT_BY_ID_TXT_LINK_VALUE + url + DOCUMENT_GET_ELEMENT_BY_ID_BTN_GET_CLICK
    }

    companion object {
        const val TAG = "//"
        const val YOUTUBE_COM_V_7_WATCH_V = "https://www.320youtube.com/v7/watch?v="
        const val BTN_BTN_SUCCESS_BTN_LG = "btn btn-success btn-lg"
        const val REGEX =
            "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)"
        const val JS =
            "javascript:(function(){" + "l=document.getElementsByClassName('btn btn-lg btn-success')[0];" + "l.click();" + "})()"
        const val YOUTUBE_TO_MP_3_MUSIC = "https://x2convert.com/download-youtube-to-mp3-music"
        const val GET_FILE_ASHX_LINKINFO = "https://x2convert.com/ajax2/getFile.ashx?linkinfo="
        const val LANG_EN_OPTION_COUNTRY_IN = "&lang=en&option=&country=IN/"
        const val ELEMENT_BY_ID_TXT_LINK_VALUE =
            "javascript:document.getElementById('txtLink').value='"
        const val DOCUMENT_GET_ELEMENT_BY_ID_BTN_GET_CLICK =
            "';document.getElementById('btnGet').click()"
        const val MORE_THEN_30_SECONDS = "it won't take more then 30 seconds."
        const val TEXT_SHADOW_1 = "text-shadow-1"
        private const val GETTING_HASH_KEY = "Getting Hash Key"
    }
}