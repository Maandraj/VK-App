package com.maandraj.album_api

import com.maandraj.feature_api.utils.FeatureApi

interface AlbumFeatureApi : FeatureApi {
    fun route(): String
}