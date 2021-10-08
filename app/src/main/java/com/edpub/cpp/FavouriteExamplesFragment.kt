package com.edpub.cpp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FavouriteExamplesFragment : Fragment() {


    private lateinit var rvFavouriteExamples : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite_examples, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavouriteExamples = view.findViewById(R.id.rvFavouriteExamples)
        rvFavouriteExamples.layoutManager = LinearLayoutManager(activity)
        rvFavouriteExamples.itemAnimator = null
        rvFavouriteExamples.adapter = ObjectsCollection.adapterFavouriteExamples


    }


}