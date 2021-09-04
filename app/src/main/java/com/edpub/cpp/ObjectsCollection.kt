package com.edpub.cpp

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.*

object ObjectsCollection {
    var isDataLoaded = false//for chapters and examples
    var isFavouriteExampleListLoaded = false
    var isFavouriteChapterKeysListLoaded = false
    var areFavouriteChaptersCopied = false
    var areFavouriteExamplesCopied = false

    var chaptersList = arrayListOf<Chapter>()
    var examplesList = arrayListOf<Chapter>()
    var favouriteChapterKeysList = arrayListOf<String>()
    var favouriteExampleKeysList = arrayListOf<String>()
    var favouriteChapters = arrayListOf<Chapter>()
    var favouriteExamples = arrayListOf<Chapter>()

    fun copyFavChaptersFromChapters(context: Context) {
        CoroutineScope(Dispatchers.Main).launch {
            var counter = 9
            while(counter>=0){
                if (isDataLoaded && isFavouriteChapterKeysListLoaded) {
                    var n = 0
                    while (n < chaptersList.size){
                        if (chaptersList[n].KEY == favouriteChapterKeysList[n]) {
                            favouriteChapters.add(chaptersList[n])
                        }
                        n++
                    }
                    break
                }
                else{
                    delay(500)
                }
                counter--
            }
            if(counter==-1){
                Toast.makeText(context, "Time limit exceed.You can try again in a moment.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}