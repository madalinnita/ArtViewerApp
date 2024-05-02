package com.nitaioanmadalin.artviewer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nitaioanmadalin.artviewer.core.utils.Constants

@Entity(tableName = Constants.MUSEUM_REMOTE_KEYS_TABLE)
data class MuseumRemoteKeysEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)