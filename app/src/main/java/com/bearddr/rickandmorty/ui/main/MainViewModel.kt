package com.bearddr.rickandmorty.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.bearddr.rickandmorty.data.model.Character
import com.bearddr.rickandmorty.util.Logger
import com.bearddr.rickandmorty.util.ReduxViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val useCase: MainUseCase
) : ReduxViewModel<MainState>(MainState()) {

  private val events = ConflatedBroadcastChannel<UIEvents>()

  init {
    handleGetCharactersEvent()
    handleSetCharacterIsFavorite()
  }

  private fun handleGetCharactersEvent() {
    events.asFlow()
        .filterIsInstance<UIEvents.GetCharactersEvent>()
        .flatMapLatest {
          useCase.getCharacters()
        }
        .onEach {
          when (it) {
            is MainResult.Success -> {
              setState {
                copy(
                    characters = it.characters,
                    action = UiAction.CharactersFetched
                )
              }
            }

            MainResult.Loading -> {
              setState {
                copy(
                    action = UiAction.Loading
                )
              }
            }

            is MainResult.Failure -> {
              setState {
                copy(
                    message = it.message,
                    action = UiAction.ShowMessage
                )
              }
            }

            is MainResult.Error500 -> {
              setState {
                copy(
                    message = it.message,
                    action = UiAction.ShowMessage
                )
              }
            }

            is MainResult.ErrorTimeOut -> {
              setState {
                copy(
                    message = it.message,
                    action = UiAction.ShowMessage
                )
              }
            }

            is MainResult.ErrorNoInternet -> {
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

  private fun handleSetCharacterIsFavorite() {
    events.asFlow()
        .filterIsInstance<UIEvents.SetCharacterIsFavorite>()
        .onEach {
          setState {
            copy(
                characters = characters.apply {
                  find { character ->
                    character.id == it.characterId
                  }?.isFavorite = it.isFavorite
                }
            )
          }
        }
        .flowOn(Dispatchers.IO)
        .launchIn(viewModelScope)
  }

  fun getCharacters() {
    events.offer(UIEvents.GetCharactersEvent)
  }

  fun setCharacterIsFavorite(characterId: Int, isFavorite: Boolean) {
    events.offer(UIEvents.SetCharacterIsFavorite(characterId, isFavorite))
  }

  fun changeListType() {
    viewModelScope.launchSetState {
      copy(
          favorites = !favorites,
          action = UiAction.CharactersFetched
      )
    }
  }
}