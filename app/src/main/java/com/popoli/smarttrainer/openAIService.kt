package com.popoli.smarttrainer

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

private const val OPENAI_API_KEY = "OPEN_API_KEY"

interface OpenAIService {
    @POST("chat/completions")
    suspend fun createChatCompletion(
        @Body chatRequest : OpenAIRequest,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization : String = "Bearer $OPENAI_API_KEY",
    ) : OpenAIResponse
}