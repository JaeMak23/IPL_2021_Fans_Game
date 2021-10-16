package com.jaesysz.ipl2021fansgame.extensions

import android.app.Activity
import android.widget.Toast

object Extensions {
    fun Activity.msg(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}