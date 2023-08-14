package com.apod.ui

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.apod.repository.ApodFetchState
import com.apod.repository.ApodFetchStateEmpty
import com.apod.repository.ApodFetchStateError
import com.apod.repository.ApodFetchStateLoading
import com.apod.repository.ApodFetchStateSuccess
import com.apod.repository.ApodRepository
import com.apod.repository.model.Apod
import com.apod.ui.apod.ApodViewModel
import com.apod.ui.apod.ApodViewStateEmpty
import com.apod.ui.apod.ApodViewStateError
import com.apod.ui.apod.ApodViewStateLoading
import com.apod.ui.apod.ApodViewStateSuccess
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
class ApodViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val dataStateFlow = MutableStateFlow<ApodFetchState>(ApodFetchStateLoading(emptyList()))

    private val sut: ApodViewModel by lazy { ApodViewModel(apodRepository) }
    private val apodRepository: ApodRepository = mockk {
        coEvery { fetchApod() } just Runs
        every { apodDataState } returns dataStateFlow
    }

    private val mockData1 =
        createMockData1(Date().apply { time = 100 }, Date().apply { time = 200 })
    private val mockData2 =
        createMockData2(Date().apply { time = 300 }, Date().apply { time = 400 })

    @Test
    fun `test fetch success`() = runTest {
        sut.viewState.test {
            assertThat(awaitItem()).isEqualTo(ApodViewStateLoading(emptyList()))
            dataStateFlow.value = ApodFetchStateSuccess(mockData1)
            assertThat(awaitItem()).isEqualTo(ApodViewStateSuccess(mockData1))
        }
    }

    @Test
    fun `test load from cache while fetching`() = runTest {
        dataStateFlow.value = ApodFetchStateLoading(mockData1)
        assertThat(sut.viewState.first()).isEqualTo(ApodViewStateLoading(mockData1))
        dataStateFlow.value = ApodFetchStateSuccess(mockData2)
        assertThat(sut.viewState.first()).isEqualTo(ApodViewStateSuccess(mockData2))
    }

    @Test
    fun `test fetch error`() = runTest {
        assertThat(sut.viewState.first()).isEqualTo(ApodViewStateLoading(emptyList()))
        dataStateFlow.value = ApodFetchStateError(mockData1, "Test Error")
        val data = sut.viewState.first()
        assertThat(data is ApodViewStateError).isTrue()
        with(data as ApodViewStateError) {
            assertThat(list.size).isEqualTo(2)
            assertThat(list[0]).isEqualTo(mockData1[0])
            assertThat(list[1]).isEqualTo(mockData1[1])
            assertThat(error).isEqualTo("Test Error")
        }
    }

    @Test
    fun `test fetch empty`() = runTest {
        dataStateFlow.value = ApodFetchStateEmpty
        assertThat(sut.viewState.first()).isEqualTo(ApodViewStateEmpty)
    }

    private fun createMockData1(date1: Date, date2: Date) = listOf(
        Apod(
            explanation = "explanation 1",
            hdUrl = "https://hd1.url",
            date = date1,
            mediaType = "image",
            serviceVersion = "v1",
            title = "title1",
            url = "https://url1.com"
        ),
        Apod(
            explanation = "explanation 2",
            hdUrl = "https://hd2.url",
            date = date2,
            mediaType = "image",
            serviceVersion = "v2",
            title = "title2",
            url = "https://url2.com"
        )
    )

    private fun createMockData2(date1: Date, date2: Date) = listOf(
        Apod(
            explanation = "explanation 1",
            hdUrl = "https://hd1.url",
            date = date1,
            mediaType = "image",
            serviceVersion = "v1",
            title = "title1",
            url = "https://url1.com"
        ),
        Apod(
            explanation = "explanation 3",
            hdUrl = "https://hd3.url",
            date = date2,
            mediaType = "image",
            serviceVersion = "v3",
            title = "title3",
            url = "https://url3.com"
        )
    )

}