package com.jaesysz.ipl2021fansgame.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.DNodes
import com.jaesysz.ipl2021fansgame.appData.TeamImage
import com.jaesysz.ipl2021fansgame.extensions.Extensions.msg
import com.jaesysz.ipl2021fansgame.models.PlayerPointsModel
import com.jaesysz.ipl2021fansgame.models.PlayerProfileModel
import com.jaesysz.ipl2021fansgame.models.Schedule
import com.jaesysz.ipl2021fansgame.models.admin.IPL2021Results
import com.jaesysz.ipl2021fansgame.playerBox.Points
import kotlinx.android.synthetic.main.activity_more_details.*

class MoreDetailsActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_details)
        msg("Loading...")

        //Add
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        val username = intent.getStringExtra("username")!!
        val rank = intent.getStringExtra("rank")!!
        val netPoints = intent.getStringExtra("totalpoints")!!

        //Initialize
        database = FirebaseDatabase.getInstance()

        //Listener
        database.getReference(DNodes.BASE).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val profile =
                    snapshot.child(DNodes.PLAYERS).child(DNodes.PROFILES).child(username)
                        .getValue(PlayerProfileModel::class.java)!!

                val schedules = getScheduleList(snapshot.child(DNodes.ADMIN).child(DNodes.SCHEDULE))
                val iplResult = snapshot.child(DNodes.ADMIN).child(DNodes.IPL_2021_RESULTS)
                    .getValue(IPL2021Results::class.java)!!

               val data= Points.get(profile, schedules, iplResult)

                updateUI(username, rank, netPoints, profile,data)
            }

            override fun onCancelled(error: DatabaseError) { }
        })

        btBack.setOnClickListener { onBackPressed() }
    }

    private fun updateUI(
        username: String,
        rank: String,
        netPoints: String,
        profile: PlayerProfileModel,
        data: PlayerPointsModel
    ) {
        tvTitle.text = "Profile : $username "

        tvUserName.text = username
        tvTotalPoints.text = netPoints
        imgTeamLogo.setImageResource(TeamImage.getLogo(profile.championTeam))
        tvTeamName.text = profile.championTeam.name

        tvTotalWin.text = data.win.toString()
        tvTotalDraw.text = data.draw.toString()
        tvTotalLost.text = data.lost.toString()
        tvTotalWinPoints.text=data.winPoints.toString()

        tvSingleDigit.text=data.singleDigit.toString()
        tvDoubleDigit.text=data.doubleDigit.toString()
        tvTotalDigitPoints.text=data.digitPoints.toString()
    }

    private fun getScheduleList(dataSnapshot: DataSnapshot): MutableList<Schedule> {
        val list: MutableList<Schedule> = mutableListOf()

        for (scheduleSnap in dataSnapshot.children) {
            val schedule = scheduleSnap.getValue(Schedule::class.java)
            list.add(schedule!!)
        }
        return list
    }
}