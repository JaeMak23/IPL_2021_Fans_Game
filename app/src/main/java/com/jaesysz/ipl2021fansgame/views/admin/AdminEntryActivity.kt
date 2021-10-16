package com.jaesysz.ipl2021fansgame.views.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.extensions.Extensions.msg
import kotlinx.android.synthetic.main.activity_admin_entry.*

class AdminEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_entry)

        //ads
        MobileAds.initialize(this)
        val adRequest= AdRequest.Builder().build()
        val adRequest1= AdRequest.Builder().build()
        adView1.loadAd(adRequest1)
        adView2.loadAd(adRequest)
        adView3.loadAd(adRequest)
        adView4.loadAd(adRequest)
        val adRequest2= AdRequest.Builder().build()
        adView5.loadAd(adRequest2)
        adView6.loadAd(adRequest2)
        adView7.loadAd(adRequest2)
        adView8.loadAd(adRequest2)

        button.setOnClickListener {
            if(password.text.toString()=="659269") {
                val intent = Intent(applicationContext, AdminDashActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                password.text.clear()
                msg("Failed to connect.")
            }
        }
    }
}