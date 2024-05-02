package com.nitaioanmadalin.artviewer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nitaioanmadalin.artviewer.core.utils.coroutine.CoroutineDispatchersProvider
import com.nitaioanmadalin.artviewer.core.utils.log.LogProvider
import com.nitaioanmadalin.artviewer.core.utils.network.AppResult
import com.nitaioanmadalin.artviewer.core.utils.network.ConnectivityUtils
import com.nitaioanmadalin.artviewer.core.utils.rx.SchedulerProvider
import com.nitaioanmadalin.artviewer.domain.usecase.collections.GetCollectionsUseCase
import com.nitaioanmadalin.artviewer.ui.collections.CollectionsScreenState
import com.nitaioanmadalin.artviewer.ui.collections.CollectionsViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val testScheduler = Schedulers.trampoline()
    private val scheduler = object: SchedulerProvider {
        override fun io(): Scheduler = testScheduler
        override fun main(): Scheduler = testScheduler
        override fun computation(): Scheduler = testScheduler
    }

    private val scope = TestScope(testDispatcher)

    @MockK(relaxed = true)
    private lateinit var connectivityUtils: ConnectivityUtils

    @MockK(relaxed = true)
    private lateinit var getCollectionsUseCase: GetCollectionsUseCase

    @MockK(relaxed = true)
    private lateinit var logProvider: LogProvider

    @MockK
    private lateinit var stateObserver: Observer<CollectionsScreenState>

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = CollectionsViewModel(getCollectionsUseCase, connectivityUtils, dispatchers, scheduler, logProvider)
    }

    @Test
    fun `getRxData emits loading state when use case returns loading`() = scope.runTest {
        val loadingResult = AppResult.Loading<List<Pet>>(emptyList())
        every {
            getCollectionsUseCase.getRxPetList()
        } returns Observable.just(loadingResult)

        val viewStateList = mutableListOf<CollectionsScreenState>()
        val job = launch {
            viewModel.state.toList(viewStateList)
        }

        viewModel.getRxData()

        advanceTimeBy(1000)

        val state = viewStateList.last()
        Assert.assertTrue(state is CollectionsScreenState.Loading && state.daoRepositories?.isEmpty() == true)

        job.cancel()
    }

    @Test
    fun `getRxData emits error state when use case returns error`() = scope.runTest {
        val errorResult = AppResult.Error(Throwable())
        every {
            getCollectionsUseCase.getRxPetList()
        } returns Observable.just(errorResult)

        val viewStateList = mutableListOf<CollectionsScreenState>()
        val job = launch {
            viewModel.state.toList(viewStateList)
        }

        viewModel.getRxData()

        advanceTimeBy(1000)

        val state = viewStateList.last()
        Assert.assertTrue(state is CollectionsScreenState.Error)

        job.cancel()
    }

    @Test
    fun `getRxData emits success state when use case returns success`() = scope.runTest {
        val pets = listOf(Pet(name = "Dog"), Pet(name = "Cat"))
        val successResult = AppResult.Success(pets)
        every {
            getCollectionsUseCase.getRxPetList()
        } returns Observable.just(successResult)

        val viewStateList = mutableListOf<CollectionsScreenState>()
        val job = launch {
            viewModel.state.toList(viewStateList)
        }

        viewModel.getRxData()

        advanceTimeBy(1000)

        val state = viewStateList.last()
        Assert.assertTrue(state is CollectionsScreenState.Success && state.repositories.size == 2)

        job.cancel()
    }
}