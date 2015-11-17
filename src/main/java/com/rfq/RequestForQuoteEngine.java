package com.rfq;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.rfq.Direction.BUY;
import static com.rfq.Direction.SELL;
import static com.rfq.Price.NO_PRICE;
import static com.rfq.Quote.NO_QUOTE;
import static java.util.Collections.max;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.maxBy;

public class RequestForQuoteEngine {

    private static final Double PRICE_DIFFERENCE = 0.02;
    private static final Function<Double, Double> ASK_ADJSUTER = price -> price + PRICE_DIFFERENCE;
    private static final Function<Double, Double> BID_ADUSTER = price -> price - PRICE_DIFFERENCE;
    private static final Comparator<Price> COMPARE_PRICE = comparing(price -> price);

    private final LiveOrderService liveOrderService;

    public RequestForQuoteEngine(LiveOrderService liveOrderBoard) {
        this.liveOrderService = liveOrderBoard;
    }

    public Quote request(Amount amount, Currency currency) {
        List<Order> orders = liveOrderService.request(currency);

        if (orders.isEmpty()) {
            return NO_QUOTE;
        } else {
            Price highestBidPrice = orders.stream()
                                         .filter(order -> order.direction() == BUY)
                                         .map(Order::price)
                                         .max(COMPARE_PRICE)
                                         .orElse(NO_PRICE);

            Price lowestPrice = orders.stream()
                                    .filter(order -> order.direction() == SELL)
                                    .map(Order::price)
                                    .min(COMPARE_PRICE)
                                    .orElse(NO_PRICE);

            return new Quote(highestBidPrice.adjustUsing(BID_ADUSTER), lowestPrice.adjustUsing(ASK_ADJSUTER));
        }
    }
}
