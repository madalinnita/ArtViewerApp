package com.nitaioanmadalin.artviewer.domain.repository

import androidx.paging.PagingData
import com.nitaioanmadalin.artviewer.data.local.entity.ArtObjectEntity
import kotlinx.coroutines.flow.Flow

interface MuseumRepository {
    fun getCollections(): Flow<PagingData<ArtObjectEntity>>
}