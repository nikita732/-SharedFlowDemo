package com.example.sharedflowdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DemoViewModel : ViewModel() {
    private val _sharedFlow = MutableSharedFlow<Int>()
    val sharedFlow: SharedFlow<Int> = _sharedFlow.asSharedFlow()
    private val _collectedValues = MutableStateFlow<List<Int>>(emptyList())
    val collectedValues: StateFlow<List<Int>> = _collectedValues.asStateFlow()

    init {
        sharedFlowInit()
        startCollecting()
    }

    private fun sharedFlowInit() {
        viewModelScope.launch {
            for (i in 1..1000) {
                delay(2000)
                _sharedFlow.emit(i)
            }
        }
    }

    private fun startCollecting() {
        viewModelScope.launch {
            sharedFlow.collect { value ->
                _collectedValues.update { currentList ->
                    currentList + value
                }
            }
        }
    }
}