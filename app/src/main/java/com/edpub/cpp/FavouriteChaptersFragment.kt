package com.edpub.cpp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavouriteChaptersFragment : Fragment() {

    private lateinit var tvEmptyListMessage : TextView
    private lateinit var rvFavouriteChapters : RecyclerView
    private lateinit var ivEmptyList : ImageView
    private lateinit var pbFavouriteChapters : CircularProgressIndicator

    private lateinit var loadData: LoadData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourite_chapters, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val bnvHomeFragmentNavigator = requireActivity().findViewById<BottomNavigationView>(R.id.bnvHomeFragmentNavigator)


        rvFavouriteChapters = view.findViewById(R.id.rvFavouriteChapters)
        rvFavouriteChapters.layoutManager = LinearLayoutManager(activity)
        rvFavouriteChapters.itemAnimator = null

        tvEmptyListMessage = view.findViewById(R.id.tvEmptyListMessage)
        ivEmptyList = view.findViewById(R.id.ivEmptyList)
        pbFavouriteChapters = view.findViewById(R.id.pbFavouriteChapters)


        ivEmptyList.setOnClickListener {
            bnvHomeFragmentNavigator.selectedItemId = R.id.miChapters
        }


        ObjectsCollection.adapterFavouriteChapters.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, ChapterActivity::class.java).apply {
                    putExtra("POSITION", position)
                    putExtra("KEY", ObjectsCollection.favouriteChaptersTitlesList[position].KEY)
                    putExtra("INVOKER", "FAV")
                }
                startActivity(intent)
            }
        })

        rvFavouriteChapters.adapter = ObjectsCollection.adapterFavouriteChapters

        loadData = ViewModelProvider(requireActivity()).get(LoadData::class.java)
        loadData.areFavouriteChapterKeysLoaded.observe(viewLifecycleOwner, Observer {
            if(loadData.areFavouriteChapterKeysLoaded.value!!){
                copyFavouriteChapters()
            }else{
                pbFavouriteChapters.visibility =View.VISIBLE
            }
        })

        loadData.areFavChaptersCopied.observe(viewLifecycleOwner, Observer {
            if(loadData.areFavChaptersCopied.value!!){
                pbFavouriteChapters.visibility = View.GONE
                if(ObjectsCollection.favouriteChapterKeysList.isEmpty()){
                    ivEmptyList.visibility = View.VISIBLE
                    tvEmptyListMessage.visibility = View.VISIBLE
                }
                else{
                    rvFavouriteChapters.visibility = View.VISIBLE
                }
            }
        })

    }
    private fun copyFavouriteChapters () {
        CoroutineScope(Dispatchers.Main).launch {
            ObjectsCollection.favouriteChaptersTitlesList.clear()
            var n = 0
            while (n < ObjectsCollection.chaptersTitlesList.size) {
                val index = ObjectsCollection.favouriteChapterKeysList.indexOf(ObjectsCollection.chaptersTitlesList[n].KEY)
                if (index != -1) {
                    ObjectsCollection.favouriteChaptersTitlesList.add(ObjectsCollection.chaptersTitlesList[n])
                    ObjectsCollection.adapterFavouriteChapters.notifyItemInserted(ObjectsCollection.favouriteChaptersTitlesList.size-1)
                }
                n++
            }
            ObjectsCollection.areFavouriteChaptersCopied = true
            loadData.areFavChaptersCopied.value = true
        }
    }

    override fun onResume() {
        super.onResume()
        if(ObjectsCollection.areFavouriteChaptersCopied && loadData.areFavouriteChapterKeysLoaded.value!!){
            pbFavouriteChapters.visibility = View.GONE
            if(ObjectsCollection.favouriteChapterKeysList.isEmpty()){
                ivEmptyList.visibility = View.VISIBLE
                tvEmptyListMessage.visibility = View.VISIBLE
            }else{
                rvFavouriteChapters.visibility = View.VISIBLE
            }
        }else{
            pbFavouriteChapters.visibility = View.VISIBLE
            ivEmptyList.visibility = View.GONE
            tvEmptyListMessage.visibility  = View.GONE
        }
    }


}