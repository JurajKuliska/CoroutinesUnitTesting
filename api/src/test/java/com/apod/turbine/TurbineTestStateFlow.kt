package com.apod.turbine

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TurbineTestStateFlow {

    @Test
    fun testTurbine() = runTest {
        val sut = TurbineStateFlowImpl(this)

        // this is a showcase of why StateFlow shouldn't be consumed using Turbine.test {}
        sut.stateFlow.test {
            assertThat(awaitItem()).isEqualTo(0)
            sut.emitStates()
            assertThat(awaitItem()).isEqualTo(1)
            assertThat(awaitItem()).isEqualTo(2)
        }
    }
}

private class TurbineStateFlowImpl(
    private val coroutineScope: CoroutineScope,
) {
    private val _stateFlow = MutableStateFlow(0)
    val stateFlow: Flow<Int> = _stateFlow.asStateFlow()
        .stateIn(coroutineScope, SharingStarted.Lazily, 0) // remove this line to make tests pass

    fun emitStates() {
        coroutineScope.launch {
            _stateFlow.value = 1
            _stateFlow.value = 2
        }
    }
}