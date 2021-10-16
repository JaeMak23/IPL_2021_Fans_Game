package com.jaesysz.ipl2021fansgame.ui.user

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
import com.jaesysz.ipl2021fansgame.appData.DNodes
import com.jaesysz.ipl2021fansgame.appData.TeamImage
import com.jaesysz.ipl2021fansgame.enums.Digits
import com.jaesysz.ipl2021fansgame.enums.Teams
import com.jaesysz.ipl2021fansgame.extensions.Extensions.msg
import com.jaesysz.ipl2021fansgame.models.PlayerProfileModel
import com.jaesysz.ipl2021fansgame.models.Schedule
import com.jaesysz.ipl2021fansgame.models.UpdatePointReport
import com.jaesysz.ipl2021fansgame.models.admin.IPL2021Results
import kotlinx.android.synthetic.main.activity_my_points.*


class MyPointsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_points)
        msg("Loading...")

        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val uid = auth.currentUser!!.uid

        database.getReference(DNodes.BASE).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userName =
                    snapshot.child(DNodes.PLAYERS).child(DNodes.AUTHENTICATION).child(uid)
                        .child("userName").value.toString()

                var profileNode =
                    snapshot.child(DNodes.PLAYERS).child(DNodes.PROFILES).child(userName)
                var champNode = snapshot.child(DNodes.ADMIN).child(DNodes.IPL_2021_RESULTS)
                var scheduleNodes = snapshot.child(DNodes.ADMIN).child(DNodes.SCHEDULE)

                var profile = profileNode.getValue(PlayerProfileModel::class.java)
                var champ = champNode.getValue(IPL2021Results::class.java)

                val schedules: MutableList<Schedule> = mutableListOf()
                for (item in scheduleNodes.children) {
                    schedules.add(item.getValue(Schedule::class.java)!!)
                }

                //Update points : returns win, draw, lost
                val points = updatePoints(profile!!, schedules)

                //update to database
                updateToDatabase(points, userName, profile)

                //Update UI
                userName =
                    snapshot.child(DNodes.PLAYERS).child(DNodes.AUTHENTICATION).child(uid)
                        .child("userName").value.toString()

                profileNode =
                    snapshot.child(DNodes.PLAYERS).child(DNodes.PROFILES).child(userName)
                champNode = snapshot.child(DNodes.ADMIN).child(DNodes.IPL_2021_RESULTS)
                scheduleNodes = snapshot.child(DNodes.ADMIN).child(DNodes.SCHEDULE)
                profile = profileNode.getValue(PlayerProfileModel::class.java)
                champ = champNode.getValue(IPL2021Results::class.java)

                schedules.clear()
                for (item in scheduleNodes.children) {
                    schedules.add(item.getValue(Schedule::class.java)!!)
                }

                updateUI(profile!!, champ!!, points.played)
            }

            override fun onCancelled(error: DatabaseError) {
                msg("Error : Permission Denied.")
            }
        })

        btBack.setOnClickListener { onBackPressed() }
    }

    private fun updateToDatabase(
        points: UpdatePointReport,
        userName: String,
        profile: PlayerProfileModel
    ) {

        val newProfile = PlayerProfileModel(
            profile.uid,
            profile.email,
            profile.userName,
            profile.fullName,
            profile.championTeam,
            points.played,
            points.win,
            points.lost,
            points.draw,
            points.singleDigit,
            points.doubleDigit,
            profile.matchResults
        )
        val queue =
            database.getReference(DNodes.BASE).child(DNodes.PLAYERS).child((DNodes.PROFILES))
                .child(userName).setValue(newProfile)
    }


    private fun updatePoints(profile: PlayerProfileModel, schedules: MutableList<Schedule>):
            UpdatePointReport {
        var played = 0
        var win = 0
        var draw = 0
        var lost = 0
        var digit1 = 0
        var digit2 = 0

        for ((id, userRes) in profile.matchResults.withIndex()) {

            var t1 = false
            var t2 = false

            if (schedules[id].result != "NA") {
                if (userRes.winner != "NA") {
                    if (schedules[id].result == schedules[id].team1.name || schedules[id].result == schedules[id].team2.name)
                        if (userRes.winner == schedules[id].result)
                            win += 1
                        else
                            lost += 1
                    else
                        draw += 1

                    played += 1
                }
            }

            if (schedules[id].team1Digit != "NA") {
                if (userRes.team1Digit != "NA")
                    if (userRes.team1Digit == schedules[id].team1Digit)
                        t1 = true
            }

            if (schedules[id].team2Digit != "NA") {
                if (userRes.team2Digit != Digits.NA.name)
                    if (userRes.team2Digit == schedules[id].team2Digit)
                        t2 = true
            }

            if (t1 && t2)
                digit2 += 1
            else if (t1 || t2)
                digit1 += 1
        }
        return UpdatePointReport(played, win, draw, lost, digit1, digit2)
    }

    private fun updateUI(profile: PlayerProfileModel, champ: IPL2021Results, played: Int) {

        val win = profile.totalWin
        val draw = profile.totalDraw
        val lost = profile.totalLost

        val singleDigit = profile.totalSingleDigit
        val doubleDigit = profile.totalDoubleDigit

        val winPoints = (win * 100) + (draw * 50)
        val digitPoints = (singleDigit * 40) + (doubleDigit * 120)

        val champPoints: Int =
            if (champ.championTeam != Teams.NA) {
                if (profile.championTeam == champ.championTeam) 500
                else if (profile.championTeam == champ.runnerUp) 300
                else 0
            } else 0

        val netPoints = champPoints + winPoints + digitPoints

        imgTeamLogo.setImageResource(TeamImage.getLogo(profile.championTeam))
        tvTeamName.text = profile.championTeam.name

        tvUserName.text = profile.userName
        tvFullName.text = profile.fullName
        tvEmailId.text = profile.email

        tvTotalMatches.text = played.toString()
        tvTotalWin.text = win.toString()
        tvTotalDraw.text = draw.toString()
        tvTotalLost.text = lost.toString()
        tvTotalWinPoints.text = winPoints.toString()

        tvSingleDigit.text = singleDigit.toString()
        tvDoubleDigit.text = doubleDigit.toString()
        tvTotalDigitPoints.text = digitPoints.toString()

        tvTotalPoints.text = netPoints.toString()
    }


}