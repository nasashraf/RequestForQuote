package com.rfq;

import java.util.function.Function;

public class Price implements Comparable<Price> {
    public static final Price NO_PRICE = new Price(-1.0);

    private final Double value;

    public Price(final Double vaule) {
        this.value = vaule;
    }

    public Price adjustUsing(Function<Double, Double> func) {
        return new Price(func.apply(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;

        Price price = (Price) o;

        if (!value.equals(price.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "Price{" +
                "value=" + value +
                '}';
    }

    @Override
    public int compareTo(Price price) {
        return value.compareTo(price.value);
    }
}
