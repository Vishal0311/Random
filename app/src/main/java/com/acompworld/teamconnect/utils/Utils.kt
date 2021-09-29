package com.acompworld.teamconnect.utils

import android.content.Context
import android.content.Intent

object Utils {
    fun moveTo(context: Context?, className: Class<*>?) {
        if(context!=null) {
            val intent = Intent(context, className)
            context.startActivity(intent)
        }
    }

    fun moveToAndClearHistory(context: Context?, className: Class<*>?) {
        if(context!=null){
            val intent = Intent(context, className)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }
}