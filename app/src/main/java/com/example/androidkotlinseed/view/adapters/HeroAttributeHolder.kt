package com.example.androidkotlinseed.view.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlinseed.BR
import com.example.androidkotlinseed.databinding.RowAttrItemBinding
import com.example.androidkotlinseed.domain.HeroAttribute

class HeroAttributeHolder(private val binding: RowAttrItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindHero(heroAttribute: HeroAttribute) {
        binding.setVariable(BR.heroAttr, heroAttribute)
    }
}