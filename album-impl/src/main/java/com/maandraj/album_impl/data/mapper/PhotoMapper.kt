package com.maandraj.album_impl.data.mapper

import com.maandraj.album_impl.domain.model.Photo
import com.maandraj.core.data.BaseMapper
import com.maandraj.core.utils.timeStampConvertDate
import com.vk.sdk.api.photos.dto.PhotosPhotoXtrRealOffset
import javax.inject.Inject

class PhotoMapper @Inject constructor(

) : BaseMapper<PhotosPhotoXtrRealOffset, Photo> {
    override fun map(res: PhotosPhotoXtrRealOffset): Photo =
        Photo(date = res.date,
            srcUrl = res.sizes?.last()?.url ?: "",
            dateString = timeStampConvertDate(res.date.toLong()),
            text = res.text)
}
