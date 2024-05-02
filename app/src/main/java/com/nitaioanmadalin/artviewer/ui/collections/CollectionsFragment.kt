package com.nitaioanmadalin.artviewer.ui.collections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.nitaioanmadalin.artviewer.ui.collections.CollectionsViewModel
import com.nitaioanmadalin.artviewer.ui.base.BaseComposeWrapperFragment
import com.nitaioanmadalin.artviewer.ui.collections.screen.CollectionsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionsFragment: BaseComposeWrapperFragment() {

    private val viewModel by activityViewModels<CollectionsViewModel>()

    @Composable
    override fun FragmentContent(modifier: Modifier) {
        val artObjects = viewModel.museumPagingFlow.collectAsLazyPagingItems()
        val viewState = viewModel.state.collectAsState()
        CollectionsScreen(
            artObjects = artObjects,
            screenState = viewState.value,
            onCollectionClicked = {
                val action = CollectionsFragmentDirections.collectionsToDetails(it)
                findNavController().navigate(action)
            },
            setState = {
                viewModel.setStateDependingOn(it)
            }
        )
    }
}