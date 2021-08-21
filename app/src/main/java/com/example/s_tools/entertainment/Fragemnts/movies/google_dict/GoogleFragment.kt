package com.example.s_tools.entertainment.Fragemnts.movies.google_dict

import android.app.Dialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.example.s_tools.entertainment.Fragemnts.movies.google_dict.upperRecyclerview.SliderGoogleAdapter
import android.widget.EditText
import android.text.TextWatcher
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.core.widget.NestedScrollView
import com.example.s_tools.TinyDB
import com.example.s_tools.tools.Imageanimation
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.s_tools.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.s_tools.MyWebService
import android.widget.Toast
import com.chootdev.recycleclick.RecycleClick
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.os.Looper
import com.kaopiz.kprogresshud.KProgressHUD
import com.example.s_tools.tools.ReadTextWeb
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.s_tools.testing.DownloadActivity
import com.example.s_tools.tools.GetDate
import com.example.s_tools.tools.Kbtomb
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class GoogleFragment : Fragment {
    var recyclerView: RecyclerView? = null
    var slider: RecyclerView? = null
    private var googleAdapter: GoogleAdapter? = null
    private var googleModelList: MutableList<Googlemodel>? = null
    private var mutableList: MutableList<Googlemodel>? = null
    var lastupdate: TextView? = null
    var info: TextView? = null
    var id: String? = null
    var searchBtn: ImageView? = null
    var cutBtn: ImageView? = null
    var reloadBtn: ImageView? = null
    var list_compact_btn: ImageView? = null
    var sortingBtn: ImageView? = null
    var goto_download_activity_btn: ImageView? = null
    var sliderGoogleAdapter: SliderGoogleAdapter? = null
    var mGroup: View? = null
    var editText: EditText? = null
    private var textWatcher: TextWatcher? = null
    private lateinit var ladyfingersListener: LoadingprogressListner
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var nestedScrollView: NestedScrollView? = null
    private var loading = false
    private var hasNextPage = false
    var tinyDB: TinyDB? = null
    var imageanimation: Imageanimation? = null

    var listSize:Int=0
        get() = googleModelList!!.size

    constructor() {
        // Required empty public constructor
    }

    constructor(
        upper_nav_recyclerview: RecyclerView?,
        sliderGoogleAdapter: SliderGoogleAdapter?,
        lastupdate: TextView?,
        info: TextView?,
        id: String?
    ) {
        this.lastupdate = lastupdate
        this.sliderGoogleAdapter = sliderGoogleAdapter
        slider = upper_nav_recyclerview
        this.id = id
        this.info = info
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        ladyfingersListener = requireActivity() as LoadingprogressListner
        imageanimation = Imageanimation()
        googleModelList = ArrayList()
        mutableList = ArrayList()
        tinyDB = TinyDB(activity)
        ladyfingersListener.visibleProgress()
        ladyfingersListener.showProgress()
        swipeRefreshLayout!!.isEnabled = false
        //        swipeRefreshLayout.setColorSchemeResources(R.color.Grey_700, R.color.info_color, R.color.yellow);
        recyclerSetup()
        recyclerClickListner()
        longCLickLsitner()
        reloadBtnListner()
        compactview_popup()
        sortingBtnClicked()
        gotoDownloadActivity()
        getPost(id)
        searchBtn!!.setOnClickListener { v: View? ->
            swipeRefreshLayout!!.setEnabled(false)
            if (editText!!.getVisibility() == View.VISIBLE) {
                swipeRefreshLayout!!.setEnabled(true)
                editText!!.setText("")
                editText!!.setVisibility(View.GONE)
                mGroup!!.setVisibility(View.VISIBLE)
                mGroup!!.startAnimation(
                    AnimationUtils.loadAnimation(
                        getActivity(),
                        R.anim.cover_img_animation
                    )
                )
                cutBtn!!.setVisibility(View.GONE)
                searchBtn!!.setImageResource(R.drawable.ic_baseline_search_24)
                hideKeyboard()
                editText!!.removeTextChangedListener(textWatcher)
            } else {
                editText!!.startAnimation(
                    AnimationUtils.loadAnimation(
                        getActivity(),
                        R.anim.cover_img_animation
                    )
                )
                editText!!.setVisibility(View.VISIBLE)
                mGroup!!.setVisibility(View.INVISIBLE)
                editText!!.requestFocus()
                searchBtn!!.setImageResource(R.drawable.ic_baseline_close_24)
                showKeyboard()
                edittext_listner()
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        retainInstance = true
        val view = inflater.inflate(R.layout.fragment_google, container, false)
        return view
    }

    private fun recyclerSetup() {
        if (tinyDB!!.getInt(LAYOUT) == DEFAULTT) {
            default_recycler_setup()
        } else if (tinyDB!!.getInt(LAYOUT) == GRID) {
            grid_recyclerSetup()
        } else if (tinyDB!!.getInt(LAYOUT) == COMPACT) {
            compact_recycler_setup()
        }
    }

    private fun default_recycler_setup() {
        googleAdapter = GoogleAdapter(activity, googleModelList, DEFAULTT)
        recyclerView!!.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView!!.adapter = googleAdapter
    }

    private fun getPost(id: String?) {
        val myWebService = MyWebService.google.create(MyWebService::class.java)
        myWebService.getFiles("'$id' in parents")!!.enqueue(object : Callback<Modelclass?> {
            override fun onResponse(call: Call<Modelclass?>, response: Response<Modelclass?>) {
                if (response.isSuccessful && response.body() != null) {
                    if (!googleModelList!!.isEmpty()) {
                        googleModelList!!.clear()
                    }
                    googleModelList!!.addAll(response.body()!!.files)
                    mutableList!!.addAll(response.body()!!.files)
                    sorting_setup()
                    setup_refreshListner()
                    if (response.body()!!.nextPageToken != null) {
                        lastUpdateSettext(googleModelList!!.size.toString() + "+ files")
                        hasNextPage = true
                        setup_nextPageListner(response.body()!!.nextPageToken)
                    } else {
                        lastUpdateSettext(googleModelList!!.size.toString() + " files")
                    }
                } else if (response.code() == 400) {
                    Toast.makeText(activity, LIMIT_REACHED, Toast.LENGTH_LONG).show()
                }
                ladyfingersListener!!.hideProgress(TIME)
            }

            override fun onFailure(call: Call<Modelclass?>, t: Throwable) {
                setup_refreshListner()
                Log.e(TAG, "onFailure: " + t.localizedMessage)
                ladyfingersListener!!.hideProgress(TIME)
                lastUpdateSettext(t.message)
                lastupdate!!.setOnClickListener(View.OnClickListener {
                    Toast.makeText(
                        activity,
                        "" + t.localizedMessage.toLowerCase().replace("google", "mbuddy"),
                        Toast.LENGTH_LONG
                    ).show()
                })
            }
        })
    }

    private fun sorting_setup() {
        if (tinyDB!!.getInt(SORTING) == 0) {
            googleAdapter!!.notifyDataSetChanged()
            //            recyclerView.scheduleLayoutAnimation();
        } else if (tinyDB!!.getInt(SORTING) == 1) {
            az_setup()
        } else if (tinyDB!!.getInt(SORTING) == 2) {
            za_setup()
        } else if (tinyDB!!.getInt(SORTING) == 3) {
            older_setup()
        } else if (tinyDB!!.getInt(SORTING) == 4) {
            newer_setup()
        }
        info_setup()
    }

    private fun recyclerClickListner() {
        lastupdate!!.visibility = View.GONE
        info!!.visibility = View.GONE
        RecycleClick.addTo(recyclerView)
            .setOnItemClickListener { recyclerView: RecyclerView?, i: Int, view: View ->
                if (googleModelList!!.get(i).getMimeType().contains("folder")) {
                    sliderGoogleAdapter!!.items.add(googleModelList!!.get(i).getName())
                    val adfragment: GoogleFragment = GoogleFragment(
                        slider,
                        sliderGoogleAdapter,
                        lastupdate,
                        info,
                        googleModelList!!.get(i).getId()
                    )
                    val fragmentTransaction: FragmentTransaction =
                        requireActivity().getSupportFragmentManager().beginTransaction()
                    adfragment.setRetainInstance(true)
                    fragmentTransaction.add(R.id.adcontainer, adfragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                    sliderGoogleAdapter!!.notifyDataSetChanged()
                    slider!!.scrollToPosition(sliderGoogleAdapter!!.items.size - 1)
                } else if (googleModelList!!.get(i).getMimeType().contains("video") or (googleModelList!!.get(i)
                        .getFileExtension() == "mkv")
                ) {
                    gotoVideoFragment(i)
                } else if (googleModelList!!.get(i).getMimeType().contains("photo") || googleModelList!!.get(i)
                        .getMimeType().contains("png")
                ) {
                    val images: MutableList<String> = ArrayList()
                    for (googlemodel: Googlemodel in googleModelList!!) {
                        images.add(getDownloadlink(googlemodel.getId()))
                    }
                    imageanimation!!.showImage(
                        getActivity(),
                        images,
                        i,
                        view.findViewById(R.id.imgvid)
                    )
                    //                new StfalconImageViewer.Builder<>(getActivity(), images, new ImageLoader<String>() {
//                    @Override
//                    public void loadImage(ImageView imageView, String imageUrl) {
//                        Glide.with(getActivity().getApplicationContext()).load(imageUrl).into(imageView);
//                    }
//                }).withTransitionFrom(view.findViewById(R.id.imgvid)).withStartPosition(i).show(true);
                } else showDownloadOptionMenu(i, googleModelList!!.get(i).getMimeType().contains("text"), true)
            }
    }

    private fun gotoVideoFragment(i: Int) {
        val adfragment = VideoFragment(
            googleModelList!![i].name, getDownloadlink(
                googleModelList!![i].id
            ), googleModelList!![i].thumbnailLink
        )
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.fragment_open_enter,
            R.anim.fragment_open_exit,
            R.anim.fragment_open_enter,
            R.anim.fragment_open_exit
        )
        fragmentTransaction.add(R.id.adcontainer, adfragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun showTextDialog(mtext: String) {
        val dialog = Dialog((activity)!!)
        dialog.setContentView(R.layout.text_show_layout)
        val text = dialog.findViewById<TextView>(R.id.text)
        text.text = mtext
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun setup_nextPageListner(token: String) {
        nestedScrollView!!.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                if (!loading && hasNextPage) {
                    loading = true
                    lastUpdateSettext("Loading...")
                    getNextPageData(token)
                }
            }
        })
    }

    private fun getNextPageData(token: String) {
        val map: MutableMap<String?, String?> = HashMap()
        map["pageToken"] = token
        MyWebService.google.create(MyWebService::class.java).getNextPage("'$id' in parents", map)!!
            .enqueue(object : Callback<Modelclass?> {
                override fun onResponse(call: Call<Modelclass?>, response: Response<Modelclass?>) {
                    api_setup(response, false)
                    loading = false
                }

                override fun onFailure(call: Call<Modelclass?>, t: Throwable) {
                    if (activity != null) {
                        lastUpdateSettext(t.message)
                        loading = false
                    }
                }
            })
    }

    private fun setup_refreshListner() {
        swipeRefreshLayout!!.isEnabled = true
        swipeRefreshLayout!!.setOnRefreshListener {
            lastUpdateSettext(REFRESHING)
            val myWebService: MyWebService = MyWebService.google.create(MyWebService::class.java)
            myWebService.getFiles("'" + id + "' in parents")!!
                .enqueue(object : Callback<Modelclass?> {
                    override fun onResponse(
                        call: Call<Modelclass?>,
                        response: Response<Modelclass?>
                    ) {
                        api_setup(response, true)
                        swipeRefreshLayout!!.setRefreshing(false)
                    }

                    override fun onFailure(call: Call<Modelclass?>, t: Throwable) {
                        onfailed(t)
                    }
                })
        }
    }

    private fun onfailed(t: Throwable) {
        if (activity != null) {
            lastUpdateSettext(t.message)
            swipeRefreshLayout!!.isRefreshing = false
        }
    }

    private fun api_setup(response: Response<Modelclass?>, from_refresh: Boolean) {
        if (response.isSuccessful && (activity != null) && (response.body() != null) && (response.body()!!
                .files != null)
        ) {
            if (from_refresh) {
                googleModelList!!.clear()
                googleModelList!!.addAll(response.body()!!.files)
                mutableList!!.clear()
            } else {
                googleModelList!!.addAll(response.body()!!.files)
            }
            mutableList!!.addAll(response.body()!!.files)
            if (response.body()!!.nextPageToken != null) {
                lastUpdateSettext(googleModelList!!.size.toString() + "+ files")
                hasNextPage = true
            } else {
                lastUpdateSettext(googleModelList!!.size.toString() + " files")
                hasNextPage = false
            }
            sorting_setup()
        } else if (response.code() == 400 && activity != null) {
            lastUpdateSettext(LIMIT_REACHED)
        } else if (activity != null && response.body()!!.files == null) {
            lastUpdateSettext("not found")
        }
    }

    private fun longCLickLsitner() {
        RecycleClick.addTo(recyclerView)
            .setOnItemLongClickListener({ recyclerView: RecyclerView?, i: Int, view: View? ->
                if (!googleModelList!!.get(i).getMimeType().contains("folder")) {
                    showDownloadOptionMenu(i, false, false)
                }
                false
            })
    }

    private fun edittext_listner() {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 0) {
                    googleAdapter!!.filter.filter(s.toString().trim { it <= ' ' })
                    cutBtn!!.visibility = View.VISIBLE
                } else {
                    googleAdapter!!.filter.filter(s)
                    cutBtn!!.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
        editText!!.addTextChangedListener(textWatcher)
        editText!!.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //searching whole
                lastUpdateSettext("searching...")
                searchWhole(editText!!.getText().toString())
                //                editText.removeTextChangedListener(textWatcher);
                return@setOnEditorActionListener true
            }
            false
        }
        cutBtn!!.setOnClickListener({ v: View? ->
            editText!!.setText("")
            cutBtn!!.setVisibility(View.GONE)
        })
    }

    private fun searchWhole(name: String) {
        swipeRefreshLayout!!.isRefreshing = true
        val myWebService = MyWebService.google.create(MyWebService::class.java)
        myWebService.getFiles("'$id' in parents and name contains '$name' and trashed = false")!!
            .enqueue(object : Callback<Modelclass?> {
                override fun onResponse(call: Call<Modelclass?>, response: Response<Modelclass?>) {
                    api_setup(response, true)
                    swipeRefreshLayout!!.isRefreshing = false
                }

                override fun onFailure(call: Call<Modelclass?>, t: Throwable) {
                    onfailed(t)
                }
            })
    }

    fun hideKeyboard() {
        val keyboard =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(editText!!.windowToken, 0)
    }

    fun showKeyboard() {
        val keyboard =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun lastUpdateSettext(msg: String?) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (getActivity() != null) {
                info_setup()
                lastupdate!!.setVisibility(View.VISIBLE)
                lastupdate!!.startAnimation(
                    AnimationUtils.loadAnimation(
                        getActivity(),
                        R.anim.fadein
                    )
                )
                lastupdate!!.setText(msg)
            }
        }, 130)
    }

    private fun info_setup() {
        if (tinyDB!!.getInt(SORTING) == 1) {
            info!!.visibility = View.VISIBLE
            info!!.text = "Order by: A-Z"
        } else if (tinyDB!!.getInt(SORTING) == 2) {
            info!!.visibility = View.VISIBLE
            info!!.text = "Order by: Z-A"
        } else if (tinyDB!!.getInt(SORTING) == 3) {
            info!!.visibility = View.VISIBLE
            info!!.text = "Order by: Older First"
        } else if (tinyDB!!.getInt(SORTING) == 4) {
            info!!.visibility = View.VISIBLE
            info!!.text = "Order by: New First"
        } else {
            info!!.visibility = View.GONE
        }
    }

    private fun init() {
        recyclerView = requireView().findViewById(R.id.namesrecyclerview)
        searchBtn = requireView().findViewById(R.id.searchicon)
        mGroup = requireView().findViewById(R.id.bottomgroup)
        cutBtn = requireView().findViewById(R.id.cutbtn)
        editText = requireView().findViewById(R.id.searchbox)
        swipeRefreshLayout = requireView().findViewById(R.id.swipe)
        nestedScrollView = requireView().findViewById(R.id.nested)
        reloadBtn = requireView().findViewById(R.id.item2)
        list_compact_btn = requireView().findViewById(R.id.item3)
        sortingBtn = requireView().findViewById(R.id.item4)
        goto_download_activity_btn = requireView().findViewById(R.id.item5)
    }

    fun reloadBtnListner() {
        reloadBtn!!.setOnClickListener { v: View? ->
            lastUpdateSettext(REFRESHING)
            googleModelList!!.clear()
            googleAdapter!!.notifyDataSetChanged()
            getPost(id)
        }
    }

    private fun showDownloadOptionMenu(position: Int, isText: Boolean, wanttodownload: Boolean) {
        val dialog = Dialog((activity)!!, R.style.jz_style_dialog_progress)
        dialog.setContentView(R.layout.google_popup_menu)
        val title = dialog.findViewById<TextView>(R.id.title)
        val open = dialog.findViewById<TextView>(R.id.open)
        val download = dialog.findViewById<TextView>(R.id.download)
        val properties = dialog.findViewById<TextView>(R.id.properties)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        title.text = googleModelList!!.get(position).name
        open.setOnClickListener { v14: View? ->
            if (googleModelList!!.get(position).getMimeType().contains("video")) {
                gotoVideoFragment(position)
            } else if (isText) {
                val show: KProgressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please Wait...")
                    .setCancellable(false).setDimAmount(0.5f).show()
                Thread(Runnable {
                    val text: List<String>? = ReadTextWeb().getTextFromWeb(
                        getDownloadlink(
                            googleModelList!!.get(position).getId()
                        )
                    )
                    if (text != null) {
                        requireActivity().runOnUiThread(Runnable {
                            show.dismiss()
                            showTextDialog(text.get(0))
                        })
                    } else {
                        requireActivity().runOnUiThread(Runnable {
                            show.dismiss()
                            Toast.makeText(
                                getActivity(),
                                "Can't Download File.",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    }
                }).start()
            } else if (wanttodownload) {
                val intent: Intent = Intent(getActivity(), DownloadActivity::class.java)
                intent.putExtra(
                    DownloadActivity.INTENTDOWNLINK,
                    getDownloadlink(googleModelList!!.get(position).getId())
                )
                intent.putExtra(
                    DownloadActivity.FILENAME,
                    googleModelList!!.get(position).getName()
                )
                intent.putExtra(
                    DownloadActivity.FILEEXTENSION,
                    googleModelList!!.get(position).getFileExtension()
                )
                startActivity(intent)
            }
        }
        download.setOnClickListener { v1: View? ->
            if (isText or wanttodownload) {
                val intent: Intent = Intent(getActivity(), DownloadActivity::class.java)
                intent.putExtra(
                    DownloadActivity.INTENTDOWNLINK,
                    getDownloadlink(googleModelList!!.get(position).getId())
                )
                intent.putExtra(
                    DownloadActivity.FILENAME,
                    googleModelList!!.get(position).getName()
                )
                intent.putExtra(
                    DownloadActivity.FILEEXTENSION,
                    googleModelList!!.get(position).getFileExtension()
                )
                startActivity(intent)
            } else {
                Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_LONG).show()
            }
        }
        properties.setOnClickListener { v12: View? ->
            dialog.dismiss()
            val dialog1: Dialog = Dialog((getActivity())!!)
            dialog1.setContentView(R.layout.google_popup_detail)
            val name: TextView = dialog1.findViewById(R.id.name)
            val mime: TextView = dialog1.findViewById(R.id.mime)
            val time: TextView = dialog1.findViewById(R.id.time)
            val filesize: TextView = dialog1.findViewById(R.id.filesize)
            val hasthumnail: TextView = dialog1.findViewById(R.id.hasthumnail)
            val extenstion: TextView = dialog1.findViewById(R.id.extenstion)
            name.setText("Name:  " + googleModelList!!.get(position).getName())
            mime.setText("Mime Type:  " + googleModelList!!.get(position).getMimeType())
            time.setText(
                "Modified Time:  " + GetDate.covertTimeToTextForWebsite(
                    googleModelList!!.get(position).getModifiedTime()
                )
            )
            filesize.setText(
                if (googleModelList!!.get(position)
                        .getSize() != null
                ) "Size:  " + Kbtomb.getFileSize(
                    googleModelList!!.get(position).getSize().toLong()
                ) else "Size:  " + "?"
            )
            hasthumnail.setText(
                if (googleModelList!!.get(position)
                        .getThumbnailLink() != null
                ) "Has Thumbnail:  Yes" else "Has Thumbnail:  No"
            )
            extenstion.setText(
                "File Extension:  " + googleModelList!!.get(position).getFileExtension()
            )
            dialog1.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog1.show()
        }
    }

    fun compactview_popup() {
        list_compact_btn!!.setOnClickListener { v: View? ->
            val dialog: Dialog = Dialog((getActivity())!!, R.style.jz_style_dialog_progress)
            dialog.setContentView(R.layout.popup_menu_list)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val grid: TextView = dialog.findViewById(R.id.one)
            val compact: TextView = dialog.findViewById(R.id.two)
            val normal: TextView = dialog.findViewById(R.id.three)
            val layout: Int = tinyDB!!.getInt(LAYOUT)
            if (layout == DEFAULTT) { //default
                normal.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_view_list_24,
                    0,
                    R.drawable.ic_baseline_check_circle_24,
                    0
                )
            } else if (layout == GRID) { //grid
                grid.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_view_grid_24,
                    0,
                    R.drawable.ic_baseline_check_circle_24,
                    0
                )
            } else if (layout == COMPACT) { //compact
                compact.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_format_list_compact_24,
                    0,
                    R.drawable.ic_baseline_check_circle_24,
                    0
                )
            }
            grid.setOnClickListener(View.OnClickListener { v1: View? ->
                tinyDB!!.putInt(LAYOUT, GRID)
                dialog.dismiss()
                grid_recyclerSetup()
            })
            compact.setOnClickListener(View.OnClickListener { v12: View? ->
                tinyDB!!.putInt(LAYOUT, COMPACT)
                dialog.dismiss()
                compact_recycler_setup()
            })
            normal.setOnClickListener(View.OnClickListener { v13: View? ->
                tinyDB!!.putInt(LAYOUT, DEFAULTT)
                dialog.dismiss()
                default_recycler_setup()
            })
        }
    }

    private fun compact_recycler_setup() {
        googleAdapter = GoogleAdapter(activity, googleModelList, COMPACT)
        recyclerView!!.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView!!.adapter = googleAdapter
    }

    private fun grid_recyclerSetup() {
        googleAdapter = GoogleAdapter(activity, googleModelList, GRID)
        recyclerView!!.layoutManager = GridLayoutManager(activity, 3, RecyclerView.VERTICAL, false)
        recyclerView!!.adapter = googleAdapter
    }

    private fun sortingBtnClicked() {
        sortingBtn!!.setOnClickListener { v: View? ->
            val tinyDB: TinyDB = TinyDB(getActivity())
            val dialog: Dialog = Dialog((getActivity())!!, R.style.jz_style_dialog_progress)
            dialog.setContentView(R.layout.sorting_layout)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            val defaultt: TextView = dialog.findViewById(R.id.defaultt)
            val az: TextView = dialog.findViewById(R.id.one)
            val za: TextView = dialog.findViewById(R.id.two)
            val older: TextView = dialog.findViewById(R.id.three)
            val newer: TextView = dialog.findViewById(R.id.four)
            val sorting: Int = tinyDB.getInt(SORTING)
            if (sorting == 0) {
                defaultt.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_check_circle_24,
                    0
                )
            } else if (sorting == 1) {
                az.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_check_circle_24,
                    0
                )
            } else if (sorting == 2) {
                za.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_check_circle_24,
                    0
                )
            } else if (sorting == 3) {
                older.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_check_circle_24,
                    0
                )
            } else if (sorting == 4) {
                newer.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_check_circle_24,
                    0
                )
            }
            defaultt.setOnClickListener(View.OnClickListener { v12: View? ->
                tinyDB.putInt(SORTING, 0)
                googleModelList!!.clear()
                googleModelList!!.addAll((mutableList)!!)
                googleAdapter!!.notifyDataSetChanged()
                dialog.dismiss()
                info_setup()
            })
            az.setOnClickListener(View.OnClickListener { v1: View? ->
                tinyDB.putInt(SORTING, 1)
                az_setup()
                dialog.dismiss()
                info_setup()
            })
            za.setOnClickListener(View.OnClickListener { v1: View? ->
                tinyDB.putInt(SORTING, 2)
                za_setup()
                dialog.dismiss()
                info_setup()
            })
            older.setOnClickListener(View.OnClickListener { v1: View? ->
                tinyDB.putInt(SORTING, 3)
                older_setup()
                dialog.dismiss()
                info_setup()
            })
            newer.setOnClickListener(View.OnClickListener { v1: View? ->
                tinyDB.putInt(SORTING, 4)
                newer_setup()
                dialog.dismiss()
                info_setup()
            })
        }
    }

    private fun newer_setup() {
        Thread {
            val googlemodelList: List<Googlemodel> = ArrayList(googleModelList)
            val d: ArrayList<Long> = ArrayList()
            for (datelong: Googlemodel in googleModelList!!) {
                d.add(GetDate.getTimeLong(datelong.getModifiedTime()))
            }
            googleModelList!!.clear()
            for (i in d.indices) {
                for (j in i + 1 until d.size) {
                    if (d.get(i) <= d.get(j)) {
                        val a: Long = d.get(i)
                        d.set(i, d.get(j))
                        d.set(j, a)
                    }
                }
                for (l in d.indices) {
                    if ((GetDate.getTimeLong(
                            googlemodelList.get(l).getModifiedTime()
                        ) == d.get(i))
                    ) {
                        googleModelList!!.add(googlemodelList.get(l))
                    }
                }
            }
            if (getActivity() != null) {
                requireActivity().runOnUiThread(Runnable { googleAdapter!!.notifyDataSetChanged() })
            }
        }.start()
    }

    private fun older_setup() {
        Thread {
            val googlemodelList: List<Googlemodel> = ArrayList(googleModelList)
            val d: ArrayList<Long> = ArrayList()
            for (datelong: Googlemodel in googleModelList!!) {
                d.add(GetDate.getTimeLong(datelong.getModifiedTime()))
            }
            googleModelList!!.clear()
            for (i in d.indices) {
                for (j in i + 1 until d.size) {
                    if (d.get(i) >= d.get(j)) {
                        val a: Long = d.get(i)
                        d.set(i, d.get(j))
                        d.set(j, a)
                    }
                }
                for (l in d.indices) {
                    if ((GetDate.getTimeLong(
                            googlemodelList.get(l).getModifiedTime()
                        ) == d.get(i))
                    ) {
                        googleModelList!!.add(googlemodelList.get(l))
                    }
                }
            }
            if (getActivity() != null) {
                requireActivity().runOnUiThread(Runnable { googleAdapter!!.notifyDataSetChanged() })
            }
        }.start()
    }

    private fun za_setup() {
        Collections.sort(
            googleModelList,
            { o1: Googlemodel, o2: Googlemodel ->
                o2.getName().compareTo(o1.getName(), ignoreCase = true)
            })
        googleAdapter!!.notifyDataSetChanged()
    }

    private fun az_setup() {
        Collections.sort(
            googleModelList,
            { o1: Googlemodel, o2: Googlemodel ->
                o1.getName().compareTo(o2.getName(), ignoreCase = true)
            })
        googleAdapter!!.notifyDataSetChanged()
    }

    fun getDownloadlink(id: String): String {
        return "https://www.googleapis.com/drive/v3/files/$id?alt=media&key=AIzaSyAqZV8HLqax1m5MOPK4Ix4mjvJh6k14DXI"
    }

    fun gotoDownloadActivity() {
        goto_download_activity_btn!!.setOnClickListener({ v: View? ->
            val intent: Intent = Intent(getActivity(), DownloadActivity::class.java)
            startActivity(intent)
        })
    }

    companion object {
        val TAG = "//"
        val LIMIT_REACHED = "Server Limit Reached!"
        val REFRESHING = "refreshing..."
        val DEFAULTT = 0
        @JvmField
        val GRID = 1
        @JvmField
        val COMPACT = 2
        val LAYOUT = "layout"
        val SORTING = "sorting"
        val TIME = 250
    }
}