package com.andriawan.testinglearning.data.remote

import com.andriawan.testinglearning.data.remote.response.Character
import com.andriawan.testinglearning.data.remote.response.DisneyCharactersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/characters")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<DisneyCharactersResponse>

    @GET("/characters/:id")
    suspend fun getCharacter(
        @Path("id") id: Int
    ): Response<Character>
}