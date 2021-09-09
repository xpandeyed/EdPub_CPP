package com.edpub.cpp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.tbHomeToolBar))

        val homeFragment = HomeFragment()
        val chaptersFragment = ChaptersFragment()
        val examplesFragment = ExamplesFragment()
        val favouritesFragment = FavouritesFragment()

        setCurrentFragment(homeFragment)

        val bnvHomeFragmentNavigator = findViewById<BottomNavigationView>(R.id.bnvHomeFragmentNavigator)
        bnvHomeFragmentNavigator.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miHome->{setCurrentFragment(homeFragment)}
                R.id.miChapters->{setCurrentFragment(chaptersFragment)}
                R.id.miExamples->{setCurrentFragment(examplesFragment)}
                R.id.miFavourites->{
                    if(ObjectsCollection.isDataLoaded && ObjectsCollection.isFavouriteChapterKeysListLoaded){
                        FunctionCollection.copyFavouriteChapters()
                        setCurrentFragment(favouritesFragment)
                    }
                    else{
                        Toast.makeText(this, "Loading Data... Try again in a moment.", Toast.LENGTH_SHORT).show()
                    }
                }
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

}