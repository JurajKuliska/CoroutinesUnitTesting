package com.strv.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.strv.repository.*
import com.strv.repository.model.Apod
import com.strv.ui.apod.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class ApodViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val testMainDispatcher = TestCoroutineDispatcher()

    private lateinit var sut: ApodViewModel
    private lateinit var apodRepository: ApodRepository
    private val dataStateFlow = MutableStateFlow<ApodFetchState>(ApodFetchStateLoading(emptyList()))

    private val mockData1 =
        createMockData1(Date().apply { time = 100 }, Date().apply { time = 200 })
    private val mockData2 =
        createMockData2(Date().apply { time = 300 }, Date().apply { time = 400 })

    @Before
    fun `set up`() {
        Dispatchers.setMain(testMainDispatcher)

        apodRepository = mockk {
            coEvery { fetchApod() } coAnswers { }
            every { apodDataState } returns dataStateFlow
        }

        sut = ApodViewModel(apodRepository)
    }

    @After
    fun `tear down`() {
        testMainDispatcher.cleanupTestCoroutines()
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    @Test
    fun `test fetch success`() = runBlockingTest {
        assertThat(sut.viewState.first()).isEqualTo(ApodViewStateLoading(emptyList()))
        dataStateFlow.value = ApodFetchStateSuccess(mockData1)
        assertThat(sut.viewState.first()).isEqualTo(ApodViewStateSuccess(mockData1))
    }

    @Test
    fun `test load from cache while fetching`() = runBlockingTest {
        dataStateFlow.value = ApodFetchStateLoading(mockData1)
        assertThat(sut.viewState.first()).isEqualTo(ApodViewStateLoading(mockData1))
        dataStateFlow.value = ApodFetchStateSuccess(mockData2)
        assertThat(sut.viewState.first()).isEqualTo(ApodViewStateSuccess(mockData2))
    }

    @Test
    fun `test fetch error`() = runBlockingTest {
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
    fun `test fetch empty`() = runBlockingTest {
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