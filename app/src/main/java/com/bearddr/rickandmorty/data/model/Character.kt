package com.bearddr.rickandmorty.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Character(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "status")
    val status: String,

    @Json(name = "species")
    val species: String,

    @Json(name = "type")
    val type: String,

    @Json(name = "gender")
    val gender: String,

    @Json(name = "image")
    val imageUrl: String,

    @Json(name = "episode")
    val episodes: List<String>,

    var isFavorite: Boolean = false
): Parcelable