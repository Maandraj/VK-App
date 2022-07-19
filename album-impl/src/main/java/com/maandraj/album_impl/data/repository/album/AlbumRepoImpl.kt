package com.maandraj.album_impl.data.repository.album

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.maandraj.album_impl.R
import com.maandraj.album_impl.data.mapper.PhotosMapper
import com.maandraj.album_impl.domain.model.Photos
import com.maandraj.core.data.result.ResultOf
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.sdk.api.photos.PhotosService
import com.vk.sdk.api.photos.dto.PhotosGetAllResponse
import java.io.File
import java.io.IOException
import javax.inject.Inject

class AlbumRepoImpl @Inject constructor(
    private val context: Context,
    private val photosMapper: PhotosMapper,
) : AlbumRepo {
    override fun getAllPhotos(photosCallback: (photos: ResultOf<Photos>) -> Unit) {
        VK.execute(PhotosService().photosGetAll(ownerId = VK.getUserId()),
            object :
                VKApiCallback<PhotosGetAllResponse> {
                override fun success(result: PhotosGetAllResponse) {
                    val photos = photosMapper.map(result)
                    photosCallback(ResultOf.Success(photos))
                    Log.i(TAG, "result true ${result.count}")
                }

                override fun fail(error: Exception) {
                    photosCallback(ResultOf.Failure(context.getString(R.string.error_getting_photos)))
                    Log.i(TAG, "result ${error.message}")
                }
            })
    }

    @SuppressLint("Range")
    override fun savePhoto(
        url: String,
        context: Context,
        directory: File,
    ): ResultOf<Boolean> {
        return try {
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val downloadUri = Uri.parse(url)

            val request = DownloadManager.Request(downloadUri).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(url.substring(url.lastIndexOf("/") + 1))
                    .setDescription("")
                    .setDestinationInExternalPublicDir(
                        directory.toString(),
                        url.substring(url.lastIndexOf("/") + 1)
                    )
            }
            val downloadId = downloadManager.enqueue(request)
            val query = DownloadManager.Query().setFilterById(downloadId)
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_FAILED -> return ResultOf.Failure(context.getString(R.string.error_download_failed))
                    DownloadManager.STATUS_SUCCESSFUL -> return ResultOf.Success(true)
                }
            }
            ResultOf.Success(true)
        } catch (ex: IOException) {
            Log.i(TAG, "result ${ex.message}")
            ResultOf.Failure(context.getString(R.string.error_save_photo))
        } catch (ex: Exception) {
            Log.i(TAG, "result ${ex.message}")
            ResultOf.Failure(context.getString(com.maandraj.core.R.string.error_unknown))
        }
    }
}

private const val TAG = "AlbumRepo"