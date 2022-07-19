package com.maandraj.auth_impl.ui.authScreen

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.maandraj.album_api.AlbumFeatureApi
import com.maandraj.auth_impl.R
import com.maandraj.auth_impl.ui.authScreen.webclient.AuthStatus
import com.maandraj.auth_impl.ui.authScreen.webclient.VKWebClient
import com.maandraj.core.data.result.ResultOf
import com.maandraj.core.utils.constants.KEY_SNACKBAR
import com.maandraj.core.utils.extensions.popSaveStateNavigation
import com.maandraj.core_ui.LogoVK
import com.maandraj.core_ui.samples.TextButtonView
import com.vk.dto.common.id.UserId
import kotlinx.coroutines.*
import java.util.regex.Pattern

@Composable
fun AuthScreen(
    navController: NavHostController,
    viewModel: AuthVM,
    modifier: Modifier,
    albumFeatureApi: AlbumFeatureApi,
) {
    val context = LocalContext.current
    val isAccountSave: ResultOf<Boolean> by viewModel.isSaveAccountState.observeAsState(ResultOf.Success(
        false))
    val isLogged: Boolean by viewModel.isAuth.observeAsState(false)
    val isLoading: Boolean by viewModel.loading.observeAsState(false)
    val authScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    viewModel.isLogged()
    val textButton: String =
        context.getString(if (isLogged) R.string.auth_logged_button_text else R.string.auth_sign_in_button_text)

    val url: String by viewModel.tokenUri.observeAsState("")
    SetObservables(isAccountSave = isAccountSave,
        navController = navController,
        scaffoldState = scaffoldState,
        scope = authScope,
        albumRoute = albumFeatureApi.route())

    Scaffold(modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        content = {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    LogoVK()
                    if (isLoading)
                        CircularProgressIndicator()
                    TextButtonView(
                        modifier = Modifier.fillMaxWidth(.7F),
                        text = textButton,
                        onClick = {
                            viewModel.isLogged()
                            if (!isLogged)
                                viewModel.getAuthUrl()
                        })
                }
            }
            if (url.isNotEmpty()) {
                VKAuthWebView(
                    scaffoldState = scaffoldState,
                    url = url,
                    scope = authScope,
                    viewModel = viewModel
                )
            }
        }
    )
}


@Composable
private fun SetObservables(
    isAccountSave: ResultOf<Boolean>,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    scope: CoroutineScope,
    albumRoute: String,
) {
    val context = LocalContext.current
    isAccountSave.let {
        when (it) {
            is ResultOf.Success -> {
                if (it.value) {
                    LaunchedEffect(KEY_SNACKBAR) {
                        scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.auth_successful_authentication))
                    }
                    navController.popSaveStateNavigation(route = albumRoute,
                        startDestinationId = navController.graph.startDestinationId)
                    scope.cancel("Finish authentication")
                }
            }
            is ResultOf.Failure -> {
                LaunchedEffect(KEY_SNACKBAR) {
                    scaffoldState.snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }
}

@Composable
fun VKAuthWebView(
    url: String,
    viewModel: AuthVM,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    context: Context = LocalContext.current,
) {
    val webView: WebView = WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        loadUrl(url)
    }
    if (url.isEmpty()) webView.visibility = View.GONE else webView.visibility = View.VISIBLE
    webView.webViewClient = VKWebClient(context) { status, msg ->
        scope.launch(Dispatchers.IO) {
            CoroutineScope(Dispatchers.Main).launch {
                if (status != AuthStatus.SUCCESS)
                    scaffoldState.snackbarHostState.showSnackbar(msg)
            }
            when (status) {

                AuthStatus.AUTH -> {
                    Log.i(TAG, msg)
                }
                AuthStatus.CONFIRM -> {
                    Log.i(TAG, msg)
                }
                AuthStatus.ERROR_INVALID -> {
                    Log.e(TAG, msg)
                }
                AuthStatus.ERROR -> {
                    Log.e(TAG, msg)
                    showAuthWindow(webView, url)
                }
                AuthStatus.ERROR_USER_DENIED -> {
                    Log.e(TAG, msg)
                    goneAuthWindow(viewModel)
                }
                AuthStatus.BLOCKED -> {
                    Log.e(TAG, msg)
                    showAuthWindow(webView, url)
                }
                AuthStatus.SUCCESS -> {
                    Log.i(TAG, msg)
                    var webUrl = ""
                    withContext(Dispatchers.Main) {
                        webView.visibility = View.GONE
                        if (webView.url == null) {
                            showAuthWindow(webView, url)
                        } else {
                            webUrl = webView.url!!
                            withContext(Dispatchers.IO) {
                                val tokenIndexStart = webUrl.indexOf("=") + 1
                                val tokenIndexEnd = webUrl.indexOf("&")
                                val token = webUrl.substring(tokenIndexStart, tokenIndexEnd)
                                val userIdMather = Pattern.compile("user_id=\\w+").matcher(webUrl)
                                if (token.isNotEmpty() && userIdMather.find()) {
                                    val userIdRegex =
                                        userIdMather.group().replace("user_id=".toRegex(), "")
                                    if (token.isNotEmpty() && userIdRegex.isNotEmpty()) {
                                        withContext(Dispatchers.Main) {
                                            val userId = UserId(value = userIdRegex.toLong())
                                            viewModel.saveAccount(
                                                context = context,
                                                accessToken = token,
                                                userId = userId)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    AndroidView(
        factory = {
            webView
        },
        update = {
            it.loadUrl(url)
        },
    )
}

private suspend fun showAuthWindow(webView: WebView, url: String) {
    CookieManager.getInstance().removeAllCookies(null)
    withContext(Dispatchers.Main) {
        webView.loadUrl(url)
    }
}

private fun goneAuthWindow(viewModel: AuthVM) {
    viewModel.authUrlClear()
}

private const val TAG = "AuthScreen"
