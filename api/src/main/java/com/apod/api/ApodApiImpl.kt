package com.apod.api

import com.apod.api.common.Response
import com.apod.api.dto.ApodDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

internal class ApodApiImpl(private val httpClient: HttpClient) : ApodApi {
    override suspend fun fetchApod(requestData: RequestData): Response =
        try {
            val response: HttpResponse = httpClient.get("apod?api_key=DEMO_KEY&count=${requestData.count}")
            if(response.status.isSuccess()) {
                Response.Success<List<ApodDto>>(response.receive())
            } else {
                Response.Error(String(response.readBytes()))
            }
        } catch (e: Exception) {
            Response.Error(e.message ?: "Unknown error")
        }
}