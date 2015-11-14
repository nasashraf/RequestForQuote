package com.rfq;

import java.util.List;

public interface LiveOrderService {

    List<Order> request(Currency currency);
}
