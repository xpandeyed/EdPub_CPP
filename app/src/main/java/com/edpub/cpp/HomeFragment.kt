package com.edpub.cpp

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text


class HomeFragment : Fragment() {

    private lateinit var loadData: LoadData

    private lateinit var rvCurrentChapter: RecyclerView
    private lateinit var rvCurrentExample: RecyclerView

    private lateinit var tbHome : Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tbHome = view.findViewById(R.id.tbHome)
        tbHome.inflateMenu(R.menu.home_activity_appbar)
        tbHome.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.miProfile->{
                    val intent = Intent(activity, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        rvCurrentChapter = view.findViewById(R.id.rvCurrentChapter)
        rvCurrentChapter.layoutManager = LinearLayoutManager(activity)
        rvCurrentChapter.itemAnimator = null
        rvCurrentChapter.adapter = ObjectsCollection.adapterCurrentChapter

        rvCurrentExample = view.findViewById(R.id.rvCurrentExample)
        rvCurrentExample.layoutManager = LinearLayoutManager(activity)
        rvCurrentExample.itemAnimator = null
        rvCurrentExample.adapter = ObjectsCollection.adapterCurrentExample

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

        ObjectsCollection.adapterCurrentExample.setOnItemClickListener(object : CurrentChapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                if(ObjectsCollection.isDataLoaded){
                    val intent = Intent(activity, ExampleActivity::class.java).apply {
                        val positionOfCurrentExample = ObjectsCollection.examplesList.indexOf(ObjectsCollection.currentExample[position])
                        putExtra("POSITION", positionOfCurrentExample)
                        putExtra("INVOKER", "fromExample")
                    }
                    startActivity(intent)
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


        loadData = ViewModelProvider(requireActivity()).get(LoadData::class.java)

        loadData.areCompletedChaptersKeysLoaded.observe(viewLifecycleOwner, Observer {
            if(loadData.areCompletedChaptersKeysLoaded.value!!){
                val pbChaptersProgress = view.findViewById<ProgressBar>(R.id.pbChaptersProgress)
                pbChaptersProgress.isIndeterminate = false
                pbChaptersProgress.max = ObjectsCollection.chaptersList.size
                pbChaptersProgress.progress = ObjectsCollection.completedChaptersKeysList.size

                val tvCompletedChapters = view.findViewById<TextView>(R.id.tvCompletedChapters)
                tvCompletedChapters.text = ObjectsCollection.completedChaptersKeysList.size.toString()
                tvCompletedChapters.setTextSize(TypedValue.COMPLEX_UNIT_SP, 55f)
                tvCompletedChapters.setTextColor(resources.getColor(R.color.primaryColor))

                val tvTotalChapters = view.findViewById<TextView>(R.id.tvTotalChapters)
                tvTotalChapters.text = "/${ObjectsCollection.chaptersList.size.toString()}"


            }

        })

        loadData.areCompletedExamplesKeysLoaded.observe(viewLifecycleOwner, Observer {
            if(loadData.areCompletedExamplesKeysLoaded.value!!){

                val pbExamplesProgress = view.findViewById<ProgressBar>(R.id.pbExamplesProgress)

                pbExamplesProgress.isIndeterminate = false
                pbExamplesProgress.max = ObjectsCollection.examplesList.size
                pbExamplesProgress.progress = ObjectsCollection.completedExamplesKeysList.size

                val tvCompletedExamples = view.findViewById<TextView>(R.id.tvCompletedExamples)
                tvCompletedExamples.text = ObjectsCollection.completedExamplesKeysList.size.toString()
                tvCompletedExamples.setTextSize(TypedValue.COMPLEX_UNIT_SP, 55f)
                tvCompletedExamples.setTextColor(resources.getColor(R.color.primaryColor))


                val tvTotalExamples = view.findViewById<TextView>(R.id.tvTotalExamples)
                tvTotalExamples.text = "/${ObjectsCollection.examplesList.size}"



            }

        })

    }
    override fun onResume() {
        super.onResume()

    }
}