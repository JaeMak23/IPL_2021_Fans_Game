package com.jaesysz.ipl2021fansgame.views.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.AppInfo
import com.jaesysz.ipl2021fansgame.appData.DNodes
import com.jaesysz.ipl2021fansgame.appData.TeamImage
import com.jaesysz.ipl2021fansgame.models.PlayerAuthenticationDetailsModel
import com.jaesysz.ipl2021fansgame.models.PlayerProfileModel
import com.jaesysz.ipl2021fansgame.ui.extra.ComingSoonActivity
import com.jaesysz.ipl2021fansgame.ui.user.*
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        AppInfo.isDashboardActive = true

        //ini
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        //ads
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        val uid = auth.currentUser!!.uid.toString()
        val baseNode = database.getReference(DNodes.BASE)

        baseNode.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val username =
                    snapshot.child(DNodes.PLAYERS).child(DNodes.AUTHENTICATION).child(uid)
                    .getValue(PlayerAuthenticationDetailsModel::class.java)!!.userName

                val profile = snapshot.child(DNodes.PLAYERS).child(DNodes.PROFILES).child(username)
                    .getValue(PlayerProfileModel::class.java)!!

                imgTeamLogo.setImageResource(TeamImage.getLogo(profile.championTeam))
                layoutTeamBackground.setBackgroundResource(TeamImage.getBackground(profile.championTeam))
                tvTeamName.text=profile.championTeam.name
                tvUserName.text=profile.userName
                tvFullName.text=profile.fullName
                tvEmailId.text=profile.email
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        cardDetails.setOnClickListener { run(MyPointsActivity::class.java)}
        cardHome.setOnClickListener { onBackPressed() }
        cardMyProfile.setOnClickListener { run(MyPointsActivity::class.java)  }
        cardPredictMatch.setOnClickListener { run(PredictDashboardActivity::class.java) }
        cardPointTable.setOnClickListener { run(PointTableActivity::class.java) }
        cardDatabase.setOnClickListener { run(DatabaseActivity::class.java) }
        cardSchedule.setOnClickListener { run(ScheduleActivity::class.java) }
        cardRules.setOnClickListener { run(HowToPlayActivity::class.java) }
        cardSettings.setOnClickListener { run(ComingSoonActivity::class.java) }

        cardLogout.setOnClickListener {
            auth.signOut()
            AppInfo.logOut()
            onBackPressed()
        }

    }

    private fun run(cls: Class<*>?) {
        val intent = Intent(applicationContext, cls)
        startActivity(intent)
    }
}