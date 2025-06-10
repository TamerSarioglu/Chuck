package com.tamersarioglu.chuck.domain.repository

import com.tamersarioglu.chuck.domain.model.JokeUI

interface ChuckRepository {
    suspend fun getRandomJoke(): Result<JokeUI>
    suspend fun getCategories(): Result<List<String>>
    suspend fun getRandomJokeByCategory(category: String): Result<JokeUI>
    suspend fun searchJokes(query: String): Result<List<JokeUI>>
}