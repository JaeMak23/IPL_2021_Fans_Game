package com.jaesysz.ipl2021fansgame.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.TeamImage
import com.jaesysz.ipl2021fansgame.models.PlayerMatchResult

import com.jaesysz.ipl2021fansgame.models.Schedule
import kotlinx.android.synthetic.main.item_player_data.view.*

class PlayerDataAdapter(
    private var myMatches: MutableList<PlayerMatchResult>,
    private var schedules: MutableList<Schedule>,
    private var totalPoints: Int
) :
    RecyclerView.Adapter<PlayerDataAdapter.PlayerDataViewHolder>() {
    inner class PlayerDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerDataViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_player_data, parent, false)
        return PlayerDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerDataViewHolder, position: Int) {
        holder.itemView.apply {
            val my = myMatches[position]
            val sch = schedules[position]

            var win = false
            var draw = false

            tvIndex.text = "${position + 1}"

            imgTeam1.setImageResource(TeamImage.getLogo(sch.team1))
            imgTeam2.setImageResource(TeamImage.getLogo(sch.team2))

            val myTeam1Digit = my.team1Digit
            val myTeam2Digit = my.team2Digit

            val pgTeam1Digit = sch.team1Digit
            val pgTeam2Digit = sch.team2Digit

            tvTeam1Digit.text = myTeam1Digit
            tvTeam2Digit.text = myTeam2Digit

            val d1 = if (sch.team1Digit != "NA" && sch.team1Digit == myTeam1Digit) {
                tvTeam1Digit.setBackgroundResource(R.color.draw_yello_bg)
                true
            } else {
                tvTeam1Digit.setBackgroundResource(R.color.white)
                false
            }

            val d2 = if (sch.team2Digit != "NA" && sch.team2Digit == myTeam2Digit) {
                tvTeam2Digit.setBackgroundResource(R.color.draw_yello_bg)
                true
            } else {
                tvTeam2Digit.setBackgroundResource(R.color.white)
                false
            }

            if (sch.result != "NA") {
                if (sch.result == "DRAW") {
                    tvWin.setBackgroundResource(R.color.draw_yello_bg)
                    win = false
                    draw = true
                } else {
                    win = if (sch.result == my.winner) {
                        tvWin.setBackgroundResource(R.color.win_green_bg)
                        true
                    } else {
                        tvWin.setBackgroundResource(R.color.white)
                        false
                    }
                }
            } else {
                tvWin.setBackgroundResource(R.color.white)
                win = false
                draw = false
            }

            val digitPoint = if (d1 && d2) 120
            else if (d1 || d2) 40
            else 0

            var winPoint = 0
            winPoint = if (win) 100
            else if(draw) 50
            else 0


            tvTotal.text = "${winPoint + digitPoint}"
        }
    }

    override fun getItemCount(): Int = myMatches.size
}