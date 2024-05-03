package com.nitaioanmadalin.artviewer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.nitaioanmadalin.artviewer.data.local.dao.MuseumDao
import com.nitaioanmadalin.artviewer.data.local.dao.MuseumDatabase
import com.nitaioanmadalin.artviewer.data.local.dao.MuseumRemoteKeysDao
import com.nitaioanmadalin.artviewer.data.local.entity.ArtObjectEntity
import com.nitaioanmadalin.artviewer.data.local.entity.HeaderImageEntity
import com.nitaioanmadalin.artviewer.data.local.entity.MuseumRemoteKeysEntity
import com.nitaioanmadalin.artviewer.data.mediator.MuseumRemoteMediator
import com.nitaioanmadalin.artviewer.data.remote.api.MuseumApi
import com.nitaioanmadalin.artviewer.data.remote.response.CollectionsResponse
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
@OptIn(ExperimentalPagingApi::class)
class MuseumRemoteMediatorTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()


    private lateinit var museumRemoteMediator: MuseumRemoteMediator
    private lateinit var museumDatabase: MuseumDatabase
    private lateinit var museumDao: MuseumDao
    private lateinit var museumRemoteKeysDao: MuseumRemoteKeysDao
    private lateinit var museumApi: MuseumApi

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
    private val scope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        museumDatabase = mockk()
        museumDao = mockk(relaxed = true)
        museumRemoteKeysDao = mockk(relaxed = true)
        museumApi = mockk(relaxed = true)

        every { museumDatabase.museumDao } returns museumDao
        every { museumDatabase.museumRemoteKeysDao } returns museumRemoteKeysDao
        every { museumDatabase.transactionExecutor } returns Executors.newCachedThreadPool()
        every { museumDatabase.suspendingTransactionId } returns ThreadLocal()
        every { museumDatabase.beginTransaction() } just runs
        every { museumDatabase.endTransaction() } just runs
        every { museumDatabase.setTransactionSuccessful() } just runs

        museumRemoteMediator = MuseumRemoteMediator(museumDatabase, museumApi)
    }

    @Test
    fun `when load is triggered with LoadType REFRESH verify methods triggered`() = runBlocking {
        val loadType = LoadType.REFRESH
        val pagingState: PagingState<Int, ArtObjectEntity> = mockk(relaxed = true)
        val artObjectsResponse: CollectionsResponse = mockk(relaxed = true)

        every { pagingState.closestItemToPosition(any()) } returns getArtObjectEntity()

        coEvery { museumApi.getCollections(any(), any()) } returns artObjectsResponse
        coEvery { museumRemoteKeysDao.deleteAllRemoteKeys() } just runs
        coEvery { museumDao.clearAll() } just runs
        coEvery { museumRemoteKeysDao.addAllRemoteKeys(any()) } just runs
        coEvery { museumDao.addCollections(any()) } just runs
        coEvery { museumRemoteKeysDao.getRemoteKeys(any()) } returns MuseumRemoteKeysEntity(
            "id",
            2,
            1
        )

        val result = museumRemoteMediator.load(loadType, pagingState)

        assertTrue(result is RemoteMediator.MediatorResult.Success)
        coVerify(exactly = 1) { museumDao.clearAll() }
        coVerify(exactly = 1) { museumRemoteKeysDao.deleteAllRemoteKeys() }
        coVerify(exactly = 1) { museumRemoteKeysDao.addAllRemoteKeys(any()) }
        coVerify(exactly = 1) { museumDao.addCollections(any()) }
    }

    @Test
    fun `when load is triggered with LoadType REFRESH and Error is thrown verify methods triggered`() =
        runBlocking {
            val loadType = LoadType.REFRESH
            val pagingState: PagingState<Int, ArtObjectEntity> = mockk(relaxed = true)
            val artObjectsResponse: CollectionsResponse = mockk(relaxed = true)

            every { pagingState.closestItemToPosition(any()) } returns getArtObjectEntity()

            coEvery { museumApi.getCollections(any(), any()) } returns artObjectsResponse
            coEvery { museumRemoteKeysDao.deleteAllRemoteKeys() } just runs
            coEvery { museumDao.clearAll() } throws Exception("Mock exception")
            coEvery { museumRemoteKeysDao.addAllRemoteKeys(any()) } just runs
            coEvery { museumDao.addCollections(any()) } just runs
            coEvery { museumRemoteKeysDao.getRemoteKeys(any()) } returns MuseumRemoteKeysEntity(
                "id",
                2,
                1
            )

            val result = museumRemoteMediator.load(loadType, pagingState)

            assertTrue(result is RemoteMediator.MediatorResult.Error)
            assertTrue((result as? RemoteMediator.MediatorResult.Error)?.throwable?.message == "Mock exception")
            coVerify(exactly = 1) { museumDao.clearAll() }
            coVerify(exactly = 0) { museumRemoteKeysDao.deleteAllRemoteKeys() }
            coVerify(exactly = 0) { museumRemoteKeysDao.addAllRemoteKeys(any()) }
            coVerify(exactly = 0) { museumDao.addCollections(any()) }
        }
}

private fun getArtObjectEntity(
    id: String = "1"
) = ArtObjectEntity(
    id = id,
    objectNumber = "2-4-6",
    title = "Title",
    principalOrFirstMaker = "FirstMaker",
    longTitle = "Long title",
    headerImage = HeaderImageEntity(400.toLong(), 400.toLong(), ""),
    productionPlaces = emptyList()
)


