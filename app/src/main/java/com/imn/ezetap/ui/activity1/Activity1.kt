/*
 * Created by Nitheesh AG on 2022/8/21
 */

package com.imn.ezetap.ui.activity1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imn.ezetap.ui.theme.EzetapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Activity1 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EzetapTheme {
                ScreenOne()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScreenOne(viewModel: Activity1ViewModel = hiltViewModel()) {

    val fetchCustomUI: () -> Unit = {
        viewModel.fetchCustomUI("https://demo.ezetap.com/mobileapps/android_assignment.json")

    }
    LaunchedEffect(key1 = Unit, block = {
        fetchCustomUI()
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = viewModel.uiState.value.result != null,
            enter = fadeIn() + scaleIn(initialScale = 0.9f),
            exit = fadeOut()
        ) {
            Activity1Screen(viewModel)
        }

        AnimatedVisibility(
            visible = viewModel.uiState.value.error != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = viewModel.uiState.value.error ?: "Something went wrong!")
                Button(onClick = fetchCustomUI) {
                    Text(text = "Retry")
                }
            }

        }
        AnimatedVisibility(
            visible = viewModel.uiState.value.loading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CircularProgressIndicator()
        }
    }
}
