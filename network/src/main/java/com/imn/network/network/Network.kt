/*
 * Created by Nitheesh AG on 2022/8/21
 */

package com.imn.network.network

import com.imn.network.model.UIResponse
import com.imn.network.util.ResultWrapper
import com.imn.network.util.safeApiCall
import okhttp3.ResponseBody

interface Network {

    suspend fun fetchCustomUI(
        url: String,
    ): ResultWrapper<UIResponse> {
        return safeApiCall { RetrofitInstance.api.fetchCustomUI(url) }
    }

    suspend fun fetchImage(
        url: String,
    ): ResultWrapper<ResponseBody> {
        return safeApiCall { RetrofitInstance.api.fetchImage(url) }
    }
}