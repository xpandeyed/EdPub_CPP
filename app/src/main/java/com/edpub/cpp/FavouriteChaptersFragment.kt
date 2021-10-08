package com.edpub.cpp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FavouriteChaptersFragment : Fragment() {

    private lateinit var rvFavouriteChapters : RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_chapters, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavouriteChapters = view.findViewById(R.id.rvFavouriteChapters)
        rvFavouriteChapters.layoutManager = LinearLayoutManager(activity)
        rvFavouriteChapters.itemAnimator = null
        rvFavouriteChapters.adapter = ObjectsCollection.adapterFavouriteChapters

    }

}