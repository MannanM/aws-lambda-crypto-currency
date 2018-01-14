package com.mannanlive.service;

import com.mannanlive.btcmarkets.domain.Currency;
import com.mannanlive.btcmarkets.domain.Instrument;
import com.mannanlive.btcmarkets.domain.account.CurrencyBalance;
import com.mannanlive.btcmarkets.domain.trading.order.Order;
import com.mannanlive.btcmarkets.domain.trading.order.OrderSide;
import com.mannanlive.btcmarkets.domain.trading.order.OrderType;
import com.mannanlive.btcmarkets.repository.BtcMarketsDao;
import com.mannanlive.btcmarkets.repository.DefaultBtcMarketsDao;

import java.util.List;

import static com.mannanlive.btcmarkets.domain.trading.order.Order.RATIO;
import static java.util.Arrays.asList;

public class SellNowService {
    private static final List<Instrument> marketInstruments = asList(Instrument.BTC, Instrument.ETH, Instrument.LTC);
    private static final String STOP_LOSS = "Stop-Loss";
    private BtcMarketsDao dao;

    public SellNowService() {
        dao = new DefaultBtcMarketsDao(System.getenv("btcApi"), System.getenv("btcPrivate"));
    }

    public SellNowService(BtcMarketsDao dao) {
        this.dao = dao;
    }

    public boolean sellAll(Instrument instrument, float currentValue) {
        long balance = getBalance(instrument);
        if (balance > 0) {
            Order order = new Order();
            order.setCurrency(Currency.AUD);
            order.setSide(OrderSide.ASK);
            order.setClientRequestId(STOP_LOSS);
            order.setInstrument(instrument);
            order.setPrice((long) (currentValue * RATIO * .99));
            order.setType(getOrderType(instrument));
            return dao.placeOrder(order).isSuccess();
        }
        return false;
    }

    private long getBalance(Instrument instrument) {
        List<CurrencyBalance> accountBalance = dao.getAccountBalance();
        for (CurrencyBalance balance : accountBalance) {
            if (balance.getCurrency().equals(instrument.name())) {
                return balance.getBalance();
            }
        }
        return 0L;
    }

    private OrderType getOrderType(Instrument instrument) {
        if (marketInstruments.contains(instrument)) {
            return OrderType.MARKET;
        }
        return OrderType.LIMIT;
    }
}
