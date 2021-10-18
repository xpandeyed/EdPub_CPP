package com.edpub.cpp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.text.TextUtils





class ChaptersFragment : Fragment() {

    private lateinit var rvChapters : RecyclerView
    private lateinit var pbChaptersProgress : CircularProgressIndicator
    private lateinit var svChapters : androidx.appcompat.widget.SearchView

    private lateinit var loadData: LoadData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_chapters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pbChaptersProgress = view.findViewById(R.id.pbChaptersProgress)
        svChapters = view.findViewById(R.id.svChapters)

        svChapters.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(newText: String?): Boolean {
                ObjectsCollection.filteredChaptersList.clear()
                if(newText.isNullOrEmpty()){
                    ObjectsCollection.filteredChaptersList.addAll(ObjectsCollection.chaptersList)
                }else{
                    for(chapter in ObjectsCollection.chaptersList){
                        if(chapter.TITLE!!.lowercase().contains(newText.lowercase())){
                            ObjectsCollection.filteredChaptersList.add(chapter)
                        }
                    }
                }
                ObjectsCollection.adapterChapters.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                ObjectsCollection.filteredChaptersList.clear()
                if(newText.isNullOrEmpty()){
                    ObjectsCollection.filteredChaptersList.addAll(ObjectsCollection.chaptersList)
                }else{
                    for(chapter in ObjectsCollection.chaptersList){
                        if(chapter.TITLE!!.lowercase().contains(newText.lowercase())){
                            ObjectsCollection.filteredChaptersList.add(chapter)
                        }
                    }
                }
                ObjectsCollection.adapterChapters.notifyDataSetChanged()
                return false
            }
        })

        svChapters.setOnCloseListener {
            ObjectsCollection.filteredChaptersList.clear()
            ObjectsCollection.filteredChaptersList.addAll(ObjectsCollection.chaptersList)
            ObjectsCollection.adapterChapters.notifyDataSetChanged()
            true
        }








        rvChapters = view.findViewById(R.id.rvChapters)
        rvChapters.layoutManager = LinearLayoutManager(activity)
        rvChapters.adapter = ObjectsCollection.adapterChapters

        ObjectsCollection.adapterChapters.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, ChapterActivity::class.java).apply {
                    putExtra("POSITION", position)
                    putExtra("INVOKER", "fromChapter")
                }
                startActivity(intent)
            }
        })

        loadData = ViewModelProvider(requireActivity()).get(LoadData::class.java)

        loadData.areChaptersLoaded.observe(viewLifecycleOwner, Observer {
            if(loadData.areChaptersLoaded.value!!){
                pbChaptersProgress.visibility = View.GONE
            }
        })


    }
}