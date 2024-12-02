package com.petelowe.workflow.repositories;

import com.petelowe.workflow.domain.Workflow;
import com.petelowe.workflow.domain.WorkflowId;
import com.petelowe.workflow.domain.daos.TaskDefinitionDao;
import com.petelowe.workflow.domain.daos.WorkflowDao;
import com.petelowe.workflow.suppliers.PartitionKeySupplier;
import com.petelowe.workflow.suppliers.SortKeySupplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;

@Repository
@Slf4j
public class WorkflowRepository {

    private final DynamoRepository dynamoRepository;

    public WorkflowRepository(DynamoRepository dynamoRepository) {
        this.dynamoRepository = dynamoRepository;
    }

    public void saveWorkFlow(WorkflowDao workflowDao) {
        dynamoRepository.put(workflowDao, WorkflowDao.class);
    }

    public Optional<WorkflowDao> getWorkflow(WorkflowId workflowId) {
        return dynamoRepository.get(
                Key.builder()
                        .partitionValue(PartitionKeySupplier.getWorkflowPartitionKey(workflowId.id()))
                        .sortValue(SortKeySupplier.getWorkflowSortKey())
                        .build(),
                WorkflowDao.class);
    }

}
