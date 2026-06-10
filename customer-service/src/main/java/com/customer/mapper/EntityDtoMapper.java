package com.customer.mapper;

import java.util.List;

import com.customer.domain.Ticker;
import com.customer.domain.TradeAction;
import com.customer.dto.CustomerInformation;
import com.customer.dto.Holding;
import com.customer.dto.StockTradeRequest;
import com.customer.dto.StockTradeResponse;
import com.customer.entity.Customer;
import com.customer.entity.PortfolioItem;

public class EntityDtoMapper {
	
	public static CustomerInformation toCustomerInformation(Customer customer, List<PortfolioItem> items ) {
		var holdings = items
					.stream()
					.map(i -> new Holding(i.getTicker(), i.getQuantity()))
					.toList();
		return new CustomerInformation(
					customer.getId(),
					customer.getName(),
					customer.getBalance(),
					holdings
				);
	}
	
	public static PortfolioItem toPortFolioItem(Integer customerId, Ticker ticker) {
		PortfolioItem portfolioItem = new PortfolioItem();
		portfolioItem.setCustomerId(customerId);
		portfolioItem.setTicker(ticker);
		portfolioItem.setQuantity(0);
		
		return portfolioItem;
	}
	
	/*
	 * 
	 * Integer customerId, 
					Ticker ticker, 
					Integer quantity, 
					Integer price,
					TradeAction action,
					Integer totalPrice,
					Integer balance
	 * 
	 * */
	
	public static StockTradeResponse toStockTradeResponse(StockTradeRequest request, Integer customerid, Integer balance) {
		return new StockTradeResponse(
				customerid, 
				request.ticker(),
				request.quantity(),
				request.price(), 
				request.action(),
				request.totalPrice(),
				balance);
	}

}
