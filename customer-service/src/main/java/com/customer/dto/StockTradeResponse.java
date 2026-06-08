package com.customer.dto;

import com.customer.domain.Ticker;
import com.customer.domain.TradeAction;

public record StockTradeResponse(Integer customerId, 
					Ticker ticker, 
					Integer quantity, 
					Integer price,
					TradeAction action,
					Integer totalPrice,
					Integer balance) {

}
