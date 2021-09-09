package com.edpub.cpp

import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

object FunctionCollection {
    suspend fun copyFavouriteChapters () {
        ObjectsCollection.favouriteChapters.clear()
        val job = CoroutineScope(Dispatchers.Main).async {
            var n = 0
            while (n < ObjectsCollection.chaptersList.size) {
                var index = ObjectsCollection.favouriteChapterKeysList.indexOf(ObjectsCollection.chaptersList[n].KEY)
                if (index != -1) {
                    ObjectsCollection.favouriteChapters.add(ObjectsCollection.chaptersList[n])
                    ObjectsCollection.adapterFavouriteChapters.notifyItemInserted(ObjectsCollection.favouriteChapters.size-1)
                }
                n++
            }
            ObjectsCollection.areFavouriteChaptersCopied = true
        }
        job.await()
    }
    suspend fun loadChapters () {
        val job = CoroutineScope(Dispatchers.IO).async {
            val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("CHAPTERS")
            databaseReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(chapter in snapshot.children){
                            val currChapter = chapter.getValue(Chapter::class.java)
                            ObjectsCollection.chaptersList.add(currChapter!!)
                            ObjectsCollection.adapterChapters.notifyItemInserted(ObjectsCollection.chaptersList.size-1)
                        }
                    }
                    ObjectsCollection.isDataLoaded = true
                }
                override fun onCancelled(error: DatabaseError) {
                    ObjectsCollection.isDataLoaded= false
                }
            })
        }
        job.await()
        if(!ObjectsCollection.isNewUser){
            loadFavouriteChapterKeys()
        }
    }
    private suspend fun loadFavouriteChapterKeys (){
        val job = CoroutineScope(Dispatchers.IO).async {
            val favChapterReference = Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP")
            favChapterReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(chapter in snapshot.children){
                            val currChapter = chapter.getValue(String::class.java)
                            ObjectsCollection.favouriteChapterKeysList.add(currChapter!!)
                        }

                    }
                    ObjectsCollection.isFavouriteChapterKeysListLoaded = true
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
        job.await()
        copyFavouriteChapters()
    }

}