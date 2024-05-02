package com.nitaioanmadalin.artviewer.data.local.entity

import android.os.Parcelable
import com.nitaioanmadalin.artviewer.domain.model.HeaderImage
import kotlinx.parcelize.Parcelize

@Parcelize
data class HeaderImageEntity(
    val width: Long,
    val height: Long,
    val url: String?
): Parcelable {
    fun toHeaderImage(): HeaderImage {
        return HeaderImage(
            width = width,
            height = height,
            url = url
        )
    }
}