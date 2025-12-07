package com.example.sharedflowdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sharedflowdemo.ui.theme.SharedFlowDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharedFlowDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenSetup(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenSetup(
    modifier: Modifier = Modifier,
    viewModel: DemoViewModel = viewModel()
) {
    MainScreen(modifier, viewModel)
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: DemoViewModel = viewModel()
) {
    val collectedValues by viewModel.collectedValues.collectAsStateWithLifecycle()

    // Используем правильный тип Int, так как sharedFlow испускает целые числа
    val messages = remember { mutableStateListOf<Int>() }

    // Запускаем сбор значений из sharedFlow
    LaunchedEffect(key1 = Unit) {
        viewModel.sharedFlow.collect {
            println("Collecting $it")
            messages.add(it)
        }
    }

    LazyColumn(modifier = modifier) {
        // Отображаем значения из StateFlow
        items(collectedValues) { message ->
            Text(
                text = "StateFlow Value = $message",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(5.dp)
            )
        }

        // Отображаем значения из SharedFlow
        items(messages) { message ->
            Text(
                text = "SharedFlow Value = $message",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SharedFlowDemoTheme {
        ScreenSetup()
    }
}