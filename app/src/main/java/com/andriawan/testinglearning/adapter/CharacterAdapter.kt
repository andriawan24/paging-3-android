package com.andriawan.testinglearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.testinglearning.R
import com.andriawan.testinglearning.data.models.CharacterDTO
import com.andriawan.testinglearning.databinding.CharacterItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions

class CharacterAdapter :
    PagingDataAdapter<CharacterDTO, CharacterAdapter.ViewHolder>(CharacterComparator) {

    class ViewHolder(
        private val binding: CharacterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: CharacterDTO?) {
            character?.let {
                binding.characterName.text = it.name
                Glide.with(binding.characterImage.context)
                    .load(it.imageUrl)
                    .transition(withCrossFade())
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.characterImage)
            }
        }

        companion object {
            fun newInstance(parent: ViewGroup): ViewHolder {
                val binding = CharacterItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    object CharacterComparator : DiffUtil.ItemCallback<CharacterDTO>() {
        override fun areItemsTheSame(oldItem: CharacterDTO, newItem: CharacterDTO): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: CharacterDTO, newItem: CharacterDTO): Boolean {
            return oldItem == newItem
        }
    }
}