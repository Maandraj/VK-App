package com.maandraj.album_impl.ui.album

import android.content.Context
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maandraj.album_impl.domain.interactor.AlbumInteractor
import com.maandraj.album_impl.domain.model.Photos
import com.maandraj.core.data.result.ResultOf
import com.maandraj.core.utils.extensions.asLiveData
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


class AlbumScreenVM @Inject constructor(
    private val albumInteractor: AlbumInteractor,
) : ViewModel() {


    private val _isSavePhoto = MutableLiveData<ResultOf<Boolean>>()
    val isSavePhoto = _isSavePhoto.asLiveData()

    private val _loading = MutableLiveData<Boolean>()
    val loading = _loading.asLiveData()

    private val _progress = MutableLiveData<Int>()
    val progress = _progress.asLiveData()


    private val _isLogout = MutableLiveData<ResultOf<Boolean>>()
    val isLogout = _isLogout.asLiveData()

    fun logout() = viewModelScope.launch {
        val resultLogout = albumInteractor.logout()
        _isLogout.postValue(resultLogout)
    }

    fun getAllPhotos(photosCallback: (photos: ResultOf<Photos>) -> Unit) = viewModelScope.launch {
        albumInteractor.getAllPhotos(photosCallback)
    }

    fun savePhoto(
        url: String,
        context: Context,
        directory: File = File(Environment.DIRECTORY_PICTURES),
        progress: (percent: Int) -> Unit,
    ) = viewModelScope.launch {
        _loading.postValue(true)
        val result = albumInteractor.savePhoto(url = url,
            context = context,
            directory = directory, progress)
        _isSavePhoto.postValue(result)
        _loading.postValue(false)
    }
}