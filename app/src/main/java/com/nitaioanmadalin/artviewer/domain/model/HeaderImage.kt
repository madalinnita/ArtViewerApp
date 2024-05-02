package com.nitaioanmadalin.artviewer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HeaderImage(
    val width: Long,
    val height: Long,
    val url: String?
): Parcelable