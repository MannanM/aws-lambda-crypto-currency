package com.mannanlive.repository;

import com.mannanlive.domain.Market;
import com.mannanlive.domain.MarketValue;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BtcMarketsDaoTest {
    @Test
    public void testGetMarketValue() throws Exception {
        MarketValue actual = new BtcMarketsDao().getMarketValue(new Market("BTC", "AUD"));
        assertEquals("BTC", actual.getInstrument());
        assertEquals("AUD", actual.getCurrency());
        assertTrue(actual.getBestAsk() > 1000f);
        assertTrue(actual.getBestBid() > 1000f);
        assertTrue(actual.getLastPrice() > 1000f);
        assertTrue(actual.getVolume24h() > 10f);
        assertTrue(actual.getTimestamp() <= new Date().getTime());
    }
}