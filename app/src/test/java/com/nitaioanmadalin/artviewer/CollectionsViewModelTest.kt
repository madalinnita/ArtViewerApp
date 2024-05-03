package com.nitaioanmadalin.artviewer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.nitaioanmadalin.artviewer.core.utils.coroutine.CoroutineDispatchersProvider
import com.nitaioanmadalin.artviewer.data.local.entity.ArtObjectEntity
import com.nitaioanmadalin.artviewer.data.local.entity.HeaderImageEntity
import com.nitaioanmadalin.artviewer.domain.usecase.collections.GetCollectionsUseCase
import com.nitaioanmadalin.artviewer.ui.collections.CollectionsViewModel
import com.nitaioanmadalin.artviewer.ui.collections.CollectionsViewState
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionsViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CollectionsViewModel

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
    private val dispatchers = object : CoroutineDispatchersProvider {
        override fun io(): CoroutineDispatcher = testDispatcher
        override fun main(): CoroutineDispatcher = testDispatcher
        override fun computation(): CoroutineDispatcher = testDispatcher
    }

    private val scope = TestScope(testDispatcher)

    @MockK(relaxed = true)
    private lateinit var getCollectionsUseCase: GetCollectionsUseCase

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = CollectionsViewModel(getCollectionsUseCase, dispatchers)
    }

    @Test
    fun `when museumPagingFlow is retrieved then call getCollection on GetCollectionsUseCase`() =
        runTest(testDispatcher) {
            val pagingData =
                PagingData.from(listOf(getArtObjectEntity("2"), getArtObjectEntity("3")))
            val mockedFlow = flowOf(pagingData)

            every {
                getCollectionsUseCase.getCollections()
            } returns mockedFlow

            viewModel.museumPagingFlow

            verify(exactly = 1) {
                getCollectionsUseCase.getCollections()
            }
        }

    @Test
    fun `when setStateDependingOn is called with refresh - Loading then trigger Loading event`() =
        scope.runTest {
            val viewStateList = mutableListOf<CollectionsViewState>()
            val job = launch {
                viewModel.state.toList(viewStateList)
            }

            viewModel.setStateDependingOn(
                CombinedLoadStates(
                    refresh = LoadState.Loading,
                    append = LoadState.Error(Throwable()),
                    prepend = LoadState.Error(Throwable()),
                    source = LoadStates(
                        LoadState.Loading,
                        LoadState.Error(Throwable()),
                        LoadState.Error(Throwable())
                    )
                )
            )

            advanceTimeBy(1000)

            val state = viewStateList.last()
            Assert.assertTrue(state is CollectionsViewState.Loading)

            job.cancel()
        }

    @Test
    fun `when setStateDependingOn is called with refresh - Error then trigger Error event`() =
        scope.runTest {
            val viewStateList = mutableListOf<CollectionsViewState>()
            val job = launch {
                viewModel.state.toList(viewStateList)
            }

            viewModel.setStateDependingOn(
                CombinedLoadStates(
                    refresh = LoadState.Error(Throwable()),
                    append = LoadState.Error(Throwable()),
                    prepend = LoadState.Error(Throwable()),
                    source = LoadStates(
                        LoadState.Error(Throwable()),
                        LoadState.Error(Throwable()),
                        LoadState.Error(Throwable())
                    )
                )
            )

            advanceTimeBy(1000)

            val state = viewStateList.last()
            Assert.assertTrue(state is CollectionsViewState.Error)

            job.cancel()
        }

    @Test
    fun `when setStateDependingOn is called with append - Loading then trigger Error event with showNextLoading true`() =
        scope.runTest {
            val viewStateList = mutableListOf<CollectionsViewState>()
            val job = launch {
                viewModel.state.toList(viewStateList)
            }

            viewModel.setStateDependingOn(
                CombinedLoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = false),
                    append = LoadState.Loading,
                    prepend = LoadState.NotLoading(endOfPaginationReached = false),
                    source = LoadStates(
                        LoadState.NotLoading(endOfPaginationReached = false),
                        LoadState.Loading,
                        LoadState.NotLoading(endOfPaginationReached = false)
                    )
                )
            )

            advanceTimeBy(1000)

            val state = viewStateList.last()
            Assert.assertTrue(state is CollectionsViewState.Success)
            Assert.assertTrue((state as? CollectionsViewState.Success)?.showNextLoading == true)

            job.cancel()
        }

    @Test
    fun `when setStateDependingOn is called with append - NotLoading then trigger Error event with showNextLoading false`() =
        scope.runTest {
            val viewStateList = mutableListOf<CollectionsViewState>()
            val job = launch {
                viewModel.state.toList(viewStateList)
            }

            viewModel.setStateDependingOn(
                CombinedLoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = false),
                    append = LoadState.NotLoading(endOfPaginationReached = false),
                    prepend = LoadState.NotLoading(endOfPaginationReached = false),
                    source = LoadStates(
                        LoadState.NotLoading(endOfPaginationReached = false),
                        LoadState.NotLoading(endOfPaginationReached = false),
                        LoadState.NotLoading(endOfPaginationReached = false)
                    )
                )
            )

            advanceTimeBy(1000)

            val state = viewStateList.last()
            Assert.assertTrue(state is CollectionsViewState.Success)
            Assert.assertTrue((state as? CollectionsViewState.Success)?.showNextLoading == false)

            job.cancel()
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
}