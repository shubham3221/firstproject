package com.example.s_tools.Services_item.VidDownloader

import android.content.*
import com.example.s_tools.tools.retrofitcalls.MySharedPref.getyoutubeVideo_part1
import com.example.s_tools.tools.retrofitcalls.MySharedPref.getyoutubeVideo_part2
import com.example.s_tools.tools.retrofitcalls.VC.check_specific
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.cardview.widget.CardView
import com.example.s_tools.Services_item.VidDownloader.RecyclerAdapter2
import com.kaopiz.kprogresshud.KProgressHUD
import android.os.Bundle
import android.util.Log
import com.example.s_tools.R
import at.huber.youtubeExtractor.YouTubeExtractor
import android.util.SparseArray
import android.view.MenuItem
import at.huber.youtubeExtractor.YtFile
import at.huber.youtubeExtractor.VideoMeta
import com.example.s_tools.tools.ToastMy
import com.example.s_tools.tools.Cvalues
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s_tools.Services_item.VidDownloader.VideoDownloader
import org.jsoup.Jsoup
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import org.json.JSONObject
import com.example.s_tools.Services_item.VidDownloader.VideoModel
import com.chootdev.recycleclick.RecycleClick
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.s_tools.Services_item.mp3download.Adapter
import com.example.s_tools.Services_item.mp3download.Model
import com.example.s_tools.testing.DownloadActivity
import org.json.JSONException
import kotlin.Throws
import com.google.android.gms.ads.AdView
import com.example.s_tools.tools.retrofitcalls.VC
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import org.jsoup.Connection
import java.lang.Exception
import java.util.ArrayList
import java.util.regex.Pattern

class VideoDownloader : AppCompatActivity() {
    //    private static final String FORM_URL_ENCODED="application/x-www-form-urlencoded";
    var recyclerView: RecyclerView? = null
    var enterurl: EditText? = null
    var textView: TextView? = null
    var fetchtxt: TextView? = null
    var progressBar: ProgressBar? = null
    var fetchbtn: CardView? = null
    private var adapter: RecyclerAdapter? = null
    private var adapter2: RecyclerAdapter2? = null
    var toolbar: Toolbar? = null
    var paste: Button? = null
    var downloadlinks: MutableList<String>? = null
    var onlineRecyclerAdapter: Adapter? = null
    var fullUrl = "https://www.youtube.com/watch?v="
    var server1: RadioButton? = null
    var server2: RadioButton? = null
    var list: List<Model>? = null
    var server2Layout: LinearLayout? = null
    var running = false
    private var progressHUD: KProgressHUD? = null
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_downloader)
        init()
        loadad()
        fetchbtn!!.setOnClickListener { v: View? ->
            count = 0
            if (enterurl!!.text.toString().length > 5 && !running) {
                running = true
                //                InputMethodManager imm=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
                var url = fullUrl + getVideoIdFromYoutubeUrl(enterurl!!.text.toString())
                if (url.contains("music")) {
                    url = url.replace("music", "www")
                }
                if (server1!!.isChecked) {
                    progressBar!!.visibility = View.VISIBLE
                    server2Layout!!.visibility = View.GONE
                    Fetchdata(this@VideoDownloader).extract(url, true, false)
                } else {
                    progressHUD = KProgressHUD.create(this@VideoDownloader)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Fetching...")
                        .setCancellable(false).setDimAmount(0.1f).show()
                    checkVersion()
                }
            }
        }
        paste!!.setOnClickListener { v: View? ->
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            // If it does contain data, decide if you can handle the data.
            if (!clipboard.hasPrimaryClip()) {
            } else if (!clipboard.primaryClipDescription!!.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                // since the clipboard has data but it is not plain text
            } else {
                val item = clipboard.primaryClip!!.getItemAt(0)
                enterurl!!.setText(item.text.toString())
            }
        }
        //        clear.setOnClickListener(view -> enterurl.setText(""));
    }

    internal inner class Fetchdata(con: Context) : YouTubeExtractor(con) {
        public override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta?) {
            if (ytFiles != null) {
                val list: MutableList<VideoMeta?> = ArrayList()
                downloadlinks = ArrayList()
                list.add(vMeta)
                //                downloadlinks.add(ytFiles.get(22).getUrl());
//                downloadlinks.add(ytFiles.get(18).getUrl());
                var i = 0
                var itag: Int
                while (i < ytFiles.size()) {
                    itag = ytFiles.keyAt(i)
                    val ytFile = ytFiles[itag]
                    if (ytFile.format.audioBitrate != -1) {
                        downloadlinks!!.add(ytFile.url)
                    }
                    i++
                }
                adapter = RecyclerAdapter(this@VideoDownloader, list, downloadlinks)
                publishProgress()
            } else {
                running = false
                progressBar!!.visibility = View.GONE
                ToastMy.errorToast(this@VideoDownloader, Cvalues.OFFLINE, ToastMy.LENGTH_LONG)
            }
        }

        override fun onProgressUpdate(vararg values: Void) {
            super.onProgressUpdate(*values)
            fetchtxt!!.text = "Fetch"
            progressBar!!.visibility = View.GONE
            recyclerView!!.layoutManager = LinearLayoutManager(this@VideoDownloader)
            recyclerView!!.adapter = adapter
            running = false
        }
    }

    private fun method2() {
        Thread {
            try {
                Log.e(TAG, "method2: ")
                publishProgress("Getting Hash Key...")
                ajexSearch(enterurl!!.text.toString())
            } catch (e: Exception) {
                progressHUD!!.dismiss()
                ToastMy.errorToast(this@VideoDownloader, Cvalues.OFFLINE, ToastMy.LENGTH_SHORT)
            }
        }.start()
    }

    private fun publishProgress(msg: String) {
        runOnUiThread { progressHUD!!.setLabel(msg) }
    }

    private fun ajexSearch(token: String) {
        try {
            publishProgress("Getting Download Links")
            val response = Jsoup.connect(
                getyoutubeVideo_part1(this@VideoDownloader)!!.trim { it <= ' ' } + token)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                .referrer("http://www.google.com").ignoreContentType(true).method(
                Connection.Method.POST
            ).header("Content-Type", "application/x-www-form-urlencoded")
                .data("q", enterurl!!.text.toString()).data("vt", "home").execute()
            Toast.makeText(this, "" + response, Toast.LENGTH_SHORT).show()
            publishProgress("Decrypting Download Links")
            val body = response.body()
            val obj = JSONObject(body)
            val videoModels = inserting_data(obj)
            publishProgress("Successful")
            adapter2 = RecyclerAdapter2(videoModels, this@VideoDownloader)
            runOnUiThread {
                count = 0
                server2Layout!!.visibility = View.VISIBLE
                fetchtxt!!.text = "Fetch"
                recyclerView!!.layoutManager = LinearLayoutManager(this@VideoDownloader)
                recyclerView!!.adapter = adapter2
                progressHUD!!.dismiss()
                running = false
                adapter2!!.notifyDataSetChanged()
            }
            RecycleClick.addTo(recyclerView)
                .setOnItemClickListener { recyclerView: RecyclerView?, i: Int, view: View? ->
                    progressHUD!!.show()
                    progressHUD!!.setLabel("Decrypting...")
                    ajaxConvert(token, videoModels[0].id, videoModels[i].convertid)
                }
            adapter2!!.notifyDataSetChanged()
        } catch (e: Exception) {
            if (e.message!!.contains("timeout")) {
                if (count == 3) {
                    runOnUiThread {
                        ToastMy.errorToast(
                            this@VideoDownloader,
                            e.localizedMessage,
                            ToastMy.LENGTH_SHORT
                        )
                        progressHUD!!.dismiss()
                    }
                } else {
                    count++
                    ajexSearch(token)
                }
            }
        }
    }

    private fun ajaxConvert(token: String, vid: String, convertID: String) {
        Thread {
            try {
                val response = Jsoup.connect(
                    getyoutubeVideo_part2(this@VideoDownloader)!!.trim { it <= ' ' } + token)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                    .referrer("http://www.google.com").ignoreContentType(true).method(
                    Connection.Method.POST
                ).timeout(7000).header("Content-Type", "application/x-www-form-urlencoded")
                    .data("vid", vid).data("k", convertID).execute()
                val body = response.body()
                val obj = JSONObject(body)
                Log.e(TAG, "ajaxConvert: $obj")
                val dlink = obj.has("dlink")
                runOnUiThread {
                    count = 0
                    progressHUD!!.dismiss()
                    if (dlink) {
                        Log.e(TAG, "ajaxConvert: " + obj.optString("dlink"))
                        val intent = Intent(this@VideoDownloader, DownloadActivity::class.java)
                        intent.putExtra(DownloadActivity.INTENTDOWNLINK, obj.optString("dlink"))
                        try {
                            intent.putExtra(
                                DownloadActivity.FILENAME,
                                obj.getString("title") + "." + obj.getString("ftype")
                            )
                        } catch (e: JSONException) {
                            Toast.makeText(this, "" + e.localizedMessage, Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                        startActivity(intent)
                    } else {
                        ToastMy.errorToast(
                            this@VideoDownloader,
                            Cvalues.OFFLINE,
                            ToastMy.LENGTH_SHORT
                        )
                    }
                }
            } catch (e: Exception) {
                if (e.message!!.contains("timeout")) {
                    if (count == 3) {
                        count++
                        ajaxConvert(token, vid, convertID)
                    } else {
                        runOnUiThread {
                            ToastMy.errorToast(
                                this@VideoDownloader,
                                e.message + ". Retrying...",
                                ToastMy.LENGTH_LONG
                            )
                        }
                    }
                } else {
                    runOnUiThread {
                        ToastMy.errorToast(this@VideoDownloader, e.message, ToastMy.LENGTH_LONG)
                        progressHUD!!.dismiss()
                    }
                }
            }
        }.start()
    }

    @Throws(Exception::class)
    private fun inserting_data(obj: JSONObject): List<VideoModel> {
        val quality1 = "18"
        val quality2 = "22"
        val quality3 = "133"
        val quality4 = "135"
        val quality5 = "137"
        val quality6 = "160"
        val mp3quality = "mp3128"
        val list: MutableList<VideoModel> = ArrayList()
        if (obj.optJSONObject("links").optJSONObject("mp4").has(quality1)) {
            list.add(
                VideoModel(
                    obj.getString("title"),
                    obj.getString("vid"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality1)
                        .optString("size"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality1)
                        .optString("q"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality1)
                        .optString("f"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality1)
                        .optString("k")
                )
            )
        }
        if (obj.optJSONObject("links").optJSONObject("mp4").has(quality2)) {
            list.add(
                VideoModel(
                    obj.getString("title"),
                    obj.getString("vid"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality2)
                        .optString("size"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality2)
                        .optString("q"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality2)
                        .optString("f"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality2)
                        .optString("k")
                )
            )
        }
        if (obj.optJSONObject("links").optJSONObject("mp4").has(quality3)) {
            list.add(
                VideoModel(
                    obj.getString("title"),
                    obj.getString("vid"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality3)
                        .optString("size"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality3)
                        .optString("q"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality3)
                        .optString("f"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality3)
                        .optString("k")
                )
            )
        }
        if (obj.optJSONObject("links").optJSONObject("mp4").has(quality4)) {
            list.add(
                VideoModel(
                    obj.getString("title"),
                    obj.getString("vid"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality4)
                        .optString("size"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality4)
                        .optString("q"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality4)
                        .optString("f"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality4)
                        .optString("k")
                )
            )
        }
        if (obj.optJSONObject("links").optJSONObject("mp4").has(quality5)) {
            list.add(
                VideoModel(
                    obj.getString("title"),
                    obj.getString("vid"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality5)
                        .optString("size"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality5)
                        .optString("q"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality5)
                        .optString("f"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality5)
                        .optString("k")
                )
            )
        }
        if (obj.optJSONObject("links").optJSONObject("mp4").has(quality6)) {
            list.add(
                VideoModel(
                    obj.getString("title"),
                    obj.getString("vid"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality6)
                        .optString("size"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality6)
                        .optString("q"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality6)
                        .optString("f"),
                    obj.optJSONObject("links").optJSONObject("mp4").optJSONObject(quality6)
                        .optString("k")
                )
            )
        }
        if (obj.optJSONObject("links").optJSONObject("mp3").has(mp3quality)) {
            list.add(
                VideoModel(
                    obj.getString("title"),
                    obj.getString("vid"),
                    obj.optJSONObject("links").optJSONObject("mp3").optJSONObject(mp3quality)
                        .optString("size"),
                    obj.optJSONObject("links").optJSONObject("mp3").optJSONObject(mp3quality)
                        .optString("q"),
                    obj.optJSONObject("links").optJSONObject("mp3").optJSONObject(mp3quality)
                        .optString("f"),
                    obj.optJSONObject("links").optJSONObject("mp3").optJSONObject(mp3quality)
                        .optString("k")
                )
            )
        }
        return list
    }

    private fun init() {
        recyclerView = findViewById(R.id.vd_recyclerview)
        enterurl = findViewById(R.id.vdedittext)
        textView = findViewById(R.id.vdText)
        progressBar = findViewById(R.id.vdprogress)
        fetchbtn = findViewById(R.id.vdget)
        fetchtxt = findViewById(R.id.fetchtxt)
        toolbar = findViewById(R.id.vidtoolbar)
        paste = findViewById(R.id.paste2)
        //        clear=findViewById(R.id.cleanvid);
        server1 = findViewById(R.id.server1)
        server2 = findViewById(R.id.server2)
        server2Layout = findViewById(R.id.server2layout)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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

    fun getVideoIdFromYoutubeUrl(url: String?): String? {
        var videoId: String? = null
        val regex =
            "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)"
        val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(url)
        if (matcher.find()) {
            videoId = matcher.group(1)
        }
        return videoId
    }

    private fun loadad() {
        var adView = AdView(this)
        adView.adSize = AdSize.BANNER
        adView.adUnitId = Cvalues.banr
        adView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun checkVersion() {
        if (getyoutubeVideo_part1(this@VideoDownloader) == null) {
            check_specific(
                this@VideoDownloader,
                true,
                false,
                false
            ) { success: Boolean, updateinfo: String? ->
                if (success) {
                    method2()
                } else {
                    Toast.makeText(this, "Server Error: Please Try Again!", Toast.LENGTH_SHORT)
                        .show()
                    progressHUD!!.dismiss()
                }
            }
        } else {
            method2()
            //            fetchMp3();
        }
        running = false
    }

    companion object {
        const val TAG = "//"
    }
}