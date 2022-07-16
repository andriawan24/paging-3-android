package com.andriawan.testinglearning.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andriawan.testinglearning.utils.Constants.CHARACTER_TABLE

@Entity(tableName = CHARACTER_TABLE)
data class CharacterDTO(
    @PrimaryKey
    val _id: Int,
    val allies: List<String>,
    val enemies: List<String>,
    val films: List<String>,
    val imageUrl: String?,
    val name: String,
    val parkAttractions: List<String>,
    val shortFilms: List<String>,
    val tvShows: List<String>,
    val url: String,
    val videoGames: List<String>
)