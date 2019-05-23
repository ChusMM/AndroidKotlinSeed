package com.example.androidkotlinseed.view.adapters

import android.app.Application
import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.databinding.RowHeroItemBinding
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.injection.BaseBindingAdapter
import com.example.androidkotlinseed.utils.ImageLoader
import kotlinx.android.synthetic.main.row_hero_item.view.*
import javax.inject.Inject

class HeroViewHolder(private val binding: RowHeroItemBinding, private val imageLoader: ImageLoader)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindHero(superHero: SuperHero, clickListener: HeroesAdapter.HeroItemClickListener) = with(itemView) {
        binding.setVariable(BR.heroModel, superHero)
        binding.executePendingBindings()

//        superHero.photo?.let {
//            imageLoader.loadFromUrlFor43AspectRatio(it, hero_pic, R.drawable.placeholder)
//        }
        itemView.setOnClickListener { clickListener.onClickHero(superHero) }
    }
}