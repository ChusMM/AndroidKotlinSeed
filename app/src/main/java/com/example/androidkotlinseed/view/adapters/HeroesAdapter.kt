package com.example.androidkotlinseed.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlinseed.R
import com.example.androidkotlinseed.domain.SuperHero
import com.example.androidkotlinseed.utils.ImageLoader

class HeroesAdapter(private val heroes: List<SuperHero>,
                    private val context: Context,
                    private val imageLoader: ImageLoader) : RecyclerView.Adapter<HeroViewHolder>() {

    private val TAG = HeroesAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_hero, parent, false)

        return HeroViewHolder(view, imageLoader)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        try {
            holder.bindHero(heroes[position])
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
