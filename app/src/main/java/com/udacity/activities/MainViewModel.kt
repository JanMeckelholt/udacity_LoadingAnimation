package com.udacity.activities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.messaging.Repository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    val isDownloadCompleted: LiveData<Boolean?>
        get() = Repository.instance().repIsDownloadCompleted

    private val _checked = MutableLiveData<Int?>()
    val checked: LiveData<Int?>
        get() = _checked

    init {
        _checked.value = -1
    }

    fun setDownloadIsCompleted(isCompleted: Boolean) {
        Repository.instance().setDownloadIsCompleted(isCompleted)
    }


}

