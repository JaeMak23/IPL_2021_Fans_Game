package com.jaesysz.ipl2021fansgame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.TeamImage
import com.jaesysz.ipl2021fansgame.models.Schedule
import kotlinx.android.synthetic.main.item_predict_card.view.*

class PredictCardAdapter(var con: Context, var schedule: MutableList<Schedule>) :
    RecyclerView.Adapter<PredictCardAdapter.PredictCardViewHolder>() {

    inner class PredictCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictCardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_predict_card, parent, false)
        return PredictCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: PredictCardViewHolder, position: Int) {

        holder.itemView.apply {

          val  item=schedule[position]
            this.tvMatchIndex.text = (position+1).toString()
            this.tvMatchTitle.text = item.matchNo
            this.tvTime.text = item.indianTime

            this.imgTeamA.setImageResource(TeamImage.getLogo( item.team1))
            this.imgTeamB.setImageResource(TeamImage.getLogo(item.team2))

            this.tvDate.text ="${item.date} ${item.day}"
            this.tvVenue.text = item.stadium
            this.tvPlace.text = item.place

           /* this.tvTeamA.text = schedule[position].team1.name
            this.tvTeamB.text = schedule[position].team2.name*/

           /* digitSpinner(this.spTeamADig, this.context)
            digitSpinner(this.spTeamBDig, this.context)
            winnerSpinner(this.spWinner, context,schedule[position].team1.name,schedule[position].team2.name)*/

           /* btSubmit.setOnClickListener {
                var digitA: Digits = Digits.NA
                var digitB: Digits = Digits.NA
                var result:String="NA"

                if (this.spTeamADig != null)
                    digitA = Digits.valueOf(this.spTeamADig.selectedItem.toString())

                if (this.spTeamBDig != null)
                    digitB = Digits.valueOf(this.spTeamBDig.selectedItem.toString())

                if (this.spWinner != null) {
                  result=spWinner.selectedItem.toString()
                }

                Toast.makeText(this.context," TA : $digitA,  TB: $digitB, Winner : $result",Toast.LENGTH_SHORT).show()
            }*/
        }
    }

    override fun getItemCount(): Int {
        return schedule.size
    }

   /* private fun digitSpinner(spinner: Spinner, con: Context) {
        val adapter =
            ArrayAdapter(con, android.R.layout.simple_spinner_dropdown_item, Digits.values())
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }*/

   /* private fun winnerSpinner(spinner: Spinner, con: Context, ta: String, tb: String) {

        val list = mutableListOf("NA", ta, tb, "DRAW", "CANCELED")

        val adapter = ArrayAdapter(con, android.R.layout.simple_spinner_dropdown_item, list.toList())
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }*/


}