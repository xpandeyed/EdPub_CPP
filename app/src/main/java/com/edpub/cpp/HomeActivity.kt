package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {


    private lateinit var loadData: LoadData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.tbHomeToolBar))


        loadData = ViewModelProvider(this).get(LoadData::class.java)
        if(!loadData.areChaptersLoaded.value!!){
            loadData.loadChapters()
        }
        if(!loadData.areExamplesLoaded.value!!){
            loadData.loadExamples()
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
            replace(R.id.flFragmentContainer, fragment)
            commit()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_activity_appbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miProfile->{
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()

    }

}