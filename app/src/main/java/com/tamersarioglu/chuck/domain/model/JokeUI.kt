package com.tamersarioglu.chuck.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class JokeUI(
    val categories: List<String>,
    val createdAt: String,
    val iconUrl: String,
    val id: String,
    val updatedAt: String,
    val url: String,
    val value: String
)
