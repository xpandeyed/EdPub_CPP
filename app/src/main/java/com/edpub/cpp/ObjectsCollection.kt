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

    var isNewUser = false

    var currentChapterPosition = 0
    var chaptersList = mutableListOf<Chapter>()
    var examplesList = mutableListOf<Chapter>()
    var favouriteChapterKeysList = mutableListOf<String>()
    var favouriteExampleKeysList = mutableListOf<String>()
    var favouriteChapters = mutableListOf<Chapter>()
    var favouriteExamples = mutableListOf<Chapter>()

    var adapterFavouriteChapters = ChapterRVAdapter(favouriteChapters)
    var adapterFavouriteExamples = ChapterRVAdapter(favouriteExamples)

    var adapterChapters = ChapterRVAdapter(chaptersList)
    var adapterExamples = ChapterRVAdapter(examplesList)

}