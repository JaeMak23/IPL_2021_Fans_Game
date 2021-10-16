package com.jaesysz.ipl2021fansgame.ui.extra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.jaesysz.ipl2021fansgame.R
import kotlinx.android.synthetic.main.activity_coming_soon.*

class ComingSoonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coming_soon)

        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}