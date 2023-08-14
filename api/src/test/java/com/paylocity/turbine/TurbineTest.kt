package com.paylocity.turbine

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TurbineTest {

    @Test
    fun testTurbine() = runTest {
        val sut = TurbineImpl()

        sut.flow.test {
            assertThat(awaitItem()).isEqualTo(1)
            skipItems(1)
            assertThat(awaitItem()).isEqualTo(3)
            cancelAndConsumeRemainingEvents()
        }
    }
}

@OptIn(FlowPreview::class)
private class TurbineImpl {
    val flow = flow {
        emit(1)
        delay(5000)
        emit(2)
        delay(5000)
        emit(3)
    }.debounce(4000)
}