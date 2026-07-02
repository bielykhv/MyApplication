package com.example.myapplication.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.myapplication.MyViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    openDetails: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text("Home screen")
            Button(
                onClick = openDetails
            ) {
                Text("Open Home Details")
            }
        }
    }
}

@Composable
fun HomeDetailsScreen(
    onBack: () -> Unit,
    openDetails: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text("Home details screen")
        }
    }
}

@Composable
fun SearchScreen(
    openDetails: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text("Search screen")
            Button(
                onClick = openDetails
            ) {
                Text("Open search Details")
            }
        }
    }
}

@Composable
fun SearchDetailsScreen(
    onBack:()-> Unit,
    openDetails: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text("search details screen")
        }
    }
}

@Composable
fun ProfileScreen(
    openDetails: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text("Profile screen")
            Button(
                onClick = openDetails
            ) {
                Text("Open profile Details")
            }
        }
    }
}

@Composable
fun ProfileDetailsScreen(
    onBack:()-> Unit,
    openDetails: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text("profile details screen")
            Button(
                onClick = openDetails
            ) {
                Text("Open profile next Details")
            }
        }
    }
}
@Composable
fun ProfileDetailsNextScreen(
    onBack:()-> Unit,
    openDetails: () -> Unit
) {
    val viewModel = koinViewModel<MyViewModel>()
    val state by viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.resetWhenScreenResumes()
                }
                Lifecycle.Event.ON_PAUSE -> {}
                Lifecycle.Event.ON_CREATE -> {}
                Lifecycle.Event.ON_START -> {}
                Lifecycle.Event.ON_STOP -> {}
                Lifecycle.Event.ON_DESTROY -> { println("screen destroyed")}
                Lifecycle.Event.ON_ANY -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            if (state.text.isNotEmpty()){
                Text(state.text)
            }
            Text("Profile details next screen")
            Button(
                onClick = openDetails
            ) {
                Text("Open next")
            }
            Button(
                onClick = { viewModel.reset() }
            ) {
                Text("убрать текст")
            }
        }
    }
}
@Composable
fun ProfileDetailsNextTextScreen(
    onBack:()-> Unit,
    openDetails: () -> Unit
) {
    val viewModel = koinViewModel<MyViewModel>()
    val state by viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.resetWhenScreenResumes()
                }

                Lifecycle.Event.ON_PAUSE -> {}
                Lifecycle.Event.ON_CREATE -> {}
                Lifecycle.Event.ON_START -> {}
                Lifecycle.Event.ON_STOP -> {}
                Lifecycle.Event.ON_DESTROY -> { println("screen destroyed")}
                Lifecycle.Event.ON_ANY -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }
    if ( state.openDialog){
        AlertDialog(
            onDismissRequest = { viewModel.hideDialog() },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.hideDialog()
                        onBack()
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.hideDialog()
                    }
                ) {
                    Text("Нет")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            if (state.text.isNotEmpty()){
                Text(state.text)
            }
            Text("Profile details next  text screen")
            Button(
                onClick = openDetails
            ) {
                Text("Open Search details screen")
            }
            Button(
                onClick = { viewModel.reset() }
            ) {
                Text("убрать текст")
            }
        }
    }
}