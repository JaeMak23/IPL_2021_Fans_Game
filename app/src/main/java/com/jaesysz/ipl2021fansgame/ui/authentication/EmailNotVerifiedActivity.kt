package com.jaesysz.ipl2021fansgame.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.AppInfo
import kotlinx.android.synthetic.main.activity_email_not_verified.*

class EmailNotVerifiedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_not_verified)

        val auth = FirebaseAuth.getInstance()

        val email = auth.currentUser!!.email
        val sender = "noreply@ipl-2021-fans-game.firebaseapp.com"

        textView1.text = "Your e-mail id < '$email' > is not verified."
        textView2.text =
            "We have sent you a verification link to your e-mail address < '$email' > from < '$sender' >."
        textView3.text = "Please verify to continue"
        textView4.text = "This e-mail is sent automatically by Firebaseapp(Google)."
        textView5.text =
            "If you have already verified your e-mail using verification link then 'login' using the application."
        textView6.text =
            "If there is any problem in logging in then 'logout' from the application and try again to 'login'."

        //btBack.setOnClickListener { onBackPressed() }

        btLogOut.setOnClickListener {
            auth.signOut()
            AppInfo.loggedOut = true
            onBackPressed()
        }
        btResend.setOnClickListener { auth.currentUser!!.sendEmailVerification() }
    }
}