package com.mannanlive.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mannanlive.domain.Market;
import com.mannanlive.domain.MarketValue;
import com.mannanlive.repository.BtcMarketsDao;

public class MarketValueHandler implements RequestHandler<Market, MarketValue> {
    public MarketValue handleRequest(Market request, Context context) {
        if (request == null || request.getInstrument() == null || request.getCurrency() == null) {
            request = new Market("BTC", "AUD");
        }
        return new BtcMarketsDao().getMarketValue(request);
    }
}
