package com.andriawan.testinglearning.data.repository

import com.andriawan.testinglearning.data.models.CharacterDTO
import com.andriawan.testinglearning.data.remote.ApiService
import com.andriawan.testinglearning.utils.mapper.CharacterMapper

class CharacterRepositoryImpl(
    private val apiService: ApiService
): CharacterRepository {

    override suspend fun getListCharacters(page: Int): List<CharacterDTO> {
        val characterMapper = CharacterMapper()

        try {
            val response = apiService.getCharacters(page)
            if (response.isSuccessful) {
                val listCharacter = response.body()?.data ?: emptyList()
                return characterMapper.mapIncoming(listCharacter)
            } else {
                throw Exception(response.message())
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    override suspend fun getCharacter(id: Int): CharacterDTO {
        try {
            val response = apiService.getCharacter(id)

            if (response.isSuccessful) {
                response.body()?.let {
                    val character = it.let {
                        CharacterDTO(
                            _id = it._id,
                            allies = it.allies,
                            enemies = it.enemies,
                            films = it.films,
                            imageUrl = it.imageUrl,
                            name = it.name,
                            parkAttractions = it.parkAttractions,
                            shortFilms = it.shortFilms,
                            tvShows = it.tvShows,
                            url = it.url,
                            videoGames = it.videoGames
                        )
                    }

                    return character
                }

                throw Exception("Unknown Error")
            } else {
                throw Exception(response.message())
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}