package com.edpub.cpp

object ObjectsCollection {
    var isDataLoaded = false//for chapters and examples and current chapter

    var isFavouriteChapterKeysListLoaded = false
    var isFavouriteExampleKeysListLoaded = false

    var areFavouriteChaptersCopied = false
    var areFavouriteExamplesCopied = false

    var isNewUser = false

    var chaptersTitlesList = mutableListOf<Title>()
    var exampleTitlesList = mutableListOf<Title>()

    var filteredChaptersTitlesList = mutableListOf<Title>()
    var filteredExamplesTitlesList = mutableListOf<Title>()

    var favouriteChapterKeysList = mutableSetOf<String>()
    var favouriteExampleKeysList = mutableSetOf<String>()

    var isCompletedChaptersListLoaded = false
    var isCompletedExamplesListLoaded = false

    var completedChaptersKeysList = mutableSetOf<String>()
    var completedExamplesKeysList = mutableSetOf<String>()


    var favouriteChaptersTitlesList = mutableListOf<Title>()
    var favouriteExamplesTitlesList = mutableListOf<Title>()

    var currentChapterKey = "C111AAA"
    var currentExampleKey = "E111AAA"

    var adapterFavouriteChapters = ChapterRVAdapter(favouriteChaptersTitlesList)
    var adapterFavouriteExamples = ChapterRVAdapter(favouriteExamplesTitlesList)

    var adapterChapters = ChapterRVAdapter(filteredChaptersTitlesList)
    var adapterExamples = ChapterRVAdapter(filteredExamplesTitlesList)


    fun sortMutableStringSet(set: MutableSet<String>):MutableSet<String>{
        val array = set.toTypedArray()
        array.sort()
        return array.toMutableSet()
    }

}