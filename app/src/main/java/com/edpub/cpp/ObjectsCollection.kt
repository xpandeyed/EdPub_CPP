package com.edpub.cpp

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*

object ObjectsCollection {
    var isDataLoaded = false//for chapters and examples
    var isFavouriteExampleListLoaded = false
    var isFavouriteChapterKeysListLoaded = false
    var areFavouriteChaptersCopied = false
    var areFavouriteExamplesCopied = false

    var chaptersList = mutableListOf<Chapter>()
    var examplesList = mutableListOf<Chapter>()
    var favouriteChapterKeysList = mutableListOf<String>()
    var favouriteExampleKeysList = mutableListOf<String>()
    var favouriteChapters = mutableListOf<Chapter>()
    var favouriteExamples = mutableListOf<Chapter>()

    var adapterChapters = ChapterRVAdapter(favouriteChapters)
    var adapterExamples = ChapterRVAdapter(favouriteExamples)

    fun copyFavChaptersFromChapters(context: Context) {
        if(!areFavouriteChaptersCopied) {
            favouriteChapters.clear()
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "Just a Moment...", Toast.LENGTH_SHORT).show()
                var counter = 9
                while (counter >= 0) {
                    if (isDataLoaded && isFavouriteChapterKeysListLoaded) {
                        var n = 0
                        while (n < chaptersList.size) {
                            var index = favouriteChapterKeysList.indexOf(chaptersList[n].KEY)
                            if (index != -1) {
                                favouriteChapters.add(chaptersList[n])
                            }
                            n++
                        }
                        areFavouriteChaptersCopied = true
                        break
                    } else {
                        delay(1000)
                    }
                    counter--
                }
                if (counter == -1) {
                    Toast.makeText(
                        context,
                        "Time limit exceeded.You can try again in a moment.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}