package com.rfq;

import java.util.ArrayList;
import java.util.List;

public class LiveOrderServiceBuilder {

    private final List<Order> orders;

    private LiveOrderServiceBuilder() {
        orders = new ArrayList<>();
    }

    static LiveOrderServiceBuilder aLiveOrderService() {
        return new LiveOrderServiceBuilder();
    }

    LiveOrderServiceBuilder withOrder(final Direction direction, final Price price, final Currency currency, final Amount amount) {
        orders.add(new Order(direction, price, currency, amount));
        return this;
    }

    LiveOrderService build() {
        return new LiveOrderService() {
            @Override
            public List<Order> request(Currency currency) {
                return orders;
            }
        };
    }
}
