package com.example.androidkotlinseed.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.databinding.RowAttrItemBinding
import com.example.androidkotlinseed.domain.HeroAttribute
import com.example.androidkotlinseed.domain.SuperHero

class HeroAttributesAdapter(hero: SuperHero,
                            private val context: Context) : RecyclerView.Adapter<HeroAttributeHolder>() {

    private val TAG = HeroAttributesAdapter::class.java.simpleName

    private val heroAttributes: List<HeroAttribute>

    init {
        heroAttributes = mutableListOf()
        heroAttributes.add(HeroAttribute(context.getString(R.string.name), hero.name))
        heroAttributes.add(HeroAttribute(context.getString(R.string.real_name), hero.realName))
        heroAttributes.add(HeroAttribute(context.getString(R.string.height), hero.height))
        heroAttributes.add(HeroAttribute(context.getString(R.string.power), hero.power))
        heroAttributes.add(HeroAttribute(context.getString(R.string.abilities), hero.abilities))
        heroAttributes.add(HeroAttribute(context.getString(R.string.groups), hero.groups))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroAttributeHolder {
        val itemRowBinding: RowAttrItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.row_attr_item, parent, false)
        return HeroAttributeHolder(itemRowBinding)
    }

    override fun getItemCount(): Int = this.heroAttributes.size

    override fun onBindViewHolder(holder: HeroAttributeHolder, position: Int) {
        try {
            holder.bindHero(heroAttributes[position])
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, e.toString())
        }
    }
}