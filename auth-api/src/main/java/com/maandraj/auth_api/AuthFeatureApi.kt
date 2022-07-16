package com.maandraj.auth_api

import com.maandraj.feature_api.utils.FeatureApi

interface AuthFeatureApi : FeatureApi {
    fun route() : String
}