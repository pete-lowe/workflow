package com.petelowe.workflow.domain.daos;

import com.petelowe.workflow.domain.DataType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

import java.time.Instant;
import java.util.List;

@SuperBuilder
@DynamoDbBean
@DynamoDbImmutable(builder = WorkflowDao.WorkflowDaoBuilder.class)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class WorkflowDao extends DynamoEntity {

    private final String workflowId;
    private final Instant creationDate;
    private final String workflowName;

    //TODO: dont do this, will change
    private final String taskIds;


    public WorkflowDao(String partitionKey,
                       String sortKey,
                       String workflowId,
                       Instant creationDate,
                       String workflowName,
                       String taskIds) {
        super(partitionKey, sortKey, DataType.WORKFLOW);
        this.workflowId = workflowId;
        this.creationDate = creationDate;
        this.workflowName = workflowName;
        this.taskIds = taskIds;
    }
}
