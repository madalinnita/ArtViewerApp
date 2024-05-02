package com.nitaioanmadalin.artviewer.data.remote.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response


class TokenInterceptor(private val accessKey: String) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("key", accessKey)
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

    companion object {
        private const val TAG = "TokenInterceptor"
    }
}