package com.jaesysz.ipl2021fansgame.views.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.*
import com.jaesysz.ipl2021fansgame.extensions.Extensions.msg
import com.jaesysz.ipl2021fansgame.models.PlayerAuthenticationDetailsModel
import com.jaesysz.ipl2021fansgame.models.PlayerProfileModel
import com.jaesysz.ipl2021fansgame.models.Schedule
import kotlinx.android.synthetic.main.activity_predict.*


class PredictActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private val digArray: ArrayList<String> =
        arrayListOf("NA", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

    private var t1="NA"
   private var t2="NA"
    private var winner="NA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict)

        //ads
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        val matchIndex = intent.extras?.getInt("INDEX", -2)!!
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        val baseNode = database.getReference(DNodes.BASE)

        edit1(false)
        edit2(false)
        editWinner(false)

        digitSpinner(spTeam1)
        digitSpinner(spTeam2)

        var uid ="NA"
        var username="NA"
        var profile:PlayerProfileModel= PlayerProfileModel()
        var schedule :Schedule= Schedule()

        baseNode.addValueEventListener(object : ValueEventListener {
            val schedules = mutableListOf<Schedule>()

            override fun onDataChange(snapshot: DataSnapshot) {
                val indStr = "${matchIndex + 1}"
                val scheduleNode = snapshot.child(DNodes.ADMIN).child(DNodes.SCHEDULE).child(indStr)
                 schedule = scheduleNode.getValue(Schedule::class.java)!!
                winnerSpinner(schedule.team1.name, schedule.team2.name)

                tvMatchIndex.text = "T20 ${matchIndex + 1} of 60"
                tvMatchNumber.text = schedule.matchNo
                tvMatchDate.text = "${schedule.day}, ${schedule.date}"
                tvTimeLeft.text = CountDownTillToday(schedule.date, schedule.indianTime).getString()

                imgTeam1.setImageResource(TeamImage.getLogo(schedule.team1))
                imgTeam2.setImageResource(TeamImage.getLogo(schedule.team2))

                imgTeam1Big.setImageResource(TeamImage.getLogo(schedule.team1))
                imgTeam2Big.setImageResource(TeamImage.getLogo(schedule.team2))


                tvTeam1.text = schedule.team1.name
                tvTeam2.text = schedule.team2.name

//                tvTeam1Digit.text = schedule.team1Digit
//                tvTeam2Digit.text = schedule.team2Digit
//                tvWinner.text = schedule.result

                //------
                 uid = auth.currentUser!!.uid.toString()
                 username =
                    snapshot.child(DNodes.PLAYERS).child(DNodes.AUTHENTICATION).child(uid)
                        .getValue(PlayerAuthenticationDetailsModel::class.java)!!.userName
                 profile = snapshot.child(DNodes.PLAYERS).child(DNodes.PROFILES).child(username)
                    .getValue(PlayerProfileModel::class.java)!!

                t1 = profile.matchResults[matchIndex].team1Digit
                t2 = profile.matchResults[matchIndex].team2Digit
                winner = profile.matchResults[matchIndex].winner

                tvTeam1DigitUser.text = t1
                tvTeam2DigitUser.text = t2
                TvWinnerUser.text = winner
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        tvTimeLeft.setOnClickListener {
            tvTimeLeft.text = CountDownTillToday(schedule.date, schedule.indianTime).getString()
        }

        imgEditTeam1.setOnClickListener{
            spTeam1.setSelection(digArray.indexOf(t1))
            edit1(true)
        }

        imgEditTeam2.setOnClickListener{
            spTeam2.setSelection(digArray.indexOf(t2))
            edit2(true)
        }

        imgEditWinner.setOnClickListener{
            spWinner.setSelection(digArray.indexOf(winner))
            editWinner(true)
        }

        imgCancel1.setOnClickListener { edit1(false) }
        imgCancel2.setOnClickListener { edit2(false) }
        imgCancelWinner.setOnClickListener { editWinner(false) }

        imgSync1.setOnClickListener {
            if(profile.uid!="000"){

                if (CountDownTillToday(
                        schedule.date,
                        schedule.indianTime
                    ).isCountEnded())
                    {
                        msg("Can't edit,  Time bar is closed")
                    }else {
                    baseNode.child(DNodes.PLAYERS).child(DNodes.PROFILES).child(username).child("matchResults")
                        .child(matchIndex.toString()).child("team1Digit")
                        .setValue(spTeam1.selectedItem.toString())

                        .addOnCompleteListener { task ->
                            if (task.isSuccessful)
                                msg("Submitted")

                            if (task.isCanceled)
                                msg("Cancelled")
                        }
                }
            }
            edit1(false)
        }

        imgSync2.setOnClickListener {
            if(profile.uid!="000"){

                if (CountDownTillToday(
                        schedule.date,
                        schedule.indianTime
                    ).isCountEnded())
                {
                    msg("Can't edit,  Time bar is closed")
                }else {
                    baseNode.child(DNodes.PLAYERS).child(DNodes.PROFILES).child(username).child("matchResults")
                        .child(matchIndex.toString()).child("team2Digit")
                        .setValue(spTeam2.selectedItem.toString())

                        .addOnCompleteListener { task ->
                            if (task.isSuccessful)
                                msg("Submitted")

                            if (task.isCanceled)
                                msg("Cancelled")
                        }
                }
            }
            edit2(false)
        }

        imgSyncWinner.setOnClickListener {
            if(profile.uid!="000"){

                if (CountDownTillToday(
                        schedule.date,
                        schedule.indianTime
                    ).isCountEnded())
                {
                    msg("Can't edit,  Time bar is closed")
                }else {
                    baseNode.child(DNodes.PLAYERS).child(DNodes.PROFILES).child(username).child("matchResults")
                        .child(matchIndex.toString()).child("winner")
                        .setValue(spWinner.selectedItem.toString())

                        .addOnCompleteListener { task ->
                            if (task.isSuccessful)
                                msg("Submitted")

                            if (task.isCanceled)
                                msg("Cancelled")
                        }
                }
            }
            editWinner(false)
        }

    }

    private fun edit1(state: Boolean) {
        if (!state) {
            tvTeam1DigitUser.visibility = View.VISIBLE
            spTeam1.visibility = View.GONE
            imgEditTeam1.visibility = View.VISIBLE
            imgSync1.visibility = View.GONE
            imgCancel1.visibility=View.GONE
        } else {
            tvTeam1DigitUser.visibility = View.GONE
            spTeam1.visibility = View.VISIBLE
            imgEditTeam1.visibility = View.GONE
            imgSync1.visibility = View.VISIBLE
            imgCancel1.visibility=View.VISIBLE
        }
    }

    private fun edit2(state: Boolean) {
        if (!state) {
            tvTeam2DigitUser.visibility = View.VISIBLE
            spTeam2.visibility = View.GONE
            imgEditTeam2.visibility = View.VISIBLE
            imgSync2.visibility = View.GONE
            imgCancel2.visibility=View.GONE
        } else {
            tvTeam2DigitUser.visibility = View.GONE
            spTeam2.visibility = View.VISIBLE
            imgEditTeam2.visibility = View.GONE
            imgSync2.visibility = View.VISIBLE
            imgCancel2.visibility=View.VISIBLE
        }
    }

    private fun editWinner(state: Boolean) {
        if (!state) {
            TvWinnerUser.visibility = View.VISIBLE
            spWinner.visibility = View.GONE
            imgEditWinner.visibility = View.VISIBLE
            imgSyncWinner.visibility = View.GONE
            imgCancelWinner.visibility=View.GONE
        } else {
            TvWinnerUser.visibility = View.GONE
            spWinner.visibility = View.VISIBLE
            imgEditWinner.visibility = View.GONE
            imgSyncWinner.visibility = View.VISIBLE
            imgCancelWinner.visibility=View.VISIBLE
        }
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
        val digArray: ArrayList<String> = arrayListOf("NA", t1, t2)

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
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }
}