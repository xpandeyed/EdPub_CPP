package com.edpub.cpp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bToCurrentChapter = view.findViewById<Button>(R.id.bToCurrentChapter)
        bToCurrentChapter.setOnClickListener {
            val intent = Intent(activity, ChapterActivity::class.java)
            startActivity(intent)
        }
        val bToRandomExample = view.findViewById<Button>(R.id.bToRandomExample)
        bToRandomExample.setOnClickListener {
            val intent = Intent(activity, ExampleActivity::class.java)
            startActivity(intent)
        }

    }
}