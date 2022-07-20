package com.maandraj.auth_impl.ui.authScreen.webclient

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.maandraj.auth_impl.R
import com.maandraj.core.BuildConfig

class VKWebClient(
    private val context: Context,
    private val onStatusChange: (status: AuthStatus, msg: String) -> Unit,
) : WebViewClient() {
    private var _currentUrl = ""
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url.toString()
        if (url.contains(SUCCESS_URL)) {
            if (url.contains(ERROR_USER_DENIED)) {
                onStatusChange(AuthStatus.ERROR_USER_DENIED,
                    context.getString(R.string.auth_error_blank))
                return true
            }
        }
        view?.loadUrl(request?.url.toString())
        return super.shouldOverrideUrlLoading(view, request)
    }

    /**
     * Check URL and return result for event
     */
    override fun onPageFinished(wv: WebView, url: String) {
        if (_currentUrl != url) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (url.contains(AUTH_URL)) {
                if (url.contains(BuildConfig.SCOPE)) {
                    imm.showSoftInput(wv, 0)
                    wv.visibility = View.VISIBLE
                    onStatusChange(AuthStatus.AUTH, context.getString(R.string.auth_log_in_text))
                }
                if (url.contains(CONFIRM_PARAM)) {
                    onStatusChange(AuthStatus.CONFIRM,
                        context.getString(R.string.auth_accept_permissions))
                }
                if (url.contains(ERROR_INVALID)) {
                    onStatusChange(AuthStatus.ERROR_INVALID,
                        context.getString(R.string.auth_invalid_username_or_password))
                }
            }
            if (url.contains(ERROR_PARAM) && !url.contains(ERROR_USER_DENIED)) {
                onStatusChange(AuthStatus.ERROR, context.getString(R.string.auth_error_blank))
            }
            if (url.contains(BLOCKED_URL)) {
                onStatusChange(AuthStatus.BLOCKED, context.getString(R.string.auth_account_blocked))
            }
            if (url.contains(SUCCESS_URL)) {
                if (!url.contains(ERROR_PARAM)) {
                    wv.visibility = View.GONE
                    onStatusChange(AuthStatus.SUCCESS,
                        context.getString(R.string.auth_successful_authentication))
                }
            }
        }
        _currentUrl = url
    }
}

private const val AUTH_URL = "https://oauth.vk.com/authorize"
private const val SUCCESS_URL = "https://oauth.vk.com/blank.html"
private const val BLOCKED_URL = "https://m.vk.com/login?act=blocked"
private const val ERROR_PARAM = "error"
private const val CONFIRM_PARAM = "q_hash"
private const val ERROR_INVALID = "email"
private const val ERROR_USER_DENIED = "user_denied"
