package com.jaesysz.ipl2021fansgame.models

import com.jaesysz.ipl2021fansgame.appData.MatchResultsDemoList
import com.jaesysz.ipl2021fansgame.enums.Teams
import java.io.Serializable
data class PlayerProfileModel(
    val uid:String ="000",
    val email:String="none@mail.com",
    val userName: String="user1",
    val fullName:String="NoName",
    val championTeam:Teams=Teams.NA,
    val totalAttempted:Int=0,
    val totalWin:Int=0,
    val totalLost:Int=0,
    val totalDraw:Int=0,
    val totalSingleDigit:Int=0,
    val totalDoubleDigit:Int=0,
    val matchResults: MutableList<PlayerMatchResult> = MatchResultsDemoList.get()
) :Serializable
