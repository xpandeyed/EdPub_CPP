package com.edpub.cpp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class FavouritesFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterChapters = ChapterRVAdapter(ObjectsCollection.chaptersList)
        val adapterExamples = ChapterRVAdapter(ObjectsCollection.chaptersList)

        val adapterList  = listOf(adapterChapters, adapterExamples)
        val adapter = FavouritesAdapterViewPager(adapterList)
        val viewPager = view.findViewById<ViewPager2>(R.id.vp2Favourites)
        viewPager.adapter = adapter


        val tabLayout = view.findViewById<TabLayout>(R.id.tlFavourites)


        TabLayoutMediator(tabLayout, viewPager){tab, position->
            when(position){
                0->tab.text = "Chapters"
                1->tab.text = "Examples"
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

    }
}