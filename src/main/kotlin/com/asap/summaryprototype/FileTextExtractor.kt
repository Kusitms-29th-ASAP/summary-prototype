package com.asap.summaryprototype

import kr.dogfoot.hwplib.reader.HWPReader
import kr.dogfoot.hwplib.tool.textextractor.TextExtractMethod
import kr.dogfoot.hwplib.tool.textextractor.TextExtractor
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream


@Component
class FileTextExtractor {
    fun extractText(file: MultipartFile): String{
        val convertedFile = file.convertToFile()
        try{
            val hwpFile = HWPReader.fromFile(convertedFile)
            return TextExtractor.extract(hwpFile, TextExtractMethod.InsertControlTextBetweenParagraphText)
        }finally {
            convertedFile.delete()
        }
    }
}

fun MultipartFile.convertToFile(): File {
    this.originalFilename?.let{
        val file = File(it)
        file.createNewFile()
        val fos = FileOutputStream(file)
        fos.write(this.bytes)
        fos.close()
        return file
    } ?: throw IllegalArgumentException("File name is empty")
}