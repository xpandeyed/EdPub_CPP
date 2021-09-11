package com.edpub.cpp

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

object FunctionCollection {
    fun copyFavouriteChapters () {
        ObjectsCollection.favouriteChapters.clear()
        CoroutineScope(Dispatchers.Main).launch {
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
            Log.i("Fav", "${ObjectsCollection.favouriteChapters}")
        }

    }
    fun copyFavouriteExamples () {
        ObjectsCollection.favouriteExamples.clear()
        CoroutineScope(Dispatchers.Main).launch {
            var n = 0
            while (n < ObjectsCollection.examplesList.size) {
                var index = ObjectsCollection.favouriteExampleKeysList.indexOf(ObjectsCollection.examplesList[n].KEY)
                if (index != -1) {
                    ObjectsCollection.favouriteExamples.add(ObjectsCollection.examplesList[n])
                    ObjectsCollection.adapterFavouriteExamples.notifyItemInserted(ObjectsCollection.favouriteExamples.size-1)
                }
                n++
            }
            ObjectsCollection.areFavouriteExamplesCopied = true
            Log.i("Fav", "${ObjectsCollection.favouriteExamples}")
        }

    }
    fun loadChapters () {
        CoroutineScope(Dispatchers.IO).launch{
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
    }
    fun loadExamples () {
        CoroutineScope(Dispatchers.IO).launch{
            val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("EXAMPLES")
            databaseReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(example in snapshot.children){
                            val currExample = example.getValue(Chapter::class.java)
                            ObjectsCollection.examplesList.add(currExample!!)
                            Log.i("FAV", "${ObjectsCollection.examplesList}")
                            ObjectsCollection.adapterExamples.notifyItemInserted(ObjectsCollection.examplesList.size-1)
                        }
                    }
                    ObjectsCollection.isDataLoaded = true
                }
                override fun onCancelled(error: DatabaseError) {
                    ObjectsCollection.isDataLoaded= false
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
            //load current chapter key
            Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_CHAP").get()
            var n = 0
            while(n<ObjectsCollection.chaptersList.size){
                if(ObjectsCollection.currentChapterKey == ObjectsCollection.chaptersList[n].KEY)
                {
                    ObjectsCollection.currentChapterPosition = n
                    ObjectsCollection.isCurrChapterLoaded = true
                    break
                }
                n++
            }

        }
    }
    fun loadFavouriteExampleKeys (){
        CoroutineScope(Dispatchers.IO).launch{
            val favChapterReference = Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("FAV_EXAM")
            favChapterReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(example in snapshot.children){
                            val currExample = example.getValue(String::class.java)
                            ObjectsCollection.favouriteExampleKeysList.add(currExample!!)
                            Log.i("FAV", "${ObjectsCollection.favouriteExampleKeysList}")
                        }
                    }
                    ObjectsCollection.isFavouriteExampleKeysListLoaded = true
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
            //load current example key
            Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_EXAM").get()
            var n = 0
            while(n<ObjectsCollection.examplesList.size){
                if(ObjectsCollection.currentExampleKey == ObjectsCollection.examplesList[n].KEY)
                {
                    ObjectsCollection.currentExamplePosition = n
                    break
                }
                n++
            }
        }
    }
}