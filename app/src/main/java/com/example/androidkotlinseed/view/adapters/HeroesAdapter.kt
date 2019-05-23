package com.example.androidkotlinseed.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.databinding.RowHeroItemBinding
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.utils.ImageLoader

class HeroesAdapter(private val heroes: List<SuperHero>,
                    private val clickListener: HeroItemClickListener,
                    private val context: Context,
                    private val imageLoader: ImageLoader) : RecyclerView.Adapter<HeroViewHolder>() {

    private val TAG = HeroesAdapter::class.java.simpleName

    interface HeroItemClickListener {
        fun onClickHero(superHero: SuperHero)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        //val view = LayoutInflater.from(context).inflate(R.layout.row_hero_item, parent, false)
        val itemRowBinding: RowHeroItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.row_hero_item, parent, false)
        return HeroViewHolder(itemRowBinding, imageLoader)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        try {
            holder.bindHero(heroes[position], clickListener)
        } catch (e: NullPointerException) {
            Log.e(TAG, e.toString())
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, e.toString())
        }
    }

    override fun getItemCount(): Int {
        return try {
            this.heroes.size
        } catch (e: NullPointerException) {
            Log.e(TAG, e.toString())
            0
        }
    }
}
