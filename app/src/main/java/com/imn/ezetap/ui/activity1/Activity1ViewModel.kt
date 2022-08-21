/*
 * Created by Nitheesh AG on 2022/8/21
 */

package com.imn.ezetap.ui.activity1

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imn.network.model.UIResponse
import com.imn.network.network.Network
import com.imn.network.util.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UIState(
    val loading: Boolean = true,
    val result: UIResponse? = null,
    val error: String? = null,
)

@HiltViewModel
class Activity1ViewModel @Inject constructor(
    private val network: Network,
) : ViewModel() {

    private val _uiState = mutableStateOf(UIState())
    val uiState: State<UIState> = _uiState

    val textFieldValues = mutableStateListOf<Pair<String, String>>()

    /*
    by pair of key and value
    we can differentiate and add text fields dynamically to the ui
     */
    fun updateTextFieldValue(pair: Pair<String, String>) {
        var index = -1
        for (i in textFieldValues.indices) {
            if (textFieldValues[i].first == pair.first) {
                index = i
                break
            }
        }
        textFieldValues[index] = pair
    }

    private fun initTextFieldValues() {
        _uiState.value.result?.uidata?.filter {
            it.uitype == "edittext"
        }?.let {
            for (i in it) {
                textFieldValues.add(
                    Pair(i.key, "")
                )
            }
        }
    }

    fun fetchCustomUI(url: String) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(
            loading = true,
            error = null
        )
        /*
         Errors and exceptions to be handled gracefully
         */
        when (val res = network.fetchCustomUI(url)) {
            is ResultWrapper.Success -> {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = null,
                    result = res.value
                )
                initTextFieldValues()
            }
            is ResultWrapper.GenericError -> {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = res.error,
                    result = null
                )
            }
            is ResultWrapper.NetworkError -> {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = "No internet!",
                    result = null
                )
            }
        }
    }

}