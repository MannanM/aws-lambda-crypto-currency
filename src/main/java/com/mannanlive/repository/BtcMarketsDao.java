package com.mannanlive.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mannanlive.domain.Market;
import com.mannanlive.domain.MarketValue;

import java.net.URL;

import static java.lang.String.format;

public class BtcMarketsDao {
    private static final String HTTPS_API_BTCMARKETS_NET = "https://api.btcmarkets.net";

    public MarketValue getMarketValue(Market req) {
        try {
            String url = format("%s/market/%s/%s/tick", HTTPS_API_BTCMARKETS_NET, req.getInstrument(), req.getCurrency());
            URL src = new URL(url);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(src, MarketValue.class);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}