package com.nitaioanmadalin.artviewer.ui.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import androidx.paging.map
import com.nitaioanmadalin.artviewer.core.utils.coroutine.CoroutineDispatchersProvider
import com.nitaioanmadalin.artviewer.domain.usecase.collections.GetCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    getCollectionsUseCase: GetCollectionsUseCase,
    dispatchers: CoroutineDispatchersProvider
) : ViewModel() {
    private val _state: MutableStateFlow<CollectionsViewState> = MutableStateFlow(
        CollectionsViewState.Loading
    )
    val state: StateFlow<CollectionsViewState> = _state

    val museumPagingFlow =
        getCollectionsUseCase
            .getCollections()
            .map { pagingData ->
                pagingData.map { it.toArtObject() }
            }
            .flowOn(dispatchers.io())
            .cachedIn(viewModelScope)

    fun setStateDependingOn(loadState: CombinedLoadStates) {
        when (loadState.refresh) {
            is LoadState.Error -> {
                _state.value = CollectionsViewState.Error(
                    ex = (loadState.refresh as LoadState.Error).error
                )
            }

            is LoadState.Loading -> {
                _state.value = CollectionsViewState.Loading
            }

            else -> {
                _state.value = CollectionsViewState.Success(
                    showNextLoading = loadState.append is LoadState.Loading
                )
            }
        }
    }
}
