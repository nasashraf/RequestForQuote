package com.rfq;

public enum Direction {

    SELL((compareResult, order1, order2) -> compareResult <= 0 ? order1 : order2),
    BUY((compareResult, order1, order2) -> compareResult >= 0 ? order1 : order2);

    private OrderComparator orderComparator;

    Direction(OrderComparator orderComparator) {
        this.orderComparator = orderComparator;
    }

    Order compare(Order order1, Order order2) {
        return orderComparator.compare(order1.price().compareTo(order2.price()), order1, order2);
    }

    private interface OrderComparator {
        Order compare(Integer compareResult, Order a, Order b);
    }
}
