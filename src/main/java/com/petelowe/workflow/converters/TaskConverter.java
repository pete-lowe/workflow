package com.petelowe.workflow.converters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.petelowe.workflow.domain.*;
import com.petelowe.workflow.domain.daos.*;
import com.petelowe.workflow.suppliers.PartitionKeySupplier;
import com.petelowe.workflow.suppliers.SortKeySupplier;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class TaskConverter {

    public static TaskDefinitionDao toDao(Task task) {
        return switch (task.getTaskType()) {
            case S3_GET -> toS3GetDao(task);
            case S3_PUT -> toS3PutDao(task);
            case EMAIL -> toEmailDao(task);
            case SMS -> toSmsDao(task);
            default -> throw new RuntimeException("No task conversion available");
        };
    }

    public static S3GetTaskDefinitionDao toS3GetDao(Task task) {
        Instant creationTime = Instant.now();
        S3GetTask s3 = (S3GetTask) task;
        return S3GetTaskDefinitionDao.builder()
                .partitionKey(PartitionKeySupplier.getTaskDefinitionPartitionKey(task.getTaskId()))
                .sortKey(SortKeySupplier.getTaskDefinitionSortKey())
                .taskId(task.getTaskId())
                .taskType(task.getTaskType().toString())
                .dataType(DataType.TASK_DEFINITION.name())
                .creationDate(creationTime)
                .fileName(s3.getFileName())
                .bucket(s3.getBucket())
                .build();
    }

    public static S3PutTaskDefinitionDao toS3PutDao(Task task) {
        Instant creationTime = Instant.now();
        S3PutTask s3 = (S3PutTask) task;
        return S3PutTaskDefinitionDao.builder()
                .partitionKey(PartitionKeySupplier.getTaskDefinitionPartitionKey(task.getTaskId()))
                .sortKey(SortKeySupplier.getTaskDefinitionSortKey())
                .taskId(task.getTaskId())
                .taskType(task.getTaskType().toString())
                .dataType(DataType.TASK_DEFINITION.name())
                .creationDate(creationTime)
                .fileName(s3.getFileName())
                .bucket(s3.getBucket())
                .build();
    }

    public static EmailTaskDefinitionDao toEmailDao(Task task) {
        Instant creationTime = Instant.now();
        EmailTask email = (EmailTask) task;
        return EmailTaskDefinitionDao.builder()
                .partitionKey(PartitionKeySupplier.getTaskDefinitionPartitionKey(task.getTaskId()))
                .sortKey(SortKeySupplier.getTaskDefinitionSortKey())
                .taskId(task.getTaskId())
                .taskType(task.getTaskType().toString())
                .dataType(DataType.TASK_DEFINITION.name())
                .creationDate(creationTime)
                .bodyContent(email.getBodyContent())
                .destinationEmail(email.getDestinationEmail())
                .subject(email.getSubject())
                .build();
    }

    public static SmsTaskDefinitionDao toSmsDao(Task task) {
        Instant creationTime = Instant.now();
        SmsTask sms = (SmsTask) task;
        return SmsTaskDefinitionDao.builder()
                .partitionKey(PartitionKeySupplier.getTaskDefinitionPartitionKey(task.getTaskId()))
                .sortKey(SortKeySupplier.getTaskDefinitionSortKey())
                .taskId(task.getTaskId())
                .taskType(task.getTaskType().toString())
                .dataType(DataType.TASK_DEFINITION.name())
                .creationDate(creationTime)
                .destinationNumber(sms.getDestinationNumber())
                .content(sms.getContent())
                .build();
    }

    public static Task toTask(TaskDefinitionDao dao) {
        return switch (TaskType.valueOf(dao.getTaskType())) {
            case EMAIL -> {
                EmailTaskDefinitionDao email = (EmailTaskDefinitionDao) dao;
                yield new EmailTask(
                        null,
                        email.getDestinationEmail(),
                        email.getSubject(),
                        email.getBodyContent());
            }
            case S3_GET -> {
                S3GetTaskDefinitionDao s3 = (S3GetTaskDefinitionDao) dao;
                yield new S3GetTask(
                        null,
                        s3.getBucket(),
                        s3.getFileName());
            }
            case S3_PUT -> {
                S3PutTaskDefinitionDao s3 = (S3PutTaskDefinitionDao) dao;
                yield new S3PutTask(
                        null,
                        s3.getBucket(),
                        s3.getFileName());
            }
            default -> throw new IllegalStateException("Unexpected value: " + TaskType.valueOf(dao.getTaskType()));
        };
    }
}
