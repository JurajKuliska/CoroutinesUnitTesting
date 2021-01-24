package com.strv.api.common

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*

internal class BaseUrl(private val baseUrl: Url) {

    class Config(var baseUrl: Url? = null)

    companion object Feature : HttpClientFeature<Config, BaseUrl> {
        override val key = AttributeKey<BaseUrl>("BaseUrl")

        override fun prepare(block: Config.() -> Unit): BaseUrl {
            val config = Config()
            block(config)
            return BaseUrl(config.baseUrl
                ?: throw IllegalArgumentException("Base URL not configured"))
        }

        override fun install(feature: BaseUrl, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                context.url {
                    val originalUrl = Url(this)
                    takeFrom(feature.baseUrl)
                    encodedPath += originalUrl.encodedPath.withoutLeadingSlash()
                    fragment = originalUrl.fragment
                    trailingQuery = originalUrl.trailingQuery
                }
            }
        }

        private fun String.withoutLeadingSlash(): String = dropWhile { it == '/' }
    }
}