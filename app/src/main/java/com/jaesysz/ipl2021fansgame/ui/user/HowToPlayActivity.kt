package com.jaesysz.ipl2021fansgame.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.jaesysz.ipl2021fansgame.R
import kotlinx.android.synthetic.main.activity_how_to_play.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class HowToPlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_play)

        //Add
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

//text Edit
            var string: String? = ""
            val stringBuilder = StringBuilder()
            val `is`: InputStream = this.resources.openRawResource(R.raw.rules)
            val reader = BufferedReader(InputStreamReader(`is`))
            while (true) {
                try {
                    if (reader.readLine().also { string = it } == null) break
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                stringBuilder.append(string).append("\n")
                textContent.text = stringBuilder
            }
            `is`.close()
    }
}