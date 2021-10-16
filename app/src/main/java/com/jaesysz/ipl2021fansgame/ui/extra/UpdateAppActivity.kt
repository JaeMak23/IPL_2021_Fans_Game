package com.jaesysz.ipl2021fansgame.ui.extra

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.AppInfo
import com.jaesysz.ipl2021fansgame.appData.DNodes
import kotlinx.android.synthetic.main.activity_update_app.*

class UpdateAppActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_app)

        MobileAds.initialize(this)
        val adRequest= AdRequest.Builder().build()
        adView.loadAd(adRequest)

        database =
            FirebaseDatabase.getInstance("https://ipl-2021-fans-game-default-rtdb.firebaseio.com/")

        tvDeviceVersion.text="2.0.${AppInfo.DEVICE_VERSION}"

        val adminNode=database.getReference(DNodes.BASE).child(DNodes.ADMIN)

        adminNode.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              val onlineVersion= snapshot.child(DNodes.VERSION).value.toString().toInt()
                val downloadLink=snapshot.child("downloadLink").value.toString()

                tvOnlineVersion.text="2.0.$onlineVersion"
                tvDownloadLink.text=downloadLink
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        btCopy.setOnClickListener {
           val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", tvDownloadLink.text.toString())
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_LONG).show()

           /* val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("RANDOM UUID",tvDownloadLink.text.toString())
            clipboard.primaryClip = clip*/
        }

        btOpen.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(tvDownloadLink.text.toString())
            startActivity(openURL)
        }
    }
}