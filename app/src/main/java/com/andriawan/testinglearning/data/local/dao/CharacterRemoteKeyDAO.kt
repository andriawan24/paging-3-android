package com.andriawan.testinglearning.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andriawan.testinglearning.data.models.paging.CharacterRemoteKey

@Dao
interface CharacterRemoteKeyDAO {

    @Query("SELECT * FROM character_remote_key WHERE id = :id")
    suspend fun getRemoteKey(id: Int): CharacterRemoteKey

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKeys(remoteKeys: List<CharacterRemoteKey>)

    @Query("DELETE FROM character_remote_key")
    suspend fun deleteAllRemoteKeys()
}