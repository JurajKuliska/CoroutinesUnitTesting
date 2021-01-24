package com.strv.api

import com.strv.api.common.Response
import com.strv.api.dto.ApodDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

internal class ApodApiImpl(private val httpClient: HttpClient) : ApodApi {
    override suspend fun fetchApod(count: Int): Response =
        try {
            val response: HttpResponse = httpClient.get(
                "apod?api_key=DEMO_KEY&count=$count"
            )
            if(response.status.isSuccess()) {
                Response.Success<List<ApodDto>>(response.receive())
            } else {
                Response.Error(String(response.readBytes()))
            }
        } catch (e: Exception) {
            Response.Error(e.message ?: "Unknown error")
        }
}