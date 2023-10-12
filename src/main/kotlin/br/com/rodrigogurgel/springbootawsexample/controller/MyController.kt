package br.com.rodrigogurgel.springbootawsexample.controller

import software.amazon.awssdk.core.sync.RequestBody as S3RequestBody
import io.awspring.cloud.sns.core.SnsTemplate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.messaging.Message
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest


@RestController
class MyController(
    private val snsTemplate: SnsTemplate,
    private val s3Client: S3Client
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MyController::class.java)
        private const val TOPIC_NAME = "arn:aws:sns:us-east-1:000000000000:my-topic"
    }

    @PostMapping
    fun postMessage(@RequestBody emails: List<String>) {
        logger.info("Send message {} to {}", emails, TOPIC_NAME)
        emails.forEach {
            snsTemplate.convertAndSend(TOPIC_NAME, it)
        }

    }

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile) {
        val putObjectRequest = PutObjectRequest
            .builder()
            .bucket("my-bucket")
            .key(file.name)
            .contentType(file.contentType)
            .build()
        val requestBody = S3RequestBody.fromBytes(file.bytes)
        s3Client.putObject(putObjectRequest, requestBody)
    }

    data class SendEmailCMD(
        val to: String,
        val subject: String,
        val body: String,
    )
}