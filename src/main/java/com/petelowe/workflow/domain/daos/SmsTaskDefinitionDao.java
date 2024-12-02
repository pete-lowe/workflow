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
@DynamoDbImmutable(builder = SmsTaskDefinitionDao.SmsTaskDefinitionDaoBuilder.class)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class SmsTaskDefinitionDao extends TaskDefinitionDao {
    private final String destinationNumber;
    private final String content;

    public SmsTaskDefinitionDao(String partitionKey,
                                String sortKey,
                                String taskId,
                                String taskType,
                                Instant creationDate,
                                DataType dataType,
                                String destinationNumber,
                                String content) {
        super(partitionKey, sortKey, taskId, taskType, creationDate, dataType);
        this.destinationNumber = destinationNumber;
        this.content = content;
    }
}
