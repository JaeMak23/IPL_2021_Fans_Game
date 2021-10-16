package com.jaesysz.ipl2021fansgame.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.AppInfo
import com.jaesysz.ipl2021fansgame.appData.DNodes
import com.jaesysz.ipl2021fansgame.extensions.Extensions.msg
import com.jaesysz.ipl2021fansgame.ui.admin.AdminLoginActivity
import com.jaesysz.ipl2021fansgame.ui.authentication.LoginActivity
import com.jaesysz.ipl2021fansgame.ui.extra.UpdateAppActivity
import com.jaesysz.ipl2021fansgame.ui.web.IplHomeActivity
import com.jaesysz.ipl2021fansgame.views.admin.AdminEntryActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    var isOlderVersion=false
    var blockApp=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //ads
        MobileAds.initialize(this)
        val adRequest= AdRequest.Builder().build()
        adView.loadAd(adRequest)

        //ini
        database=FirebaseDatabase
            .getInstance("https://ipl-2021-fans-game-default-rtdb.firebaseio.com/")
        tvDeviceVersion.text="Version 2.${AppInfo.DEVICE_VERSION}"

        val baseNode = database.getReference(DNodes.BASE)

        baseNode.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val curVer = snapshot.child(DNodes.ADMIN).child("Version").value.toString().toInt()

                btUpdateApp.visibility=
                    if(AppInfo.DEVICE_VERSION<curVer) View.VISIBLE else View.GONE
            }

            override fun onCancelled(error: DatabaseError) {   msg("Server did not respond.")}
        })

       imgShareAppIcon.setOnClickListener {

           baseNode.addValueEventListener(object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                   val url = snapshot.child(DNodes.ADMIN).child("downloadLink").value.toString()
                   val content="IPL 2021 Fans Game \n\n Download link : \n$url "

                   val shareIntent = Intent()
                   shareIntent.action = Intent.ACTION_SEND
                   shareIntent.putExtra(Intent.EXTRA_TEXT, content)
                   shareIntent.type = "text/plain"
                   startActivity(Intent.createChooser(shareIntent,"send to"))
               }

               override fun onCancelled(error: DatabaseError) {   msg("Server did not respond.")}
           })
       }

        btGoToIPLHome.setOnClickListener { run( IplHomeActivity::class.java)}
        btPlayGame.setOnClickListener {run( LoginActivity::class.java)  }
        tvDeviceVersion.setOnClickListener {run( AdminEntryActivity::class.java)}
        btUpdateApp.setOnClickListener {run( UpdateAppActivity::class.java) }
    }

    private fun run(cls: Class<*>?) {
        val intent = Intent(applicationContext, cls)
        startActivity(intent)
    }
}