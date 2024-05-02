package com.nitaioanmadalin.artviewer.core.utils.log

import android.util.Log

class LogProviderImpl: LogProvider {
    override fun logDebug(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun logError(tag: String, message: String) {
        Log.e(tag, message)
    }

    override fun logInfo(tag: String, message: String) {
        Log.i(tag, message)
    }
}