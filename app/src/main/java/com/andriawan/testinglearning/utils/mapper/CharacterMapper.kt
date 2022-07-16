package com.andriawan.testinglearning.utils.mapper

import com.andriawan.testinglearning.data.models.CharacterDTO
import com.andriawan.testinglearning.data.remote.response.Character

class CharacterMapper: Mapper<List<CharacterDTO>, List<Character>> {
    override fun mapIncoming(response: List<Character>): List<CharacterDTO> {
        val characterList = response.map {
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

        return characterList
    }

    override fun mapOutgoing(data: List<CharacterDTO>): List<Character> {
        val characterList = data.map {
            Character(
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

        return characterList
    }
}