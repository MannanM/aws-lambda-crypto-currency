package com.mannanlive.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mannanlive.btcmarkets.domain.Currency;
import com.mannanlive.btcmarkets.domain.Instrument;
import com.mannanlive.btcmarkets.domain.Market;
import com.mannanlive.btcmarkets.domain.marketdata.MarketValue;
import com.mannanlive.btcmarkets.repository.DefaultUnauthenticatedBtcMarketsDao;
import com.mannanlive.btcmarkets.repository.UnauthenticatedBtcMarketsDao;
import com.mannanlive.domain.MarketValueDynamoDB;
import com.mannanlive.repository.DynamoDBDao;
import com.mannanlive.service.StopLossService;

import java.util.ArrayList;
import java.util.List;

public class StopLossHandler implements RequestHandler<Market, List<MarketValue>> {
    private DynamoDBDao db = new DynamoDBDao();
    private UnauthenticatedBtcMarketsDao dao = new DefaultUnauthenticatedBtcMarketsDao();
    private StopLossService stopLossService = new StopLossService(db);

    public List<MarketValue> handleRequest(Market request, Context context) {
        List<MarketValue> result = new ArrayList<>();

        for (Instrument instrument : Instrument.values()) {
            MarketValue marketValue = dao.getMarketValue(instrument, Currency.AUD);
            db.save(new MarketValueDynamoDB(marketValue));
            result.add(marketValue);

            stopLossService.check(marketValue);
        }
        return result;
    }
}
