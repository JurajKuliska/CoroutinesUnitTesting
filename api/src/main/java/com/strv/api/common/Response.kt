package com.strv.api.common

sealed class Response {
    data class Success<T>(val body: T) : Response()
    data class Error(val message: String) : Response()
}
