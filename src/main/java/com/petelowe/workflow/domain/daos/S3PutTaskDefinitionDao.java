package com.petelowe.workflow.domain.daos;

import com.petelowe.workflow.domain.DataType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

import java.time.Instant;

@SuperBuilder
@DynamoDbBean
@DynamoDbImmutable(builder = S3PutTaskDefinitionDao.S3PutTaskDefinitionDaoBuilder.class)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class S3PutTaskDefinitionDao extends TaskDefinitionDao {
    private final String bucket;
    private final String fileName;

    public S3PutTaskDefinitionDao(String partitionKey,
                                  String sortKey,
                                  String taskId,
                                  String taskType,
                                  Instant creationDate,
                                  DataType dataType,
                                  String bucket,
                                  String fileName) {
        super(partitionKey, sortKey, taskId, taskType, creationDate, dataType);
        this.bucket = bucket;
        this.fileName = fileName;
    }
}
