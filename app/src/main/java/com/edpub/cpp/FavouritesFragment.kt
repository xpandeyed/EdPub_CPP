package com.edpub.cpp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

        var adapterList  = listOf(ObjectsCollection.adapterChapters, ObjectsCollection.adapterExamples)
        var viewPagerAdapter = FavouritesAdapterViewPager(adapterList)
        var viewPager = view.findViewById<ViewPager2>(R.id.vp2Favourites)

        viewPager.adapter = viewPagerAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tlFavourites)

        TabLayoutMediator(tabLayout, viewPager){tab, position->
            when(position){
                0->tab.text = "Chapters"
                1->tab.text = "Examples"
            }
        }.attach()

        CoroutineScope(Dispatchers.Main).launch {
            ObjectsCollection.adapterChapters.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
                override fun onItemClick(position: Int) {
                    val intent = Intent(activity, ChapterActivity::class.java).apply {
                        putExtra("POSITION", position)
                        putExtra("INVOKER", "fromFav")
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

            adapterList  = listOf(ObjectsCollection.adapterChapters, ObjectsCollection.adapterExamples)
            viewPagerAdapter = FavouritesAdapterViewPager(adapterList)
            viewPager = view.findViewById(R.id.vp2Favourites)

            var n = 40
            while(n>0){
                if(ObjectsCollection.areFavouriteChaptersCopied){
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
                    break
                }
                else{
                    when(n){
                        35-> Toast.makeText(activity, "Just a moment, we are loading data.", Toast.LENGTH_SHORT).show()
                        20-> Toast.makeText(activity, "Poor internet connection, We are still loading.", Toast.LENGTH_SHORT).show()
                    }
                    delay(250)
                    n--
                }
                if(n==0){
                    Toast.makeText(activity, "Time limit exceeded. You can try again in a moment.", Toast.LENGTH_SHORT).show()
                }

            }




        }

    }
}