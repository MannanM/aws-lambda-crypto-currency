package com.mannanlive.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mannanlive.btcmarkets.domain.Currency;
import com.mannanlive.btcmarkets.domain.Instrument;
import com.mannanlive.btcmarkets.domain.Market;
import com.mannanlive.btcmarkets.domain.marketdata.MarketValue;
import com.mannanlive.btcmarkets.repository.DefaultUnauthenticatedBtcMarketsDao;

public class MarketValueHandler implements RequestHandler<Market, MarketValue> {
    public MarketValue handleRequest(Market request, Context context) {
        if (request == null || request.getInstrument() == null || request.getCurrency() == null) {
            request = new Market(Instrument.BTC, Currency.AUD);
        }

        return new DefaultUnauthenticatedBtcMarketsDao().getMarketValue(request.getInstrument(), request.getCurrency());
    }
}
