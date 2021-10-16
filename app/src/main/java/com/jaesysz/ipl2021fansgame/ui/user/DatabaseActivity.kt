package com.jaesysz.ipl2021fansgame.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.adapter.MatchesDigitPointAdapter
import com.jaesysz.ipl2021fansgame.adapter.PlayerDataAdapter
import com.jaesysz.ipl2021fansgame.appData.DNodes
import com.jaesysz.ipl2021fansgame.appData.TeamImage
import com.jaesysz.ipl2021fansgame.extensions.Extensions.msg
import com.jaesysz.ipl2021fansgame.models.DatabaseAllUsersSingleMatchDigitsModel
import com.jaesysz.ipl2021fansgame.models.PlayerProfileModel
import com.jaesysz.ipl2021fansgame.models.Schedule
import com.jaesysz.ipl2021fansgame.models.admin.IPL2021Results
import com.jaesysz.ipl2021fansgame.playerBox.Points
import kotlinx.android.synthetic.main.activity_database.*

class DatabaseActivity : AppCompatActivity() {

    private val listSortBy: MutableList<String> = mutableListOf()
    private lateinit var database: FirebaseDatabase
    private val players: MutableList<PlayerProfileModel> = mutableListOf()
    private val schedules: MutableList<Schedule> = mutableListOf()
    private val users: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        //ads
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        msg("Refreshing...")

        listSortBy.add("Players")
        listSortBy.add("Matches")

        createSpinnerBy()
        updateStatus()

        btBack.setOnClickListener { onBackPressed() }

        database =
            FirebaseDatabase.getInstance("https://ipl-2021-fans-game-default-rtdb.firebaseio.com/")

        val baseNode = database.getReference(DNodes.BASE)

        baseNode.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                updateStatus()
                players.clear()

                for (plrNode in snapshot.child(DNodes.PLAYERS).child(DNodes.PROFILES).children) {
                    players.add(plrNode.getValue(PlayerProfileModel::class.java)!!)
                }

                users.clear()
                for (plr in players) {
                    users.add(plr.userName)
                }

                schedules.clear()
                for (schNode in snapshot.child(DNodes.ADMIN).child(DNodes.SCHEDULE).children) {
                    schedules.add(schNode.getValue(Schedule::class.java)!!)
                }

                val iplResult = snapshot.child(DNodes.ADMIN).child(DNodes.IPL_2021_RESULTS)
                    .getValue(IPL2021Results::class.java)!!

                createSpPlayer(iplResult)
                createSpMatches(players, schedules)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        btBack.setOnClickListener { onBackPressed() }
    }

    private fun createSpinnerBy() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listSortBy)
        spBy.adapter = adapter

        spBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                updateStatus()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    fun updateStatus() {
        if (spBy.selectedItemPosition == 0) {
            layoutPlayers.visibility = View.VISIBLE
            layoutMatches.visibility = View.GONE
            spPlayer.visibility=View.VISIBLE
            spMatches.visibility=View.GONE
        } else {
            layoutPlayers.visibility = View.GONE
            layoutMatches.visibility = View.VISIBLE
            spPlayer.visibility=View.GONE
            spMatches.visibility=View.VISIBLE
        }
    }

    fun createSpPlayer(iplResult: IPL2021Results) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, users)
        spPlayer.adapter = adapter

        spPlayer.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                val totalPoints = Points.get(players[pos], schedules, iplResult).totalPoints
                val adapterPlayer =
                    PlayerDataAdapter(players[pos].matchResults, schedules, totalPoints)
                rvPlayers.adapter = adapterPlayer
                rvPlayers.layoutManager = LinearLayoutManager(applicationContext)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    fun createSpMatches(
        players: MutableList<PlayerProfileModel>,
        scheduleList: MutableList<Schedule>
    ) {
        val matches: MutableList<String> = mutableListOf()

        for (id in 1..60)
            matches.add(id.toString())

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, matches)
        spMatches.adapter = adapter

        spMatches.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {

                val sch = scheduleList[pos]
                team1.setImageResource(TeamImage.getLogo(sch.team1))
                team2.setImageResource(TeamImage.getLogo(sch.team2))

                tvDigits.text ="[ ${sch.team1.name}:${sch.team1Digit}, ${sch.team2.name}:${sch.team2Digit} ]"

                val record: MutableList<DatabaseAllUsersSingleMatchDigitsModel> = mutableListOf()
                for (plr in players) {
                    record.add(
                        DatabaseAllUsersSingleMatchDigitsModel(
                            plr.userName,
                            plr.matchResults[pos].team1Digit,
                            plr.matchResults[pos].team2Digit
                        )
                    )
                }

                val adapterMatches = MatchesDigitPointAdapter(record)
                rvMatch.adapter = adapterMatches
                rvMatch.layoutManager = LinearLayoutManager(applicationContext)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


    }


}