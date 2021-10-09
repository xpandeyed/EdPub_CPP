package com.edpub.cpp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator

class FavouritesAdapterViewPager(private val favouriteFragmentsList : ArrayList<Fragment>, favouriteFragment : Fragment) : FragmentStateAdapter(favouriteFragment){

    override fun getItemCount(): Int {
        return favouriteFragmentsList.size
    }

    override fun createFragment(position: Int): Fragment {
        return favouriteFragmentsList[position]
    }



}