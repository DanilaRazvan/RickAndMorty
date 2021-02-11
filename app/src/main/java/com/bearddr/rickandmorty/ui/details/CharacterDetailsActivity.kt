package com.bearddr.rickandmorty.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bearddr.rickandmorty.databinding.ActivityCharacterDetailsBinding
import com.bearddr.rickandmorty.ui.details.recyclerview.EpisodeAdapter
import com.bearddr.rickandmorty.util.extention.loadFromUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CharacterDetailsActivity : AppCompatActivity() {

  private lateinit var binding: ActivityCharacterDetailsBinding
  private lateinit var episodeAdapter: EpisodeAdapter

  private val viewModel: CharacterDetailsViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityCharacterDetailsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.topAppBar)

    setView()
    setListeners()
    setObservers()
  }

  private fun setView() {
    episodeAdapter = EpisodeAdapter()
    binding.episodesRecyclerView.apply {
      adapter = episodeAdapter
      layoutManager = LinearLayoutManager(this@CharacterDetailsActivity, LinearLayoutManager.VERTICAL, false)
      addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
    }
  }

  private fun setListeners() {
    binding.topAppBar.setNavigationOnClickListener {
      onBackPressed()
      finish()
    }
  }

  private fun setObservers() {
    viewModel.state.observe(this, { state ->
      when(state.action) {
        UiAction.Idle -> {
          showProgressBar(false)
        }

        UiAction.ShowCharacter -> {
          showProgressBar(false)
          binding.ivCharacterImage.loadFromUrl(state.character!!.imageUrl)
          title = state.character.name
          viewModel.getEpisodes()
        }

        UiAction.Loading -> {
          showProgressBar(true)
        }

        UiAction.EpisodesFetched -> {
          showProgressBar(false)
          episodeAdapter.setDataSource(state.episodes)
        }

        UiAction.ShowMessage -> {
          showProgressBar(false)
        }
      }
    })
  }

  private fun showProgressBar(cond: Boolean) {
    binding.progressBar.visibility = if (cond) View.VISIBLE else View.GONE
    binding.episodesRecyclerView.visibility = if (cond) View.GONE else View.VISIBLE
  }
}