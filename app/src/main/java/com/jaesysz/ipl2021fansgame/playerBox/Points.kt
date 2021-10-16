package com.jaesysz.ipl2021fansgame.playerBox

import com.jaesysz.ipl2021fansgame.enums.Digits
import com.jaesysz.ipl2021fansgame.enums.Teams
import com.jaesysz.ipl2021fansgame.models.PlayerPointsModel
import com.jaesysz.ipl2021fansgame.models.PlayerProfileModel
import com.jaesysz.ipl2021fansgame.models.Schedule
import com.jaesysz.ipl2021fansgame.models.admin.IPL2021Results

object Points {

    fun get(profile:PlayerProfileModel,schedules: MutableList<Schedule>,iplResult : IPL2021Results):PlayerPointsModel{
        var played =0
        var win =0
        var draw=0
        var lost=0
        var singleDigit=0
        var doubleDigit=0
        var winPoints:Int=0
        var digitPoints:Int=0
        var totalPoints:Int=0
        var champPoints:Int=0

        for((id,userRes) in profile.matchResults.withIndex()){
            var t1 = false
            var t2 = false

            if(schedules[id].result!="NA") {
                if (userRes.winner != "NA") {
                    if (schedules[id].result == schedules[id].team1.name || schedules[id].result == schedules[id].team2.name)
                        if (userRes.winner == schedules[id].result)
                            win += 1
                        else
                            lost += 1
                    else
                        draw += 1

                    played += 1
                }
            }

            if(schedules[id].team1Digit!="NA") {
                if (userRes.team1Digit != "NA")
                    if (userRes.team1Digit == schedules[id].team1Digit)
                        t1 = true
            }

            if(schedules[id].team2Digit!="NA") {
                if (userRes.team2Digit != Digits.NA.name)
                    if (userRes.team2Digit == schedules[id].team2Digit)
                        t2 = true
            }

            if (t1 && t2)
                doubleDigit += 1
            else if (t1 || t2)
                singleDigit += 1
        }

        winPoints = (win * 100) + (draw * 50)
        digitPoints = (singleDigit * 40) + (doubleDigit * 120)

         champPoints =
            if (iplResult.championTeam != Teams.NA) {
                if (profile.championTeam == iplResult.championTeam) 500
                else if (profile.championTeam == iplResult.runnerUp) 300
                else 0
            } else 0

        totalPoints=champPoints + winPoints + digitPoints

        return PlayerPointsModel(played, win, draw, lost, singleDigit, doubleDigit, winPoints, digitPoints,champPoints, totalPoints)
    }
}