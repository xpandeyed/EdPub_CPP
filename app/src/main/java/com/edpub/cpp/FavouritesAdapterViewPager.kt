package com.edpub.cpp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavouritesAdapterViewPager(private var adapterList : List<ChapterRVAdapter>, private val loadData: LoadData, private val viewLifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<FavouritesAdapterViewPager.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var rvFavouritesViewPager : RecyclerView = view.findViewById(R.id.rvFavouritesViewPager)
    }

    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favourites_view_pager, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val currAdapter = adapterList[position]
        holder.rvFavouritesViewPager.layoutManager = LinearLayoutManager(context)
        holder.rvFavouritesViewPager.itemAnimator = null
        holder.rvFavouritesViewPager.adapter = currAdapter
    }

    override fun getItemCount(): Int {
        return adapterList.size
    }
}