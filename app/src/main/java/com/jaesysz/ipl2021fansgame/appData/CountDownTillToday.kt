package com.jaesysz.ipl2021fansgame.appData

import java.text.SimpleDateFormat
import java.util.*

class CountDownTillToday(dateStr: String, timeStr: String) {

    private var str = ""
    private var isDone = false
    private var leftDays: Long = 0
    private var leftHours: Long = 0
    private var leftMinutes: Long = 0
    private var leftMilli: Long = 0
    private var leftSeconds: Long = 0

    fun getString(): String = str
    fun isCountEnded(): Boolean = isDone
    fun getLeftDays(): Long = leftDays
    fun getLeftHours(): Long = leftHours
    fun getLeftMinutes(): Long = leftMinutes
    fun getLeftSeconds(): Long = leftSeconds
    fun getLeftMilliSeconds(): Long = leftMilli

    init {
        val date = createDate(dateStr, timeStr)!!

        val now = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            .parse(SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date()))!!

        val count=(date.time - now.time)
        leftMilli = if(count>0) count else 0

        leftDays = leftMilli / 86400000
        leftHours = leftMilli % 86400000 / 3600000
        leftMinutes = leftMilli % 86400000 % 3600000 / 60000
        leftSeconds=leftMilli % 86400000 % 3600000 % 60000/1000

        str = "$leftDays Days, $leftHours Hours, $leftMinutes Minutes, $leftSeconds Seconds"

        isDone = if (leftDays > 0)
            false
        else if (leftHours > 0)
            false
        else
            leftMinutes <= 0

    }

    private fun createDate(date: String, time: String): Date? {
        val time = convertTime12to24(time)
        val d = SimpleDateFormat("MMM-dd").parse(date)
        val date = SimpleDateFormat("dd/MM/2021").format(d)
        return SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("$date $time")
    }

    private fun convertTime12to24(time: String): String {
        val aaa = time.split(" ")
        val split = aaa[0].split(':')
        val hour: Int = split[0].toInt()
        val hr = if (aaa[1] == "PM") hour + 12 else hour
        return "$hr:${split[1]}:00 ${aaa[1]}"
    }
}