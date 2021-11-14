package com.edpub.cpp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavouriteExamplesFragment : Fragment() {

    private lateinit var rvFavouriteExamples : RecyclerView
    private lateinit var pbExamplesProgress : CircularProgressIndicator
    private lateinit var tvEmptyListMessage : TextView
    private lateinit var ivEmptyList : ImageView

    private lateinit var loadData: LoadData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favourite_examples, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bnvHomeFragmentNavigator = requireActivity().findViewById<BottomNavigationView>(R.id.bnvHomeFragmentNavigator)


        rvFavouriteExamples = view.findViewById(R.id.rvFavouriteExamples)
        rvFavouriteExamples.layoutManager = LinearLayoutManager(activity)
        rvFavouriteExamples.itemAnimator = null

        tvEmptyListMessage = view.findViewById(R.id.tvEmptyListMessage)
        ivEmptyList = view.findViewById(R.id.ivEmptyList)
        pbExamplesProgress = view.findViewById(R.id.pbExamplesProgress)

        ivEmptyList.setOnClickListener {
            bnvHomeFragmentNavigator.selectedItemId = R.id.miExamples
        }

        ObjectsCollection.adapterFavouriteExamples.setOnItemClickListener(object : ChapterRVAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(activity, ExampleActivity::class.java).apply {
                    putExtra("POSITION", position)
                    putExtra("KEY", ObjectsCollection.favouriteExamplesTitlesList[position].KEY)
                    putExtra("INVOKER", "FAV")
                }
                startActivity(intent)
            }
        })


        rvFavouriteExamples.adapter = ObjectsCollection.adapterFavouriteExamples

        loadData = ViewModelProvider(requireActivity()).get(LoadData::class.java)
        loadData.areFavouriteExampleKeysLoaded.observe(viewLifecycleOwner, Observer {
            if(loadData.areFavouriteExampleKeysLoaded.value!!){
                copyFavouriteExamples()
            }else{
                pbExamplesProgress.visibility =View.VISIBLE
            }
        })
        loadData.areFavExamplesCopied.observe(viewLifecycleOwner, Observer {
            if(loadData.areFavExamplesCopied.value!!){
                Log.i("XPND", "fav examples copied")
                Log.i("XPND", ObjectsCollection.favouriteExampleKeysList.toString())
                pbExamplesProgress.visibility = View.GONE
                if(ObjectsCollection.favouriteExampleKeysList.isEmpty()){
                    ivEmptyList.visibility = View.VISIBLE
                    tvEmptyListMessage.visibility = View.VISIBLE
                }
                else{
                    rvFavouriteExamples.visibility = View.VISIBLE
                }
            }
        })



    }

    private fun copyFavouriteExamples () {

        CoroutineScope(Dispatchers.Main).launch {
            ObjectsCollection.favouriteExamplesTitlesList.clear()
            var n = 0
            while (n < ObjectsCollection.exampleTitlesList.size) {
                val index = ObjectsCollection.favouriteExampleKeysList.indexOf(ObjectsCollection.exampleTitlesList[n].KEY)
                if (index != -1) {
                    ObjectsCollection.favouriteExamplesTitlesList.add(ObjectsCollection.exampleTitlesList[n])
                    ObjectsCollection.adapterFavouriteExamples.notifyItemInserted(ObjectsCollection.favouriteExamplesTitlesList.size-1)
                }
                n++
            }
            ObjectsCollection.areFavouriteExamplesCopied = true
            loadData.areFavExamplesCopied.value = true
        }

    }

    override fun onResume() {
        super.onResume()
        if(ObjectsCollection.areFavouriteExamplesCopied && loadData.areFavouriteExampleKeysLoaded.value!!){
            pbExamplesProgress.visibility = View.GONE
            if(ObjectsCollection.favouriteExampleKeysList.isEmpty()){
                ivEmptyList.visibility = View.VISIBLE
                tvEmptyListMessage.visibility = View.VISIBLE
            }else{
                rvFavouriteExamples.visibility = View.VISIBLE
            }
        }else{
            pbExamplesProgress.visibility = View.VISIBLE
            ivEmptyList.visibility = View.GONE
            tvEmptyListMessage.visibility  = View.GONE
        }
    }

}