package com.bearddr.rickandmorty.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearddr.rickandmorty.R
import com.bearddr.rickandmorty.data.model.Character
import com.bearddr.rickandmorty.databinding.ActivityMainBinding
import com.bearddr.rickandmorty.ui.details.CharacterDetailsActivity
import com.bearddr.rickandmorty.ui.main.recyclerview.CharacterAdapter
import com.bearddr.rickandmorty.util.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private val viewModel: MainViewModel by viewModels()

  private lateinit var characterAdapter: CharacterAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.topAppBar)

    setView()
    setListeners()
    setObservers()
    viewModel.getCharacters()
  }

  private fun setView() {
    characterAdapter = CharacterAdapter(this, {
      startDetailsActivity(it)
    }, { characterId, isFavorite ->
      viewModel.setCharacterIsFavorite(characterId, isFavorite)
    })

    binding.charactersRecyclerview.apply {
      adapter = characterAdapter
      layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
    }

    binding.charactersLayout.isRefreshing = false
  }

  private fun setListeners() {
    binding.charactersLayout.setOnRefreshListener {
      viewModel.getCharacters()
    }
  }

  private fun setObservers() {
    viewModel.state.observe(this, { state ->
      when (state.action) {
        UiAction.Idle -> {
          showProgressBar(false)
          binding.charactersLayout.isRefreshing = false
        }

        UiAction.Loading -> {
          //show progress bar
          showProgressBar(true)
          binding.charactersLayout.isRefreshing = false
          Logger.logd("Activity", "loading")
        }

        UiAction.CharactersFetched -> {
          Logger.logd("Activity", "Characters fetched : ${state.characters}")
          // update recyclerview items
          showProgressBar(false)
          binding.charactersLayout.isRefreshing = false
          binding.topAppBar.menu.getItem(0).title = if (state.favorites) getString(R.string.complete_list) else getString(R.string.favorites_list)

          characterAdapter.setDataSource(
              if (state.favorites){
                state.characters.filter { character ->
                  character.isFavorite
                }
              } else {
                state.characters
              }
          )
          characterAdapter.notifyDataSetChanged()
        }

        UiAction.ShowMessage -> {
          showProgressBar(false)
          binding.charactersLayout.isRefreshing = false
          Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
        }
      }
    })
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.options_menu, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.listFilter -> {
        viewModel.changeListType()
      }

      R.id.resetFavoritesList -> {
        characterAdapter.resetFavorites()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun showProgressBar(cond: Boolean) {
    binding.progressBar.visibility = if (cond) View.VISIBLE else View.GONE
    binding.charactersLayout.visibility = if (cond) View.GONE else View.VISIBLE
  }

  private fun startDetailsActivity(character: Character) {
    startActivity(Intent(this, CharacterDetailsActivity::class.java).apply {
      putExtra("character", character)
    })
  }
}