package com.rfq;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.rfq.Currency.USD;
import static com.rfq.Direction.BUY;
import static com.rfq.LiveOrderServiceBuilder.aLiveOrderService;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class RequestForQuoteEngineTest implements LiveOrderService {

    private RequestForQuoteEngine requestForQuoteEngine;
    private LiveOrderService liveOrderServiceStub;

    @Before
    public void createSUT() {
        requestForQuoteEngine = new RequestForQuoteEngine(this);
    }

    @Test public void
    noQuote_WhenNoClientOrdersRegistered() {
        liveOrderServiceStub = aLiveOrderService().build();
        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote, is(Quote.NO_QUOTE));
    }


    @Test public void
    priceWeBuyAtIsTheValueOfTheProfit_WhenRegisteredClientBuyOrdersPriceIsZero() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(0.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.bid(), is(new Price(0.02)));
    }

    @Test public void
    priceWeSellAtIsTheValueOfTheProfit_WhenRegisteredClientSellOrdersIsTwiceTheProfit() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(Direction.SELL, new Price(0.04), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.ask(), is(new Price(0.02)));
    }

    @Test public void
    priceWeBuyAtIsRegisteredBuyOrderPlusProfit_WhenOnlySingleClientBuyOrderRegistered() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(100.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.bid(), is(new Price(100.02)));
    }

    @Test public void
    priceWeSellAtIsRegisteredSellOrderMinusProfit_WhenOnlySingleClientBuyOrderRegistered() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(100.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.ask(), is(new Price(99.98)));
    }

    @Override
    public List<Order> request(Currency currency) {
        return liveOrderServiceStub.request(currency);
    }
}
