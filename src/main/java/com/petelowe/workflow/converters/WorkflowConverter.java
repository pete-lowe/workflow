package com.petelowe.workflow.converters;

import com.petelowe.workflow.domain.DataType;
import com.petelowe.workflow.domain.Task;
import com.petelowe.workflow.domain.Workflow;
import com.petelowe.workflow.domain.daos.WorkflowDao;
import com.petelowe.workflow.suppliers.PartitionKeySupplier;
import com.petelowe.workflow.suppliers.SortKeySupplier;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
public class WorkflowConverter {

    public static WorkflowDao toDao(Workflow workflow) {
        Instant creationTime = Instant.now();
        String workflowId = UUID.randomUUID().toString();
        return WorkflowDao.builder()
                .partitionKey(PartitionKeySupplier.getWorkflowPartitionKey(workflowId))
                .sortKey(SortKeySupplier.getWorkflowSortKey())
                .workflowName(workflow.workflowName())
                .workflowId(workflowId)
                .taskIds(String.join(",", workflow.tasks().stream().map(Task::getTaskId).toList()))
                .creationDate(creationTime)
                .dataType(DataType.WORKFLOW.name())
                .build();
    }

    public static Workflow toDto(WorkflowDao workflowDao, List<Task> tasks) {
        return new Workflow(
                workflowDao.getWorkflowName(),
                workflowDao.getWorkflowId(),
                tasks
        );
    }


}
