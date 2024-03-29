package com.apod.api

import com.apod.api.common.Response
import java.util.UUID

interface ApodApi {
    suspend fun fetchApod(count: RequestData): Response
}

data class RequestData(
    val count: Int,
    val id: String = UUID.randomUUID().toString()
)