package com.paylocity.api

import com.paylocity.api.common.Response
import java.util.UUID

interface ApodApi {
    suspend fun fetchApod(count: RequestData): Response
}

data class RequestData(
    val count: Int,
    val id: String = UUID.randomUUID().toString()
)