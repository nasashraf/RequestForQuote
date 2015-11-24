package com.rfq;

import java.util.List;
import java.util.function.Function;

import static com.rfq.Direction.BUY;
import static com.rfq.Direction.SELL;
import static com.rfq.Quote.NO_QUOTE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

public class RequestForQuoteEngine {

    private static final Double PRICE_DIFFERENCE = 0.02;
    private static final Function<Double, Double> ASK_ADJUSTER = price -> price + PRICE_DIFFERENCE;
    private static final Function<Double, Double> BID_ADJUSTER = price -> price - PRICE_DIFFERENCE;

    private final LiveOrderService liveOrderService;

    public RequestForQuoteEngine(LiveOrderService liveOrderBoard) {
        this.liveOrderService = liveOrderBoard;
    }

    public Quote request(Amount amount, Currency currency) {
        List<Order> orders = liveOrderService.request(currency);

        if (orders.isEmpty()) {
            return NO_QUOTE;
        } else {
            Quote quote = orders.stream()
                                .sorted(comparing(Order::price))
                                .collect(collectingAndThen(groupingBy(order -> order.direction(),
                                                                      reducing((a, b) -> a.direction().compare(a, b))),
                                                           result -> {return new Quote(result.get(BUY).get().price().adjustUsing(BID_ADJUSTER), result.get(SELL).get().price().adjustUsing(ASK_ADJUSTER));}
                                ));

            return quote;
        }
    }
}
