package com.rfq;

public class Order {

    private final Direction direction;
    private final Price price;
    private final Currency currency;
    private final Amount amount;

    Order(final Direction direction, final Price price, final Currency currency, final Amount amount) {
        this.direction = direction;
        this.price = price;
        this.currency = currency;
        this.amount = amount;
    }

    Price price() {
        return price;
    }

    Direction direction() {
        return direction;
    }
}
