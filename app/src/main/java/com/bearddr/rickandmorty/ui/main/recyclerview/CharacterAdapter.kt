package com.bearddr.rickandmorty.ui.main.recyclerview

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bearddr.rickandmorty.R
import com.bearddr.rickandmorty.data.model.Character
import com.bearddr.rickandmorty.databinding.CharacterListItemBinding
import com.bearddr.rickandmorty.util.extention.loadFromUrl

class CharacterAdapter(
    private val context: Context,
    private val startDetailsActivity: (character: Character) -> Unit,
    private val setIsFavorite: (characterId: Int, isFavorite: Boolean) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

  private val characters = mutableListOf<Character>()

  fun setDataSource(newCharacters: List<Character>) {
    this.characters.clear()
    this.characters.addAll(newCharacters)
    this.notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
    val itemBinding = CharacterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return CharacterViewHolder(itemBinding)
  }

  override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
    val character = characters[position]

    holder.binding.root.setOnClickListener {
      startDetailsActivity(character)
    }

    holder.binding.root.setOnLongClickListener {
      val popupMenu = PopupMenu(it.context, it)
      popupMenu.inflate(R.menu.contextual_menu)
      popupMenu.menu.getItem(0).title = if (character.isFavorite) context.getString(R.string.remove_from_favorites) else context.getString(R.string.add_to_favorites)

      popupMenu.setOnMenuItemClickListener {
        when (it.itemId) {
          R.id.favoriteLabel -> {
            setIsFavorite(character.id, !character.isFavorite)
            notifyItemChanged(position)
            true
          }
          else -> false
        }
      }
      popupMenu.show()
      true
    }

    holder.binding.tvCharacterId.backgroundTintList = ColorStateList.valueOf(
        if (character.isFavorite) context.getColor(R.color.favorite)
        else context.getColor(R.color.regular)
    )

    holder.binding.ivCharacterImage.loadFromUrl(character.imageUrl)
    holder.binding.tvCharacterGender.text = character.gender
    holder.binding.tvCharacterId.text = character.id.toString()
    holder.binding.tvCharacterName.text = character.name
    holder.binding.tvCharacterSpecies.text = character.species
    holder.binding.tvCharacterStatus.text = character.status
    holder.binding.tvCharacterType.text = character.type
  }

  override fun getItemCount(): Int = characters.size


  fun resetFavorites() {
    characters.onEach {
      it.isFavorite = false
    }
    notifyDataSetChanged()
  }


  inner class CharacterViewHolder(val binding: CharacterListItemBinding) : RecyclerView.ViewHolder(binding.root)
}