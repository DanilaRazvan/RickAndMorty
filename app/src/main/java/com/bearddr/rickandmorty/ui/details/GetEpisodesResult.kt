package com.bearddr.rickandmorty.ui.details

import com.bearddr.rickandmorty.data.model.Episode
import com.bearddr.rickandmorty.ui.main.MainResult
import com.bearddr.rickandmorty.util.Constants

sealed class GetEpisodesResult {

  data class Success(
      val episodes: List<Episode>
  ) : GetEpisodesResult()

  object Loading: GetEpisodesResult()

  data class Failure(
      val message: String,
      val code: Int = -1
  ): GetEpisodesResult()

  data class ErrorNoInternet(
      val message: String = Constants.ERROR_NO_INTERNET
  ) : GetEpisodesResult()
}