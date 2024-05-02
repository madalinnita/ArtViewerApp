package com.nitaioanmadalin.artviewer.core.utils.network

sealed class AppResult<out T> {
    data class Loading<out T>(val daoData: T? = null) : AppResult<T>()
    data class Success<out T>(val successData: T) : AppResult<T>()
    class Error(
        val exception: Throwable,
        val message: String? = exception.localizedMessage
    ) : AppResult<Nothing>()
}