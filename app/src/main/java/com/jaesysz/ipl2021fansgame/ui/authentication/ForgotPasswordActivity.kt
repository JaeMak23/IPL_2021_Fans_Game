package com.jaesysz.ipl2021fansgame.ui.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.extensions.Extensions.msg
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val auth = FirebaseAuth.getInstance()

        btBack.setOnClickListener { onBackPressed() }

        btSendCode.setOnClickListener {
            auth.sendPasswordResetEmail(tbEmail.text.toString()).addOnCompleteListener { task ->
                msg(
                    if (task.isSuccessful)
                        "Password reset request sent email : ${tbEmail.text.toString()}"
                    else
                        "Failed to send reset password link to Email"
                )
            }
        }
    }
}