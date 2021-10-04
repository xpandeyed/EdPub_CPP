package com.edpub.cpp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoadData : ViewModel() {

    var isNewUser = MutableLiveData<Boolean>(false)

    var areChaptersLoaded = MutableLiveData<Boolean>(false)
    var areExamplesLoaded = MutableLiveData<Boolean>(false)

    var areFavouriteChapterKeysLoaded = MutableLiveData<Boolean>(false)
    var areFavouriteExampleKeysLoaded = MutableLiveData<Boolean>(false)

    var isCurrChapterLoaded = MutableLiveData<Boolean>(false)
    var isCurrExampleLoaded = MutableLiveData<Boolean>(false)

    var areFavChaptersCopied = MutableLiveData<Boolean>(false)
    var areFavExamplesCopied = MutableLiveData<Boolean>(false)









}