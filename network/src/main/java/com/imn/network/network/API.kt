/*
 * Created by Nitheesh AG on 2022/8/21
 */

package com.imn.network.network

import com.imn.network.model.UIResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface API {

    @GET
    suspend fun fetchCustomUI(
        @Url url: String,
    ): UIResponse

    @GET
    suspend fun fetchImage(
        @Url url: String,
    ): ResponseBody
}