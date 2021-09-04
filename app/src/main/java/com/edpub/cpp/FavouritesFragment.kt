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

        val adapterChapters = ChapterRVAdapter(ObjectsCollection.favouriteChapters)
        val adapterExamples = ChapterRVAdapter(ObjectsCollection.favouriteExamples)

        adapterChapters.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val title = ObjectsCollection.chaptersList[position].TITLE
                val text = ObjectsCollection.chaptersList[position].TEXT
                val code = ObjectsCollection.chaptersList[position].CODE
                val key = ObjectsCollection.chaptersList[position].KEY
                val intent = Intent(activity, ChapterActivity::class.java).apply {
                    putExtra("TITLE", title)
                    putExtra("TEXT", text)
                    putExtra("CODE", code)
                    putExtra("KEY", key)
                }
                startActivity(intent)
            }
        })
        adapterExamples.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val title = ObjectsCollection.chaptersList[position].TITLE
                val text = ObjectsCollection.chaptersList[position].TEXT
                val code = ObjectsCollection.chaptersList[position].CODE
                val key = ObjectsCollection.chaptersList[position].KEY
                val intent = Intent(activity, ChapterActivity::class.java).apply {
                    putExtra("TITLE", title)
                    putExtra("TEXT", text)
                    putExtra("CODE", code)
                    putExtra("KEY", key)
                }
                startActivity(intent)
            }
        })


        val adapterList  = listOf(adapterChapters, adapterExamples)
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