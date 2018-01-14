package com.mannanlive.service;

import com.mannanlive.btcmarkets.domain.Currency;
import com.mannanlive.btcmarkets.domain.Instrument;
import com.mannanlive.btcmarkets.domain.marketdata.MarketValue;
import com.mannanlive.domain.MarketValueDynamoDB;
import com.mannanlive.repository.DynamoDBDao;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

@RunWith(EasyMockRunner.class)
public class StopLossServiceTest extends EasyMockSupport {
    private StopLossService service;

    @Mock
    private DynamoDBDao dynamo;

    @Mock
    private SellNowService sellNowService;

    @Before
    public void setUp() {
        service = new StopLossService(dynamo, sellNowService);
    }

    @Test
    public void checkDoesNothingWhenDaoDoesntFindAnything() {
        expect(dynamo.get(Instrument.BTC, "-stop")).andReturn(null);

        replayAll();
        service.check(createMarketValue(100f, 95f));
        verifyAll();
    }

    @Test
    public void checkDoesNothingWhenSameValue() {
        expect(dynamo.get(Instrument.BTC, "-stop")).andReturn(createMarketValue(100f, 95f));

        replayAll();
        service.check(createMarketValue(100f, 95f));
        verifyAll();
    }

    @Test
    public void checkUpdatesStopLossWhenCurrentValueIsHigherThanCurrent() {
        expect(dynamo.get(Instrument.BTC, "-stop")).andReturn(createMarketValue(100f, 95f));
        dynamo.update(isA(MarketValueDynamoDB.class));

        replayAll();
        service.check(createMarketValue(106f, 95f));
        verifyAll();
    }

    @Test
    public void checkSellsWhenBelowValue() {
        expect(dynamo.get(Instrument.BTC, "-stop")).andReturn(createMarketValue(200f, 50f));
        expect(sellNowService.sellAll(Instrument.BTC, 100f)).andReturn(true);
        dynamo.delete(isA(MarketValueDynamoDB.class));

        replayAll();
        service.check(createMarketValue(100f, 95f));
        verifyAll();
    }

    @Test
    public void checkDoesNotDeleteWhenSellServiceFails() {
        expect(dynamo.get(Instrument.BTC, "-stop")).andReturn(createMarketValue(200f, 50f));
        expect(sellNowService.sellAll(Instrument.BTC, 100f)).andReturn(false);

        replayAll();
        service.check(createMarketValue(100f, 95f));
        verifyAll();
    }

    @Test
    public void checkDoesntSellsWhenBelowValueButBidIsHigher() {
        expect(dynamo.get(Instrument.BTC, "-stop")).andReturn(createMarketValue(200f, 101f));

        replayAll();
        service.check(createMarketValue(100f, 95f));
        verifyAll();
    }

    private MarketValue createMarketValue(float ask, float bid) {
        MarketValue currentValue = new MarketValue();
        currentValue.setInstrument(Instrument.BTC);
        currentValue.setCurrency(Currency.AUD);
        currentValue.setBestAsk(ask);
        currentValue.setBestBid(bid);
        currentValue.setLastPrice(97.5f);
        currentValue.setTimestamp(new Date().getTime());
        return currentValue;
    }
}
