package com.nitaioanmadalin.artviewer.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.nitaioanmadalin.artviewer.ui.theme.ArtViewerAppTheme
abstract class BaseComposeWrapperFragment: Fragment() {

    @Composable
    abstract fun FragmentContent(modifier: Modifier)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ArtViewerAppTheme {
                    FragmentContent(modifier = Modifier)
                }
            }
        }
    }
}