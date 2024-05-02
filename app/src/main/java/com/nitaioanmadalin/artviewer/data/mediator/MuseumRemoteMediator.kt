package com.nitaioanmadalin.artviewer.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nitaioanmadalin.artviewer.core.utils.Constants
import com.nitaioanmadalin.artviewer.data.local.dao.MuseumDatabase
import com.nitaioanmadalin.artviewer.data.local.entity.ArtObjectEntity
import com.nitaioanmadalin.artviewer.data.local.entity.MuseumRemoteKeysEntity
import com.nitaioanmadalin.artviewer.data.remote.api.MuseumApi

@OptIn(ExperimentalPagingApi::class)
class MuseumRemoteMediator(
    private val museumDatabase: MuseumDatabase,
    private val museumApi: MuseumApi
) : RemoteMediator<Int, ArtObjectEntity>() {

    private val museumDao = museumDatabase.museumDao
    private val museumRemoteKeysDao = museumDatabase.museumRemoteKeysDao

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArtObjectEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = museumApi.getCollections(page = currentPage, pageSize = Constants.ITEMS_PER_PAGE)
            val endOfPaginationReached = response.artObjects.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            museumDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    museumDao.clearAll()
                    museumRemoteKeysDao.deleteAllRemoteKeys()
                }
                val keys = response.artObjects
                    .filterNotNull()
                    .map { artObject ->
                    MuseumRemoteKeysEntity(
                        id = artObject.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                museumRemoteKeysDao.addAllRemoteKeys(remoteKeys = keys)
                museumDao.addCollections(response.artObjects.filterNotNull().map { it.toArtObjectEntity() })
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ArtObjectEntity>
    ): MuseumRemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                museumRemoteKeysDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, ArtObjectEntity>
    ): MuseumRemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { artObject ->
                museumRemoteKeysDao.getRemoteKeys(id = artObject.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, ArtObjectEntity>
    ): MuseumRemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { artObject ->
                museumRemoteKeysDao.getRemoteKeys(id = artObject.id)
            }
    }
}
