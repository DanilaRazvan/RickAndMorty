package com.bearddr.rickandmorty.data.remote

import com.bearddr.rickandmorty.data.model.Character
import com.bearddr.rickandmorty.data.model.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RickMortyApi {

  @GET("character/{ids}")
  suspend fun getCharacters(@Path(value = "ids") ids: String): List<Character>

  @GET("episode/{ids}")
  suspend fun getEpisodes(@Path(value = "ids") ids: String): List<Episode>

  @GET("episode/{id}")
  suspend fun getEpisode(@Path(value = "id") ids: String): Episode
}