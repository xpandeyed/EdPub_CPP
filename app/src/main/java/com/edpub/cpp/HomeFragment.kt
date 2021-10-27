package com.edpub.cpp

import android.content.Intent
import android.os.Bundle
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


class HomeFragment : Fragment() {

    private lateinit var loadData: LoadData

    private lateinit var tbHome : Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tbHome = view.findViewById(R.id.tbHome)
        tbHome.inflateMenu(R.menu.home_fragment_appbar)
        tbHome.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.miProfile->{
                    val intent = Intent(activity, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        val bToRandomExample = view.findViewById<Button>(R.id.bToRandomExample)
        bToRandomExample.setOnClickListener {
            if(ObjectsCollection.exampleTitlesList.size!=0){
                Toast.makeText(activity, "TODO", Toast.LENGTH_SHORT).show()
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
                pbChaptersProgress.max = ObjectsCollection.chaptersTitlesList.size
                pbChaptersProgress.progress = ObjectsCollection.completedChaptersKeysList.size

                val tvCompletedChapters = view.findViewById<TextView>(R.id.tvCompletedChapters)
                tvCompletedChapters.text = ObjectsCollection.completedChaptersKeysList.size.toString()
                tvCompletedChapters.setTextSize(TypedValue.COMPLEX_UNIT_SP, 55f)
                tvCompletedChapters.setTextColor(resources.getColor(R.color.primaryColor))

                val tvTotalChapters = view.findViewById<TextView>(R.id.tvTotalChapters)
                tvTotalChapters.text = "/${ObjectsCollection.chaptersTitlesList.size.toString()}"


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
                tvTotalExamples.text = "/${ObjectsCollection.exampleTitlesList.size}"
            }

        })

    }
}