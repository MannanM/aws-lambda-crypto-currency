package com.mannanlive.domain;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MarketValueTweetTest {
    @Test
    public void getStatus() {
        MarketValue marketValue = new MarketValue();
        marketValue.setCurrency("AUD");
        marketValue.setInstrument("BTC");
        marketValue.setLastPrice(23456.23f);
        marketValue.setTimestamp(1476243360);

        MarketValueTweet marketValueTweet = new MarketValueTweet(marketValue);
        assertThat(marketValueTweet.getStatus(),
                is("The current value of BTC at 23:36:00 on 12/10/2016 (AEST) is $23,456.23 AUD.\n#bitcoin #australia"));
    }
}
