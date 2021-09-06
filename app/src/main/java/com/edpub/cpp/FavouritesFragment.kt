package com.edpub.cpp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.ObjectStreamException


class FavouritesFragment : Fragment() {

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObjectsCollection.copyFavChaptersFromChapters(activity?.applicationContext!!)



        ObjectsCollection.adapterChapters.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, ChapterActivity::class.java).apply {
                    putExtra("POSITION", position)
                    putExtra("IS_FROM_FAV", true)
                }
                startActivity(intent)
            }
        })
        ObjectsCollection.adapterExamples.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, ChapterActivity::class.java).apply {
                    putExtra("POSITION", position)
                    putExtra("IS_FROM_FAV", true)
                }
                startActivity(intent)
            }
        })


        val adapterList  = listOf(ObjectsCollection.adapterChapters, ObjectsCollection.adapterExamples)
        val viewPagerAdapter = FavouritesAdapterViewPager(adapterList)
        val viewPager = view.findViewById<ViewPager2>(R.id.vp2Favourites)
        viewPager.adapter = viewPagerAdapter


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