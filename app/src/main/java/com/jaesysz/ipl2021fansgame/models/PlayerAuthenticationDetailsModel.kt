package com.jaesysz.ipl2021fansgame.models

import java.io.Serializable

data class PlayerAuthenticationDetailsModel(
    val uid:String ="000",
    val email:String="none@mail.com",
    val userName: String="user1",
    val emailVerified:Boolean=false
):Serializable
