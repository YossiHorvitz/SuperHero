package com.user.superhero.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.user.superhero.R
import com.user.superhero.data.Hero
import com.user.superhero.utils.extensions.load
import kotlinx.android.synthetic.main.list_item.view.*

class SuperHeroAdapter(
    var list: List<Hero>,
    private val listener: ((view: View, hero: Hero?) -> Unit)? = null
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
            with(imageView) {
                load(hero.image.url) {
                    listener?.let {
                        if (position != RecyclerView.NO_POSITION)
                            it(this, hero)
                    }
                }
            }
        }
    }

    override fun getItemCount() = list.size

    inner class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val labelImageView: ImageView = itemView.label_image_view
        val imageView: ImageView = itemView.image_view
        val heroName: TextView = itemView.hero_name_text_view
    }
}