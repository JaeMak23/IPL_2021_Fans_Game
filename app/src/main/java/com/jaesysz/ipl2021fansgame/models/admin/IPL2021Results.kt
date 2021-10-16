package com.jaesysz.ipl2021fansgame.models.admin

import com.jaesysz.ipl2021fansgame.enums.Teams
import java.io.Serializable

data class IPL2021Results(
    val championTeam:Teams=Teams.NA,
    val runnerUp:Teams=Teams.NA
):Serializable
