package com.paylocity.persistence

import com.google.common.truth.Truth.assertThat
import com.paylocity.persistence.dao.ApodDao
import com.paylocity.persistence.model.ApodEntity
import com.paylocity.persistence.model.ApodModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class ApodPersistenceTest {

    private lateinit var sut: ApodPersistence

    private val apodDao = object : ApodDao {
        val currentData = MutableStateFlow<List<ApodEntity>>(emptyList())
        override suspend fun updateData(list: List<ApodEntity>) {
            currentData.value = list
        }

        override fun getAll(): Flow<List<ApodEntity>> = currentData
    }

    @Before
    fun setup() {
        sut = ApodPersistenceImpl(apodDao)
    }

    @Test
    fun `save data to db and read`() = runBlockingTest {
        val date1 = Date().apply { time = 900 }
        val date2 = Date().apply { time = 900000000 }
        val mockData = getMockData1(date1, date2)

        assertThat(sut.apodList.first()).isEqualTo(emptyList<ApodModel>())
        sut.updateData(mockData)

        assertThat(sut.apodList.first()).isEqualTo(mockData)
    }

    @Test
    fun `save data to db and overwrite it with new data`() = runBlockingTest {
        val date1 = Date().apply { time = 900 }
        val date2 = Date().apply { time = 900000000 }
        val date3 = Date().apply { time = 6000000009 }
        val mockData1 = getMockData1(date1, date2)
        val mockData2 = getMockData2(date3)

        sut.updateData(mockData1)
        sut.updateData(mockData2)

        assertThat(sut.apodList.first()).isEqualTo(mockData2)
    }

    private fun getMockData1(date1: Date, date2: Date) = listOf(
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

    private fun getMockData2(date: Date) = listOf(
        ApodModel(
            explanation = "explanation 3",
            hdUrl = "https://hd3.url",
            date = date,
            mediaType = "image",
            serviceVersion = "v3",
            title = "title3",
            url = "https://url3.com"
        )
    )
}