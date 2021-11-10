package com.edpub.cpp

import android.content.Intent
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var loadData: LoadData

    private lateinit var tbHome : Toolbar

    private lateinit var bCurrChapter : Button
    private lateinit var bCurrExample : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bCurrChapter = view.findViewById(R.id.bCurrChapter)

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

        loadData.isCurrChapterLoaded.observe(viewLifecycleOwner, {
            if(loadData.isCurrChapterLoaded.value!!){
                Log.i("EDP", ObjectsCollection.currentChapterKey)
                bCurrChapter.visibility = View.VISIBLE
                FirebaseDatabase.getInstance().getReference("C_TITLES").child(ObjectsCollection.currentChapterKey).child("TITLE").get().addOnSuccessListener {
                    Log.i("EDP", ObjectsCollection.currentChapterKey)
                    bCurrChapter.text = it.value.toString()
                }

            }
        })


        loadData.areCompletedChaptersKeysLoaded.observe(viewLifecycleOwner, {
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
                pbExamplesProgress.max = ObjectsCollection.exampleTitlesList.size
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

    override fun onResume() {
        if(loadData.isCurrChapterLoaded.value!!){
            Log.i("EDP", ObjectsCollection.currentChapterKey)
            bCurrChapter.visibility = View.VISIBLE
            FirebaseDatabase.getInstance().getReference("C_TITLES").child(ObjectsCollection.currentChapterKey).child("TITLE").get().addOnSuccessListener {
                Log.i("EDP", ObjectsCollection.currentChapterKey)
                bCurrChapter.text = it.value.toString()
            }
        }
        super.onResume()
    }
}