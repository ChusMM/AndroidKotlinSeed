package com.example.androidkotlinseed.view.adapters

import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlinseed.databinding.RowHeroItemBinding
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.view.mvc.heroeslist.HeroesListViewMvc

class HeroViewHolder(private val binding: RowHeroItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bindHero(superHero: SuperHero, clickListener: HeroesListViewMvc.ViewListener?) = with(itemView) {
        binding.setVariable(BR.heroModel, superHero)
        binding.executePendingBindings()

        itemView.setOnClickListener { clickListener?.onClickHero(superHero) }
    }
}