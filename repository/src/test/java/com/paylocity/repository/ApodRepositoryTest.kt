package com.paylocity.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.paylocity.api.ApodApi
import com.paylocity.api.common.Response
import com.paylocity.api.dto.ApodDto
import com.paylocity.persistence.ApodPersistence
import com.paylocity.persistence.model.ApodModel
import com.paylocity.repository.model.Apod
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class ApodRepositoryTest {

    private val apodApiResponseChannel = Channel<List<ApodDto>>()
    private val apodApiErrorResponseChannel = Channel<String>()
    private val apodApi = mockk<ApodApi> {
        coEvery { fetchApod(any()) } coAnswers { Response.Success(apodApiResponseChannel.receive()) }
    }

    private val apodApiError = mockk<ApodApi> {
        coEvery { fetchApod(any()) } coAnswers { Response.Error(apodApiErrorResponseChannel.receive()) }
    }

    private val mockSavedData =
        createSavedMockData(Date().apply { time = 900 }, Date().apply { time = 800 })
    private val mockApiData =
        createApiMockData(Date().apply { time = 900 }, Date().apply { time = 800 })

    private val apodSavedData = MutableStateFlow<List<ApodModel>>(emptyList())
    private val apodPersistence = mockk<ApodPersistence> {
        coEvery { updateData(any()) } answers { apodSavedData.value = mockSavedData }
        every { apodList } returns apodSavedData
    }

    private val apodSavedDataInitialNonEmpty = MutableStateFlow(mockSavedData)
    private val apodPersistenceInitialNonEmpty = mockk<ApodPersistence> {
        every { apodList } returns apodSavedDataInitialNonEmpty
    }

    private val sutInitialEmpty: ApodRepository = ApodRepositoryImpl(apodApi, apodPersistence)
    private val sutInitialNonEmpty: ApodRepository =
        ApodRepositoryImpl(apodApi, apodPersistenceInitialNonEmpty)
    private val sutApiError: ApodRepository =
        ApodRepositoryImpl(apodApiError, apodPersistenceInitialNonEmpty)

    @Test
    fun `test loading`() = runTest {
        assertThat(sutInitialEmpty.apodDataState.first()).isEqualTo(ApodFetchStateEmpty)

        val job = launch { sutInitialEmpty.fetchApod() }

        assertThat(sutInitialEmpty.apodDataState.first()).isEqualTo(ApodFetchStateLoading(emptyList()))

        job.cancel()
    }

    @Test
    fun `test loading with cached data`() = runTest {
        val job = launch { sutInitialNonEmpty.fetchApod() }

        val fetchState = sutInitialNonEmpty.apodDataState.first()
        assertThat(fetchState is ApodFetchStateLoading).isTrue()
        val data = (fetchState as ApodFetchStateLoading).list

        assertThat(data.size).isEqualTo(2)

        data[0].checkData(
            expectedDate = Date().apply { time = 900 },
            expectedHdUrl = "https://hd1.url",
            expectedExplanation = "explanation 1",
            expectedMediaType = "image",
            expectedServiceVersion = "v1",
            expectedTitle = "title1",
            expectedUrl = "https://url1.com"
        )

        data[1].checkData(
            expectedDate = Date().apply { time = 800 },
            expectedHdUrl = "https://hd2.url",
            expectedExplanation = "explanation 2",
            expectedMediaType = "image",
            expectedServiceVersion = "v2",
            expectedTitle = "title2",
            expectedUrl = "https://url2.com"
        )

        job.cancel()
    }

    @Test
    fun `test loading and then success`() = runTest {
        val job = launch { sutInitialEmpty.fetchApod() }

        val fetchState = sutInitialEmpty.apodDataState
        assertThat(fetchState.first()).isEqualTo(ApodFetchStateLoading(emptyList()))

        apodApiResponseChannel.send(mockApiData)

        assertThat(fetchState.first() is ApodFetchStateSuccess).isTrue()

        val data = (fetchState.first() as ApodFetchStateSuccess).list

        assertThat(data.size).isEqualTo(2)

        data[0].checkData(
            expectedDate = Date().apply { time = 900 },
            expectedHdUrl = "https://hd1.url",
            expectedExplanation = "explanation 1",
            expectedMediaType = "image",
            expectedServiceVersion = "v1",
            expectedTitle = "title1",
            expectedUrl = "https://url1.com"
        )

        data[1].checkData(
            expectedDate = Date().apply { time = 800 },
            expectedHdUrl = "https://hd2.url",
            expectedExplanation = "explanation 2",
            expectedMediaType = "image",
            expectedServiceVersion = "v2",
            expectedTitle = "title2",
            expectedUrl = "https://url2.com"
        )

        job.cancel()
    }

    @Test
    fun `test loading and then error`() = runTest {
        val job = launch { sutApiError.fetchApod() }

        val fetchState = sutApiError.apodDataState
        assertThat(fetchState.first() is ApodFetchStateLoading).isTrue()

        apodApiErrorResponseChannel.send("Test Error")

        assertThat(fetchState.first() is ApodFetchStateError).isTrue()

        val data = (fetchState.first() as ApodFetchStateError)

        assertThat(data.error).isEqualTo("Test Error")

        data.list[0].checkData(
            expectedDate = Date().apply { time = 900 },
            expectedHdUrl = "https://hd1.url",
            expectedExplanation = "explanation 1",
            expectedMediaType = "image",
            expectedServiceVersion = "v1",
            expectedTitle = "title1",
            expectedUrl = "https://url1.com"
        )

        data.list[1].checkData(
            expectedDate = Date().apply { time = 800 },
            expectedHdUrl = "https://hd2.url",
            expectedExplanation = "explanation 2",
            expectedMediaType = "image",
            expectedServiceVersion = "v2",
            expectedTitle = "title2",
            expectedUrl = "https://url2.com"
        )


        job.cancel()
    }

    private fun Apod.checkData(
        expectedDate: Date,
        expectedHdUrl: String,
        expectedExplanation: String,
        expectedMediaType: String,
        expectedServiceVersion: String,
        expectedTitle: String,
        expectedUrl: String
    ) {
        assertThat(date.time).isEqualTo(expectedDate.time)
        assertThat(explanation).isEqualTo(expectedExplanation)
        assertThat(hdUrl).isEqualTo(expectedHdUrl)
        assertThat(mediaType).isEqualTo(expectedMediaType)
        assertThat(serviceVersion).isEqualTo(expectedServiceVersion)
        assertThat(title).isEqualTo(expectedTitle)
        assertThat(url).isEqualTo(expectedUrl)
    }

    private fun createSavedMockData(date1: Date, date2: Date) = listOf(
        ApodModel(
            explanation = "explanation 1",
            hdUrl = "https://hd1.url",
            date = date1,
            mediaType = "image",
            serviceVersion = "v1",
            title = "title1",
            url = "https://url1.com"
        ),
        ApodModel(
            explanation = "explanation 2",
            hdUrl = "https://hd2.url",
            date = date2,
            mediaType = "image",
            serviceVersion = "v2",
            title = "title2",
            url = "https://url2.com"
        )
    )

    private fun createApiMockData(date1: Date, date2: Date) = listOf(
        ApodDto(
            explanation = "explanation 1",
            hdUrl = "https://hd1.url",
            date = date1,
            mediaType = "image",
            serviceVersion = "v1",
            title = "title1",
            url = "https://url1.com"
        ),
        ApodDto(
            explanation = "explanation 2",
            hdUrl = "https://hd2.url",
            date = date2,
            mediaType = "image",
            serviceVersion = "v2",
            title = "title2",
            url = "https://url2.com"
        )
    )
}