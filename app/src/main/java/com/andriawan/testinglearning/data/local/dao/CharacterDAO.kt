package com.andriawan.testinglearning.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andriawan.testinglearning.data.models.CharacterDTO

@Dao
interface CharacterDAO {

    @Query("SELECT * FROM character")
    fun getCharacters(): PagingSource<Int, CharacterDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterDTO>)

    @Query("DELETE FROM character")
    suspend fun deleteAllCharacters()
}
