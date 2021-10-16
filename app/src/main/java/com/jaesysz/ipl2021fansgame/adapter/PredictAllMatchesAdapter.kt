package com.jaesysz.ipl2021fansgame.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.TeamImage
import com.jaesysz.ipl2021fansgame.models.Schedule
import com.jaesysz.ipl2021fansgame.views.user.PredictActivity
import kotlinx.android.synthetic.main.item_predict_all_matches.view.*

class PredictAllMatchesAdapter(private var schedules: MutableList<Schedule>):RecyclerView.Adapter<PredictAllMatchesAdapter.PredictAllMatchesViewHolder>() {
    inner class PredictAllMatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictAllMatchesViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_predict_all_matches, parent, false)
        return PredictAllMatchesViewHolder(view)
    }

    override fun onBindViewHolder(holder: PredictAllMatchesViewHolder, position: Int) {
        holder.itemView.apply {
           val item=schedules[position]
            tvMatchIndex.text="T20 ${position+1} of 60"
            tvMatchNumber.text=item.matchNo
            tvMatchDate.text="${item.day}, ${item.date}"

            imgTeam1.setImageResource(TeamImage.getLogo(item.team1))
            imgTeam2.setImageResource(TeamImage.getLogo(item.team2))

            tvTeam1.text=item.team1.name
            tvTeam2.text=item.team2.name

            tvStadium.text=item.stadium
            tvVenue.text=item.place

            cardToday1.setOnClickListener {
                val intent  = Intent(it.context, PredictActivity::class.java)
                intent.putExtra("INDEX",position)
               it.context.startActivity(intent)

            }

        }
    }

    override fun getItemCount(): Int =schedules.size
}