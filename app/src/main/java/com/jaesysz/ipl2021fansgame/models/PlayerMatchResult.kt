package com.jaesysz.ipl2021fansgame.models

import java.io.Serializable

data class PlayerMatchResult(
    val team1Digit:String="NA",
    val team2Digit:String="NA",
    val winner:String="NA"
):Serializable
