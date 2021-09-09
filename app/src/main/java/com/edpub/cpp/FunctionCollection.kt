package com.edpub.cpp

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

object FunctionCollection {
    fun copyFavouriteChapters () {
        Log.i("FAV", "Copying Favourite Chapters")
        ObjectsCollection.favouriteChapters.clear()
        CoroutineScope(Dispatchers.Main).launch {
            var n = 0
            Log.i("FAV", "Size of chapter list : ${ObjectsCollection.chaptersList.size}")
            while (n < ObjectsCollection.chaptersList.size) {
                var index = ObjectsCollection.favouriteChapterKeysList.indexOf(ObjectsCollection.chaptersList[n].KEY)
                if (index != -1) {
                    ObjectsCollection.favouriteChapters.add(ObjectsCollection.chaptersList[n])
                    ObjectsCollection.adapterFavouriteChapters.notifyItemInserted(ObjectsCollection.favouriteChapters.size-1)
                }
                n++
                Log.i("FAV", "$n = ${ObjectsCollection.favouriteChapters}")
            }
            ObjectsCollection.areFavouriteChaptersCopied = true
        }

    }
    suspend fun loadChapters () {
        Log.i("FAV", "Loading chapters...")
        val job = CoroutineScope(Dispatchers.IO).async {
            val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("CHAPTERS")
            databaseReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(chapter in snapshot.children){
                            val currChapter = chapter.getValue(Chapter::class.java)
                            ObjectsCollection.chaptersList.add(currChapter!!)
                            Log.i("FAV", "${ObjectsCollection.chaptersList}")
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
        job.join()
        if(!ObjectsCollection.isNewUser && job.isCompleted){
            loadFavouriteChapterKeys()
        }
    }
    private suspend fun loadFavouriteChapterKeys (){
        Log.i("FAV", "Loading fav chapters")
        CoroutineScope(Dispatchers.IO).launch{
            val favChapterReference = Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("FAV_CHAP")
            favChapterReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(chapter in snapshot.children){
                            val currChapter = chapter.getValue(String::class.java)
                            ObjectsCollection.favouriteChapterKeysList.add(currChapter!!)
                            Log.i("FAV", "${ObjectsCollection.favouriteChapterKeysList}")
                        }
                    }
                    ObjectsCollection.isFavouriteChapterKeysListLoaded = true
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }


    }

}