package com.popoli.smarttrainer

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenAIResponse(
    val choices: List<Choice>
)

data class Choice(
    val index: Int,
    val message: Message
)
