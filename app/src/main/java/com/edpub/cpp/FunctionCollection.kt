package com.edpub.cpp

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

object FunctionCollection {

    fun loadChapters () {
        CoroutineScope(Dispatchers.IO).launch{
            val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("CHAPTERS")
            databaseReference.addValueEventListener(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        ObjectsCollection.chaptersList.clear()
                        for(chapter in snapshot.children){
                            val currChapter = chapter.getValue(Chapter::class.java)
                            ObjectsCollection.chaptersList.add(currChapter!!)
                            ObjectsCollection.adapterChapters.notifyItemInserted(ObjectsCollection.chaptersList.size-1)
                        }
                        ObjectsCollection.isDataLoaded = true
                    }
                    if(Firebase.auth.currentUser!=null){
                        loadFavouriteChapterKeys()
                    }
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
                        ObjectsCollection.examplesList.clear()
                        for(example in snapshot.children){
                            val currExample = example.getValue(Chapter::class.java)
                            ObjectsCollection.examplesList.add(currExample!!)
                            ObjectsCollection.adapterExamples.notifyItemInserted(ObjectsCollection.examplesList.size-1)
                        }
                        ObjectsCollection.isDataLoaded = true
                    }
                    if(Firebase.auth.currentUser!=null){
                        loadFavouriteExampleKeys()
                    }

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
                        ObjectsCollection.favouriteChapterKeysList.clear()
                        for(chapter in snapshot.children){
                            val currChapter = chapter.getValue(String::class.java)
                            ObjectsCollection.favouriteChapterKeysList.add(currChapter!!)
                        }
                    }
                    ObjectsCollection.isFavouriteChapterKeysListLoaded = true
                    loadCurrentChapterKey()
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

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
                    loadCurrentExampleKey()
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

        }
    }


    fun loadCurrentChapterKey(){
        Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_CHAP").get().addOnSuccessListener {
            ObjectsCollection.currentChapterKey = it.value.toString()
            var n = 0
            while(n<ObjectsCollection.chaptersList.size){
                if(ObjectsCollection.currentChapterKey == ObjectsCollection.chaptersList[n].KEY)
                {
                    ObjectsCollection.currentChapterPosition = n
                    ObjectsCollection.currentChapter.clear()
                    ObjectsCollection.currentChapter.add(ObjectsCollection.chaptersList[ObjectsCollection.currentChapterPosition])
                    ObjectsCollection.adapterCurrentChapter.notifyItemInserted(ObjectsCollection.currentChapter.size-1)
                    ObjectsCollection.isCurrChapterLoaded = true
                    break
                }
                n++
            }
        }

    }

    fun loadCurrentExampleKey(){
        Firebase.database.getReference("USERS").child(Firebase.auth.currentUser!!.uid).child("CURR_EXAM").get().addOnSuccessListener {
            ObjectsCollection.currentExampleKey = it.value.toString()
            var n = 0
            while(n<ObjectsCollection.examplesList.size){
                if(ObjectsCollection.currentExampleKey == ObjectsCollection.examplesList[n].KEY)
                {
                    ObjectsCollection.currentExamplePosition = n
                    ObjectsCollection.currentExample.clear()
                    ObjectsCollection.currentExample.add(ObjectsCollection.examplesList[ObjectsCollection.currentExamplePosition])
                    ObjectsCollection.adapterCurrentExample.notifyItemInserted(ObjectsCollection.currentExample.size-1)
                    ObjectsCollection.isCurrExampleLoaded = true
                    break
                }
                n++
            }
        }
    }
}