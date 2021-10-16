package com.jaesysz.ipl2021fansgame.models

import java.io.Serializable

data class DatabaseAllUsersSingleMatchDigitsModel(
    val userName: String ="NA",
    val team1Digit:String="NA",
    val team2Digit: String="NA"
):Serializable
