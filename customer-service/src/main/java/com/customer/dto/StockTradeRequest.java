package com.customer.dto;

import com.customer.domain.Ticker;
import com.customer.domain.TradeAction;

public record StockTradeRequest(Ticker ticker, 
		Integer price, 
		Integer quantity, 
		TradeAction action) {
	
	public Integer totalPrice() {
		return quantity * price;
	}

}
