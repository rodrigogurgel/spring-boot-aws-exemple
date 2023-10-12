package br.com.rodrigogurgel.springbootawsexample.consumers

import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.web.bind.annotation.RequestBody

@Configuration
class MySqsConsumer(
    private val mailSender: JavaMailSender,
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MySqsConsumer::class.java)
    }

    @SqsListener("my-queue")
    fun listen(@RequestBody email: String) {
        val randomNum = Math.random() * 10
        if (email.contains("error", ignoreCase = true)) throw RuntimeException("Fail to process message: $email")
        logger.info("Received message {}", email)

        val message = SimpleMailMessage()
        message.setTo(email)
        message.subject = "Sent from SQS my-topic"
        message.text = "This message was send from SQS listener"

        mailSender.send(message)
    }

    @SqsListener("my-queue-dlq", factory = "dlq")
    fun listenDlq(@RequestBody message: String) {
        logger.info("Received message from DLQ {}", message)
    }
}