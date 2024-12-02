package com.petelowe.workflow.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petelowe.workflow.config.properties.AwsConfigurationProperties;
import com.petelowe.workflow.domain.daos.DynamoEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public class DynamoRepository {

    private final String tableName;
    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbClient client;
    private final ObjectMapper objectMapper;

    public DynamoRepository(DynamoDbEnhancedClient enhancedClient,
                            DynamoDbClient client,
                            ObjectMapper objectMapper,
                            AwsConfigurationProperties awsConfigProperties) {
        this.tableName = awsConfigProperties.dynamoDbTableName();
        this.enhancedClient = enhancedClient;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public <T extends DynamoEntity> void put(T item, Class type) {
        table(tableSchema(type)).putItem(item);
    }

    public <T> Optional<T> get(Key key, Class<T> type) {
        return Optional.ofNullable(table(tableSchema(type)).getItem(key));
    }

    public <T> Stream<T> queryKeyMatching(Key key, Class<T> type, String index) {
        return table(tableSchema(type)).index(index).query(getKeyWithValue(key))
                .stream()
                .map(Page::items)
                .flatMap(List::stream);
    }

    private QueryEnhancedRequest getKeyWithValue(Key key) {
        return QueryEnhancedRequest
                .builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .build();
    }

    private <T> DynamoDbTable<T> table(TableSchema<T> schema) {
        return enhancedClient.table(tableName, schema);
    }

    private static <T> TableSchema<T> tableSchema(Class<T> type) {
        return TableSchema.fromImmutableClass(type);
    }
}
