package com.mannanlive.service;

import com.mannanlive.btcmarkets.domain.Currency;
import com.mannanlive.btcmarkets.domain.Instrument;
import com.mannanlive.btcmarkets.domain.marketdata.MarketValue;
import com.mannanlive.domain.MarketValueDynamoDB;
import com.mannanlive.repository.DynamoDBDao;

import static java.lang.String.format;

public class StopLossService {
    private static final float STOP_LIMIT_PERCENT = .95f;
    private static final String STOP_LOSS_SUFFIX = "-stop";
    private final DynamoDBDao dao;
    private final SellNowService service;

    public StopLossService(DynamoDBDao db) {
        dao = db;
        service = new SellNowService();
    }

    public StopLossService(DynamoDBDao dao, SellNowService service) {
        this.dao = dao;
        this.service = service;
    }

    public void check(MarketValue currentValue) {
        MarketValue stopLoss = dao.get(currentValue.getInstrument(), STOP_LOSS_SUFFIX);
        if (stopLoss == null) {
            System.out.println("No Stop Loss order found for " + currentValue.getInstrument().name());
            return;
        }

        float currentMarketValue = currentValue.getBestAsk();

        //below stop loss value
        if (currentMarketValue < stopLoss.getBestAsk()) {
            valueIsBelowStopLoss(currentValue, stopLoss, currentMarketValue);
        } else {
            valueIsAboveStopLoss(stopLoss, currentMarketValue);
        }
    }

    private void valueIsBelowStopLoss(MarketValue currentValue, MarketValue stopLoss, float currentMarketValue) {
        System.out.println(format("Current value $%f%s is below Stop Loss of %f", currentMarketValue,
                currentValue.getInstrument().name(), stopLoss.getBestAsk()));
        //above minimum selling point
        if (currentMarketValue > stopLoss.getBestBid()) {
            sellNow(currentValue, stopLoss, currentMarketValue);
        } else {
            System.out.println(format("However Stop Loss reserve of $%f is not met", stopLoss.getBestBid()));
        }
    }

    private void sellNow(MarketValue currentValue, MarketValue stopLoss, float currentMarketValue) {
        if (service.sellAll(currentValue.getInstrument(), currentMarketValue)) {
            System.out.println(format("Place order to sell all %s!", currentValue.getInstrument().name()));
            dao.delete(new MarketValueDynamoDB(stopLoss, STOP_LOSS_SUFFIX));
        } else {
            System.out.println("Error when attempting to sell cyrpto");
        }
    }

    private void valueIsAboveStopLoss(MarketValue stopLoss, float currentMarketValue) {
        System.out.println(format("Current value $%f%s is above Stop Loss of $%f", currentMarketValue,
                stopLoss.getInstrument().name(), stopLoss.getBestAsk()));
        float newStopLoss = currentMarketValue * STOP_LIMIT_PERCENT;

        if (newStopLoss > stopLoss.getBestAsk()) {
            System.out.println(format("%s Stop Loss has been increased to $%f",
                    stopLoss.getInstrument().name(), newStopLoss));
            stopLoss.setBestAsk(newStopLoss);

            dao.update(new MarketValueDynamoDB(stopLoss, STOP_LOSS_SUFFIX));
        }
    }

    public static void main(String... args) {
        createStopLoss(Instrument.BCH, 3738.0f);
    }

    private static void createStopLoss(Instrument instrument, float minimum) {
        DynamoDBDao db = new DynamoDBDao();
        MarketValue newStopLoss = new MarketValue();
        newStopLoss.setCurrency(Currency.AUD);
        newStopLoss.setInstrument(instrument);
        newStopLoss.setTimestamp(0);
        newStopLoss.setBestBid(minimum);
        newStopLoss.setBestAsk(0f);
        db.save(new MarketValueDynamoDB(newStopLoss, STOP_LOSS_SUFFIX));
    }
}
