package com.edpub.cpp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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


        CoroutineScope(Dispatchers.Main).launch{
            var n = 9
            while(n>=0){
                if(ObjectsCollection.isDataLoaded){

                    val adapter = ChapterRVAdapter(ObjectsCollection.chaptersList)
                    rvChapters.adapter = adapter

                    adapter.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(activity, ChapterActivity::class.java).apply {
                                putExtra("POSITION", position)
                            }
                            startActivity(intent)
                        }
                    })
                    break
                }//end of if
                else{
                    if(n==9){Toast.makeText(activity, "Just A Moment. Loading data...", Toast.LENGTH_SHORT).show()}
                    delay(500)
                    n--
                }
            }//end of while
            if(n==-1){
                Toast.makeText(activity, "Could not load data withing time!!", Toast.LENGTH_SHORT).show()
            }

        }


    }


}