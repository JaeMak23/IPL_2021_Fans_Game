package com.jaesysz.ipl2021fansgame.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jaesysz.ipl2021fansgame.R
import kotlinx.android.synthetic.main.activity_admin_login.*

class AdminLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        btConnect.setOnClickListener {
            if(tbPassword.text.toString()=="659269")
            {
                val intent= Intent(applicationContext,AdminDashboardActivity::class.java)
                startActivity(intent)
            }
            else
                tbPassword.text.clear()
        }
    }
}