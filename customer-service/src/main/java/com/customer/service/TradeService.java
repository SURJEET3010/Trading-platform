package com.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.customer.dto.StockTradeRequest;
import com.customer.dto.StockTradeResponse;
import com.customer.entity.Customer;
import com.customer.entity.PortfolioItem;
import com.customer.exceptions.ApplicationException;
import com.customer.mapper.EntityDtoMapper;
import com.customer.repository.CustomerRepository;
import com.customer.repository.PortfolioItemRepository;

import reactor.core.publisher.Mono;

@Service
public class TradeService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private PortfolioItemRepository portfolioItemRepository;
	

	public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest request){
		return switch (request.action()) {
		case BUY -> this.buyStock(customerId, request);
		case SELL -> this.sellStock(customerId, request);
		};
	}
	
	@Transactional
	private Mono<StockTradeResponse> buyStock(Integer customerId, StockTradeRequest request){
		Mono<Customer> customerMono = this.customerRepository.findById(customerId)
			.switchIfEmpty(ApplicationException.customerNotFound(customerId))
			.filter(c -> c.getBalance() >= request.totalPrice())
			.switchIfEmpty(ApplicationException.insufficientBalance(customerId));
		
		Mono<PortfolioItem> portFolioItemMono = this.portfolioItemRepository
			.findByCustomerIdAndTicker(customerId, request.ticker())
			.defaultIfEmpty(EntityDtoMapper.toPortFolioItem(customerId, request.ticker()));
	
		return customerMono.zipWhen(customer -> portFolioItemMono).flatMap(t -> this.executeBuy(t.getT1(), t.getT2(), request));
	}
	
	private Mono<StockTradeResponse> executeBuy(Customer customer, PortfolioItem portfolioItem, StockTradeRequest request){
		
		customer.setBalance(customer.getBalance() - request.totalPrice());
		portfolioItem.setQuantity(portfolioItem.getQuantity() + request.quantity());
		return this.saveAndBuildRespons(customer, request, portfolioItem);
	}
	
	private Mono<StockTradeResponse> sellStock(Integer customerId,StockTradeRequest request ){
		Mono<Customer> customerMono = this.customerRepository.findById(customerId)
															 .switchIfEmpty(ApplicationException.customerNotFound(customerId));
		
			Mono<PortfolioItem> portFolioItemMono = this.portfolioItemRepository
														.findByCustomerIdAndTicker(customerId, request.ticker())
														.filter(pi -> pi.getQuantity() >= request.quantity())
														.defaultIfEmpty(EntityDtoMapper.toPortFolioItem(customerId, request.ticker()));
		
			return customerMono.zipWhen(customer -> portFolioItemMono)
								.flatMap(t -> this.executeSell(t.getT1(), t.getT2(), request));
	}
	
	
	private Mono<StockTradeResponse> executeSell(Customer customer, PortfolioItem portfolioItem, StockTradeRequest request){
		
		customer.setBalance(customer.getBalance() + request.totalPrice());
		portfolioItem.setQuantity(portfolioItem.getQuantity() - request.quantity());
		return this.saveAndBuildRespons(customer, request, portfolioItem);

	}
	
	private Mono<StockTradeResponse> saveAndBuildRespons(Customer customer, StockTradeRequest request, PortfolioItem portfolioItem) {
		var response = EntityDtoMapper.toStockTradeResponse(request, customer.getId(), customer.getBalance());
		return Mono.zip(this.customerRepository.save(customer), this.portfolioItemRepository.save(portfolioItem))
		.thenReturn(response);
	}
	
	
	
}
