package com.nitaioanmadalin.artviewer.ui.collections

sealed class CollectionsViewState {
    object Loading: CollectionsViewState()

    data class Error(
        val ex: Throwable
    ): CollectionsViewState()

    data class Success(
        val showNextLoading: Boolean
    ): CollectionsViewState()
}