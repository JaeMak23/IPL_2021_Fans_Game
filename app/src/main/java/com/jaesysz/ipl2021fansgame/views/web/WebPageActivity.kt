package com.jaesysz.ipl2021fansgame.views.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.IntentKeys
import kotlinx.android.synthetic.main.activity_web_page.*

class WebPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_page)

        //ads
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        val link = intent.extras?.getString(IntentKeys.URL_LINK,"https://www.iplt20.com/")

//        Original
//        webView.webViewClient = android.webkit.WebViewClient()
//        New
        webView.webViewClient=WebViewClient()
        if (link != null) {
            webView.loadUrl(link)
        }
        webView.settings.javaScriptEnabled = true

    }

    override fun onBackPressed() {
        if ( webView!!.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressBar.visibility = View.GONE
        }
    }
}