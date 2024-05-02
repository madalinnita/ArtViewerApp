package com.nitaioanmadalin.artviewer.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nitaioanmadalin.artviewer.core.utils.Constants
import com.nitaioanmadalin.artviewer.data.local.dao.MuseumDatabase
import com.nitaioanmadalin.artviewer.data.local.entity.ArtObjectEntity
import com.nitaioanmadalin.artviewer.data.mediator.MuseumRemoteMediator
import com.nitaioanmadalin.artviewer.data.remote.api.MuseumApi
import com.nitaioanmadalin.artviewer.domain.repository.MuseumRepository
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class MuseumRepositoryImpl(
    private val museumApi: MuseumApi,
    private val museumDatabase: MuseumDatabase
) : MuseumRepository {
    override fun getCollections(): Flow<PagingData<ArtObjectEntity>> {
        val pagingSourceFactory = { museumDatabase.museumDao.getAllCollections() }
        return Pager(
            config = PagingConfig(pageSize = Constants.ITEMS_PER_PAGE),
            remoteMediator = MuseumRemoteMediator(
                museumDatabase = museumDatabase,
                museumApi = museumApi
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}
