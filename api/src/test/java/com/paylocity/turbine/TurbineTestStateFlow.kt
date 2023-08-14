package com.paylocity.turbine

import app.cash.turbine.test
import com.google.common.truth.Truth
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
        .stateIn(coroutineScope, SharingStarted.Lazily, 0)

    fun emitStates() {
        coroutineScope.launch {
            _stateFlow.value = 1
            _stateFlow.value = 2
        }
    }
}