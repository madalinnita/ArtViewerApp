package com.nitaioanmadalin.artviewer.ui.collections.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.nitaioanmadalin.artviewer.domain.model.ArtObject
import com.nitaioanmadalin.artviewer.ui.collections.content.CollectionsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsScreen(
    artObjects: LazyPagingItems<ArtObject>,
    onCollectionClicked: (ArtObject) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = artObjects.loadState) {
        if(artObjects.loadState.refresh is LoadState.Error) {
            val errorMessage = "Error - ${(artObjects.loadState.refresh as LoadState.Error).error.message}"
            Log.e(TAG, errorMessage)
            Toast.makeText(
                context,
                errorMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        topBar = {
            CollectionsTopBar()
        }
    ) { paddingValues ->
        Surface(Modifier.padding(paddingValues)) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (artObjects.loadState.refresh is LoadState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    CollectionsContent(
                        artObjects = artObjects,
                        onCollectionClicked = onCollectionClicked
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsTopBar() {
    TopAppBar(
        title = { Text("Collections", color = Color.Black) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.LightGray,
            titleContentColor = Color.White
        )
    )
}

private const val TAG = "CollectionsScreen"