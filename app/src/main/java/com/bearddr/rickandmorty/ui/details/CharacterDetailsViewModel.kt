package com.bearddr.rickandmorty.ui.details

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bearddr.rickandmorty.ui.main.UIEvents
import com.bearddr.rickandmorty.util.Logger
import com.bearddr.rickandmorty.util.ReduxViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class CharacterDetailsViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val getEpisodesUseCase: GetEpisodesUseCase
) : ReduxViewModel<CharacterDetailsViewState>(CharacterDetailsViewState()) {

  private val events = ConflatedBroadcastChannel<UiEvents>()

  init {
    viewModelScope.launchSetState {
      copy(
          character = savedStateHandle.get("character"),
          action = UiAction.ShowCharacter
      )
    }

    handleGetEpisodesEvent()
  }

  private fun handleGetEpisodesEvent() {
    events.asFlow()
        .filterIsInstance<UiEvents.GetEpisodesEvent>()
        .flatMapLatest {
          getEpisodesUseCase.getEpisodes(currentState().character!!.episodes)
        }
        .onEach {
          when(it) {
            is GetEpisodesResult.Success -> {
              Logger.logd("ViewModel", "success: ${it.episodes}")
              setState {
                copy(
                    episodes = it.episodes,
                    action = UiAction.EpisodesFetched
                )
              }
            }

            GetEpisodesResult.Loading -> {
              Logger.logd("ViewModel", "loading")
              setState {
                copy(
                    action = UiAction.Loading
                )
              }
            }

            is GetEpisodesResult.Failure -> {
              Logger.logd("ViewModel", "failure")
              setState {
                copy(
                    message = it.message,
                    action = UiAction.ShowMessage
                )
              }
            }

            is GetEpisodesResult.ErrorNoInternet -> {
              setState {
                copy(
                    message = it.message,
                    action = UiAction.ShowMessage
                )
              }
            }
          }
        }
        .flowOn(Dispatchers.IO)
        .launchIn(viewModelScope)
  }

  fun getEpisodes() {
    Logger.logd("ViewModel", "get Episodes")
    events.offer(UiEvents.GetEpisodesEvent)
  }
}