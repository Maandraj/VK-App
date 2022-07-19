package com.maandraj.album_impl.data.repository.album

import android.content.Context
import android.os.Environment
import com.maandraj.album_impl.domain.model.Photos
import com.maandraj.core.data.result.ResultOf
import java.io.File

interface AlbumRepo {
    fun getAllPhotos(photosCallback: (photos: ResultOf<Photos>) -> Unit)
    fun savePhoto(
        url: String,
        context: Context,
        directory: File = File(Environment.DIRECTORY_PICTURES),
    ): ResultOf<Boolean>
}