package com.petelowe.workflow.domain.daos;

import com.petelowe.workflow.domain.DataType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

import java.time.Instant;

import static com.petelowe.workflow.repositories.TaskDefinitionRepository.TASK_DEFINITION_GSI;
import static com.petelowe.workflow.repositories.TaskDefinitionRepository.TASK_TYPE_GSI;

@SuperBuilder
@DynamoDbBean
@DynamoDbImmutable(builder = TaskDefinitionDao.TaskDefinitionDaoBuilder.class)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class TaskDefinitionDao extends DynamoEntity {

    private final String taskId;

    @Getter(onMethod_ = @DynamoDbSecondaryPartitionKey(indexNames = {TASK_TYPE_GSI}))
    private final String taskType;

    @Getter(onMethod_ = @DynamoDbSecondarySortKey(indexNames = {TASK_TYPE_GSI, TASK_DEFINITION_GSI}))
    private final Instant creationDate;

    public TaskDefinitionDao(String partitionKey,
                             String sortKey,
                             String taskId,
                             String taskType,
                             Instant creationDate,
                             DataType dataType) {
        super(partitionKey, sortKey, dataType);
        this.taskId = taskId;
        this.taskType = taskType;
        this.creationDate = creationDate;
    }
}
