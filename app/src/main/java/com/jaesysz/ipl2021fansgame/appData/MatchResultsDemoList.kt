package com.jaesysz.ipl2021fansgame.appData

import com.jaesysz.ipl2021fansgame.models.PlayerMatchResult

object MatchResultsDemoList {

    fun get():MutableList<PlayerMatchResult>{
       val list:MutableList<PlayerMatchResult> = mutableListOf()
        for(id in 1..60){
            list.add(PlayerMatchResult())
        }
        return list
    }
}