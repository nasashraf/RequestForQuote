package com.rfq;

public class Amount {

    private final Integer value;

    public Amount(final Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Amount)) return false;

        Amount amount = (Amount) o;

        return value.equals(amount.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
