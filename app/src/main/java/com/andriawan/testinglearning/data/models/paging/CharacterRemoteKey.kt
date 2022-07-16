package com.andriawan.testinglearning.data.models.paging

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andriawan.testinglearning.utils.Constants.CHARACTER_REMOTE_KEY_TABLE

@Entity(tableName = CHARACTER_REMOTE_KEY_TABLE)
data class CharacterRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val prevPage: Int?,
    val nextPage: Int?
)