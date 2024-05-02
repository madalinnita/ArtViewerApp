package com.nitaioanmadalin.artviewer.core.utils.network

import android.content.Context
import android.net.ConnectivityManager

class ConnectivityUtils {
    fun isConnectionAvailable(context: Context): Boolean {
        try {
            val conMgr = context.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = conMgr.activeNetworkInfo
            if (info == null) {
                return false
            } else if (info.state.toString() == "CONNECTED") {
                return true
            }
        } catch (e: Exception) {
        }
        return false
    }
}