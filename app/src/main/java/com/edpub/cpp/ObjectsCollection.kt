package com.edpub.cpp

object ObjectsCollection {
    var isDataLoaded = false
    var isFavouriteExampleListLoaded = false
    var isFavouriteChapterListLoaded = false
    var isFavouriteChapterCopied = false
    var isFavouriteExampleCopied = false

    var chaptersList = arrayListOf<Chapter>()
    var examplesList = arrayListOf<Chapter>()
    var favouriteChaptersList = arrayListOf<String>()
    var favouriteExamplesList = arrayListOf<String>()
    var favouriteChapters = arrayListOf<Chapter>()
    var favouriteExamples = arrayListOf<Example>()

}