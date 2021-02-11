package com.bearddr.rickandmorty.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Episode(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "episode")
    val seasonEpisode: String,

    @Json(name = "air_date")
    val airDate: String,

    var isExpanded: Boolean = false
)