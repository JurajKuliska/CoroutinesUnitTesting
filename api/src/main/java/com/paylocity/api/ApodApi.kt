package com.paylocity.api

import com.paylocity.api.common.Response

interface ApodApi {
    suspend fun fetchApod(count: Int): Response
}