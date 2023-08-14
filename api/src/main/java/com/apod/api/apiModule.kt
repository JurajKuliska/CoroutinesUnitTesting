package com.apod.api

import com.apod.api.common.BaseUrl
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.http.*
import org.koin.dsl.module

val apiModule = module {
    single<ApodApi> {
        ApodApiImpl(get())
    }

    single {
        HttpClient(OkHttp) {
            install(BaseUrl) {
                baseUrl = Url("https://api.nasa.gov/planetary/")
            }
            install(JsonFeature) {
                serializer =
                    KotlinxSerializer(kotlinx.serialization.json.Json { ignoreUnknownKeys = true })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }
            expectSuccess = false
        }
    }
}