package com.edpub.cpp

object ObjectsCollection {
    var isDataLoaded = false//for chapters and examples and current chapter

    var isFavouriteChapterKeysListLoaded = false
    var isFavouriteExampleKeysListLoaded = false

    var areFavouriteChaptersCopied = false
    var areFavouriteExamplesCopied = false

    var isNewUser = false


    var currentExamplePosition = 0
    var currentExampleKey = "E111AAA"

    var examplesList = mutableListOf<Chapter>()

    var chaptersTitlesList = mutableListOf<Title>()
    var exampleTitlesList = mutableListOf<Title>()


    var filteredChaptersList = mutableListOf<Chapter>()
    var filteredExamplesList = mutableListOf<Chapter>()

    var filteredChaptersTitlesList = mutableListOf<Title>()
    var filteredExamplesTitlesList = mutableListOf<Title>()

    var favouriteChapterKeysList = mutableSetOf<String>()
    var favouriteExampleKeysList = mutableSetOf<String>()

    var isCompletedChaptersListLoaded = false
    var isCompletedExamplesListLoaded = false


    var completedChaptersKeysList = mutableSetOf<String>()
    var completedExamplesKeysList = mutableSetOf<String>()

    var favouriteChapters = mutableListOf<Chapter>()
    var favouriteExamples = mutableListOf<Chapter>()

    var favouriteChaptersTitlesList = mutableListOf<Title>()
    var favouriteExamplesTitlesList = mutableListOf<Title>()

    var currentChapter = mutableListOf<Chapter>()
    var currentExample = mutableListOf<Chapter>()

    var adapterFavouriteChapters = ChapterRVAdapter(favouriteChaptersTitlesList)
    var adapterFavouriteExamples = ChapterRVAdapter(favouriteExamplesTitlesList)
    var adapterCurrentChapter = CurrentChapter(currentChapter)
    var adapterCurrentExample = CurrentChapter(currentExample)

    var adapterChapters = ChapterRVAdapter(filteredChaptersTitlesList)
    var adapterExamples = ChapterRVAdapter(filteredExamplesTitlesList)


    fun sortMutableStringSet(set: MutableSet<String>):MutableSet<String>{
        val array = set.toTypedArray()
        array.sort()
        return array.toMutableSet()
    }

}