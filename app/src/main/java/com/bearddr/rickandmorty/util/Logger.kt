package com.bearddr.rickandmorty.util

import android.util.Log

object Logger {
    fun logd(place: String, message: String) = Log.d("AppDebug", "$place: $message")
}