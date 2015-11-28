package com.rfq;

import static com.rfq.Price.NO_PRICE;

public class Quote {

    public static final Quote NO_QUOTE = new Quote(NO_PRICE, NO_PRICE) {
        @Override
        public String toString() {
            return "NO_QUOTE";
        }
    };

    private Price bidPrice;
    private Price askPrice;

    public Quote(Price bidPrice, Price askPrice) {
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    static Quote aQuote(Price bidPrice, Price askPrice) {
        if (bidPrice.equals(NO_PRICE) || askPrice.equals(NO_PRICE)) {
            return NO_QUOTE;
        } else {
            return new Quote(bidPrice, askPrice);
        }
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
