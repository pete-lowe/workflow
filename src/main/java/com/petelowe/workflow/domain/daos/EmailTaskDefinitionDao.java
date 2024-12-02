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
@DynamoDbImmutable(builder = EmailTaskDefinitionDao.EmailTaskDefinitionDaoBuilder.class)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class EmailTaskDefinitionDao extends TaskDefinitionDao {
    private final String destinationEmail;
    private final String subject;
    private final String bodyContent;

    public EmailTaskDefinitionDao(String partitionKey,
                                  String sortKey,
                                  String taskId,
                                  String taskType,
                                  Instant creationDate,
                                  DataType dataType,
                                  String destinationEmail,
                                  String subject,
                                  String bodyContent) {
        super(partitionKey, sortKey, taskId, taskType, creationDate, dataType);
        this.destinationEmail = destinationEmail;
        this.subject = subject;
        this.bodyContent = bodyContent;
    }
}
