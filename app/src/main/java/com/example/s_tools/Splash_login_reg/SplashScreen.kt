package com.example.s_tools.Splash_login_reg

import com.example.s_tools.Splash_login_reg.loginapi_call.LoginApiCall.login
import com.example.s_tools.Splash_login_reg.RegisterApiCall.RegisterNewAccount.registerNew
import com.example.s_tools.tools.retrofitcalls.VC.check
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.os.Bundle
import com.example.s_tools.R
import android.view.WindowManager
import com.example.s_tools.tools.Validation
import com.example.s_tools.Splash_login_reg.loginapi_call.LoginApiCall
import com.example.s_tools.Splash_login_reg.RegisterApiCall.RegisterNewAccount
import com.example.s_tools.tools.ToastMy
import android.content.Intent
import com.example.s_tools.entertainment.VideoActivity
import com.kaopiz.kprogresshud.KProgressHUD
import com.example.s_tools.tools.retrofitcalls.VC
import android.app.Activity
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorCompat

class SplashScreen() : AppCompatActivity() {
    var name: EditText? = null
    var username: EditText? = null
    var email: EditText? = null
    var pass: EditText? = null
    var scrollView: ScrollView? = null
    var logo: ImageView? = null
    var register: Button? = null
    var back_btn: Button? = null
    var login: CardView? = null
    var registerok: CardView? = null
    var registerHint: TextView? = null
    var nootwelcomemsg: TextView? = null
    var statusemail: TextView? = null
    var statuspass: TextView? = null
    var logintxt: TextView? = null
    var registertxt: TextView? = null
    var hintLayout: LinearLayout? = null
    var loginprogress: ProgressBar? = null
    var registerprogress: ProgressBar? = null
    var taphere: TextView? = null
    var line: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.splashscreen_bg)
        init()
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        animate()
        Handler().postDelayed({ setupListner() }, 1000)
    }

    private fun setupListner() {
        login!!.setOnClickListener { v: View? ->
            statuspass!!.setText("")
            if (pass!!.getText().toString().isEmpty() || pass!!.getText().toString()
                    .contains(" ")
            ) {
                statuspass!!.setText("Not Valid Password")
                return@setOnClickListener
            } else if (email!!.getText().toString().isEmpty() || !Validation.isValidEmailId(
                    email!!.getText().toString(),
                    this@SplashScreen
                )
            ) {
                return@setOnClickListener
            }
            logintxt!!.setText(WAIT)
            loginprogress!!.setVisibility(View.VISIBLE)
            hideKeyboard()
            login(
                this@SplashScreen,
                email!!.getText().toString(),
                pass!!.getText().toString()
            ) { success: Boolean ->
                loginprogress!!.setVisibility(View.GONE)
                if (!success) {
                    logintxt!!.setText(LOGIN)
                } else {
                    putAppData()
                }
            }
        }


        //successfull register
        registerok!!.setOnClickListener { v: View? ->
            val validPassword: Boolean = Validation.isValidPassword(
                pass!!.getText().toString(), this@SplashScreen
            )
            val validEmailId: Boolean = Validation.isValidEmailId(
                email!!.getText().toString().trim { it <= ' ' },
                this@SplashScreen
            )
            if (validEmailId && validPassword) {
                if (name!!.getText().toString().isEmpty() || (name!!.getText()
                        .toString().length < 3) || name!!.getText().toString().contains(" ")
                ) {
                    Toast.makeText(this@SplashScreen, INVALID_NAME, Toast.LENGTH_SHORT).show()
                    registertxt!!.setText(REGISTER)
                    registerprogress!!.setVisibility(View.GONE)
                    return@setOnClickListener
                }
                if (username!!.getText().toString().isEmpty() || (username!!.getText()
                        .toString().length < 4) || username!!.getText().toString().contains(" ")
                ) {
                    Toast.makeText(this@SplashScreen, INVALID_USERNAME, Toast.LENGTH_SHORT).show()
                    registertxt!!.setText(REGISTER)
                    registerprogress!!.setVisibility(View.GONE)
                    return@setOnClickListener
                }
                hideKeyboard()
                registertxt!!.setText(WAIT)
                registerprogress!!.setVisibility(View.VISIBLE)

                //register api call
                registerNew(
                    this@SplashScreen,
                    name!!.getText().toString(),
                    username!!.getText().toString(),
                    email!!.getText().toString(),
                    pass!!.getText().toString(),
                    null,
                    object : RegisterNewAccount.ApiCallback {
                        override fun onResponse(success: Boolean) {
                            if (!success) {
                                registertxt!!.setText(REGISTER)
                                registerprogress!!.setVisibility(View.GONE)
                            } else {
                                login(
                                    this@SplashScreen,
                                    email!!.getText().toString(),
                                    pass!!.getText().toString()
                                ) { success1: Boolean ->
                                    if (success) {
                                        ToastMy.successToast(
                                            this@SplashScreen,
                                            "Registered Successful",
                                            ToastMy.LENGTH_SHORT
                                        )
                                        startActivity(
                                            Intent(
                                                this@SplashScreen,
                                                VideoActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                }
                            }
                        }
                    })
            }
        }

        //register
        register!!.setOnClickListener(View.OnClickListener { //                animate();
            line!!.visibility = View.VISIBLE
            registerHint!!.visibility = View.VISIBLE
            logo!!.visibility = View.GONE
            nootwelcomemsg!!.visibility = View.GONE
            name!!.visibility = View.VISIBLE
            hintLayout!!.visibility = View.GONE
            registerok!!.visibility = View.VISIBLE
            back_btn!!.visibility = View.VISIBLE
            register!!.visibility = View.GONE
            login!!.visibility = View.GONE
            username!!.visibility = View.VISIBLE
            logo!!.layoutParams.height = 230
            logo!!.layoutParams.width = 230
        })
        //back btn
        back_btn!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                line!!.visibility = View.GONE
                registerHint!!.visibility = View.GONE
                logo!!.visibility = View.VISIBLE
                nootwelcomemsg!!.visibility = View.VISIBLE
                name!!.visibility = View.GONE
                hintLayout!!.visibility = View.VISIBLE
                registerok!!.visibility = View.GONE
                back_btn!!.visibility = View.GONE
                register!!.visibility = View.VISIBLE
                login!!.visibility = View.VISIBLE
                username!!.visibility = View.GONE
                logo!!.layoutParams.height = 260
                logo!!.layoutParams.width = 260
            }
        })
        taphere!!.setOnClickListener({ view: View? ->
            email!!.setText(
                email!!.getText().append("@gmail.com")
            )
        })
    }

    private fun putAppData() {
        val kProgressHUD =
            KProgressHUD.create(this@SplashScreen).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please Wait...").setDimAmount(0.5f)
                .setCancellable(false).show()
        check(this@SplashScreen) { success: Boolean, updateinfo: String? ->
            if (success) {
                kProgressHUD.dismiss()
                startActivity(Intent(this@SplashScreen, VideoActivity::class.java))
                finish()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    private fun init() {
        line = findViewById(R.id.lineview)
        taphere = findViewById(R.id.taphere)
        registerHint = findViewById(R.id.registerhint)
        name = findViewById<View>(R.id.name_edittext) as EditText
        username = findViewById<View>(R.id.username_editext) as EditText
        email = findViewById<View>(R.id.email) as EditText
        pass = findViewById<View>(R.id.password) as EditText
        statuspass = findViewById<View>(R.id.status_password) as TextView
        nootwelcomemsg = findViewById<View>(R.id.noot_welcome_you) as TextView
        hintLayout = findViewById<View>(R.id.hint_linearLayout) as LinearLayout
        scrollView = findViewById<View>(R.id.mscrollView) as ScrollView
        logo = findViewById<View>(R.id.img_logo) as ImageView
        register = findViewById<View>(R.id.register_btn) as Button
        registerok = findViewById<View>(R.id.register_ok) as CardView
        back_btn = findViewById<View>(R.id.back_btn_registrationform) as Button
        login = findViewById<View>(R.id.login_btn) as CardView
        loginprogress = findViewById(R.id.login_ok_progressbar)
        registerprogress = findViewById(R.id.register_ok_progressbar)
        logintxt = findViewById(R.id.login_txt)
        registertxt = findViewById(R.id.register_ok_txt)
    }

    private fun animate() {
        val logoImageView = findViewById<View>(R.id.img_logo) as ImageView
        val container = findViewById<View>(R.id.container) as ViewGroup
        ViewCompat.animate(logoImageView)
            .translationY(-150f)
            .setStartDelay(STARTUP_DELAY.toLong())
            .setDuration(1000).setInterpolator(
                DecelerateInterpolator(2.0f)
            ).start()
        for (i in 0 until container.childCount) {
            val v = container.getChildAt(i)
            var viewAnimator: ViewPropertyAnimatorCompat
            if (v !is Button) {
                viewAnimator = ViewCompat.animate(v)
                    .translationY(0f).alpha(1f)
                    .setStartDelay(((ITEM_DELAY) + 200).toLong())
                    .setDuration(500)
            } else {
                viewAnimator = ViewCompat.animate(v)
                    .scaleY(1f).scaleX(1f)
                    .setStartDelay(((ITEM_DELAY) + 400).toLong())
                    .setDuration(500)
            }
            viewAnimator.setInterpolator(DecelerateInterpolator()).start()
        }
    }

    companion object {
        val STARTUP_DELAY = 300
        val ITEM_DELAY = 300
        val TAG = "mtag"
        val INVALID_NAME = "Invalid Name"
        val INVALID_USERNAME = "Invalid username"
        val WAIT = "Please wait.."
        val REGISTER = "REGISTER"
        val LOGIN = "LOGIN"
    }
}