package com.mannanlive.domain;

import com.mannanlive.btcmarkets.domain.marketdata.MarketValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder;

public class MarketValueDynamoDB {
    private static final int MILLISECONDS = 1000;
    private static final String ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    private static final String UTC = "UTC";

    private final Map<String,AttributeValue> values = new HashMap<>();

    public MarketValueDynamoDB(MarketValue marketValue) {
        values.put("crypto", builder().s(marketValue.getInstrument().name()).build());
        values.put("ask", builder().n(format(marketValue.getBestAsk())).build());
        values.put("bid", builder().n(format(marketValue.getBestBid())).build());
        values.put("last", builder().n(format(marketValue.getLastPrice())).build());
        values.put("dateTime", builder().s(format(marketValue.getTimestamp())).build());
    }

    private String format(long timestamp) {
        return format(new Date(timestamp * MILLISECONDS));
    }

    private String format(Date time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ISO);
        dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
        return dateFormat.format(time);
    }

    private String format(Float value) {
        return value.toString();
    }

    public Map<String, AttributeValue> getValues() {
        return values;
    }
}
