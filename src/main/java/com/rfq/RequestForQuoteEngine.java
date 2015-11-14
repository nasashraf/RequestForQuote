package com.rfq;

import java.util.List;
import java.util.function.Function;

import static com.rfq.Quote.NO_QUOTE;

public class RequestForQuoteEngine {

    private static final Double PRICE_DIFFERENCE = 0.02;
    private static final Function<Double, Double> ASK_ADJSUTER = price -> price + PRICE_DIFFERENCE;
    private static final Function<Double, Double> BID_ADUSTER = price -> price - PRICE_DIFFERENCE;

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
            return new Quote(registeredPrice.adjustUsing(BID_ADUSTER), registeredPrice.adjustUsing(ASK_ADJSUTER));
        }
    }
}
