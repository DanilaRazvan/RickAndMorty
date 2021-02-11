package com.bearddr.rickandmorty.ui.details.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bearddr.rickandmorty.data.model.Episode
import com.bearddr.rickandmorty.databinding.EpisodeListItemBinding

class EpisodeAdapter: RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    private val episodes = mutableListOf<Episode>()

    fun setDataSource(newEpisodes: List<Episode>) {
        this.episodes.clear()
        this.episodes.addAll(newEpisodes)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val itemBinding = EpisodeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = episodes[position]

        holder.binding.root.setOnClickListener {
            episode.isExpanded = !episode.isExpanded
            notifyItemChanged(position)
        }

        holder.binding.tvEpisodeId.text = episode.id.toString()
        holder.binding.tvEpisodeName.text = episode.name
        holder.binding.tvAirDate.text = episode.airDate
        holder.binding.tvSeasonEpisode.text = episode.seasonEpisode
        holder.binding.detailsView.visibility = if (episode.isExpanded) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = episodes.size

    inner class EpisodeViewHolder(val binding: EpisodeListItemBinding): RecyclerView.ViewHolder(binding.root)
}