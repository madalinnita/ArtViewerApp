package com.nitaioanmadalin.artviewer.data.remote.api

import com.nitaioanmadalin.artviewer.data.remote.response.CollectionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MuseumApi {

    @GET("/api/nl//collection?s=artist&imgonly=true")
    suspend fun getCollections(
        @Query("p") page: Int,
        @Query("ps") pageSize: Int
    ): CollectionsResponse

    companion object {
        const val BASE_URL = "https://www.rijksmuseum.nl/"
    }
}