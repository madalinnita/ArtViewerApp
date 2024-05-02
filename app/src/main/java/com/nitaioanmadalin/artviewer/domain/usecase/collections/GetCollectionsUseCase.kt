package com.nitaioanmadalin.artviewer.domain.usecase.collections

import androidx.paging.PagingData
import com.nitaioanmadalin.artviewer.data.local.entity.ArtObjectEntity
import kotlinx.coroutines.flow.Flow

interface GetCollectionsUseCase {
    fun getCollections(): Flow<PagingData<ArtObjectEntity>>
}