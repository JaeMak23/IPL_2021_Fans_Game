package com.jaesysz.ipl2021fansgame.appData

import com.jaesysz.ipl2021fansgame.R
import com.jaesysz.ipl2021fansgame.enums.Teams

object TeamImage {

    fun getLogo(position: Int): Int {
        return when (position) {
            1 -> R.drawable.logo_csk
            2 -> R.drawable.logo_dc
            3 -> R.drawable.logo_kkr
            4 -> R.drawable.logo_pbks
            5 -> R.drawable.logo_mi
            6 -> R.drawable.logo_rcb
            7 -> R.drawable.logo_rr
            8 -> R.drawable.logo_srh
            else -> R.drawable.logo_na
        }
    }

    fun getLogo(team: Teams): Int {
        return when (team) {
            Teams.CSK -> R.drawable.logo_csk
            Teams.DC -> R.drawable.logo_dc
            Teams.KKR -> R.drawable.logo_kkr
            Teams.PBKS -> R.drawable.logo_pbks
            Teams.MI -> R.drawable.logo_mi
            Teams.RCB -> R.drawable.logo_rcb
            Teams.RR -> R.drawable.logo_rr
            Teams.SRH -> R.drawable.logo_srh
            else -> R.drawable.logo_na
        }
    }

    fun getLogo(teamName: String): Int {
        return when (teamName) {
            Teams.CSK.name -> R.drawable.logo_csk
            Teams.DC.name -> R.drawable.logo_dc
            Teams.KKR.name -> R.drawable.logo_kkr
            Teams.PBKS.name -> R.drawable.logo_pbks
            Teams.MI.name -> R.drawable.logo_mi
            Teams.RCB.name -> R.drawable.logo_rcb
            Teams.RR.name -> R.drawable.logo_rr
            Teams.SRH.name -> R.drawable.logo_srh
            else -> R.drawable.logo_na
        }
    }

    fun getBackground(team:Teams):Int
    {
        return when (team) {
            Teams.CSK -> R.drawable.csk_background
            Teams.DC-> R.drawable.dc_background
            Teams.KKR-> R.drawable.kkr_background
            Teams.PBKS -> R.drawable.kxip_background
            Teams.MI-> R.drawable.mi_background
            Teams.RCB -> R.drawable.rcb_background_gradient
            Teams.RR -> R.drawable.rr_background
            Teams.SRH -> R.drawable.srh_background
            else -> R.drawable.gradient_plum_plate
        }
    }

}