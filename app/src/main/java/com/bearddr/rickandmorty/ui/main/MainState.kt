package com.bearddr.rickandmorty.ui.main

import com.bearddr.rickandmorty.data.model.Character

data class MainState(
    val characters: List<Character> = emptyList(),
    val favorites: Boolean = false,
    val message: String = "",
    val action: UiAction = UiAction.Idle
)

sealed class UiAction {

    object Idle: UiAction()

    object Loading: UiAction()

    object CharactersFetched: UiAction()

    object ShowMessage: UiAction()
}