package com.bearddr.rickandmorty.ui.main


sealed class UIEvents {
  object ClearFavorites: UIEvents()

  object GetCharactersEvent : UIEvents()

  data class SetCharacterIsFavorite(
      val characterId: Int,
      val isFavorite: Boolean
  ) : UIEvents()
}