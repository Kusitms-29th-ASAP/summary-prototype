package com.asap.summaryprototype

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Controller
class TextSummaryController(
    val textSummarizer: TextSummarizer,
    val fileTextExtractor:  FileTextExtractor
) {

    @PostMapping("/api/summary")
    fun getSummary(
        @ModelAttribute summaryRequest: SummaryRequest,
        modelAttribute: Model
    ): String{
        val text = fileTextExtractor.extractText(summaryRequest.file)
        val response = SummaryResponse(textSummarizer.summaryText(text))
        modelAttribute.addAttribute("summaryResponse", response)
        return "summary_response"
    }


    val text by lazy {
        """
        |안녕하세요. 반갑습니다.
        |오늘은 날씨가 좋네요.
        |""".trimMargin()
    }
}