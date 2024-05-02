package com.nitaioanmadalin.artviewer.data.remote.dto

import android.os.Parcelable
import com.nitaioanmadalin.artviewer.data.local.entity.ArtObjectEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtObjectDto(
    val id: String,
    val objectNumber: String,
    val title: String,
    val principalOrFirstMaker: String,
    val longTitle: String,
    val headerImage: HeaderImageDto,
    val productionPlaces: List<String>
): Parcelable {
    fun toArtObjectEntity(): ArtObjectEntity {
        return ArtObjectEntity(
            id = id,
            objectNumber = objectNumber,
            title = title,
            principalOrFirstMaker = principalOrFirstMaker,
            longTitle = longTitle,
            headerImage = headerImage.toHeaderImageEntity(),
            productionPlaces = productionPlaces
        )
    }
}