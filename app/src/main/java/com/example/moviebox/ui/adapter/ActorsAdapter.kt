package com.example.moviebox.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.moviebox.R
import com.example.moviebox.databinding.ItemActorBinding
import com.example.moviebox.model.Cast
import com.example.moviebox.util.constant.DurationConstants.CROSSFADE_DURATION
import com.example.moviebox.util.constant.NetworkConstants.IMAGE_BASE_URL

class ActorsAdapter(
    val onActorClick: (Cast) -> Unit = {},
) : BaseAdapter<Cast, ActorsAdapter.ActorsViewHolder>(ActorsDiffCallback(), onActorClick) {
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int,
    ): ActorsViewHolder {
        val binding = ItemActorBinding.inflate(inflater, parent, false)
        return ActorsViewHolder(binding)
    }

    override fun bind(
        holder: ActorsViewHolder,
        item: Cast,
    ) {
        holder.bind(item)
    }

    inner class ActorsViewHolder(
        private val binding: ItemActorBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(actor: Cast) {
            binding.ivActor.load(IMAGE_BASE_URL + actor.profile_path) {
                crossfade(CROSSFADE_DURATION)
                placeholder(R.drawable.ic_generic_movie_poster)
                transformations(CircleCropTransformation())
                error(R.drawable.ic_generic_movie_poster)
            }
            binding.tvActorName.text = actor.name
            binding.root.setOnClickListener {
                onActorClick(actor)
            }
        }
    }
}

class ActorsDiffCallback : DiffUtil.ItemCallback<Cast>() {
    override fun areItemsTheSame(
        oldItem: Cast,
        newItem: Cast,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Cast,
        newItem: Cast,
    ) = oldItem == newItem
}
