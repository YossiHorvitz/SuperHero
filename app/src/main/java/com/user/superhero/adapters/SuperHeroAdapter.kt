package com.user.superhero.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.user.superhero.R
import com.user.superhero.data.APIResponse
import kotlinx.android.synthetic.main.list_item.view.*

class SuperHeroAdapter(
    var list: List<APIResponse.Results>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SuperHeroAdapter.ExampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ExampleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        holder.apply {
            val hero = list[position]

            heroName.text = hero.name
            labelImageView.visibility = if (position == 0) View.VISIBLE else View.GONE
            imageView.loadImage(hero.image.url)
        }
    }

    override fun getItemCount() = list.size

    inner class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val labelImageView: ImageView = itemView.label_image_view
        val imageView: ImageView = itemView.image_view
        val heroName: TextView = itemView.hero_name_text_view

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    /**
     * load hero image
     * */
    private fun ImageView.loadImage(url: String) {
        Glide.with(context)
            .load(url)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(this)
    }

    interface OnItemClickListener {

        /**
         * call when item was clicked
         * @param position item position in the list
         * */
        fun onItemClick(position: Int)
    }
}