package com.customer.exceptions;

public class InsufficientBalanceException extends RuntimeException {
	
	private static final String MESSAGE = "Customer [id=%id] does not have enough funds for trade";

	public InsufficientBalanceException(Integer customerId) {
		super(MESSAGE.formatted(customerId));
	}
	
	

}
