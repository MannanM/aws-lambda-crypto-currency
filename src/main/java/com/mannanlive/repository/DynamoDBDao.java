package com.mannanlive.repository;

import com.mannanlive.btcmarkets.domain.Instrument;
import com.mannanlive.btcmarkets.domain.marketdata.MarketValue;
import com.mannanlive.domain.MarketValueDynamoDB;
import software.amazon.awssdk.core.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDBClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBDao {
    public static final String CRYPTO_VALUE = "crypto-value";
    private DynamoDBClient client = DynamoDBClient.builder()
            .region(Region.AP_SOUTHEAST_2)
            .build();

    public void save(MarketValueDynamoDB marketValue) {
        client.putItem(PutItemRequest.builder()
                .tableName(CRYPTO_VALUE)
                .item(marketValue.getValues())
                .build());
    }

    public MarketValue get(Instrument instrument, String suffix) {
        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put("crypto", AttributeValue.builder().s(instrument.name() + suffix).build());
        attrValues.put("dateTime", AttributeValue.builder().s("1970-01-01T00:00:00.000Z").build());
        GetItemResponse item = client.getItem(GetItemRequest.builder().tableName(CRYPTO_VALUE).key(attrValues).build());
        Map<String, AttributeValue> data = item.item();
        if (data == null) {
            return null;
        }
        return MarketValueDynamoDB.translate(data);
    }

    public void update(MarketValueDynamoDB dynamoDB) {
        client.updateItem(UpdateItemRequest.builder()
                .tableName(CRYPTO_VALUE)
                .key(dynamoDB.getKeyValues())
                .attributeUpdates(dynamoDB.getUpdateValues())
                .build());
    }

    public void delete(MarketValueDynamoDB dynamoDB) {
        client.deleteItem(DeleteItemRequest.builder()
                .tableName(CRYPTO_VALUE)
                .key(dynamoDB.getKeyValues())
                .build());
    }

    public List<MarketValue> getAll(Instrument instrument) {
        List<Map<String, AttributeValue>> items = getAll(instrument.name());
        List<MarketValue> values = new ArrayList<>();
        for (Map<String, AttributeValue> item : items) {
            MarketValue value = MarketValueDynamoDB.translate(item);
            values.add(value);
        }

        return values;
    }

    private List<Map<String, AttributeValue>> getAll(String partition_key_val) {
        String partition_key_name = "crypto";
        String partition_alias = "#a";

        System.out.format("Querying %s", CRYPTO_VALUE);
        System.out.println("");

        //set up an alias for the partition key name in case it's a reserved word
        Map<String, String> attrNameAlias = new HashMap<>();
        attrNameAlias.put(partition_alias, partition_key_name);

        //set up mapping of the partition name with the value
        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":" + partition_key_name, AttributeValue.builder().s(partition_key_val).build());

        QueryRequest queryReq = QueryRequest.builder()
                .tableName(CRYPTO_VALUE)
                .keyConditionExpression(partition_alias + " = :" + partition_key_name)
                .expressionAttributeNames(attrNameAlias)
                .expressionAttributeValues(attrValues)
                .build();

        return client.query(queryReq).items();
    }
}
