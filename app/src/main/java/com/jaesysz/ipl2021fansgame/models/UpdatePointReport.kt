package com.jaesysz.ipl2021fansgame.models

import java.io.Serializable

data class UpdatePointReport(
    val played:Int=0,
    val win:Int=0,
    val draw:Int=0,
    val lost:Int=0,
    val singleDigit:Int=0,
    val doubleDigit:Int=0
):Serializable
