package com.popoli.smarttrainer

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val OPENAI_BASE_URL = "https://api.openai.com/v1/"
object ApiClient {
    @Volatile
    private var INSTANCE : OpenAIService? = null

    fun getInstance() : OpenAIService{
        synchronized(this){
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val okHttpClient = OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).
                                writeTimeout(120, TimeUnit.SECONDS).build()
            return INSTANCE ?: Retrofit.Builder().baseUrl(OPENAI_BASE_URL).client(okHttpClient)
                                .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
                                .create(OpenAIService::class.java).also { INSTANCE = it }
        }
    }
}