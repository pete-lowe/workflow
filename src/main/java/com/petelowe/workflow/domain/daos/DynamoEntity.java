package com.petelowe.workflow.domain.daos;

import com.petelowe.workflow.domain.DataType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import static com.petelowe.workflow.repositories.TaskDefinitionRepository.TASK_DEFINITION_GSI;

@EqualsAndHashCode
@Getter
@SuperBuilder
@ToString
public abstract class DynamoEntity {

    @Getter(onMethod_ = @DynamoDbPartitionKey)
    protected final String partitionKey;
    @Getter(onMethod_ = @DynamoDbSortKey)
    protected final String sortKey;
    @Getter(onMethod_ = @DynamoDbSecondaryPartitionKey(indexNames = {TASK_DEFINITION_GSI}))
    private final String dataType;

    public DynamoEntity(String partitionKey, String sortKey, DataType dataType) {
        this.partitionKey = partitionKey;
        this.sortKey = sortKey;
        this.dataType = dataType.name();
    }

}
