package com.rfq;

public class Quote {

    public static final Quote NO_QUOTE = new Quote(new Price(-1.0), new Price(-1.0));

    private Price bidPrice;
    private Price askPrice;

    public Quote(Price bidPrice, Price askPrice) {
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    public Price bid() {
        return bidPrice;
    }

    public Price ask() {
        return askPrice;
    }


    @Override
    public String toString() {
        return "Quote{" +
                "bidPrice=" + bidPrice +
                ", askPrice=" + askPrice +
                '}';
    }
}
