package com.jaesysz.ipl2021fansgame.ui.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.*
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.*
import com.jaesysz.ipl2021fansgame.extensions.Extensions.msg
import com.jaesysz.ipl2021fansgame.utils.FirebaseAuthExceptionUtility
import com.jaesysz.ipl2021fansgame.views.user.DashboardActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AppInfo.load()
        auth = FirebaseAuth.getInstance()

        tbEmail.doOnTextChanged { text, start, before, count ->
            tilEmail.error = if (tbEmail.text.isNullOrEmpty()) "Email is empty."
            else if (tbEmail.text!!.length < 4) "Email must have 4 or more characters."
            else if (!tbEmail.text!!.contains('@') || !tbEmail.text!!.contains('.')) "Invalid Email."
            else ""
        }

        tbPassword.doOnTextChanged { text, start, before, count ->
            tilPassword.error = if (tbPassword.text.isNullOrEmpty()) "Password is empty."
            else if (tbPassword.text!!.length < 4) "Password must have 4 or more characters."
            else ""
        }

        btForgotPassword.setOnClickListener {
            val intent = Intent(applicationContext, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        btHome.setOnClickListener { onBackPressed() }

        btRegister.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }

        btLogin.setOnClickListener {
            msg("Authenticating...")

            if (!validateAllFields()) {

                val email = tbEmail.text.toString().trim()
                val password = tbPassword.text.toString()

                //orginal code
                /*auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                          msg("Login successful")
                            AppInfo.logIn(auth.currentUser!!)
                            login()
                        } else {
                            msg("Authentication failed.")
                        }
                    }*/

                auth.signInWithEmailAndPassword(email, password).addOnFailureListener { ex ->
                    msg("Error Message : ${ex.message}")

                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        msg("Login successful.")
                        AppInfo.logIn(auth.currentUser!!)
                        login()
                    }
                    if (task.isCanceled) {
                        msg("Authentication canceled.")
                        msg(FirebaseAuthExceptionUtility.toString(task.exception as FirebaseAuthException))
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (auth.currentUser != null) {
            if (AppInfo.isDashboardActive) {
                AppInfo.isDashboardActive = false
                onBackPressed()
            } else {
                AppInfo.logIn(auth.currentUser!!)
                login()
            }
        }
    }

    private fun login() {

        val activity = if (auth.currentUser!!.isEmailVerified)
           // NewDashboardActivity::class.java
               DashboardActivity::class.java
        else
            EmailNotVerifiedActivity::class.java
        startActivity(Intent(applicationContext, activity))
    }

    private fun validateAllFields(): Boolean {
        var hasError = true
        val str = if (tbEmail.text.isNullOrEmpty()) "Email is empty."
        else if (tbEmail.text!!.length < 4) "Email must have 4 or more characters."
        else if (!tbEmail.text!!.contains('@') || !tbEmail.text!!.contains('.')) "Invalid Email."
        else if (tbPassword.text.isNullOrEmpty()) "Password is empty."
        else if (tbPassword.text!!.length < 4) "Password must have 4 or more characters."
        else {
            hasError = false
            "Connecting..."
        }

        msg(str)
        return hasError
    }
}