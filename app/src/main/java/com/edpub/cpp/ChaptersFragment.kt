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
import com.google.android.material.progressindicator.CircularProgressIndicator

class ChaptersFragment : Fragment() {

    private lateinit var rvChapters : RecyclerView
    private lateinit var pbChaptersProgress : CircularProgressIndicator
    private lateinit var svChapters : androidx.appcompat.widget.SearchView

    private lateinit var loadData: LoadData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chapters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pbChaptersProgress = view.findViewById(R.id.pbChaptersProgress)
        svChapters = view.findViewById(R.id.svChapters)
        rvChapters = view.findViewById(R.id.rvChapters)

        svChapters.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(newText: String?): Boolean {
                ObjectsCollection.filteredChaptersTitlesList.clear()
                if(newText.isNullOrEmpty()){
                    ObjectsCollection.filteredChaptersTitlesList.addAll(ObjectsCollection.chaptersTitlesList)
                }else{
                    for(chapter in ObjectsCollection.chaptersTitlesList){
                        if(chapter.TITLE!!.lowercase().contains(newText.lowercase())){
                            ObjectsCollection.filteredChaptersTitlesList.add(chapter)
                        }
                    }
                }
                ObjectsCollection.adapterChapters.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                ObjectsCollection.filteredChaptersTitlesList.clear()
                if(newText.isNullOrEmpty()){
                    ObjectsCollection.filteredChaptersTitlesList.addAll(ObjectsCollection.chaptersTitlesList)
                }else{
                    for(chapter in ObjectsCollection.chaptersTitlesList){
                        if(chapter.TITLE!!.lowercase().contains(newText.lowercase())){
                            ObjectsCollection.filteredChaptersTitlesList.add(chapter)
                        }
                    }
                }
                ObjectsCollection.adapterChapters.notifyDataSetChanged()
                return false
            }
        })

        svChapters.setOnCloseListener {
            ObjectsCollection.filteredChaptersTitlesList.clear()
            ObjectsCollection.filteredChaptersTitlesList.addAll(ObjectsCollection.chaptersTitlesList)
            ObjectsCollection.adapterChapters.notifyDataSetChanged()
            true
        }


        rvChapters.layoutManager = LinearLayoutManager(activity)
        rvChapters.adapter = ObjectsCollection.adapterChapters

        ObjectsCollection.adapterChapters.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, ChapterActivity::class.java).apply {
                    val currChapterPosition = ObjectsCollection.chaptersTitlesList.indexOf(ObjectsCollection.filteredChaptersTitlesList[position])
                    putExtra("POSITION", currChapterPosition)
                    putExtra("KEY", ObjectsCollection.filteredChaptersTitlesList[position].KEY)
                }
                startActivity(intent)
            }
        })

        loadData = ViewModelProvider(requireActivity()).get(LoadData::class.java)

        loadData.areChapterTitlesLoaded.observe(viewLifecycleOwner, Observer {
            if(loadData.areChapterTitlesLoaded.value!!){
                pbChaptersProgress.visibility = View.GONE
            }
        })
    }
}