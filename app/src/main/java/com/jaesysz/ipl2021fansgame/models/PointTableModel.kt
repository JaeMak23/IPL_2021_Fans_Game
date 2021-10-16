package com.jaesysz.ipl2021fansgame.models

import com.jaesysz.ipl2021fansgame.enums.Teams
import java.io.Serializable

data class PointTableModel(
    val rank:Int=0,
    val userName: String="user1",
    val championTeam:Teams=Teams.NA,
   val totalPoints:Int=0
):Serializable
