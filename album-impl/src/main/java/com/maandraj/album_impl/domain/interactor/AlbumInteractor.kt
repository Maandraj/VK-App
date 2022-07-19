package com.maandraj.album_impl.domain.interactor

import android.content.Context
import com.maandraj.album_impl.data.repository.album.AlbumRepo
import com.maandraj.album_impl.domain.model.Photos
import com.maandraj.core.data.config.ConfigRepo
import com.maandraj.core.data.result.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class AlbumInteractor @Inject constructor(
    private val configRepo: ConfigRepo,
    private val albumRepo: AlbumRepo,
) {
    fun getAllPhotos(photosCallback: (photos: ResultOf<Photos>) -> Unit) =
        albumRepo.getAllPhotos(photosCallback)

    suspend fun savePhoto(
        url: String,
        context: Context,
        directory: File,
    ) = withContext(Dispatchers.IO) {
        albumRepo.savePhoto(
            url = url,
            directory = directory,
            context = context)
    }

    fun logout() = configRepo.logout()
}