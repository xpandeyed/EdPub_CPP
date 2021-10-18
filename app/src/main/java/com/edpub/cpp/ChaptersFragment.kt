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


class ChaptersFragment : Fragment() {

    private lateinit var rvChapters : RecyclerView
    private lateinit var pbChaptersProgress : CircularProgressIndicator
    private lateinit var tbChapters : Toolbar

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
        tbChapters = view.findViewById(R.id.tbChapters)

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