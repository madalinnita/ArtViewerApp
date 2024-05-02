package com.nitaioanmadalin.artviewer.data.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nitaioanmadalin.artviewer.data.local.converters.ListConverters
import com.nitaioanmadalin.artviewer.data.local.entity.ArtObjectEntity
import com.nitaioanmadalin.artviewer.data.local.entity.MuseumRemoteKeysEntity

@Database(
    entities = [ArtObjectEntity::class, MuseumRemoteKeysEntity::class],
    version = 1
)
@TypeConverters(ListConverters::class)
abstract class MuseumDatabase: RoomDatabase() {
    abstract val museumDao: MuseumDao
    abstract val museumRemoteKeysDao: MuseumRemoteKeysDao
}