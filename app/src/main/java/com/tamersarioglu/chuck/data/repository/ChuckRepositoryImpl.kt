package com.tamersarioglu.chuck.data.repository

import com.tamersarioglu.chuck.data.api.ChuckApiService
import com.tamersarioglu.chuck.data.mapper.JokeMapper
import com.tamersarioglu.chuck.domain.model.JokeUI
import com.tamersarioglu.chuck.domain.repository.ChuckRepository
import javax.inject.Inject

class ChuckRepositoryImpl @Inject constructor(
    private val jokeMapper: JokeMapper,
    private val chuckApi: ChuckApiService
) : ChuckRepository {
    /**
     * Fetches a random joke from the Chuck Norris API and maps it to the domain model.
     *
     * @return A [Result] containing a [JokeUI] instance or an error.
     */
    override suspend fun getRandomJoke(): Result<JokeUI> {
        return try {
            val joke = chuckApi.getRandomJoke()
            val jokeUI = jokeMapper.mapToUI(joke)
            Result.success(jokeUI)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCategories(): Result<List<String>> {
        return try {
            val categories = chuckApi.getCategories()
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRandomJokeByCategory(category: String): Result<JokeUI> {
        return try {
            val joke = chuckApi.getRandomJokeByCategory(category)
            val jokeUI = jokeMapper.mapToUI(joke)
            Result.success(jokeUI)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchJokes(query: String): Result<List<JokeUI>> {
        return try {
            val searchResponse = chuckApi.searchJokes(query)
            val jokesUI = jokeMapper.mapToUI(searchResponse.result)
            Result.success(jokesUI)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}