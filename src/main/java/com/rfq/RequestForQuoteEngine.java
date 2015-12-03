package com.rfq;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.rfq.Direction.BUY;
import static com.rfq.Direction.SELL;
import static com.rfq.Price.NO_PRICE;
import static com.rfq.Quote.aQuote;
import static java.util.stream.Collectors.*;

public class RequestForQuoteEngine {
    private static final List<Price> EMPTY_PRICE_LIST = Collections.<Price>emptyList();
    private static final Double PRICE_DIFFERENCE = 0.02;
    private static final Function<Double, Double> ASK_ADJUSTER = price -> price + PRICE_DIFFERENCE;
    private static final Function<Double, Double> BID_ADJUSTER = price -> price - PRICE_DIFFERENCE;

    private static final Comparator<Price> MIN_COMPARATOR = Price::compareTo;
    private static final Comparator<Price> MAX_COMPARATOR = MIN_COMPARATOR.reversed();
    private static final Function<List<Price>, Price> FIND_MIN_PRICE = findPriceUsing(MIN_COMPARATOR);
    private static final Function<List<Price>, Price> FIND_MAX_PRICE = findPriceUsing(MAX_COMPARATOR);

    private final LiveOrderService liveOrderService;

    public RequestForQuoteEngine(LiveOrderService liveOrderBoard) {
        this.liveOrderService = liveOrderBoard;
    }

    public Quote request(Amount amount, Currency currency) {
        List<Order> orders = liveOrderService.request(currency);

        Quote quote = orders.stream()
                            .filter(order -> order.amount().equals(amount))
                            .filter(order -> order.currency().equals(currency))
                            .collect(collectingAndThen(groupingBy(Order::direction, mapping(Order::price, toList())),
                                                       pricesPerDirection -> {return aQuote(FIND_MAX_PRICE.apply(get(BUY).pricesFrom(pricesPerDirection)).adjustUsing(BID_ADJUSTER),
                                                                                            FIND_MIN_PRICE.apply(get(SELL).pricesFrom(pricesPerDirection)).adjustUsing(ASK_ADJUSTER));}
                                                      )
                                    );

        return quote;
    }

    private PricesForDirection get(Direction direction) {
        return pricesPerDirection -> pricesPerDirection.getOrDefault(direction, EMPTY_PRICE_LIST);
    }

    private static Function<List<Price>, Price> findPriceUsing(Comparator<Price> comp) {
        return prices -> prices.stream()
                                .sorted(comp)
                                .findFirst()
                                .orElse(NO_PRICE);
    }

    @FunctionalInterface
    private interface PricesForDirection {
        List<Price> pricesFrom(Map<Direction, List<Price>> pricesPerDirection);
    }

}
