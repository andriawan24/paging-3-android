package com.andriawan.testinglearning.data.repository

import com.andriawan.testinglearning.data.models.CharacterDTO

interface CharacterRepository {
    @Throws(Exception::class)
    suspend fun getListCharacters(page: Int): List<CharacterDTO>
    @Throws(Exception::class)
    suspend fun getCharacter(id: Int): CharacterDTO
}