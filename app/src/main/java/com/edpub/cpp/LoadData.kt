package com.edpub.cpp

import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoadData : ViewModel() {

    var isNewUser = MutableLiveData<Boolean>(false)

    //var areChaptersLoaded = MutableLiveData<Boolean>(false)
    var areExamplesLoaded = MutableLiveData<Boolean>(false)

    var areChapterTitlesLoaded = MutableLiveData<Boolean>(false)
    var areExampleTitlesLoaded = MutableLiveData<Boolean>(false)

    var areFavouriteChapterKeysLoaded = MutableLiveData<Boolean>(false)
    var areFavouriteExampleKeysLoaded = MutableLiveData<Boolean>(false)

    var isCurrChapterLoaded = MutableLiveData<Boolean>(false)
    var isCurrExampleLoaded = MutableLiveData<Boolean>(false)

    var areFavChaptersCopied = MutableLiveData<Boolean>(false)
    var areFavExamplesCopied = MutableLiveData<Boolean>(false)

    var areCompletedChaptersKeysLoaded = MutableLiveData<Boolean>(false)
    var areCompletedExamplesKeysLoaded = MutableLiveData<Boolean>(false)



    fun loadFavouriteExampleKeys (){
        CoroutineScope(Dispatchers.IO).launch{
            val favExampleReference = Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("FAV_EXAM")
            favExampleReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        ObjectsCollection.favouriteExampleKeysList.clear()
                        for(example in snapshot.children){
                            val currExample = example.getValue(String::class.java)
                            ObjectsCollection.favouriteExampleKeysList.add(currExample!!)
                        }
                    }
                    ObjectsCollection.isFavouriteExampleKeysListLoaded = true
                    areFavouriteExampleKeysLoaded.value = true
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

        }
    }


    fun loadCompletedChaptersKeys (){
        CoroutineScope(Dispatchers.IO).launch{
            val completedChapterReference = Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("COMP_CHAP")
            completedChapterReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        ObjectsCollection.completedChaptersKeysList.clear()
                        for(chapter in snapshot.children){
                            val currChapter = chapter.getValue(String::class.java)
                            ObjectsCollection.completedChaptersKeysList.add(currChapter!!)
                        }
                        loadFavouriteChapterKeys()
                    }
                    ObjectsCollection.isCompletedChaptersListLoaded = true
                    areCompletedChaptersKeysLoaded.value = true
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }


    fun loadCompletedExamplesKeys (){
        CoroutineScope(Dispatchers.IO).launch{
            val completedExampleReference = Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("COMP_EXAM")
            completedExampleReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        ObjectsCollection.completedExamplesKeysList.clear()
                        for(example in snapshot.children){
                            val currExample = example.getValue(String::class.java)
                            ObjectsCollection.completedExamplesKeysList.add(currExample!!)
                        }
                    }
                    ObjectsCollection.isCompletedExamplesListLoaded = true
                    areCompletedExamplesKeysLoaded.value = true
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    fun loadChapterTitles(){
        Log.i("XPND", "load Chapter Titles Called")
        CoroutineScope(Dispatchers.IO).launch{
            val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("C_TITLES")
            databaseReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        Log.i("XPND", "Chapter titles exist")
                        ObjectsCollection.chaptersTitlesList.clear()
                        for(title in snapshot.children){
                            val currTitle = title.getValue(Title::class.java)
                            ObjectsCollection.chaptersTitlesList.add(currTitle!!)
                        }
                        areChapterTitlesLoaded.value = true
                        ObjectsCollection.filteredChaptersTitlesList.clear()
                        ObjectsCollection.filteredChaptersTitlesList.addAll(ObjectsCollection.chaptersTitlesList)
                        ObjectsCollection.adapterChapters.notifyDataSetChanged()

                        loadCurrChapter()
                        loadCompletedChaptersKeys()

                    }else{
                        Log.i("XPND", "Chapter titles do not exist")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.i("XPND", error.toString())
                }
            })
        }
    }
    fun loadFavouriteChapterKeys (){
        CoroutineScope(Dispatchers.IO).launch{
            val favChapterReference = Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP")
            favChapterReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        ObjectsCollection.favouriteChapterKeysList.clear()
                        for(chapter in snapshot.children){
                            val currChapter = chapter.getValue(String::class.java)
                            ObjectsCollection.favouriteChapterKeysList.add(currChapter!!)
                        }
                    }
                    ObjectsCollection.isFavouriteChapterKeysListLoaded = true
                    areFavouriteChapterKeysLoaded.value = true
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

        }
    }
    fun loadCurrChapter(){
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseDatabase.getInstance().getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_CHAP").get().addOnSuccessListener {
                Log.i("EDP", it.toString())
                ObjectsCollection.currentChapterKey = it.value.toString()
                isCurrChapterLoaded.value = true
            }
        }
    }

}