package com.rfq;

public class Order {

    private final Direction direction;
    private final Price price;
    private final Currency currency;
    private final Amount amount;

    public static final Order NO_ORDER = new Order(Direction.NO_DIRECTION, Price.NO_PRICE, null, new Amount(-1));

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

    Amount amount() {
        return amount;
    }

    Currency currency() {
        return currency;
    }

    @Override
    public String toString() {
        return "Order{" +
                "direction=" + direction +
                ", price=" + price +
                ", currency=" + currency +
                ", amount=" + amount +
                '}';
    }
}
