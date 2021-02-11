package com.bearddr.rickandmorty.data

import com.bearddr.rickandmorty.data.model.Character
import com.bearddr.rickandmorty.data.model.Episode
import retrofit2.Response

interface MainRepository {
  suspend fun getCharacters(ids: String): List<Character>

  suspend fun getEpisode(id: String): Episode

  suspend fun getEpisodes(ids: String): List<Episode>
}