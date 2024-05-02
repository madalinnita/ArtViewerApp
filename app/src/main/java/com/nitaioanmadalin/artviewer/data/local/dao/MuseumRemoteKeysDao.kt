package com.nitaioanmadalin.artviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.nitaioanmadalin.artviewer.core.utils.Constants
import com.nitaioanmadalin.artviewer.data.local.entity.MuseumRemoteKeysEntity

@Dao
interface MuseumRemoteKeysDao {
    @Query("SELECT * FROM museum_remote_keys_table WHERE id =:id")
    suspend fun getRemoteKeys(id: String): MuseumRemoteKeysEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<MuseumRemoteKeysEntity>)

    @Query("DELETE FROM museum_remote_keys_table")
    suspend fun deleteAllRemoteKeys()
}