package com.nitaioanmadalin.artviewer.ui.collectiondetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nitaioanmadalin.artviewer.ui.base.BaseComposeWrapperFragment
import com.nitaioanmadalin.artviewer.ui.collectiondetails.screen.CollectionDetailsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionDetailsFragment: BaseComposeWrapperFragment() {

    private val args: CollectionDetailsFragmentArgs by navArgs()

    @Composable
    override fun FragmentContent(modifier: Modifier) {
        CollectionDetailsScreen(
            artObject = args.artObject,
            navigator = findNavController()
        )
    }
}