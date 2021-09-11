package com.edpub.cpp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ExamplesFragment : Fragment() {

    private lateinit var rvExamples: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_examples, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }
}