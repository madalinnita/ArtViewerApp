package com.nitaioanmadalin.artviewer.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nitaioanmadalin.artviewer.data.local.entity.ArtObjectEntity

@Dao
interface MuseumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCollections(collections: List<ArtObjectEntity>)

    @Query("SELECT * FROM art_objects_table")
    fun getAllCollections(): PagingSource<Int, ArtObjectEntity>

    @Query("DELETE FROM art_objects_table")
    suspend fun clearAll()
}