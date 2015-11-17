package com.rfq;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.rfq.Direction.BUY;
import static com.rfq.Direction.SELL;
import static com.rfq.Quote.NO_QUOTE;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

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
            Map<Direction, List<Order>> ordersPerDirection = orders.stream()
                                                                .sorted(comparing(Order::price))
                                                                .collect(groupingBy(Order::direction));

            Order highestPricedBuy = ordersPerDirection.get(BUY).get(ordersPerDirection.get(BUY).size() - 1);
            Order lowestPricedSell = ordersPerDirection.get(SELL).get(0);

            return new Quote(highestPricedBuy.price().adjustUsing(BID_ADUSTER), lowestPricedSell.price().adjustUsing(ASK_ADJSUTER));
        }
    }
}
