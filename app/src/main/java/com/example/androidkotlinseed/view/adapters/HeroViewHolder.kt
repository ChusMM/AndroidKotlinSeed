package com.example.androidkotlinseed.view.adapters

import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlinseed.databinding.RowHeroItemBinding
import com.example.androidkotlinseed.domain.SuperHero

class HeroViewHolder(private val binding: RowHeroItemBinding)
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