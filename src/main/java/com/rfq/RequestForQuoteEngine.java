package com.rfq;

import java.util.List;

import static com.rfq.Quote.NO_QUOTE;

public class RequestForQuoteEngine {

    private static final Double PRICE_DIFFERENCE = 0.02;

    private final LiveOrderService liveOrderService;

    public RequestForQuoteEngine(LiveOrderService liveOrderBoard) {
        this.liveOrderService = liveOrderBoard;
    }

    public Quote request(Amount amount, Currency currency) {
        List<Order> orders = liveOrderService.request(currency);

        if (orders.isEmpty()) {
            return NO_QUOTE;
        } else {
            Price registeredPrice = orders.get(0).price();
            return new Quote(registeredPrice.adjustBy(price -> price + PRICE_DIFFERENCE), registeredPrice.adjustBy(price -> price - PRICE_DIFFERENCE));
        }

    }
}
