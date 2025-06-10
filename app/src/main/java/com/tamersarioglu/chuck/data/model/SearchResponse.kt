package com.tamersarioglu.chuck.data.model


import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("result")
    val result: List<Joke>,
    @SerializedName("total")
    val total: Int
)