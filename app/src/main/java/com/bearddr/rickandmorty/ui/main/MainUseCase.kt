package com.bearddr.rickandmorty.ui.main

import com.bearddr.rickandmorty.data.MainRepository
import com.bearddr.rickandmorty.util.NetworkHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import java.lang.Exception
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.random.Random

class MainUseCase @Inject constructor(
    private val repository: MainRepository,
    private val networkHelper: NetworkHelper
) {
  suspend fun getCharacters(): Flow<MainResult> =
      flow { emit(handleGetCharactersResponse()) }
          .onStart { emit(MainResult.Loading) }

  private suspend fun handleGetCharactersResponse(): MainResult {
    if (networkHelper.isNetworkConnected()) {
      try {
        val characters = repository.getCharacters("1," + List(9) { Random.nextInt(2, 671) }.joinToString(","))
        return MainResult.Success(characters)
      } catch (e: Exception) {
        if (e !is HttpException && e !is SocketTimeoutException)
          return MainResult.Failure(e.message.toString())

        return when (e) {
          is HttpException -> {
            when (e.code()) {
              500 -> MainResult.Error500()
              else -> MainResult.Failure(e.message(), e.code())
            }
          }

          else -> MainResult.ErrorTimeOut()
        }
      }
    } else {
      return MainResult.ErrorNoInternet()
    }
  }
}