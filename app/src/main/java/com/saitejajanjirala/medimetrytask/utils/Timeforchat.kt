package com.saitejajanjirala.medimetrytask.utils

import java.text.SimpleDateFormat
import java.util.*

class Timeforchat {
    companion object{
        fun gettimestamp():String{
            val sdf= SimpleDateFormat("yyyy-mm-dd HH:mm:ss")
            sdf.timeZone= TimeZone.getTimeZone("GMT")
            return sdf.format(Date())
        }
    }
}