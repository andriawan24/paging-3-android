package com.andriawan.testinglearning.data.remote.response

data class DisneyCharactersResponse(
    val count: Int,
    val `data`: List<Character>,
    val nextPage: String? = null,
    val previousPage: String? = null,
    val totalPages: Int
)