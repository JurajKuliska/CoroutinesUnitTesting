package com.apod.repository.mockstatic

import android.net.Uri
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Test

class MockStaticTest {

    @Test
    fun `test class using Uri`() {
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("hello testers") } returns uriMock

        val sut = ClassUsingUri()
        assertThat(sut.url).isEqualTo(uriMock)
    }
}

private class ClassUsingUri {

    val url: Uri = Uri.parse("hello testers")
}