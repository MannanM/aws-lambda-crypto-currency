package com.mannanlive.domain;

import com.mannanlive.btcmarkets.domain.Instrument;
import com.mannanlive.btcmarkets.domain.marketdata.MarketValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static software.amazon.awssdk.services.dynamodb.model.AttributeValue.builder;

public class MarketValueDynamoDB {
    private static final int MILLISECONDS = 1000;
    private static final String ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(ISO);
    private static final String UTC = "UTC";

    private final Map<String,AttributeValue> values = new HashMap<>();

    public MarketValueDynamoDB(MarketValue marketValue) {
        createValues(marketValue, "");
    }

    public MarketValueDynamoDB(MarketValue marketValue, String suffix) {
        createValues(marketValue, suffix);
    }

    private void createValues(MarketValue marketValue, String suffix) {
        values.put("crypto", builder().s(marketValue.getInstrument().name() + suffix).build());
        values.put("ask", builder().n(format(marketValue.getBestAsk())).build());
        values.put("bid", builder().n(format(marketValue.getBestBid())).build());
        values.put("last", builder().n(format(marketValue.getLastPrice())).build());
        values.put("dateTime", builder().s(format(marketValue.getTimestamp())).build());
    }

    public static MarketValue translate(Map<String, AttributeValue> item) {
        MarketValue marketValue = new MarketValue();
        marketValue.setInstrument(Instrument.valueOf(item.get("crypto").s().split("-")[0]));
        marketValue.setBestAsk(Float.valueOf(item.get("ask").n()));
        marketValue.setBestBid(Float.valueOf(item.get("bid").n()));
        marketValue.setLastPrice(Float.valueOf(item.get("last").n()));
        Date dateTime = null;
        try {
            dateTime = dateFormat.parse(item.get("dateTime").s());
            marketValue.setTimestamp(dateTime.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return marketValue;
    }

    private String format(long timestamp) {
        return format(new Date(timestamp * MILLISECONDS));
    }

    private String format(Date time) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
        return dateFormat.format(time);
    }

    private String format(Float value) {
        return value.toString();
    }

    public Map<String, AttributeValue> getValues() {
        return values;
    }

    public Map<String, AttributeValue> getKeyValues() {
        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put("crypto", values.get("crypto"));
        attrValues.put("dateTime", values.get("dateTime"));
        return attrValues;
    }

    public Map<String, AttributeValueUpdate> getUpdateValues() {
        Map<String, AttributeValueUpdate> attrValues = new HashMap<>();
        attrValues.put("ask", AttributeValueUpdate.builder().value(values.get("ask")).build());
        attrValues.put("bid", AttributeValueUpdate.builder().value(values.get("bid")).build());
        attrValues.put("last", AttributeValueUpdate.builder().value(values.get("last")).build());
        return attrValues;
    }
}
