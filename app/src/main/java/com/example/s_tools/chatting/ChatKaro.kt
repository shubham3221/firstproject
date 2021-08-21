package com.example.s_tools.chatting

import android.app.Dialog
import kotlinx.coroutines.Dispatchers.IO
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.s_tools.chatting.messageModel.MessageModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.cardview.widget.CardView
import com.example.s_tools.TinyDB
import android.view.animation.Animation
import android.os.Bundle
import com.example.s_tools.R
import com.example.s_tools.tools.retrofitcalls.MySharedPref
import com.example.s_tools.tools.Cvalues
import com.example.s_tools.entertainment.VideoActivity
import com.example.s_tools.tools.ToastMy
import com.google.gson.JsonObject
import com.example.s_tools.chatting.messageModel.AllMessages
import android.graphics.drawable.ColorDrawable
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.s_tools.Splash_login_reg.SplashScreen
import com.example.s_tools.tools.executeAsyncTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.ArrayList

class ChatKaro : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var editText: EditText? = null
    var sendBtn: ImageButton? = null
    var list: MutableList<MessageModel>? = null
        @Synchronized get
        @Synchronized set
    lateinit var madapter: ChatkaroAdapter
    var toolbar: Toolbar? = null
    private val senderId = 0
    private var chatToken: String? = null
    var manager: LinearLayoutManager? = null
    private var sendCardView: CardView? = null
    var toolbarProgressbar: ProgressBar? = null
    var onlineDot: View? = null
    var positions = IntArray(1)
    var chatid = 0
    var errorPosition: MutableList<Int> = ArrayList()
    var tinyDB: TinyDB? = null
    var animation: Animation? = null
    var startUpdating = true

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.chatkaro_layout)
        init()
        animation = AnimationUtils.loadAnimation(this@ChatKaro, R.anim.dashboard_imp_news)
        asyncTask()
        sendBtn!!.setOnClickListener { v: View? ->
            val msg = editText!!.text.toString()
            editText!!.setText("")
            sendBtn!!.startAnimation(animation)
            Handler(Looper.getMainLooper()).postDelayed({
                if (!msg.isEmpty()) {
                    if (MySharedPref.getChatid(this@ChatKaro) == 0) {
                        showMsgRecycler(msg)
                        firstMessage(msg, positions[0])
                        return@postDelayed
                    }
                    conversationStart(msg)
                }
            }, 90)
        }
    }

    private fun asyncTask() {
        CoroutineScope(Main).executeAsyncTask(
            onPreExecute = {
                list = ArrayList()
        } , doInBackground = {
                tinyDB = TinyDB(this@ChatKaro)
                madapter = ChatkaroAdapter(this@ChatKaro, list!!)
                manager = LinearLayoutManager(this@ChatKaro);
                firstTimegetToken()
            } , onPostExecute = { result->
                recyclerView!!.setHasFixedSize(true)
                manager!!.stackFromEnd = true
                recyclerView!!.layoutManager = manager
                recyclerView!!.adapter = madapter
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                toolbar!!.title = Cvalues.CONNECTING
            })
    }


    private fun firstMessage(msg: String, position: Int) {
        ChatkaroApi.firstMessage(
            this@ChatKaro,
            position,
            MySharedPref.getTokenMessages(this@ChatKaro),
            msg
        ) { yes: Boolean, model: List<MessageModel>?, pos: Int ->
            if (yes) {
                if (model == null) {
                    toolbar!!.title = "Session Expired! Generating new Session"
                    ChatkaroApi.getToken(
                        this@ChatKaro,
                        MySharedPref.getUsername(this@ChatKaro),
                        MySharedPref.getPassword(this@ChatKaro)
                    ) { success: Boolean, token: String? ->
                        if (success) {
                            chatToken = token
                            MySharedPref.putTokenMessages(this@ChatKaro, token)
                            toolbar!!.title = "Generated..."

                            //now again sending previous message
                            ChatkaroApi.firstMessage(
                                this@ChatKaro,
                                position,
                                chatToken,
                                msg
                            ) { upSuccess: Boolean, upModel: List<MessageModel>?, upPos: Int ->
                                if (upSuccess && upModel != null) {
                                    ChatkaroApi.putchatid_Meta(
                                        this@ChatKaro,
                                        upModel[0].id
                                    ) { submit: Boolean ->
                                        if (submit) {
                                            MySharedPref.putChatid(this@ChatKaro, chatid)
                                            list!![0].messages[list!!.size - 1].date_sent =
                                                upModel[0].messages[list!!.size - 1].date_sent
                                            madapter!!.notifyDataSetChanged()
                                            recyclerView!!.scrollToPosition(list!!.size - 1)
                                            updating()
                                        } else {
                                            loginAgain()
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    ChatkaroApi.putchatid_Meta(this@ChatKaro, model[0].id) { success: Boolean ->
                        if (success) {
                            chatid = model[0].id
                            chatToken = MySharedPref.getTokenMessages(this@ChatKaro)
                            MySharedPref.putChatid(this@ChatKaro, model[0].id)
                            list!![0].messages[positions[0]].date_sent =
                                model[0].messages[positions[0]].date_sent
                            madapter!!.notifyDataSetChanged()
                            recyclerView!!.scrollToPosition(list!![0].messages.size - 1)
                        } else {
                            loginAgain()
                        }
                    }
                }
            } else {
//                list.get(0).getMessages().get(0)..setDate(ERROR);
                list!![0].messages[list!!.size - 1].date_sent = "error"
                madapter!!.notifyDataSetChanged()
                recyclerView!!.scrollToPosition(list!!.size - 1)
            }
        }
    }

    private fun firstTimegetToken() {
        when {
            MySharedPref.getTokenMessages(this@ChatKaro) == null -> {
                ChatkaroApi.getToken(
                    this@ChatKaro,
                    MySharedPref.getUsername(this@ChatKaro),
                    MySharedPref.getPassword(this@ChatKaro)
                ) { success: Boolean, token: String? ->
                    if (success) {
                        chatToken = token
                        MySharedPref.putTokenMessages(this@ChatKaro, token)
                        toolbar!!.title = Cvalues.CONNECTED
                        checkThreadMessage()
                    }
                }
            }
            MySharedPref.getChatid(this@ChatKaro) == 0 -> {
                checkThreadMessage()
            }
            else -> {
                tinyDB!!.putInt(Cvalues.CHATMESSAGECOUNT, 0) //to remove badge notifi.
                chatToken = MySharedPref.getTokenMessages(this@ChatKaro)
                chatid = MySharedPref.getChatid(this@ChatKaro)
                chatHistory
            }
        }
    }

    private fun checkThreadMessage() {
        ChatkaroApi().checkMetaforChatID(this@ChatKaro) { success: Boolean, mchatid: Int ->
            if (success) {
                if (mchatid == 0) {
                    showSendBtn()
                } else {
                    toolbar!!.title = "Checking History..."
                    chatid = mchatid
                    MySharedPref.putChatid(this@ChatKaro, mchatid)
                    chatToken = MySharedPref.getTokenMessages(this@ChatKaro)
                    chatHistory
                }
            } else {
                hideSendBtn()
                loginAgain()
            }
        }
    }

    private val chatHistory: Unit
        private get() {
            ChatkaroApi.getThread(
                this@ChatKaro,
                chatToken,
                chatid
            ) { success: Boolean, model: List<MessageModel>? ->
                if (success && model != null) {
                    showSendBtn()
                    if (!model.isEmpty()) {
                        tinyDB!!.putBoolean(VideoActivity.ISOPENMESSAGES, false)
                        tinyDB!!.putInt(Cvalues.CHATMESSAGECOUNT, model[0].messages.size)
                        list!!.addAll(model)
                        madapter!!.notifyDataSetChanged()
                    }
                } else if (success) {
                    ToastMy.successToast(
                        this@ChatKaro,
                        "old chat was deleted",
                        ToastMy.LENGTH_SHORT
                    )
                    chatid = 0
                    MySharedPref.putChatid(this@ChatKaro, 0)
                    showSendBtn()
                } else {
                    toolbarProgressbar!!.visibility = View.GONE
                    toolbar!!.title = Cvalues.ERROR
                }
            }
        }

    private fun showMsgRecycler(s: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("raw", s)
        val messagesList: MutableList<AllMessages> = ArrayList()
        messagesList.add(AllMessages("sending", jsonObject, 0))
        if (list!!.isEmpty()) {
            list!!.add(MessageModel(senderId, 0, senderId, "sending", 0, messagesList))
        } else {
            list!![0].messages.add(AllMessages("sending", jsonObject, 0))
        }
        madapter!!.notifyDataSetChanged()
        positions[0] = list!![0].messages.size - 1
        recyclerView!!.scrollToPosition(list!![0].messages.size - 1)
        updating()
    }

    private fun conversationStart(msg: String) {
//        isThreadPause=true;
        showMsgRecycler(msg)
        ChatkaroApi().sendMessage(
            this@ChatKaro,
            chatToken,
            msg,
            chatid,
            positions[0]
        ) { success: Boolean, model: List<MessageModel>, pos: Int ->
            if (success) {
                tinyDB!!.putInt(Cvalues.CHATMESSAGECOUNT, model[0].messages.size)
                list!!.clear()
                list!!.add(model[0])
                madapter!!.notifyDataSetChanged()
                recyclerView!!.scrollToPosition(list!![0].messages.size - 1)
            } else {
                errorPosition.add(pos)
                list!![0].messages[pos].date_sent = Cvalues.DIDN_T_SENT
                recyclerView!!.scrollToPosition(list!![0].messages.size - 1)
                madapter!!.notifyDataSetChanged()
            }

        }
    }

    private fun loginAgain() {
        val dialog = Dialog(this@ChatKaro)
        dialog.setContentView(R.layout.logout_dialog)
        val button = dialog.findViewById<ImageView>(R.id.loginbtnPopup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        button.setOnClickListener { v: View? ->
            startActivity(Intent(this@ChatKaro, SplashScreen::class.java))
            finishAffinity()
        }
    }

    private fun showSendBtn() {
        toolbarProgressbar!!.visibility = View.GONE
        sendBtn!!.visibility = View.VISIBLE
        sendCardView!!.visibility = View.VISIBLE
        onlineDot!!.visibility = View.VISIBLE
        toolbar!!.title = "ONLINE"
    }

    private fun hideSendBtn() {
        toolbarProgressbar!!.visibility = View.GONE
        sendBtn!!.visibility = View.GONE
        sendCardView!!.visibility = View.GONE
        onlineDot!!.visibility = View.GONE
        toolbar!!.title = Cvalues.OFFLINE
    }

    fun updating() {
        if (startUpdating){
            startUpdating = false
            lifecycleScope.launch(IO){
                while (this.isActive){
                    delay(4000)
                    ChatkaroApi.getThread(this@ChatKaro,chatToken,chatid) { success, model ->
                        if (success){
                            list!!.clear()
                            list!!.add(model[0])
                            madapter.notifyDataSetChanged()
                            recyclerView!!.scrollToPosition(list!![0].messages.size - 1)
                        }
                        Log.e("//", "updating: " )
                        updating()
                    }
                }
            }
        }
    }

    private fun dialog_Update(header: String, message: String, isFromAutoUpdate: Boolean) {
        val update_dialog = Dialog(this@ChatKaro)
        update_dialog.setContentView(R.layout.askforupdateonoff)
        val headermsg = update_dialog.findViewById<TextView>(R.id.titlePopup)
        val msg = update_dialog.findViewById<TextView>(R.id.errorText)
        headermsg.text = header
        msg.text = message
        val yes = update_dialog.findViewById<Button>(R.id.yesupdate)
        val no = update_dialog.findViewById<Button>(R.id.noupdate)
        update_dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        update_dialog.show()
        yes.setOnClickListener { v: View? ->
            if (isFromAutoUpdate) {
                tinyDB!!.putInt(VideoActivity.AUTOUPDATE, 1)
                update_dialog.dismiss()
                ToastMy.successToast(this@ChatKaro, Cvalues.SUCCESS, ToastMy.LENGTH_SHORT)
            } else {
                update_dialog.dismiss()
                ToastMy.successToast(this@ChatKaro, Cvalues.PLEASE_WAIT, ToastMy.LENGTH_SHORT)
                lifecycleScope.launch {
                    delay(1000)
                    hideSendBtn()
                    toolbarProgressbar!!.visibility = View.VISIBLE
                    toolbar!!.title = "Troubleshooting..."
                    ChatkaroApi().troubleShooting(
                        this@ChatKaro,
                        MySharedPref.getTokenMessages(this@ChatKaro),
                        MySharedPref.getChatid(this@ChatKaro)
                    ) { success: Boolean, token: String? ->
                        if (success) {
                            ToastMy.successToast(
                                this@ChatKaro,
                                Cvalues.SUCCESS,
                                ToastMy.LENGTH_SHORT
                            )
                            startActivity(Intent(this@ChatKaro, ChatKaro::class.java))
                            finish()
                        } else {
                            toolbarProgressbar!!.visibility = View.GONE
                            toolbar!!.title = Cvalues.ERROR
                        }
                    }
                }
            }
        }
        no.setOnClickListener { v: View? ->
            if (isFromAutoUpdate) {
                tinyDB!!.putInt(VideoActivity.AUTOUPDATE, 2)
                update_dialog.dismiss()
            }
            ToastMy.successToast(this@ChatKaro, "OK", ToastMy.LENGTH_SHORT)
        }
    }

    private fun init() {
        onlineDot = findViewById(R.id.onlineDot)
        toolbarProgressbar = findViewById(R.id.toolbar_progress_bar)
        sendCardView = findViewById(R.id.sendMsgCardView)
        editText = findViewById<View>(R.id.chat_editxt) as EditText
        sendBtn = findViewById<View>(R.id.chat_btn_send) as ImageButton
        recyclerView = findViewById<View>(R.id.chat_recycler) as RecyclerView
        toolbar = findViewById<View>(R.id.chat_toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.updtaemenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.updatemenu) {
            dialog_Update(
                "Turn On Notification?",
                "Do You Want Auto Update Messages When App Launch?",
                true
            )
        }
        if (item.itemId == R.id.troubleshoot) {
            dialog_Update(
                "TroubleShooting",
                "Are You Facing Any Error While Sending Messages?",
                false
            )
        }
        return super.onOptionsItemSelected(item)
    }
}