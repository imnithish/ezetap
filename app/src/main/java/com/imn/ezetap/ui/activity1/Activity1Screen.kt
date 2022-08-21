/*
 * Created by Nitheesh AG on 2022/8/21
 */

package com.imn.ezetap.ui.activity1

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.imn.ezetap.data.Data
import com.imn.ezetap.data.UIData
import com.imn.ezetap.ui.activity2.Activity2
import com.imn.network.model.UIResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Activity1Screen(viewModel: Activity1ViewModel) {
    val context = LocalContext.current

    /*
     UI elements are to be sent to Activity 2 in the fields mentioned in the custom JSON
     */
    val openActivity2: (UIResponse) -> Unit = {
        try {
            val expected = ArrayList<UIData>()
            val formatted = it.uidata.mapTo(expected) { data ->
                UIData(
                    data.hint, data.key, data.uitype, data.value
                )
            }
            val data = Data(
                logoUrl = it.logoUrl,
                headingText = it.headingText,
                uiData = formatted
            )

            val intent = Intent(context, Activity2::class.java)
            intent.putExtra(
                "ui_data",
                Json.encodeToString(data)
            )
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("Activity1Screen", e.message ?: e.localizedMessage)
        }
    }

    BackdropScaffold(appBar = { },
        peekHeight = 128.dp,
        gesturesEnabled = false,
        backLayerBackgroundColor = Color.Black,
        backLayerContent = {
            val imageLoader = ImageLoader.Builder(context).components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }.build()
            Box(Modifier.fillMaxWidth()) {
                Image(
                    modifier = Modifier
                        .size(130.dp)
                        .align(Alignment.Center), painter = rememberAsyncImagePainter(
                        model = viewModel.uiState.value.result?.logoUrl, imageLoader = imageLoader
                    ), contentDescription = ""
                )
            }

        },
        frontLayerContent = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(12.dp),
                content = {
                    item {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight(500),
                            text = viewModel.uiState.value.result?.headingText ?: ""
                        )
                    }

                    item { Spacer(modifier = Modifier.height(4.dp)) }
                    viewModel.uiState.value.result?.uidata?.forEachIndexed { _, data ->
                        when (data.uitype) {
                            "label" -> {
                                item(key = data.key) {
                                    Text(
                                        text = data.value,
                                        modifier = Modifier.padding(top = 12.dp)
                                    )
                                }
                            }
                            "edittext" -> {
                                item(key = data.key) {
                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = viewModel.textFieldValues.find {
                                            it.first == data.key
                                        }?.second ?: "",
                                        onValueChange = {
                                            viewModel.updateTextFieldValue(
                                                Pair(data.key, it)
                                            )
                                        },
                                        label = { Text(data.hint) },
                                        shape = MaterialTheme.shapes.large
                                    )

                                }
                            }
                            "button" -> {
                                item {
                                    Button(
                                        onClick = {
                                            viewModel.uiState.value.result?.let {
                                                openActivity2(it)
                                            }
                                        },
                                        modifier = Modifier
                                            .padding(
                                                top = 16.dp,
                                                start = 64.dp,
                                                end = 64.dp
                                            )
                                            .fillMaxWidth()
                                            .defaultMinSize(minHeight = 48.dp),
                                        shape = MaterialTheme.shapes.large,
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.Black,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text(
                                            text = data.value,
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            )
        })
}