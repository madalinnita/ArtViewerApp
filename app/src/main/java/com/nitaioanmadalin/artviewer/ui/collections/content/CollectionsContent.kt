package com.nitaioanmadalin.artviewer.ui.collections.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import coil.compose.rememberImagePainter
import com.nitaioanmadalin.artviewer.R
import com.nitaioanmadalin.artviewer.domain.model.ArtObject
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun CollectionsContent(
    artObjects: LazyPagingItems<ArtObject>,
    onCollectionClicked: (ArtObject) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            count = artObjects.itemCount,
            key = artObjects.itemKey { it.id }
        ) { index: Int ->
            artObjects[index]?.let { artObject ->
                if (index == 0 || artObjects[index - 1]?.principalOrFirstMaker
                    != artObject.principalOrFirstMaker
                ) {
                    AuthorHeader(artObject.principalOrFirstMaker)
                }
                ArtObjectItem(
                    artObject = artObject,
                    onCollectionClicked = onCollectionClicked
                )
            }
        }
        item {
            if (artObjects.loadState.append is LoadState.Loading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun AuthorHeader(author: String) {
    Text(
        text = author,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier
            .padding(top = 16.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
    )
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Color.LightGray)
    )
}

@Composable
fun ArtObjectItem(
    artObject: ArtObject,
    onCollectionClicked: (ArtObject) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onCollectionClicked.invoke(artObject) }
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val url = artObject.headerImage.url
            LoadNetworkImage(url = url)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                    text = artObject.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = artObject.principalOrFirstMaker,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
    }
}

@Composable
fun LoadNetworkImage(url: String?) {
    val painter =
        rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(data = url ?: R.drawable.noimage)
                .apply(block = fun ImageRequest.Builder.() {
                    crossfade(1000)
                    placeholder(R.drawable.loadingplaceholder)
                    error(R.drawable.error_image)
                }
                ).build()
        )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .width(150.dp)
            .height(400.dp)
    )
}

