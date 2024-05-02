package com.nitaioanmadalin.artviewer.ui.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.nitaioanmadalin.artviewer.domain.usecase.collections.GetCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    getCollectionsUseCase: GetCollectionsUseCase
) : ViewModel() {
    val museumPagingFlow =
        getCollectionsUseCase
            .getCollections()
            .map { pagingData ->
                pagingData.map { it.toArtObject() }
            }
            .cachedIn(viewModelScope)
}
