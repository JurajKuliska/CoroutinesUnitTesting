package com.paylocity.persistence

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.paylocity.persistence.dao.ApodDao
import com.paylocity.persistence.dao.ApodDb
import com.paylocity.persistence.model.ApodEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class ApodDaoTest {
    private lateinit var sut: ApodDao
    private lateinit var db: ApodDb

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ApodDb::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .allowMainThreadQueries().build()
        sut = db.apodDao()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun `test insert data and read`() = runBlocking {
        val data1 = sut.getAll().first()
        assertThat(data1).isEmpty()

        val date1 = Date().apply { time = 900 }
        val date2 = Date().apply { time = 900000000 }
        sut.updateData(getMockData1(date1, date2))

        val data2 = sut.getAll().first()
        assertThat(data2.size).isEqualTo(2)
        assertThat(data2[0].id).isNotEqualTo(data2[1].id)

        data2[0].checkData(
            expectedDate = date1,
            expectedExplanation = "explanation 1",
            expectedHdUrl = "https://hd1.url",
            expectedMediaType = "image",
            expectedServiceVersion = "v1",
            expectedTitle = "title1",
            expectedUrl = "https://url1.com"
        )

        data2[1].checkData(
            expectedDate = date2,
            expectedExplanation = "explanation 2",
            expectedHdUrl = "https://hd2.url",
            expectedMediaType = "image",
            expectedServiceVersion = "v2",
            expectedTitle = "title2",
            expectedUrl = "https://url2.com"
        )
    }

    @Test
    fun `test insert data, read, insert different data and read`() = runBlocking {
        sut.updateData(getMockData1(Date(), Date()))
        val date = Date().apply { time = 60000009 }
        sut.updateData(getMockData2(date))
        val data = sut.getAll().first()
        assertThat(data.size).isEqualTo(1)
        data[0].checkData(
            expectedDate = date,
            expectedExplanation = "explanation 3",
            expectedHdUrl = "https://hd3.url",
            expectedMediaType = "image",
            expectedServiceVersion = "v3",
            expectedTitle = "title3",
            expectedUrl = "https://url3.com"
        )
    }

    @Test
    fun `test insert empty data and read`() = runBlocking {
        sut.updateData(getMockData1(Date(), Date()))
        sut.updateData(emptyList())
        assertThat(sut.getAll().first()).isEmpty()
    }

    private fun ApodEntity.checkData(
        expectedDate: Date,
        expectedHdUrl: String,
        expectedExplanation: String,
        expectedMediaType: String,
        expectedServiceVersion: String,
        expectedTitle: String,
        expectedUrl: String
    ) {
        assertThat(id).isNotEqualTo(0)
        assertThat(date.time).isEqualTo(expectedDate.time)
        assertThat(explanation).isEqualTo(expectedExplanation)
        assertThat(hdUrl).isEqualTo(expectedHdUrl)
        assertThat(mediaType).isEqualTo(expectedMediaType)
        assertThat(serviceVersion).isEqualTo(expectedServiceVersion)
        assertThat(title).isEqualTo(expectedTitle)
        assertThat(url).isEqualTo(expectedUrl)
    }

    private fun getMockData1(date1: Date, date2: Date) = listOf(
        ApodEntity(
            id = 0,
            explanation = "explanation 1",
            hdUrl = "https://hd1.url",
            date = date1,
            mediaType = "image",
            serviceVersion = "v1",
            title = "title1",
            url = "https://url1.com"
        ),
        ApodEntity(
            id = 0,
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
        ApodEntity(
            id = 0,
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