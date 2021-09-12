package com.edpub.cpp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var rvCurrentChapter: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvCurrentChapter = view.findViewById(R.id.rvCurrentChapter)
        rvCurrentChapter.layoutManager = LinearLayoutManager(activity)
        rvCurrentChapter.itemAnimator = null
        rvCurrentChapter.adapter = ObjectsCollection.adapterCurrentChapter

        ObjectsCollection.adapterCurrentChapter.setOnItemClickListener(object : CurrentChapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                if(ObjectsCollection.isDataLoaded){
                    val intent = Intent(activity, ChapterActivity::class.java).apply {
                        val positionOfCurrChapter = ObjectsCollection.chaptersList.indexOf(ObjectsCollection.currentChapter[position])
                        putExtra("POSITION", positionOfCurrChapter)
                        putExtra("INVOKER", "fromChapter")
                    }
                    startActivity(intent)
                }
                else{
                    Toast.makeText(activity, "Loading Data...", Toast.LENGTH_SHORT).show()
                }
            }
        })

        val bToRandomExample = view.findViewById<Button>(R.id.bToRandomExample)
        bToRandomExample.setOnClickListener {
            if(ObjectsCollection.examplesList.size!=0){
                val randomIndex = (0 until ObjectsCollection.examplesList.size).random()
                val intent = Intent(activity, ExampleActivity::class.java)
                intent.putExtra("INVOKER", "fromExample")
                intent.putExtra("POSITION", randomIndex)
                startActivity(intent)
            }
            else{
                Toast.makeText(activity, "Examples not loaded yet. Wait a moment...", Toast.LENGTH_SHORT).show()
            }
        }

    }
}