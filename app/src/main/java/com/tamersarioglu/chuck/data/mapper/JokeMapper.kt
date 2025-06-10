package com.tamersarioglu.chuck.data.mapper

import com.tamersarioglu.chuck.data.model.Joke
import com.tamersarioglu.chuck.domain.model.JokeUI
import javax.inject.Inject

class JokeMapper @Inject constructor() {
    /**
     * Maps a [Joke] data model to a [JokeUI] domain model.
     *
     * @param joke The [Joke] instance to be mapped.
     * @return A [JokeUI] instance containing the same data as the input [Joke].
     */
    fun mapToUI(joke: Joke): JokeUI{
        return JokeUI(
            categories = joke.categories,
            createdAt = joke.createdAt,
            iconUrl = joke.iconUrl,
            id = joke.id,
            updatedAt = joke.updatedAt,
            url = joke.url,
            value = joke.value
        )
    }

    /**
     * Maps a list of [Joke] data models to a list of [JokeUI] domain models.
     *
     * @param jokes The list of [Joke] instances to be mapped.
     * @return A list of [JokeUI] instances containing the same data as the input [Joke] list.
     */
    fun mapToUI(jokes: List<Joke>): List<JokeUI> {
        return jokes.map { mapToUI(it) }
    }

}