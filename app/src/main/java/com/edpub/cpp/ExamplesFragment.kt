package com.edpub.cpp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator


class ExamplesFragment : Fragment() {

    private lateinit var rvExamples: RecyclerView
    private lateinit var pbExamplesProgress : CircularProgressIndicator

    private lateinit var loadData: LoadData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_examples, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pbExamplesProgress = view.findViewById(R.id.pbExamplesProgress)

        rvExamples = view.findViewById(R.id.rvExamples)
        rvExamples.layoutManager = LinearLayoutManager(activity)
        rvExamples.adapter = ObjectsCollection.adapterExamples

        ObjectsCollection.adapterExamples.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, ExampleActivity::class.java).apply {
                    putExtra("POSITION", position)
                    putExtra("INVOKER", "fromExample")
                }
                startActivity(intent)
            }
        })

        loadData = ViewModelProvider(requireActivity()).get(LoadData::class.java)
        loadData.areExamplesLoaded.observe(viewLifecycleOwner, Observer {
            if(loadData.areExamplesLoaded.value!!){
                pbExamplesProgress.visibility = View.GONE
            }
        })
    }

    override fun onResume() {
        super.onResume()

    }
}