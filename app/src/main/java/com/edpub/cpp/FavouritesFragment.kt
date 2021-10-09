package com.edpub.cpp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch


class FavouritesFragment : Fragment() {

    private lateinit var loadData: LoadData

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val favouriteChaptersFragment = FavouriteChaptersFragment()
        val favouriteExamplesFragment = FavouriteExamplesFragment()
        val fragments : ArrayList<Fragment> = arrayListOf(favouriteChaptersFragment, favouriteExamplesFragment)

        val viewPager = view.findViewById<ViewPager2>(R.id.vp2Favourites)
        val tabLayout = view.findViewById<TabLayout>(R.id.tlFavourites)

        val favouriteViewPagerAdapter = FavouritesAdapterViewPager(fragments, this)

        viewPager.isSaveEnabled = false
        viewPager.adapter = favouriteViewPagerAdapter


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