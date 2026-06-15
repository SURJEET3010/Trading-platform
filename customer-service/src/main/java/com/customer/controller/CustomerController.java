package com.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer.dto.CustomerInformation;
import com.customer.dto.StockTradeRequest;
import com.customer.dto.StockTradeResponse;
import com.customer.service.CustomerService;
import com.customer.service.TradeService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private TradeService tradeService;
	
	@GetMapping("/{customerId}")
	public Mono<CustomerInformation> getCustomer(@PathVariable Integer customerId){
		return this.customerService.getCustomerInformation(customerId);
	}

	@PostMapping("/{customerid}/trade")
	public Mono<StockTradeResponse> trade(@PathVariable Integer customerId, @RequestBody Mono<StockTradeRequest> mono){
		return mono.flatMap(req -> this.tradeService.trade(customerId, req));
	}
}
