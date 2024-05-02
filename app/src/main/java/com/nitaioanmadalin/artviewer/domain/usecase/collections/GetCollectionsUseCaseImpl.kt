package com.nitaioanmadalin.artviewer.domain.usecase.collections

import androidx.paging.PagingData
import com.nitaioanmadalin.artviewer.data.local.entity.ArtObjectEntity
import com.nitaioanmadalin.artviewer.domain.repository.MuseumRepository
import kotlinx.coroutines.flow.Flow

class GetCollectionsUseCaseImpl(
    private val repository: MuseumRepository
): GetCollectionsUseCase {
    override fun getCollections(): Flow<PagingData<ArtObjectEntity>> {
        return repository.getCollections()
    }
}