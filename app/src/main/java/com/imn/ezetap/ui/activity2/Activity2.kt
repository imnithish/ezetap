/*
 * Created by Nitheesh AG on 2022/8/21
 */

package com.imn.ezetap.ui.activity2

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
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
import com.imn.ezetap.ui.theme.EzetapTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class Activity2 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data: String? = intent.getStringExtra("ui_data")

        setContent {
            EzetapTheme {
                /*
                 Activity 2 will display all the elements passed by Activity 1
                 */
                when (val res = deSerialize(data)) {
                    null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.background),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Something went wrong!")
                        }
                    }
                    else -> {
                        ScreenTwo(res)
                    }
                }
            }
        }
    }

    private fun deSerialize(data: String?): Data? {
        return try {
            if (data == null)
                null
            else
                Json.decodeFromString<Data>(data)
        } catch (e: Exception) {
            null
        }
    }
}

@Composable
fun ScreenTwo(data: Data) {

    val context = LocalContext.current

    /*
  by pair of key and value
  we can differentiate and add text fields dynamically to the ui
   */
    val textFieldValues = mutableStateListOf<Pair<String, String>>()

    LaunchedEffect(key1 = Unit, block = {
        for (i in data.uiData) {
            textFieldValues.add(
                Pair(i.key ?: "", "")
            )
        }
    })

    val updateTextFieldValue: (pair: Pair<String, String>) -> Unit = { pair ->
        var index = -1
        for (i in textFieldValues.indices) {
            if (textFieldValues[i].first == pair.first) {
                index = i
                break
            }
        }
        textFieldValues[index] = pair
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(12.dp),
        content = {
            item {
                val imageLoader = ImageLoader.Builder(context).components {
                    if (Build.VERSION.SDK_INT >= 28) {
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
                            model = data.logoUrl, imageLoader = imageLoader
                        ), contentDescription = ""
                    )
                }
            }
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(500),
                    text = data.headingText
                )
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }
            data.uiData.forEachIndexed { _, data ->
                when (data.uitype) {
                    "label" -> {
                        item(key = data.key) {
                            Text(
                                text = data.value ?: "",
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }
                    "edittext" -> {
                        item(key = data.key) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = textFieldValues.find {
                                    it.first == data.key
                                }?.second ?: "",
                                onValueChange = {
                                    updateTextFieldValue(
                                        Pair(data.key ?: "", it)
                                    )
                                },
                                label = { Text(data.hint ?: "") },
                                shape = MaterialTheme.shapes.large
                            )

                        }
                    }
                    "button" -> {
                        item {
                            Button(
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        "This is activity 2 :D",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
                                    text = data.value ?: "",
                                )
                            }
                        }

                    }
                }
            }
        }
    )

}
