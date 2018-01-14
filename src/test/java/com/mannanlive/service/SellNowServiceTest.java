package com.mannanlive.service;

import com.mannanlive.btcmarkets.domain.Instrument;
import com.mannanlive.btcmarkets.domain.account.CurrencyBalance;
import com.mannanlive.btcmarkets.domain.trading.PlaceOrderResponse;
import com.mannanlive.btcmarkets.domain.trading.order.Order;
import com.mannanlive.btcmarkets.repository.BtcMarketsDao;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(EasyMockRunner.class)
public class SellNowServiceTest extends EasyMockSupport {
    @TestSubject
    private SellNowService service = new SellNowService();

    @Mock
    private BtcMarketsDao dao;

    @Test
    public void sellAll() {
        CurrencyBalance etc = createBalance("ETC", 99);
        CurrencyBalance btc = createBalance("BTC", 100);
        CurrencyBalance bch = createBalance("BCH", 100);
        PlaceOrderResponse response = new PlaceOrderResponse();
        response.setSuccess(true);

        expect(dao.getAccountBalance()).andReturn(Arrays.asList(etc, btc, bch));
        expect(dao.placeOrder(isA(Order.class))).andReturn(response);

        replayAll();
        assertThat(service.sellAll(Instrument.BTC, 100f), equalTo(true));
        verifyAll();
    }

    @Test
    public void sellAllReturnsFalseWhenPlaceOrderReturnsFalse() {
        PlaceOrderResponse response = new PlaceOrderResponse();
        response.setSuccess(false);

        expect(dao.getAccountBalance()).andReturn(Collections.singletonList(createBalance("BTC", 100)));
        expect(dao.placeOrder(isA(Order.class))).andReturn(response);

        replayAll();
        assertThat(service.sellAll(Instrument.BTC, 100f), equalTo(false));
        verifyAll();
    }

    @Test
    public void sellAllReturnsFalseWhenBalanceIsZero() {
        expect(dao.getAccountBalance()).andReturn(Collections.singletonList(createBalance("BTC", 0)));

        replayAll();
        assertThat(service.sellAll(Instrument.BTC, 100f), equalTo(false));
        verifyAll();
    }

    @Test
    public void sellAllReturnsFalseWhenCurrencyIsntFound() {
        expect(dao.getAccountBalance()).andReturn(Collections.singletonList(createBalance("XXX", 0)));

        replayAll();
        assertThat(service.sellAll(Instrument.BTC, 100f), equalTo(false));
        verifyAll();
    }

    private CurrencyBalance createBalance(String currency, int balance) {
        CurrencyBalance result = new CurrencyBalance();
        result.setBalance(balance);
        result.setCurrency(currency);
        return result;
    }
}
