# spring-boot-aws-exemple

## AWS CLI Command References
To help understand next commands check the AWS CLI Command References link [here](https://docs.aws.amazon.com/cli/latest/)

## Create SNS topic

```bash
awslocal sns create-topic --name my-topic
```

## Create SQS queue
```bash
awslocal sqs create-queue --queue-name "my-queue"
```

## Get SQS queue arn
```bash
awslocal sqs get-queue-attributes --queue-url "http://localhost:4566/000000000000/my-queue" --attribute-names QueueArn
```

## Subscribe SQS queue on SNS topic
```bash
awslocal sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:my-topic --protocol sqs --notification-endpoint arn:aws:sqs:us-east-1:000000000000:my-queue --attributes RawMessageDelivery=true
```

## Send a message to SNS topic
```bash
awslocal sns publish --topic-arn arn:aws:sns:us-east-1:000000000000:my-topic  --message "test message for pub-sub-test topic"
```

## Create a new queue to use as DLQ queue
```bash
awslocal sqs create-queue --queue-name "my-queue-dlq"
```

## Get SQS DLQ queue arn
```bash
awslocal sqs get-queue-attributes --queue-url "http://localhost:4566/000000000000/my-queue-dlq" --attribute-names QueueArn
```
## Set SQS DLQ queue to main SQS queue
```bash
awslocal sqs set-queue-attributes --queue-url http://localhost:4566/000000000000/my-queue --attributes '{ \"RedrivePolicy\": \"{\\\"deadLetterTargetArn\\\":\\\"arn:aws:sqs:us-east-1:000000000000:my-queue-dlq\\\", \\\"maxReceiveCount\\\":\\\"3\\\"}\" }'
```

## Create S3 Bucket
```bash
awslocal s3api create-bucket --bucket my-bucket
```