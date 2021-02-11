package com.bearddr.rickandmorty.ui.main

import com.bearddr.rickandmorty.data.model.Character
import com.bearddr.rickandmorty.util.Constants

sealed class MainResult {

  data class Success(
      val characters: List<Character>
  ) : MainResult()

  object Loading: MainResult()

  data class Failure(
      val message: String,
      val code: Int = -1
  ): MainResult()

  data class Error500(
      val message: String = Constants.ERROR_500
  ) : MainResult()

  data class ErrorTimeOut(
      val message: String = Constants.ERROR_TIME_OUT
  ) : MainResult()

  data class ErrorNoInternet(
      val message: String = Constants.ERROR_NO_INTERNET
  ) : MainResult()
}
