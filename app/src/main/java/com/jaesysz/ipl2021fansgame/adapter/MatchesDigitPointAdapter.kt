package com.jaesysz.ipl2021fansgame.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.models.DatabaseAllUsersSingleMatchDigitsModel
import kotlinx.android.synthetic.main.item_matches_digit_points.view.*


class MatchesDigitPointAdapter (
    private var players:MutableList<DatabaseAllUsersSingleMatchDigitsModel>
        ):RecyclerView.Adapter<MatchesDigitPointAdapter.MatchesDigitPointViewHolder>(){

    inner class MatchesDigitPointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesDigitPointViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_matches_digit_points, parent, false)
        return MatchesDigitPointViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchesDigitPointViewHolder, position: Int) {
        holder.itemView.apply {
            val item=players[position]
           tvUserName.text=item.userName
            team1Digit.text=item.team1Digit
            team2Digit.text=item.team2Digit
        }
    }

    override fun getItemCount(): Int =players.size
}