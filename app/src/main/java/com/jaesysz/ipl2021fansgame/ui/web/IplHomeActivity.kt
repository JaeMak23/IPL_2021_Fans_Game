package com.jaesysz.ipl2021fansgame.ui.web

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.IntentKeys
import com.jaesysz.ipl2021fansgame.views.web.WebPageActivity
import kotlinx.android.synthetic.main.activity_ipl_home.*

class IplHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipl_home)

        MobileAds.initialize(this)
        val adRequest= AdRequest.Builder().build()
        adView.loadAd(adRequest)

        btMatchesByGoogle.setOnClickListener {
            connect("https://www.google.com/search?q=ipl+schedule&rlz=1C1CHBF_enIN910IN910&sxsrf=ALeKk024HHMQUiuISnfgDl0WUgxPo4OoQQ%3A1616741246513&ei=foNdYNPtHv2_3LUPotmJ6AM&oq=ipl+schedule&gs_lcp=Cgdnd3Mtd2l6EAMyCQgjECcQRhD9ATIFCAAQsQMyCggAELEDEIMBEEMyBAgAEEMyBAgAEEMyBAgAEEMyCAgAELEDEIMBMgQIABBDMggIABCxAxCDATICCAA6BwgAEEcQsAM6BwgAELADEEM6DQguELADEMgDEEMQkwI6CgguELADEMgDEEM6BAgjECc6CwgAELEDEIMBEMkDOgUIABCSAzoHCAAQsQMQQzoFCC4QsQM6BggAEAoQQ0oFCDgSATFQ3jVY81Rgs1doAXACeACAAZUDiAHlGZIBCTAuOS42LjAuMZgBAKABAaoBB2d3cy13aXrIAQ_AAQE&sclient=gws-wiz&ved=0ahUKEwjTpMS9rs3vAhX9H7cAHaJsAj0Q4dUDCA0&uact=5#sie=lg;/g/11fqtnjjg0;5;/m/03b_lm1;mt;fp;1;;")
        }

        btTableByGoogle.setOnClickListener {
            connect("https://www.google.com/search?q=ipl+schedule&rlz=1C1CHBF_enIN910IN910&sxsrf=ALeKk024HHMQUiuISnfgDl0WUgxPo4OoQQ%3A1616741246513&ei=foNdYNPtHv2_3LUPotmJ6AM&oq=ipl+schedule&gs_lcp=Cgdnd3Mtd2l6EAMyCQgjECcQRhD9ATIFCAAQsQMyCggAELEDEIMBEEMyBAgAEEMyBAgAEEMyBAgAEEMyCAgAELEDEIMBMgQIABBDMggIABCxAxCDATICCAA6BwgAEEcQsAM6BwgAELADEEM6DQguELADEMgDEEMQkwI6CgguELADEMgDEEM6BAgjECc6CwgAELEDEIMBEMkDOgUIABCSAzoHCAAQsQMQQzoFCC4QsQM6BggAEAoQQ0oFCDgSATFQ3jVY81Rgs1doAXACeACAAZUDiAHlGZIBCTAuOS42LjAuMZgBAKABAaoBB2d3cy13aXrIAQ_AAQE&sclient=gws-wiz&ved=0ahUKEwjTpMS9rs3vAhX9H7cAHaJsAj0Q4dUDCA0&uact=5#sie=lg;/g/11fqtnjjg0;5;/m/03b_lm1;mt;fp;1;;")
        }

        btHomeIPL.setOnClickListener { connect("https://www.iplt20.com/") }
        btScheduleIPL.setOnClickListener { connect("https://www.iplt20.com/matches/schedule/men") }
        btResultIPL.setOnClickListener { connect("https://www.iplt20.com/matches/schedule/men") }
        btPointTableIPL.setOnClickListener { connect("https://www.iplt20.com/points-table/men/2021") }
        btStatsBySeasonIPL.setOnClickListener { connect("https://www.iplt20.com/stats/2020/most-runs") }
        btStatsByALlTimeIPL.setOnClickListener { connect("https://www.iplt20.com/stats/2020/most-runs") }
        btAuctionIPL.setOnClickListener { connect("https://www.iplt20.com/auction/2021") }
        btTeamsIPL.setOnClickListener { connect("https://www.iplt20.com/teams/men") }
        btNewsIPL.setOnClickListener { connect("https://www.iplt20.com/news") }
        btFantasyIPL.setOnClickListener { connect("https://fantasy.iplt20.com/") }
        btPhotosIPL.setOnClickListener { connect("https://www.iplt20.com/photos") }
        btVideoIPL.setOnClickListener { connect("https://www.iplt20.com/video") }
    }

   private fun connect (url:String)
   {
       val intent  = Intent(applicationContext,WebPageActivity::class.java)
       intent.putExtra(IntentKeys.URL_LINK,url)
       startActivity(intent)
   }
}