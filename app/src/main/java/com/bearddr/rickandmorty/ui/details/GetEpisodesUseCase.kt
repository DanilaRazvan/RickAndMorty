package com.bearddr.rickandmorty.ui.details

import com.bearddr.rickandmorty.data.MainRepository
import com.bearddr.rickandmorty.util.Logger
import com.bearddr.rickandmorty.util.NetworkHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.delayFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import java.lang.Exception
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.random.Random

class GetEpisodesUseCase @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val repository: MainRepository
) {

  suspend fun getEpisodes(episodes: List<String>): Flow<GetEpisodesResult> = flow {
    emit(handleGetEpisodesResponse(episodes))
  }
      .onStart {
        emit(GetEpisodesResult.Loading)
        delay(2000)
      }

  private suspend fun handleGetEpisodesResponse(episodes: List<String>): GetEpisodesResult {
    return if (networkHelper.isNetworkConnected()) {
      try {
        val episodesResult =
            when {
              episodes.size > 1 -> {
                repository.getEpisodes(
                    episodes.map {
                      Integer.parseInt(it.substring(it.lastIndexOf("/") + 1))
                    }.joinToString(",")
                )
              }

              episodes.size == 1 -> {
                listOf(repository.getEpisode(
                    episodes.map {
                      Integer.parseInt(it.substring(it.lastIndexOf("/") + 1))
                    }.joinToString(",")
                ))
              }

              else -> {
                emptyList()
              }
            }
        GetEpisodesResult.Success(episodesResult)
      } catch (e: Exception) {
        Logger.logd("UseCase", e.message.toString())
        GetEpisodesResult.Failure(e.message.toString())
      }
    } else {
      GetEpisodesResult.ErrorNoInternet()
    }
  }
}