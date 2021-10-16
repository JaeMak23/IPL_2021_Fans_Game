package com.jaesysz.ipl2021fansgame.models

import com.jaesysz.ipl2021fansgame.enums.Teams
import java.io.Serializable

data class Schedule(
    val date:String="NA",
    val day:String="NA",
    val between:String="NA",
    val team1:Teams=Teams.NA,
    val team2:Teams=Teams.NA,
    val matchNo:String="NA",
    val stadium:String="NA", //stadium
    val place:String="NA",
    val indianTime:String="NA",
    val globalTime:String="NA",
    val team1Digit:String="NA",
    val team2Digit:String="NA",
    val result:String="NA"
):Serializable
