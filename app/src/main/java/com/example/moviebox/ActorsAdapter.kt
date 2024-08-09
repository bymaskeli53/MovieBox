package com.example.moviebox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.moviebox.databinding.ItemActorBinding
import com.example.moviebox.model.Cast
import com.example.moviebox.util.DurationConstants.CROSSFADE_DURATION
import com.example.moviebox.util.NetworkConstants.IMAGE_BASE_URL

class ActorsAdapter(
    val onActorClick: (Cast) -> Unit = {},
) : ListAdapter<Cast, ActorsAdapter.ActorsViewHolder>(ActorsDiffCallback()) {
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ActorsViewHolder {
        val binding = ItemActorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActorsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ActorsViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}

class ActorsDiffCallback : DiffUtil.ItemCallback<Cast>() {
    override fun areItemsTheSame(
        oldItem: Cast,
        newItem: Cast,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Cast,
        newItem: Cast,
    ): Boolean = oldItem == newItem
}
