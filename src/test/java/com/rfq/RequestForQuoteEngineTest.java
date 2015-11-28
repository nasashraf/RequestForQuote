package com.rfq;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.rfq.Currency.GBP;
import static com.rfq.Currency.USD;
import static com.rfq.Direction.BUY;
import static com.rfq.Direction.SELL;
import static com.rfq.LiveOrderServiceBuilder.aLiveOrderService;
import static com.rfq.Quote.NO_QUOTE;
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

        assertThat(quote, is(NO_QUOTE));
    }


    @Test public void
    priceWeBuyAtIsTheValueOfTheProfit_WhenRegisteredClientBuyOrdersPriceIsZero() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(0.04), USD, new Amount(100))
                .withOrder(SELL, new Price(0.04), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.bid(), is(new Price(0.02)));
    }

    @Test public void
    priceWeSellAtIsTheValueOfTheProfit_WhenRegisteredClientSellOrdersIsTwiceTheProfit() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(SELL, new Price(0.00), USD, new Amount(100))
                .withOrder(BUY, new Price(0.00), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.ask(), is(new Price(0.02)));
    }

    @Test public void
    priceWeBuyAtIsRegisteredBuyOrderPlusProfit_WhenOnlySingleClientBuyOrderRegistered() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(100.0), USD, new Amount(100))
                .withOrder(SELL, new Price(100.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.bid(), is(new Price(99.98)));
    }

    @Test public void
    priceWeSellAtIsRegisteredSellOrderMinusProfit_WhenOnlySingleClientBuyOrderRegistered() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(SELL, new Price(100.0), USD, new Amount(100))
                .withOrder(BUY, new Price(100.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.ask(), is(new Price(100.02)));
    }

    @Test public void
    bidPriceIsHighestBuyPrice_WhenMultipleClientBuyOrdersRegistered() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(100.0), USD, new Amount(100))
                .withOrder(BUY, new Price(200.0), USD, new Amount(100))
                .withOrder(SELL, new Price(200.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.bid(), is(new Price(199.98)));
    }

    @Test public void
    askPriceIsLowestSellPrice_WhenMultipleClientSellOrdersRegistered() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(SELL, new Price(200.0), USD, new Amount(100))
                .withOrder(SELL, new Price(100.0), USD, new Amount(100))
                .withOrder(BUY, new Price(100.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.ask(), is(new Price(100.02)));
    }

    @Test public void
    bidPriceIsBuyPrice_WhenSingleClientBuyAndSellOrdersRegistered() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(100.0), USD, new Amount(100))
                .withOrder(SELL, new Price(200.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.bid(), is(new Price(99.98)));
    }

    @Test public void
    askPriceIsSellPrice_WhenSingleClientBuyAndSellOrdersRegistered() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(SELL, new Price(200.0), USD, new Amount(100))
                .withOrder(BUY, new Price(100.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.ask(), is(new Price(200.02)));
    }

    @Test public void
    bidPriceIsHighestBuyPrice_WhenMultipleClientBuyAndSellOrdersRegistered() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(100.0), USD, new Amount(100))
                .withOrder(BUY, new Price(300.0), USD, new Amount(100))
                .withOrder(SELL, new Price(200.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.bid(), is(new Price(299.98)));
    }


    @Test public void
    askPriceIsLowestSellPrice_WhenMulitpleClientBuyAndSellOrdersRegistered() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(SELL, new Price(200.0), USD, new Amount(100))
                .withOrder(SELL, new Price(100.0), USD, new Amount(100))
                .withOrder(BUY, new Price(100.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote.ask(), is(new Price(100.02)));
    }


    @Test public void
    noQuote_WhenNoBuyOrderRegisteredAtAll() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(SELL, new Price(100.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote, is(NO_QUOTE));
    }

    @Test public void
    noQuote_WhenNoSellyOrderRegisteredAtAll() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(100.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote, is(NO_QUOTE));
    }

    @Test public void
    noQuote_WhenNoBuyOrderRegisteredForAmountRequested() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(200.0), USD, new Amount(100))
                .withOrder(SELL, new Price(100.0), USD, new Amount(200))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(200), USD);

        assertThat(quote, is(NO_QUOTE));
    }

    @Test public void
    noQuote_WhenNoSellOrderRegisteredForAmountRequested() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(200.0), USD, new Amount(200))
                .withOrder(SELL, new Price(100.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(200), USD);

        assertThat(quote, is(NO_QUOTE));
    }

    @Test public void
    noQuote_WhenNoBuyOrderRegisteredForCurrencyRequested() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(200.0), GBP, new Amount(100))
                .withOrder(SELL, new Price(100.0), USD, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote, is(NO_QUOTE));
    }

    @Test public void
    noQuote_WhenNoSellOrderRegisteredForCurrencyRequested() {
        liveOrderServiceStub = aLiveOrderService()
                .withOrder(BUY, new Price(200.0), USD, new Amount(100))
                .withOrder(SELL, new Price(100.0), GBP, new Amount(100))
                .build();

        Quote quote = requestForQuoteEngine.request(new Amount(100), USD);

        assertThat(quote, is(NO_QUOTE));
    }

    @Override
    public List<Order> request(Currency currency) {
        return liveOrderServiceStub.request(currency);
    }
}
