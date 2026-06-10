package com.customer.service;

import org.springframework.stereotype.Service;

import com.customer.dto.CustomerInformation;
import com.customer.entity.Customer;
import com.customer.exceptions.ApplicationException;
import com.customer.mapper.EntityDtoMapper;
import com.customer.repository.CustomerRepository;
import com.customer.repository.PortfolioItemRepository;

import reactor.core.publisher.Mono;

@Service
public class CustomerService {
	
	private final CustomerRepository customerRepository;
	private final PortfolioItemRepository portfolioItemRepository;
	
	public CustomerService(CustomerRepository customerRepository, PortfolioItemRepository portfolioItemRepository) {
		super();
		this.customerRepository = customerRepository;
		this.portfolioItemRepository = portfolioItemRepository;
	}
	
	
	private Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
		return this.customerRepository.findById(customerId)
				.switchIfEmpty(ApplicationException.customerNotFound(customerId))
				.flatMap(this::buildCustomerInformation);
	}
	
	private Mono<CustomerInformation> buildCustomerInformation(Customer customer){
		return this.portfolioItemRepository
				.findAllByCustomerId(customer.getId())
				.collectList()
				.map(list -> EntityDtoMapper.toCustomerInformation(customer, list));
				
	}

}
