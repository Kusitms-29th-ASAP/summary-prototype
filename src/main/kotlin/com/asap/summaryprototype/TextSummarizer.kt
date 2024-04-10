package com.asap.summaryprototype

import kotlinx.coroutines.reactor.awaitSingle
import org.intellij.lang.annotations.Language
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientException

@Component
class TextSummarizer(
    private val webClient: WebClient = WebClient.create("https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize"),
    @Value("\${clova.api-key}") private val clovaApiKey: String,
    @Value("\${clova.api-key-id}") private val clovaApiKeyId: String,
) {


    fun summaryText(text: String): String {
        val replacedText = text.replace("\n","")
        return webClient.post()
            .headers {
                it.add("X-NCP-APIGW-API-KEY-ID", clovaApiKeyId)
                it.add("X-NCP-APIGW-API-KEY", clovaApiKey)
            }
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                ClovaSummaryRequest(
                    ClovaDocument("가정통신문 요약", replacedText),
                    ClovaOption("ko", "general", 0, 4)
                )
            )
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals) {
                it.bodyToMono(String::class.java).map { Exception(it) }
            }
            .bodyToMono(ClovaSummaryResponse::class.java)
            .block()?.summary ?: "fail"
    }
}

data class ClovaSummaryRequest(
    val document: ClovaDocument,
    val option: ClovaOption
)

data class ClovaDocument(
    val title: String,
    val content: String
)

data class ClovaOption(
    val language: String,
    val model: String,
    val tone: Int,
    val summaryCount: Int
)

data class ClovaSummaryResponse(
    val summary: String
)