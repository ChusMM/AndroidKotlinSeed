package com.example.androidkotlinseed.api

import androidx.annotation.StringRes
import com.example.androidkotlinseed.R
import java.net.HttpURLConnection.HTTP_FORBIDDEN
import java.net.HttpURLConnection.HTTP_NOT_FOUND

enum class CallError(private val errorCode: Int, @StringRes val msgStringRes: Int) {
    NOT_FOUND(HTTP_NOT_FOUND, R.string.cannot_reach_server),
    FORBIDDEN(HTTP_FORBIDDEN, R.string.server_access_forbidden),
    UNKNOWN_ERROR(-1, R.string.server_call_failed);

    override fun toString(): String {
        return "CallError(errorCode=$errorCode, msgStringRes=$msgStringRes)"
    }

    companion object {
        fun buildFromErrorCode(errorCode: Int): CallError {
            return when (errorCode) {
                HTTP_NOT_FOUND -> NOT_FOUND
                HTTP_FORBIDDEN -> FORBIDDEN
                else           -> UNKNOWN_ERROR
            }
        }
    }
}