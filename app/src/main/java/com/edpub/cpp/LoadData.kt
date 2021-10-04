package com.edpub.cpp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoadData : ViewModel() {
    var areFavouriteChapterKeysLoaded = MutableLiveData<Boolean>(false)

}