package com.edpub.cpp

object ObjectsCollection {
    var isDataLoaded = false//for chapters and examples and current chapter
    var isFavouriteExampleListLoaded = false
    var isFavouriteChapterKeysListLoaded = false //for favourite chapter keys and current chapter
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