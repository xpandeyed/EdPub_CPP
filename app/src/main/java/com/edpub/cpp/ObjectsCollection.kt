package com.edpub.cpp

object ObjectsCollection {
    var isDataLoaded = false//for chapters and examples and current chapter

    var isFavouriteChapterKeysListLoaded = false //for favourite chapter keys and current chapter
    var isFavouriteExampleKeysListLoaded = false //for favourite chapter keys and current chapter

    var areFavouriteChaptersCopied = false
    var areFavouriteExamplesCopied = false

    var isCurrChapterLoaded = false
    var isCurrExampleLoaded = false

    var isNewUser = false

    var currentChapterPosition = 0
    var currentChapterKey = "C111AAA"

    var currentExamplePosition = 0
    var currentExampleKey = "E111AAA"

    var chaptersList = mutableListOf<Chapter>()
    var examplesList = mutableListOf<Chapter>()

    var favouriteChapterKeysList = mutableSetOf<String>()
    var favouriteExampleKeysList = mutableSetOf<String>()

    var favouriteChapters = mutableListOf<Chapter>()
    var favouriteExamples = mutableListOf<Chapter>()

    var currentChapter = mutableListOf<Chapter>()
    var currentExample = mutableListOf<Chapter>()

    var adapterFavouriteChapters = ChapterRVAdapter(favouriteChapters)
    var adapterFavouriteExamples = ChapterRVAdapter(favouriteExamples)
    var adapterCurrentChapter = CurrentChapter(currentChapter)
    var adapterCurrentExample = CurrentChapter(currentExample)

    var adapterChapters = ChapterRVAdapter(chaptersList)
    var adapterExamples = ChapterRVAdapter(examplesList)

}