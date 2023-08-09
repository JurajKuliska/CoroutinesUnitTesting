package com.paylocity.api

import com.google.common.truth.Truth.assertThat
import com.paylocity.api.common.BaseUrl
import com.paylocity.api.common.Response
import com.paylocity.api.dto.ApodDto
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.intellij.lang.annotations.Language
import org.junit.Test
import java.util.*

class ApodApiTest {

    private val mockWebServer = MockWebServer()

    private val httpClient = HttpClient(OkHttp) {
        install(BaseUrl) {
            baseUrl = Url("http://${mockWebServer.hostName}:${mockWebServer.port}")
        }
        install(JsonFeature) {
            serializer =
                KotlinxSerializer(kotlinx.serialization.json.Json { ignoreUnknownKeys = true })
        }
        expectSuccess = false
    }

    @Language("Json")
    private val mockResponseSuccess = """
        [
          {
            "copyright": "Robert Gendler",
            "date": "2001-05-10",
            "explanation": "Spiral galaxies viewed face-on display a grand design",
            "hdurl": "https://apod.nasa.gov/apod/image/0105/spiraledge_gendler_big.jpg",
            "media_type": "image",
            "service_version": "v1",
            "title": "Spirals On Edge",
            "url": "https://apod.nasa.gov/apod/image/0105/spiraledge_gendler.jpg"
          },
          {
            "date": "1998-10-26",
            "explanation": "Space travel entered the age of the ion drive",
            "hdurl": "https://apod.nasa.gov/apod/image/9810/ion_jpl_big.gif",
            "media_type": "image",
            "service_version": "v1",
            "title": "An Ion Drive for Deep Space 1",
            "url": "https://apod.nasa.gov/apod/image/9810/ion_jpl.jpg"
          }
        ]
    """.trimIndent()

    private val mockResponseError = "Wrong API Key"

    @Test
    fun `test response success`() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest) =
                MockResponse()
                    .setResponseCode(200)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody(mockResponseSuccess)
        }

        val sut: ApodApi = ApodApiImpl(httpClient)

        runTest {
            val response = sut.fetchApod(2)
            assertThat(response is Response.Success<*>).isTrue()
            (response as Response.Success<List<ApodDto>>).let {
                assertThat(it.body.size).isEqualTo(2)

                // 1st item
                it.body[0].checkData(
                    expectedDay = 10,
                    expectedMonth = Calendar.MAY,
                    expectedYear = 2001,
                    expectedHdUrl = "https://apod.nasa.gov/apod/image/0105/spiraledge_gendler_big.jpg",
                    expectedExplanation = "Spiral galaxies viewed face-on display a grand design",
                    expectedMediaType = "image",
                    expectedServiceVersion = "v1",
                    expectedTitle = "Spirals On Edge",
                    expectedUrl = "https://apod.nasa.gov/apod/image/0105/spiraledge_gendler.jpg"
                )

                // 2nd item
                it.body[1].checkData(
                    expectedDay = 26,
                    expectedMonth = Calendar.OCTOBER,
                    expectedYear = 1998,
                    expectedHdUrl = "https://apod.nasa.gov/apod/image/9810/ion_jpl_big.gif",
                    expectedExplanation = "Space travel entered the age of the ion drive",
                    expectedMediaType = "image",
                    expectedServiceVersion = "v1",
                    expectedTitle = "An Ion Drive for Deep Space 1",
                    expectedUrl = "https://apod.nasa.gov/apod/image/9810/ion_jpl.jpg"
                )
            }
        }
    }

    @Test
    fun `test response error`() {
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest) =
                MockResponse()
                    .setResponseCode(403)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .setBody(mockResponseError)
        }

        val sut: ApodApi = ApodApiImpl(httpClient)

        runTest {
            val response = sut.fetchApod(2)
            assertThat(response is Response.Error).isTrue()
            assertThat((response as Response.Error).message).isEqualTo("Wrong API Key")
        }
    }

    private fun ApodDto.checkData(
        expectedDay: Int,
        expectedMonth: Int,
        expectedYear: Int,
        expectedHdUrl: String,
        expectedExplanation: String,
        expectedMediaType: String,
        expectedServiceVersion: String,
        expectedTitle: String,
        expectedUrl: String
    ) {
        with(Calendar.getInstance().apply {
            time = date
        }) {
            assertThat(get(Calendar.YEAR)).isEqualTo(expectedYear)
            assertThat(get(Calendar.MONTH)).isEqualTo(expectedMonth)
            assertThat(get(Calendar.DAY_OF_MONTH)).isEqualTo(expectedDay)
        }
        assertThat(explanation).isEqualTo(expectedExplanation)
        assertThat(hdUrl).isEqualTo(expectedHdUrl)
        assertThat(mediaType).isEqualTo(expectedMediaType)
        assertThat(serviceVersion).isEqualTo(expectedServiceVersion)
        assertThat(title).isEqualTo(expectedTitle)
        assertThat(url).isEqualTo(expectedUrl)
    }
}
