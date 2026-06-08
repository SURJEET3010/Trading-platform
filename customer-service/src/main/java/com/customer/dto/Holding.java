package com.customer.dto;

import com.customer.domain.Ticker;

public record Holding(Ticker ticker, Integer quantity) {

}
