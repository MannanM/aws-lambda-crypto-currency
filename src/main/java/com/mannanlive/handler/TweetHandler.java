package com.mannanlive.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mannanlive.btcmarkets.domain.Market;
import com.mannanlive.btcmarkets.domain.marketdata.MarketValue;
import com.mannanlive.domain.MarketValueTweet;
import com.mannanlive.repository.TwitterDao;

public class TweetHandler implements RequestHandler<Market, MarketValue> {
    public MarketValue handleRequest(Market request, Context context) {
        MarketValue marketValue = new MarketValueHandler().handleRequest(request, context);

        if (marketValue != null) {
            MarketValueTweet tweet = new MarketValueTweet(marketValue);
            new TwitterDao().tweet(tweet.getStatus());
        }

        return marketValue;
    }
}
