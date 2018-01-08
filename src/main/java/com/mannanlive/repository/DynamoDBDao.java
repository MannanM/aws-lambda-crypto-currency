package com.mannanlive.repository;

import com.mannanlive.btcmarkets.domain.marketdata.MarketValue;
import com.mannanlive.domain.MarketValueDynamoDB;
import software.amazon.awssdk.core.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDBClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder;

public class DynamoDBDao {
    private DynamoDBClient client = DynamoDBClient.builder()
            .region(Region.AP_SOUTHEAST_2)
            .build();

    public void save(MarketValueDynamoDB marketValue) {
        client.putItem(PutItemRequest.builder()
                .tableName("crypto-value")
                .item(marketValue.getValues()).build());
    }
}
