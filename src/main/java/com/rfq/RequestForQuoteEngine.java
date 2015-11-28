package com.rfq;

import java.util.List;
import java.util.function.Function;

import static com.rfq.Direction.BUY;
import static com.rfq.Direction.SELL;
import static com.rfq.Order.NO_ORDER;
import static com.rfq.Quote.aQuote;
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

        Quote quote = orders.stream()
                            .filter(order -> order.amount().equals(amount))
                            .filter(order -> order.currency().equals(currency))
                            .collect(collectingAndThen(groupingBy(Order::direction,
                                                                  reducing(NO_ORDER, (a, b) -> a.compareTo(b))),
                                                       result -> {return aQuote(result.getOrDefault(BUY, NO_ORDER).price().adjustUsing(BID_ADJUSTER),
                                                                                result.getOrDefault(SELL, NO_ORDER).price().adjustUsing(ASK_ADJUSTER));}
                            ));

        return quote;
    }
}
