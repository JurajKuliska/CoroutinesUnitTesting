package com.paylocity.turbine

import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
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

        var currentValue = 0
        launch {
            sut.flow.collect {
                currentValue = it
            }
        }

        Truth.assertThat(currentValue).isEqualTo(0)
        advanceTimeBy(3998)
        Truth.assertThat(currentValue).isEqualTo(0)
        advanceTimeBy(5)
        Truth.assertThat(currentValue).isEqualTo(1)
        advanceTimeBy(4970)
        Truth.assertThat(currentValue).isEqualTo(1)
        advanceTimeBy(50)
        Truth.assertThat(currentValue).isEqualTo(2)

//        sut.flow.test {
//            assertThat(awaitItem()).isEqualTo(1)
//            assertThat(awaitItem()).isEqualTo(2)
//            assertThat(awaitItem()).isEqualTo(3)
//            cancelAndConsumeRemainingEvents()
//        }
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