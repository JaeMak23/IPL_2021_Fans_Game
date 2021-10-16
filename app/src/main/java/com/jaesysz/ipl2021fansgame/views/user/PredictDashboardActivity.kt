package com.jaesysz.ipl2021fansgame.views.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.adapter.PredictAllMatchesAdapter
import com.jaesysz.ipl2021fansgame.appData.*
import com.jaesysz.ipl2021fansgame.extensions.Extensions.msg
import com.jaesysz.ipl2021fansgame.models.Schedule
import kotlinx.android.synthetic.main.activity_predict_dashboard.*


class PredictDashboardActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict_dashboard)

        //ini
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        //ads
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        val uid = auth.currentUser!!.uid.toString()
        val baseNode = database.getReference(DNodes.BASE)

        msg("Loading..")

        cardToday1.visibility= View.GONE
        cardToday2.visibility=View.GONE
        var ind=-2
        var ind1=-2

        baseNode.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val scheduleNode = snapshot.child(DNodes.ADMIN).child(DNodes.SCHEDULE)
                val schedules = mutableListOf<Schedule>()
                for (scheduleNodeValue in scheduleNode.children) {
                    val schedule=scheduleNodeValue.getValue(Schedule::class.java)!!
                    schedules.add(schedule)
                }

                val todaysMatch = GetTodaysMatch.get(schedules)
               // msg("${todaysMatch.count()}, First match : ${todaysMatch[0].date}")

                if(todaysMatch.size>0){
                    cardToday1.visibility=View.VISIBLE
                    ind=MatchIndices.get(todaysMatch[0].matchNo)
                    tvMatchIndex.text=  "T20 ${ind+1} of 60"
                    tvMatchNumber.text=todaysMatch[0].matchNo
                    tvMatchDate.text="${todaysMatch[0].day}, ${todaysMatch[0].date}"

                    imgTeam1.setImageResource(TeamImage.getLogo(todaysMatch[0].team1))
                    imgTeam2.setImageResource(TeamImage.getLogo(todaysMatch[0].team2))
                    tvTeam1.text=todaysMatch[0].team1.name
                    tvTeam2.text=todaysMatch[0].team2.name
                    tvStadium.text=todaysMatch[0].stadium
                    tvVenue.text=todaysMatch[0].place

                    if(todaysMatch.size>1){
                        cardToday2.visibility=View.VISIBLE
                        ind1=MatchIndices.get(todaysMatch[1].matchNo)
                        tvMatchIndex2.text=  "T20 ${ind1+1} of 60"
                        tvMatchNumber2.text=todaysMatch[1].matchNo
                        tvMatchDate2.text="${todaysMatch[1].day}, ${todaysMatch[1].date}"

                        imgTeam1B.setImageResource(TeamImage.getLogo(todaysMatch[1].team1))
                        imgTeam2B.setImageResource(TeamImage.getLogo(todaysMatch[1].team2))
                        tvTeam1B.text=todaysMatch[1].team1.name
                        tvTeam2B.text=todaysMatch[1].team2.name
                        tvStadiumB.text=todaysMatch[1].stadium
                        tvVenueB.text=todaysMatch[1].place
                    }
                }
                updateUI(schedules)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        cardToday1.setOnClickListener {
            if(ind>-1){
                val intent  = Intent(applicationContext, PredictActivity::class.java)
                intent.putExtra("INDEX",ind)
                startActivity(intent)
            }
        }

        cardToday2.setOnClickListener {
            if(ind1>-1){
                val intent  = Intent(applicationContext, PredictActivity::class.java)
                intent.putExtra("INDEX",ind1)
                startActivity(intent)
            }
        }
    }

    fun updateUI(schedules: MutableList<Schedule>) {
  val adapter = PredictAllMatchesAdapter(schedules )
        rvAllMatches.adapter = adapter
        rvAllMatches.layoutManager = LinearLayoutManager(applicationContext)
    }


}