package com.jaesysz.ipl2021fansgame.ui.admin

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.DNodes
import com.jaesysz.ipl2021fansgame.appData.TeamImage
import com.jaesysz.ipl2021fansgame.extensions.Extensions.msg
import com.jaesysz.ipl2021fansgame.models.Schedule
import com.jaesysz.ipl2021fansgame.pass.PredictMatchPass
import kotlinx.android.synthetic.main.activity_admin_dashboard.*

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var scheduleNode: DatabaseReference
    val scheduleList: MutableList<Schedule> = mutableListOf()
    var selectedMatch: Int = -1

    private val digArray: ArrayList<String> =
        arrayListOf("NA", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        database = FirebaseDatabase.getInstance()

        val scheduleNode =
            database.getReference(DNodes.BASE).child(DNodes.ADMIN).child(DNodes.SCHEDULE)

        scheduleNode.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                scheduleList.clear()
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        scheduleList.add(item.getValue(Schedule::class.java)!!)

                        if (scheduleList.count() > 0)
                            selectedMatch = 0
                        createSpinner()
                        if (selectedMatch != -1)
                            updateUI(selectedMatch)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                scheduleList.clear()
                scheduleList.add(Schedule())
                msg("Cancelled. ")
            }
        })

        btSubmit.setOnClickListener {

            if (selectedMatch > -1) {
                msg("selected match : $selectedMatch")
               val match=(selectedMatch+1).toString()


                scheduleNode.child(match).child("team1Digit")
                    .setValue(spTeam1Digit.selectedItem.toString())

                scheduleNode.child(match).child("team2Digit")
                    .setValue(spTeam2Digit.selectedItem.toString())

                scheduleNode.child(match).child("result")
                    .setValue(spWinner.selectedItem.toString())
            }
        }

    }

    private fun createSpinner() {
        val list: MutableList<String> = mutableListOf()
        for (schedule in scheduleList) {
            list.add(schedule.matchNo)
        }

        val adapter =
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_spinner_dropdown_item,
                list
            )
        spSelectMatch.adapter = adapter

        if (PredictMatchPass.matchPredictAt > 0) {
            spSelectMatch.setSelection(PredictMatchPass.matchPredictAt)
        }

        spSelectMatch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                selectedMatch = pos
                updateUI(pos)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateUI(pos: Int) {
        val item = scheduleList[pos]

        imgTeam1.setImageResource(TeamImage.getLogo(item.team1))
        imgTeam2.setImageResource(TeamImage.getLogo(item.team2))

        tvStadium.text = item.stadium
        tvPlace.text = item.place

        tvTeam1.text = item.team1.name
        tvTeam2.text = item.team2.name

        tvBetween.text = item.between

        tvTeam1Digit.text = item.team1Digit
        tvTeam2Digit.text = item.team2Digit
        tvWinner.text = item.result

        digitSpinner(spTeam1Digit)
        digitSpinner(spTeam2Digit)
        winnerSpinner(item.team1.name, item.team2.name)
    }

    private fun digitSpinner(sp: Spinner) {
        // Add Teams to spinner : spTeam
        if (sp != null) {
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, digArray)
            sp.adapter = adapter

            sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun winnerSpinner(t1: String, t2: String) {
        val digArray: ArrayList<String> = arrayListOf("NA", t1, t2,"DRAW")

        if (spWinner != null) {
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, digArray)
            spWinner.adapter = adapter

            spWinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) { }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }
}