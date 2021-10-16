package com.jaesysz.ipl2021fansgame.adapter

import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.appData.TeamImage
import com.jaesysz.ipl2021fansgame.models.PointTableModel
import com.jaesysz.ipl2021fansgame.ui.user.MoreDetailsActivity
import kotlinx.android.synthetic.main.item_card_point_table.view.*


class PointTableAdapter(private var pointTable: MutableList<PointTableModel>) :
    RecyclerView.Adapter<PointTableAdapter.PointTableViewHolder>() {

    inner class PointTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointTableViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_card_point_table, parent, false)
        return PointTableViewHolder(view)
    }

    override fun onBindViewHolder(holder: PointTableViewHolder, position: Int) {
        holder.itemView.apply {
            val item = pointTable[position]

         val image:Int= when(position){
                0->R.drawable.ic_medal_first
             1->R.drawable.ic_medal_second
             2->R.drawable.ic_medal_third
             else->R.drawable.ic_baseball_24
            }
            imgTag.setImageResource(image)

            tvRank.text = "${position + 1}"
            imgTeamLogo.setImageResource(TeamImage.getLogo(item.championTeam))
            tvUserName.text = item.userName
            tvTotalPoints.text = item.totalPoints.toString()

            setOnClickListener {
                val intent = Intent(it.context, MoreDetailsActivity::class.java)
                intent.putExtra("username", item.userName)
                intent.putExtra("rank", item.userName)
                intent.putExtra("totalpoints", item.totalPoints.toString())
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = pointTable.size
}