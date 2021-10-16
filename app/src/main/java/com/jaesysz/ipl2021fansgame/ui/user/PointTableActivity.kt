package com.jaesysz.ipl2021fansgame.ui.user


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.adapter.PointTableAdapter
import com.jaesysz.ipl2021fansgame.appData.DNodes
import com.jaesysz.ipl2021fansgame.appData.TeamImage
import com.jaesysz.ipl2021fansgame.models.PlayerAuthenticationDetailsModel
import com.jaesysz.ipl2021fansgame.models.PlayerProfileModel
import com.jaesysz.ipl2021fansgame.models.PointTableModel
import com.jaesysz.ipl2021fansgame.models.Schedule
import com.jaesysz.ipl2021fansgame.models.admin.IPL2021Results
import com.jaesysz.ipl2021fansgame.playerBox.Points
import kotlinx.android.synthetic.main.activity_point_table.*

class PointTableActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_table)

        //Add
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        //Initialize
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        //Events
        btBack.setOnClickListener { onBackPressed() }

        //Listener
        database.getReference(DNodes.BASE).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val uid = auth.uid!!

                val username =
                    snapshot.child(DNodes.PLAYERS).child(DNodes.AUTHENTICATION).child(uid)
                        .getValue(PlayerAuthenticationDetailsModel::class.java)!!.userName

                val myProfile =
                    snapshot.child(DNodes.PLAYERS).child(DNodes.PROFILES).child(username)
                        .getValue(PlayerProfileModel::class.java)!!

                val allProfiles = getProfiles(snapshot.child(DNodes.PLAYERS).child(DNodes.PROFILES))
                val schedules = getScheduleList(snapshot.child(DNodes.ADMIN).child(DNodes.SCHEDULE))
                val iplResult = snapshot.child(DNodes.ADMIN).child(DNodes.IPL_2021_RESULTS)
                    .getValue(IPL2021Results::class.java)!!

             val allPoints=   updateRecycleView(allProfiles, schedules, iplResult)
                updateMyCard(allPoints,username)
            }

            override fun onCancelled(error: DatabaseError) { }
        })

        cardMyPoints.setOnClickListener {
            val intent = Intent(applicationContext, MyPointsActivity::class.java)
            startActivity(intent)
        }
    }



    private fun getProfiles(snapshot: DataSnapshot): MutableList<PlayerProfileModel> {
        val list: MutableList<PlayerProfileModel> = mutableListOf()

        for (profileSnap in snapshot.children) {
            val profile = profileSnap.getValue(PlayerProfileModel::class.java)
            list.add(profile!!)
        }
        return list
    }

    private fun getScheduleList(dataSnapshot: DataSnapshot): MutableList<Schedule> {
        val list: MutableList<Schedule> = mutableListOf()

        for (scheduleSnap in dataSnapshot.children) {
            val schedule = scheduleSnap.getValue(Schedule::class.java)
            list.add(schedule!!)
        }
        return list
    }

    private fun updateRecycleView(
        allProfiles: MutableList<PlayerProfileModel>,
        schedules: MutableList<Schedule>,
        iplResult: IPL2021Results
    ): MutableList<PointTableModel> {

        var pointTable: MutableList<PointTableModel> = mutableListOf()

        if (allProfiles.isEmpty())
            pointTable.add(PointTableModel())
        else {
            for (profile in allProfiles) {
                pointTable.add(PointTableModel(
                    0,
                    profile.userName,
                    profile.championTeam,
                    Points.get(profile, schedules, iplResult).totalPoints
                ))
            }
        }

        pointTable = pointTable.sortedBy { it.userName } as MutableList<PointTableModel>
        pointTable =
            pointTable.sortedByDescending { it.totalPoints } as MutableList<PointTableModel>

        val adapter = PointTableAdapter(pointTable)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        return pointTable
    }

    private fun updateMyCard(allPoints: MutableList<PointTableModel>, username: String) {
      for((id,item) in allPoints.withIndex()){
         if( item.userName==username){

             val image:Int= when(id){
                 0->R.drawable.ic_medal_first
                 1->R.drawable.ic_medal_second
                 2->R.drawable.ic_medal_third
                 else->R.drawable.ic_baseball_24

             }
             imgTag.setImageResource(image)

             tvRank.text="${id+1}"
             imgTeamLogo.setImageResource(TeamImage.getLogo(item.championTeam))
             tvUserName.text=item.userName
             tvTotalPoints.text=item.totalPoints.toString()
         }
      }
    }
}


