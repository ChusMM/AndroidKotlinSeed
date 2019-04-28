package com.example.androidkotlinseed.view.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.databinding.RowHeroBinding
import com.example.androidkotlinseed.utils.ImageLoader

class HeroViewHolder(private val binding: RowHeroBinding, private val imageLoader: ImageLoader)
    : RecyclerView.ViewHolder(binding.root) {

    fun <T>bindHero(item: T) = with(itemView) {
        binding.setVariable(BR.heroModel, item)
        binding.executePendingBindings()

        /*hero.photo?.let {
            imageLoader.loadFromUrlFor43AspectRatio(it, hero_pic, R.drawable.placeholder)
        }
        hero_name.text = hero.name*/

        //itemView.setOnClickListener { listener.onHeroClicked(hero) }
    }

    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, imageUrl: String) {
        imageLoader.loadFromUrlFor43AspectRatio(imageUrl, view, R.drawable.placeholder)
    }
}