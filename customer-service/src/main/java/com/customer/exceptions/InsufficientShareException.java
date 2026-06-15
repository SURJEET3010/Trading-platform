package com.customer.exceptions;

public class InsufficientShareException extends RuntimeException {
	
	private static final String MESSAGE = "Customer [id=%id] does not have enough shares for trade";

	public InsufficientShareException(Integer customerId) {
		super(MESSAGE.formatted(customerId));
	}
}
