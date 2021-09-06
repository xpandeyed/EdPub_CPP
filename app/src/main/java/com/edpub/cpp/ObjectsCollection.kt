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
                var counter = 10
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
                        break
                    } else {
                        delay(500)
                    }
                    counter--
                }
                if (counter == -1) {
                    Toast.makeText(
                        context,
                        "Time limit exceed.You can try again in a moment.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        areFavouriteChaptersCopied = true
    }
}