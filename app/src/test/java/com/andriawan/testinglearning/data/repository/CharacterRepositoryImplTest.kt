package com.andriawan.testinglearning.data.repository

import com.andriawan.testinglearning.data.models.CharacterDTO
import com.andriawan.testinglearning.data.remote.ApiService
import com.andriawan.testinglearning.utils.Constants.BASE_URL
import com.andriawan.testinglearning.utils.Constants.DEFAULT_TIMEOUT
import com.andriawan.testinglearning.utils.interceptor.NetworkConnectionInterceptor
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CharacterRepositoryImplTest {

    private lateinit var characterRepository: CharacterRepository

    @Before
    fun setup() {
        val client = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .build()

        val apiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        characterRepository = CharacterRepositoryImpl(apiService)
    }

    @Test
    fun `Fetch data from API should return list character`() = runBlocking {
        val listCharacter = characterRepository.getListCharacters(1)

        assert(listCharacter.isNotEmpty())
    }

    @Test
    fun `Page out of index should return empty list`() = runBlocking {
        val listCharacter = characterRepository.getListCharacters(10000)

        assert(listCharacter.isEmpty())
    }

    @Test
    fun `First data should equals dummy data`() = runBlocking {
        val character = characterRepository.getListCharacters(1)[0]
        val charToCompare = CharacterDTO(
            _id = 6,
            name = "'Olu Mel",
            imageUrl = "https://static.wikia.nocookie.net/disney/images/6/61/Olu_main.png",
            url = "https://api.disneyapi.dev/characters/6",
            enemies = emptyList(),
            films = emptyList(),
            videoGames = emptyList(),
            tvShows = emptyList(),
            shortFilms = emptyList(),
            parkAttractions = emptyList(),
            allies = emptyList()
        )

        assert(character == charToCompare)
    }
}