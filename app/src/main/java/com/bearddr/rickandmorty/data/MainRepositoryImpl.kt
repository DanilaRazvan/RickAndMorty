package com.bearddr.rickandmorty.data

import com.bearddr.rickandmorty.data.model.Character
import com.bearddr.rickandmorty.data.model.Episode
import com.bearddr.rickandmorty.data.remote.RickMortyApi
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: RickMortyApi
) : MainRepository {
  override suspend fun getCharacters(ids: String): List<Character> = api.getCharacters(ids)

  override suspend fun getEpisode(id: String): Episode  = api.getEpisode(id)

  override suspend fun getEpisodes(ids: String): List<Episode> = api.getEpisodes(ids)
}