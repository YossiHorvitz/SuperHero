package com.user.superhero.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.user.superhero.data.Hero
import com.user.superhero.databinding.ListItemBinding
import com.user.superhero.utils.extensions.load

class SuperHeroAdapter(
    private val listener: ((view: View, hero: Hero?) -> Unit)? = null
) : ListAdapter<Hero, SuperHeroAdapter.HeroViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class HeroViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hero: Hero) {
            binding.apply {
                heroNameTextView.text = hero.name
                labelImageView.isVisible = hero.isSuggestion
                with(imageView) {
                    load(hero.image.url) {
                        listener?.let { it(this, hero) }
                    }
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Hero>() {
        override fun areItemsTheSame(oldItem: Hero, newItem: Hero): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hero, newItem: Hero): Boolean {
            return oldItem == newItem
        }
    }
}