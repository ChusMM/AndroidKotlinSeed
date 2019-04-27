package com.example.androidkotlinseed.view.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.utils.ImageLoader
import kotlinx.android.synthetic.main.row_hero.view.*

class HeroViewHolder(itemView: View, private val imageLoader: ImageLoader)
    : RecyclerView.ViewHolder(itemView) {

    fun bindHero(hero: SuperHero) = with(itemView) {
        hero.photo?.let {
            imageLoader.loadFromUrlFor43AspectRatio(it, hero_pic, R.drawable.placeholder)
        }
        hero_name.text = hero.name

        //itemView.setOnClickListener { listener.onHeroClicked(hero) }
    }
}