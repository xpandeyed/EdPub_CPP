package com.edpub.cpp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch{
            var n = 50
            while(n>0){
                if(ObjectsCollection.isDataLoaded){
                    view.findViewById<TextView>(R.id.tvCurrentLessonName).text = ObjectsCollection.chaptersList[ObjectsCollection.currentChapterPosition].TITLE
                    break
                }
                else{
                    delay(500)
                    n--
                }
            }
        }

        val bToCurrentChapter = view.findViewById<Button>(R.id.bToCurrentChapter)
        bToCurrentChapter.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                var n = 20
                while(n>0){
                    if(ObjectsCollection.isDataLoaded){
                        val intent = Intent(activity, ChapterActivity::class.java)
                        intent.putExtra("INVOKER", "fromCurrChap")
                        startActivity(intent)
                        break
                    }
                    else{
                        delay(250)
                        n--
                    }
                }
                if(n==0){
                    Toast.makeText(activity, "Data is not loaded yet. Try again in a moment.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val bToRandomExample = view.findViewById<Button>(R.id.bToRandomExample)
        bToRandomExample.setOnClickListener {
            Toast.makeText(activity, "Random Example will be shown.", Toast.LENGTH_SHORT).show()
        }

    }
}