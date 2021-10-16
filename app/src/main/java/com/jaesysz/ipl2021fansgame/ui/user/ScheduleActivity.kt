package com.jaesysz.ipl2021fansgame.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.adapter.PredictCardAdapter
import com.jaesysz.ipl2021fansgame.appData.DNodes
import com.jaesysz.ipl2021fansgame.models.Schedule
import kotlinx.android.synthetic.main.activity_schedule.*

class ScheduleActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private val listOfSchedule: MutableList<Schedule> = mutableListOf(Schedule())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        MobileAds.initialize(this)
        val adRequest= AdRequest.Builder().build()
        adView.loadAd(adRequest)

        database = FirebaseDatabase.getInstance()

        val adminNode = database.getReference(DNodes.BASE).child(DNodes.ADMIN)
        val scheduleNode = adminNode.child(DNodes.SCHEDULE)

        listOfSchedule.clear()

        scheduleNode.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (id in snapshot.children) {
                        val schedule = id.getValue(Schedule::class.java)
                        listOfSchedule.add(schedule!!)
                        updateUI()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                updateUI()
            }
        })

        btBack.setOnClickListener { onBackPressed() }
    }

    private fun updateUI(){
        if (listOfSchedule.count()<1)
            listOfSchedule.add(Schedule())

        val adapter=PredictCardAdapter(applicationContext,listOfSchedule)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(applicationContext)
    }
}