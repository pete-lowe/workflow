package com.petelowe.workflow.repositories;

import com.petelowe.workflow.domain.Task;
import com.petelowe.workflow.domain.TaskId;
import com.petelowe.workflow.domain.TaskType;
import com.petelowe.workflow.domain.daos.*;
import com.petelowe.workflow.suppliers.PartitionKeySupplier;
import com.petelowe.workflow.suppliers.SortKeySupplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.Optional;
import java.util.stream.Stream;

import static com.petelowe.workflow.converters.TaskConverter.toDao;

@Repository
@Slf4j
public class TaskDefinitionRepository {

    public static final String TASK_TYPE_GSI = "taskTypeGsi";
    public static final String TASK_DEFINITION_GSI = "taskDefinitionGsi";

    private final DynamoRepository dynamoRepository;

    public TaskDefinitionRepository(DynamoRepository dynamoRepository) {
        this.dynamoRepository = dynamoRepository;
    }

    public void saveTask(Task task) {
        switch (task.getTaskType()) {
            case S3_GET -> dynamoRepository.put(toDao(task), S3GetTaskDefinitionDao.class);
            case EMAIL -> dynamoRepository.put(toDao(task), EmailTaskDefinitionDao.class);
            case S3_PUT -> dynamoRepository.put(toDao(task), S3PutTaskDefinitionDao.class);
            case SMS -> dynamoRepository.put(toDao(task), SmsTaskDefinitionDao.class);
        }
    }

    public Optional<TaskDefinitionDao> getTaskById(TaskId taskId) {
        //TODO: silly but need to sort of generic type mappings
        var dao = dynamoRepository.get(
                Key.builder()
                        .partitionValue(PartitionKeySupplier.getTaskDefinitionPartitionKey(taskId.id()))
                        .sortValue(SortKeySupplier.getTaskDefinitionSortKey())
                        .build(),
                TaskDefinitionDao.class);

        return dao.map(daoValue ->
                dynamoRepository.get(
                        Key.builder()
                                .partitionValue(PartitionKeySupplier.getTaskDefinitionPartitionKey(taskId.id()))
                                .sortValue(SortKeySupplier.getTaskDefinitionSortKey())
                                .build(),
                        getDaoType(TaskType.valueOf(daoValue.getTaskType()))
                )
        ).orElse(Optional.empty());
    }

    public Stream<TaskDefinitionDao> getTaskDefinitionsByType(TaskType taskType) {
        return dynamoRepository.queryKeyMatching(
                Key.builder()
                        .partitionValue(taskType.name())
                        .build(),
                getDaoType(taskType),
                TASK_TYPE_GSI);
    }

    private Class getDaoType(TaskType taskType) {
        return switch (taskType) {
            case S3_GET -> S3GetTaskDefinitionDao.class;
            case S3_PUT -> S3PutTaskDefinitionDao.class;
            case SMS -> SmsTaskDefinitionDao.class;
            case EMAIL -> EmailTaskDefinitionDao.class;
            default -> throw new IllegalStateException("Unexpected value: " + taskType);
        };
    }


}
