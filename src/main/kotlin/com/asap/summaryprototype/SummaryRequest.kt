package com.asap.summaryprototype

import org.springframework.web.multipart.MultipartFile

data class SummaryRequest(
    val file: MultipartFile
) {
}