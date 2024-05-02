package com.nitaioanmadalin.artviewer.data.remote.dto

import android.os.Parcelable
import com.nitaioanmadalin.artviewer.data.local.entity.HeaderImageEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class HeaderImageDto(
    val width: Long,
    val height: Long,
    val url: String?
): Parcelable {
    fun toHeaderImageEntity(): HeaderImageEntity {
        return HeaderImageEntity(
            width = width,
            height = height,
            url = url
        )
    }
}