package com.popoli.smarttrainer

data class OpenAIRequest(
    val messages: List<Message>,
    val model: String
)

data class Message(
    val content: String,
    val role: String
)