package com.customer.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.customer.domain.Ticker;
import com.customer.entity.PortfolioItem;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, Integer> {
	
	public Flux<PortfolioItem> findAllByCustomerId(Integer customerId);
	
	public Mono<PortfolioItem> findByCustomerIdAndTicker(Integer customerId, Ticker ticker);

}
