package com.maandraj.vk_app.ui.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.auth_impl.presentation.navigation.AuthFeatureImpl
import com.maandraj.core.data.config.ConfigRepo
import com.maandraj.core.data.config.ConfigRepoImpl
import com.maandraj.vk_app.App
import com.maandraj.vk_app.ui.main.AppContent
import com.maandraj.vk_app.ui.theme.VKAppTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authFeatureApi: AuthFeatureApi

    @Inject
    lateinit var configRepo: ConfigRepo
    private val appComponent by lazy {
        (application as App).appComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContent {
            VKAppTheme {
                AppContent(authFeatureApi = authFeatureApi,
                    configRepo = configRepo
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    val shared  = LocalContext.current.getSharedPreferences("", Context.MODE_PRIVATE)
    VKAppTheme {
        AppContent(authFeatureApi =AuthFeatureImpl(),
            configRepo =ConfigRepoImpl(shared))
    }
}