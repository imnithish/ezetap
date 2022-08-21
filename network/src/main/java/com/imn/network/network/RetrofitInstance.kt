/*
 * Created by Nitheesh AG on 2022/8/21
 */

package com.imn.network.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {

        private val retrofit by lazy {
            val loggingInterceptor = HttpLoggingInterceptor()

            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl("https://demo.ezetap.com/")
                .build()
        }

        val api: API by lazy {
            retrofit.create(API::class.java)
        }
    }
}
