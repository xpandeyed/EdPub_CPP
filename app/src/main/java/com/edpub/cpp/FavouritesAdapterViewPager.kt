package com.edpub.cpp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator

class FavouritesAdapterViewPager(private var adapterList : List<ChapterRVAdapter>, private val loadData: LoadData, private val viewLifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<FavouritesAdapterViewPager.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var rvFavouritesViewPager : RecyclerView = view.findViewById(R.id.rvFavouritesViewPager)
        var pbFavourites : CircularProgressIndicator = view.findViewById(R.id.pbFavourites)

    }

    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favourites_view_pager, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        loadData.areFavChaptersCopied.observe(viewLifecycleOwner, Observer {
            Log.i("Fuck", "Called")
            Log.i("Fuck", loadData.areFavChaptersCopied.value.toString())
            if(loadData.areFavChaptersCopied.value!!){
                Log.i("Fuck", loadData.areFavChaptersCopied.value.toString())
                Log.i("Fuck", ObjectsCollection.areFavouriteChaptersCopied.toString())

                holder.pbFavourites.visibility = View.GONE

            }
        })


        val currAdapter = adapterList[position]
        holder.rvFavouritesViewPager.layoutManager = LinearLayoutManager(context)
        holder.rvFavouritesViewPager.itemAnimator = null
        holder.rvFavouritesViewPager.adapter = currAdapter

    }

    override fun getItemCount(): Int {
        return adapterList.size
    }
}