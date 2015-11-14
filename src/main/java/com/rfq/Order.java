package com.rfq;

public class Order {

    private Direction direction;
    private Price price;
    private Currency currency;
    private Amount amount;

    public Order(Direction direction, Price price, Currency currency, Amount amount) {
        this.direction = direction;
        this.price = price;
        this.currency = currency;
        this.amount = amount;
    }


    public Price price() {
        return price;
    }
}
