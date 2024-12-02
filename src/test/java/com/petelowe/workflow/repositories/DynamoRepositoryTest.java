package com.petelowe.workflow.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petelowe.workflow.config.properties.AwsConfigurationProperties;
import com.petelowe.workflow.domain.daos.DynamoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DynamoRepositoryTest {

    private DynamoRepository dynamoRepository;
    private DynamoDbEnhancedClient enhancedClient;
    private DynamoDbClient dynamoClient;
    private ObjectMapper objectMapper;
    private AwsConfigurationProperties awsConfigProperties;

    @BeforeEach
    void setUp() {
        enhancedClient = mock(DynamoDbEnhancedClient.class);
        dynamoClient = mock(DynamoDbClient.class);
        objectMapper = mock(ObjectMapper.class);
        awsConfigProperties = mock(AwsConfigurationProperties.class);

        when(awsConfigProperties.dynamoDbTableName()).thenReturn("workflow");

        dynamoRepository = new DynamoRepository(enhancedClient, dynamoClient, objectMapper, awsConfigProperties);
    }

    @Test
    void testPutItem() {
        //Given
        DynamoEntity item = mock(DynamoEntity.class);
        DynamoDbTable<DynamoEntity> table = mock(DynamoDbTable.class);

        TableSchema<DynamoEntity> schema = TableSchema.fromImmutableClass(DynamoEntity.class);
        when(enhancedClient.table("workflow", schema)).thenReturn(table);

        //When
        dynamoRepository.put(item, DynamoEntity.class);

        // Assert
        verify(table).putItem(item);
    }

    @Test
    void testGetItem() {
        //Given
        Key key = Key.builder().partitionValue("partitionKey").build();
        DynamoDbTable<DynamoEntity> table = mock(DynamoDbTable.class);
        DynamoEntity expectedItem = mock(DynamoEntity.class);

        TableSchema<DynamoEntity> schema = TableSchema.fromImmutableClass(DynamoEntity.class);
        when(enhancedClient.table("workflow", schema)).thenReturn(table);
        when(table.getItem(key)).thenReturn(expectedItem);

        //When
        Optional<DynamoEntity> result = dynamoRepository.get(key, DynamoEntity.class);

        //Then
        assertTrue(result.isPresent());
        assertEquals(expectedItem, result.get());
    }

    @Test
    void testGetItem_NotFound() {
        //Given
        Key key = Key.builder().partitionValue("partitionKey").build();
        DynamoDbTable<DynamoEntity> table = mock(DynamoDbTable.class);

        TableSchema<DynamoEntity> schema = TableSchema.fromImmutableClass(DynamoEntity.class);
        when(enhancedClient.table("workflow", schema)).thenReturn(table);
        when(table.getItem(key)).thenReturn(null);

        //When
        Optional<DynamoEntity> result = dynamoRepository.get(key, DynamoEntity.class);

        //Then
        assertTrue(result.isEmpty());
    }

    @Test
    void testQueryKeyMatching() {
        //Given
        Key key = Key.builder().partitionValue("partitionKey").build();
        String index = "testIndex";
        DynamoDbTable<DynamoEntity> table = mock(DynamoDbTable.class);
        DynamoDbIndex<DynamoEntity> tableIndex = mock(DynamoDbIndex.class);

        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder().build();
        when(table.index(index)).thenReturn(tableIndex);
        when(tableIndex.query(any(QueryEnhancedRequest.class)))
                .thenReturn((SdkIterable<Page<DynamoEntity>>) Stream.of(Page.create(Collections.singletonList(mock(DynamoEntity.class)))));

        TableSchema<DynamoEntity> schema = TableSchema.fromImmutableClass(DynamoEntity.class);
        when(enhancedClient.table("workflow", schema)).thenReturn(table);

        //When
        Stream<DynamoEntity> resultStream = dynamoRepository.queryKeyMatching(key, DynamoEntity.class, index);
        List<DynamoEntity> resultList = resultStream.toList();

        //Then
        assertEquals(1, resultList.size());
    }
}
