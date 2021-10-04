package com.edpub.cpp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FavouritesFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            copyFavouriteChapters()
            copyFavouriteExamples()
        }
    }

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            Log.i("Fragment", "OnCreateView Called")
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("Fragment", "OnViewCreated Called")

        val adapterList  = mutableListOf(ObjectsCollection.adapterFavouriteChapters, ObjectsCollection.adapterFavouriteExamples)
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


        ObjectsCollection.adapterFavouriteChapters.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    val intent = Intent(activity, ChapterActivity::class.java).apply {
                        putExtra("POSITION", position)
                        putExtra("INVOKER", "fromFav")
                    }
                    startActivity(intent)
                }
            })
        ObjectsCollection.adapterFavouriteExamples.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    val intent = Intent(activity, ExampleActivity::class.java).apply {
                        putExtra("POSITION", position)
                        putExtra("INVOKER", "fromFav")
                    }
                    startActivity(intent)
                }
            })

    }



    private fun copyFavouriteChapters () {
        ObjectsCollection.favouriteChapters.clear()
        CoroutineScope(Dispatchers.Main).launch {
            var n = 0
            while (n < ObjectsCollection.chaptersList.size) {
                val index = ObjectsCollection.favouriteChapterKeysList.indexOf(ObjectsCollection.chaptersList[n].KEY)
                if (index != -1) {
                    ObjectsCollection.favouriteChapters.add(ObjectsCollection.chaptersList[n])
                    ObjectsCollection.adapterFavouriteChapters.notifyItemInserted(ObjectsCollection.favouriteChapters.size-1)
                }
                n++
            }
            ObjectsCollection.areFavouriteChaptersCopied = true
        }
    }

    private fun copyFavouriteExamples () {
        ObjectsCollection.favouriteExamples.clear()
        CoroutineScope(Dispatchers.Main).launch {
            var n = 0
            while (n < ObjectsCollection.examplesList.size) {
                val index = ObjectsCollection.favouriteExampleKeysList.indexOf(ObjectsCollection.examplesList[n].KEY)
                if (index != -1) {
                    ObjectsCollection.favouriteExamples.add(ObjectsCollection.examplesList[n])
                    ObjectsCollection.adapterFavouriteExamples.notifyItemInserted(ObjectsCollection.favouriteExamples.size-1)
                }
                n++
            }
            ObjectsCollection.areFavouriteExamplesCopied = true
        }

    }


}