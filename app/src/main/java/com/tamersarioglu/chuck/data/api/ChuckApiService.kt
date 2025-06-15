package com.tamersarioglu.chuck.data.api

import com.tamersarioglu.chuck.data.model.Joke
import com.tamersarioglu.chuck.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ChuckApiService {

    @GET("jokes/random")
    suspend fun getRandomJoke(): Joke

    @GET("jokes/categories")
    suspend fun getCategories(): List<String>

    @GET("jokes/random")
    suspend fun getRandomJokeByCategory(
        @Query("category") category: String
    ): Joke

    @GET("jokes/search")
    suspend fun searchJokes(
        @Query("query") query: String,
    ): SearchResponse
}