package com.strv.api

import com.strv.api.common.Response

interface ApodApi {
    suspend fun fetchApod(count: Int): Response
}