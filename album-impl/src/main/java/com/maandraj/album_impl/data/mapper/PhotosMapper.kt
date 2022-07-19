package com.maandraj.album_impl.data.mapper

import com.maandraj.album_impl.domain.model.Photos
import com.maandraj.core.data.BaseMapper
import com.vk.sdk.api.photos.dto.PhotosGetAllResponse
import javax.inject.Inject

class PhotosMapper @Inject constructor(
    private val photoMapper: PhotoMapper,
) : BaseMapper<PhotosGetAllResponse, Photos> {
    override fun map(res: PhotosGetAllResponse): Photos {
        val items = res.items
        return if (items.isNullOrEmpty())
            Photos(mutableListOf())
        else Photos(items = items.map { itemRes -> photoMapper.map(itemRes) })

    }

}