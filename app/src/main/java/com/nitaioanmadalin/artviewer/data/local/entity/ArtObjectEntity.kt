package com.nitaioanmadalin.artviewer.data.local.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nitaioanmadalin.artviewer.core.utils.Constants
import com.nitaioanmadalin.artviewer.domain.model.ArtObject
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Constants.ART_OBJECTS_TABLE)
data class ArtObjectEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val objectNumber: String,
    val title: String,
    val principalOrFirstMaker: String,
    val longTitle: String,
    @Embedded val headerImage: HeaderImageEntity,
    val productionPlaces: List<String>
): Parcelable {
    fun toArtObject(): ArtObject {
        return ArtObject(
            id = id,
            objectNumber = objectNumber,
            title = title,
            principalOrFirstMaker = principalOrFirstMaker,
            longTitle = longTitle,
            headerImage = headerImage.toHeaderImage(),
            productionPlaces = productionPlaces
        )
    }
}