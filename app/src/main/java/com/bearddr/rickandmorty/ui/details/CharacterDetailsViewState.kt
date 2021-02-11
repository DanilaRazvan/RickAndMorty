package com.bearddr.rickandmorty.ui.details

import com.bearddr.rickandmorty.data.model.Character
import com.bearddr.rickandmorty.data.model.Episode

data class CharacterDetailsViewState(
    val character: Character? = null,
    val episodes: List<Episode> = emptyList(),
    val action: UiAction = UiAction.Idle,
    val message: String = ""
)

sealed class UiAction {
    object Idle: UiAction()

    object ShowCharacter: UiAction()

    object Loading: UiAction()

    object EpisodesFetched: UiAction()

    object ShowMessage: UiAction()
}