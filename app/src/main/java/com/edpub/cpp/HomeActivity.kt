package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity() {
    private lateinit var loadData : LoadData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        loadData = ViewModelProvider(this).get(LoadData::class.java)
        if(!loadData.areChapterTitlesLoaded.value!!){
            loadData.loadChapterTitles()
        }
        if(!loadData.areExampleTitlesLoaded.value!!){
            loadData.loadExampleTitles()
        }
        val homeFragment = HomeFragment()
        val chaptersFragment = ChaptersFragment()
        val examplesFragment = ExamplesFragment()
        val favouritesFragment = FavouritesFragment()

        if(savedInstanceState==null){
            setCurrentFragment(homeFragment)
        }

        val bnvHomeFragmentNavigator = findViewById<BottomNavigationView>(R.id.bnvHomeFragmentNavigator)
        bnvHomeFragmentNavigator.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miHome->{setCurrentFragment(homeFragment)}
                R.id.miChapters->{setCurrentFragment(chaptersFragment)}
                R.id.miExamples->{setCurrentFragment(examplesFragment)}
                R.id.miFavourites->{ setCurrentFragment(favouritesFragment)}
            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.clFragmentContainer, fragment)
            commit()
        }
    }


    override fun onBackPressed() {
        val bnvHomeFragmentNavigator = findViewById<BottomNavigationView>(R.id.bnvHomeFragmentNavigator)
        if(bnvHomeFragmentNavigator.selectedItemId==R.id.miHome){
            super.onBackPressed()
        }
        else{
            bnvHomeFragmentNavigator.selectedItemId=R.id.miHome
        }

    }

}