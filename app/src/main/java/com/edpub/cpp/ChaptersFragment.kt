package com.edpub.cpp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ChaptersFragment : Fragment() {

    private lateinit var rvChapters : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chapters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvChapters = view.findViewById(R.id.rvChapters)
        rvChapters.layoutManager = LinearLayoutManager(activity)
        val adapter = ChapterRVAdapter(ObjectsCollection.chaptersList)
        rvChapters.adapter = adapter
        rvChapters.setHasFixedSize(true)
        adapter.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val title = ObjectsCollection.chaptersList[position].TITLE
                val text = ObjectsCollection.chaptersList[position].TEXT
                val code = ObjectsCollection.chaptersList[position].CODE
                val intent = Intent(activity, ChapterActivity::class.java).apply {
                    putExtra("TITLE", title)
                    putExtra("TEXT", text)
                    putExtra("CODE", code)
                }
                startActivity(intent)
            }
        })
    }


}