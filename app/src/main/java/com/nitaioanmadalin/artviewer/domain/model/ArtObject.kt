package com.nitaioanmadalin.artviewer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtObject(
    val id: String,
    val objectNumber: String,
    val title: String,
    val principalOrFirstMaker: String,
    val longTitle: String,
    val headerImage: HeaderImage,
    val productionPlaces: List<String>
): Parcelable