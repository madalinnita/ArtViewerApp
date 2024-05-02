package com.nitaioanmadalin.artviewer.data.remote.response

import com.nitaioanmadalin.artviewer.data.remote.dto.ArtObjectDto

data class CollectionsResponse(
    val artObjects: List<ArtObjectDto?> = emptyList()
)