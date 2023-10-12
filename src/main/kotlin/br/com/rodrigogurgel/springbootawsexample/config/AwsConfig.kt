package br.com.rodrigogurgel.springbootawsexample.config

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory
import io.awspring.cloud.sqs.listener.SqsContainerOptionsBuilder
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement
import io.awspring.cloud.sqs.listener.acknowledgement.AcknowledgementResultCallback
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode
import java.time.OffsetDateTime
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import software.amazon.awssdk.services.sqs.SqsAsyncClient


@Configuration
class AwsConfig {
    @Bean
    fun defaultSqsListenerContainerFactory(sqsAsyncClient: SqsAsyncClient): SqsMessageListenerContainerFactory<Any> {
        return SqsMessageListenerContainerFactory.builder<Any>()
            .configure { options: SqsContainerOptionsBuilder ->
                options.acknowledgementMode(
                    AcknowledgementMode.ON_SUCCESS
                )
            }
            .acknowledgementResultCallback(AckResultCallback())
            .sqsAsyncClient(sqsAsyncClient)
            .build()
    }

    @Bean("dlq")
    fun dlqSqsListenerContainerFactory(sqsAsyncClient: SqsAsyncClient): SqsMessageListenerContainerFactory<Any> {
        return SqsMessageListenerContainerFactory.builder<Any>()
            .acknowledgementResultCallback(DlqAckResultCallback())
            .sqsAsyncClient(sqsAsyncClient)
            .build()
    }

    internal class AckResultCallback : AcknowledgementResultCallback<Any> {
        companion object {
            private val logger = LoggerFactory.getLogger(AckResultCallback::class.java)
        }
        override fun onSuccess(messages: MutableCollection<Message<Any>>) {
            logger.info("Ack with success at {}", OffsetDateTime.now())
        }

        override fun onFailure(messages: MutableCollection<Message<Any>>, t: Throwable) {
            logger.error("Ack with fail", t)
        }
    }

    internal class DlqAckResultCallback : AcknowledgementResultCallback<Any> {
        companion object {
            private val logger = LoggerFactory.getLogger(AckResultCallback::class.java)
        }
        override fun onSuccess(messages: MutableCollection<Message<Any>>) {
            logger.info("Ack from dlq success at {}", OffsetDateTime.now())
        }

        override fun onFailure(messages: MutableCollection<Message<Any>>, t: Throwable) {
            logger.error("Ack from dlq fail", t)
        }
    }
}